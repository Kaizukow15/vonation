import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * User accounts (register + sign in), saved in the PostgreSQL "users" table.
 *
 * Passwords are never stored as plain text: each one is salted and hashed with
 * PBKDF2-HMAC-SHA256 (built into Java, no extra library needed).
 */
public final class UserStore {

    public static final int MIN_PASSWORD_LENGTH = 8;

    /** The admin signs in with this username (the "email" column of the users table). */
    public static final String ADMIN_USERNAME = "admin";
    private static boolean adminChecked = false;

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    // Philippine mobile number: 09XXXXXXXXX or +639XXXXXXXXX
    private static final Pattern PHONE = Pattern.compile("^(09\\d{9}|\\+639\\d{9})$");
    private static final String ALGORITHM = "pbkdf2_sha256";
    private static final int ITERATIONS = 600_000;
    private static final int SALT_BYTES = 16;
    private static final int KEY_BITS = 256;

    /** A signed-in / registered account (never contains the password). */
    public static final class User {
        public final int id;
        public final String fullName, email, role, phone;
        public final LocalDate birthDate; // null for older accounts that have none

        User(int id, String fullName, String email, String role, String phone, LocalDate birthDate) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.role = role;
            this.phone = phone;
            this.birthDate = birthDate;
        }
    }

    /** Thrown by register() when the email already has an account. */
    public static class DuplicateEmailException extends Exception {
        private static final long serialVersionUID = 1L;

        public DuplicateEmailException() {
            super("That email is already registered.");
        }
    }

    private UserStore() {
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.length() <= 150 && EMAIL.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE.matcher(normalizePhone(phone)).matches();
    }

    /** Removes spaces/dashes/brackets and turns +63 into 0, so every number is saved the same way. */
    public static String normalizePhone(String phone) {
        String p = clean(phone).replaceAll("[\\s\\-()]", "");
        return p.startsWith("+63") ? "0" + p.substring(3) : p;
    }

    /** Creates a new account with the role "User". Callers should validate the input first. */
    public static User register(String fullName, String email, String phone, LocalDate birthDate, String password)
            throws IOException, DuplicateEmailException {
        String name = clean(fullName);
        String mail = normalize(email);
        String contact = normalizePhone(phone);
        String passwordHash = hash(password);
        String sql = "INSERT INTO users (full_name, email, password_hash, role, phone, birth_date) "
                + "VALUES (?, ?, ?, 'User', ?, ?) RETURNING id";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, mail);
            ps.setString(3, passwordHash);
            ps.setString(4, contact);
            if (birthDate == null) ps.setNull(5, Types.DATE);
            else ps.setDate(5, Date.valueOf(birthDate));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return new User(rs.getInt(1), name, mail, "User", contact, birthDate);
            }
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) throw new DuplicateEmailException(); // unique email
            throw new IOException("Database error: " + e.getMessage(), e);
        }
    }

    public static boolean isAdmin(User u) {
        return u != null && "Admin".equalsIgnoreCase(u.role);
    }

    /**
     * Password for the FIRST admin account only. Read from "admin.password" in db.properties (not in git).
     * If it is missing, a random password is generated and printed once in the console, so no default
     * password is ever written in the source code.
     */
    private static String initialAdminPassword() {
        try {
            java.nio.file.Path cfg = java.nio.file.Paths.get(System.getProperty("user.dir"), "db.properties");
            if (java.nio.file.Files.exists(cfg)) {
                java.util.Properties pr = new java.util.Properties();
                try (java.io.InputStream in = java.nio.file.Files.newInputStream(cfg)) {
                    pr.load(in);
                }
                String fromFile = pr.getProperty("admin.password", "").trim();
                if (fromFile.length() >= MIN_PASSWORD_LENGTH) return fromFile;
            }
        } catch (IOException ignored) {
        }
        byte[] raw = new byte[9];
        new SecureRandom().nextBytes(raw);
        String generated = Base64.getUrlEncoder().withoutPadding().encodeToString(raw);
        System.out.println("[voNation] First admin account created. Username: " + ADMIN_USERNAME
                + "  Password: " + generated + "  (shown once - change it after signing in)");
        return generated;
    }

    /**
     * Makes sure an admin account exists in the database. The first time, it is created with the username
     * "admin" and the password from "admin.password" in db.properties (or a random one printed once in the console),
     * saved hashed like every other password. If the table already has an
     * account with the role Admin, nothing is changed - so a changed admin password is never overwritten.
     */
    private static synchronized void ensureAdmin() throws SQLException {
        if (adminChecked) return;
        try (Connection c = Database.get()) {
            try (PreparedStatement ps = c.prepareStatement("SELECT 1 FROM users WHERE role = 'Admin' LIMIT 1");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    adminChecked = true;
                    return;
                }
            }
            String sql = "INSERT INTO users (full_name, email, password_hash, role, phone) "
                    + "VALUES ('Administrator', ?, ?, 'Admin', '') ON CONFLICT (email) DO NOTHING";
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, ADMIN_USERNAME);
                ps.setString(2, hash(initialAdminPassword()));
                ps.executeUpdate();
            }
            adminChecked = true;
        }
    }

    /** Returns the account if the email (or the admin username) and password are right, or null if they are not. */
    public static User authenticate(String email, String password) throws IOException {
        String sql = "SELECT id, full_name, email, password_hash, role, phone, birth_date FROM users WHERE email = ?";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ensureAdmin();
            ps.setString(1, normalize(email));
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                if (!verify(password, rs.getString(4))) return null;
                Date b = rs.getDate(7);
                return new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(5),
                        rs.getString(6), b == null ? null : b.toLocalDate());
            }
        } catch (SQLException e) {
            throw new IOException("Database error: " + e.getMessage(), e);
        }
    }

    /** {full name, email, created at (yyyy-MM-ddTHH:mm:ss)} for the profile page, or null if not found. */
    public static String[] lookup(String email) {
        String sql = "SELECT full_name, email, created_at FROM users WHERE email = ?";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, normalize(email));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new String[]{rs.getString(1), rs.getString(2),
                            rs.getTimestamp(3).toLocalDateTime().withNano(0).toString()};
                }
            }
        } catch (SQLException e) {
            System.err.println("UserStore: could not read account: " + e.getMessage());
        }
        return null;
    }

    /** {phone, address} saved for this account (empty strings when not set), or null if not found. */
    public static String[] profile(String email) {
        String sql = "SELECT phone, address FROM users WHERE email = ?";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, normalize(email));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new String[]{rs.getString(1) == null ? "" : rs.getString(1),
                            rs.getString(2) == null ? "" : rs.getString(2)};
                }
            }
        } catch (SQLException e) {
            System.err.println("UserStore: could not read profile: " + e.getMessage());
        }
        return null;
    }

    /** Saves the name, contact number and address of this account. */
    public static boolean updateProfile(String email, String fullName, String phone, String address) {
        String sql = "UPDATE users SET full_name = ?, phone = ?, address = ? WHERE email = ?";
        try (Connection c = Database.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, clean(fullName));
            ps.setString(2, clean(phone));
            ps.setString(3, clean(address));
            ps.setString(4, normalize(email));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("UserStore: could not save profile: " + e.getMessage());
            return false;
        }
    }

    /** Changes the password. Returns false if the current password is wrong; throws if the database fails. */
    public static boolean changePassword(String email, String current, String next) throws IOException {
        String select = "SELECT password_hash FROM users WHERE email = ?";
        String update = "UPDATE users SET password_hash = ? WHERE email = ?";
        try (Connection c = Database.get()) {
            try (PreparedStatement ps = c.prepareStatement(select)) {
                ps.setString(1, normalize(email));
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next() || !verify(current, rs.getString(1))) return false;
                }
            }
            try (PreparedStatement ps = c.prepareStatement(update)) {
                ps.setString(1, hash(next));
                ps.setString(2, normalize(email));
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new IOException("Database error: " + e.getMessage(), e);
        }
    }

    /** Keeps a value on one line / one column of the file. */
    private static String clean(String s) {
        return s == null ? "" : s.replaceAll("[\\t\\r\\n]+", " ").trim();
    }

    private static String normalize(String email) {
        return clean(email).toLowerCase(Locale.ROOT);
    }

    // ---------- password hashing ----------

    static String hash(String password) {
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        byte[] key = pbkdf2(password, salt, ITERATIONS);
        return ALGORITHM + "$" + ITERATIONS + "$"
                + Base64.getEncoder().encodeToString(salt) + "$"
                + Base64.getEncoder().encodeToString(key);
    }

    static boolean verify(String password, String stored) {
        if (stored == null) return false;
        String[] p = stored.split("\\$");
        if (p.length != 4 || !ALGORITHM.equals(p[0])) return false;
        try {
            byte[] expected = Base64.getDecoder().decode(p[3]);
            byte[] actual = pbkdf2(password, Base64.getDecoder().decode(p[2]), Integer.parseInt(p[1]));
            return MessageDigest.isEqual(expected, actual); // constant-time compare
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] pbkdf2(String password, byte[] salt, int iterations) {
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return f.generateSecret(new PBEKeySpec(password.toCharArray(), salt, iterations, KEY_BITS)).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Password hashing is not available", e);
        }
    }
}