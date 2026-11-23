import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Events & programs (community service) created by the admin and joined by users
 * (PostgreSQL tables "events" and "event_registrations").
 *
 * The admin and the user dashboards both read the SAME "events" rows, so whatever the admin saves
 * (name, date, time, location, category, status, photo, initial joined count) is what the user sees.
 *
 * Admin rows: {name, "MMM d, yyyy / time", location, category, status, joined, photoPath, id, volunteersNeeded}
 * User rows:  {title, category, yyyy-MM-dd, location, "0" (unused), photoPath, category, time, organizer,
 *              about, req1, req2, req3, id, status, joined, volunteersNeeded}
 *
 * "joined" is the same number on both sides: the user registrations that were not rejected
 * (plus events.initial_joined, which is 0 for new events). "volunteersNeeded" (events.capacity) is the
 * number the admin asked for; 0 means no limit. A user cannot join once joined reaches volunteersNeeded.
 */
public final class EventStore {

    private static final DateTimeFormatter DISPLAY = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
    private static boolean loadedOk = false; // never delete rows unless the last read worked

    /** SQL for the joined number (needs the events table aliased as "e"). */
    private static final String JOINED_SQL =
            "(e.initial_joined + (SELECT count(*) FROM event_registrations r "
                    + "WHERE r.event_id = e.id AND r.status <> 'Rejected'))";

    private EventStore() {
    }

    // ---------- admin side ----------

