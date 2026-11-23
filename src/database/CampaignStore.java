import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Donations (campaigns) that the ADMIN sets up and the USER donates to.
 * Saved in the PostgreSQL "campaigns" table (see Database.java / schema.sql).
 *
 * The admin sets: name, description, photo, goal amount, GCash account name + number,
 * and the walk-in drop-off location, days and opening / closing time.
 * The user's Cash Donation page shows exactly these values.
 */
public final class CampaignStore {

    public static final String TYPE_CASH = "Cash";
    public static final String TYPE_INKIND = "In-Kind";

    /** Every category, in the order the filter chips show them (after "All"). */
    public static final String[] CATEGORIES = {"Cash", "Education", "Relief Goods", "Clothes", "General Goods", "Others"};
    /** The categories an In-Kind donation can be put in (Cash donations are always "Cash"). */
    public static final String[] INKIND_CATEGORIES = {"Education", "Relief Goods", "Clothes", "General Goods", "Others"};

    /**
     * The category as chosen by the admin. Cash can be in any category (default "Cash"); In-Kind cannot be
     * "Cash" (default "Others"). Older records saved as "Relief Funds" are now "Relief Goods".
     */
    public static String normalizeCategory(String type, String category) {
        String cat = category == null ? "" : category.trim();
        if ("Relief Funds".equalsIgnoreCase(cat)) cat = "Relief Goods";
        boolean inKind = TYPE_INKIND.equalsIgnoreCase(type);
        for (String c : inKind ? INKIND_CATEGORIES : CATEGORIES) {
            if (c.equalsIgnoreCase(cat)) return c;
        }
        return inKind ? "Others" : "Cash";
    }

    public static class Campaign {
        public final int id;
        public final String name, description, photoPath;
        public final String gcashName, gcashNumber;
        public final String walkinLocation, walkinDays, walkinFrom, walkinTo;
        public final String status;
        public final double goal, raised;
        /** "Cash" or "In-Kind" (chosen by the admin). */
        public final String type;
        /** In-Kind only: the items the admin is asking for, separated by commas. */
        public final String itemsNeeded;
        /** Cash, Education, Relief Goods, Clothes, General Goods or Others (chosen by the admin). */
        public final String category;

        Campaign(int id, String name, String description, double goal, String photoPath,
                 String gcashName, String gcashNumber, String walkinLocation, String walkinDays,
                 String walkinFrom, String walkinTo, String status, double raised,
                 String type, String itemsNeeded, String category) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.goal = goal;
            this.photoPath = photoPath;
            this.gcashName = gcashName;
            this.gcashNumber = gcashNumber;
            this.walkinLocation = walkinLocation;
            this.walkinDays = walkinDays;
            this.walkinFrom = walkinFrom;
            this.walkinTo = walkinTo;
            this.status = status;
            this.raised = raised;
            this.type = (type == null || type.isBlank()) ? TYPE_CASH : type;
            this.itemsNeeded = itemsNeeded == null ? "" : itemsNeeded;
            this.category = normalizeCategory(this.type, category);
        }

        public boolean isCash() {
            return TYPE_CASH.equalsIgnoreCase(type);
        }

        public boolean isInKind() {
            return TYPE_INKIND.equalsIgnoreCase(type);
        }

        /** The items the admin asked for, one per element (empty for cash donations). */
        public List<String> itemList() {
            List<String> out = new ArrayList<>();
            for (String part : itemsNeeded.split(",")) {
                if (!part.trim().isEmpty()) out.add(part.trim());
            }
            return out;
        }

        public boolean isActive() {
            return "Active".equalsIgnoreCase(status);
        }

        /** "Mon-Sat, 8:00 AM - 5:00 PM" */
        public String walkinSchedule() {
            return walkinDays + ", " + walkinFrom + " \u2013 " + walkinTo;
        }

        public String goalText() {
            return peso(goal);
        }

        public String raisedText() {
            return peso(raised);
        }

        /** True when the admin set a target: a peso goal for Cash, a number of items for In-Kind. */
        public boolean hasGoal() {
            return goal > 0;
        }

        /**
         * Cash: "Raised \u20B11,200 of \u20B15,000".
         * In-Kind: "Received 12 of 100 items" (the quantities users donated, once the admin has processed them).
         */
        public String progressText() {
            if (isInKind()) {
                long target = Math.round(goal);
                return "Received " + whole(raised) + " of " + whole(goal) + (target == 1 ? " item" : " items");
            }
            return "Raised " + raisedText() + " of " + goalText();
        }

