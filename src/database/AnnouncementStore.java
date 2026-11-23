import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Community announcements that the admin posts and the users read (PostgreSQL table "announcements").
 * They show on the user's Home page (Community Announcements card and the sliding banner).
 */
public final class AnnouncementStore {

    public static class Announcement {
        public final int id;
        public final String title, body, tag, photoPath, date;

        Announcement(int id, String title, String body, String tag, String photoPath, String date) {
            this.id = id;
            this.title = title;
            this.body = body;
            this.tag = tag;
            this.photoPath = photoPath;
            this.date = date;
        }
    }

    private static final DateTimeFormatter DISPLAY = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);

    private AnnouncementStore() {
    }

    /** Newest first. Empty when there are none (or the database could not be read). */
    public static List<Announcement> all() {
        List<Announcement> out = new ArrayList<>();
        String sql = "SELECT id, title, body, tag, photo_path, created_at::date FROM announcements ORDER BY id DESC";
        try (Connection c = Database.get(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                out.add(new Announcement(rs.getInt(1), nz(rs.getString(2)), nz(rs.getString(3)), nz(rs.getString(4)),
                        resolvePhoto(rs.getString(5)), rs.getDate(6).toLocalDate().format(DISPLAY)));
            }
        } catch (SQLException e) {
            System.err.println("AnnouncementStore: could not read announcements: " + e.getMessage());
        }
        return out;
    }

    public static boolean add(String title, String body, String tag, String photoPath) {
        String sql = "INSERT INTO announcements (title, body, tag, photo_path) VALUES (?, ?, ?, ?)";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nz(title).trim());
            ps.setString(2, nz(body).trim());
            ps.setString(3, nz(tag).trim().isEmpty() ? "Announcement" : tag.trim());
            ps.setString(4, nz(photoPath));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("AnnouncementStore: could not save announcement: " + e.getMessage());
            return false;
        }
    }

    public static boolean delete(int id) {
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement("DELETE FROM announcements WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("AnnouncementStore: could not delete announcement: " + e.getMessage());
            return false;
        }
    }

    private static String nz(String s) {
        return s == null ? "" : s;
    }

    /** The saved path is the admin's full path; if that file is not there, try the copy in "announcement_photos". */
    private static String resolvePhoto(String path) {
        if (path == null || path.isBlank()) return "";
        try {
            File f = new File(path);
            if (f.isFile()) return path;
            File alt = new File(new File(System.getProperty("user.dir"), "announcement_photos"), f.getName());
            if (alt.isFile()) return alt.getPath();
        } catch (RuntimeException ignored) {
            // keep the stored path
        }
        return path;
    }
}
