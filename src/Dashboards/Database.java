import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Connection helper for PostgreSQL.
 *
 * Settings are read from "db.properties" in the folder the app runs from
 * (the project root when you press Run in IntelliJ). Copy "db.properties.example"
 * to "db.properties" and fill in your own values. db.properties is kept out of git.
 *
 * The tables are NOT created here. Run schema.sql once in the "vonation" database
 * (pgAdmin > Query Tool > open schema.sql > Execute). Every new table or column goes into schema.sql.
 *
 * Run this class's main() once to test the connection.
 */
public final class Database {

    private static final Path CONFIG = Paths.get(System.getProperty("user.dir"), "db.properties");

    private Database() {
    }

    /** Opens a new connection. Always use try-with-resources so it gets closed. */
    public static synchronized Connection get() throws SQLException {
        Properties p = loadConfig();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC driver not found. Add postgresql-42.x.jar to the "
                    + "project libraries (File > Project Structure > Modules > Dependencies).", e);
        }
        return DriverManager.getConnection(
                p.getProperty("db.url", "jdbc:postgresql://localhost:5432/vonation"),
                p.getProperty("db.user", "postgres"),
                p.getProperty("db.password", ""));
    }

    private static Properties loadConfig() throws SQLException {
        if (!Files.exists(CONFIG)) {
            throw new SQLException("db.properties not found at " + CONFIG
                    + " (copy db.properties.example to db.properties and fill it in)");
        }
        Properties p = new Properties();
        try (InputStream in = Files.newInputStream(CONFIG)) {
            p.load(in);
        } catch (IOException e) {
            throw new SQLException("Could not read " + CONFIG + ": " + e.getMessage(), e);
        }
        return p;
    }

    /** Quick connection test: right-click > Run 'Database.main()'. */
    public static void main(String[] args) {
        try (Connection c = get()) {
            System.out.println("Connected OK: " + c.getMetaData().getURL());
        } catch (SQLException e) {
            System.err.println("Connection FAILED: " + e.getMessage());
        }
    }
}