    public static synchronized List<String[]> adminRows() {
        List<String[]> out = new ArrayList<>();
        String sql = "SELECT e.id, e.name, e.event_date, e.time_slot, e.location, e.category, e.status, e.photo_path, "
                + JOINED_SQL + ", e.capacity FROM events e ORDER BY e.id DESC";
        try (Connection c = Database.get(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                out.add(toAdminRow(rs.getInt(1), rs.getString(2), rs.getDate(3).toLocalDate(), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9),
                        rs.getInt(10)));
            }
            loadedOk = true;
        } catch (SQLException e) {
            System.err.println("EventStore: could not read events: " + e.getMessage());
        }
        return out;
    }

    /**
     * {event id -> joined number} for every event, or null when the database could not be read.
     * The admin screen uses it to keep the "N joined" on its cards equal to what the users see.
     */
    public static Map<String, String> joinedCountsOrNull() {
        Map<String, String> out = new HashMap<>();
        String sql = "SELECT e.id, " + JOINED_SQL + " FROM events e";
        try (Connection c = Database.get(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) out.put(String.valueOf(rs.getInt(1)), String.valueOf(rs.getInt(2)));
        } catch (SQLException e) {
            System.err.println("EventStore: could not read joined counts: " + e.getMessage());
            return null;
        }
        return out;
    }

    /**
     * Saves the admin's current lists: new rows (empty id) are inserted and get their id back,
     * existing rows are updated, and rows that are no longer in the lists are deleted.
     */
    public static synchronized void sync(List<String[]> active, List<String[]> completed) {
        List<String[]> all = new ArrayList<>(active);
        all.addAll(completed);
        List<String[]> assigned = new ArrayList<>(); // rows that got a new id in this call
        try (Connection c = Database.get()) {
            c.setAutoCommit(false);
            try {
                Set<Integer> keep = new HashSet<>();
                try (PreparedStatement ins = c.prepareStatement(
                        "INSERT INTO events (name, event_date, time_slot, location, category, status, photo_path, "
                                + "initial_joined, capacity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id");
                     PreparedStatement upd = c.prepareStatement(
                             "UPDATE events SET name = ?, event_date = ?, time_slot = ?, location = ?, "
                                     + "category = ?, status = ?, photo_path = ? WHERE id = ?")) {
                    for (String[] r : all) {
                        String idStr = r.length > 7 && r[7] != null ? r[7].trim() : "";
                        PreparedStatement ps = idStr.isEmpty() ? ins : upd;
                        ps.setString(1, r[0]);
                        ps.setDate(2, Date.valueOf(parseDate(r[1])));
                        ps.setString(3, timeSlot(r[1]));
                        ps.setString(4, r[2]);
                        ps.setString(5, r[3]);
                        ps.setString(6, r[4]);
                        ps.setString(7, r.length > 6 && r[6] != null ? r[6] : "");
                        if (idStr.isEmpty()) {
                            // a brand-new event: the joined number the admin typed is the starting count
                            ps.setInt(8, parseCount(r.length > 5 ? r[5] : "0"));
                            ps.setInt(9, parseCount(r.length > 8 ? r[8] : "0"));
                            try (ResultSet rs = ps.executeQuery()) {
                                rs.next();
                                int id = rs.getInt(1);
                                keep.add(id);
                                if (r.length > 7) {
                                    r[7] = String.valueOf(id);
                                    assigned.add(r);
                                }
                            }
                        } else {
                            int id = Integer.parseInt(idStr);
                            ps.setInt(8, id);
                            ps.executeUpdate();
                            keep.add(id);
                        }
                    }
                }
                if (loadedOk) {
                    List<Integer> gone = new ArrayList<>();
                    try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT id FROM events")) {
                        while (rs.next()) if (!keep.contains(rs.getInt(1))) gone.add(rs.getInt(1));
                    }
                    try (PreparedStatement del = c.prepareStatement("DELETE FROM events WHERE id = ?")) {
                        for (int id : gone) {
                            del.setInt(1, id);
                            del.executeUpdate();
                        }
                    }
                }
                c.commit();
            } catch (SQLException | RuntimeException e) {
                try {
                    c.rollback();
                } catch (SQLException ignored) {
                    // the connection is closed right after this anyway
                }
                throw e;
            }
        } catch (SQLException | RuntimeException e) {
            // the insert was rolled back, so the ids we handed out do not exist: forget them
            for (String[] r : assigned) r[7] = "";
            System.err.println("EventStore: could not save events: " + e.getMessage());
        }
    }

    // ---------- user side ----------

    /** Events the user can still join (not completed / cancelled). Same data the admin saved. */
    public static List<String[]> userRows() {
        List<String[]> rows = userRowsOrNull();
        return rows == null ? new ArrayList<>() : rows;
    }

    /** Same as userRows(), but null when the database could not be read (so a screen can keep what it shows). */
    public static List<String[]> userRowsOrNull() {
        List<String[]> out = new ArrayList<>();
        String sql = "SELECT e.id, e.name, e.event_date, e.time_slot, e.location, e.category, e.photo_path, "
                + "e.status, " + JOINED_SQL + ", e.capacity FROM events e "
                + "WHERE e.status NOT IN ('Completed', 'Cancelled') ORDER BY e.event_date, e.id";
        try (Connection c = Database.get(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                out.add(toUserRow(rs.getInt(1), rs.getString(2), rs.getDate(3).toLocalDate(), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9),
                        rs.getInt(10)));
            }
        } catch (SQLException e) {
            System.err.println("EventStore: could not read events: " + e.getMessage());
            return null;
        }
        return out;
    }

    public static final int JOIN_OK = 1, JOIN_ERROR = 0, JOIN_FULL = -1;

    /** Saves a user's registration for an event. Returns false if it could not be saved (or the event is full). */
    public static boolean join(String eventId, String userEmail, String fullName, String contact) {
        return tryJoin(eventId, userEmail, fullName, contact) == JOIN_OK;
    }

    /**
     * Saves a user's registration unless the event already has all the volunteers it needs.
     * The check and the insert are one statement, so two users cannot take the last slot together.
     * Returns JOIN_OK, JOIN_FULL or JOIN_ERROR.
     */
    public static int tryJoin(String eventId, String userEmail, String fullName, String contact) {
        String insert = "INSERT INTO event_registrations (event_id, user_email, full_name, contact) "
                + "SELECT ?::integer, ?::varchar, ?::varchar, ?::varchar WHERE "
                + "(SELECT e.capacity FROM events e WHERE e.id = ?::integer) = 0 OR "
                + "(SELECT e.initial_joined + (SELECT count(*) FROM event_registrations r "
                + "WHERE r.event_id = e.id AND r.status <> 'Rejected') FROM events e WHERE e.id = ?::integer) "
                + "< (SELECT e.capacity FROM events e WHERE e.id = ?::integer) "
                + "ON CONFLICT (event_id, user_email) DO NOTHING";
        try (Connection c = Database.get()) {
            int id = Integer.parseInt(eventId);
            String email = userEmail.toLowerCase(Locale.ROOT);
            try (PreparedStatement ps = c.prepareStatement(insert)) {
                ps.setInt(1, id);
                ps.setString(2, email);
                ps.setString(3, fullName);
                ps.setString(4, contact);
                ps.setInt(5, id);
                ps.setInt(6, id);
                ps.setInt(7, id);
                if (ps.executeUpdate() > 0) return JOIN_OK;
            }
            // nothing inserted: either this user already joined, the event is full, or the event is gone
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT 1 FROM event_registrations WHERE event_id = ? AND lower(user_email) = lower(?)")) {
                ps.setInt(1, id);
                ps.setString(2, email);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return JOIN_OK;
                }
            }
            try (PreparedStatement ps = c.prepareStatement("SELECT 1 FROM events WHERE id = ?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? JOIN_FULL : JOIN_ERROR;
                }
            }
        } catch (SQLException | NumberFormatException e) {
            System.err.println("EventStore: could not save registration: " + e.getMessage());
            return JOIN_ERROR;
        }
    }

    /** Ids of the events this user already joined (the same id that is in column 13 of a user row). */
    public static Set<String> joinedEventIds(String userEmail) {
        Set<String> ids = joinedEventIdsOrNull(userEmail);
        return ids == null ? new HashSet<>() : ids;
    }

    /** Same as joinedEventIds(), but null when the database could not be read. */
    public static Set<String> joinedEventIdsOrNull(String userEmail) {
        Set<String> out = new HashSet<>();
        String sql = "SELECT event_id FROM event_registrations WHERE lower(user_email) = lower(?)";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, userEmail);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(String.valueOf(rs.getInt(1)));
            }
        } catch (SQLException e) {
            System.err.println("EventStore: could not read registrations: " + e.getMessage());
            return null;
        }
        return out;
    }

    /** event id -> this user's registration status (Pending / Approved / Rejected); null if it could not be read. */
    public static java.util.Map<String, String> registrationStatusesOrNull(String userEmail) {
        java.util.Map<String, String> out = new java.util.HashMap<>();
        String sql = "SELECT event_id, status FROM event_registrations WHERE lower(user_email) = lower(?)";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, userEmail);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.put(String.valueOf(rs.getInt(1)), nz(rs.getString(2)));
            }
        } catch (SQLException e) {
            System.err.println("EventStore: could not read registrations: " + e.getMessage());
            return null;
        }
        return out;
    }

    /** {registration id, full name, email, contact, status, date} of everyone who joined this event (admin). */
    public static List<String[]> registrationsForEvent(String eventId) {
        List<String[]> out = new ArrayList<>();
        String sql = "SELECT id, full_name, user_email, contact, status, created_at::date FROM event_registrations "
                + "WHERE event_id = ? ORDER BY id";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(eventId));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new String[]{String.valueOf(rs.getInt(1)), nz(rs.getString(2)), nz(rs.getString(3)),
                            nz(rs.getString(4)), nz(rs.getString(5)), rs.getDate(6).toLocalDate().format(DISPLAY)});
                }
            }
        } catch (SQLException | NumberFormatException e) {
            System.err.println("EventStore: could not read the event's registrations: " + e.getMessage());
        }
        return out;
    }

    /**
     * {registration id, event name, full name, email, contact, date, event id} of every registration that is still
     * waiting for the admin (all events); null if the database could not be read.
     */
    public static List<String[]> pendingRegistrationsOrNull() {
        List<String[]> out = new ArrayList<>();
        String sql = "SELECT r.id, e.name, r.full_name, r.user_email, r.contact, r.created_at::date, e.id "
                + "FROM event_registrations r JOIN events e ON e.id = r.event_id "
                + "WHERE r.status = 'Pending' ORDER BY r.id";
        try (Connection c = Database.get(); java.sql.Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                out.add(new String[]{String.valueOf(rs.getInt(1)), nz(rs.getString(2)), nz(rs.getString(3)),
                        nz(rs.getString(4)), nz(rs.getString(5)), rs.getDate(6).toLocalDate().format(DISPLAY),
                        String.valueOf(rs.getInt(7))});
            }
        } catch (SQLException e) {
            System.err.println("EventStore: could not read pending registrations: " + e.getMessage());
            return null;
        }
        return out;
    }

    /** Admin approves or rejects one registration ("Approved", "Rejected" or "Pending"). */
    public static boolean setRegistrationStatus(String registrationId, String status) {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("UPDATE event_registrations SET status = ? WHERE id = ?")) {
            ps.setString(1, status);
            ps.setInt(2, Integer.parseInt(registrationId));
            return ps.executeUpdate() > 0;
        } catch (SQLException | NumberFormatException e) {
            System.err.println("EventStore: could not update the registration: " + e.getMessage());
            return false;
        }
    }

    /** {event name, registration date (MMM d, yyyy), status} for each event this user joined. */
    public static List<String[]> registrationsFor(String userEmail) {
        List<String[]> out = new ArrayList<>();
        String sql = "SELECT e.name, r.created_at::date, r.status FROM event_registrations r "
                + "JOIN events e ON e.id = r.event_id WHERE lower(r.user_email) = lower(?) ORDER BY r.id";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, userEmail);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new String[]{rs.getString(1), rs.getDate(2).toLocalDate().format(DISPLAY), rs.getString(3)});
                }
            }
        } catch (SQLException e) {
            System.err.println("EventStore: could not read registrations: " + e.getMessage());
        }
        return out;
    }

    // ---------- row mapping (kept separate from SQL so it can be tested without a database) ----------

    static String[] toAdminRow(int id, String name, LocalDate date, String time, String location, String category,
                               String status, String photo, int joined, int capacity) {
        String when = date.format(DISPLAY);
        String t = nz(time);
        return new String[]{nz(name), t.isEmpty() ? when : when + " / " + t, nz(location), nz(category), nz(status),
                String.valueOf(joined), resolvePhoto(photo), String.valueOf(id), String.valueOf(capacity)};
    }

    static String[] toUserRow(int id, String name, LocalDate date, String time, String location, String category,
                              String photo, String status, int joined, int capacity) {
        return new String[]{nz(name), nz(category), date.toString(), nz(location), "0", resolvePhoto(photo),
                nz(category), nz(time), "VoNation Admin", "", "", "", "", String.valueOf(id), nz(status),
                String.valueOf(joined), String.valueOf(capacity)};
    }

    // ---------- helpers ----------

    private static String nz(String s) {
        return s == null ? "" : s;
    }

    private static int parseCount(String s) {
        try {
            return Math.max(0, Integer.parseInt(s == null ? "0" : s.trim()));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * The photo path is saved as the admin's absolute path. If that file is not there (project folder moved,
     * or the user app runs from another folder), fall back to the copy in the "event_photos" folder.
     */
    static String resolvePhoto(String path) {
        if (path == null || path.isBlank()) return "";
        try {
            File f = new File(path);
            if (f.isFile()) return path;
            File alt = new File(new File(System.getProperty("user.dir"), "event_photos"), f.getName());
            if (alt.isFile()) return alt.getPath();
        } catch (RuntimeException ignored) {
            // keep the stored path
        }
        return path;
    }

    private static LocalDate parseDate(String dateAndTime) {
        String d = dateAndTime == null ? "" : dateAndTime;
        int slash = d.indexOf('/');
        if (slash > 0) d = d.substring(0, slash);
        try {
            return LocalDate.parse(d.trim(), DISPLAY);
        } catch (RuntimeException e) {
            return LocalDate.now();
        }
    }

    private static String timeSlot(String dateAndTime) {
        if (dateAndTime == null) return "";
        int slash = dateAndTime.indexOf('/');
        return slash >= 0 ? dateAndTime.substring(slash + 1).trim() : "";
    }
}