        /** 0 - 100 */
        public int percent() {
            if (goal <= 0) return 0;
            return (int) Math.max(0, Math.min(100, Math.round(raised * 100.0 / goal)));
        }
    }

    private static final String COLS =
            "c.id, c.name, c.description, c.goal_amount, c.photo_path, c.gcash_name, c.gcash_number, "
                    + "c.walkin_location, c.walkin_days, c.walkin_from, c.walkin_to, c.status, "
                    + "COALESCE((SELECT CASE WHEN c.campaign_type = 'In-Kind' "
                    + "THEN SUM(COALESCE(CAST(substring(d.item from '^[[:space:]]*([0-9]{1,9})([^0-9]|$)') AS INTEGER), 0)) "
                    + "ELSE SUM(d.amount) END "
                    + "FROM donations d WHERE d.campaign_id = c.id "
                    + "AND d.status NOT IN ('Pending', 'Cancelled', 'Canceled')), 0), "
                    + "c.campaign_type, c.items_needed, c.category";

    private CampaignStore() {
    }

    /** Every donation the admin made (newest first). */
    public static List<Campaign> all() {
        return query("SELECT " + COLS + " FROM campaigns c ORDER BY c.id DESC", null);
    }

    /** Only the donations that are open for users (newest first). */
    public static List<Campaign> active() {
        return query("SELECT " + COLS + " FROM campaigns c WHERE c.status = 'Active' ORDER BY c.id DESC", null);
    }

    /** Open donations of one type ("Cash" or "In-Kind"), newest first. */
    public static List<Campaign> activeOfType(String type) {
        List<Campaign> out = new ArrayList<>();
        for (Campaign c : active()) {
            if (type.equalsIgnoreCase(c.type)) out.add(c);
        }
        return out;
    }

    public static Campaign get(int id) {
        List<Campaign> l = query("SELECT " + COLS + " FROM campaigns c WHERE c.id = ?", id);
        return l.isEmpty() ? null : l.get(0);
    }

    /** Inserts a new donation (id = 0) or updates an existing one. Returns its id. */
    public static int save(int id, String type, String name, String description, double goal, String photoPath,
                           String gcashName, String gcashNumber, String walkinLocation, String walkinDays,
                           String walkinFrom, String walkinTo, String itemsNeeded, String category) {
        try (Connection c = Database.get()) {
            if (id <= 0) {
                String sql = "INSERT INTO campaigns (name, description, goal_amount, photo_path, gcash_name, "
                        + "gcash_number, walkin_location, walkin_days, walkin_from, walkin_to, campaign_type, "
                        + "items_needed, category, status) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Active') RETURNING id";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    fill(ps, type, name, description, goal, photoPath, gcashName, gcashNumber,
                            walkinLocation, walkinDays, walkinFrom, walkinTo, itemsNeeded, category);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        return rs.getInt(1);
                    }
                }
            }
            String sql = "UPDATE campaigns SET name = ?, description = ?, goal_amount = ?, photo_path = ?, "
                    + "gcash_name = ?, gcash_number = ?, walkin_location = ?, walkin_days = ?, "
                    + "walkin_from = ?, walkin_to = ?, campaign_type = ?, items_needed = ?, category = ? WHERE id = ?";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                fill(ps, type, name, description, goal, photoPath, gcashName, gcashNumber,
                        walkinLocation, walkinDays, walkinFrom, walkinTo, itemsNeeded, category);
                ps.setInt(14, id);
                ps.executeUpdate();
                return id;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not save the donation to the database: " + e.getMessage(), e);
        }
    }

    public static boolean setStatus(int id, String status) {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("UPDATE campaigns SET status = ? WHERE id = ?")) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("CampaignStore.setStatus failed: " + e.getMessage());
            return false;
        }
    }

    /** Deletes the donation. Money already donated to it stays in the donations table. */
    public static boolean delete(int id) {
        try (Connection c = Database.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM campaigns WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("CampaignStore.delete failed: " + e.getMessage());
            return false;
        }
    }

    private static void fill(PreparedStatement ps, String type, String name, String description, double goal,
                             String photoPath, String gcashName, String gcashNumber, String walkinLocation,
                             String walkinDays, String walkinFrom, String walkinTo, String itemsNeeded,
                             String category) throws SQLException {
        ps.setString(1, clean(name));
        ps.setString(2, description == null ? "" : description.trim());
        ps.setDouble(3, goal);
        ps.setString(4, photoPath == null ? "" : photoPath);
        ps.setString(5, clean(gcashName));
        ps.setString(6, clean(gcashNumber));
        ps.setString(7, clean(walkinLocation));
        ps.setString(8, clean(walkinDays));
        ps.setString(9, clean(walkinFrom));
        ps.setString(10, clean(walkinTo));
        ps.setString(11, TYPE_INKIND.equalsIgnoreCase(type) ? TYPE_INKIND : TYPE_CASH);
        ps.setString(12, clean(itemsNeeded));
        ps.setString(13, normalizeCategory(type, category));
    }

    private static List<Campaign> query(String sql, Integer id) {
        List<Campaign> out = new ArrayList<>();
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (id != null) ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Campaign(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4),
                            rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
                            rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12),
                            rs.getDouble(13), rs.getString(14), rs.getString(15), rs.getString(16)));
                }
            }
        } catch (SQLException e) {
            System.err.println("CampaignStore: could not read donations: " + e.getMessage());
        }
        return out;
    }

    private static String whole(double v) {
        return String.format(Locale.ENGLISH, "%,d", Math.round(v));
    }

    private static String peso(double v) {
        return v == Math.rint(v) ? String.format(Locale.ENGLISH, "\u20B1%,.0f", v)
                : String.format(Locale.ENGLISH, "\u20B1%,.2f", v);
    }

    private static String clean(String s) {
        return s == null ? "" : s.replaceAll("[\\t\\r\\n]+", " ").trim();
    }
}