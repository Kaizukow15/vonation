import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * One-way sync: local PostgreSQL (main) -> Neon (cloud copy).
 *
 * The app keeps reading and writing ONLY the local database (Database.get()).
 * This class runs in the background, only makes OUTBOUND connections to Neon,
 * and copies every row of each table to Neon (insert new, update changed, delete removed),
 * keeping the same ids so foreign keys still match.
 *
 * If Neon / the internet is down, it just logs a message and tries again on the next round.
 * Local data is never changed or deleted by this class.
 *
 * db.properties needs these extra lines (leave neon.url out to turn the sync off):
 *   neon.url=jdbc:postgresql://ep-xxxx.ap-southeast-1.aws.neon.tech/neondb?sslmode=require
 *   neon.user=neondb_owner
 *   neon.password=your_neon_password
 *   neon.sync.seconds=30
 */
public final class NeonSync {

    /** Parents first, children last (foreign keys). */
    private static final String[] TABLES = {
            "users", "campaigns", "events", "donations",
            "event_registrations", "volunteers", "announcements"
    };

    private static final Path CONFIG = Paths.get(System.getProperty("user.dir"), "db.properties");
    private static ScheduledExecutorService pool;
    private static volatile boolean running;

    private NeonSync() {
    }

    /** Call once when the app starts. Safe to call if Neon is not configured. */
    public static synchronized void start() {
        if (pool != null) return;
        Properties p = load();
        if (p == null || p.getProperty("neon.url", "").isBlank()) {
            System.out.println("[NeonSync] neon.url not set in db.properties - sync is OFF.");
            return;
        }
        int seconds = 30;
        try {
            seconds = Math.max(5, Integer.parseInt(p.getProperty("neon.sync.seconds", "30").trim()));
        } catch (NumberFormatException ignored) {
        }
        pool = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "neon-sync");
            t.setDaemon(true);
            return t;
        });
        pool.scheduleWithFixedDelay(NeonSync::syncNow, 3, seconds, TimeUnit.SECONDS);
        System.out.println("[NeonSync] started, every " + seconds + "s.");
    }

    /** Runs one full sync round. Never throws. */
    public static synchronized void syncNow() {
        if (running) return;
        running = true;
        Properties p = load();
        if (p == null) {
            running = false;
            return;
        }
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("[NeonSync] PostgreSQL JDBC driver not found.");
            running = false;
            return;
        }
        try (Connection local = Database.get();
             Connection neon = DriverManager.getConnection(
                     p.getProperty("neon.url"), p.getProperty("neon.user", ""), p.getProperty("neon.password", ""))) {
            neon.setAutoCommit(false);
            try {
                // 1) remove rows that no longer exist locally (children first)
                for (int i = TABLES.length - 1; i >= 0; i--) {
                    if (tableExists(local, TABLES[i]) && tableExists(neon, TABLES[i])) {
                        deleteMissing(local, neon, TABLES[i]);
                    }
                }
                // 2) insert / update everything else (parents first)
                int total = 0;
                for (String t : TABLES) {
                    if (tableExists(local, t) && tableExists(neon, t)) {
                        total += upsertAll(local, neon, t);
                        fixSequence(neon, t);
                    }
                }
                neon.commit();
                System.out.println("[NeonSync] OK - " + total + " rows checked.");
            } catch (SQLException e) {
                neon.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("[NeonSync] skipped (will retry): " + e.getMessage());
        } finally {
            running = false;
        }
    }

    private static int upsertAll(Connection local, Connection neon, String table) throws SQLException {
        int count = 0;
        try (Statement st = local.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM " + table + " ORDER BY id")) {
            ResultSetMetaData md = rs.getMetaData();
            int n = md.getColumnCount();
            StringBuilder cols = new StringBuilder(), marks = new StringBuilder(), sets = new StringBuilder();
            for (int i = 1; i <= n; i++) {
                String c = md.getColumnName(i);
                if (i > 1) {
                    cols.append(", ");
                    marks.append(", ");
                }
                cols.append(c);
                marks.append('?');
                if (!c.equalsIgnoreCase("id")) {
                    if (sets.length() > 0) sets.append(", ");
                    sets.append(c).append(" = EXCLUDED.").append(c);
                }
            }
            String sql = "INSERT INTO " + table + " (" + cols + ") OVERRIDING SYSTEM VALUE VALUES (" + marks + ") "
                    + "ON CONFLICT (id) DO UPDATE SET " + sets;
            try (PreparedStatement ps = neon.prepareStatement(sql)) {
                while (rs.next()) {
                    for (int i = 1; i <= n; i++) ps.setObject(i, rs.getObject(i));
                    ps.addBatch();
                    count++;
                }
                ps.executeBatch();
            }
        }
        return count;
    }

    private static void deleteMissing(Connection local, Connection neon, String table) throws SQLException {
        Set<Integer> localIds = new HashSet<>();
        try (Statement st = local.createStatement(); ResultSet rs = st.executeQuery("SELECT id FROM " + table)) {
            while (rs.next()) localIds.add(rs.getInt(1));
        }
        List<Integer> gone = new ArrayList<>();
        try (Statement st = neon.createStatement(); ResultSet rs = st.executeQuery("SELECT id FROM " + table)) {
            while (rs.next()) if (!localIds.contains(rs.getInt(1))) gone.add(rs.getInt(1));
        }
        if (gone.isEmpty()) return;
        try (PreparedStatement ps = neon.prepareStatement("DELETE FROM " + table + " WHERE id = ?")) {
            for (int id : gone) {
                ps.setInt(1, id);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    /** Keeps Neon's id counter ahead of the copied ids (needed if you ever switch the app to Neon). */
    private static void fixSequence(Connection neon, String table) {
        String sql = "SELECT setval(pg_get_serial_sequence('" + table + "', 'id'), "
                + "COALESCE((SELECT MAX(id) FROM " + table + "), 1), (SELECT MAX(id) FROM " + table + ") IS NOT NULL)";
        try (Statement st = neon.createStatement()) {
            st.execute(sql);
        } catch (SQLException ignored) {
            // table has no id sequence - nothing to fix
        }
    }

    private static boolean tableExists(Connection c, String table) {
        try (PreparedStatement ps = c.prepareStatement("SELECT to_regclass(?) IS NOT NULL")) {
            ps.setString(1, "public." + table);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getBoolean(1);
            }
        } catch (SQLException e) {
            return false;
        }
    }

    private static Properties load() {
        if (!Files.exists(CONFIG)) return null;
        Properties p = new Properties();
        try (InputStream in = Files.newInputStream(CONFIG)) {
            p.load(in);
            return p;
        } catch (IOException e) {
            return null;
        }
    }

    /** Test: right-click > Run 'NeonSync.main()' to do one sync round. */
    public static void main(String[] args) {
        syncNow();
    }
}
