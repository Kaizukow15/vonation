import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Volunteer registrations (PostgreSQL table "volunteers").
 * A user submits a registration (status Pending); the admin approves (Active) or rejects it (Rejected).
 * Admin rows: {"V-001", name, contact, role, status, applied date (MMM d, yyyy)}
 */
public final class VolunteerStore {

    private static final DateTimeFormatter DISPLAY = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);

    private VolunteerStore() {
    }

    public static boolean add(String userEmail, String fullName, String contact, String address,
                              String role, String availability, String skills, String notes) {
        String sql = "INSERT INTO volunteers (user_email, full_name, contact, address, role, availability, skills, notes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, userEmail.toLowerCase(Locale.ROOT));
            ps.setString(2, fullName);
            ps.setString(3, contact);
            ps.setString(4, address == null ? "" : address);
            ps.setString(5, role == null ? "" : role);
            ps.setString(6, availability == null ? "" : availability);
            ps.setString(7, skills == null ? "" : skills);
            ps.setString(8, notes == null ? "" : notes);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("VolunteerStore: could not save registration: " + e.getMessage());
            return false;
        }
    }

    /** True if this user already has a registration that was not rejected. */
    public static boolean isRegistered(String userEmail) {
        String sql = "SELECT 1 FROM volunteers WHERE lower(user_email) = lower(?) AND status <> 'Rejected' LIMIT 1";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, userEmail);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("VolunteerStore: could not read registration: " + e.getMessage());
            return false;
        }
    }

    /** True only when the admin has approved this user as a volunteer (status Active). */
    public static boolean isApproved(String userEmail) {
        String sql = "SELECT 1 FROM volunteers WHERE lower(user_email) = lower(?) AND status = 'Active' LIMIT 1";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, userEmail);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("VolunteerStore: could not read approval: " + e.getMessage());
            return false;
        }
    }

    /**
     * {full name, contact number} exactly as the user typed them in the volunteer registration
     * (their latest registration that was not rejected), or null if they have not registered.
     */
    public static String[] registeredDetails(String userEmail) {
        String sql = "SELECT full_name, contact FROM volunteers "
                + "WHERE lower(user_email) = lower(?) AND status <> 'Rejected' ORDER BY id DESC LIMIT 1";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, userEmail);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString(1);
                    String contact = rs.getString(2);
                    return new String[]{name == null ? "" : name, contact == null ? "" : contact};
                }
            }
        } catch (SQLException e) {
            System.err.println("VolunteerStore: could not read registration details: " + e.getMessage());
        }
        return null;
    }

    /** {applied date (MMM d, yyyy), status} of this user's latest registration, or null if none. */
    public static String[] latestFor(String userEmail) {
        String sql = "SELECT created_at::date, status FROM volunteers WHERE lower(user_email) = lower(?) "
                + "ORDER BY id DESC LIMIT 1";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, userEmail);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new String[]{rs.getDate(1).toLocalDate().format(DISPLAY), rs.getString(2)};
            }
        } catch (SQLException e) {
            System.err.println("VolunteerStore: could not read registration: " + e.getMessage());
        }
        return null;
    }

    /** Status of this user's latest registration (Pending / Active / Inactive / Rejected), or null if none. */
    public static String statusOf(String userEmail) {
        String sql = "SELECT status FROM volunteers WHERE lower(user_email) = lower(?) ORDER BY id DESC LIMIT 1";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, userEmail);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString(1);
            }
        } catch (SQLException e) {
            System.err.println("VolunteerStore: could not read status: " + e.getMessage());
        }
        return null;
    }

    /**
     * The volunteer switches themselves between Active and Inactive. Only a registration the admin already
     * approved (Active or Inactive) can change; a Pending or Rejected one is left alone.
     */
    public static boolean setOwnStatus(String userEmail, String status) {
        if (!"Active".equals(status) && !"Inactive".equals(status)) return false;
        String sql = "UPDATE volunteers SET status = ? WHERE status IN ('Active', 'Inactive') AND id = "
                + "(SELECT id FROM volunteers WHERE lower(user_email) = lower(?) ORDER BY id DESC LIMIT 1)";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, userEmail);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("VolunteerStore: could not update own status: " + e.getMessage());
            return false;
        }
    }

    /** Admin rows for the given statuses, newest first. */
    public static List<String[]> rows(String... statuses) {
        List<String[]> out = new ArrayList<>();
        StringBuilder in = new StringBuilder();
        for (int i = 0; i < statuses.length; i++) in.append(i == 0 ? "?" : ", ?");
        String sql = "SELECT id, full_name, contact, role, status, created_at::date FROM volunteers "
                + "WHERE status IN (" + in + ") ORDER BY id DESC";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < statuses.length; i++) ps.setString(i + 1, statuses[i]);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new String[]{String.format("V-%03d", rs.getInt(1)), rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getString(5), rs.getDate(6).toLocalDate().format(DISPLAY)});
                }
            }
        } catch (SQLException e) {
            System.err.println("VolunteerStore: could not read volunteers: " + e.getMessage());
        }
        return out;
    }

    /** Admin decision: status becomes Active, Inactive or Rejected. adminId looks like "V-004". */
    public static boolean setStatus(String adminId, String status) {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("UPDATE volunteers SET status = ? WHERE id = ?")) {
            ps.setString(1, status);
            ps.setInt(2, Integer.parseInt(adminId.replaceAll("\\D", "")));
            return ps.executeUpdate() > 0;
        } catch (SQLException | NumberFormatException e) {
            System.err.println("VolunteerStore: could not update status: " + e.getMessage());
            return false;
        }
    }
}