import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminDashboardFX {

    // ---------- Colors (same palette as the user dashboard and login screen) ----------
    private static final String NAVY = "#1b2a4a";
    private static final String NAVY_DEEP = "#121d35";
    private static final String BLUE = "#2563eb";
    private static final String BLUE_HOVER = "#1d4ed8";
    private static final String BLUE_LIGHT = "#dbe7ff";
    private static final String GREEN = "#16a34a";
    private static final String GREEN_LIGHT = "#dcfce7";
    private static final String ORANGE = "#ea580c";
    private static final String ORANGE_LIGHT = "#ffedd5";
    private static final String PURPLE = "#7c3aed";
    private static final String PURPLE_LIGHT = "#ede9fe";
    private static final String RED = "#dc2626";
    private static final String RED_LIGHT = "#fee2e2";
    private static final String YELLOW = "#ca8a04";
    private static final String YELLOW_LIGHT = "#fef9c3";
    private static final String TEXT_DARK = NAVY_DEEP;
    private static final String TEXT_MUTED = "#5b6b86";

    // Glass card look (translucent white + soft white border)
    private static final String GLASS =
            "-fx-background-color: rgba(255,255,255,0.62);" +
                    "-fx-background-radius: 20;" +
                    "-fx-border-color: rgba(255,255,255,0.9);" +
                    "-fx-border-radius: 20;" +
                    "-fx-border-width: 1.5;";

    // ---------- Icons (Material Design, 24x24 paths) ----------
    private static final String ICON_CALENDAR = "M17 12h-5v5h5v-5zM16 1v2H8V1H6v2H5c-1.11 0-1.99.9-1.99 2L3 19c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2h-1V1h-2zm3 18H5V8h14v11z";
    private static final String ICON_CHECK = "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z";
    private static final String ICON_CANCEL = "M12 2C6.47 2 2 6.47 2 12s4.47 10 10 10 10-4.47 10-10S17.53 2 12 2zm5 13.59L15.59 17 12 13.41 8.41 17 7 15.59 10.59 12 7 8.41 8.41 7 12 10.59 15.59 7 17 8.41 13.41 12 17 15.59z";
    private static final String ICON_MONEY = "M11.8 10.9c-2.27-.59-3-1.2-3-2.15 0-1.09 1.01-1.85 2.7-1.85 1.78 0 2.44.85 2.5 2.1h2.21c-.07-1.72-1.12-3.3-3.21-3.81V3h-3v2.16c-1.94.42-3.5 1.68-3.5 3.61 0 2.31 1.91 3.46 4.7 4.13 2.5.6 3 1.48 3 2.41 0 .69-.49 1.79-2.7 1.79-2.06 0-2.87-.92-2.98-2.1h-2.2c.12 2.19 1.76 3.42 3.68 3.83V21h3v-2.15c1.95-.37 3.5-1.5 3.5-3.55 0-2.84-2.43-3.81-4.7-4.4z";
    private static final String ICON_PEOPLE = "M16 11c1.66 0 2.99-1.34 2.99-3S17.66 5 16 5c-1.66 0-3 1.34-3 3s1.34 3 3 3zm-8 0c1.66 0 2.99-1.34 2.99-3S9.66 5 8 5C6.34 5 5 6.34 5 8s1.34 3 3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z";
    private static final String ICON_HEART = "M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z";
    private static final String ICON_CLOCK = "M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm.5-13H11v6l5.25 3.15.75-1.23-4.5-2.67z";
    private static final String ICON_BELL = "M12 22c1.1 0 2-.9 2-2h-4c0 1.1.89 2 2 2zm6-6v-5c0-3.07-1.64-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.63 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2z";
    private static final String ICON_MEDICAL = "M19 3H5c-1.1 0-1.99.9-1.99 2L3 19c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-1 11h-4v4h-4v-4H6v-4h4V6h4v4h4v4z";
    private static final String ICON_SETTINGS = "M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58c.18-.14.23-.41.12-.61l-1.92-3.32c-.12-.22-.37-.29-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54c-.04-.24-.24-.41-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62-.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.09.63-.09.94s.02.64.07.94l-2.03 1.58c-.18.14-.23.41-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z";
    private static final String ICON_PERSON = "M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z";
    private static final String ICON_DASHBOARD = "M3 13h8V3H3v10zm0 8h8v-6H3v6zm10 0h8V11h-8v10zm0-18v6h8V3h-8z";
    private static final String ICON_BOX = "M20 2H4c-1 0-2 .9-2 2v3.01c0 .72.43 1.34 1 1.69V20c0 1.1 1.1 2 2 2h14c.9 0 2-.9 2-2V8.7c.57-.35 1-.97 1-1.69V4c0-1.1-1-2-2-2zm-5 12H9v-2h6v2zm5-7H4V4l16-.02V7z";
    private static final String ICON_BUSINESS = "M12 7V3H2v18h20V7H12zM6 19H4v-2h2v2zm0-4H4v-2h2v2zm0-4H4V9h2v2zm0-4H4V5h2v2zm4 12H8v-2h2v2zm0-4H8v-2h2v2zm0-4H8V9h2v2zm0-4H8V5h2v2zm10 12h-8v-2h2v-2h-2v-2h2v-2h-2V9h8v10zm-2-8h-2v2h2v-2zm0 4h-2v2h2v-2z";
    private static final String ICON_RECORDS = "M19 3h-4.18C14.4 1.84 13.3 1 12 1c-1.3 0-2.4.84-2.82 2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 0c.55 0 1 .45 1 1s-.45 1-1 1-1-.45-1-1 .45-1 1-1zm2 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z";
    private static final String ICON_CHEVRONS_LEFT = "M18 7l-5 5l5 5M12 7l-5 5l5 5";
    private static final String ICON_CHEVRONS = "M7 7l5 5l-5 5M13 7l5 5l-5 5";
    private static final String ICON_PIN = "M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5a2.5 2.5 0 1 1 0-5 2.5 2.5 0 0 1 0 5z";

    private static final double SIDEBAR_EXPANDED = 270;
    private static final double SIDEBAR_COLLAPSED = 82;

    private final String username;
    private final Stage stage = new Stage();
    private final StackPane contentArea = new StackPane();
    private final Map<String, Node> pages = new HashMap<>();

    // Shared Volunteer Lists (Pending vs Approved)
    private final ObservableList<String[]> pendingVolunteerList = FXCollections.observableArrayList();
    private final ObservableList<String[]> approvedVolunteerList = FXCollections.observableArrayList();
    private final ObservableList<String[]> rejectedVolunteerList = FXCollections.observableArrayList();

    // Shared Donation Lists (Pending vs Approved)
    // Columns: {"#", "DONOR NAME", "TYPE", "ITEM / DESCRIPTION", "DATE", "STATUS"}
    private final ObservableList<String[]> pendingDonationList = FXCollections.observableArrayList();
    private final ObservableList<String[]> approvedDonationList = FXCollections.observableArrayList();
    private final ObservableList<String[]> cancelledDonationList = FXCollections.observableArrayList();

    // Shared Event Lists (Active/Upcoming vs Completed)
    // Columns: {"EVENT NAME", "DATE & TIME", "LOCATION", "TYPE", "STATUS", "JOINED"}
    private final ObservableList<String[]> activeEventList = FXCollections.observableArrayList();
    private final ObservableList<String[]> completedEventList = FXCollections.observableArrayList();

    private Runnable refreshRecordsHook = () -> {};
    private Runnable refreshHomeHook = () -> {};
    private Runnable refreshVolunteersHook = () -> {};
    private final java.util.concurrent.atomic.AtomicBoolean volunteersRefreshing =
            new java.util.concurrent.atomic.AtomicBoolean(false);
    private Runnable refreshDonationsHook = () -> {};
    private Runnable refreshEventsCountersHook = () -> {};
    // redraws the three event grids (set when the Events page is built)
    private Runnable rebuildEventGridsHook = () -> {};
    private Timeline joinedPoll;
    private final java.util.concurrent.atomic.AtomicBoolean joinedRefreshing = new java.util.concurrent.atomic.AtomicBoolean(false);

    // home / records widgets that are rebuilt from the real lists (no sample data)
    private final List<Runnable> liveRefreshers = new ArrayList<>();
    private final Label[] homeDonOut = new Label[1];
    private final Label[] homeVolOut = new Label[1];
    private final Label[] homeGoodsOut = new Label[1];

    // sidebar state
    private static class SideItem {
        final HBox row; final Circle dot; final Group icon; final Label text;
        SideItem(HBox row, Circle dot, Group icon, Label text) {
            this.row = row; this.dot = dot; this.icon = icon; this.text = text;
        }
    }
    private final Map<String, SideItem> sideItems = new LinkedHashMap<>();
    private final List<Label> sideTexts = new ArrayList<>();
    private final List<Node> sideHeaders = new ArrayList<>();
    private StackPane sidebar;
    private StackPane avatarCircle;
    private Label greeting;
    private Button toggleBtn;
    private Group chevronsLeftIcon, chevronsRightIcon;
    private Timeline sidebarAnim;
    private boolean sidebarCollapsed = true;
    private String currentPage = "home";

    // notifications (bell in the top bar)
    private static class Notif {
        final String icon, color, bg, text; final long ts; boolean unread;
        Notif(String icon, String color, String bg, String text, long ts, boolean unread) {
            this.icon = icon; this.color = color; this.bg = bg; this.text = text; this.ts = ts; this.unread = unread;
        }
    }
    private final List<Notif> notifications = new ArrayList<>();
    private Label bellBadge;
    private HBox topActions;

    public AdminDashboardFX(String username) {
        this.username = username;
        seedVolunteersData();
        seedDonationsData();
        seedEventsData();
        build();
    }

    private void seedVolunteersData() {
        approvedVolunteerList.addAll(VolunteerStore.rows("Active", "Inactive"));
        pendingVolunteerList.addAll(VolunteerStore.rows("Pending"));
        rejectedVolunteerList.addAll(VolunteerStore.rows("Rejected"));
    }

    /** donation id (D-0001) -> {donor full name, campaign name, campaign photo path}; refreshed with the donations. */
    private Map<String, String[]> donationDetails = new HashMap<>();
    private final Map<String, Image> donationPhotoCache = new HashMap<>();

    private void seedDonationsData() {
        donationDetails = DonationStore.details();
        List<String[]> initialRows = new ArrayList<>();
        int n = 0;
        for (DonationStore.Donation d : DonationStore.all()) {
            String st = (d.status == null || d.status.isBlank()) ? "Pending" : d.status.trim();
            initialRows.add(new String[]{String.format("%03d", ++n), d.donor, d.type, d.item, d.date, st, d.id});
        }
        sortNewestFirst(initialRows, 4, 0);

        for (String[] row : initialRows) {
            if ("Pending".equalsIgnoreCase(row[5])) {
                pendingDonationList.add(row);
            } else if ("Cancelled".equalsIgnoreCase(row[5]) || "Canceled".equalsIgnoreCase(row[5])) {
                cancelledDonationList.add(row);
            } else {
                approvedDonationList.add(row);
            }
        }
    }

    private void seedEventsData() {
        for (String[] row : EventStore.adminRows()) {
            if ("Completed".equalsIgnoreCase(row[4])) completedEventList.add(row);
            else activeEventList.add(row);
        }
    }

    /** Saves the events lists to PostgreSQL (new rows get their id, removed rows are deleted). */
    private void persistEvents() {
        EventStore.sync(activeEventList, completedEventList);
    }

    public void show() {
        stage.setOpacity(0.0);
        stage.show();
        new Timeline(new KeyFrame(javafx.util.Duration.millis(600),
                new KeyValue(stage.opacityProperty(), 1.0, Interpolator.EASE_BOTH))).play();
    }

    private void build() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-font-family: 'Segoe UI';");
        LinearGradient pageBg = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#eaf1ff")),
                new Stop(0.55, Color.web("#a9c8f5")),
                new Stop(1, Color.web("#6fa6e6")));
        root.setBackground(new Background(new BackgroundFill(pageBg, null, null)));

        seedNotifications();
        pages.put("home", buildHomePage());
        pages.put("events", buildEventsPage());
        pages.put("donations", buildDonationsPage());
        pages.put("adddonation", buildAddDonationPage());
        pages.put("addevent", buildAddEventPage());
        pages.put("addannouncement", buildAddAnnouncementPage());
        pages.put("volunteers", buildVolunteersPage());
        pages.put("inactive", buildInactiveVolunteersPage());
        pages.put("records", buildRecordsPage());
        pages.put("settings", buildSettingsPage());

        VBox right = new VBox(buildTopBar(), contentArea);
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        root.setLeft(buildSidebar());
        root.setCenter(right);

        Scene scene = new Scene(root, 1250, 760);
        stage.setTitle("VoNation - Admin Dashboard");
        stage.setScene(scene);
        stage.setMaximized(true);
        showPage("home");

        // Users keep joining events while this window is open: keep the "N joined" numbers current,
        // so they stay equal to what the users see on their side.
        joinedPoll = new Timeline(new KeyFrame(javafx.util.Duration.seconds(5), ev -> {
            refreshJoinedCounts();
            refreshVolunteersIfChanged();
            refreshPendingRegsIfChanged();
        }));
        joinedPoll.setCycleCount(Timeline.INDEFINITE);
        joinedPoll.play();
        stage.setOnHidden(ev -> joinedPoll.stop());
    }

    /** Puts freshly read volunteer rows into the lists; unless force is set, only when something changed. */
    private void applyVolunteerRows(List<String[]> pend, List<String[]> appr, List<String[]> rej, boolean force) {
        boolean dbEmpty = pend.isEmpty() && appr.isEmpty() && rej.isEmpty();
        boolean haveRows = !pendingVolunteerList.isEmpty() || !approvedVolunteerList.isEmpty()
                || !rejectedVolunteerList.isEmpty();
        if (dbEmpty && haveRows) return; // the read most likely failed: keep what is shown
        if (!force && sameRows(pend, pendingVolunteerList) && sameRows(appr, approvedVolunteerList)
                && sameRows(rej, rejectedVolunteerList)) return;
        pendingVolunteerList.setAll(pend);
        approvedVolunteerList.setAll(appr);
        rejectedVolunteerList.setAll(rej);
        refreshVolunteersHook.run();
    }

    private static boolean sameRows(List<String[]> a, List<String[]> b) {
        if (a.size() != b.size()) return false;
        for (int i = 0; i < a.size(); i++) {
            if (!java.util.Arrays.equals(a.get(i), b.get(i))) return false;
        }
        return true;
    }

    /** Checks PostgreSQL (off the UI thread) for new / changed volunteer registrations and redraws if needed. */
    private void refreshVolunteersIfChanged() {
        if (!volunteersRefreshing.compareAndSet(false, true)) return;
        Thread t = new Thread(() -> {
            final List<String[]> pend = VolunteerStore.rows("Pending");
            final List<String[]> appr = VolunteerStore.rows("Active", "Inactive");
            final List<String[]> rej = VolunteerStore.rows("Rejected");
            javafx.application.Platform.runLater(() -> {
                try {
                    applyVolunteerRows(pend, appr, rej, false);
                } finally {
                    volunteersRefreshing.set(false);
                }
            });
        }, "admin-volunteers-refresh");
        t.setDaemon(true);
        t.start();
    }

    /** Reads the joined numbers from the database (off the UI thread) and redraws the event cards if any changed. */
    private void refreshJoinedCounts() {
        if (!joinedRefreshing.compareAndSet(false, true)) return;
        Thread t = new Thread(() -> {
            try {
                final Map<String, String> counts = EventStore.joinedCountsOrNull();
                javafx.application.Platform.runLater(() -> {
                    try {
                        applyJoinedCounts(counts);
                    } finally {
                        joinedRefreshing.set(false);
                    }
                });
            } catch (RuntimeException ex) {
                joinedRefreshing.set(false);
                System.err.println("Joined-count refresh failed: " + ex.getMessage());
            }
        }, "admin-joined-refresh");
        t.setDaemon(true);
        t.start();
    }

    /** Runs on the UI thread. A null map means the database could not be read: keep what is shown. */
    private void applyJoinedCounts(Map<String, String> counts) {
        if (counts == null) return;
        boolean changed = false;
        for (List<String[]> list : List.of(activeEventList, completedEventList)) {
            for (String[] r : list) {
                if (r.length < 8 || r[7] == null || r[7].isBlank()) continue;
                String c = counts.get(r[7].trim());
                if (c != null && !c.equals(r[5])) {
                    r[5] = c;
                    changed = true;
                }
            }
        }
        if (changed) rebuildEventGridsHook.run();
    }

    private void showPage(String name) {
        // always show what is really in PostgreSQL (also donations added by users or typed in psql)
        if ("donations".equals(name) && pages.containsKey("donations")) reloadDonationsFromDb();
        if ("home".equals(name)) rebuildAnnouncementsHook.run();
        if ("events".equals(name) || "home".equals(name)) refreshPendingRegsIfChanged();
        // users register as volunteers while this window is open: always show what is in PostgreSQL
        if (("volunteers".equals(name) || "inactive".equals(name) || "home".equals(name) || "records".equals(name))
                && pages.containsKey("volunteers")) {
            applyVolunteerRows(VolunteerStore.rows("Pending"), VolunteerStore.rows("Active", "Inactive"),
                    VolunteerStore.rows("Rejected"), true);
        }
        contentArea.getChildren().setAll(pages.get(name));
        // the Inactive Volunteers sub-page belongs to the Volunteers section in the sidebar
        // (the Add Donation form belongs to the Donations section)
        currentPage = "inactive".equals(name) ? "volunteers" : "adddonation".equals(name) ? "donations"
                : "addevent".equals(name) ? "events" : "addannouncement".equals(name) ? "home" : name;
        for (Map.Entry<String, SideItem> en : sideItems.entrySet()) {
            styleSideItem(en.getValue(), en.getKey().equals(currentPage));
        }
    }

    // ---------- Sidebar ----------

    private StackPane buildSidebar() {
        VBox content = new VBox(0);
        content.setStyle("-fx-background-color: linear-gradient(to bottom, " + NAVY + ", " + NAVY_DEEP + ");");

        sidebar = new StackPane(content);
        StackPane.setAlignment(content, Pos.TOP_LEFT);
        sidebar.setPrefWidth(sidebarCollapsed ? SIDEBAR_COLLAPSED : SIDEBAR_EXPANDED);
        sidebar.minWidthProperty().bind(sidebar.prefWidthProperty());
        sidebar.maxWidthProperty().bind(sidebar.prefWidthProperty());
        content.prefWidthProperty().bind(sidebar.prefWidthProperty());
        content.minWidthProperty().bind(sidebar.prefWidthProperty());
        content.maxWidthProperty().bind(sidebar.prefWidthProperty());

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(sidebar.widthProperty());
        clip.heightProperty().bind(sidebar.heightProperty());
        sidebar.setClip(clip);

        avatarCircle = new StackPane();
        avatarCircle.setStyle("-fx-background-color: white; -fx-background-radius: 100;");

        greeting = new Label("Admin");
        greeting.setWrapText(true);
        greeting.setMaxWidth(200);
        greeting.setTextAlignment(TextAlignment.CENTER);
        greeting.setAlignment(Pos.CENTER); // centres a one-line name inside the label
        greeting.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        greeting.setTextFill(Color.WHITE);

        VBox profile = new VBox(14, avatarCircle, greeting);
        profile.setAlignment(Pos.CENTER);
        profile.setPadding(new Insets(30, 0, 26, 0));

        content.getChildren().addAll(
                profile,
                sideItem("home", "Dashboard", ICON_DASHBOARD, () -> showPage("home")),
                sideGap(14),
                sideHeader("Management"),
                sideItem("events", "Events & Programs", ICON_CALENDAR, () -> showPage("events")),
                sideItem("donations", "Donations", ICON_HEART, () -> showPage("donations")),
                sideItem("volunteers", "Volunteers", ICON_PEOPLE, () -> showPage("volunteers")),
                sideItem("records", "Records", ICON_RECORDS, () -> showPage("records"))
        );

        applySidebarMode(!sidebarCollapsed);
        return sidebar;
    }

    private HBox sideItem(String key, String text, String iconPath, Runnable action) {
        Circle dot = new Circle(5, Color.web("#fbbf24"));
        dot.setVisible(false);
        Group ic = icon(iconPath, "#aeb9d1", 20);
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        lbl.setTextFill(Color.web("#c3cde3"));

        HBox row = new HBox(14, dot, ic, lbl);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(13, 24, 13, 24));
        row.setOnMouseClicked(e -> action.run());

        SideItem item = new SideItem(row, dot, ic, lbl);
        sideItems.put(key, item);
        sideTexts.add(lbl);
        row.setOnMouseEntered(e -> {
            if (!key.equals(currentPage)) row.setStyle("-fx-background-color: rgba(255,255,255,0.07); -fx-cursor: hand;");
        });
        row.setOnMouseExited(e -> styleSideItem(item, key.equals(currentPage)));
        return row;
    }

    private void styleSideItem(SideItem item, boolean active) {
        item.row.setStyle((active ? "-fx-background-color: rgba(0,0,0,0.30);" : "") + "-fx-cursor: hand;");
        item.text.setTextFill(active ? Color.WHITE : Color.web("#c3cde3"));
        item.text.setFont(Font.font("Segoe UI", active ? FontWeight.BOLD : FontWeight.SEMI_BOLD, 14));
        Node n = item.icon.getChildren().get(0);
        if (n instanceof SVGPath) ((SVGPath) n).setFill(Color.web(active ? "#ffffff" : "#aeb9d1"));
        item.dot.setVisible(active && !sidebarCollapsed);
        item.dot.setManaged(!sidebarCollapsed);
        item.row.setAlignment(sidebarCollapsed ? Pos.CENTER : Pos.CENTER_LEFT);
        item.row.setPadding(sidebarCollapsed ? new Insets(13, 0, 13, 0) : new Insets(13, 24, 13, 24));
    }

    private Label sideHeader(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        l.setTextFill(Color.WHITE);
        l.setPadding(new Insets(10, 24, 6, 24));
        sideHeaders.add(l);
        return l;
    }

    private Region sideGap(double h) {
        Region r = new Region();
        r.setMinHeight(h);
        r.setMaxHeight(h);
        return r;
    }

    private void applySidebarMode(boolean showText) {
        for (Label l : sideTexts) { l.setVisible(showText); l.setManaged(showText); }
        for (Node n : sideHeaders) { n.setVisible(showText); n.setManaged(showText); }
        greeting.setVisible(showText);
        greeting.setManaged(showText);
        refreshAvatar();
        for (Map.Entry<String, SideItem> en : sideItems.entrySet()) {
            styleSideItem(en.getValue(), en.getKey().equals(currentPage));
        }
    }

    private void toggleSidebar() {
        sidebarCollapsed = !sidebarCollapsed;
        toggleBtn.setGraphic(sidebarCollapsed ? chevronsRightIcon : chevronsLeftIcon);

        if (sidebarAnim != null) sidebarAnim.stop();
        sidebarAnim = new Timeline(new KeyFrame(Duration.millis(220),
                new KeyValue(sidebar.prefWidthProperty(),
                        sidebarCollapsed ? SIDEBAR_COLLAPSED : SIDEBAR_EXPANDED, Interpolator.EASE_BOTH)));
        applySidebarMode(false);
        if (!sidebarCollapsed) {
            sidebarAnim.setOnFinished(e -> applySidebarMode(true));
        }
        sidebarAnim.play();
    }

    private void refreshAvatar() {
        double d = sidebarCollapsed ? 48 : 92;
        avatarCircle.setPrefSize(d, d);
        avatarCircle.setMinSize(d, d);
        avatarCircle.setMaxSize(d, d);
        avatarCircle.getChildren().setAll(icon(ICON_PERSON, "#9aa7c0", d * 0.58));
    }

    // ---------- Top bar ----------

    private HBox buildTopBar() {
        HBox bar = new HBox(16);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(12, 28, 12, 24));
        bar.setStyle("-fx-background-color: rgba(255,255,255,0.65); " +
                "-fx-border-color: rgba(255,255,255,0.9); -fx-border-width: 0 0 1.5 0;");

        chevronsLeftIcon = strokeIcon(ICON_CHEVRONS_LEFT, NAVY, 26);
        chevronsRightIcon = strokeIcon(ICON_CHEVRONS, NAVY, 26);
        toggleBtn = new Button();
        toggleBtn.setGraphic(sidebarCollapsed ? chevronsRightIcon : chevronsLeftIcon);
        toggleBtn.setMinSize(46, 46);
        toggleBtn.setMaxSize(46, 46);
        String tBase = "-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 14; " +
                "-fx-border-color: rgba(255,255,255,0.95); -fx-border-radius: 14; -fx-cursor: hand;";
        String tHover = tBase.replace("0.7)", "1.0)");
        toggleBtn.setStyle(tBase);
        toggleBtn.setOnMouseEntered(e -> toggleBtn.setStyle(tHover));
        toggleBtn.setOnMouseExited(e -> toggleBtn.setStyle(tBase));
        toggleBtn.setOnAction(e -> toggleSidebar());

        VBox brandText = new VBox(1);
        Label brandTitle = new Label("VoNation");
        brandTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        brandTitle.setTextFill(Color.web(NAVY_DEEP));
        Label brandSub = new Label("Admin Panel \u2014 Volunteer and Donation Management System");
        brandSub.setFont(Font.font("Segoe UI", 11));
        brandSub.setTextFill(Color.web(TEXT_MUTED));
        brandText.getChildren().addAll(brandTitle, brandSub);

        Button bellBtn = topIconButton("notification.png", "Notifications");
        bellBadge = new Label();
        bellBadge.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-size: 10px; " +
                "-fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 1 5 1 5;");
        bellBadge.setMouseTransparent(true);
        StackPane bellWrap = new StackPane(bellBtn, bellBadge);
        StackPane.setAlignment(bellBadge, Pos.TOP_RIGHT);
        StackPane.setMargin(bellBadge, new Insets(-4, -4, 0, 0));
        bellBtn.setOnAction(e -> showNotifications());

        Button gearBtn = topIconButton("settings.png", "Settings");
        gearBtn.setOnAction(e -> showPage("settings"));

        topActions = new HBox(10, bellWrap, gearBtn);
        topActions.setAlignment(Pos.CENTER);
        refreshBellBadge();

        bar.getChildren().addAll(toggleBtn, brandText, spacer(), topActions);
        return bar;
    }

    private Button topIconButton(String imageFile, String tip) {
        Button b = new Button();
        ImageView iv = loadIcon(imageFile, 22);
        if (iv != null) b.setGraphic(iv); else b.setText(tip.substring(0, 1));
        b.setMinSize(42, 42);
        b.setMaxSize(42, 42);
        String base = "-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 14; " +
                "-fx-border-color: rgba(255,255,255,0.95); -fx-border-radius: 14; -fx-cursor: hand;";
        String hover = base.replace("0.7)", "1.0)");
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(base));
        Tooltip.install(b, new Tooltip(tip));
        return b;
    }

    private ImageView loadIcon(String imageFile, double size) {
        try {
            java.io.InputStream in = getClass().getResourceAsStream("/images/" + imageFile);
            if (in == null) {
                System.out.println("ICON ERROR: /images/" + imageFile + " not found");
                return null;
            }
            ImageView iv = new ImageView(new Image(in));
            iv.setFitWidth(size);
            iv.setFitHeight(size);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
            return iv;
        } catch (Exception e) {
            System.out.println("ICON ERROR: " + e.getMessage());
            return null;
        }
    }

    // ---------- Notifications ----------

    private void seedNotifications() {
        long now = System.currentTimeMillis(), H = 3600_000L;
        notifications.add(new Notif(ICON_CHECK, GREEN, GREEN_LIGHT,
                "Welcome back, " + username + "!", now, true));
        List<DonationStore.Donation> fresh = DonationStore.all();
        for (int i = fresh.size() - 1, shown = 0; i >= 0 && shown < 3; i--, shown++) {
            DonationStore.Donation d = fresh.get(i);
            notifications.add(0, new Notif(ICON_MONEY, GREEN, GREEN_LIGHT,
                    "New donation " + d.id + ": " + d.item + " (" + d.donor + ")", now - 60_000L, true));
        }
    }

    private void addNotification(String iconPath, String color, String bg, String text) {
        notifications.add(0, new Notif(iconPath, color, bg, text, System.currentTimeMillis(), true));
        refreshBellBadge();
        for (Runnable r : liveRefreshers) r.run();
    }

    private void refreshBellBadge() {
        if (bellBadge == null) return;
        long n = notifications.stream().filter(x -> x.unread).count();
        bellBadge.setText(n > 9 ? "9+" : String.valueOf(n));
        bellBadge.setVisible(n > 0);
        bellBadge.setManaged(n > 0);
    }

    private String relativeTime(long ts) {
        long min = Math.max(0, (System.currentTimeMillis() - ts) / 60000);
        if (min < 1) return "Just now";
        if (min < 60) return min + " min ago";
        long h = min / 60;
        if (h < 24) return h + (h == 1 ? " hour ago" : " hours ago");
        long d = h / 24;
        return d + (d == 1 ? " day ago" : " days ago");
    }

    private HBox notifRow(Notif n, Runnable onClick) {
        StackPane iconBox = new StackPane(icon(n.icon, n.color, 18));
        iconBox.setMinSize(38, 38);
        iconBox.setMaxSize(38, 38);
        iconBox.setStyle("-fx-background-color: " + n.bg + "; -fx-background-radius: 12;");

        Label textL = new Label(n.text);
        textL.setFont(Font.font("Segoe UI", n.unread ? FontWeight.BOLD : FontWeight.NORMAL, 13));
        textL.setTextFill(Color.web(NAVY_DEEP));
        textL.setWrapText(true);
        Label timeL = new Label(relativeTime(n.ts));
        timeL.setFont(Font.font("Segoe UI", 11));
        timeL.setTextFill(Color.web(TEXT_MUTED));
        VBox textBox = new VBox(2, textL, timeL);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        Circle dot = new Circle(4, Color.web(BLUE));
        dot.setVisible(n.unread);

        HBox row = new HBox(12, iconBox, textBox, dot);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 12, 10, 12));
        String base = n.unread ? "-fx-background-color: rgba(37,99,235,0.07); " : "-fx-background-color: transparent; ";
        String hover = "-fx-background-color: rgba(27,42,74,0.07); ";
        String common = "-fx-background-radius: 12; -fx-cursor: hand;";
        row.setStyle(base + common);
        row.setOnMouseEntered(e -> row.setStyle(hover + common));
        row.setOnMouseExited(e -> row.setStyle(base + common));
        row.setOnMouseClicked(e -> onClick.run());
        return row;
    }

    private void showNotifications() {
        final double POP_W = 340;
        Popup pop = new Popup();
        pop.setAutoHide(true);

        Label title = new Label("Notifications");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        title.setTextFill(Color.web(NAVY_DEEP));
        Label markAll = new Label("Mark all as read");
        markAll.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        markAll.setTextFill(Color.web(BLUE));
        markAll.setStyle("-fx-cursor: hand;");
        HBox head = new HBox(title, spacer(), markAll);
        head.setAlignment(Pos.CENTER_LEFT);
        head.setPadding(new Insets(0, 4, 0, 4));

        VBox list = new VBox(2);
        list.setPadding(new Insets(0, 2, 0, 2));
        ScrollPane sp = new ScrollPane(list);
        sp.setFitToWidth(true);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(sp, Priority.ALWAYS);

        Runnable[] render = new Runnable[1];
        render[0] = () -> {
            list.getChildren().clear();
            if (notifications.isEmpty()) {
                Label empty = new Label("You're all caught up.\nNo new notifications.");
                empty.setFont(Font.font("Segoe UI", 12));
                empty.setTextFill(Color.web(TEXT_MUTED));
                empty.setPadding(new Insets(14, 8, 14, 8));
                list.getChildren().add(empty);
            }
            for (Notif n : notifications) {
                list.getChildren().add(notifRow(n, () -> {
                    n.unread = false;
                    refreshBellBadge();
                    render[0].run();
                }));
            }
            markAll.setVisible(notifications.stream().anyMatch(x -> x.unread));
        };
        markAll.setOnMouseClicked(e -> {
            notifications.forEach(x -> x.unread = false);
            refreshBellBadge();
            render[0].run();
        });
        render[0].run();

        VBox box = new VBox(10, head, sp);
        box.setPadding(new Insets(16, 12, 12, 12));
        box.setPrefWidth(POP_W);
        box.setMaxWidth(POP_W);
        box.setStyle("-fx-background-color: white; -fx-background-radius: 18;");
        box.setEffect(new DropShadow(16, Color.rgb(18, 29, 53, 0.25)));

        StackPane wrap = new StackPane(box);
        wrap.setPadding(new Insets(14));
        pop.getContent().add(wrap);

        Bounds b = topActions.localToScreen(topActions.getBoundsInLocal());
        double popY = b.getMaxY() - 2;
        double windowBottom = stage.getY() + stage.getHeight();
        double boxH = Math.max(200, windowBottom - popY - 14 * 2);
        box.setPrefHeight(boxH);
        box.setMinHeight(boxH);
        box.setMaxHeight(boxH);
        pop.show(topActions, b.getMaxX() - POP_W - 14, popY);
    }

    private void confirmLogout() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to log out?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(choice -> {
            if (choice == ButtonType.YES) {
                stage.close();
                new LoginFX().start(new Stage());
            }
        });
    }

    // ---------- Home page ----------

    private ScrollPane buildHomePage() {
        VBox body = new VBox(22);
        body.setPadding(new Insets(28, 40, 28, 40));

        Label welcome = new Label("Welcome back, " + username + "!");
        welcome.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        welcome.setTextFill(Color.web(NAVY_DEEP));
        Label welcomeSub = new Label("Here's an overview of your community's activity.");
        welcomeSub.setFont(Font.font("Segoe UI", 14));
        welcomeSub.setTextFill(Color.web(TEXT_MUTED));
        VBox welcomeBox = new VBox(2, welcome, welcomeSub);

        Label[] homeEventsOut = new Label[1];
        HBox statRow = equalRow(
                statCardBound(ICON_MONEY, ORANGE, "Total Donations", "\u20B10", "Approved cash donations", homeDonOut),
                statCardBound(ICON_PEOPLE, PURPLE, "Total Volunteers", "0", "Approved volunteers", homeVolOut),
                statCardBound(ICON_BOX, BLUE, "Physical Goods", "0 items", "Approved in-kind donations", homeGoodsOut),
                statCardBound(ICON_CALENDAR, GREEN, "Programs & Events",
                        String.valueOf(activeEventList.size() + completedEventList.size()), "Events & programs", homeEventsOut)
        );

        VBox overview = buildDonationsOverview(homeEventsOut);
        VBox rightCol = buildRightColumn();
        HBox.setHgrow(overview, Priority.ALWAYS);
        overview.setMaxWidth(Double.MAX_VALUE);
        rightCol.setPrefWidth(440);
        rightCol.setMinWidth(380);
        HBox panelsRow = new HBox(20, overview, rightCol);
        panelsRow.setAlignment(Pos.TOP_LEFT);

        body.getChildren().addAll(welcomeBox, statRow, buildAnnouncementsCard(), panelsRow);
        return wrapScroll(body);
    }

    private static final String ICON_TREND_UP = "M16 6l2.29 2.29-4.88 4.88-4-4L2 16.59 3.41 18l6-6 4 4 6.3-6.29L22 12V6z";
    private static final String ICON_TREND_DOWN = "M16 18l2.29-2.29-4.88-4.88-4 4L2 7.41 3.41 6l6 6 4-4 6.3 6.29L22 12v6z";

    private VBox statCard(String iconPath, String iconColor, String iconBg,
                          String title, String value, String sub, String subColor) {
        return statCardBound(iconPath, iconColor, title, value, sub, null);
    }

    private VBox statCardBound(String iconPath, String iconColor,
                               String title, String initialValue, String sub, Label[] valueLabelOut) {
        String[] grad = statGradient(iconColor);
        boolean down = sub.startsWith("\u25BC");
        String trendText = sub.replace("\u25B2", "").replace("\u25BC", "").trim()
                .replace(" vs. last month", " This month")
                .replace(" vs. last week", " This week");

        Label titleL = new Label(title);
        titleL.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        titleL.setTextFill(Color.WHITE);

        Label valueL = new Label(initialValue);
        valueL.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        valueL.setTextFill(Color.WHITE);
        if (valueLabelOut != null && valueLabelOut.length > 0) {
            valueLabelOut[0] = valueL;
        }

        Group trendIcon = icon(down ? ICON_TREND_DOWN : ICON_TREND_UP, "#ffffff", 18);
        Label trendL = new Label(trendText);
        trendL.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));
        trendL.setTextFill(Color.WHITE);
        HBox trendRow = new HBox(8, trendIcon, trendL);
        trendRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(10, titleL, valueL, trendRow);
        card.setPadding(new Insets(22, 24, 22, 24));
        card.setStyle("-fx-background-color: linear-gradient(to bottom right, " + grad[0] + ", " + grad[1] + ");"
                + "-fx-background-radius: 14;");
        card.setEffect(new DropShadow(20, Color.rgb(20, 40, 90, 0.20)));
        return card;
    }

    private String[] statGradient(String color) {
        if (GREEN.equals(color))  return new String[]{"#22b883", "#1c9e6e"};
        if (BLUE.equals(color))   return new String[]{"#2aa7e6", "#2490d4"};
        if (PURPLE.equals(color)) return new String[]{"#6366b8", "#4f539e"};
        if (ORANGE.equals(color)) return new String[]{"#f58a3c", "#e06a1c"};
        if (RED.equals(color))    return new String[]{"#ef5b5b", "#d33f3f"};
        return new String[]{NAVY, NAVY_DEEP};
    }

    private VBox buildDonationsOverview(Label[] homeEventsOut) {
        Label title = sectionTitle("Donations Overview", ICON_HEART);
        HBox header = new HBox(title);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox cashMini = miniStat("Cash Donations", "\u20B10", "No approved donations yet");
        VBox goodsMini = miniStat("Physical Goods", "0 items", "No approved donations yet");
        HBox miniStats = new HBox(14, cashMini, goodsMini);
        for (Node n : miniStats.getChildren()) {
            HBox.setHgrow(n, Priority.ALWAYS);
            ((Region) n).setMaxWidth(Double.MAX_VALUE);
            ((Region) n).setPrefWidth(10);
        }

        Label recentLabel = new Label("Recent Approved Donations");
        recentLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        recentLabel.setTextFill(Color.web(NAVY_DEEP));

        ObservableList<String[]> recentApprovedRows = FXCollections.observableArrayList();
        Runnable syncRecentHome = () -> {
            recentApprovedRows.clear();
            int count = Math.min(5, approvedDonationList.size());
            for (int i = 0; i < count; i++) {
                String[] d = approvedDonationList.get(i);
                recentApprovedRows.add(new String[]{d[4], d[1], d[2], d[3], d[5]});
            }
            if (homeEventsOut != null && homeEventsOut[0] != null) {
                homeEventsOut[0].setText(String.valueOf(activeEventList.size() + completedEventList.size()));
            }
            double cash = approvedCashTotal();
            int goods = approvedGoodsCount();
            int cashN = approvedCashCount();
            int total = approvedDonationList.size();
            if (homeDonOut[0] != null) homeDonOut[0].setText(formatPeso(cash));
            if (homeVolOut[0] != null) homeVolOut[0].setText(String.valueOf(approvedVolunteerList.size()));
            if (homeGoodsOut[0] != null) homeGoodsOut[0].setText(goods + " items");
            ((Label) cashMini.getChildren().get(1)).setText(formatPeso(cash));
            ((Label) cashMini.getChildren().get(2)).setText(total == 0
                    ? "No approved donations yet" : (cashN * 100 / total) + "% of approved donations");
            ((Label) goodsMini.getChildren().get(1)).setText(goods + " items");
            ((Label) goodsMini.getChildren().get(2)).setText(total == 0
                    ? "No approved donations yet" : (goods * 100 / total) + "% of approved donations");
        };
        refreshHomeHook = syncRecentHome;
        syncRecentHome.run();

        TableView<String[]> table = buildTableFromObservable(
                new String[]{"DATE", "DONOR / SOURCE", "TYPE", "AMOUNT / ITEM", "STATUS"},
                recentApprovedRows
        );

        return glassCard(new VBox(14, header, miniStats, recentLabel, table));
    }

    // ---- live totals computed from the approved donation list ----

    private static double parseAmount(String s) {
        if (s == null) return 0;
        String digits = s.replaceAll("[^0-9.]", "");
        if (digits.isEmpty() || digits.equals(".")) return 0;
        try {
            return Double.parseDouble(digits);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private double approvedCashTotal() {
        double sum = 0;
        for (String[] d : approvedDonationList) {
            if ("Cash".equalsIgnoreCase(d[2])) sum += parseAmount(d[3]);
        }
        return sum;
    }

    private int approvedCashCount() {
        int n = 0;
        for (String[] d : approvedDonationList) if ("Cash".equalsIgnoreCase(d[2])) n++;
        return n;
    }

    private int approvedGoodsCount() {
        int n = 0;
        for (String[] d : approvedDonationList) if ("In-Kind".equalsIgnoreCase(d[2])) n++;
        return n;
    }

    private static String formatPeso(double v) {
        return "\u20B1" + (v == Math.rint(v) ? String.format("%,.0f", v) : String.format("%,.2f", v));
    }

    private VBox miniStat(String title, String value, String sub) {
        Label titleL = new Label(title);
        titleL.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));
        titleL.setTextFill(Color.web(TEXT_MUTED));
        Label valueL = new Label(value);
        valueL.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        valueL.setTextFill(Color.web(NAVY_DEEP));
        Label subL = new Label(sub);
        subL.setFont(Font.font("Segoe UI", 11));
        subL.setTextFill(Color.web(TEXT_MUTED));
        VBox box = new VBox(2, titleL, valueL, subL);
        box.setPadding(new Insets(14, 16, 14, 16));
        box.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 14; " +
                "-fx-border-color: rgba(255,255,255,0.95); -fx-border-radius: 14;");
        return box;
    }

    private VBox buildRightColumn() {
        return new VBox(20, buildUpcomingEvents(), buildRecentActivity());
    }

    private VBox buildUpcomingEvents() {
        Label title = sectionTitle("Upcoming Events", ICON_CALENDAR);
        Label link = linkLabel("View All \u2192", () -> showPage("events"));
        HBox header = new HBox(title, spacer(), link);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox list = new VBox(0);
        Runnable refresh = () -> {
            list.getChildren().clear();
            int shown = 0;
            for (String[] ev : activeEventList) {
                if (shown == 3) break;
                if (shown > 0) list.getChildren().add(softSeparator());
                list.getChildren().add(eventRow(badgeDate(ev[1]), ev[0], timePart(ev[1]), ev[4], BLUE_LIGHT, BLUE));
                shown++;
            }
            if (shown == 0) list.getChildren().add(emptyNote("No upcoming events yet. Add one in Events & Programs."));
        };
        Runnable prev = refreshHomeHook;
        refreshHomeHook = () -> {
            prev.run();
            refresh.run();
        };
        refresh.run();

        return glassCard(new VBox(12, header, list));
    }

    private String badgeDate(String dateAndTime) {
        String d = extractDatePart(dateAndTime);
        String[] p = d.trim().split("[ ,]+");
        return p.length >= 2 ? (p[0] + " " + p[1]).toUpperCase() : d.toUpperCase();
    }

    private String timePart(String dateAndTime) {
        if (dateAndTime == null) return "";
        int slash = dateAndTime.indexOf('/');
        return slash >= 0 ? dateAndTime.substring(slash + 1).trim() : "";
    }

    private HBox eventRow(String date, String name, String time, String status, String badgeBg, String badgeFg) {
        Label dateBadge = new Label(date);
        dateBadge.setStyle("-fx-background-color: linear-gradient(to bottom right, " + NAVY + ", " + BLUE + ");" +
                "-fx-text-fill: white; -fx-font-size: 11; -fx-font-weight: bold; -fx-background-radius: 14;");
        dateBadge.setMinSize(56, 56);
        dateBadge.setMaxSize(56, 56);
        dateBadge.setAlignment(Pos.CENTER);
        dateBadge.setWrapText(true);
        dateBadge.setTextAlignment(TextAlignment.CENTER);

        Label nameL = new Label(name);
        nameL.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        nameL.setTextFill(Color.web(NAVY_DEEP));
        Label timeL = new Label(time);
        timeL.setFont(Font.font("Segoe UI", 12));
        timeL.setTextFill(Color.web(TEXT_MUTED));
        VBox textBox = new VBox(3, nameL, timeL);

        HBox row = new HBox(16, dateBadge, textBox, spacer(), statusBadge(status, badgeBg, badgeFg));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 0, 12, 0));
        return row;
    }

    private Label statusBadge(String text, String bg, String fg) {
        Label l = new Label(text);
        l.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: " + fg + "; -fx-font-size: 11; " +
                "-fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 4 10 4 10;");
        return l;
    }

    private VBox buildRecentActivity() {
        Label title = sectionTitle("Recent Activity", ICON_CLOCK);
        VBox list = new VBox(0);
        Runnable refresh = () -> fillActivityList(list);
        liveRefreshers.add(refresh);
        refresh.run();
        return glassCard(new VBox(12, title, list));
    }

    /** Fills a list with the latest real activity (from the notifications log), newest first. */
    private void fillActivityList(VBox list) {
        list.getChildren().clear();
        int shown = 0;
        for (Notif n : notifications) {
            if (n.text.startsWith("Welcome back")) continue;
            if (shown == 4) break;
            if (shown > 0) list.getChildren().add(softSeparator());
            list.getChildren().add(activityRow(n.icon, n.color, n.bg, n.text, relativeTime(n.ts)));
            shown++;
        }
        if (shown == 0) list.getChildren().add(emptyNote("No recent activity yet."));
    }

    private HBox activityRow(String iconPath, String iconColor, String iconBg, String text, String time) {
        StackPane iconBox = new StackPane(icon(iconPath, iconColor, 18));
        iconBox.setMinSize(38, 38);
        iconBox.setMaxSize(38, 38);
        iconBox.setStyle("-fx-background-color: " + iconBg + "; -fx-background-radius: 12;");

        Label textL = new Label(text);
        textL.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        textL.setTextFill(Color.web(NAVY_DEEP));
        textL.setWrapText(true);
        Label timeL = new Label(time);
        timeL.setFont(Font.font("Segoe UI", 11));
        timeL.setTextFill(Color.web(TEXT_MUTED));
        VBox textBox = new VBox(2, textL, timeL);

        HBox row = new HBox(14, iconBox, textBox);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 0, 12, 0));
        return row;
    }

    // ---------- Events & Programs page (Add Event + Separated Active/Upcoming vs Completed) ----------

    private ScrollPane buildEventsPage() {
        VBox body = new VBox(22);
        body.setPadding(new Insets(28, 40, 28, 40));

        Label[] totalEvtOut = new Label[1];
        Label[] activeEvtOut = new Label[1];
        Label[] openEvtOut = new Label[1];
        Label[] compEvtOut = new Label[1];
        Label[] upcomingEvtOut = new Label[1];

        Label activeCountBadge = new Label();
        activeCountBadge.setStyle("-fx-background-color: #ffedd5; -fx-text-fill: #c2410c; -fx-font-weight: bold; "
                + "-fx-font-size: 12px; -fx-padding: 4 12 4 12; -fx-background-radius: 16;");

        Label completedCountBadge = new Label();
        completedCountBadge.setStyle("-fx-background-color: #dcfce7; -fx-text-fill: #15803d; -fx-font-weight: bold; "
                + "-fx-font-size: 12px; -fx-padding: 4 12 4 12; -fx-background-radius: 16;");

        Label upcomingCountBadge = new Label();
        upcomingCountBadge.setStyle("-fx-background-color: #dbeafe; -fx-text-fill: #1d4ed8; -fx-font-weight: bold; "
                + "-fx-font-size: 12px; -fx-padding: 4 12 4 12; -fx-background-radius: 16;");

        Runnable updateEventCounters = () -> {
            persistEvents();
            // Active = ongoing / registration open.  Upcoming = upcoming / scheduled.
            int activeCount = (int) activeEventList.stream().filter(r -> isActiveEventStatus(r[4])).count();
            int upcomingCount = activeEventList.size() - activeCount;
            int compCount = completedEventList.size();
            int totalCount = activeEventList.size() + compCount;
            long regOpenCount = activeEventList.stream()
                    .filter(r -> "Registration Open".equalsIgnoreCase(r[4]) || "Upcoming".equalsIgnoreCase(r[4]))
                    .count();

            if (totalEvtOut[0] != null) totalEvtOut[0].setText(String.valueOf(totalCount));
            if (activeEvtOut[0] != null) activeEvtOut[0].setText(String.valueOf(activeCount));
            if (upcomingEvtOut[0] != null) upcomingEvtOut[0].setText(String.valueOf(upcomingCount));
            if (openEvtOut[0] != null) openEvtOut[0].setText(String.valueOf(regOpenCount));
            if (compEvtOut[0] != null) compEvtOut[0].setText(String.valueOf(compCount));

            activeCountBadge.setText(activeCount + " Active");
            upcomingCountBadge.setText(upcomingCount + " Upcoming");
            completedCountBadge.setText(compCount + " Completed");
            refreshHomeHook.run();
            refreshRecordsHook.run();
        };
        refreshEventsCountersHook = updateEventCounters;

        Button addBtn = primaryButton("+ Add Event");
        addBtn.setOnAction(e -> openEventForm());
        body.getChildren().add(pageHeader("Events & Programs",
                "Create and manage all community events and programs.", addBtn));

        ScrollPane[] evtScrollRef = new ScrollPane[1];
        Node[] evtActiveRef = new Node[1];
        Node[] evtUpcomingRef = new Node[1];
        Node[] evtCompletedRef = new Node[1];

        VBox evtTotalCard = statCardBound(ICON_CALENDAR, BLUE, "Total Events", "0", "All events & programs", totalEvtOut);
        VBox evtActiveStat = statCardBound(ICON_CLOCK, ORANGE, "Active Events", "0", "Ongoing & open now", activeEvtOut);
        VBox evtUpcomingStat = statCardBound(ICON_CALENDAR, PURPLE, "Upcoming Events", "0", "Scheduled ahead", upcomingEvtOut);
        VBox evtOpenStat = statCardBound(ICON_PEOPLE, BLUE, "Open for Volunteers", "0", "Accepting participants", openEvtOut);
        VBox evtCompletedStat = statCardBound(ICON_CHECK, GREEN, "Completed Events", "0", "Successfully finished", compEvtOut);

        makeClickableCard(evtTotalCard, "View active events", () -> scrollToNode(evtScrollRef[0], evtActiveRef[0]));
        makeClickableCard(evtActiveStat, "View active events", () -> scrollToNode(evtScrollRef[0], evtActiveRef[0]));
        makeClickableCard(evtUpcomingStat, "View upcoming events", () -> scrollToNode(evtScrollRef[0], evtUpcomingRef[0]));
        makeClickableCard(evtOpenStat, "View upcoming events open for volunteers", () -> scrollToNode(evtScrollRef[0], evtUpcomingRef[0]));
        makeClickableCard(evtCompletedStat, "View completed events", () -> scrollToNode(evtScrollRef[0], evtCompletedRef[0]));

        HBox stats = equalRow(evtTotalCard, evtActiveStat, evtUpcomingStat, evtOpenStat, evtCompletedStat);
        body.getChildren().add(stats);

        // 1. ACTIVE EVENTS (ongoing / registration open)
        Label activeTitle = sectionTitle("Active Events", ICON_CLOCK);
        Label activeSub = new Label("Events that are ongoing or currently open for registration. Mark an event as Completed once finished.");
        activeSub.setFont(Font.font("Segoe UI", 12.5));
        activeSub.setTextFill(Color.web(TEXT_MUTED));
        HBox activeHeader = new HBox(12, new VBox(2, activeTitle, activeSub), spacer(), activeCountBadge);
        activeHeader.setAlignment(Pos.CENTER_LEFT);

        final double cardW = 330;
        FlowPane activeGrid = new FlowPane(20, 20);
        activeGrid.setPrefWrapLength(4 * cardW);
        activeGrid.setAlignment(Pos.TOP_LEFT);

        FlowPane upcomingGrid = new FlowPane(20, 20);
        upcomingGrid.setPrefWrapLength(4 * cardW);
        upcomingGrid.setAlignment(Pos.TOP_LEFT);

        FlowPane completedGrid = new FlowPane(20, 20);
        completedGrid.setPrefWrapLength(4 * cardW);
        completedGrid.setAlignment(Pos.TOP_LEFT);

        final String[] activeFilter = {"All"};
        final Runnable[] rebuildActive = new Runnable[1];
        final Runnable[] rebuildUpcoming = new Runnable[1];
        final Runnable[] rebuildCompleted = new Runnable[1];

        List<Button> tabButtons = new ArrayList<>();
        HBox tabs = new HBox(10);
        tabs.setAlignment(Pos.CENTER_LEFT);

        Runnable refreshAll = () -> {
            updateEventCounters.run();
            rebuildActive[0].run();
            rebuildUpcoming[0].run();
            rebuildCompleted[0].run();
        };

        for (String category : new String[]{"All", "Environment", "Health", "Relief", "Training", "Outreach"}) {
            Button tab = new Button(category);
            tab.setCursor(Cursor.HAND);
            tab.setFocusTraversable(false);
            styleFilterTab(tab, "All".equals(category));
            tab.setOnAction(e -> {
                activeFilter[0] = category;
                for (Button b : tabButtons) styleFilterTab(b, b.getText().equals(activeFilter[0]));
                rebuildActive[0].run();
                rebuildUpcoming[0].run();
            });
            tabButtons.add(tab);
            tabs.getChildren().add(tab);
        }

        rebuildActive[0] = () -> {
            activeGrid.getChildren().clear();
            for (String[] ev : activeEventList) {
                if (!isActiveEventStatus(ev[4])) continue;
                if (!"All".equals(activeFilter[0]) && !activeFilter[0].equalsIgnoreCase(ev[3])) continue;
                activeGrid.getChildren().add(eventCard(ev, false, cardW, refreshAll));
            }
            if (activeGrid.getChildren().isEmpty()) {
                activeGrid.getChildren().add(emptyNote("No active events in this category right now."));
            }
        };

        rebuildUpcoming[0] = () -> {
            upcomingGrid.getChildren().clear();
            for (String[] ev : activeEventList) {
                if (isActiveEventStatus(ev[4])) continue;
                if (!"All".equals(activeFilter[0]) && !activeFilter[0].equalsIgnoreCase(ev[3])) continue;
                upcomingGrid.getChildren().add(eventCard(ev, false, cardW, refreshAll));
            }
            if (upcomingGrid.getChildren().isEmpty()) {
                upcomingGrid.getChildren().add(emptyNote("No upcoming events in this category yet."));
            }
        };

        rebuildCompleted[0] = () -> {
            completedGrid.getChildren().clear();
            for (String[] ev : completedEventList) {
                completedGrid.getChildren().add(eventCard(ev, true, cardW, refreshAll));
            }
            if (completedGrid.getChildren().isEmpty()) {
                completedGrid.getChildren().add(emptyNote("No completed events yet."));
            }
        };

        rebuildEventGridsHook = () -> {
            rebuildActive[0].run();
            rebuildUpcoming[0].run();
            rebuildCompleted[0].run();
        };

        VBox activeCard = glassCard(new VBox(16, activeHeader, activeGrid));

        // 2. UPCOMING EVENTS (upcoming / scheduled)
        Label upcomingTitle = sectionTitle("Upcoming Events", ICON_CALENDAR);
        Label upcomingSub = new Label("Events that are scheduled or coming up soon.");
        upcomingSub.setFont(Font.font("Segoe UI", 12.5));
        upcomingSub.setTextFill(Color.web(TEXT_MUTED));
        HBox upcomingHeader = new HBox(12, new VBox(2, upcomingTitle, upcomingSub), spacer(), upcomingCountBadge);
        upcomingHeader.setAlignment(Pos.CENTER_LEFT);
        VBox upcomingCard = glassCard(new VBox(16, upcomingHeader, upcomingGrid));

        // 3. COMPLETED EVENTS - archive cards
        Label completedTitle = sectionTitle("Completed Events", ICON_CHECK);
        Label completedSub = new Label("Archive of completed community events and programs.");
        completedSub.setFont(Font.font("Segoe UI", 12.5));
        completedSub.setTextFill(Color.web(TEXT_MUTED));
        HBox completedHeader = new HBox(12, new VBox(2, completedTitle, completedSub), spacer(), completedCountBadge);
        completedHeader.setAlignment(Pos.CENTER_LEFT);

        VBox completedCard = glassCard(new VBox(16, completedHeader, completedGrid));

        updateEventCounters.run();
        rebuildActive[0].run();
        rebuildUpcoming[0].run();
        rebuildCompleted[0].run();

        // category tabs filter both the Active and Upcoming sections
        body.getChildren().addAll(buildPendingRegistrationsCard(), tabs, activeCard, upcomingCard, completedCard);
        evtActiveRef[0] = tabs;
        evtUpcomingRef[0] = upcomingCard;
        evtCompletedRef[0] = completedCard;
        ScrollPane evtScroll = wrapScroll(body);
        evtScrollRef[0] = evtScroll;
        return evtScroll;
    }

    /** Active = happening now / open for registration. Everything else in the list is upcoming. */
    private static boolean isActiveEventStatus(String status) {
        return "Registration Open".equalsIgnoreCase(status) || "Ongoing".equalsIgnoreCase(status);
    }

    /** Small note shown inside a card grid when nothing matches. */
    private Label emptyNote(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        l.setTextFill(Color.web(TEXT_MUTED));
        l.setPadding(new Insets(26, 0, 26, 4));
        return l;
    }

    /** Pill-style filter tab (blue when selected, outlined when not). */
    private void styleFilterTab(Button b, boolean active) {
        String base = "-fx-font-weight: bold; -fx-font-size: 12.5px; -fx-background-radius: 20; "
                + "-fx-padding: 8 18 8 18; -fx-cursor: hand;";
        if (active) {
            b.setStyle("-fx-background-color: " + BLUE + "; -fx-text-fill: white;" + base);
        } else {
            b.setStyle("-fx-background-color: white; -fx-text-fill: " + TEXT_MUTED + ";"
                    + "-fx-border-color: #dbe3ef; -fx-border-radius: 20;" + base);
        }
    }

    /**
     * One event tile: photo on top (or a gradient placeholder), then the name,
     * date, location, type/status badges and the Complete / Cancel actions.
     */
    private StackPane eventCard(String[] ev, boolean completed, double cardWidth, Runnable onChange) {
        StackPane photo = new StackPane();
        photo.setMinHeight(168);
        photo.setPrefHeight(168);
        photo.setMaxHeight(168);

        Image img = null;
        if (ev.length > 6 && ev[6] != null && !ev[6].isEmpty()) {
            try {
                File f = new File(ev[6]);
                if (f.isFile()) {
                    img = new Image(f.toURI().toString(), cardWidth * 2, 336, true, true, false);
                    if (img.isError() || img.getWidth() <= 0) img = null;
                }
            } catch (Exception ignored) {
                img = null;
            }
        }

        if (img != null) {
            photo.setBackground(new Background(new BackgroundImage(img,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                    new BackgroundSize(cardWidth, 168, false, false, false, true))));
        } else {
            // No photo yet: a category-coloured gradient stands in for the image
            String[] grad = switch (ev[3] == null ? "" : ev[3]) {
                case "Environment" -> new String[]{"#16a34a", "#0d9488"};
                case "Health" -> new String[]{"#f87171", "#dc2626"};
                case "Relief" -> new String[]{"#fb923c", "#ea580c"};
                case "Training" -> new String[]{"#a78bfa", "#6d28d9"};
                case "Outreach" -> new String[]{"#60a5fa", "#2563eb"};
                default -> new String[]{"#3b82f6", "#7c3aed"};
            };
            photo.setBackground(new Background(new BackgroundFill(
                    new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.web(grad[0])), new Stop(1, Color.web(grad[1]))),
                    CornerRadii.EMPTY, Insets.EMPTY)));
            Group glyph = icon(ICON_CALENDAR, "#ffffff", 46);
            glyph.setOpacity(0.55);
            photo.getChildren().add(glyph);
        }

        Label name = new Label(ev[0]);
        name.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15.5));
        name.setTextFill(Color.web(NAVY_DEEP));
        name.setWrapText(true);
        name.setMaxHeight(42);

        HBox dateRow = cardInfo(ICON_CALENDAR, ev[1]);
        HBox locRow = cardInfo(ICON_PIN, ev[2]);

        HBox meta = new HBox(8, badge("TYPE", ev[3]), badge("STATUS", ev[4]));
        meta.setAlignment(Pos.CENTER_LEFT);

        HBox joinedRow = cardInfo(ICON_PEOPLE, joinedText(ev));

        VBox body = new VBox(9, name, dateRow, locRow, meta, joinedRow);
        if (ev.length > 8 && rowNumber(ev[8]) > 0) body.getChildren().add(volunteerBar(ev));
        body.setPadding(new Insets(16, 18, 16, 18));

        if (!completed) {
            Button completeBtn = flatAction("\u2713 Complete", "#16a34a", "#15803d");
            Button cancelBtn = flatAction("\u2715 Cancel", "#fee2e2", "#fecaca");
            cancelBtn.setTextFill(Color.web("#b91c1c"));
            completeBtn.setTextFill(Color.WHITE);
            completeBtn.setOnAction(e -> {
                activeEventList.remove(ev);
                completedEventList.add(0, new String[]{
                        ev[0], ev[1], ev[2], ev[3], "Completed", ev[5], ev.length > 6 ? ev[6] : "",
                        ev.length > 7 ? ev[7] : ""
                });
                addNotification(ICON_CHECK, GREEN, GREEN_LIGHT, "Event marked as Completed: " + ev[0]);
                onChange.run();
            });
            cancelBtn.setOnAction(e -> {
                activeEventList.remove(ev);
                addNotification(ICON_CANCEL, RED, RED_LIGHT, "Event cancelled/removed: " + ev[0]);
                onChange.run();
            });
            // an Upcoming event first has to be approved: that moves it to Active Events
            HBox actions;
            if (isActiveEventStatus(ev[4])) {
                actions = new HBox(8, completeBtn, cancelBtn);
            } else {
                Button approveBtn = flatAction("\u2713 Approve", "#16a34a", "#15803d");
                approveBtn.setTextFill(Color.WHITE);
                approveBtn.setOnAction(e -> {
                    ev[4] = "Registration Open";
                    addNotification(ICON_CHECK, GREEN, GREEN_LIGHT, "Event approved and now Active: " + ev[0]);
                    onChange.run();
                });
                actions = new HBox(8, approveBtn, cancelBtn);
            }
            actions.setAlignment(Pos.CENTER_LEFT);
            body.getChildren().add(actions);
        }

        VBox card = new VBox(photo, body);
        card.setOnMouseClicked(e -> {
            if (!(e.getTarget() instanceof Button)) {
                showAdminEventDetails(ev, completed, onChange);
            }
        });
        card.setCursor(Cursor.HAND);
        card.setPrefWidth(cardWidth);
        card.setMinWidth(cardWidth);
        card.setMaxWidth(cardWidth);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 18; "
                + "-fx-border-color: #e6ecf6; -fx-border-radius: 18;");

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(card.widthProperty());
        clip.heightProperty().bind(card.heightProperty());
        clip.setArcWidth(36);
        clip.setArcHeight(36);
        card.setClip(clip);

        StackPane holder = new StackPane(card);
        DropShadow shadow = new DropShadow(20, Color.rgb(20, 40, 90, 0.13));
        holder.setEffect(shadow);

        // hover animation: the card lifts a little and its shadow grows, so it is clear it can be clicked
        Timeline hover = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(holder.translateYProperty(), 0),
                        new KeyValue(holder.scaleXProperty(), 1.0),
                        new KeyValue(holder.scaleYProperty(), 1.0),
                        new KeyValue(shadow.radiusProperty(), 20),
                        new KeyValue(shadow.offsetYProperty(), 0)),
                new KeyFrame(Duration.millis(170),
                        new KeyValue(holder.translateYProperty(), -6, Interpolator.EASE_OUT),
                        new KeyValue(holder.scaleXProperty(), 1.015, Interpolator.EASE_OUT),
                        new KeyValue(holder.scaleYProperty(), 1.015, Interpolator.EASE_OUT),
                        new KeyValue(shadow.radiusProperty(), 32, Interpolator.EASE_OUT),
                        new KeyValue(shadow.offsetYProperty(), 8, Interpolator.EASE_OUT)));
        holder.setOnMouseEntered(e -> {
            hover.setRate(1);
            hover.play();
        });
        holder.setOnMouseExited(e -> {
            hover.setRate(-1);
            hover.play();
        });
        // a short press effect when it is clicked
        card.setOnMousePressed(e -> { card.setScaleX(0.985); card.setScaleY(0.985); });
        card.setOnMouseReleased(e -> { card.setScaleX(1.0); card.setScaleY(1.0); });
        return holder;
    }

    /**
     * Admin event details window inspired by the supplied volunteer event
     * participation reference. It displays only fields currently stored
     * in the event record; no sample organizer/requirements are invented.
     */
    // ---------- pending volunteer registrations (Events page), same idea as Pending Donations ----------

    private final VBox pendingRegList = new VBox(10);
    private final Label pendingRegBadge = new Label();
    private List<String[]> shownPendingRegs = null;
    private final java.util.concurrent.atomic.AtomicBoolean pendingRegsRefreshing =
            new java.util.concurrent.atomic.AtomicBoolean(false);

    private VBox buildPendingRegistrationsCard() {
        Label title = sectionTitle("Pending Volunteer Registrations", ICON_CLOCK);
        Label sub = new Label("Verify and approve the volunteers who joined your events. Users see your decision right away.");
        sub.setFont(Font.font("Segoe UI", 12.5));
        sub.setTextFill(Color.web(TEXT_MUTED));
        pendingRegBadge.setStyle("-fx-background-color: #ffedd5; -fx-text-fill: #c2410c; -fx-font-weight: bold; "
                + "-fx-font-size: 12px; -fx-padding: 4 12 4 12; -fx-background-radius: 16;");
        HBox header = new HBox(12, new VBox(2, title, sub), spacer(), pendingRegBadge);
        header.setAlignment(Pos.CENTER_LEFT);
        VBox card = glassCard(new VBox(16, header, pendingRegList));
        shownPendingRegs = null;
        List<String[]> first = EventStore.pendingRegistrationsOrNull();
        applyPendingRegs(first == null ? new ArrayList<>() : first);
        return card;
    }

    /** Redraws the pending list when it differs from what is shown. */
    private void applyPendingRegs(List<String[]> rows) {
        if (shownPendingRegs != null && sameRows(rows, shownPendingRegs)) return;
        shownPendingRegs = rows;
        pendingRegBadge.setText(rows.size() + " Pending");
        pendingRegList.getChildren().clear();
        if (rows.isEmpty()) {
            pendingRegList.getChildren().add(emptyNote("No pending volunteer registrations awaiting approval."));
            return;
        }
        for (String[] r : rows) { // {id, event name, full name, email, contact, date, event id}
            Label who = new Label(r[2].isBlank() ? r[3] : r[2]);
            who.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14.5));
            who.setTextFill(Color.web(NAVY_DEEP));
            Label evLbl = new Label("Joined: " + r[1]);
            evLbl.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12.5));
            evLbl.setTextFill(Color.web(NAVY));
            Label sub = new Label(r[3] + (r[4].isBlank() ? "" : " \u2022 " + r[4]) + " \u2022 " + r[5]);
            sub.setFont(Font.font("Segoe UI", 12));
            sub.setTextFill(Color.web(TEXT_MUTED));
            sub.setWrapText(true);
            VBox texts = new VBox(3, who, evLbl, sub);
            HBox.setHgrow(texts, Priority.ALWAYS);

            Button approve = flatAction("\u2713 Approve", "#16a34a", "#15803d");
            approve.setTextFill(Color.WHITE);
            Button reject = flatAction("\u2715 Reject", "#fee2e2", "#fecaca");
            reject.setTextFill(Color.web("#b91c1c"));
            approve.setOnAction(e -> {
                if (EventStore.setRegistrationStatus(r[0], "Approved")) {
                    addNotification(ICON_CHECK, GREEN, GREEN_LIGHT, "Approved " + who.getText() + " for " + r[1]);
                    afterRegistrationDecision();
                } else {
                    infoAlert("Could not save your decision. Please try again.");
                }
            });
            reject.setOnAction(e -> {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Reject Registration");
                confirm.setHeaderText("Reject this volunteer?");
                confirm.setContentText(who.getText() + " will not be able to join " + r[1] + ".");
                confirm.showAndWait().ifPresent(response -> {
                    if (response != ButtonType.OK) return;
                    if (EventStore.setRegistrationStatus(r[0], "Rejected")) {
                        addNotification(ICON_CANCEL, RED, RED_LIGHT, "Rejected " + who.getText() + " for " + r[1]);
                        afterRegistrationDecision();
                    } else {
                        infoAlert("Could not save your decision. Please try again.");
                    }
                });
            });

            HBox actions = new HBox(8, approve, reject);
            actions.setAlignment(Pos.CENTER_RIGHT);
            HBox row = new HBox(14, texts, actions);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(12, 16, 12, 16));
            row.setStyle("-fx-background-color: white; -fx-background-radius: 14; "
                    + "-fx-border-color: #e6ecf6; -fx-border-radius: 14;");
            pendingRegList.getChildren().add(row);
        }
    }

    private void afterRegistrationDecision() {
        List<String[]> fresh = EventStore.pendingRegistrationsOrNull();
        if (fresh != null) applyPendingRegs(fresh);
        refreshJoinedCounts();
    }

    /** Checks PostgreSQL (off the UI thread) for new registrations and redraws the pending list if needed. */
    private void refreshPendingRegsIfChanged() {
        if (!pendingRegsRefreshing.compareAndSet(false, true)) return;
        Thread t = new Thread(() -> {
            final List<String[]> fresh = EventStore.pendingRegistrationsOrNull();
            javafx.application.Platform.runLater(() -> {
                try {
                    if (fresh != null) applyPendingRegs(fresh);
                } finally {
                    pendingRegsRefreshing.set(false);
                }
            });
        }, "admin-pending-regs");
        t.setDaemon(true);
        t.start();
    }

    /** Saves the admin's decision on one registration, then redraws the list and the joined counts. */
    private void decideRegistration(String registrationId, String status, Runnable reload) {
        if (EventStore.setRegistrationStatus(registrationId, status)) {
            reload.run();
            afterRegistrationDecision();
        } else {
            infoAlert("Could not save your decision. Please try again.");
        }
    }

    private void showAdminEventDetails(String[] ev, boolean completed, Runnable onChange) {
        Stage dialog = new Stage();
        dialog.initOwner(stage);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Event Details - " + ev[0]);

        String photoPath = ev.length > 6 && ev[6] != null ? ev[6] : "";
        ImageView eventImage = new ImageView();
        eventImage.setFitWidth(360);
        eventImage.setFitHeight(270);
        eventImage.setPreserveRatio(false);
        eventImage.setSmooth(true);

        boolean hasPhoto = false;
        if (!photoPath.isBlank()) {
            try {
                File imageFile = new File(photoPath);
                if (imageFile.isFile()) {
                    Image image = new Image(imageFile.toURI().toString(), 720, 540, true, true);
                    if (!image.isError()) {
                        eventImage.setImage(image);
                        hasPhoto = true;
                    }
                }
            } catch (Exception ignored) {
                // Use the visual placeholder below if the photo is unavailable.
            }
        }

        StackPane imageFrame = new StackPane();
        imageFrame.setPrefSize(360, 270);
        imageFrame.setMinSize(360, 270);
        imageFrame.setMaxSize(360, 270);
        imageFrame.setStyle("-fx-background-color: linear-gradient(to bottom right, #60a5fa, #2563eb); "
                + "-fx-background-radius: 16;");
        if (hasPhoto) {
            imageFrame.getChildren().add(eventImage);
        } else {
            Label noPhoto = new Label("No event photo available");
            noPhoto.setTextFill(Color.WHITE);
            noPhoto.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
            imageFrame.getChildren().add(noPhoto);
        }

        Label eventName = new Label(ev[0]);
        eventName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 25));
        eventName.setTextFill(Color.web(NAVY_DEEP));
        eventName.setWrapText(true);

        Label status = new Label(ev[4]);
        status.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        status.setTextFill(Color.web(completed ? "#15803d" : "#166534"));
        status.setPadding(new Insets(6, 12, 6, 12));
        status.setStyle("-fx-background-color: " + (completed ? "#dcfce7" : "#dbeafe")
                + "; -fx-background-radius: 18;");

        Label aboutTitle = new Label("About This Event");
        aboutTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
        aboutTitle.setTextFill(Color.web(NAVY_DEEP));

        Label aboutText = new Label(
                "Event information recorded by the administrator. "
                        + "For additional details, update the event information in the system."
        );
        aboutText.setFont(Font.font("Segoe UI", 13));
        aboutText.setTextFill(Color.web(TEXT_MUTED));
        aboutText.setWrapText(true);

        VBox aboutBox = new VBox(10, aboutTitle, aboutText);
        aboutBox.setPadding(new Insets(16));
        aboutBox.setStyle("-fx-background-color: #f1f5ff; -fx-background-radius: 14;");

        Label participationTitle = new Label("Volunteer Participation");
        participationTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
        participationTitle.setTextFill(Color.web(NAVY_DEEP));

        Label joinedValue = new Label(joinedText(ev).replace(" joined", " volunteers joined"));
        joinedValue.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        joinedValue.setTextFill(Color.web(NAVY));

        // ----- who joined: the admin approves or rejects each registration (saved in PostgreSQL) -----
        final String eventId = ev.length > 7 && ev[7] != null ? ev[7] : "";
        VBox regList = new VBox(8);
        ScrollPane regScroll = new ScrollPane(regList);
        regScroll.setFitToWidth(true);
        regScroll.setPrefViewportHeight(200);
        regScroll.setMaxHeight(230);
        regScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        final Runnable[] loadRegs = new Runnable[1];
        loadRegs[0] = () -> {
            regList.getChildren().clear();
            List<String[]> regs = eventId.isBlank() ? new ArrayList<>() : EventStore.registrationsForEvent(eventId);
            if (regs.isEmpty()) {
                Label none = new Label(eventId.isBlank() ? "Registrations show up once the event is saved."
                        : "No one has joined yet.");
                none.setFont(Font.font("Segoe UI", 12));
                none.setTextFill(Color.web(TEXT_MUTED));
                regList.getChildren().add(none);
                return;
            }
            for (String[] r : regs) { // {id, name, email, contact, status, date}
                Label who = new Label(r[1].isBlank() ? r[2] : r[1]);
                who.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
                who.setTextFill(Color.web(NAVY_DEEP));
                Label sub = new Label(r[2] + (r[3].isBlank() ? "" : " \u2022 " + r[3]) + " \u2022 " + r[5]);
                sub.setFont(Font.font("Segoe UI", 11));
                sub.setTextFill(Color.web(TEXT_MUTED));
                sub.setWrapText(true);
                VBox texts = new VBox(2, who, sub);
                HBox.setHgrow(texts, Priority.ALWAYS);

                boolean approved = "Approved".equalsIgnoreCase(r[4]);
                boolean rejected = "Rejected".equalsIgnoreCase(r[4]);
                Label chip = new Label(r[4]);
                chip.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 3 9 3 9; "
                        + (approved ? "-fx-background-color: #dcfce7; -fx-text-fill: #16a34a;"
                        : rejected ? "-fx-background-color: #fee2e2; -fx-text-fill: #b91c1c;"
                        : "-fx-background-color: #ffedd5; -fx-text-fill: #b45309;"));

                HBox row = new HBox(8, texts, chip);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setPadding(new Insets(8, 10, 8, 12));
                row.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
                if (!approved) {
                    Button approve = flatAction("Approve", "#16a34a", "#15803d");
                    approve.setTextFill(Color.WHITE);
                    approve.setOnAction(e -> decideRegistration(r[0], "Approved", loadRegs[0]));
                    row.getChildren().add(approve);
                }
                if (!rejected) {
                    Button reject = flatAction("Reject", "#fee2e2", "#fecaca");
                    reject.setTextFill(Color.web("#b91c1c"));
                    reject.setOnAction(e -> decideRegistration(r[0], "Rejected", loadRegs[0]));
                    row.getChildren().add(reject);
                }
                regList.getChildren().add(row);
            }
        };
        loadRegs[0].run();

        VBox participationBox = new VBox(10, participationTitle, joinedValue, regScroll);
        if (ev.length > 8 && rowNumber(ev[8]) > 0) participationBox.getChildren().add(2, volunteerBar(ev));
        participationBox.setPadding(new Insets(16));
        participationBox.setStyle("-fx-background-color: #f1f5ff; -fx-background-radius: 14;");

        VBox left = new VBox(16, imageFrame, aboutBox);
        left.setPrefWidth(360);
        left.setMinWidth(360);

        VBox right = new VBox(12,
                eventName,
                status,
                new Separator(),
                cardInfo(ICON_CALENDAR, ev[1]),
                cardInfo(ICON_PIN, ev[2]),
                cardInfo(ICON_PEOPLE, joinedText(ev).replace(" joined", " volunteers joined")),
                badge("CATEGORY", ev[3]),
                participationBox
        );
        right.setPrefWidth(440);
        right.setFillWidth(true);

        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: white; -fx-text-fill: " + BLUE
                + "; -fx-font-weight: bold; -fx-background-radius: 10; "
                + "-fx-border-color: " + BLUE + "; -fx-border-radius: 10; -fx-padding: 10 24;");

        HBox footerButtons = new HBox(10);
        footerButtons.setAlignment(Pos.CENTER_RIGHT);

        if (!completed) {
            Button completeBtn = flatAction("\u2713 Mark as Completed", "#16a34a", "#15803d");
            completeBtn.setTextFill(Color.WHITE);
            completeBtn.setOnAction(e -> {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.initOwner(dialog);
                confirm.setTitle("Complete Event");
                confirm.setHeaderText("Mark this event as completed?");
                confirm.setContentText(ev[0]);
                confirm.showAndWait().ifPresent(answer -> {
                    if (answer == ButtonType.OK) {
                        activeEventList.remove(ev);
                        completedEventList.add(0, new String[]{
                                ev[0], ev[1], ev[2], ev[3], "Completed", ev[5],
                                ev.length > 6 ? ev[6] : "", ev.length > 7 ? ev[7] : ""
                        });
                        addNotification(ICON_CHECK, GREEN, GREEN_LIGHT,
                                "Event marked as Completed: " + ev[0]);
                        onChange.run();
                        dialog.close();
                    }
                });
            });

            Button cancelEventBtn = flatAction("\u2715 Cancel Event", "#fee2e2", "#fecaca");
            cancelEventBtn.setTextFill(Color.web("#b91c1c"));
            cancelEventBtn.setOnAction(e -> {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.initOwner(dialog);
                confirm.setTitle("Cancel Event");
                confirm.setHeaderText("Cancel this event?");
                confirm.setContentText("This will remove it from Active & Upcoming Events.");
                confirm.showAndWait().ifPresent(answer -> {
                    if (answer == ButtonType.OK) {
                        activeEventList.remove(ev);
                        addNotification(ICON_CANCEL, RED, RED_LIGHT,
                                "Event cancelled: " + ev[0]);
                        onChange.run();
                        dialog.close();
                    }
                });
            });
            footerButtons.getChildren().addAll(completeBtn, cancelEventBtn, closeBtn);
        } else {
            footerButtons.getChildren().add(closeBtn);
        }

        closeBtn.setOnAction(e -> dialog.close());

        HBox content = new HBox(24, left, right);
        content.setPadding(new Insets(24));
        content.setAlignment(Pos.TOP_LEFT);

        VBox root = new VBox(content, new Separator(), footerButtons);
        root.setPadding(new Insets(8, 18, 18, 18));
        root.setStyle("-fx-background-color: #ffffff; -fx-font-family: 'Segoe UI';");

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: white; -fx-background-color: white;");
        Scene scene = new Scene(scroll, 900, 680);
        dialog.setScene(scene);
        dialog.setMinWidth(760);
        dialog.setMinHeight(580);
        dialog.showAndWait();
    }

    /** Small muted icon + text row used inside event cards. */
    private HBox cardInfo(String iconPath, String text) {
        Group g = icon(iconPath, "#8b9bb4", 15);
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", 12.5));
        l.setTextFill(Color.web(TEXT_MUTED));
        HBox row = new HBox(8, g, l);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    /** Compact card action button. */
    private Button flatAction(String text, String bg, String hover) {
        Button b = new Button(text);
        b.setCursor(Cursor.HAND);
        b.setFocusTraversable(false);
        String base = "-fx-font-weight: bold; -fx-font-size: 12px; -fx-background-radius: 10; "
                + "-fx-padding: 7 14 7 14; -fx-cursor: hand;";
        b.setStyle("-fx-background-color: " + bg + ";" + base);
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: " + hover + ";" + base));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: " + bg + ";" + base));
        return b;
    }

    /** Empties the Add Event form. Set by buildAddEventPage. */
    private Runnable eventFormReset = () -> {};

    private void openEventForm() {
        eventFormReset.run();
        showPage("addevent");
    }

    /** The Add Event form (a page, like Add Donation): photo, name and location on the left, details on the right. */
    private ScrollPane buildAddEventPage() {
        VBox body = new VBox(20);
        body.setPadding(new Insets(28, 40, 28, 40));
        body.setAlignment(Pos.TOP_CENTER);

        // ----- title card -----
        Label titleLbl = new Label("Add Event");
        titleLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        titleLbl.setTextFill(Color.web(NAVY_DEEP));
        Label subLbl = new Label("Create a community event or program for the users: its name, photo, date, time and "
                + "location. What you save here shows on the user's Events page. Completed events go directly to "
                + "the Completed section.");
        subLbl.setFont(Font.font("Segoe UI", 13));
        subLbl.setTextFill(Color.web(TEXT_MUTED));
        subLbl.setWrapText(true);
        VBox titleTexts = new VBox(3, titleLbl, subLbl);
        titleTexts.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(titleTexts, Priority.ALWAYS);

        Button backBtn = new Button("\u2190 Back");
        String backBase = "-fx-background-color: " + NAVY + "; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-font-size: 13px; -fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 11 22 11 22;";
        backBtn.setStyle(backBase);
        backBtn.setMinWidth(Region.USE_PREF_SIZE);
        backBtn.setOnMouseEntered(e -> backBtn.setStyle(backBase.replace(NAVY, NAVY_DEEP)));
        backBtn.setOnMouseExited(e -> backBtn.setStyle(backBase));

        HBox titleRow = new HBox(14, titleTexts, backBtn);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        VBox titleCard = glassCard(titleRow);
        titleCard.setMaxWidth(1000);

        // ================= left column: photo + name + location =================
        final double PHOTO_H = 230;
        String[] photoPath = {""};

        StackPane photoBox = new StackPane();
        photoBox.setMinSize(0, PHOTO_H);
        photoBox.setPrefSize(10, PHOTO_H);
        photoBox.setMaxSize(Double.MAX_VALUE, PHOTO_H);
        photoBox.setStyle("-fx-background-color: #e8eefb; -fx-background-radius: 24;");
        Label noPhoto = new Label("No photo yet");
        noPhoto.setFont(Font.font("Segoe UI", 13));
        noPhoto.setTextFill(Color.web(TEXT_MUTED));
        ImageView photoView = new ImageView();
        photoView.setManaged(false);
        photoView.setSmooth(true);
        photoView.setFitHeight(PHOTO_H);
        photoView.fitWidthProperty().bind(photoBox.widthProperty());
        photoBox.getChildren().addAll(noPhoto, photoView);
        Rectangle photoClip = new Rectangle();
        photoClip.setArcWidth(24);
        photoClip.setArcHeight(24);
        photoClip.widthProperty().bind(photoBox.widthProperty());
        photoClip.setHeight(PHOTO_H);
        photoBox.setClip(photoClip);

        Runnable applyViewport = () -> {
            Image img = photoView.getImage();
            double bw = photoBox.getWidth();
            if (img == null || bw <= 0 || img.getHeight() <= 0) {
                photoView.setViewport(null);
                return;
            }
            double boxRatio = bw / PHOTO_H, imgRatio = img.getWidth() / img.getHeight();
            double vw = imgRatio > boxRatio ? img.getHeight() * boxRatio : img.getWidth();
            double vh = imgRatio > boxRatio ? img.getHeight() : img.getWidth() / boxRatio;
            photoView.setViewport(new javafx.geometry.Rectangle2D(
                    (img.getWidth() - vw) / 2, (img.getHeight() - vh) / 2, vw, vh));
        };
        photoBox.widthProperty().addListener((obs, o, w) -> applyViewport.run());

        Label photoName = new Label("No photo selected");
        photoName.setFont(Font.font("Segoe UI", 12));
        photoName.setTextFill(Color.web(TEXT_MUTED));

        Runnable refreshPhoto = () -> {
            Image img = null;
            try {
                if (!photoPath[0].isBlank() && new File(photoPath[0]).isFile()) {
                    img = new Image(new File(photoPath[0]).toURI().toString());
                    photoName.setText(new File(photoPath[0]).getName());
                } else {
                    photoName.setText("No photo selected");
                }
            } catch (Exception ex) {
                img = null;
            }
            photoView.setImage(img);
            noPhoto.setVisible(img == null);
            applyViewport.run();
        };

        Button choosePhotoBtn = flatAction("\uD83D\uDCF7 Choose Photo", BLUE_LIGHT, "#c7d9fb");
        choosePhotoBtn.setTextFill(Color.web(BLUE));
        Button removePhotoBtn = flatAction("Remove", "#fee2e2", "#fecaca");
        removePhotoBtn.setTextFill(Color.web("#b91c1c"));
        HBox photoActions = new HBox(10, choosePhotoBtn, removePhotoBtn, photoName);
        photoActions.setAlignment(Pos.CENTER_LEFT);

        TextField nameField = new TextField();
        nameField.setPromptText("e.g. Coastal Clean-Up Drive");
        styleInput(nameField);

        TextField locationField = new TextField();
        locationField.setPromptText("e.g. Brgy. San Antonio, Tuy");
        styleInput(locationField);

        VBox left = new VBox(12, photoBox, photoActions,
                spaceBefore(formLabel("Event Name")), nameField,
                spaceBefore(formLabel("Location")), locationField);
        left.setMinWidth(0);
        left.setPrefWidth(440);
        HBox.setHgrow(left, Priority.ALWAYS);

        // ================= right column: event details =================
        Label detTitle = new Label("Event Details");
        detTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 19));
        detTitle.setTextFill(Color.web(NAVY_DEEP));
        Label detSub = new Label("Set when the event happens, what kind it is and how many volunteers you need.");
        detSub.setFont(Font.font("Segoe UI", 13));
        detSub.setTextFill(Color.web(TEXT_MUTED));
        detSub.setWrapText(true);

        DatePicker datePicker = new DatePicker(LocalDate.now().plusDays(7));
        datePicker.setMaxWidth(Double.MAX_VALUE);
        styleInput(datePicker);

        List<String> times = timeOptions();
        ComboBox<String> fromBox = new ComboBox<>(FXCollections.observableArrayList(times));
        ComboBox<String> toBox = new ComboBox<>(FXCollections.observableArrayList(times));
        for (ComboBox<String> cb : java.util.List.of(fromBox, toBox)) {
            cb.setMaxWidth(Double.MAX_VALUE);
            cb.setVisibleRowCount(8);
            styleInput(cb);
        }
        VBox fromCol = new VBox(6, formLabel("Start Time"), fromBox);
        VBox toCol = new VBox(6, formLabel("End Time"), toBox);
        HBox.setHgrow(fromCol, Priority.ALWAYS);
        HBox.setHgrow(toCol, Priority.ALWAYS);
        HBox timeRow = new HBox(12, fromCol, toCol);

        ComboBox<String> typeBox = new ComboBox<>(FXCollections.observableArrayList(
                "Environment", "Health", "Relief", "Training", "Outreach"));
        typeBox.setMaxWidth(Double.MAX_VALUE);
        styleInput(typeBox);
        ComboBox<String> statusBox = new ComboBox<>(FXCollections.observableArrayList(
                "Upcoming", "Registration Open"));
        statusBox.setMaxWidth(Double.MAX_VALUE);
        styleInput(statusBox);
        VBox typeCol = new VBox(6, formLabel("Category / Type"), typeBox);
        VBox statusCol = new VBox(6, formLabel("Initial Status"), statusBox);
        HBox.setHgrow(typeCol, Priority.ALWAYS);
        HBox.setHgrow(statusCol, Priority.ALWAYS);
        HBox typeStatusRow = new HBox(12, typeCol, statusCol);

        TextField joinedField = new TextField("20");
        joinedField.setPromptText("e.g. 20");
        styleInput(joinedField);
        Label joinedHint = new Label("How many volunteers you need. Users can join until the event is full (e.g. 1 / 20 joined).");
        joinedHint.setFont(Font.font("Segoe UI", 11));
        joinedHint.setTextFill(Color.web(TEXT_MUTED));
        joinedHint.setWrapText(true);

        Label errorLabel = new Label(" ");
        errorLabel.setFont(Font.font("Segoe UI", 13));
        errorLabel.setWrapText(true);
        errorLabel.setTextFill(Color.web("#9b2c2c"));

        Button saveBtn = primaryButton("Save Event");
        saveBtn.setGraphic(icon(ICON_CALENDAR, "#ffffff", 17));
        saveBtn.setGraphicTextGap(8);
        saveBtn.setMaxWidth(Double.MAX_VALUE);

        VBox right = new VBox(12, detTitle, detSub,
                formLabel("Event Date"), datePicker,
                timeRow, typeStatusRow,
                formLabel("Volunteers Needed"), joinedField, joinedHint,
                errorLabel, saveBtn);
        right.setMinWidth(0);
        right.setPrefWidth(440);
        HBox.setHgrow(right, Priority.ALWAYS);

        Separator divider = new Separator(javafx.geometry.Orientation.VERTICAL);
        divider.setOpacity(0.5);
        HBox columns = new HBox(28, left, divider, right);
        columns.setAlignment(Pos.TOP_LEFT);

        VBox formCard = glassCard(columns);
        formCard.setMaxWidth(1000);
        formCard.setPadding(new Insets(28));

        // ================= behaviour =================
        eventFormReset = () -> {
            nameField.clear();
            locationField.clear();
            datePicker.setValue(LocalDate.now().plusDays(7));
            fromBox.setValue("8:00 AM");
            toBox.setValue("12:00 PM");
            typeBox.setValue("Environment");
            statusBox.setValue("Upcoming");
            joinedField.setText("20");
            photoPath[0] = "";
            errorLabel.setText(" ");
            refreshPhoto.run();
        };

        choosePhotoBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose a photo for this event");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                    "Images", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));
            File picked = chooser.showOpenDialog(stage);
            if (picked == null) return;
            Image test = new Image(picked.toURI().toString(), 208, 148, true, true, false);
            if (test.isError() || test.getWidth() <= 0) {
                errorLabel.setText("That file could not be read as an image.");
                return;
            }
            errorLabel.setText(" ");
            photoPath[0] = picked.getAbsolutePath();
            refreshPhoto.run();
        });
        removePhotoBtn.setOnAction(e -> {
            photoPath[0] = "";
            refreshPhoto.run();
        });

        backBtn.setOnAction(e -> {
            eventFormReset.run();
            showPage("events");
        });

        saveBtn.setOnAction(e -> {
            String evName = txt(nameField);
            String loc = txt(locationField);
            LocalDate pickedDate = datePicker.getValue();
            String from = fromBox.getValue() == null ? "" : fromBox.getValue();
            String to = toBox.getValue() == null ? "" : toBox.getValue();
            String needed = txt(joinedField);

            if (evName.isEmpty()) {
                errorLabel.setText("Please enter the event name.");
                return;
            }
            if (loc.isEmpty()) {
                errorLabel.setText("Please enter the event location.");
                return;
            }
            if (pickedDate == null) {
                errorLabel.setText("Please pick the event date.");
                return;
            }
            if (from.isEmpty() || to.isEmpty() || times.indexOf(from) >= times.indexOf(to)) {
                errorLabel.setText("Please set a start time that is earlier than the end time.");
                return;
            }
            try {
                if (Integer.parseInt(needed) < 1) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                errorLabel.setText("Volunteers Needed must be a whole number (1 or more).");
                return;
            }

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
            String dateTimeStr = pickedDate.format(fmt) + " / " + from + " - " + to;
            String typeVal = typeBox.getValue();
            String statusVal = statusBox.getValue();

            // Keep a copy of the chosen photo next to the app so the event keeps its image
            // even if the original file is moved or deleted later.
            String savedPhoto = photoPath[0].isEmpty() ? "" : copyEventPhoto(photoPath[0]);
            String[] newRow = new String[]{evName, dateTimeStr, loc, typeVal, statusVal, "0", savedPhoto, "", needed};
            if ("Completed".equalsIgnoreCase(statusVal)) {
                completedEventList.add(0, newRow);
            } else {
                activeEventList.add(0, newRow);
            }

            addNotification(ICON_CALENDAR, BLUE, BLUE_LIGHT,
                    "New event added: " + evName + " (" + statusVal + ")");
            refreshEventsCountersHook.run();
            rebuildEventGridsHook.run();
            eventFormReset.run();
            showPage("events");
        });

        eventFormReset.run();

        body.getChildren().addAll(titleCard, formCard);
        return wrapScroll(body);
    }

    private VBox formGroup(String labelText, Node field) {
        Label lbl = new Label(labelText);
        lbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12.5));
        lbl.setTextFill(Color.web(NAVY));
        VBox box = new VBox(5, lbl, field);
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    // ---------- Donations page (Separated: Pending Donations vs Approved / Processed Donations) ----------

    private String peso(double v) {
        return v == Math.rint(v) ? String.format(java.util.Locale.ENGLISH, "\u20B1%,.0f", v)
                : String.format(java.util.Locale.ENGLISH, "\u20B1%,.2f", v);
    }

    private double parseCashAmount(String text) {
        if (text == null) return 0;
        String cleaned = text.replaceAll("[^0-9.]", "");
        if (cleaned.isEmpty()) return 0;
        try {
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private ScrollPane buildDonationsPage() {
        VBox body = new VBox(22);
        body.setPadding(new Insets(28, 40, 28, 40));

        Button addBtn = primaryButton("+ Add Donation");
        addBtn.setOnAction(e -> openCampaignForm(null));
        body.getChildren().add(pageHeader("Donations",
                "Review pending donations and manage all approved / processed donations.", addBtn));

        Label[] totalDonOut = new Label[1];
        Label[] cashDonOut = new Label[1];
        Label[] pendingDonOut = new Label[1];
        Label[] goodsDonOut = new Label[1];

        ScrollPane[] donScrollRef = new ScrollPane[1];
        Node[] donPendingRef = new Node[1];
        Node[] donApprovedRef = new Node[1];

        VBox donCashStat = statCardBound(ICON_MONEY, ORANGE, "Total Approved Cash", "\u20B10", "Approved cash donations", totalDonOut);
        VBox donApprovedStat = statCardBound(ICON_CHECK, GREEN, "Approved Donations", "0", "All approved donations", cashDonOut);
        VBox donPendingStat = statCardBound(ICON_CLOCK, PURPLE, "Pending Approval", "0", "Awaiting admin decision", pendingDonOut);
        VBox donGoodsStat = statCardBound(ICON_BOX, BLUE, "In-Kind / Goods", "0 items", "Approved in-kind donations", goodsDonOut);

        makeClickableCard(donCashStat, "View approved donations", () -> scrollToNode(donScrollRef[0], donApprovedRef[0]));
        makeClickableCard(donApprovedStat, "View approved donations", () -> scrollToNode(donScrollRef[0], donApprovedRef[0]));
        makeClickableCard(donPendingStat, "Review pending donations", () -> scrollToNode(donScrollRef[0], donPendingRef[0]));
        makeClickableCard(donGoodsStat, "View approved donations", () -> scrollToNode(donScrollRef[0], donApprovedRef[0]));

        HBox stats = equalRow(donCashStat, donApprovedStat, donPendingStat, donGoodsStat);
        body.getChildren().add(stats);

        Label pendingCountBadge = new Label();
        pendingCountBadge.setStyle("-fx-background-color: #ffedd5; -fx-text-fill: #c2410c; -fx-font-weight: bold; "
                + "-fx-font-size: 12px; -fx-padding: 4 12 4 12; -fx-background-radius: 16;");

        Label approvedCountBadge = new Label();
        approvedCountBadge.setStyle("-fx-background-color: #dcfce7; -fx-text-fill: #15803d; -fx-font-weight: bold; "
                + "-fx-font-size: 12px; -fx-padding: 4 12 4 12; -fx-background-radius: 16;");

        Label cancelledCountBadge = new Label();
        cancelledCountBadge.setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #b91c1c; -fx-font-weight: bold; "
                + "-fx-font-size: 12px; -fx-padding: 4 12 4 12; -fx-background-radius: 16;");

        Runnable updateDonationCounters = () -> {
            double approvedCashSum = 0;
            int inKindCount = 0;
            for (String[] row : approvedDonationList) {
                if ("Cash".equalsIgnoreCase(row[2])) {
                    approvedCashSum += parseCashAmount(row[3]);
                } else {
                    inKindCount++;
                }
            }
            int pendCount = pendingDonationList.size();
            int appCount = approvedDonationList.size();

            if (totalDonOut[0] != null) totalDonOut[0].setText(peso(approvedCashSum));
            if (cashDonOut[0] != null) cashDonOut[0].setText(String.valueOf(appCount));
            if (pendingDonOut[0] != null) pendingDonOut[0].setText(String.valueOf(pendCount));
            if (goodsDonOut[0] != null) goodsDonOut[0].setText(inKindCount + " records");

            pendingCountBadge.setText(pendCount + " Pending");
            approvedCountBadge.setText(appCount + " Approved");
            cancelledCountBadge.setText(cancelledDonationList.size() + " Cancelled");
            refreshHomeHook.run();
            refreshRecordsHook.run();
        };

        final double cardW = 330;
        FlowPane pendingGrid = new FlowPane(20, 20);
        pendingGrid.setPrefWrapLength(4 * cardW);
        pendingGrid.setAlignment(Pos.TOP_LEFT);

        // approved donations sit in ONE row that is swiped (drag, arrows or sideways scroll) once it is full
        HBox approvedGrid = new HBox(20);
        approvedGrid.setAlignment(Pos.TOP_LEFT);
        approvedGrid.setPadding(new Insets(4, 8, 24, 8));
        Button approvedPrev = swipeArrow("\u2039");
        Button approvedNext = swipeArrow("\u203A");
        StackPane approvedView = swipeViewport(approvedGrid, cardW + 20, approvedPrev, approvedNext);

        FlowPane cancelledGrid = new FlowPane(20, 20);
        cancelledGrid.setPrefWrapLength(4 * cardW);
        cancelledGrid.setAlignment(Pos.TOP_LEFT);

        final Runnable[] rebuildPending = new Runnable[1];
        final Runnable[] rebuildApproved = new Runnable[1];
        final Runnable[] rebuildCancelled = new Runnable[1];

        Runnable refreshAll = () -> {
            updateDonationCounters.run();
            rebuildPending[0].run();
            rebuildApproved[0].run();
            rebuildCancelled[0].run();
        };

        refreshDonationsHook = refreshAll;

        rebuildPending[0] = () -> {
            pendingGrid.getChildren().clear();
            for (String[] d : pendingDonationList) {
                pendingGrid.getChildren().add(donationCard(d, true, cardW, refreshAll));
            }
            if (pendingGrid.getChildren().isEmpty()) {
                pendingGrid.getChildren().add(emptyNote("No pending donations awaiting approval."));
            }
        };

        rebuildApproved[0] = () -> {
            approvedGrid.getChildren().clear();
            for (String[] d : approvedDonationList) {
                approvedGrid.getChildren().add(donationCard(d, false, cardW, refreshAll));
            }
            if (approvedGrid.getChildren().isEmpty()) {
                approvedGrid.getChildren().add(emptyNote("No approved donations yet."));
            }
        };

        rebuildCancelled[0] = () -> {
            cancelledGrid.getChildren().clear();
            for (String[] d : cancelledDonationList) {
                cancelledGrid.getChildren().add(donationCard(d, false, cardW, refreshAll));
            }
            if (cancelledGrid.getChildren().isEmpty()) {
                cancelledGrid.getChildren().add(emptyNote("No cancelled donations yet."));
            }
        };

        // 1. PENDING DONATIONS - Approve or Cancel
        Label pendingTitle = sectionTitle("Pending Donations", ICON_CLOCK);
        Label pendingSub = new Label("Verify and approve incoming donations before moving them to processed records.");
        pendingSub.setFont(Font.font("Segoe UI", 12.5));
        pendingSub.setTextFill(Color.web(TEXT_MUTED));
        HBox pendingHeader = new HBox(12, new VBox(2, pendingTitle, pendingSub), spacer(), pendingCountBadge);
        pendingHeader.setAlignment(Pos.CENTER_LEFT);

        VBox pendingCard = glassCard(new VBox(16, pendingHeader, pendingGrid));

        // 2. APPROVED / PROCESSED DONATIONS - cards
        Label approvedTitle = sectionTitle("Approved & Processed Donations", ICON_CHECK);
        Label approvedSub = new Label("Official donations that have been verified and approved by the administrator.");
        approvedSub.setFont(Font.font("Segoe UI", 12.5));
        approvedSub.setTextFill(Color.web(TEXT_MUTED));
        HBox approvedHeader = new HBox(10, new VBox(2, approvedTitle, approvedSub), spacer(),
                approvedPrev, approvedNext, approvedCountBadge);
        approvedHeader.setAlignment(Pos.CENTER_LEFT);

        VBox approvedCard = glassCard(new VBox(16, approvedHeader, approvedView));

        Label cancelledTitle = sectionTitle("Cancelled Donations", ICON_CANCEL);
        Label cancelledSub = new Label("These donations have been cancelled and are no longer active.");
        cancelledSub.setFont(Font.font("Segoe UI", 12.5));
        cancelledSub.setTextFill(Color.web(TEXT_MUTED));
        HBox cancelledHeader = new HBox(12, new VBox(2, cancelledTitle, cancelledSub), spacer(), cancelledCountBadge);
        cancelledHeader.setAlignment(Pos.CENTER_LEFT);
        VBox cancelledCard = glassCard(new VBox(16, cancelledHeader, cancelledGrid));

        updateDonationCounters.run();
        rebuildPending[0].run();
        rebuildApproved[0].run();
        rebuildCancelled[0].run();

        // 0. DONATIONS THE ADMIN SET UP (what the users can donate to)
        Label campTitle = sectionTitle("Donations Open to Users", ICON_HEART);
        Label campSub = new Label("The donations you set up. Users see the active ones on their Donations page.");
        campSub.setFont(Font.font("Segoe UI", 12.5));
        campSub.setTextFill(Color.web(TEXT_MUTED));
        Label campCountBadge = new Label();
        campCountBadge.setStyle("-fx-background-color: #dbeafe; -fx-text-fill: #1d4ed8; -fx-font-weight: bold; "
                + "-fx-font-size: 12px; -fx-padding: 4 12 4 12; -fx-background-radius: 16;");
        HBox campHeader = new HBox(12, new VBox(2, campTitle, campSub), spacer(), campCountBadge);
        campHeader.setAlignment(Pos.CENTER_LEFT);
        FlowPane campaignGrid = new FlowPane(20, 20);
        campaignGrid.setPrefWrapLength(4 * cardW);
        campaignGrid.setAlignment(Pos.TOP_LEFT);
        // category chips: All, Cash, Education, Relief Goods, Clothes, General Goods, Others
        String[] campCategory = {"All"};
        List<Button> campChips = new ArrayList<>();
        FlowPane campChipRow = new FlowPane(10, 8);
        campChipRow.setAlignment(Pos.CENTER_LEFT);
        VBox campaignsSection = glassCard(new VBox(16, campHeader, campChipRow, campaignGrid));

        rebuildCampaignGridHook = () -> {
            campaignGrid.getChildren().clear();
            List<CampaignStore.Campaign> everything = CampaignStore.all();
            List<CampaignStore.Campaign> all = new ArrayList<>();
            for (CampaignStore.Campaign c : everything) {
                if ("All".equals(campCategory[0]) || campCategory[0].equalsIgnoreCase(c.category)) all.add(c);
            }
            campCountBadge.setText(all.size() + (all.size() == 1 ? " Donation" : " Donations"));
            for (CampaignStore.Campaign c : all) {
                campaignGrid.getChildren().add(campaignCard(c, cardW, rebuildCampaignGridHook));
            }
            if (everything.isEmpty()) {
                campaignGrid.getChildren().add(emptyNote("No donations yet. Click \"+ Add Donation\" to set one up for the users."));
            } else if (all.isEmpty()) {
                campaignGrid.getChildren().add(emptyNote("No donations in this category yet."));
            }
        };
        for (String cat : new String[]{"All", "Cash", "Education", "Relief Goods", "Clothes", "General Goods", "Others"}) {
            Button chip = new Button(cat);
            chip.setCursor(Cursor.HAND);
            chip.setFocusTraversable(false);
            styleFilterTab(chip, "All".equals(cat));
            chip.setOnAction(e -> {
                campCategory[0] = cat;
                for (Button b : campChips) styleFilterTab(b, b.getText().equals(campCategory[0]));
                rebuildCampaignGridHook.run();
            });
            campChips.add(chip);
            campChipRow.getChildren().add(chip);
        }
        rebuildCampaignGridHook.run();

        body.getChildren().addAll(campaignsSection, pendingCard, approvedCard, cancelledCard);
        donPendingRef[0] = pendingCard;
        donApprovedRef[0] = approvedCard;
        ScrollPane donScroll = wrapScroll(body);
        donScrollRef[0] = donScroll;
        return donScroll;
    }

    /** The campaign photo (cached), or null when there is none or the file is missing. */
    private Image donationPhoto(String path) {
        if (path == null || path.isBlank()) return null;
        Image cached = donationPhotoCache.get(path);
        if (cached != null) return cached;
        File f = new File(path);
        if (!f.isFile()) {
            // the photo was saved with a path from another folder: look in ./campaign_photos by file name
            f = new File(System.getProperty("user.dir"), "campaign_photos" + File.separator + f.getName());
        }
        if (!f.isFile()) return null;
        try {
            Image img = new Image(f.toURI().toString(), 760, 0, true, true);
            if (img.isError()) return null;
            donationPhotoCache.put(path, img);
            return img;
        } catch (RuntimeException e) {
            return null;
        }
    }

    /** An image that fills w x h like a cover photo (centered, cropped, not stretched). */
    private ImageView coverImage(Image img, double w, double h) {
        ImageView iv = new ImageView(img);
        iv.setSmooth(true);
        iv.setPreserveRatio(false);
        iv.setFitWidth(w);
        iv.setFitHeight(h);
        double iw = img.getWidth(), ih = img.getHeight();
        if (iw > 0 && ih > 0) {
            double scale = Math.max(w / iw, h / ih);
            double vw = w / scale, vh = h / scale;
            iv.setViewport(new javafx.geometry.Rectangle2D((iw - vw) / 2, (ih - vh) / 2, vw, vh));
        }
        return iv;
    }

    private Button swipeArrow(String glyph) {
        Button b = new Button(glyph);
        b.setCursor(Cursor.HAND);
        b.setFocusTraversable(false);
        b.setMinSize(32, 32);
        b.setMaxSize(32, 32);
        b.setStyle("-fx-background-color: white; -fx-text-fill: " + NAVY + "; -fx-font-size: 18px; -fx-font-weight: bold; "
                + "-fx-background-radius: 16; -fx-border-color: #d3dcf0; -fx-border-radius: 16; -fx-padding: 0 0 2 0;");
        return b;
    }

    /**
     * Shows the cards of {@code track} in one row inside a clipped view. When there are more cards than
     * fit, the row can be swiped: drag it, use the arrow buttons, or scroll sideways. The arrows only
     * show (and are only enabled) when there is something to swipe to.
     */
    private StackPane swipeViewport(HBox track, double step, Button prev, Button next) {
        StackPane view = new StackPane(track);
        view.setAlignment(Pos.TOP_LEFT);
        view.setMinWidth(0);    // the row may be wider than the page; it must never stretch the page
        view.setPrefWidth(0);
        track.setMaxWidth(Region.USE_PREF_SIZE);   // keep the full width of all cards, do not squeeze them
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(view.widthProperty());
        clip.heightProperty().bind(view.heightProperty());
        view.setClip(clip);

        Timeline[] anim = new Timeline[1];
        java.util.function.DoubleUnaryOperator clamp =
                x -> Math.max(Math.min(0, view.getWidth() - track.getWidth()), Math.min(0, x));
        Runnable refreshArrows = () -> {
            boolean overflow = track.getWidth() > view.getWidth() + 1;
            for (Button b : new Button[]{prev, next}) {
                b.setVisible(overflow);
                b.setManaged(overflow);
            }
            prev.setDisable(track.getTranslateX() >= -0.5);
            next.setDisable(track.getTranslateX() <= Math.min(0, view.getWidth() - track.getWidth()) + 0.5);
        };
        java.util.function.DoubleConsumer slideTo = target -> {
            if (anim[0] != null) anim[0].stop();
            anim[0] = new Timeline(new KeyFrame(Duration.millis(260),
                    new KeyValue(track.translateXProperty(), clamp.applyAsDouble(target), Interpolator.EASE_BOTH)));
            anim[0].play();
        };
        // whole cards that fit in the view move at a time
        java.util.function.DoubleSupplier page = () -> Math.max(1, Math.floor(view.getWidth() / step)) * step;
        prev.setOnAction(e -> slideTo.accept(track.getTranslateX() + page.getAsDouble()));
        next.setOnAction(e -> slideTo.accept(track.getTranslateX() - page.getAsDouble()));

        double[] drag = new double[2];   // mouse x at press, track x at press
        view.setOnMousePressed(e -> {
            if (anim[0] != null) anim[0].stop();
            drag[0] = e.getSceneX();
            drag[1] = track.getTranslateX();
        });
        view.setOnMouseDragged(e -> {
            track.setTranslateX(clamp.applyAsDouble(drag[1] + e.getSceneX() - drag[0]));
            view.setCursor(Cursor.CLOSED_HAND);
        });
        view.setOnMouseReleased(e -> view.setCursor(Cursor.DEFAULT));
        // sideways scroll (touchpad or Shift + wheel); a plain wheel still scrolls the page
        view.addEventFilter(javafx.scene.input.ScrollEvent.SCROLL, e -> {
            double dx = e.getDeltaX() != 0 ? e.getDeltaX() : (e.isShiftDown() ? e.getDeltaY() : 0);
            if (dx != 0) {
                track.setTranslateX(clamp.applyAsDouble(track.getTranslateX() + dx));
                e.consume();
            }
        });

        track.translateXProperty().addListener((o, a, b) -> refreshArrows.run());
        Runnable relayout = () -> {
            track.setTranslateX(clamp.applyAsDouble(track.getTranslateX()));
            refreshArrows.run();
        };
        view.widthProperty().addListener((o, a, b) -> relayout.run());
        track.widthProperty().addListener((o, a, b) -> relayout.run());
        refreshArrows.run();
        return view;
    }

    /**
     * One donation tile: coloured banner with the type icon and the donation ID,
     * then donor, amount / items, date, type + status badges and the actions.
     */
    private StackPane donationCard(String[] d, boolean pending, double cardWidth, Runnable onChange) {
        boolean cash = d[2] != null && d[2].equalsIgnoreCase("Cash");

        StackPane photo = new StackPane();
        photo.setMinHeight(150);
        photo.setPrefHeight(150);
        photo.setMaxHeight(150);
        String[] grad = cash
                ? new String[]{"#22c55e", "#0d9488"}
                : new String[]{"#a78bfa", "#6d28d9"};
        photo.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web(grad[0])), new Stop(1, Color.web(grad[1]))),
                CornerRadii.EMPTY, Insets.EMPTY)));

        Group glyph = icon(cash ? ICON_MONEY : ICON_BOX, "#ffffff", 44);
        glyph.setOpacity(0.6);
        photo.getChildren().add(glyph);

        // the photo of the donation (campaign) this was given to, and the donor's full name
        String[] extra = d.length > 6 && d[6] != null ? donationDetails.get(d[6]) : null;
        Image campaignImg = extra == null ? null : donationPhoto(extra[2]);
        if (campaignImg != null) {
            photo.getChildren().remove(glyph);
            photo.getChildren().add(0, coverImage(campaignImg, cardWidth, 150));
        }
        if (extra != null && !extra[1].isBlank()) {
            Label forLbl = new Label(extra[1]);
            forLbl.setMaxWidth(cardWidth - 24);
            forLbl.setStyle("-fx-background-color: rgba(15,23,42,0.62); -fx-text-fill: white; "
                    + "-fx-font-weight: bold; -fx-font-size: 11px; -fx-padding: 3 10 3 10; -fx-background-radius: 12;");
            StackPane.setAlignment(forLbl, Pos.BOTTOM_LEFT);
            StackPane.setMargin(forLbl, new Insets(12));
            photo.getChildren().add(forLbl);
        }

        Label idChip = new Label(d.length > 6 && d[6] != null && !d[6].isBlank() ? d[6] : "D-" + d[0]);
        idChip.setStyle("-fx-background-color: rgba(255,255,255,0.88); -fx-text-fill: " + NAVY + "; "
                + "-fx-font-weight: bold; -fx-font-size: 11px; -fx-padding: 3 10 3 10; -fx-background-radius: 12;");
        StackPane.setAlignment(idChip, Pos.TOP_LEFT);
        StackPane.setMargin(idChip, new Insets(12));
        photo.getChildren().add(idChip);

        Label donor = new Label(extra != null && !extra[0].isBlank() ? extra[0] : d[1]);
        donor.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15.5));
        donor.setTextFill(Color.web(NAVY_DEEP));
        donor.setWrapText(true);

        Label amount = new Label(d[3]);
        amount.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
        amount.setTextFill(Color.web(NAVY_DEEP));
        amount.setWrapText(true);

        HBox dateRow = cardInfo(ICON_CLOCK, d[4]);

        HBox badgeRow = new HBox(8, badge("TYPE", d[2]), badge("STATUS", d[5]));
        badgeRow.setAlignment(Pos.CENTER_LEFT);

        VBox body = new VBox(9, donor, amount, dateRow, badgeRow);
        body.setPadding(new Insets(16, 18, 16, 18));

        if (pending) {
            Button approveBtn = flatAction("\u2713 Approve", "#16a34a", "#15803d");
            approveBtn.setTextFill(Color.WHITE);
            Button cancelBtn = flatAction("\u2715 Cancel", "#fee2e2", "#fecaca");
            cancelBtn.setTextFill(Color.web("#b91c1c"));

            approveBtn.setOnAction(e -> {
                if (d.length > 6 && d[6] != null && !d[6].isBlank()) {
                    DonationStore.updateStatus(d[6], "Processed");
                }
                pendingDonationList.remove(d);
                approvedDonationList.add(0, new String[]{d[0], d[1], d[2], d[3], d[4], "Processed", d.length > 6 ? d[6] : ""});
                addNotification(ICON_CHECK, GREEN, GREEN_LIGHT,
                        "Donation #" + d[0] + " approved: " + d[3] + " (" + d[1] + ")");
                onChange.run();
            });
            cancelBtn.setOnAction(e -> {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Cancel Donation");
                confirm.setHeaderText("Cancel this donation?");
                confirm.setContentText("Are you sure you want to cancel donation #" + d[0] + " from " + d[1] + "?");
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        if (d.length > 6 && d[6] != null && !d[6].isBlank()) {
                            DonationStore.updateStatus(d[6], "Cancelled");
                        }
                        pendingDonationList.remove(d);
                        cancelledDonationList.add(0, new String[]{d[0], d[1], d[2], d[3], d[4], "Cancelled", d.length > 6 ? d[6] : ""});
                        addNotification(ICON_CANCEL, RED, RED_LIGHT,
                                "Donation #" + d[0] + " cancelled: " + d[3] + " (" + d[1] + ")");
                        onChange.run();
                    }
                });
            });

            HBox actions = new HBox(8, approveBtn, cancelBtn);
            actions.setAlignment(Pos.CENTER_LEFT);
            body.getChildren().add(actions);
        }

        VBox card = new VBox(photo, body);
        card.setPrefWidth(cardWidth);
        card.setMinWidth(cardWidth);
        card.setMaxWidth(cardWidth);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 18; "
                + "-fx-border-color: #e6ecf6; -fx-border-radius: 18;");

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(card.widthProperty());
        clip.heightProperty().bind(card.heightProperty());
        clip.setArcWidth(36);
        clip.setArcHeight(36);
        card.setClip(clip);

        StackPane holder = new StackPane(card);
        holder.setEffect(new DropShadow(20, Color.rgb(20, 40, 90, 0.13)));
        return holder;
    }

    // ---------- Add / Edit Donation page (the admin sets up a donation; users see it) ----------

    private static final String ICON_TICK = "M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z";

    /** Fills the form with a donation to edit, or with an empty form when null. Set by buildAddDonationPage. */
    private java.util.function.Consumer<CampaignStore.Campaign> campaignFormLoader = c -> {};
    /** Redraws the "Donations Open to Users" cards. Set by buildDonationsPage. */
    private Runnable rebuildCampaignGridHook = () -> {};

    private void openCampaignForm(CampaignStore.Campaign c) {
        campaignFormLoader.accept(c);
        showPage("adddonation");
    }

    /** Re-reads the donations table and redraws the Donations page and the Home / Records numbers. */
    private void reloadDonationsFromDb() {
        pendingDonationList.clear();
        approvedDonationList.clear();
        cancelledDonationList.clear();
        seedDonationsData();
        refreshDonationsHook.run();
        rebuildCampaignGridHook.run();
    }

    private static String txt(TextInputControl t) {
        return t.getText() == null ? "" : t.getText().trim();
    }

    /** 5:00 AM, 5:30 AM ... 10:00 PM */
    private static List<String> timeOptions() {
        List<String> out = new ArrayList<>();
        for (int m = 5 * 60; m <= 22 * 60; m += 30) {
            int h = m / 60, min = m % 60;
            int h12 = h % 12 == 0 ? 12 : h % 12;
            out.add(h12 + ":" + (min == 0 ? "00" : "30") + (h < 12 ? " AM" : " PM"));
        }
        return out;
    }

    private ScrollPane buildAddDonationPage() {
        VBox body = new VBox(20);
        body.setPadding(new Insets(28, 40, 28, 40));
        body.setAlignment(Pos.TOP_CENTER);

        // ----- title card (changes between Add and Edit) -----
        Label titleLbl = new Label("Add Donation");
        titleLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        titleLbl.setTextFill(Color.web(NAVY_DEEP));
        Label subLbl = new Label("Set up a donation for the users: choose Cash or In-Kind, then add its name, photo "
                + "and details. What you save here shows on the user's Donations page.");
        subLbl.setFont(Font.font("Segoe UI", 13));
        subLbl.setTextFill(Color.web(TEXT_MUTED));
        subLbl.setWrapText(true);
        VBox titleTexts = new VBox(3, titleLbl, subLbl);
        titleTexts.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(titleTexts, Priority.ALWAYS);

        Button backBtn = new Button("\u2190 Back");
        String backBase = "-fx-background-color: " + NAVY + "; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-font-size: 13px; -fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 11 22 11 22;";
        backBtn.setStyle(backBase);
        backBtn.setMinWidth(Region.USE_PREF_SIZE);
        backBtn.setOnMouseEntered(e -> backBtn.setStyle(backBase.replace(NAVY, NAVY_DEEP)));
        backBtn.setOnMouseExited(e -> backBtn.setStyle(backBase));

        HBox titleRow = new HBox(14, titleTexts, backBtn);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        VBox titleCard = glassCard(titleRow);
        titleCard.setMaxWidth(1000);

        // ================= left column: photo + name + description + goal =================
        final double PHOTO_H = 230;
        String[] photoPath = {""};
        boolean[] photoChanged = {false};

        StackPane photoBox = new StackPane();
        photoBox.setMinSize(0, PHOTO_H);
        photoBox.setPrefSize(10, PHOTO_H);
        photoBox.setMaxSize(Double.MAX_VALUE, PHOTO_H);
        photoBox.setStyle("-fx-background-color: #e8eefb; -fx-background-radius: 24;");
        ImageView photoView = new ImageView();
        photoView.setManaged(false);
        photoView.setSmooth(true);
        photoView.setFitHeight(PHOTO_H);
        photoView.fitWidthProperty().bind(photoBox.widthProperty());
        Label noPhoto = new Label("No photo yet");
        noPhoto.setFont(Font.font("Segoe UI", 13));
        noPhoto.setTextFill(Color.web(TEXT_MUTED));
        photoBox.getChildren().addAll(noPhoto, photoView);
        Rectangle photoClip = new Rectangle();
        photoClip.setArcWidth(24);
        photoClip.setArcHeight(24);
        photoClip.widthProperty().bind(photoBox.widthProperty());
        photoClip.setHeight(PHOTO_H);
        photoBox.setClip(photoClip);

        Runnable applyViewport = () -> {
            Image img = photoView.getImage();
            double bw = photoBox.getWidth();
            if (img == null || bw <= 0 || img.getHeight() <= 0) {
                photoView.setViewport(null);
                return;
            }
            double boxRatio = bw / PHOTO_H, imgRatio = img.getWidth() / img.getHeight();
            double vw = imgRatio > boxRatio ? img.getHeight() * boxRatio : img.getWidth();
            double vh = imgRatio > boxRatio ? img.getHeight() : img.getWidth() / boxRatio;
            photoView.setViewport(new javafx.geometry.Rectangle2D(
                    (img.getWidth() - vw) / 2, (img.getHeight() - vh) / 2, vw, vh));
        };
        photoBox.widthProperty().addListener((obs, o, w) -> applyViewport.run());

        Label photoName = new Label("No photo selected");
        photoName.setFont(Font.font("Segoe UI", 12));
        photoName.setTextFill(Color.web(TEXT_MUTED));

        Runnable refreshPhoto = () -> {
            Image img = null;
            try {
                if (!photoPath[0].isBlank() && new File(photoPath[0]).isFile()) {
                    img = new Image(new File(photoPath[0]).toURI().toString());
                    photoName.setText(new File(photoPath[0]).getName());
                } else {
                    photoName.setText("No photo selected");
                }
            } catch (Exception ex) {
                img = null;
            }
            photoView.setImage(img);
            noPhoto.setVisible(img == null);
            applyViewport.run();
        };

        Button choosePhotoBtn = flatAction("\uD83D\uDCF7 Choose Photo", BLUE_LIGHT, "#c7d9fb");
        choosePhotoBtn.setTextFill(Color.web(BLUE));
        Button removePhotoBtn = flatAction("Remove", "#fee2e2", "#fecaca");
        removePhotoBtn.setTextFill(Color.web("#b91c1c"));
        HBox photoActions = new HBox(10, choosePhotoBtn, removePhotoBtn, photoName);
        photoActions.setAlignment(Pos.CENTER_LEFT);

        TextField nameField = new TextField();
        nameField.setPromptText("Enter donation name (e.g. Typhoon Relief & Community Fund)");
        styleInput(nameField);

        TextArea descArea = new TextArea();
        descArea.setPromptText("Tell users what this donation is for...");
        descArea.setWrapText(true);
        descArea.setPrefRowCount(3);
        descArea.setPrefHeight(90);
        descArea.setStyle("-fx-control-inner-background: #f9fafb; -fx-background-radius: 10; "
                + "-fx-border-color: #d1d5db; -fx-border-radius: 10; -fx-font-size: 13px;");

        TextField goalField = new TextField();
        goalField.setPromptText("Enter how much we need to raise...");
        goalField.setPrefHeight(44);
        goalField.setStyle("-fx-background-color: #f9fafb; -fx-background-radius: 0 10 10 0; -fx-border-color: #d1d5db; "
                + "-fx-border-radius: 0 10 10 0; -fx-font-size: 14px; -fx-padding: 0 12 0 12;");
        HBox.setHgrow(goalField, Priority.ALWAYS);
        Label peso = new Label("\u20B1");
        peso.setAlignment(Pos.CENTER);
        peso.setMinWidth(46);
        peso.setPrefHeight(44);
        peso.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        peso.setTextFill(Color.web(NAVY));
        peso.setStyle("-fx-background-color: #eef2ff; -fx-background-radius: 10 0 0 10; -fx-border-color: #d1d5db; "
                + "-fx-border-radius: 10 0 0 10; -fx-border-width: 1 0 1 1;");
        HBox goalRow = new HBox(peso, goalField);

        // the category decides under which chip the users find this donation on their Donations page
        ComboBox<String> categoryBox = new ComboBox<>(FXCollections.observableArrayList(CampaignStore.CATEGORIES));
        categoryBox.setValue(CampaignStore.CATEGORIES[0]);
        categoryBox.setMaxWidth(Double.MAX_VALUE);
        styleInput(categoryBox);

        Label goalLabel = spaceBefore(formLabel("Goal Amount (\u20B1)"));
        VBox left = new VBox(12, photoBox, photoActions,
                spaceBefore(formLabel("Donation Name")), nameField,
                spaceBefore(formLabel("Category")), categoryBox,
                spaceBefore(formLabel("Description")), descArea,
                goalLabel, goalRow);
        left.setMinWidth(0);
        left.setPrefWidth(440);
        HBox.setHgrow(left, Priority.ALWAYS);

        // ================= right column: type + payment method / items =================
        Label typeTitle = new Label("Donation Type");
        typeTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 19));
        typeTitle.setTextFill(Color.web(NAVY_DEEP));
        Label typeSub = new Label("Choose what donors will give for this donation.");
        typeSub.setFont(Font.font("Segoe UI", 13));
        typeSub.setTextFill(Color.web(TEXT_MUTED));
        ToggleGroup typeGroup = new ToggleGroup();
        ToggleButton cashTypeBtn = new ToggleButton();
        ToggleButton kindTypeBtn = new ToggleButton();
        cashTypeBtn.setToggleGroup(typeGroup);
        kindTypeBtn.setToggleGroup(typeGroup);
        cashTypeBtn.setSelected(true);
        typeGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            if (newT == null && oldT != null) oldT.setSelected(true);
        });
        StackPane cashTypeOption = paymentOption(cashTypeBtn, icon(ICON_MONEY, BLUE, 32), "Cash");
        StackPane kindTypeOption = paymentOption(kindTypeBtn, icon(ICON_BOX, BLUE, 32), "In-Kind");
        HBox typeRow = new HBox(12, cashTypeOption, kindTypeOption);
        HBox.setHgrow(cashTypeOption, Priority.ALWAYS);
        HBox.setHgrow(kindTypeOption, Priority.ALWAYS);

        Label pmTitle = new Label("Payment Method");
        pmTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 19));
        pmTitle.setTextFill(Color.web(NAVY_DEEP));
        Label pmSub = new Label("Set up both ways donors can give. Click one to fill in its details.");
        pmSub.setFont(Font.font("Segoe UI", 13));
        pmSub.setTextFill(Color.web(TEXT_MUTED));
        pmSub.setWrapText(true);

        ToggleGroup group = new ToggleGroup();
        ToggleButton gcashBtn = new ToggleButton();
        ToggleButton walkBtn = new ToggleButton();
        gcashBtn.setToggleGroup(group);
        walkBtn.setToggleGroup(group);
        gcashBtn.setSelected(true);
        group.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            if (newT == null && oldT != null) oldT.setSelected(true);
        });

        Label gMark = new Label("G");
        gMark.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 17px;");
        StackPane gcashIcon = new StackPane(new Circle(17, Color.web("#0a6cf0")), gMark);
        gcashIcon.setMaxSize(34, 34);
        StackPane gcashOption = paymentOption(gcashBtn, gcashIcon, "Through GCash");
        StackPane walkOption = paymentOption(walkBtn, icon(ICON_MONEY, BLUE, 32), "Walk-in");
        HBox choiceRow = new HBox(12, gcashOption, walkOption);
        HBox.setHgrow(gcashOption, Priority.ALWAYS);
        HBox.setHgrow(walkOption, Priority.ALWAYS);

        // ----- GCash details (the admin types the real account) -----
        Label gcTitle = new Label("GCash Payment Details");
        gcTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        gcTitle.setTextFill(Color.web(NAVY_DEEP));
        Label gcHelp = new Label("Donors will send their donation to this GCash account, then enter the reference number from their receipt.");
        gcHelp.setWrapText(true);
        gcHelp.setMinHeight(Region.USE_PREF_SIZE);
        gcHelp.setFont(Font.font("Segoe UI", 13));
        gcHelp.setTextFill(Color.web(TEXT_MUTED));
        TextField gcName = new TextField();
        gcName.setPromptText("Name on the GCash account");
        styleInput(gcName);
        TextField gcNumber = new TextField();
        gcNumber.setPromptText("e.g. 0917 123 4567");
        styleInput(gcNumber);
        VBox gcashPanel = new VBox(10, gcTitle, gcHelp,
                formLabel("GCash Account Name"), gcName,
                formLabel("GCash Number"), gcNumber);
        gcashPanel.setStyle("-fx-background-color: #eef4ff; -fx-background-radius: 14; "
                + "-fx-border-color: #bcd1f7; -fx-border-radius: 14; -fx-padding: 16 18 16 18;");

        // ----- Walk-in details (location + days + time, all typed by the admin) -----
        Label wkTitle = new Label("Walk-in Donation");
        wkTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        wkTitle.setTextFill(Color.web(NAVY_DEEP));
        Label wkHelp = new Label("Donors bring their cash to this drop-off location during the hours you set.");
        wkHelp.setWrapText(true);
        wkHelp.setMinHeight(Region.USE_PREF_SIZE);
        wkHelp.setFont(Font.font("Segoe UI", 13));
        wkHelp.setTextFill(Color.web(TEXT_MUTED));
        TextField wkLocation = new TextField();
        wkLocation.setPromptText("e.g. VoNation Center, Balayan, Batangas");
        styleInput(wkLocation);

        ComboBox<String> daysBox = new ComboBox<>(FXCollections.observableArrayList(
                "Mon\u2013Fri", "Mon\u2013Sat", "Mon\u2013Sun", "Sat\u2013Sun"));
        daysBox.setEditable(true);
        daysBox.setMaxWidth(Double.MAX_VALUE);
        styleInput(daysBox);

        List<String> times = timeOptions();
        ComboBox<String> fromBox = new ComboBox<>(FXCollections.observableArrayList(times));
        ComboBox<String> toBox = new ComboBox<>(FXCollections.observableArrayList(times));
        for (ComboBox<String> cb : java.util.List.of(fromBox, toBox)) {
            cb.setMaxWidth(Double.MAX_VALUE);
            cb.setVisibleRowCount(8);
            styleInput(cb);
        }
        VBox fromCol = new VBox(6, formLabel("Opening Time"), fromBox);
        VBox toCol = new VBox(6, formLabel("Closing Time"), toBox);
        HBox.setHgrow(fromCol, Priority.ALWAYS);
        HBox.setHgrow(toCol, Priority.ALWAYS);
        HBox timeRow = new HBox(12, fromCol, toCol);

        VBox walkPanel = new VBox(10, wkTitle, wkHelp,
                formLabel("Drop-off Location"), wkLocation,
                formLabel("Days Open"), daysBox,
                timeRow);
        walkPanel.setStyle("-fx-background-color: #eef4ff; -fx-background-radius: 14; "
                + "-fx-border-color: #bcd1f7; -fx-border-radius: 14; -fx-padding: 16 18 16 18;");

        // ----- In-Kind details: the items the admin is asking for -----
        Label itTitle = new Label("Items Needed");
        itTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        itTitle.setTextFill(Color.web(NAVY_DEEP));
        Label itHelp = new Label("List the items you are asking for, separated by commas. Donors can pick them when they donate.");
        itHelp.setWrapText(true);
        itHelp.setMinHeight(Region.USE_PREF_SIZE);
        itHelp.setFont(Font.font("Segoe UI", 13));
        itHelp.setTextFill(Color.web(TEXT_MUTED));
        TextField itemsField = new TextField();
        itemsField.setPromptText("e.g. Canned Goods, Rice (kg), Bottled Water, Blankets");
        styleInput(itemsField);
        TextField itemsGoalField = new TextField();
        itemsGoalField.setPromptText("e.g. 100 (how many items you need in total)");
        styleInput(itemsGoalField);
        VBox itemsPanel = new VBox(10, itTitle, itHelp,
                formLabel("Items"), itemsField,
                formLabel("Target Quantity"), itemsGoalField);
        itemsPanel.setStyle("-fx-background-color: #eef4ff; -fx-background-radius: 14; "
                + "-fx-border-color: #bcd1f7; -fx-border-radius: 14; -fx-padding: 16 18 16 18;");

        Label errorLabel = new Label(" ");
        errorLabel.setFont(Font.font("Segoe UI", 13));
        errorLabel.setWrapText(true);
        errorLabel.setTextFill(Color.web("#9b2c2c"));

        Runnable applyMethod = () -> {
            boolean cashType = cashTypeBtn.isSelected();
            boolean gcash = gcashBtn.isSelected();
            for (Node n : new Node[]{goalLabel, goalRow, pmTitle, pmSub, choiceRow}) {
                n.setVisible(cashType);
                n.setManaged(cashType);
            }
            gcashPanel.setVisible(cashType && gcash);
            gcashPanel.setManaged(cashType && gcash);
            boolean showWalk = !cashType || !gcash;
            walkPanel.setVisible(showWalk);
            walkPanel.setManaged(showWalk);
            itemsPanel.setVisible(!cashType);
            itemsPanel.setManaged(!cashType);
            // Cash can be put in any category; In-Kind has no "Cash" category
            List<String> catOptions = java.util.Arrays.asList(cashType
                    ? CampaignStore.CATEGORIES : CampaignStore.INKIND_CATEGORIES);
            String curCat = categoryBox.getValue();
            categoryBox.getItems().setAll(catOptions);
            categoryBox.setValue(curCat != null && catOptions.contains(curCat) ? curCat : catOptions.get(0));
            wkTitle.setText(cashType ? "Walk-in Donation" : "Drop-off Details");
            wkHelp.setText(cashType
                    ? "Donors bring their cash to this drop-off location during the hours you set."
                    : "Donors bring the items to this drop-off location during the hours you set.");
        };
        group.selectedToggleProperty().addListener((obs, o, n) -> applyMethod.run());
        typeGroup.selectedToggleProperty().addListener((obs, o, n) -> applyMethod.run());
        applyMethod.run();

        Button saveBtn = primaryButton("Save Donation");
        saveBtn.setGraphic(icon(ICON_HEART, "#ffffff", 17));
        saveBtn.setGraphicTextGap(8);
        saveBtn.setMaxWidth(Double.MAX_VALUE);

        VBox.setMargin(pmTitle, new Insets(8, 0, 0, 0));
        VBox right = new VBox(12, typeTitle, typeSub, typeRow, pmTitle, pmSub, choiceRow,
                gcashPanel, itemsPanel, walkPanel,
                errorLabel, saveBtn);
        right.setMinWidth(0);
        right.setPrefWidth(440);
        HBox.setHgrow(right, Priority.ALWAYS);

        Separator divider = new Separator(javafx.geometry.Orientation.VERTICAL);
        divider.setOpacity(0.5);
        HBox columns = new HBox(28, left, divider, right);
        columns.setAlignment(Pos.TOP_LEFT);

        VBox formCard = glassCard(columns);
        formCard.setMaxWidth(1000);
        formCard.setPadding(new Insets(28));

        // ================= behaviour =================
        int[] editingId = {0};

        campaignFormLoader = c -> {
            editingId[0] = c == null ? 0 : c.id;
            titleLbl.setText(c == null ? "Add Donation" : "Edit Donation");
            saveBtn.setText(c == null ? "Save Donation" : "Update Donation");
            nameField.setText(c == null ? "" : c.name);
            descArea.setText(c == null ? "" : c.description);
            itemsGoalField.setText(c != null && c.isInKind() && c.goal > 0 ? String.valueOf(Math.round(c.goal)) : "");
            goalField.setText(c == null || c.goal <= 0 || c.isInKind() ? ""
                    : (c.goal == Math.rint(c.goal) ? String.valueOf((long) c.goal) : String.valueOf(c.goal)));
            gcName.setText(c == null ? "" : c.gcashName);
            gcNumber.setText(c == null ? "" : c.gcashNumber);
            wkLocation.setText(c == null ? "" : c.walkinLocation);
            daysBox.setValue(c == null || c.walkinDays.isBlank() ? "Mon\u2013Sat" : c.walkinDays);
            fromBox.setValue(c == null || c.walkinFrom.isBlank() ? "8:00 AM" : c.walkinFrom);
            toBox.setValue(c == null || c.walkinTo.isBlank() ? "5:00 PM" : c.walkinTo);
            photoPath[0] = c == null ? "" : c.photoPath;
            photoChanged[0] = false;
            (c != null && c.isInKind() ? kindTypeBtn : cashTypeBtn).setSelected(true);
            itemsField.setText(c == null ? "" : c.itemsNeeded);
            categoryBox.setValue(c != null ? c.category : CampaignStore.CATEGORIES[0]);
            gcashBtn.setSelected(true);
            errorLabel.setText(" ");
            refreshPhoto.run();
            applyMethod.run();
        };

        choosePhotoBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose a photo for this donation");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                    "Images", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));
            File picked = chooser.showOpenDialog(stage);
            if (picked == null) return;
            Image test = new Image(picked.toURI().toString(), 208, 148, true, true, false);
            if (test.isError() || test.getWidth() <= 0) {
                errorLabel.setText("That file could not be read as an image.");
                return;
            }
            errorLabel.setText(" ");
            photoPath[0] = picked.getAbsolutePath();
            photoChanged[0] = true;
            refreshPhoto.run();
        });
        removePhotoBtn.setOnAction(e -> {
            photoPath[0] = "";
            photoChanged[0] = true;
            refreshPhoto.run();
        });

        backBtn.setOnAction(e -> {
            campaignFormLoader.accept(null);
            showPage("donations");
        });

        saveBtn.setOnAction(e -> {
            String name = txt(nameField);
            String desc = txt(descArea);
            String gName = txt(gcName);
            String gNum = txt(gcNumber);
            String loc = txt(wkLocation);
            String days = daysBox.getEditor().getText() == null ? "" : daysBox.getEditor().getText().trim();
            String from = fromBox.getValue() == null ? "" : fromBox.getValue();
            String to = toBox.getValue() == null ? "" : toBox.getValue();
            boolean inKind = kindTypeBtn.isSelected();
            String itemsText = txt(itemsField);

            double goal;
            try {
                goal = Double.parseDouble(txt(goalField).replace(",", "").replace("\u20B1", ""));
            } catch (NumberFormatException ex) {
                goal = -1;
            }

            if (name.isEmpty()) {
                errorLabel.setText("Please enter the donation name.");
                return;
            }
            if (inKind && itemsText.isEmpty()) {
                errorLabel.setText("Please list the items you need (separated by commas).");
                return;
            }
            String targetTxt = txt(itemsGoalField).replaceAll("\\D", "");
            final int targetItems = (targetTxt.isEmpty() || targetTxt.length() > 9) ? 0 : Integer.parseInt(targetTxt);
            if (inKind && targetItems < 1) {
                errorLabel.setText("Please enter how many items you need in total (1 or more).");
                return;
            }
            if (!inKind && goal <= 0) {
                errorLabel.setText("Please enter the goal amount (greater than 0).");
                return;
            }
            if (!inKind && gName.isEmpty()) {
                gcashBtn.setSelected(true);
                errorLabel.setText("Please enter the GCash account name.");
                return;
            }
            if (!inKind && gNum.replaceAll("\\D", "").length() < 10) {
                gcashBtn.setSelected(true);
                errorLabel.setText("Please enter a valid GCash number (at least 10 digits).");
                return;
            }
            if (loc.isEmpty()) {
                if (!inKind) walkBtn.setSelected(true);
                errorLabel.setText("Please enter the drop-off location.");
                return;
            }
            if (days.isEmpty()) {
                if (!inKind) walkBtn.setSelected(true);
                errorLabel.setText("Please enter the days the drop-off is open.");
                return;
            }
            if (from.isEmpty() || to.isEmpty() || (times.indexOf(from) >= 0 && times.indexOf(to) >= 0
                    && times.indexOf(from) >= times.indexOf(to))) {
                if (!inKind) walkBtn.setSelected(true);
                errorLabel.setText("Please set an opening time that is earlier than the closing time.");
                return;
            }

            try {
                String savedPhoto = photoPath[0];
                if (photoChanged[0] && !savedPhoto.isBlank()) savedPhoto = copyCampaignPhoto(savedPhoto);
                boolean isNew = editingId[0] == 0;
                CampaignStore.save(editingId[0], inKind ? CampaignStore.TYPE_INKIND : CampaignStore.TYPE_CASH,
                        name, desc, inKind ? targetItems : goal, savedPhoto, inKind ? "" : gName, inKind ? "" : gNum,
                        loc, days, from, to, inKind ? itemsText : "",
                        categoryBox.getValue());
                addNotification(ICON_HEART, GREEN, GREEN_LIGHT,
                        "Donation \"" + name + "\" was " + (isNew ? "added" : "updated") + ". Users can see it now.");
                campaignFormLoader.accept(null);
                showPage("donations");
            } catch (RuntimeException ex) {
                errorLabel.setText(ex.getMessage());
            }
        });

        campaignFormLoader.accept(null);

        body.getChildren().addAll(titleCard, formCard);
        return wrapScroll(body);
    }

    /** Keeps a copy of the picked photo in ./campaign_photos so it still works if the original is moved. */
    private String copyCampaignPhoto(String source) {
        try {
            Path dir = Paths.get(System.getProperty("user.dir"), "campaign_photos");
            Files.createDirectories(dir);
            Path src = Paths.get(source);
            String base = src.getFileName().toString().replaceAll("[^A-Za-z0-9._-]", "_");
            Path dest = dir.resolve(System.currentTimeMillis() + "_" + base);
            Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
            return dest.toString();
        } catch (Exception ex) {
            return source;
        }
    }

    /** One donation tile on the admin Donations page: photo, name, goal / raised, GCash + walk-in, actions. */
    private StackPane campaignCard(CampaignStore.Campaign c, double cardWidth, Runnable onChange) {
        StackPane photo;
        if (!c.photoPath.isBlank() && new File(c.photoPath).isFile()) {
            photo = croppedPhoto("file:" + c.photoPath, 150, 0);
        } else {
            Label none = new Label("No photo");
            none.setTextFill(Color.web(TEXT_MUTED));
            photo = new StackPane(none);
            photo.setMinHeight(150);
            photo.setPrefHeight(150);
            photo.setMaxHeight(150);
            photo.setStyle("-fx-background-color: #e8eefb;");
        }

        Label statusChip = badge("STATUS", c.isActive() ? "Active" : "Closed");
        StackPane.setAlignment(statusChip, Pos.TOP_LEFT);
        StackPane.setMargin(statusChip, new Insets(12));
        StackPane photoWrap = new StackPane(photo, statusChip);

        Label name = new Label(c.name);
        name.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15.5));
        name.setTextFill(Color.web(NAVY_DEEP));
        name.setWrapText(true);

        Label money = new Label(c.progressText() + " (" + c.percent() + "%)");
        money.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12.5));
        money.setTextFill(Color.web(NAVY_DEEP));
        ProgressBar bar = new ProgressBar(c.percent() / 100.0);
        bar.setMaxWidth(Double.MAX_VALUE);
        bar.setStyle("-fx-accent: " + BLUE + ";");

        Label gcash = new Label("GCash: " + c.gcashName + " \u2022 " + c.gcashNumber);
        gcash.setFont(Font.font("Segoe UI", 12.5));
        gcash.setTextFill(Color.web(TEXT_MUTED));
        gcash.setWrapText(true);
        Label walk = new Label("Walk-in: " + c.walkinLocation + " | " + c.walkinSchedule());
        walk.setFont(Font.font("Segoe UI", 12.5));
        walk.setTextFill(Color.web(TEXT_MUTED));
        walk.setWrapText(true);

        Button editBtn = flatAction("Edit", BLUE_LIGHT, "#c7d9fb");
        editBtn.setTextFill(Color.web(BLUE));
        Button toggleBtn = flatAction(c.isActive() ? "Close" : "Reopen", "#fef9c3", "#fef08a");
        toggleBtn.setTextFill(Color.web(YELLOW));
        Button deleteBtn = flatAction("Delete", "#fee2e2", "#fecaca");
        deleteBtn.setTextFill(Color.web("#b91c1c"));
        HBox actions = new HBox(8, editBtn, toggleBtn, deleteBtn);
        actions.setAlignment(Pos.CENTER_LEFT);

        editBtn.setOnAction(e -> openCampaignForm(c));
        toggleBtn.setOnAction(e -> {
            CampaignStore.setStatus(c.id, c.isActive() ? "Closed" : "Active");
            onChange.run();
        });
        deleteBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Delete Donation");
            confirm.setHeaderText("Delete \"" + c.name + "\"?");
            confirm.setContentText("Users will no longer see it. Donations already made to it are kept in the records.");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    CampaignStore.delete(c.id);
                    onChange.run();
                }
            });
        });

        Label typeLbl = new Label(c.isInKind() ? "IN-KIND \u2022 " + c.category.toUpperCase()
                : "Cash".equalsIgnoreCase(c.category) ? "CASH DONATION" : "CASH \u2022 " + c.category.toUpperCase());
        typeLbl.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: "
                + (c.isInKind() ? "#b45309" : "#1d4ed8") + ";");
        VBox info;
        if (c.isInKind()) {
            Label items = new Label("Items needed: " + (c.itemsNeeded.isBlank() ? "\u2014" : c.itemsNeeded));
            items.setFont(Font.font("Segoe UI", 12.5));
            items.setTextFill(Color.web(TEXT_MUTED));
            items.setWrapText(true);
            walk.setText("Drop-off: " + c.walkinLocation + " | " + c.walkinSchedule());
            info = c.hasGoal() ? new VBox(9, typeLbl, name, items, money, bar, walk, actions)
                    : new VBox(9, typeLbl, name, items, walk, actions);
        } else {
            info = new VBox(9, typeLbl, name, money, bar, gcash, walk, actions);
        }
        info.setPadding(new Insets(16, 18, 16, 18));

        VBox card = new VBox(photoWrap, info);
        card.setPrefWidth(cardWidth);
        card.setMinWidth(cardWidth);
        card.setMaxWidth(cardWidth);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 18; "
                + "-fx-border-color: #e6ecf6; -fx-border-radius: 18;");
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(card.widthProperty());
        clip.heightProperty().bind(card.heightProperty());
        clip.setArcWidth(36);
        clip.setArcHeight(36);
        card.setClip(clip);

        StackPane holder = new StackPane(card);
        holder.setEffect(new DropShadow(20, Color.rgb(20, 40, 90, 0.13)));
        return holder;
    }

    /** Selectable payment card (icon + name + tick circle in the corner). */
    private StackPane paymentOption(ToggleButton tb, Node iconNode, String text) {
        Label name = new Label(text);
        name.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        name.setTextFill(Color.web(NAVY_DEEP));
        VBox content = new VBox(8, iconNode, name);
        content.setAlignment(Pos.CENTER);
        tb.setGraphic(content);
        tb.setMaxWidth(Double.MAX_VALUE);
        tb.setPrefHeight(96);

        Circle ring = new Circle(10);
        Group tick = icon(ICON_TICK, "#ffffff", 13);
        StackPane indicator = new StackPane(ring, tick);
        indicator.setMaxSize(20, 20);
        indicator.setMouseTransparent(true);

        Runnable restyle = () -> {
            boolean on = tb.isSelected();
            tb.setStyle("-fx-cursor: hand; -fx-background-radius: 12; -fx-border-radius: 12; "
                    + (on ? "-fx-background-color: #eef4ff; -fx-border-color: " + BLUE + "; -fx-border-width: 2;"
                    : "-fx-background-color: rgba(255,255,255,0.9); -fx-border-color: #d1d9ea; -fx-border-width: 1.5;"));
            ring.setFill(on ? Color.web(BLUE) : Color.WHITE);
            ring.setStroke(on ? Color.web(BLUE) : Color.web("#b8c2d6"));
            tick.setVisible(on);
        };
        tb.selectedProperty().addListener((obs, o, n) -> restyle.run());
        restyle.run();

        StackPane wrap = new StackPane(tb, indicator);
        StackPane.setAlignment(indicator, Pos.TOP_RIGHT);
        StackPane.setMargin(indicator, new Insets(8, 8, 0, 0));
        return wrap;
    }

    private Label formLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        l.setTextFill(Color.web(NAVY));
        return l;
    }

    private Label spaceBefore(Label l) {
        VBox.setMargin(l, new Insets(8, 0, 0, 0));
        return l;
    }

    private void styleInput(Control c) {
        c.setPrefHeight(44);
        c.setStyle("-fx-background-color: #f9fafb; -fx-background-radius: 10; "
                + "-fx-border-color: #d1d5db; -fx-border-radius: 10; -fx-font-size: 13px;");
        if (c instanceof TextField) {
            c.setStyle(c.getStyle() + "-fx-padding: 0 12 0 12;");
        }
    }

    /**
     * Photo cropped to fill a rounded box of the given height. imageFile is a name in /images,
     * or "file:" + a full path to a photo the admin picked.
     */
    private StackPane croppedPhoto(String imageFile, double h, double arc) {
        StackPane box = new StackPane();
        box.setMinSize(0, h);
        box.setPrefSize(10, h);
        box.setMaxSize(Double.MAX_VALUE, h);
        try (java.io.InputStream in = imageFile.startsWith("file:")
                ? new java.io.FileInputStream(imageFile.substring(5))
                : getClass().getResourceAsStream("/images/" + imageFile)) {
            if (in != null) {
                Image img = new Image(in);
                ImageView iv = new ImageView(img);
                iv.setManaged(false);
                iv.setSmooth(true);
                iv.setFitHeight(h);
                iv.fitWidthProperty().bind(box.widthProperty());
                box.widthProperty().addListener((obs, o, w) -> {
                    double bw = w.doubleValue();
                    if (bw <= 0) return;
                    double boxRatio = bw / h, imgRatio = img.getWidth() / img.getHeight();
                    double vw = imgRatio > boxRatio ? img.getHeight() * boxRatio : img.getWidth();
                    double vh = imgRatio > boxRatio ? img.getHeight() : img.getWidth() / boxRatio;
                    iv.setViewport(new javafx.geometry.Rectangle2D((img.getWidth() - vw) / 2, (img.getHeight() - vh) / 2, vw, vh));
                });
                box.getChildren().add(iv);
            } else {
                System.out.println("IMAGE ERROR: /images/" + imageFile + " not found");
            }
        } catch (Exception ex) {
            System.out.println("IMAGE ERROR: " + ex.getMessage());
        }
        if (arc > 0) {
            Rectangle clip = new Rectangle();
            clip.setArcWidth(arc);
            clip.setArcHeight(arc);
            clip.widthProperty().bind(box.widthProperty());
            clip.setHeight(h);
            box.setClip(clip);
        }
        return box;
    }

    // ---------- Volunteers page (Separated: Pending Applications vs Approved Volunteers) ----------

    private ScrollPane buildVolunteersPage() {
        VBox body = new VBox(22);
        body.setPadding(new Insets(28, 40, 28, 40));

        body.getChildren().add(pageHeader("Volunteers",
                "Review pending volunteer applications and manage approved volunteers.", null));

        Label[] totalOut = new Label[1];
        Label[] activeOut = new Label[1];
        Label[] pendingOut = new Label[1];
        Label[] inactiveOut = new Label[1];

        // targets for the clickable stat cards (filled in once the cards below exist)
        ScrollPane[] scrollRef = new ScrollPane[1];
        Node[] approvedRef = new Node[1];
        Node[] pendingRef = new Node[1];

        VBox totalCard = statCardBound(ICON_PEOPLE, PURPLE, "Total Volunteers", "0", "All volunteers", totalOut);
        VBox activeCard = statCardBound(ICON_CHECK, GREEN, "Approved (Active)", "0", "Approved & active", activeOut);
        VBox pendingStatCard = statCardBound(ICON_CLOCK, ORANGE, "Pending Approval", "0", "Awaiting admin decision", pendingOut);
        VBox inactiveCard = statCardBound(ICON_CANCEL, RED, "Inactive Volunteers", "0", "Currently inactive", inactiveOut);

        makeClickableCard(totalCard, "View all approved volunteers", () -> scrollToNode(scrollRef[0], approvedRef[0]));
        makeClickableCard(activeCard, "View approved volunteers", () -> scrollToNode(scrollRef[0], approvedRef[0]));
        makeClickableCard(pendingStatCard, "Review pending applications", () -> scrollToNode(scrollRef[0], pendingRef[0]));
        makeClickableCard(inactiveCard, "View inactive volunteers", () -> showPage("inactive"));

        HBox stats = equalRow(totalCard, activeCard, pendingStatCard, inactiveCard);
        body.getChildren().add(stats);

        Label pendingCountBadge = new Label();
        pendingCountBadge.setStyle("-fx-background-color: #ffedd5; -fx-text-fill: #c2410c; -fx-font-weight: bold; "
                + "-fx-font-size: 12px; -fx-padding: 4 12 4 12; -fx-background-radius: 16;");

        Label approvedCountBadge = new Label();
        approvedCountBadge.setStyle("-fx-background-color: #dcfce7; -fx-text-fill: #15803d; -fx-font-weight: bold; "
                + "-fx-font-size: 12px; -fx-padding: 4 12 4 12; -fx-background-radius: 16;");

        Label cancelledCountBadge = new Label();
        cancelledCountBadge.setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #b91c1c; -fx-font-weight: bold; "
                + "-fx-font-size: 12px; -fx-padding: 4 12 4 12; -fx-background-radius: 16;");

        Label inactiveCountBadge = new Label();
        inactiveCountBadge.setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #b91c1c; -fx-font-weight: bold; "
                + "-fx-font-size: 12px; -fx-padding: 4 12 4 12; -fx-background-radius: 16;");

        Label rejectedCountBadge = new Label();
        rejectedCountBadge.setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #b91c1c; -fx-font-weight: bold; "
                + "-fx-font-size: 12px; -fx-padding: 4 12 4 12; -fx-background-radius: 16;");

        Runnable updateVolunteerCounters = () -> {
            long activeCount = approvedVolunteerList.stream()
                    .filter(r -> "Active".equalsIgnoreCase(r[4])).count();
            long inactiveCount = approvedVolunteerList.stream()
                    .filter(r -> "Inactive".equalsIgnoreCase(r[4])).count();
            int pendCount = pendingVolunteerList.size();
            int totalCount = approvedVolunteerList.size() + pendCount;

            if (totalOut[0] != null) totalOut[0].setText(String.valueOf(totalCount));
            if (activeOut[0] != null) activeOut[0].setText(String.valueOf(activeCount));
            if (pendingOut[0] != null) pendingOut[0].setText(String.valueOf(pendCount));
            if (inactiveOut[0] != null) inactiveOut[0].setText(String.valueOf(inactiveCount));

            pendingCountBadge.setText(pendCount + " Pending");
            approvedCountBadge.setText(activeCount + " Approved");
            inactiveCountBadge.setText(inactiveCount + " Inactive");
            rejectedCountBadge.setText(rejectedVolunteerList.size() + " Rejected");
            refreshHomeHook.run();
            refreshRecordsHook.run();
        };
        refreshVolunteersHook = updateVolunteerCounters;

        // 1. PENDING VOLUNTEER APPLICATIONS TABLE (With Approve & Reject buttons)
        Label pendingTitle = sectionTitle("Pending Volunteer Applications", ICON_CLOCK);
        Label pendingSub = new Label("Approve applicants to move them to the official volunteers list, or reject their request.");
        pendingSub.setFont(Font.font("Segoe UI", 12.5));
        pendingSub.setTextFill(Color.web(TEXT_MUTED));
        HBox pendingHeader = new HBox(12, new VBox(2, pendingTitle, pendingSub), spacer(), pendingCountBadge);
        pendingHeader.setAlignment(Pos.CENTER_LEFT);

        TableView<String[]> pendingTable = buildPendingVolunteersTable(updateVolunteerCounters);
        VBox pendingCard = glassCard(new VBox(14, pendingHeader, pendingTable));

        // 2. APPROVED / OFFICIAL VOLUNTEERS TABLE
        Label approvedTitle = sectionTitle("Approved Volunteers", ICON_CHECK);
        Label approvedSub = new Label("Active volunteers who have already been approved. Set a volunteer as inactive to move them to the Inactive list.");
        approvedSub.setFont(Font.font("Segoe UI", 12.5));
        approvedSub.setTextFill(Color.web(TEXT_MUTED));
        HBox approvedHeader = new HBox(12, new VBox(2, approvedTitle, approvedSub), spacer(), approvedCountBadge);
        approvedHeader.setAlignment(Pos.CENTER_LEFT);

        // live view: only Active volunteers stay here; Inactive ones move to the Inactive Volunteers page
        javafx.collections.transformation.FilteredList<String[]> activeVolunteerList =
                new javafx.collections.transformation.FilteredList<>(approvedVolunteerList,
                        r -> "Active".equalsIgnoreCase(r[4]));
        TableView<String[]> approvedTable = buildApprovedVolunteersTable(activeVolunteerList, updateVolunteerCounters);
        Label approvedEmpty = new Label("No active volunteers right now.");
        approvedEmpty.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13.5));
        approvedEmpty.setTextFill(Color.web(TEXT_MUTED));
        approvedTable.setPlaceholder(approvedEmpty);
        VBox approvedCard = glassCard(new VBox(14, approvedHeader, approvedTable));

        // 2b. INACTIVE VOLUNTEERS (right below the approved ones): "Set Active" brings a volunteer back up
        Label inactiveTitle = sectionTitle("Inactive Volunteers", ICON_CANCEL);
        Label inactiveSub = new Label("Approved volunteers who are currently inactive. Set a volunteer as active to move them back.");
        inactiveSub.setFont(Font.font("Segoe UI", 12.5));
        inactiveSub.setTextFill(Color.web(TEXT_MUTED));
        HBox inactiveHeader = new HBox(12, new VBox(2, inactiveTitle, inactiveSub), spacer(), inactiveCountBadge);
        inactiveHeader.setAlignment(Pos.CENTER_LEFT);
        javafx.collections.transformation.FilteredList<String[]> inactiveVolunteerList =
                new javafx.collections.transformation.FilteredList<>(approvedVolunteerList,
                        r -> "Inactive".equalsIgnoreCase(r[4]));
        TableView<String[]> inactiveTable = buildApprovedVolunteersTable(inactiveVolunteerList, updateVolunteerCounters);
        Label inactiveEmpty = new Label("No inactive volunteers right now.");
        inactiveEmpty.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13.5));
        inactiveEmpty.setTextFill(Color.web(TEXT_MUTED));
        inactiveTable.setPlaceholder(inactiveEmpty);
        VBox inactiveListCard = glassCard(new VBox(14, inactiveHeader, inactiveTable));

        // 3. REJECTED / REMOVED APPLICATIONS TABLE (applications removed from the pending list)
        Label rejectedTitle = sectionTitle("Rejected Applications", ICON_CANCEL);
        Label rejectedSub = new Label("Applications that were removed from the pending list.");
        rejectedSub.setFont(Font.font("Segoe UI", 12.5));
        rejectedSub.setTextFill(Color.web(TEXT_MUTED));
        HBox rejectedHeader = new HBox(12, new VBox(2, rejectedTitle, rejectedSub), spacer(), rejectedCountBadge);
        rejectedHeader.setAlignment(Pos.CENTER_LEFT);

        TableView<String[]> rejectedTable = buildTableFromObservable(
                new String[]{"ID", "NAME", "CONTACT", "ROLE / INTEREST", "STATUS", "DATE REGISTERED"},
                rejectedVolunteerList
        );
        Label rejectedEmpty = new Label("No rejected applications.");
        rejectedEmpty.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13.5));
        rejectedEmpty.setTextFill(Color.web(TEXT_MUTED));
        rejectedTable.setPlaceholder(rejectedEmpty);
        VBox rejectedCard = glassCard(new VBox(14, rejectedHeader, rejectedTable));

        updateVolunteerCounters.run();

        body.getChildren().addAll(pendingCard, approvedCard, inactiveListCard, rejectedCard);
        pendingRef[0] = pendingCard;
        approvedRef[0] = approvedCard;
        ScrollPane scroll = wrapScroll(body);
        scrollRef[0] = scroll;
        return scroll;
    }

    /**
     * Makes a stat card behave like a button: hand cursor, lifts and grows on hover with a stronger
     * shadow, squishes slightly while pressed, then springs back and runs the action on click.
     */
    private void makeClickableCard(VBox card, String tip, Runnable action) {
        card.setCursor(Cursor.HAND);
        Tooltip.install(card, new Tooltip(tip));

        DropShadow shadow = card.getEffect() instanceof DropShadow
                ? (DropShadow) card.getEffect() : new DropShadow(20, Color.rgb(20, 40, 90, 0.20));
        card.setEffect(shadow);

        final double baseRadius = shadow.getRadius();
        final boolean[] hover = {false};

        java.util.function.BiConsumer<Double, Double> animateTo = (scale, lift) -> {
            boolean raised = lift != 0;
            new Timeline(new KeyFrame(Duration.millis(160),
                    new KeyValue(card.scaleXProperty(), scale, Interpolator.EASE_BOTH),
                    new KeyValue(card.scaleYProperty(), scale, Interpolator.EASE_BOTH),
                    new KeyValue(card.translateYProperty(), lift, Interpolator.EASE_BOTH),
                    new KeyValue(shadow.radiusProperty(), raised ? baseRadius + 14 : baseRadius, Interpolator.EASE_BOTH),
                    new KeyValue(shadow.offsetYProperty(), raised ? 8.0 : 0.0, Interpolator.EASE_BOTH)
            )).play();
        };

        card.setOnMouseEntered(e -> { hover[0] = true; animateTo.accept(1.04, -5.0); });
        card.setOnMouseExited(e -> { hover[0] = false; animateTo.accept(1.0, 0.0); });
        card.setOnMousePressed(e -> animateTo.accept(0.97, 0.0));
        card.setOnMouseReleased(e -> {
            if (hover[0]) animateTo.accept(1.04, -5.0);
        });
        card.setOnMouseClicked(e -> {
            if (hover[0]) action.run();
        });
    }

    /** Smoothly scrolls a ScrollPane so that the given child of its content sits near the top. */
    private void scrollToNode(ScrollPane sp, Node target) {
        if (sp == null || target == null) return;
        javafx.application.Platform.runLater(() -> {
            sp.applyCss();
            sp.layout();
            double contentH = sp.getContent().getBoundsInLocal().getHeight();
            double max = contentH - sp.getViewportBounds().getHeight();
            if (max <= 0) return;
            double y = Math.max(0, target.getBoundsInParent().getMinY() - 12);
            double v = Math.min(1.0, y / max);
            new Timeline(new KeyFrame(Duration.millis(420),
                    new KeyValue(sp.vvalueProperty(), v, Interpolator.EASE_BOTH))).play();
        });
    }

    // ---------- Inactive Volunteers page (opened by clicking the "Inactive Volunteers" card) ----------

    private ScrollPane buildInactiveVolunteersPage() {
        VBox body = new VBox(22);
        body.setPadding(new Insets(28, 40, 28, 40));

        Button backBtn = primaryButton("\u2190 Back to Volunteers");
        backBtn.setOnAction(e -> showPage("volunteers"));
        body.getChildren().add(pageHeader("Inactive Volunteers",
                "Approved volunteers who are currently marked as inactive.", backBtn));

        // live view: always reflects the approved list, showing only Inactive members
        javafx.collections.transformation.FilteredList<String[]> inactiveList =
                new javafx.collections.transformation.FilteredList<>(approvedVolunteerList,
                        r -> "Inactive".equalsIgnoreCase(r[4]));

        Label countBadge = new Label();
        countBadge.setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #b91c1c; -fx-font-weight: bold; "
                + "-fx-font-size: 12px; -fx-padding: 4 12 4 12; -fx-background-radius: 16;");
        Runnable updateCount = () -> countBadge.setText(inactiveList.size() + " Inactive");
        inactiveList.addListener((javafx.collections.ListChangeListener<String[]>) c -> updateCount.run());
        updateCount.run();

        Label title = sectionTitle("Inactive Members", ICON_CANCEL);
        Label sub = new Label("These volunteers are not currently active in any program.");
        sub.setFont(Font.font("Segoe UI", 12.5));
        sub.setTextFill(Color.web(TEXT_MUTED));
        HBox header = new HBox(12, new VBox(2, title, sub), spacer(), countBadge);
        header.setAlignment(Pos.CENTER_LEFT);

        TableView<String[]> table = buildApprovedVolunteersTable(inactiveList, () -> refreshVolunteersHook.run());
        Label empty = new Label("No inactive volunteers right now.");
        empty.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13.5));
        empty.setTextFill(Color.web(TEXT_MUTED));
        table.setPlaceholder(empty);

        body.getChildren().add(glassCard(new VBox(14, header, table)));
        return wrapScroll(body);
    }

    /**
     * Table for approved volunteers with one toggle button per row: "Set Inactive" for an Active volunteer,
     * "Set Active" for an Inactive one. The change is saved to the database and the row moves to the other
     * list (Approved Volunteers <-> Inactive Volunteers).
     */
    private TableView<String[]> buildApprovedVolunteersTable(ObservableList<String[]> list, Runnable onChange) {
        String[] columns = {"ID", "NAME", "CONTACT", "ROLE / INTEREST", "STATUS", "DATE REGISTERED", "ACTION"};
        TableView<String[]> table = new TableView<>(list);
        table.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent; " +
                "-fx-table-cell-border-color: transparent; -fx-background-insets: 0; -fx-padding: 0; " +
                "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-font-size: 14px;");
        table.setFixedCellSize(TABLE_ROW_H);
        table.setPlaceholder(new Label("No records yet."));

        final double totalWeight = columns.length + 0.5;
        for (int i = 0; i < columns.length; i++) {
            final int idx = i;
            final String name = columns[i];
            TableColumn<String[], String> col = new TableColumn<>(name);
            col.prefWidthProperty().bind(table.widthProperty()
                    .multiply(("ACTION".equals(name) ? 1.5 : 1.0) / totalWeight).subtract(2));
            col.setSortable(false);
            col.setStyle("-fx-alignment: CENTER_LEFT; -fx-padding: 0 14 0 14; -fx-text-fill: " + NAVY + ";");

            if (name.equals("ACTION")) {
                col.setCellValueFactory(row -> new javafx.beans.property.SimpleStringProperty(row.getValue()[4]));
                col.setCellFactory(c -> new TableCell<String[], String>() {
                    private final Button toggleBtn = new Button();

                    {
                        toggleBtn.setOnAction(e -> {
                            if (getIndex() < 0 || getIndex() >= getTableView().getItems().size()) return;
                            toggleVolunteerStatus(getTableView().getItems().get(getIndex()), onChange);
                        });
                    }

                    @Override
                    protected void updateItem(String status, boolean empty) {
                        super.updateItem(status, empty);
                        setText(null);
                        if (empty || status == null) {
                            setGraphic(null);
                            return;
                        }
                        boolean active = "Active".equalsIgnoreCase(status);
                        toggleBtn.setText(active ? "Set Inactive" : "Set Active");
                        String base = active
                                ? "-fx-background-color: #fee2e2; -fx-text-fill: #b91c1c;"
                                : "-fx-background-color: #16a34a; -fx-text-fill: white;";
                        toggleBtn.setStyle(base + " -fx-font-weight: bold; -fx-font-size: 12px; "
                                + "-fx-padding: 6 13 6 13; -fx-background-radius: 10; -fx-cursor: hand;");
                        setGraphic(toggleBtn);
                    }
                });
            } else {
                col.setCellValueFactory(row -> new javafx.beans.property.SimpleStringProperty(row.getValue()[idx]));
                col.setCellFactory(c -> new TableCell<String[], String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else if (name.equals("STATUS")) {
                            setText(null);
                            setGraphic(badge(name, item));
                        } else {
                            setText(item);
                            setGraphic(null);
                        }
                    }
                });
            }
            table.getColumns().add(col);
        }

        styleRowsAndHeight(table);
        return table;
    }

    /** Active <-> Inactive for one approved volunteer: saves to the database, then moves the row. */
    private void toggleVolunteerStatus(String[] row, Runnable onChange) {
        int at = -1;
        for (int i = 0; i < approvedVolunteerList.size(); i++) {
            if (approvedVolunteerList.get(i)[0].equals(row[0])) {
                at = i;
                break;
            }
        }
        if (at < 0) return;
        String next = "Active".equalsIgnoreCase(row[4]) ? "Inactive" : "Active";
        if (!VolunteerStore.setStatus(row[0], next)) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Volunteer Status");
            err.setHeaderText("Could not change the status.");
            err.setContentText("Please check the database connection and try again.");
            err.showAndWait();
            return;
        }
        approvedVolunteerList.set(at, new String[]{row[0], row[1], row[2], row[3], next, row[5]});
        if ("Inactive".equals(next)) {
            addNotification(ICON_CANCEL, RED, RED_LIGHT,
                    "Volunteer set to inactive: " + row[1] + " (" + row[0] + ")");
        } else {
            addNotification(ICON_CHECK, GREEN, GREEN_LIGHT,
                    "Volunteer set to active: " + row[1] + " (" + row[0] + ")");
        }
        onChange.run();
    }

    /** Table specifically for Pending Volunteers with Approve and Reject action buttons. */
    private TableView<String[]> buildPendingVolunteersTable(Runnable onChange) {
        String[] columns = {"ID", "NAME", "CONTACT", "ROLE / INTEREST", "STATUS", "DATE REGISTERED", "DECISION / ACTION"};
        TableView<String[]> table = new TableView<>(pendingVolunteerList);
        table.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent; " +
                "-fx-table-cell-border-color: transparent; -fx-background-insets: 0; -fx-padding: 0; " +
                "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-font-size: 14px;");
        table.setFixedCellSize(TABLE_ROW_H);
        Label emptyLbl = new Label("No pending volunteer applications right now.");
        emptyLbl.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13.5));
        emptyLbl.setTextFill(Color.web(TEXT_MUTED));
        table.setPlaceholder(emptyLbl);

        final double totalWeight = columns.length + 0.9;
        for (int i = 0; i < columns.length; i++) {
            final int idx = i;
            final String name = columns[i];
            TableColumn<String[], String> col = new TableColumn<>(name);
            col.prefWidthProperty().bind(table.widthProperty()
                    .multiply(("DECISION / ACTION".equals(name) ? 1.9 : 1.0) / totalWeight).subtract(2));
            col.setSortable(false);
            col.setStyle("-fx-alignment: CENTER_LEFT; -fx-padding: 0 14 0 14; -fx-text-fill: " + NAVY + ";");

            if (name.equals("DECISION / ACTION")) {
                col.setCellValueFactory(row -> new javafx.beans.property.SimpleStringProperty(""));
                col.setCellFactory(c -> new TableCell<String[], String>() {
                    private final Button approveBtn = new Button("\u2713 Approve");
                    private final Button removeBtn = new Button("\u2715 Remove");
                    private final HBox actions = new HBox(8, approveBtn, removeBtn);

                    {
                        actions.setAlignment(Pos.CENTER_LEFT);
                        String appBase = "-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-weight: bold; "
                                + "-fx-font-size: 12px; -fx-padding: 6 13 6 13; -fx-background-radius: 10; -fx-cursor: hand;";
                        String appHover = "-fx-background-color: #15803d; -fx-text-fill: white; -fx-font-weight: bold; "
                                + "-fx-font-size: 12px; -fx-padding: 6 13 6 13; -fx-background-radius: 10; -fx-cursor: hand;";
                        approveBtn.setStyle(appBase);
                        approveBtn.setOnMouseEntered(e -> approveBtn.setStyle(appHover));
                        approveBtn.setOnMouseExited(e -> approveBtn.setStyle(appBase));

                        String removeBase = "-fx-background-color: #fee2e2; -fx-text-fill: #b91c1c; -fx-font-weight: bold; "
                                + "-fx-font-size: 12px; -fx-padding: 6 13 6 13; -fx-background-radius: 10; -fx-cursor: hand;";
                        String removeHover = "-fx-background-color: #fecaca; -fx-text-fill: #991b1b; -fx-font-weight: bold; "
                                + "-fx-font-size: 12px; -fx-padding: 6 13 6 13; -fx-background-radius: 10; -fx-cursor: hand;";
                        removeBtn.setStyle(removeBase);
                        removeBtn.setOnMouseEntered(e -> removeBtn.setStyle(removeHover));
                        removeBtn.setOnMouseExited(e -> removeBtn.setStyle(removeBase));

                        approveBtn.setOnAction(e -> {
                            String[] rowData = getTableView().getItems().get(getIndex());
                            pendingVolunteerList.remove(rowData);
                            VolunteerStore.setStatus(rowData[0], "Active");
                            String[] approvedRow = new String[]{
                                    rowData[0], rowData[1], rowData[2], rowData[3], "Active", rowData[5]
                            };
                            approvedVolunteerList.add(0, approvedRow);
                            addNotification(ICON_CHECK, GREEN, GREEN_LIGHT,
                                    "Volunteer approved: " + rowData[1] + " (" + rowData[0] + ")");
                            onChange.run();
                        });

                        removeBtn.setOnAction(e -> {
                            String[] rowData = getTableView().getItems().get(getIndex());
                            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                            confirm.setTitle("Remove Volunteer Application");
                            confirm.setHeaderText("Move this application to Rejected Volunteer Applications?");
                            confirm.setContentText(rowData[1] + " (" + rowData[0] + ")");
                            confirm.showAndWait().ifPresent(answer -> {
                                if (answer == ButtonType.OK) {
                                    pendingVolunteerList.remove(rowData);
                                    VolunteerStore.setStatus(rowData[0], "Rejected");
                                    rejectedVolunteerList.add(0, new String[]{
                                            rowData[0], rowData[1], rowData[2], rowData[3], "Rejected", rowData[5]
                                    });
                                    addNotification(ICON_CANCEL, RED, RED_LIGHT,
                                            "Volunteer application moved to rejected records: "
                                                    + rowData[1] + " (" + rowData[0] + ")");
                                    onChange.run();
                                }
                            });
                        });
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(null);
                        setGraphic(empty ? null : actions);
                    }
                });
            } else {
                col.setCellValueFactory(row -> new javafx.beans.property.SimpleStringProperty(row.getValue()[idx]));
                col.setCellFactory(c -> new TableCell<String[], String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else if (name.equals("STATUS") || name.equals("TYPE")) {
                            setText(null);
                            setGraphic(badge(name, item));
                        } else {
                            setText(item);
                            setGraphic(null);
                        }
                    }
                });
            }
            table.getColumns().add(col);
        }

        styleRowsAndHeight(table);
        return table;
    }

    // ---------- Records page ----------

    private String extractDatePart(String dateAndTime) {
        if (dateAndTime == null) return "";
        int slash = dateAndTime.indexOf('/');
        return (slash > 0 ? dateAndTime.substring(0, slash) : dateAndTime).trim();
    }

    private ScrollPane buildRecordsPage() {
        VBox body = new VBox(22);
        body.setPadding(new Insets(28, 40, 28, 40));

        body.getChildren().add(pageHeader("Records",
                "Access and manage all system records \u2014 volunteers, donations, and events.", null));

        TableView<String[]> table = buildTable(
                new String[]{"ID", "TYPE", "NAME / TITLE", "DETAILS", "DATE", "STATUS"},
                new String[0][]
        );

        // Functional filter tabs: All Records, Volunteers, Donations, Pending, Events
        Button tabAll = tabButton("All Records", true);
        Button tabVol = tabButton("Volunteers", false);
        Button tabDon = tabButton("Donations", false);
        Button tabPend = tabButton("Pending", false);
        Button tabEvt = tabButton("Events", false);
        Button[] allTabBtns = {tabAll, tabVol, tabDon, tabPend, tabEvt};
        String[] activeTabKey = {"all"};

        Label[] volStatValue = new Label[1];
        Label[] donStatValue = new Label[1];
        Label[] evtStatValue = new Label[1];

        Runnable reloadRecordsTable = () -> {
            List<String[]> approvedVolRecords = new ArrayList<>();
            for (String[] v : approvedVolunteerList) {
                approvedVolRecords.add(new String[]{v[0], "Volunteer", v[1], v[3], v[5], v[4]});
            }

            List<String[]> approvedDonRecords = new ArrayList<>();
            for (String[] d : approvedDonationList) {
                approvedDonRecords.add(new String[]{"D-" + d[0], "Donation", d[1], d[2] + " \u2014 " + d[3], d[4], d[5]});
            }

            List<String[]> allEventRecords = new ArrayList<>();
            int eIdx = 0;
            for (String[] ev : activeEventList) {
                allEventRecords.add(new String[]{
                        String.format("E-%03d", ++eIdx), "Event", ev[0], ev[3] + " \u2014 " + ev[2], extractDatePart(ev[1]), ev[4]
                });
            }
            for (String[] ev : completedEventList) {
                allEventRecords.add(new String[]{
                        String.format("E-%03d", ++eIdx), "Event", ev[0], ev[3] + " \u2014 " + ev[2], extractDatePart(ev[1]), ev[4]
                });
            }

            List<String[]> pendingAllRecords = new ArrayList<>();
            for (String[] v : pendingVolunteerList) {
                pendingAllRecords.add(new String[]{v[0], "Volunteer", v[1], v[3], v[5], v[4]});
            }
            for (String[] d : pendingDonationList) {
                pendingAllRecords.add(new String[]{"D-" + d[0], "Donation", d[1], d[2] + " \u2014 " + d[3], d[4], d[5]});
            }
            sortNewestFirst(pendingAllRecords, 4, 0);

            if (volStatValue[0] != null) {
                volStatValue[0].setText(String.valueOf(approvedVolRecords.size()));
            }
            if (donStatValue[0] != null) {
                donStatValue[0].setText(String.valueOf(approvedDonRecords.size()));
            }
            if (evtStatValue[0] != null) {
                evtStatValue[0].setText(String.valueOf(allEventRecords.size()));
            }

            switch (activeTabKey[0]) {
                case "vol":
                    table.getItems().setAll(approvedVolRecords);
                    break;
                case "don":
                    table.getItems().setAll(approvedDonRecords);
                    break;
                case "pend":
                    table.getItems().setAll(pendingAllRecords);
                    break;
                case "evt":
                    table.getItems().setAll(allEventRecords);
                    break;
                default:
                    List<String[]> combined = new ArrayList<>();
                    combined.addAll(approvedVolRecords);
                    combined.addAll(approvedDonRecords);
                    combined.addAll(allEventRecords);
                    sortNewestFirst(combined, 4, 0);
                    table.getItems().setAll(combined);
                    break;
            }
        };

        refreshRecordsHook = reloadRecordsTable;

        Runnable selectAll = () -> {
            activeTabKey[0] = "all";
            setActiveTab(tabAll, allTabBtns);
            reloadRecordsTable.run();
        };
        Runnable selectVol = () -> {
            activeTabKey[0] = "vol";
            setActiveTab(tabVol, allTabBtns);
            reloadRecordsTable.run();
        };
        Runnable selectDon = () -> {
            activeTabKey[0] = "don";
            setActiveTab(tabDon, allTabBtns);
            reloadRecordsTable.run();
        };
        Runnable selectPend = () -> {
            activeTabKey[0] = "pend";
            setActiveTab(tabPend, allTabBtns);
            reloadRecordsTable.run();
        };
        Runnable selectEvt = () -> {
            activeTabKey[0] = "evt";
            setActiveTab(tabEvt, allTabBtns);
            reloadRecordsTable.run();
        };

        tabAll.setOnAction(e -> selectAll.run());
        tabVol.setOnAction(e -> selectVol.run());
        tabDon.setOnAction(e -> selectDon.run());
        tabPend.setOnAction(e -> selectPend.run());
        tabEvt.setOnAction(e -> selectEvt.run());

        body.getChildren().add(equalRow(
                recordStatBound(ICON_PEOPLE, PURPLE, PURPLE_LIGHT, "Approved Volunteers",
                        String.valueOf(approvedVolunteerList.size()), selectVol, volStatValue),
                recordStatBound(ICON_MONEY, ORANGE, ORANGE_LIGHT, "Approved Donations",
                        String.valueOf(approvedDonationList.size()), selectDon, donStatValue),
                recordStatBound(ICON_CALENDAR, BLUE, BLUE_LIGHT, "Event Registrations",
                        String.valueOf(activeEventList.size() + completedEventList.size()), selectEvt, evtStatValue)
        ));

        reloadRecordsTable.run();

        HBox tabs = new HBox(8, tabAll, tabVol, tabDon, tabPend, tabEvt);

        VBox tableCard = glassCard(new VBox(14, tabs, table));
        HBox.setHgrow(tableCard, Priority.ALWAYS);
        tableCard.setMaxWidth(Double.MAX_VALUE);

        VBox rightCol = new VBox(20, buildDonationOverviewMini(), buildRecordsActivity());
        rightCol.setPrefWidth(380);
        rightCol.setMinWidth(340);

        HBox panelsRow = new HBox(20, tableCard, rightCol);
        panelsRow.setAlignment(Pos.TOP_LEFT);
        body.getChildren().add(panelsRow);

        return wrapScroll(body);
    }

    private VBox recordStat(String iconPath, String iconColor, String iconBg, String title, String value, Runnable onView) {
        return recordStatBound(iconPath, iconColor, iconBg, title, value, onView, null);
    }

    private VBox recordStatBound(String iconPath, String iconColor, String iconBg,
                                 String title, String value, Runnable onView, Label[] valueOut) {
        StackPane iconBox = new StackPane(icon(iconPath, iconColor, 24));
        iconBox.setMinSize(52, 52);
        iconBox.setMaxSize(52, 52);
        iconBox.setStyle("-fx-background-color: " + iconBg + "; -fx-background-radius: 16;");

        Label titleL = new Label(title);
        titleL.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));
        titleL.setTextFill(Color.web(TEXT_MUTED));
        Label valueL = new Label(value);
        valueL.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        valueL.setTextFill(Color.web(NAVY_DEEP));
        if (valueOut != null && valueOut.length > 0) valueOut[0] = valueL;

        VBox textBox = new VBox(1, titleL, valueL);
        HBox content = new HBox(14, iconBox, textBox);
        content.setAlignment(Pos.CENTER_LEFT);

        VBox card = glassCard(content);
        card.setPadding(new Insets(20, 22, 20, 22));
        return card;
    }

    private static final String TAB_ON = "-fx-background-color: " + BLUE + "; -fx-text-fill: white; -fx-font-weight: bold; " +
            "-fx-background-radius: 16; -fx-cursor: hand; -fx-padding: 7 16 7 16;";
    private static final String TAB_OFF = "-fx-background-color: rgba(255,255,255,0.7); -fx-text-fill: " + NAVY + "; -fx-font-weight: bold; " +
            "-fx-background-radius: 16; -fx-cursor: hand; -fx-padding: 7 16 7 16;";
    private static final String TAB_HOVER = "-fx-background-color: rgba(255,255,255,0.95); -fx-text-fill: " + BLUE + "; -fx-font-weight: bold; " +
            "-fx-background-radius: 16; -fx-cursor: hand; -fx-padding: 7 16 7 16;";

    private Button tabButton(String text, boolean active) {
        Button btn = new Button(text);
        btn.setUserData(active);
        btn.setStyle(active ? TAB_ON : TAB_OFF);
        btn.setOnMouseEntered(e -> {
            if (!Boolean.TRUE.equals(btn.getUserData())) btn.setStyle(TAB_HOVER);
        });
        btn.setOnMouseExited(e -> {
            if (!Boolean.TRUE.equals(btn.getUserData())) btn.setStyle(TAB_OFF);
        });
        return btn;
    }

    private void setActiveTab(Button selected, Button... all) {
        for (Button b : all) {
            boolean isSelected = (b == selected);
            b.setUserData(isSelected);
            b.setStyle(isSelected ? TAB_ON : TAB_OFF);
        }
    }

    private VBox buildDonationOverviewMini() {
        Label title = sectionTitle("Donation Overview", ICON_HEART);

        VBox list = new VBox(8);
        Runnable refresh = () -> {
            list.getChildren().clear();
            int total = approvedDonationList.size();
            int goods = approvedGoodsCount();
            int cashN = approvedCashCount();
            list.getChildren().addAll(
                    donationRow("Approved Donations", String.valueOf(total), null),
                    softSeparator(),
                    donationRow("Cash", formatPeso(approvedCashTotal()), total == 0 ? null : (cashN * 100 / total) + "%"),
                    softSeparator(),
                    donationRow("Physical Goods", goods + " items", total == 0 ? null : (goods * 100 / total) + "%")
            );
        };
        liveRefreshers.add(refresh);
        refresh.run();

        return glassCard(new VBox(12, title, list));
    }

    private HBox donationRow(String label, String value, String pct) {
        Label labelL = new Label(label);
        labelL.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        labelL.setTextFill(Color.web(TEXT_MUTED));
        Label valueL = new Label(value + (pct != null ? "  " + pct : ""));
        valueL.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        valueL.setTextFill(Color.web(NAVY_DEEP));
        HBox row = new HBox(labelL, spacer(), valueL);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private VBox buildRecordsActivity() {
        Label title = sectionTitle("Recent Activity", ICON_CLOCK);
        VBox list = new VBox(0);
        Runnable refresh = () -> fillActivityList(list);
        liveRefreshers.add(refresh);
        refresh.run();
        return glassCard(new VBox(12, title, list));
    }

    // ---------- Community announcements: added from the admin Home, shown on the user's Home page ----------

    private Runnable rebuildAnnouncementsHook = () -> {};
    private Runnable announcementFormReset = () -> {};

    /** The "Community Announcements" card on the admin Home page: an Add button and the latest posts. */
    private VBox buildAnnouncementsCard() {
        Button addBtn = primaryButton("+ Add Announcement");
        addBtn.setOnAction(e -> {
            announcementFormReset.run();
            showPage("addannouncement");
        });
        Label title = sectionTitle("Community Announcements", ICON_BELL);
        Label sub = new Label("Post a notice for the users. It shows on their Home page under Community Announcements.");
        sub.setFont(Font.font("Segoe UI", 12.5));
        sub.setTextFill(Color.web(TEXT_MUTED));
        HBox header = new HBox(12, new VBox(2, title, sub), spacer(), addBtn);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox list = new VBox(10);
        rebuildAnnouncementsHook = () -> {
            list.getChildren().clear();
            List<AnnouncementStore.Announcement> all = AnnouncementStore.all();
            for (int i = 0; i < Math.min(5, all.size()); i++) {
                list.getChildren().add(announcementRow(all.get(i)));
            }
            boolean any = !list.getChildren().isEmpty();
            list.setVisible(any);
            list.setManaged(any);
        };
        rebuildAnnouncementsHook.run();
        return glassCard(new VBox(16, header, list));
    }

    private HBox announcementRow(AnnouncementStore.Announcement a) {
        Label title = new Label(a.title);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14.5));
        title.setTextFill(Color.web(NAVY_DEEP));
        title.setWrapText(true);
        Label date = new Label(a.date);
        date.setFont(Font.font("Segoe UI", 12));
        date.setTextFill(Color.web(TEXT_MUTED));
        HBox meta = new HBox(10, badge("TYPE", a.tag), date);
        meta.setAlignment(Pos.CENTER_LEFT);
        Label bodyLbl = new Label(a.body);
        bodyLbl.setFont(Font.font("Segoe UI", 13));
        bodyLbl.setTextFill(Color.web(NAVY));
        bodyLbl.setWrapText(true);
        VBox texts = new VBox(5, title, meta, bodyLbl);
        HBox.setHgrow(texts, Priority.ALWAYS);

        Button del = flatAction("Delete", "#fee2e2", "#fecaca");
        del.setTextFill(Color.web("#b91c1c"));
        del.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Delete \"" + a.title + "\"? Users will no longer see it.", ButtonType.YES, ButtonType.NO);
            confirm.setHeaderText(null);
            confirm.showAndWait().filter(b -> b == ButtonType.YES).ifPresent(b -> {
                AnnouncementStore.delete(a.id);
                rebuildAnnouncementsHook.run();
            });
        });

        HBox row = new HBox(14, texts, del);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setStyle("-fx-background-color: white; -fx-background-radius: 14; "
                + "-fx-border-color: #e6ecf6; -fx-border-radius: 14;");
        return row;
    }

    private ScrollPane buildAddAnnouncementPage() {
        VBox body = new VBox(20);
        body.setPadding(new Insets(28, 40, 28, 40));
        body.setAlignment(Pos.TOP_CENTER);

        // ----- title card -----
        Label titleLbl = new Label("Add Announcement");
        titleLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        titleLbl.setTextFill(Color.web(NAVY_DEEP));
        Label subLbl = new Label("Write a notice for the community. Users see it on their Home page under "
                + "Community Announcements.");
        subLbl.setFont(Font.font("Segoe UI", 13));
        subLbl.setTextFill(Color.web(TEXT_MUTED));
        subLbl.setWrapText(true);
        VBox titleTexts = new VBox(3, titleLbl, subLbl);
        HBox.setHgrow(titleTexts, Priority.ALWAYS);

        Button backBtn = new Button("\u2190 Back");
        String backBase = "-fx-background-color: " + NAVY + "; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-font-size: 13px; -fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 11 22 11 22;";
        backBtn.setStyle(backBase);
        backBtn.setMinWidth(Region.USE_PREF_SIZE);
        backBtn.setOnMouseEntered(e -> backBtn.setStyle(backBase.replace(NAVY, NAVY_DEEP)));
        backBtn.setOnMouseExited(e -> backBtn.setStyle(backBase));

        HBox titleRow = new HBox(14, titleTexts, backBtn);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        VBox titleCard = glassCard(titleRow);
        titleCard.setMaxWidth(760);

        // ----- form -----
        TextField titleField = new TextField();
        titleField.setPromptText("e.g. Barangay Medical & Dental Mission");
        styleInput(titleField);
        ComboBox<String> tagBox = new ComboBox<>(FXCollections.observableArrayList(
                "Announcement", "Reminder", "Advisory", "Update"));
        tagBox.setMaxWidth(Double.MAX_VALUE);
        styleInput(tagBox);
        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Write the announcement here...");
        messageArea.setWrapText(true);
        messageArea.setPrefRowCount(6);
        messageArea.setStyle("-fx-background-color: #f9fafb; -fx-background-radius: 10; -fx-border-color: #d1d5db; "
                + "-fx-border-radius: 10; -fx-font-size: 13px;");

        Label errorLabel = new Label(" ");
        errorLabel.setFont(Font.font("Segoe UI", 13));
        errorLabel.setWrapText(true);
        errorLabel.setTextFill(Color.web("#9b2c2c"));

        Button saveBtn = primaryButton("Post Announcement");
        saveBtn.setGraphic(icon(ICON_BELL, "#ffffff", 17));
        saveBtn.setGraphicTextGap(8);
        saveBtn.setMaxWidth(Double.MAX_VALUE);

        VBox form = new VBox(12,
                formLabel("Title"), titleField,
                formLabel("Type"), tagBox,
                formLabel("Message"), messageArea,
                errorLabel, saveBtn);
        VBox formCard = glassCard(form);
        formCard.setMaxWidth(760);
        formCard.setPadding(new Insets(28));

        // ----- behaviour -----
        announcementFormReset = () -> {
            titleField.clear();
            messageArea.clear();
            tagBox.setValue("Announcement");
            errorLabel.setText(" ");
        };
        backBtn.setOnAction(e -> {
            announcementFormReset.run();
            showPage("home");
        });
        saveBtn.setOnAction(e -> {
            String t = txt(titleField);
            String msg = messageArea.getText() == null ? "" : messageArea.getText().trim();
            if (t.isEmpty()) {
                errorLabel.setText("Please enter the title.");
                return;
            }
            if (msg.isEmpty()) {
                errorLabel.setText("Please write the message.");
                return;
            }
            if (!AnnouncementStore.add(t, msg, tagBox.getValue(), "")) {
                errorLabel.setText("Could not save the announcement. Please try again.");
                return;
            }
            addNotification(ICON_BELL, ORANGE, ORANGE_LIGHT, "Announcement posted: " + t);
            announcementFormReset.run();
            showPage("home");
        });
        announcementFormReset.run();

        body.getChildren().addAll(titleCard, formCard);
        return wrapScroll(body);
    }

    // ---------- Settings page (gear) ----------

    private ScrollPane buildSettingsPage() {
        VBox body = new VBox(22);
        body.setPadding(new Insets(28, 40, 28, 40));

        VBox header = pageHeader("Settings", "Manage your admin account and password.", null);

        // ----- account -----
        Button logoutBtn = new Button("Log Out");
        String logoutBase = "-fx-background-color: #fee2e2; -fx-text-fill: #b91c1c; -fx-font-weight: bold; " +
                "-fx-font-size: 13px; -fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 11 22 11 22;";
        String logoutHover = logoutBase.replace("#fee2e2", "#fecaca");
        logoutBtn.setStyle(logoutBase);
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle(logoutHover));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle(logoutBase));
        logoutBtn.setOnAction(e -> confirmLogout());
        VBox accountCard = glassCard(new VBox(10,
                sectionTitle("Account", ICON_SETTINGS),
                infoRow("Username", username),
                infoRow("Role", "Administrator"),
                logoutBtn));

        // ----- change password (checks the current password and saves the new one in PostgreSQL) -----
        PasswordField currentF = new PasswordField();
        currentF.setPromptText("Current password");
        styleInput(currentF);
        PasswordField newF = new PasswordField();
        newF.setPromptText("New password (at least " + UserStore.MIN_PASSWORD_LENGTH + " characters)");
        styleInput(newF);
        PasswordField confirmF = new PasswordField();
        confirmF.setPromptText("Confirm new password");
        styleInput(confirmF);
        Label pwMsg = new Label(" ");
        pwMsg.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));
        pwMsg.setWrapText(true);
        Button updatePw = primaryButton("Update Password");
        updatePw.setOnAction(e -> {
            String cur = currentF.getText(), nw = newF.getText(), cf = confirmF.getText();
            pwMsg.setTextFill(Color.web("#b91c1c"));
            if (cur.isEmpty() || nw.isEmpty() || cf.isEmpty()) {
                pwMsg.setText("Please fill out all password fields.");
            } else if (nw.length() < UserStore.MIN_PASSWORD_LENGTH) {
                pwMsg.setText("New password must be at least " + UserStore.MIN_PASSWORD_LENGTH + " characters.");
            } else if (!nw.equals(cf)) {
                pwMsg.setText("New password and confirmation don't match.");
            } else if (nw.equals(cur)) {
                pwMsg.setText("New password must be different from the current one.");
            } else {
                try {
                    if (!UserStore.changePassword(username, cur, nw)) {
                        pwMsg.setText("Your current password is incorrect.");
                        return;
                    }
                } catch (java.io.IOException ex) {
                    pwMsg.setText("Could not change the password. Please try again.");
                    return;
                }
                currentF.clear();
                newF.clear();
                confirmF.clear();
                pwMsg.setTextFill(Color.web(GREEN));
                pwMsg.setText("Your password was changed.");
                addNotification(ICON_SETTINGS, ORANGE, ORANGE_LIGHT, "Admin password was changed.");
            }
        });
        VBox passwordCard = glassCard(new VBox(10,
                sectionTitle("Change Password", ICON_SETTINGS),
                formLabel("Current Password"), currentF,
                formLabel("New Password"), newF,
                formLabel("Confirm New Password"), confirmF,
                pwMsg, updatePw));

        HBox columns = new HBox(20, accountCard, passwordCard);
        columns.setAlignment(Pos.TOP_LEFT);
        for (VBox card : new VBox[]{accountCard, passwordCard}) {
            HBox.setHgrow(card, Priority.ALWAYS);
            card.setPrefWidth(10);
            card.setMaxWidth(Double.MAX_VALUE);
        }

        body.getChildren().addAll(header, columns);
        return wrapScroll(body);
    }

    private CheckBox notifBox(String text, boolean selected) {
        CheckBox cb = new CheckBox(text);
        cb.setSelected(selected);
        cb.setFont(Font.font("Segoe UI", 14));
        cb.setTextFill(Color.web(NAVY));
        cb.setStyle("-fx-cursor: hand;");
        return cb;
    }

    private HBox infoRow(String label, String value) {
        Label l = new Label(label);
        l.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        l.setTextFill(Color.web(TEXT_MUTED));
        Label v = new Label(value);
        v.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        v.setTextFill(Color.web(NAVY_DEEP));
        HBox row = new HBox(10, l, spacer(), v);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 0, 6, 0));
        return row;
    }

    // ---------- Shared list-page template ----------

    private ScrollPane buildListPage(String title, String subtitle, String addLabel, String addMessage,
                                     HBox statRow, String[] columns, String[][] data) {
        VBox body = new VBox(22);
        body.setPadding(new Insets(28, 40, 28, 40));

        Button addBtn = primaryButton(addLabel);
        addBtn.setOnAction(e -> infoAlert(addMessage));
        body.getChildren().add(pageHeader(title, subtitle, addBtn));
        if (statRow != null) body.getChildren().add(statRow);
        body.getChildren().add(glassCard(buildTable(columns, data)));
        return wrapScroll(body);
    }

    // ---------- Helpers ----------

    private VBox glassCard(Node content) {
        VBox card = new VBox(content);
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setStyle(GLASS);
        card.setEffect(new DropShadow(24, Color.rgb(20, 40, 90, 0.14)));
        return card;
    }

    private HBox equalRow(Node... cards) {
        HBox row = new HBox(20, cards);
        for (Node n : row.getChildren()) {
            HBox.setHgrow(n, Priority.ALWAYS);
            ((Region) n).setMaxWidth(Double.MAX_VALUE);
            ((Region) n).setPrefWidth(10);
        }
        return row;
    }

    private VBox pageHeader(String titleText, String subText, Node action) {
        Label title = new Label(titleText);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(Color.web(NAVY_DEEP));
        Label sub = new Label(subText);
        sub.setFont(Font.font("Segoe UI", 13));
        sub.setTextFill(Color.web(TEXT_MUTED));
        sub.setWrapText(true);
        VBox textBox = new VBox(3, title, sub);

        HBox row = new HBox(12, textBox, spacer());
        if (action != null) row.getChildren().add(action);
        row.setAlignment(Pos.CENTER_LEFT);
        return glassCard(row);
    }

    private Label sectionTitle(String text, String iconPath) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
        l.setTextFill(Color.web(NAVY_DEEP));
        l.setGraphic(icon(iconPath, NAVY, 20));
        l.setGraphicTextGap(10);
        return l;
    }

    private Label linkLabel(String text, Runnable action) {
        Label link = new Label(text);
        link.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        link.setTextFill(Color.web(BLUE));
        link.setStyle("-fx-cursor: hand;");
        link.setOnMouseClicked(e -> action.run());
        return link;
    }

    private Button primaryButton(String text) {
        Button b = new Button(text);
        String base = "-fx-background-color: " + BLUE + "; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 13px; -fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 11 22 11 22;";
        String hover = base.replace(BLUE, BLUE_HOVER);
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(base));
        return b;
    }

    private Separator softSeparator() {
        Separator s = new Separator();
        s.setStyle("-fx-opacity: 0.45;");
        return s;
    }

    private Group icon(String path, String color, double size) {
        SVGPath p = new SVGPath();
        p.setContent(path);
        p.setFill(Color.web(color));
        double s = size / 24.0;
        p.setScaleX(s);
        p.setScaleY(s);
        return new Group(p);
    }

    private Group strokeIcon(String path, String color, double size) {
        SVGPath p = new SVGPath();
        p.setContent(path);
        p.setFill(null);
        p.setStroke(Color.web(color));
        p.setStrokeWidth(2);
        p.setStrokeLineCap(StrokeLineCap.ROUND);
        p.setStrokeLineJoin(StrokeLineJoin.ROUND);
        double sc = size / 24.0;
        p.setScaleX(sc);
        p.setScaleY(sc);
        return new Group(p);
    }

    private ScrollPane wrapScroll(Node body) {
        ScrollPane scroll = new ScrollPane(body);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scroll;
    }

    private Region spacer() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }

    // ---------- Pretty table (shared look for every list) ----------

    private static final double TABLE_ROW_H = 54;
    private static final double TABLE_HEADER_H = 48;

    private TableView<String[]> buildTable(String[] columns, String[][] data) {
        ObservableList<String[]> list = FXCollections.observableArrayList(data);
        return buildTableFromObservable(columns, list);
    }

    private TableView<String[]> buildTableFromObservable(String[] columns, ObservableList<String[]> list) {
        TableView<String[]> table = new TableView<>(list);
        table.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent; " +
                "-fx-table-cell-border-color: transparent; -fx-background-insets: 0; -fx-padding: 0; " +
                "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-font-size: 14px;");
        table.setFixedCellSize(TABLE_ROW_H);
        table.setPlaceholder(new Label("No records yet."));

        for (int i = 0; i < columns.length; i++) {
            final int idx = i;
            final String name = columns[i];
            TableColumn<String[], String> col = new TableColumn<>(name);
            col.setCellValueFactory(row -> new javafx.beans.property.SimpleStringProperty(row.getValue()[idx]));
            col.prefWidthProperty().bind(table.widthProperty().divide(columns.length).subtract(2));
            col.setSortable(false);
            col.setStyle("-fx-alignment: CENTER_LEFT; -fx-padding: 0 16 0 16; -fx-text-fill: " + NAVY + ";");
            col.setCellFactory(c -> new TableCell<String[], String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else if (name.equals("STATUS") || name.equals("TYPE")) {
                        setText(null);
                        setGraphic(badge(name, item));
                    } else {
                        setText(item);
                        setGraphic(null);
                    }
                }
            });
            table.getColumns().add(col);
        }

        styleRowsAndHeight(table);
        return table;
    }

    /**
     * Copies a picked photo into ./event_photos so the event keeps its image even if the
     * original file is moved or deleted. Falls back to the original path if copying fails.
     */
    private String copyEventPhoto(String source) {
        try {
            Path dir = Paths.get(System.getProperty("user.dir"), "event_photos");
            Files.createDirectories(dir);
            Path src = Paths.get(source);
            String base = src.getFileName().toString().replaceAll("[^A-Za-z0-9._-]", "_");
            Path dest = dir.resolve(System.currentTimeMillis() + "_" + base);
            Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
            return dest.toString();
        } catch (Exception ex) {
            return source;
        }
    }

    private void styleRowsAndHeight(TableView<String[]> table) {
        table.setRowFactory(tv -> {
            TableRow<String[]> row = new TableRow<>();
            Runnable paint = () -> {
                if (row.isEmpty()) {
                    row.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
                    return;
                }
                String bg = row.isSelected() ? "rgba(37,99,235,0.14)"
                        : row.isHover() ? "rgba(37,99,235,0.08)"
                        : (row.getIndex() % 2 == 0 ? "rgba(255,255,255,0.60)" : "rgba(255,255,255,0.28)");
                row.setStyle("-fx-background-color: " + bg + "; -fx-background-insets: 0; " +
                        "-fx-border-color: transparent transparent rgba(27,42,74,0.07) transparent;");
            };
            row.emptyProperty().addListener((o, a, b) -> paint.run());
            row.hoverProperty().addListener((o, a, b) -> paint.run());
            row.selectedProperty().addListener((o, a, b) -> paint.run());
            row.indexProperty().addListener((o, a, b) -> paint.run());
            paint.run();
            return row;
        });

        table.prefHeightProperty().bind(javafx.beans.binding.Bindings.createDoubleBinding(
                () -> Math.max(115.0, table.getItems().size() * TABLE_ROW_H + TABLE_HEADER_H + 6),
                table.getItems()));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());

        table.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin != null) javafx.application.Platform.runLater(() -> styleTableHeader(table));
        });
        table.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) javafx.application.Platform.runLater(() -> styleTableHeader(table));
        });
    }

    private void styleTableHeader(TableView<?> table) {
        Node bg = table.lookup(".column-header-background");
        if (bg != null) bg.setStyle("-fx-background-color: transparent;");
        Node filler = table.lookup(".column-header-background .filler");
        if (filler != null) filler.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        for (Node n : table.lookupAll(".column-header")) {
            n.setStyle("-fx-background-color: transparent; " +
                    "-fx-border-color: transparent transparent rgba(27,42,74,0.18) transparent; " +
                    "-fx-border-width: 0 0 1.5 0; -fx-size: " + TABLE_HEADER_H + "px;");
        }
        for (Node n : table.lookupAll(".column-header .label")) {
            n.setStyle("-fx-text-fill: #5b6b86; -fx-font-weight: bold; -fx-font-size: 12px; " +
                    "-fx-alignment: CENTER_LEFT; -fx-padding: 0 16 0 16;");
        }
    }

    private Label badge(String column, String text) {
        String t = text.trim().toLowerCase(java.util.Locale.ENGLISH);
        String fg, bg;
        if (column.equals("TYPE")) {
            if (t.equals("cash")) { fg = "#1d4ed8"; bg = "#dbeafe"; }
            else if (t.equals("in-kind") || t.equals("in kind") || t.equals("goods")) { fg = "#6d28d9"; bg = "#ede9fe"; }
            else if (t.equals("volunteer")) { fg = "#7c3aed"; bg = "#ede9fe"; }
            else if (t.equals("donation")) { fg = "#ea580c"; bg = "#ffedd5"; }
            else if (t.equals("event")) { fg = "#2563eb"; bg = "#dbeafe"; }
            else { fg = "#475569"; bg = "#e2e8f0"; }
        } else if (t.matches("received|processed|approved|active|completed|confirmed|verified|done|joined|registered")) {
            fg = "#15803d"; bg = "#dcfce7";
        } else if (t.matches("pending|ongoing|in progress|processing|review|full|upcoming|scheduled|registration open")) {
            fg = "#c2410c"; bg = "#ffedd5";
        } else if (t.matches("rejected|cancelled|canceled|declined|inactive|closed")) {
            fg = "#b91c1c"; bg = "#fee2e2";
        } else {
            fg = "#1d4ed8"; bg = "#dbeafe";
        }
        Label l = new Label(text);
        l.setStyle("-fx-background-color: " + bg + "; -fx-text-fill: " + fg + "; -fx-font-weight: bold; " +
                "-fx-font-size: 12px; -fx-padding: 5 13 5 13; -fx-background-radius: 20;");
        return l;
    }

    // ---------- Newest first ----------

    private static void sortNewestFirst(List<String[]> rows, int dateCol, int numCol) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
        rows.sort((a, b) -> {
            int c = parseRowDate(b[dateCol], f).compareTo(parseRowDate(a[dateCol], f));
            return c != 0 ? c : Integer.compare(rowNumber(b[numCol]), rowNumber(a[numCol]));
        });
    }

    private static LocalDate parseRowDate(String s, DateTimeFormatter f) {
        try {
            return LocalDate.parse(s.trim(), f);
        } catch (Exception e) {
            return LocalDate.MIN;
        }
    }

    /** "3 / 20 joined (15%)" when the admin set how many volunteers are needed, otherwise "3 joined". */
    private static String joinedText(String[] ev) {
        int needed = ev.length > 8 ? rowNumber(ev[8]) : 0;
        return needed > 0 ? ev[5] + " / " + needed + " joined (" + volunteerPercent(ev) + "%)" : ev[5] + " joined";
    }

    /** How full the event is, 0-100 (0 when the admin set no number of volunteers needed). */
    private static int volunteerPercent(String[] ev) {
        int needed = ev.length > 8 ? rowNumber(ev[8]) : 0;
        if (needed <= 0) return 0;
        return Math.min(100, (int) Math.round(rowNumber(ev[5]) * 100.0 / needed));
    }

    private ProgressBar volunteerBar(String[] ev) {
        ProgressBar bar = new ProgressBar(volunteerPercent(ev) / 100.0);
        bar.setMaxWidth(Double.MAX_VALUE);
        bar.setStyle("-fx-accent: " + BLUE + ";");
        return bar;
    }

    private static int rowNumber(String s) {
        try {
            return Integer.parseInt(s.replaceAll("\\D", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void infoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}