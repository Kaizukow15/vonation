import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Donation records used by both the User and the Admin dashboards.
 * Saved in the PostgreSQL "donations" table (see Database.java / schema.sql).
 * Donation ids are shown as D-0001, D-0002, ...
 */
public class DonationStore {

    public static class Donation {
        public final String id, donor, type, item, date, note, status;
        public final double amount; // peso amount for cash donations, 0 for in-kind

        Donation(String id, String donor, String type, String item, double amount,
                 String date, String note, String status) {
            this.id = id;
            this.donor = donor;
            this.type = type;
            this.item = item;
            this.amount = amount;
            this.date = date;
            this.note = note;
            this.status = status;
        }
    }

    private static final DateTimeFormatter DISPLAY = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
    private static final String COLS = "id, donor, donation_type, item, amount, donated_on, note, status";

    private DonationStore() {
    }

    public static List<Donation> all() {
        return query("SELECT " + COLS + " FROM donations ORDER BY id", null);
    }

    public static List<Donation> byDonor(String donor) {
        return query("SELECT " + COLS + " FROM donations WHERE lower(donor) = lower(?) ORDER BY id", donor);
    }

    public static Donation add(String donor, String type, String item, double amount,
                               String date, String note) {
        return add(donor, type, item, amount, date, note, null);
    }

    /** Same as above, but links the donation to the admin's donation (campaigns.id). */
    public static Donation add(String donor, String type, String item, double amount,
                               String date, String note, Integer campaignId) {
        String sql = "INSERT INTO donations (donor, donation_type, item, amount, donated_on, note, status, campaign_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, 'Pending', ?) RETURNING id";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, clean(donor));
            ps.setString(2, type);
            ps.setString(3, clean(item));
            ps.setDouble(4, amount);
            ps.setDate(5, Date.valueOf(parseDate(date)));
            ps.setString(6, clean(note));
            if (campaignId == null) ps.setNull(7, java.sql.Types.INTEGER);
            else ps.setInt(7, campaignId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return new Donation(formatId(rs.getInt(1)), clean(donor), type, clean(item), amount,
                        parseDate(date).format(DISPLAY), clean(note), "Pending");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not save the donation to the database: " + e.getMessage(), e);
        }
    }

    /**
     * For every donation, keyed by its id (D-0001 ...): {donor's full name, name of the campaign it was
     * given to, photo path of that campaign}. The name falls back to the donor text (the email) when
     * no account is found; the campaign name and photo are empty when it is not linked to a campaign.
     */
    public static java.util.Map<String, String[]> details() {
        java.util.Map<String, String[]> out = new java.util.HashMap<>();
        // 1) every donation + the donor's full name (kept apart so a problem in the 2nd query cannot hide it)
        String names = "SELECT d.id, COALESCE(NULLIF(u.full_name, ''), d.donor) FROM donations d "
                + "LEFT JOIN users u ON lower(u.email) = lower(d.donor)";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(names);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.put(formatId(rs.getInt(1)), new String[]{rs.getString(2), "", ""});
            }
        } catch (SQLException e) {
            System.err.println("DonationStore.details (names) failed: " + e.getMessage());
        }
        // 2) the campaign each donation was given to
        String camps = "SELECT d.id, COALESCE(c.name, ''), COALESCE(c.photo_path, '') FROM donations d "
                + "JOIN campaigns c ON c.id = d.campaign_id";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(camps);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String[] row = out.computeIfAbsent(formatId(rs.getInt(1)), k -> new String[]{"", "", ""});
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
            }
        } catch (SQLException e) {
            System.err.println("DonationStore.details (campaigns) failed: " + e.getMessage());
        }
        System.err.println("DonationStore.details: " + out.size() + " donation(s) read");
        return out;
    }

    /** Updates a donation status and saves the change for both dashboards. */
    public static boolean updateStatus(String id, String newStatus) {
        if (id == null || newStatus == null || newStatus.isBlank()) return false;
        int num;
        try {
            num = Integer.parseInt(id.replaceAll("\\D", ""));
        } catch (NumberFormatException ex) {
            return false;
        }
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("UPDATE donations SET status = ? WHERE id = ?")) {
            ps.setString(1, clean(newStatus));
            ps.setInt(2, num);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("DonationStore.updateStatus failed: " + e.getMessage());
            return false;
        }
    }

    private static List<Donation> query(String sql, String param) {
        List<Donation> out = new ArrayList<>();
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (param != null) ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Donation(formatId(rs.getInt(1)), rs.getString(2), rs.getString(3), rs.getString(4),
                            rs.getDouble(5), rs.getDate(6).toLocalDate().format(DISPLAY),
                            rs.getString(7), rs.getString(8)));
                }
            }
        } catch (SQLException e) {
            System.err.println("DonationStore: could not read donations: " + e.getMessage());
        }
        return out;
    }

    private static String formatId(int id) {
        return String.format("D-%04d", id);
    }

    private static LocalDate parseDate(String s) {
        try {
            return LocalDate.parse(s == null ? "" : s.trim(), DISPLAY);
        } catch (RuntimeException e) {
            return LocalDate.now();
        }
    }

    private static String clean(String s) {
        return s == null ? "" : s.replaceAll("[\\t\\r\\n]+", " ").trim();
    }
}