import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Pos;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.StageStyle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UserDashboardFX {

    // ---------- Colors (same palette as the login screen) ----------
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
    private static final String ICON_HOME = "M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z";
    private static final String ICON_CALENDAR = "M17 12h-5v5h5v-5zM16 1v2H8V1H6v2H5c-1.11 0-1.99.9-1.99 2L3 19c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2h-1V1h-2zm3 18H5V8h14v11z";
    private static final String ICON_CHECK = "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z";
    private static final String ICON_MONEY = "M11.8 10.9c-2.27-.59-3-1.2-3-2.15 0-1.09 1.01-1.85 2.7-1.85 1.78 0 2.44.85 2.5 2.1h2.21c-.07-1.72-1.12-3.3-3.21-3.81V3h-3v2.16c-1.94.42-3.5 1.68-3.5 3.61 0 2.31 1.91 3.46 4.7 4.13 2.5.6 3 1.48 3 2.41 0 .69-.49 1.79-2.7 1.79-2.06 0-2.87-.92-2.98-2.1h-2.2c.12 2.19 1.76 3.42 3.68 3.83V21h3v-2.15c1.95-.37 3.5-1.5 3.5-3.55 0-2.84-2.43-3.81-4.7-4.4z";
    private static final String ICON_PEOPLE = "M16 11c1.66 0 2.99-1.34 2.99-3S17.66 5 16 5c-1.66 0-3 1.34-3 3s1.34 3 3 3zm-8 0c1.66 0 2.99-1.34 2.99-3S9.66 5 8 5C6.34 5 5 6.34 5 8s1.34 3 3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z";
    private static final String ICON_WARN = "M1 21h22L12 2 1 21zm12-3h-2v-2h2v2zm0-4h-2v-4h2v4z";
    private static final String ICON_LIST = "M4 10.5c-.83 0-1.5.67-1.5 1.5s.67 1.5 1.5 1.5 1.5-.67 1.5-1.5-.67-1.5-1.5-1.5zm0-6c-.83 0-1.5.67-1.5 1.5S3.17 7.5 4 7.5 5.5 6.83 5.5 6 4.83 4.5 4 4.5zm0 12c-.83 0-1.5.68-1.5 1.5s.68 1.5 1.5 1.5 1.5-.68 1.5-1.5-.67-1.5-1.5-1.5zM7 19h14v-2H7v2zm0-6h14v-2H7v2zm0-8v2h14V5H7z";
    private static final String ICON_FORM = "M19 3h-4.18C14.4 1.84 13.3 1 12 1c-1.3 0-2.4.84-2.82 2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-7 0c.55 0 1 .45 1 1s-.45 1-1 1-1-.45-1-1 .45-1 1-1zm2 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z";
    private static final String ICON_SEND = "M2.01 21L23 12 2.01 3 2 10l15 2-15 2z";
    private static final String ICON_CHEVRON = "M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z";
    private static final String EV_PIN = "M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z";
    private static final String EV_PHONE = "M6.62 10.79c1.44 2.83 3.76 5.14 6.59 6.59l2.2-2.2c.27-.27.67-.36 1.02-.24 1.12.37 2.33.57 3.57.57.55 0 1 .45 1 1V20c0 .55-.45 1-1 1-9.39 0-17-7.61-17-17 0-.55.45-1 1-1h3.5c.55 0 1 .45 1 1 0 1.25.2 2.45.57 3.57.11.35.03.74-.25 1.02l-2.2 2.2z";
    private static final String EV_BACK = "M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z";
    private static final String EV_CLOSE = "M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z";
    private static final String EV_TICK = "M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z";
    private static final String EV_SHIELD = "M12 1L3 5v6c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V5l-9-4zm-2 16l-4-4 1.41-1.41L10 14.17l6.59-6.59L18 9l-8 8z";
    private static final String EV_SEARCH = "M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z";
    private static final String EV_STAR = "M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z";
    private static final String EV_HOURGLASS = "M6 2v6h.01L6 8.01 10 12l-4 4 .01.01H6V22h12v-5.99h-.01L18 16l-4-4 4-3.99-.01-.01H18V2H6zm10 14.5V20H8v-3.5l4-4 4 4zm-4-5l-4-4V4h8v3.5l-4 4z";
    private static final String EV_GLOBE = "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 17.93c-3.95-.49-7-3.85-7-7.93 0-.62.08-1.21.21-1.79L9 15v1c0 1.1.9 2 2 2v1.93zm6.9-2.54c-.26-.81-1-1.39-1.9-1.39h-1v-3c0-.55-.45-1-1-1H8v-2h2c.55 0 1-.45 1-1V7h2c1.1 0 2-.9 2-2v-.41c2.93 1.19 5 4.06 5 7.41 0 2.08-.8 3.97-2.1 5.39z";
    private static final String EV_INFO = "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-6h2v6zm0-8h-2V7h2v2z";
    private static final String ICON_HEART = "M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z";
    private static final String ICON_CLOCK = "M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm.5-13H11v6l5.25 3.15.75-1.23-4.5-2.67z";
    private static final String ICON_BELL = "M12 22c1.1 0 2-.9 2-2h-4c0 1.1.89 2 2 2zm6-6v-5c0-3.07-1.64-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.63 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2z";
    private static final String ICON_MEDICAL = "M19 3H5c-1.1 0-1.99.9-1.99 2L3 19c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-1 11h-4v4h-4v-4H6v-4h4V6h4v4h4v4z";

    private static final String ICON_SETTINGS = "M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58c.18-.14.23-.41.12-.61l-1.92-3.32c-.12-.22-.37-.29-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54c-.04-.24-.24-.41-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.09.63-.09.94s.02.64.07.94l-2.03 1.58c-.18.14-.23.41-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z";
    private static final String ICON_PERSON = "M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z";
    private static final String ICON_LOCK = "M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z";

    private static final String ICON_DASHBOARD = "M3 13h8V3H3v10zm0 8h8v-6H3v6zm10 0h8V11h-8v10zm0-18v6h8V3h-8z";
    private static final String ICON_LOGOUT = "M17 7l-1.41 1.41L18.17 11H8v2h10.17l-2.58 2.58L17 17l5-5zM4 5h8V3H4c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h8v-2H4V5z";
    // Outline icons for the sidebar toggle (round caps, same style for << and >>)
    private static final String ICON_CHEVRONS_LEFT = "M18 7l-5 5l5 5M12 7l-5 5l5 5";
    private static final String ICON_CHEVRONS = "M7 7l5 5l-5 5M13 7l5 5l-5 5";

    // Outline icons (stroke style) for the sidebar and the home stat cards
    private static final String S_HOME = "M3 9 l9 -7 9 7 v11 a2 2 0 0 1 -2 2 H5 a2 2 0 0 1 -2 -2 z M9 22 V12 h6 v10";
    private static final String S_HEART = "M20.84 4.61 a5.5 5.5 0 0 0 -7.78 0 L12 5.67 l-1.06 -1.06 a5.5 5.5 0 0 0 -7.78 7.78 l1.06 1.06 L12 21.23 l7.78 -7.78 1.06 -1.06 a5.5 5.5 0 0 0 0 -7.78 z";
    private static final String S_USERS = "M17 21 v-2 a4 4 0 0 0 -4 -4 H5 a4 4 0 0 0 -4 4 v2 M9 11 a4 4 0 1 0 0 -8 a4 4 0 1 0 0 8 z M23 21 v-2 a4 4 0 0 0 -3 -3.87 M16 3.13 a4 4 0 0 1 0 7.75";
    private static final String S_CALENDAR = "M5 4 h14 a2 2 0 0 1 2 2 v14 a2 2 0 0 1 -2 2 H5 a2 2 0 0 1 -2 -2 V6 a2 2 0 0 1 2 -2 z M16 2 v4 M8 2 v4 M3 10 h18";
    private static final String S_CAL_CHECK = S_CALENDAR + " M9 16 l2 2 l4 -4";
    private static final String S_ACTIVITY = "M22 12 h-4 l-3 9 L9 3 l-3 9 H2";
    private static final String S_GIFT = "M20 12 v10 H4 V12 M2 7 h20 v5 H2 z M12 22 V7 M12 7 H7.5 a2.5 2.5 0 0 1 0 -5 C11 2 12 7 12 7 z M12 7 h4.5 a2.5 2.5 0 0 0 0 -5 C13 2 12 7 12 7 z";
    private static final String S_USER_CHECK = "M16 21 v-2 a4 4 0 0 0 -4 -4 H5 a4 4 0 0 0 -4 4 v2 M8.5 11 a4 4 0 1 0 0 -8 a4 4 0 1 0 0 8 z M17 11 l2 2 l4 -4";

    private static final double SIDEBAR_EXPANDED = 270;
    private static final double SIDEBAR_COLLAPSED = 82;

    private final String username;
    private final Stage stage = new Stage();
    private final StackPane contentArea = new StackPane();
    private final Map<String, Node> pages = new HashMap<>();
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
    private StackPane sidebar;   // outer box: its width animates between 0 and SIDEBAR_EXPANDED
    private StackPane avatarCircle;   // profile circle at the top of the sidebar
    private StackPane topAvatar;      // small profile circle in the top bar
    private Image profileImage;       // null = default person icon
    private Label greeting;
    private Button toggleBtn;
    private Group chevronsLeftIcon, chevronsRightIcon;
    private Timeline sidebarAnim;
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
    private boolean sidebarCollapsed = true;   // starts small (icon strip) after login
    private String currentPage = "home";
    private Label errorLabel;

    // donations page state (so the Donate form can add rows and update the counters)
    private Runnable resetCashForm;

    // the donations (set up by the admin) that the Cash / In-Kind Donation pages are showing
    private CampaignStore.Campaign cashCampaign;
    private CampaignStore.Campaign inKindCampaign;

    // volunteers / events / my activities state
    private boolean volunteerRegistered = false;
    /** True once the admin approved the volunteer registration; only then can the user join events. */
    private boolean volunteerApproved = false;
    // ids of the events this user joined (same ids as the admin's events in the database)
    private final java.util.Set<String> joinedEvents = new java.util.HashSet<>();
    /** event id -> this user's registration status as set by the admin (Pending / Approved / Rejected). */
    private final Map<String, String> eventRegStatus = new HashMap<>();
    private final List<Node> registerBanners = new ArrayList<>();
    // every row: {type, activity, date, status, transaction id, amount}
    private final List<String[]> activityRows = new ArrayList<>();
    private Runnable refreshActivities;
    private java.util.function.Consumer<String> setActivityFilter;
    private Label donationsCountLbl;
    private int evtTxnCounter = 0, volTxnCounter = 0;
    private final Map<String, Button> eventJoinButtons = new HashMap<>();
    private String volunteerName = "", volunteerContact = "";
    private Label eventsJoinedLbl;
    private Label eventsAvailableLbl;
    // redraws the Events page cards + filter chips from eventRows (set when the page is built)
    private Runnable reloadEventsPage = () -> {};
    private Timeline eventsPoll;
    private final java.util.concurrent.atomic.AtomicBoolean eventsRefreshing = new java.util.concurrent.atomic.AtomicBoolean(false);
    private volatile int joinStamp = 0;   // bumped on every successful join, so an older refresh cannot undo it

    /** The "Welcome back" line on the home page (kept so it follows a name change in Edit Profile). */
    private Label welcomeLbl;

    /** The user's full name for greetings (never the login email, unless the account has no name). */
    private String displayName() {
        String[] account = UserStore.lookup(username);
        return account != null && account[0] != null && !account[0].isBlank() ? account[0].trim() : username;
    }

    public UserDashboardFX(String username) {
        this.username = username;
        java.util.Map<String, String> startStatuses = EventStore.registrationStatusesOrNull(username);
        if (startStatuses != null) {
            eventRegStatus.putAll(startStatuses);
            joinedEvents.addAll(startStatuses.keySet());
        }
        volunteerRegistered = VolunteerStore.isRegistered(username);
        volunteerApproved = volunteerRegistered && VolunteerStore.isApproved(username);
        if (volunteerRegistered) {
            String[] reg = VolunteerStore.registeredDetails(username);
            if (reg != null) {
                volunteerName = reg[0];
                volunteerContact = reg[1];
            }
        }
        build();
    }

    public void show() {
        // Smooth fade-in when opening (crossfades with the login window fading out)
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
        pages.put("donations", buildDonationsPage());
        pages.put("volunteers", withWaves(buildVolunteersPage()));
        pages.put("events", buildEventsPage());
        pages.put("activities", withWaves(buildActivitiesPage()));
        pages.put("register", buildRegisterPage());
        pages.put("donate", new StackPane()); // built by openInKindDonation() from the admin's donation
        pages.put("gcash", new StackPane()); // built by openCashDonation() from the admin's donation
        pages.put("settings", withWaves(buildSettingsPage()));

        VBox right = new VBox(buildTopBar(), contentArea);
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        root.setLeft(buildSidebar());
        root.setCenter(right);

        Scene scene = new Scene(root, 1050, 650);
        stage.setTitle("VoNation - Dashboard");
        stage.setScene(scene);
        stage.setMaximized(true);
        showPage("home");

        // Pick up what the admin adds / changes while this window is open (every few seconds).
        eventsPoll = new Timeline(new KeyFrame(javafx.util.Duration.seconds(4), ev -> refreshEventsFromDb()));
        eventsPoll.setCycleCount(Timeline.INDEFINITE);
        eventsPoll.play();
        homePoll = new Timeline(new KeyFrame(javafx.util.Duration.seconds(8), ev -> refreshHomeContent()));
        homePoll.setCycleCount(Timeline.INDEFINITE);
        homePoll.play();
        stage.setOnHidden(ev -> {
            eventsPoll.stop();
            homePoll.stop();
        });
    }

    /** Soft light wave shapes at the bottom of a page (behind the content, never scrolls). */
    private StackPane withWaves(Node page) {
        Pane waves = new Pane();
        waves.setMouseTransparent(true);
        javafx.scene.shape.Path p1 = new javafx.scene.shape.Path();
        javafx.scene.shape.Path p2 = new javafx.scene.shape.Path();
        javafx.scene.shape.Path p3 = new javafx.scene.shape.Path();
        for (javafx.scene.shape.Path p : new javafx.scene.shape.Path[]{p1, p2, p3}) p.setStroke(null);
        p1.setFill(Color.rgb(255, 255, 255, 0.30));
        p2.setFill(Color.rgb(140, 180, 240, 0.28));
        p3.setFill(Color.rgb(255, 255, 255, 0.22));
        waves.getChildren().addAll(p1, p2, p3);

        Runnable layout = () -> {
            double w = waves.getWidth(), h = waves.getHeight();
            if (w <= 0 || h <= 0) return;
            double b = Math.min(320, h * 0.35);   // height of the wave band
            double y = h - b;
            fillWave(p1, w, h, y + 0.25 * b, new double[]{0.25, y - 0.05 * b, 0.50, y + 0.45 * b, 0.75, y + 0.15 * b,
                    0.88, y - 0.02 * b, 0.95, y + 0.05 * b, 1.0, y - 0.05 * b});
            fillWave(p2, w, h, y + 0.60 * b, new double[]{0.30, y + 0.30 * b, 0.55, y + 0.85 * b, 0.80, y + 0.35 * b,
                    0.90, y + 0.20 * b, 0.96, y + 0.25 * b, 1.0, y + 0.20 * b});
            fillWave(p3, w, h, y + 0.90 * b, new double[]{0.20, y + 0.65 * b, 0.50, y + 1.00 * b, 0.70, y + 0.75 * b,
                    0.85, y + 0.60 * b, 0.95, y + 0.70 * b, 1.0, y + 0.55 * b});
        };
        waves.widthProperty().addListener((obs, o, n) -> layout.run());
        waves.heightProperty().addListener((obs, o, n) -> layout.run());
        return new StackPane(waves, page);
    }

    /** c = {x1f, y1, x2f, y2, x3f, y3, x4f, y4, x5f, y5, x6f, y6}: x as a fraction of the width, y in pixels. */
    private void fillWave(javafx.scene.shape.Path path, double w, double h, double startY, double[] c) {
        path.getElements().setAll(
                new javafx.scene.shape.MoveTo(0, startY),
                new javafx.scene.shape.CubicCurveTo(w * c[0], c[1], w * c[2], c[3], w * c[4], c[5]),
                new javafx.scene.shape.CubicCurveTo(w * c[6], c[7], w * c[8], c[9], w * c[10], c[11]),
                new javafx.scene.shape.LineTo(w, h),
                new javafx.scene.shape.LineTo(0, h),
                new javafx.scene.shape.ClosePath());
    }

    /** Light decoration for the right side of the Volunteers banner: soft circles and waves. */
    private Pane heroDecoration() {
        Pane deco = new Pane();
        deco.setMinSize(360, 230);
        deco.setPrefSize(360, 230);
        deco.setMaxSize(360, 230);
        deco.setMouseTransparent(true);
        Circle big = new Circle(270, 78, 52, Color.rgb(255, 255, 255, 0.40));
        Circle mid = new Circle(215, 120, 30, Color.rgb(150, 185, 240, 0.25));
        Circle small = new Circle(318, 150, 18, Color.rgb(255, 255, 255, 0.45));
        javafx.scene.shape.Path w1 = new javafx.scene.shape.Path(
                new javafx.scene.shape.MoveTo(0, 165),
                new javafx.scene.shape.CubicCurveTo(90, 125, 180, 205, 270, 155),
                new javafx.scene.shape.CubicCurveTo(310, 133, 340, 140, 360, 125),
                new javafx.scene.shape.LineTo(360, 230),
                new javafx.scene.shape.LineTo(0, 230),
                new javafx.scene.shape.ClosePath());
        w1.setStroke(null);
        w1.setFill(Color.rgb(255, 255, 255, 0.35));
        javafx.scene.shape.Path w2 = new javafx.scene.shape.Path(
                new javafx.scene.shape.MoveTo(0, 198),
                new javafx.scene.shape.CubicCurveTo(100, 165, 200, 225, 290, 183),
                new javafx.scene.shape.CubicCurveTo(320, 168, 345, 173, 360, 163),
                new javafx.scene.shape.LineTo(360, 230),
                new javafx.scene.shape.LineTo(0, 230),
                new javafx.scene.shape.ClosePath());
        w2.setStroke(null);
        w2.setFill(Color.rgb(140, 180, 240, 0.30));
        deco.getChildren().addAll(big, mid, small, w1, w2);
        return deco;
    }

    private void showPage(String name) {
        if (name.equals("settings")) updateProfileLabels();
        if (name.equals("home") && refreshRecent != null) refreshRecent.run();
        if (name.equals("events") || name.equals("home")) refreshEventsFromDb();
        if (name.equals("home")) refreshHomeContent();
        if (name.equals("volunteers")) refreshVolunteerStatusBtn.run();   // the admin may have approved / changed the status
        if (name.equals("donations")) pages.put("donations", buildDonationsPage()); // the admin may have added / changed donations
        contentArea.getChildren().setAll(pages.get(name));
        // the register page belongs to the "Volunteers" tab, the donate page to "Donations"
        currentPage = name.equals("register") ? "volunteers"
                : (name.equals("donate") || name.equals("gcash")) ? "donations" : name;
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
        // keep contents inside while the width animates
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(sidebar.widthProperty());
        clip.heightProperty().bind(sidebar.heightProperty());
        sidebar.setClip(clip);

        // profile circle (default person icon, or the user's photo)
        avatarCircle = new StackPane();
        avatarCircle.setStyle("-fx-background-color: white; -fx-background-radius: 100; -fx-cursor: hand;");
        Tooltip.install(avatarCircle, new Tooltip("Click to change your photo"));
        avatarCircle.setOnMouseClicked(e -> choosePhoto());

        // the user's full name (not the email, and no "Hi,")
        String[] account = UserStore.lookup(username);
        String shownName = account != null && account[0] != null && !account[0].isBlank() ? account[0] : username;
        greeting = new Label(shownName);
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
                sideItem("home", "Home", S_HOME, () -> showPage("home")),
                sideGap(14),
                sideHeader("Management"),
                sideItem("donations", "Donations", S_HEART, () -> showPage("donations")),
                sideItem("volunteers", "Volunteers", S_USERS, () -> showPage("volunteers")),
                sideItem("events", "Events", S_CALENDAR, () -> showPage("events")),
                sideItem("activities", "My Activities", S_ACTIVITY, () -> showPage("activities"))
        );


        applySidebarMode(!sidebarCollapsed);
        return sidebar;
    }

    private HBox sideItem(String key, String text, String iconPath, Runnable action) {
        Circle dot = new Circle(5, Color.web("#fbbf24")); // yellow marker for the active page
        dot.setVisible(false);
        Group ic = strokeIcon(iconPath, "#aeb9d1", 22);
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
        if (n instanceof SVGPath) {
            SVGPath sp = (SVGPath) n;
            Color c = Color.web(active ? "#ffffff" : "#aeb9d1");
            if (sp.getFill() == null) sp.setStroke(c); else sp.setFill(c);   // outline icons use stroke
        }
        // strip mode: icons only, centered
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

    private Region sideDivider() {
        Region r = new Region();
        r.setMinHeight(1);
        r.setMaxHeight(1);
        r.setStyle("-fx-background-color: rgba(255,255,255,0.14);");
        VBox.setMargin(r, new Insets(12, 22, 12, 22));
        return r;
    }

    private Region sideGap(double h) {
        Region r = new Region();
        r.setMinHeight(h);
        r.setMaxHeight(h);
        return r;
    }

    /** Full sidebar (text shown) or icon strip (text hidden). */
    private void applySidebarMode(boolean showText) {
        for (Label l : sideTexts) { l.setVisible(showText); l.setManaged(showText); }
        for (Node n : sideHeaders) { n.setVisible(showText); n.setManaged(showText); }
        greeting.setVisible(showText);
        greeting.setManaged(showText);
        refreshAvatars();
        for (Map.Entry<String, SideItem> en : sideItems.entrySet()) {
            styleSideItem(en.getValue(), en.getKey().equals(currentPage));
        }
    }

    /** << shrinks the sidebar to an icon strip (icon becomes >>); >> expands it again. */
    private void toggleSidebar() {
        sidebarCollapsed = !sidebarCollapsed;
        toggleBtn.setGraphic(sidebarCollapsed ? chevronsRightIcon : chevronsLeftIcon);

        if (sidebarAnim != null) sidebarAnim.stop();
        sidebarAnim = new Timeline(new KeyFrame(Duration.millis(220),
                new KeyValue(sidebar.prefWidthProperty(),
                        sidebarCollapsed ? SIDEBAR_COLLAPSED : SIDEBAR_EXPANDED, Interpolator.EASE_BOTH)));
        applySidebarMode(false); // text is hidden while sliding; it comes back after expanding
        if (!sidebarCollapsed) {
            sidebarAnim.setOnFinished(e -> applySidebarMode(true));
        }
        sidebarAnim.play();
    }

    // ---------- Profile photo ----------

    /** Updates the sidebar circle and the small top-bar circle. */
    private void refreshAvatars() {
        double d = sidebarCollapsed ? 48 : 92;
        avatarCircle.setPrefSize(d, d);
        avatarCircle.setMinSize(d, d);
        avatarCircle.setMaxSize(d, d);
        if (profileImage != null) {
            avatarCircle.getChildren().setAll(photoNode(profileImage, d));
        } else {
            avatarCircle.getChildren().setAll(icon(ICON_PERSON, "#9aa7c0", d * 0.58)); // default
        }
        fillProfileAvatar();
        if (topAvatar != null) {
            if (profileImage != null) {
                topAvatar.getChildren().setAll(photoNode(profileImage, 40));
            } else {
                topAvatar.getChildren().setAll(topInitial());
            }
        }
    }

    private Label topInitial() {
        Label l = new Label(username.substring(0, 1).toUpperCase());
        l.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px;");
        return l;
    }

    /** Square-crops the picture from the center and clips it to a circle. */
    private ImageView photoNode(Image img, double d) {
        ImageView iv = new ImageView(img);
        double w = img.getWidth(), h = img.getHeight(), side = Math.min(w, h);
        iv.setViewport(new Rectangle2D((w - side) / 2, (h - side) / 2, side, side));
        iv.setFitWidth(d);
        iv.setFitHeight(d);
        iv.setClip(new Circle(d / 2, d / 2, d / 2));
        return iv;
    }

    private void choosePhoto() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose a profile photo");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        java.io.File f = fc.showOpenDialog(stage);
        if (f == null) return;
        Image img = new Image(f.toURI().toString());
        if (img.isError() || img.getWidth() <= 0) {
            infoAlert("Sorry, that image couldn't be opened.");
            return;
        }
        profileImage = img;
        refreshAvatars();
        addNotification(ICON_PERSON, BLUE, BLUE_LIGHT, "Your profile photo was updated.");
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

    // ---------- Top bar ----------

    private HBox buildTopBar() {
        HBox bar = new HBox(16);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(12, 28, 12, 24));
        bar.setStyle("-fx-background-color: rgba(255,255,255,0.65); " +
                "-fx-border-color: rgba(255,255,255,0.9); -fx-border-width: 0 0 1.5 0;");

        // Sidebar toggle: << when open, >> when collapsed
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
        Label brandSub = new Label("Volunteer and Donation Management System");
        brandSub.setFont(Font.font("Segoe UI", 11));
        brandSub.setTextFill(Color.web(TEXT_MUTED));
        brandText.getChildren().addAll(brandTitle, brandSub);

        // Notification bell (with unread badge) + settings gear, at the far right
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

        ImageView topLogo = loadLogo(44);   // VoNation logo between the >> button and the name
        if (topLogo != null) bar.getChildren().addAll(toggleBtn, topLogo, brandText, spacer(), topActions);
        else bar.getChildren().addAll(toggleBtn, brandText, spacer(), topActions);
        return bar;
    }

    /** Square icon button for the top bar (same look as the sidebar toggle). */
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
                "Welcome to VoNation, " + displayName() + "!", now, true));
    }

    /** Adds a new (unread) notification at the top and updates the bell badge. */
    private void addNotification(String iconPath, String color, String bg, String text) {
        notifications.add(0, new Notif(iconPath, color, bg, text, System.currentTimeMillis(), true));
        refreshBellBadge();
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

    /** Dropdown under the bell: list of notifications, click one to mark it read. */
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

        StackPane wrap = new StackPane(box);   // padding so the shadow isn't cut off
        wrap.setPadding(new Insets(14));
        pop.getContent().add(wrap);

        Bounds b = topActions.localToScreen(topActions.getBoundsInLocal());
        double popY = b.getMaxY() - 2;
        double windowBottom = stage.getY() + stage.getHeight();
        double boxH = Math.max(200, windowBottom - popY - 14 * 2);   // fill down to the bottom
        box.setPrefHeight(boxH);
        box.setMinHeight(boxH);
        box.setMaxHeight(boxH);
        pop.show(topActions, b.getMaxX() - POP_W - 14, popY);
    }

    private ImageView loadLogo(double height) {
        try {
            java.io.InputStream in = getClass().getResourceAsStream("/images/Logo_ni_vonation.png");
            if (in == null) {
                System.out.println("LOGO ERROR: /images/Logo_ni_vonation.png not found");
                return null;
            }
            ImageView iv = new ImageView(new Image(in));
            iv.setFitHeight(height);
            iv.setPreserveRatio(true);
            return iv;
        } catch (Exception e) {
            System.out.println("LOGO ERROR: " + e.getMessage());
            return null;
        }
    }

    // ---------- Home page ----------

    private ScrollPane buildHomePage() {
        VBox body = new VBox(22);
        body.setPadding(new Insets(28, 40, 28, 40));

        Label welcome = new Label("Welcome back, " + displayName() + "!");
        welcomeLbl = welcome;
        welcome.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        welcome.setTextFill(Color.web(NAVY_DEEP));
        Label welcomeSub = new Label("Here's what's happening in your community today.");
        welcomeSub.setFont(Font.font("Segoe UI", 14));
        welcomeSub.setTextFill(Color.web(TEXT_MUTED));
        VBox welcomeBox = new VBox(2, welcome, welcomeSub);

        // ----- the 4 cards on top; each one opens the page it belongs to -----
        VBox availableCard = statCard(S_CALENDAR, BLUE, BLUE_LIGHT, "Events Available",
                String.valueOf(eventRows.size()), "From admin", TEXT_MUTED);
        eventsAvailableLbl = (Label) availableCard.getUserData();
        makeClickable(availableCard, () -> showPage("events"));

        VBox joinedCard = statCard(S_CAL_CHECK, GREEN, GREEN_LIGHT, "Events Joined", String.valueOf(joinedCount()), "My registrations", GREEN);
        eventsJoinedLbl = (Label) joinedCard.getUserData();
        makeClickable(joinedCard, () -> {
            if (setActivityFilter != null) setActivityFilter.accept("Event");
            showPage("activities");
        });

        VBox donationsCard = statCard(S_GIFT, ORANGE, ORANGE_LIGHT, "My Donations", String.valueOf(DonationStore.byDonor(username).size()), "Total submitted", GREEN);
        donationsCountLbl = (Label) donationsCard.getUserData();
        makeClickable(donationsCard, () -> {
            if (setActivityFilter != null) setActivityFilter.accept("Donation");
            showPage("activities");
        });

        VBox volunteerCard = statCard(S_USER_CHECK, PURPLE, PURPLE_LIGHT, "Volunteer Status",
                "\u2014", "Not registered", TEXT_MUTED);
        makeClickable(volunteerCard, () -> showPage("volunteers"));

        HBox statRow = new HBox(20, availableCard, joinedCard, donationsCard, volunteerCard);
        for (Node n : statRow.getChildren()) {
            HBox.setHgrow(n, Priority.ALWAYS);
            ((Region) n).setMaxWidth(Double.MAX_VALUE);
            ((Region) n).setPrefWidth(10); // equal widths
        }

        // ----- sliding photo banner: the admin's announcements, donations and events (empty = hidden) -----
        carouselHolder.visibleProperty().bind(javafx.beans.binding.Bindings.isNotEmpty(carouselHolder.getChildren()));
        carouselHolder.managedProperty().bind(carouselHolder.visibleProperty());

        // ----- panels: Announcements + Recent Activity (left), Advisory + About Us (right) -----
        VBox leftCol = new VBox(20, announcementsHolder, buildActivityCard());
        HBox.setHgrow(leftCol, Priority.ALWAYS);
        leftCol.setMaxWidth(Double.MAX_VALUE);
        VBox rightCol = new VBox(20, buildAdvisoryCard(), buildAboutCard());
        rightCol.setPrefWidth(440);
        rightCol.setMinWidth(380);
        HBox panelsRow = new HBox(20, leftCol, rightCol);
        panelsRow.setAlignment(Pos.TOP_LEFT);

        body.getChildren().addAll(welcomeBox, statRow, carouselHolder, panelsRow);
        homeSig = null;
        applyHomeContent(AnnouncementStore.all(), CampaignStore.active(), EventStore.userRows());
        return wrapScroll(body);
    }

    /** Makes a card clickable: hand cursor, a small lift on hover, and an action on click. */
    private void makeClickable(Region card, Runnable action) {
        card.setStyle(card.getStyle() + "-fx-cursor: hand;");
        card.setOnMouseEntered(e -> card.setTranslateY(-3));
        card.setOnMouseExited(e -> card.setTranslateY(0));
        card.setOnMouseClicked(e -> action.run());
    }

    private VBox buildAboutCard() {
        Label title = sectionTitle("About Us", EV_INFO);
        VBox list = new VBox(0,
                noticeRow(EV_GLOBE, BLUE, BLUE_LIGHT, "Mission",
                        "To provide an organized, reliable, and efficient platform that simplifies volunteer coordination and donation management.", null),
                softSeparator(),
                noticeRow(EV_SEARCH, PURPLE, PURPLE_LIGHT, "Vision",
                        "To promote transparency, accountability, and active community participation through technology.", null)
        );
        return glassCard(new VBox(12, title, list));
    }

    // ---------- Home page carousel ----------

    // The home banner and the announcements card show what the admin posted. Both are filled from PostgreSQL
    // (announcements, open donations, open events) when Home is built, and again every few seconds.
    private final VBox carouselHolder = new VBox();
    private final VBox announcementsHolder = new VBox();
    private String homeSig = null;
    private Timeline homePoll;
    private final java.util.concurrent.atomic.AtomicBoolean homeRefreshing = new java.util.concurrent.atomic.AtomicBoolean(false);

    private static String shorten(String text, int max) {
        String t = text == null ? "" : text.replaceAll("\\s+", " ").trim();
        return t.length() <= max ? t : t.substring(0, max - 1).trim() + "\u2026";
    }

    /** Builds the banner slides {title, description, tag, image, action, target id} and redraws if anything changed. */
    private void applyHomeContent(List<AnnouncementStore.Announcement> anns, List<CampaignStore.Campaign> camps,
                                  List<String[]> events) {
        List<String[]> slides = new ArrayList<>();
        for (int i = 0; i < Math.min(4, events.size()); i++) {
            String[] e = events.get(i);
            String when = e[2];
            try {
                when = LocalDate.parse(e[2]).format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH));
            } catch (RuntimeException ignored) {
                // keep the raw text
            }
            String img = e[5] != null && !e[5].isBlank() && new java.io.File(e[5]).isFile() ? "file:" + e[5] : "";
            slides.add(new String[]{e[0], shorten(when + " \u2022 " + e[3], 95), e[1], img, "event", e[13]});
        }
        for (int i = 0; i < Math.min(4, camps.size()); i++) {
            CampaignStore.Campaign c = camps.get(i);
            String desc = c.description == null || c.description.isBlank()
                    ? (c.isInKind() && !c.hasGoal() ? "Drop-off: " + c.walkinLocation : c.progressText())
                    : c.description;
            slides.add(new String[]{c.name, shorten(desc, 95), c.category, campaignPhotoRef(c), "donate", String.valueOf(c.id)});
        }

        StringBuilder sig = new StringBuilder(java.util.Arrays.deepToString(slides.toArray()));
        for (AnnouncementStore.Announcement a : anns) {
            sig.append('|').append(a.id).append(a.title).append(a.body).append(a.tag).append(a.date).append(a.photoPath);
        }
        if (sig.toString().equals(homeSig)) return;
        homeSig = sig.toString();

        carouselHolder.getChildren().clear();
        if (!slides.isEmpty()) carouselHolder.getChildren().add(buildHomeCarousel(slides));
        announcementsHolder.getChildren().setAll(buildAnnouncementsCard(anns));
    }

    /** Reads PostgreSQL off the UI thread and redraws the banner / announcements only when something changed. */
    private void refreshHomeContent() {
        if (!homeRefreshing.compareAndSet(false, true)) return;
        Thread t = new Thread(() -> {
            final List<AnnouncementStore.Announcement> anns = AnnouncementStore.all();
            final List<CampaignStore.Campaign> camps = CampaignStore.active();
            final List<String[]> events = EventStore.userRowsOrNull();
            javafx.application.Platform.runLater(() -> {
                try {
                    if (events != null) applyHomeContent(anns, camps, events);
                } finally {
                    homeRefreshing.set(false);
                }
            });
        }, "user-home-refresh");
        t.setDaemon(true);
        t.start();
    }

    private void onHomeSlideAction(String action, String targetId) {
        if ("event".equals(action)) {
            if (!volunteerRegistered) {
                infoAlert("Please register as a volunteer first before joining events.");
                showPage("register");
            } else if (!checkVolunteerApproved()) {
                infoAlert(notApprovedMessage());
            } else {
                showPage("events");
            }
            return;
        }
        try {
            CampaignStore.Campaign c = CampaignStore.get(Integer.parseInt(targetId));
            if (c == null || !c.isActive()) {
                infoAlert("This donation is no longer open.");
            } else if (c.isInKind()) {
                openInKindDonation(c);
            } else {
                openCashDonation(c);
            }
        } catch (NumberFormatException ignored) {
            // not a donation slide
        }
    }

    private StackPane buildHomeCarousel(List<String[]> slidesData) {
        final int n = slidesData.size();
        final double GAP = 18, PAD_V = 22;

        Pane track = new Pane();
        track.setMinWidth(0);
        track.prefHeightProperty().bind(track.widthProperty().subtract(2 * GAP).divide(3).multiply(0.58).add(2 * PAD_V));
        track.minHeightProperty().bind(track.prefHeightProperty());
        Rectangle trackClip = new Rectangle();
        trackClip.widthProperty().bind(track.widthProperty());
        trackClip.heightProperty().bind(track.heightProperty());
        track.setClip(trackClip);

        List<StackPane> slides = new ArrayList<>();
        List<Region> dims = new ArrayList<>();
        List<Node> captions = new ArrayList<>();
        List<Button> regButtons = new ArrayList<>();
        for (String[] sd : slidesData) {
            StackPane inner = new StackPane();

            // photo, center-cropped to fill the card
            try (java.io.InputStream in = openImage(sd[3])) {
                if (in != null) {
                    Image img = new Image(in);
                    ImageView iv = new ImageView(img);
                    iv.setManaged(false);
                    iv.setSmooth(true);
                    iv.fitWidthProperty().bind(inner.widthProperty());
                    iv.fitHeightProperty().bind(inner.heightProperty());
                    Runnable crop = () -> {
                        double bw = inner.getWidth(), bh = inner.getHeight();
                        if (bw <= 0 || bh <= 0) return;
                        double boxRatio = bw / bh, imgRatio = img.getWidth() / img.getHeight();
                        double vw = imgRatio > boxRatio ? img.getHeight() * boxRatio : img.getWidth();
                        double vh = imgRatio > boxRatio ? img.getHeight() : img.getWidth() / boxRatio;
                        iv.setViewport(new Rectangle2D((img.getWidth() - vw) / 2, (img.getHeight() - vh) / 2, vw, vh));
                    };
                    inner.widthProperty().addListener((o, a, b) -> crop.run());
                    inner.heightProperty().addListener((o, a, b) -> crop.run());
                    inner.getChildren().add(iv);
                } else {
                    // no photo: a plain blue background so the text stays readable
                    inner.setStyle("-fx-background-color: linear-gradient(to bottom right, #3b82f6, #1e3a8a);");
                }
            } catch (Exception ex) {
                System.out.println("IMAGE ERROR: " + ex.getMessage());
            }

            // dark fade at the bottom so the text is readable
            Region fade = new Region();
            fade.setMouseTransparent(true);
            fade.setStyle("-fx-background-color: linear-gradient(to bottom, rgba(8,20,45,0.0) 38%, rgba(8,20,45,0.88) 100%);");

            Label t = new Label(sd[0]);
            t.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
            t.setTextFill(Color.WHITE);
            t.setWrapText(true);
            t.setMinHeight(Region.USE_PREF_SIZE);
            t.maxWidthProperty().bind(inner.widthProperty().subtract(44));
            Label d = new Label(sd[1]);
            d.setFont(Font.font("Segoe UI", 13));
            d.setTextFill(Color.web("#e6edf8"));
            d.setWrapText(true);
            d.setMinHeight(Region.USE_PREF_SIZE);
            d.maxWidthProperty().bind(inner.widthProperty().subtract(44));
            Label tag = new Label(sd[2]);
            tag.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 11));
            tag.setTextFill(Color.WHITE);
            tag.setPadding(new Insets(5, 14, 5, 14));
            tag.setStyle("-fx-background-color: rgba(255,255,255,0.16); -fx-background-radius: 16; " +
                    "-fx-border-color: rgba(255,255,255,0.55); -fx-border-radius: 16;");
            VBox caption = new VBox(6, t, d, tag);
            caption.setAlignment(Pos.BOTTOM_LEFT);
            caption.setPadding(new Insets(0, 20, 16, 22));
            caption.setMouseTransparent(true);
            StackPane.setAlignment(caption, Pos.BOTTOM_LEFT);
            caption.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

            Button reg = null;
            if (sd[4].equals("event") || sd[4].equals("donate")) {
                final String slideAction = sd[4], slideTarget = sd[5];
                reg = new Button(slideAction.equals("donate") ? "Donate Now  \u2192" : "Join as Volunteer  \u2192");
                reg.setStyle("-fx-background-color: white; -fx-text-fill: " + NAVY_DEEP + "; -fx-font-weight: bold; " +
                        "-fx-font-size: 13px; -fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 9 16 9 16;");
                reg.setOnAction(e -> onHomeSlideAction(slideAction, slideTarget));
                StackPane.setAlignment(reg, Pos.BOTTOM_RIGHT);
                StackPane.setMargin(reg, new Insets(0, 18, 16, 0));
            }

            // darkens the cards that are not in the middle
            Region dim = new Region();
            dim.setMouseTransparent(true);
            dim.setStyle("-fx-background-color: black;");
            dim.setOpacity(0);

            inner.getChildren().addAll(fade, caption);
            if (reg != null) inner.getChildren().add(reg);
            inner.getChildren().add(dim);

            Rectangle rc = new Rectangle();
            rc.setArcWidth(32);
            rc.setArcHeight(32);
            rc.widthProperty().bind(inner.widthProperty());
            rc.heightProperty().bind(inner.heightProperty());
            inner.setClip(rc);

            // the shadow sits on an outer box, because a clipped node would cut its own shadow off
            StackPane slide = new StackPane(inner);
            slide.setEffect(new DropShadow(24, 0, 8, Color.rgb(20, 40, 90, 0.32)));
            track.getChildren().add(slide);
            slides.add(slide);
            dims.add(dim);
            captions.add(caption);
            regButtons.add(reg);
        }

        // ----- dots -----
        HBox dots = new HBox(8);
        dots.setAlignment(Pos.CENTER);
        dots.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        List<Region> dotNodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Region dot = new Region();
            dot.setStyle("-fx-cursor: hand;");
            dotNodes.add(dot);
            dots.getChildren().add(dot);
        }

        // ----- position of the carousel: 0 = first slide in the middle, 1 = second, ... (can be in between) -----
        DoubleProperty pos = new SimpleDoubleProperty(0);
        Runnable layout = () -> {
            double w = track.getWidth();
            if (w <= 0) return;
            double cw = (w - 2 * GAP) / 3.0;
            double ch = cw * 0.58;
            double p = pos.get();
            for (int i = 0; i < n; i++) {
                double d = i - p;
                d = d - n * Math.floor((d + n / 2.0) / n);   // nearest copy of this slide, -n/2 .. n/2
                StackPane sl = slides.get(i);
                sl.setMinSize(cw, ch);
                sl.setPrefSize(cw, ch);
                sl.setMaxSize(cw, ch);
                sl.setLayoutX((w - cw) / 2 + d * (cw + GAP));
                sl.setLayoutY(PAD_V);
                double a = Math.min(1, Math.abs(d));
                sl.setVisible(Math.abs(d) < 2.3);
                dims.get(i).setOpacity(a * 0.5);
                captions.get(i).setOpacity(1 - 0.4 * a);
                Button rb = regButtons.get(i);
                if (rb != null) {
                    rb.setOpacity(Math.max(0, 1 - a * 1.6));
                    rb.setMouseTransparent(a > 0.5);
                }
            }
            int active = Math.floorMod((int) Math.round(p), n);
            for (int i = 0; i < n; i++) {
                Region dot = dotNodes.get(i);
                boolean on = i == active;
                double dw = on ? 22 : 8;
                dot.setMinSize(dw, 8);
                dot.setPrefSize(dw, 8);
                dot.setMaxSize(dw, 8);
                dot.setStyle("-fx-cursor: hand; -fx-background-radius: 4; -fx-background-color: " +
                        (on ? "rgba(255,255,255,0.95)" : "rgba(255,255,255,0.55)") + ";");
            }
        };
        track.widthProperty().addListener((o, a, b) -> layout.run());
        pos.addListener((o, a, b) -> layout.run());
        layout.run();

        // ----- moving -----
        Timeline[] anim = new Timeline[1];
        java.util.function.DoubleConsumer goTo = target -> {
            if (anim[0] != null) anim[0].stop();
            anim[0] = new Timeline(new KeyFrame(Duration.millis(520),
                    new KeyValue(pos, target, Interpolator.EASE_BOTH)));
            anim[0].play();
        };
        Timeline autoplay = new Timeline(new KeyFrame(Duration.seconds(5),
                e -> goTo.accept(Math.round(pos.get()) + 1)));
        autoplay.setCycleCount(Timeline.INDEFINITE);

        for (int i = 0; i < n; i++) {
            final int idx = i;
            dotNodes.get(i).setOnMouseClicked(e -> {
                long cur = Math.round(pos.get());
                long delta = Math.floorMod(idx - cur + n / 2, n) - n / 2;
                goTo.accept(cur + delta);
                autoplay.playFromStart();
                e.consume();
            });
        }

        // ----- arrows on both sides -----
        Button prev = new Button();
        Group prevIcon = icon(ICON_CHEVRON, NAVY_DEEP, 26);
        prevIcon.setRotate(180);
        prev.setGraphic(prevIcon);
        Button next = new Button();
        next.setGraphic(icon(ICON_CHEVRON, NAVY_DEEP, 26));
        for (Button ab : new Button[]{prev, next}) {
            ab.setMinSize(46, 46);
            ab.setPrefSize(46, 46);
            ab.setMaxSize(46, 46);
            ab.setStyle("-fx-background-color: rgba(255,255,255,0.95); -fx-background-radius: 23; -fx-cursor: hand;");
            ab.setEffect(new DropShadow(12, 0, 3, Color.rgb(0, 0, 0, 0.30)));
        }
        prev.setOnAction(e -> {
            goTo.accept(Math.round(pos.get()) - 1);
            autoplay.playFromStart();
        });
        next.setOnAction(e -> {
            goTo.accept(Math.round(pos.get()) + 1);
            autoplay.playFromStart();
        });

        StackPane stack = new StackPane(track, dots, prev, next);
        StackPane.setAlignment(dots, Pos.BOTTOM_CENTER);
        StackPane.setMargin(dots, new Insets(0, 0, PAD_V + 14, 0));
        StackPane.setAlignment(prev, Pos.CENTER_LEFT);
        StackPane.setMargin(prev, new Insets(0, 0, 0, 16));
        StackPane.setAlignment(next, Pos.CENTER_RIGHT);
        StackPane.setMargin(next, new Insets(0, 16, 0, 0));

        // ----- swipe with the mouse, or click a side card -----
        double[] press = new double[2];      // x of the press, position at that time
        boolean[] dragging = {false};
        stack.setOnMousePressed(e -> {
            if (anim[0] != null) anim[0].stop();
            press[0] = e.getSceneX();
            press[1] = pos.get();
            dragging[0] = false;
            autoplay.pause();
        });
        stack.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - press[0];
            if (Math.abs(dx) > 6) dragging[0] = true;
            if (dragging[0]) {
                double cw = (track.getWidth() - 2 * GAP) / 3.0;
                pos.set(press[1] - dx / (cw + GAP));
            }
        });
        stack.setOnMouseReleased(e -> {
            double cw = (track.getWidth() - 2 * GAP) / 3.0;
            long start = Math.round(press[1]);
            if (dragging[0]) {
                double moved = pos.get() - press[1];
                long target = moved > 0.12 ? start + 1 : moved < -0.12 ? start - 1 : start;
                goTo.accept(target);
            } else if (!isInsideButton(e.getTarget())) {
                long offset = Math.round((e.getX() - stack.getWidth() / 2) / (cw + GAP));
                if (offset != 0) goTo.accept(Math.round(pos.get()) + offset);
            }
            if (stack.getScene() != null && !stack.isHover()) autoplay.playFromStart();
        });

        // pause while the mouse is over it, and while another page is open
        stack.setOnMouseEntered(e -> autoplay.pause());
        stack.setOnMouseExited(e -> {
            if (stack.getScene() != null) autoplay.play();
        });
        stack.sceneProperty().addListener((o, oldS, newS) -> {
            if (newS == null) autoplay.pause();
            else autoplay.play();
        });
        return stack;
    }

    private boolean isInsideButton(Object target) {
        Node nd = target instanceof Node ? (Node) target : null;
        while (nd != null) {
            if (nd instanceof Button) return true;
            nd = nd.getParent();
        }
        return false;
    }

    private VBox statCard(String iconPath, String iconColor, String iconBg,
                          String title, String value, String sub, String subColor) {
        StackPane iconBox = new StackPane(strokeIcon(iconPath, iconColor, 26));
        iconBox.setMinSize(56, 56);
        iconBox.setMaxSize(56, 56);
        iconBox.setStyle("-fx-background-color: " + iconBg + "; -fx-background-radius: 16;");

        Label titleL = new Label(title);
        titleL.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));
        titleL.setTextFill(Color.web(TEXT_MUTED));

        Label valueL = new Label(value);
        valueL.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        valueL.setTextFill(Color.web(NAVY_DEEP));

        Label subL = new Label(sub);
        subL.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));
        subL.setTextFill(Color.web(subColor));

        VBox textBox = new VBox(1, titleL, valueL, subL);
        HBox content = new HBox(16, iconBox, textBox);
        content.setAlignment(Pos.CENTER_LEFT);

        VBox card = glassCard(content);
        card.setPadding(new Insets(20, 22, 20, 22));
        card.setUserData(valueL);
        return card;
    }

    private VBox buildAnnouncementsCard(List<AnnouncementStore.Announcement> anns) {
        Label title = sectionTitle("Community Announcements", ICON_BELL);
        VBox list = new VBox(0);
        if (anns.isEmpty()) {
            Label none = new Label("No announcements yet.");
            none.setFont(Font.font("Segoe UI", 13));
            none.setTextFill(Color.web(TEXT_MUTED));
            none.setPadding(new Insets(14, 0, 14, 0));
            list.getChildren().add(none);
        } else {
            for (int i = 0; i < Math.min(5, anns.size()); i++) {
                AnnouncementStore.Announcement a = anns.get(i);
                if (i > 0) list.getChildren().add(softSeparator());
                list.getChildren().add(noticeRow(ICON_BELL, BLUE, BLUE_LIGHT, a.title, a.body, a.tag + " \u2022 " + a.date));
            }
        }
        return glassCard(new VBox(12, title, list));
    }

    private VBox buildAdvisoryCard() {
        Label title = sectionTitle("Advisory", ICON_BELL);
        VBox list = new VBox(0,
                noticeRow(ICON_BELL, ORANGE, ORANGE_LIGHT, "Weather advisory",
                        "Outdoor events may be moved or cancelled when it rains. Check your notifications before heading out.", null),
                softSeparator(),
                noticeRow(ICON_CHECK, BLUE, BLUE_LIGHT, "Bring a valid ID",
                        "A valid ID is required for volunteer check-in at every event.", null),
                softSeparator(),
                noticeRow(ICON_MONEY, GREEN, GREEN_LIGHT, "Donation reminder",
                        "Your donation is marked Received once the admin confirms it.", null),
                softSeparator(),
                noticeRow(ICON_MEDICAL, "#dc2626", "#fee2e2", "Stay safe",
                        "Wear comfortable clothes, bring water, and follow the event coordinator's instructions.", null)
        );
        return glassCard(new VBox(12, title, list));
    }

    /** Icon + bold title + short description (+ optional time), used by Announcements and Advisory. */
    private HBox noticeRow(String iconPath, String iconColor, String iconBg, String title, String body, String time) {
        StackPane iconBox = new StackPane(icon(iconPath, iconColor, 18));
        iconBox.setMinSize(38, 38);
        iconBox.setMaxSize(38, 38);
        iconBox.setStyle("-fx-background-color: " + iconBg + "; -fx-background-radius: 12;");

        Label titleL = new Label(title);
        titleL.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        titleL.setTextFill(Color.web(NAVY_DEEP));
        titleL.setWrapText(true);
        Label bodyL = new Label(body);
        bodyL.setFont(Font.font("Segoe UI", 12));
        bodyL.setTextFill(Color.web(TEXT_MUTED));
        bodyL.setWrapText(true);
        VBox textBox = new VBox(2, titleL, bodyL);
        if (time != null) {
            Label timeL = new Label(time);
            timeL.setFont(Font.font("Segoe UI", 11));
            timeL.setTextFill(Color.web(BLUE));
            textBox.getChildren().add(timeL);
        }
        HBox.setHgrow(textBox, Priority.ALWAYS);

        HBox row = new HBox(14, iconBox, textBox);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 0, 12, 0));
        return row;
    }

    private Runnable refreshRecent;
    private VBox recentList;

    /** Recent Activity on the Home page: shows the user's latest items from My Activities and links to that page. */
    private VBox buildActivityCard() {
        Label title = sectionTitle("Recent Activity", ICON_CLOCK);
        Button viewAll = new Button("View all");
        viewAll.setStyle("-fx-background-color: transparent; -fx-text-fill: " + BLUE + "; -fx-font-weight: bold; " +
                "-fx-font-size: 13px; -fx-cursor: hand;");
        viewAll.setOnAction(e -> showPage("activities"));
        HBox head = new HBox(title, spacer(), viewAll);
        head.setAlignment(Pos.CENTER_LEFT);

        recentList = new VBox(0);
        refreshRecent = this::fillRecentActivity;
        fillRecentActivity();
        return glassCard(new VBox(12, head, recentList));
    }

    private void fillRecentActivity() {
        if (recentList == null) return;
        recentList.getChildren().clear();
        if (activityRows.isEmpty()) {
            Label none = new Label("No activity yet. Your donations, volunteer registration and joined events will show up here.");
            none.setWrapText(true);
            none.setMinHeight(Region.USE_PREF_SIZE);
            none.setFont(Font.font("Segoe UI", 13));
            none.setTextFill(Color.web(TEXT_MUTED));
            none.setPadding(new Insets(12, 0, 12, 0));
            recentList.getChildren().add(none);
            return;
        }
        int shown = Math.min(4, activityRows.size());
        for (int i = 0; i < shown; i++) {
            String[] r = activityRows.get(i);   // {type, text, date, status, txn, amount}
            String ic = ICON_HEART, col = "#db2777", bg = "#fce7f3";
            if ("Event".equals(r[0])) {
                ic = ICON_CALENDAR; col = BLUE; bg = BLUE_LIGHT;
            } else if ("Volunteer".equals(r[0])) {
                ic = ICON_PEOPLE; col = PURPLE; bg = PURPLE_LIGHT;
            }
            HBox row = activityRow(ic, col, bg, r[1], r[2] + "  \u00B7  " + r[3]);
            row.setStyle("-fx-cursor: hand;");
            row.setOnMouseClicked(e -> showPage("activities"));
            if (i > 0) recentList.getChildren().add(softSeparator());
            recentList.getChildren().add(row);
        }
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

    // ---------- Donations page ----------

    private ScrollPane buildDonationsPage() {
        VBox body = new VBox(26);
        body.setPadding(new Insets(32, 48, 32, 48));

        Label title = new Label("Donation Opportunities");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        title.setTextFill(Color.web(NAVY_DEEP));

        // ----- search bar + Search button -----
        TextField search = new TextField();
        search.setPromptText("Search for donations...");
        styleInput(search);
        search.setPrefHeight(54);
        search.setStyle(search.getStyle() + "-fx-font-size: 16px;");
        HBox.setHgrow(search, Priority.ALWAYS);
        search.setMaxWidth(Double.MAX_VALUE);
        Button searchBtn = primaryButton("Search", 16, 15, 34);
        HBox searchRow = new HBox(16, search, searchBtn);
        searchRow.setAlignment(Pos.CENTER_LEFT);

        // ----- the donation opportunity cards -----
        // Cash and In-Kind: the donations the admin set up (PostgreSQL "campaigns"). No hardcoded cards.
        FlowPane cards = new FlowPane(28, 28);
        cards.setAlignment(Pos.TOP_LEFT);
        List<Node> cardNodes = new ArrayList<>();
        List<CampaignStore.Campaign> openDonations = CampaignStore.active();
        for (CampaignStore.Campaign cd : openDonations) {
            VBox cc;
            if (cd.isInKind()) {
                cc = donationOpportunityCard(cd.name, cd.hasGoal() ? cd.progressText() : "", cd.percent(),
                        campaignPhotoRef(cd), GREEN, () -> openInKindDonation(cd));
            } else {
                String progress = "Raised " + cd.raisedText() + " of " + cd.goalText();
                cc = donationOpportunityCard(cd.name, progress, cd.percent(), campaignPhotoRef(cd), BLUE,
                        () -> openCashDonation(cd));
            }
            cc.setUserData(new String[]{cd.name.toLowerCase(Locale.ENGLISH), cd.category});
            cardNodes.add(cc);
            cards.getChildren().add(cc);
        }
        Label noCashNote = new Label("No donations are open yet. Please check back soon.");
        noCashNote.setFont(Font.font("Segoe UI", 14));
        noCashNote.setTextFill(Color.web(TEXT_MUTED));

        // ----- filter chips: All, Cash, Education, Relief Goods, Clothes, General Goods, Others -----
        String[] cats = {"All", "Cash", "Education", "Relief Goods", "Clothes", "General Goods", "Others"};
        String[] active = {"All"};
        Runnable[] applyFilter = new Runnable[1];
        HBox chipRow = new HBox(14);
        chipRow.setAlignment(Pos.CENTER_LEFT);
        List<Button> chips = new ArrayList<>();
        for (String cat : cats) {
            Button chip = new Button(cat);
            chip.setUserData(cat);
            chip.setOnAction(e -> {
                active[0] = cat;
                applyFilter[0].run();
            });
            chips.add(chip);
            chipRow.getChildren().add(chip);
        }
        applyFilter[0] = () -> {
            String q = search.getText() == null ? "" : search.getText().trim().toLowerCase(Locale.ENGLISH);
            for (Button chip : chips) {
                boolean on = chip.getUserData().equals(active[0]);
                chip.setStyle("-fx-background-radius: 26; -fx-cursor: hand; -fx-font-size: 16px; -fx-padding: 12 34 12 34; " +
                        (on ? "-fx-background-color: " + BLUE + "; -fx-text-fill: white; -fx-font-weight: bold;"
                                : "-fx-background-color: " + BLUE_LIGHT + "; -fx-text-fill: " + NAVY + ";"));
            }
            for (Node c : cardNodes) {
                String[] meta = (String[]) c.getUserData();
                boolean okCat = "All".equals(active[0]) || meta[1].equals(active[0]);
                boolean okText = q.isEmpty() || meta[0].contains(q) || meta[1].toLowerCase(Locale.ENGLISH).contains(q);
                c.setVisible(okCat && okText);
                c.setManaged(okCat && okText);
            }
        };
        searchBtn.setOnAction(e -> applyFilter[0].run());
        search.setOnAction(e -> applyFilter[0].run());
        search.textProperty().addListener((obs, o, n) -> applyFilter[0].run());
        applyFilter[0].run();

        body.getChildren().addAll(title, searchRow, chipRow, cards);
        if (openDonations.isEmpty()) body.getChildren().add(noCashNote);
        return wrapScroll(body);
    }

    /** One card: photo on top, name and a Donate Now button. */
    private VBox donationOpportunityCard(String name, String goal, int percent, String imageFile,
                                         String barColor, Runnable onDonate) {
        final double IMG_H = 270;

        // photo box: grows with the card, photo is center-cropped (never stretched)
        StackPane imgBox = new StackPane();
        imgBox.setStyle("-fx-background-color: #e8eefb;"); // shows when the admin added no photo
        imgBox.setMinSize(0, IMG_H);
        imgBox.setPrefSize(10, IMG_H);
        imgBox.setMaxSize(Double.MAX_VALUE, IMG_H);
        try (java.io.InputStream in = openImage(imageFile)) {
            if (in != null) {
                Image img = new Image(in);
                ImageView iv = new ImageView(img);
                iv.setManaged(false);
                iv.setSmooth(true);
                iv.setFitHeight(IMG_H);
                iv.fitWidthProperty().bind(imgBox.widthProperty());
                imgBox.widthProperty().addListener((obs, o, w) -> {
                    double bw = w.doubleValue();
                    if (bw <= 0) return;
                    double boxRatio = bw / IMG_H, imgRatio = img.getWidth() / img.getHeight();
                    double vw = imgRatio > boxRatio ? img.getHeight() * boxRatio : img.getWidth();
                    double vh = imgRatio > boxRatio ? img.getHeight() : img.getWidth() / boxRatio;
                    iv.setViewport(new Rectangle2D((img.getWidth() - vw) / 2, (img.getHeight() - vh) / 2, vw, vh));
                });
                imgBox.getChildren().add(iv);
            } else if (imageFile != null && !imageFile.isBlank()) {
                System.out.println("IMAGE ERROR: /images/" + imageFile + " not found");
            }
        } catch (Exception ex) {
            System.out.println("IMAGE ERROR: " + ex.getMessage());
        }
        Rectangle clip = new Rectangle();
        clip.setArcWidth(28);
        clip.setArcHeight(28);
        clip.widthProperty().bind(imgBox.widthProperty());
        clip.setHeight(IMG_H + 30); // extra height hides the bottom rounding, only the top corners stay round
        imgBox.setClip(clip);

        Label nameL = new Label(name);
        nameL.setFont(Font.font("Segoe UI", FontWeight.BOLD, 21));
        nameL.setTextFill(Color.web(NAVY_DEEP));

        Button donateNow = primaryButton("Donate Now", 17, 15, 22);
        donateNow.setMaxWidth(Double.MAX_VALUE);
        donateNow.setOnAction(e -> onDonate.run());

        VBox info = new VBox(14, nameL);
        if (goal != null && !goal.isBlank()) {
            Label goalL = new Label(goal + "  (" + percent + "%)");
            goalL.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
            goalL.setTextFill(Color.web(TEXT_MUTED));
            ProgressBar bar = new ProgressBar(percent / 100.0);
            bar.setMaxWidth(Double.MAX_VALUE);
            bar.setStyle("-fx-accent: " + barColor + ";");
            info.getChildren().addAll(goalL, bar);
        }
        info.getChildren().add(donateNow);
        info.setPadding(new Insets(18, 22, 22, 22));

        VBox card = new VBox(imgBox, info);
        card.setMinWidth(0);
        card.setPrefWidth(400);
        card.setMaxWidth(640);
        card.setStyle(GLASS);
        card.setEffect(new DropShadow(24, Color.rgb(20, 40, 90, 0.14)));
        return card;
    }

    /** Opens the Cash Donation page for a donation the admin set up (re-read from PostgreSQL, so it is current). */
    private void openCashDonation(CampaignStore.Campaign selected) {
        CampaignStore.Campaign fresh = CampaignStore.get(selected.id);
        if (fresh == null || !fresh.isActive()) {
            infoAlert("This donation is no longer open.");
            showPage("donations");
            return;
        }
        cashCampaign = fresh;
        pages.put("gcash", buildGcashPage());
        showPage("gcash");
    }

    /** Photo reference for a donation: "file:" + the admin's photo, or "" when the admin added no photo. */
    private String campaignPhotoRef(CampaignStore.Campaign cd) {
        return (cd.photoPath != null && !cd.photoPath.isBlank() && new java.io.File(cd.photoPath).isFile())
                ? "file:" + cd.photoPath : "";
    }

    /** A photo from /images, or "file:" + a full path to a photo the admin picked (null when there is none). */
    private java.io.InputStream openImage(String ref) throws java.io.IOException {
        if (ref == null || ref.isBlank()) return null;
        if (ref.startsWith("file:")) {
            java.io.File f = new java.io.File(ref.substring(5));
            return f.isFile() ? new java.io.FileInputStream(f) : null;
        }
        return getClass().getResourceAsStream("/images/" + ref);
    }

    // ---------- Small form helpers for the donation pages ----------

    private Label hintLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", 11));
        l.setTextFill(Color.web(TEXT_MUTED));
        return l;
    }

    /** Small rounded "suggestion" button; clicking it fills a field. */
    private Button chipButton(String text) {
        Button b = new Button(text);
        String base = "-fx-background-color: rgba(255,255,255,0.85); -fx-text-fill: " + NAVY + "; -fx-font-size: 12px; " +
                "-fx-background-radius: 14; -fx-border-color: #d1d9ea; -fx-border-radius: 14; -fx-cursor: hand; " +
                "-fx-padding: 5 12 5 12;";
        String hover = base.replace("rgba(255,255,255,0.85)", BLUE_LIGHT);
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(base));
        return b;
    }

    /** Pill that can be selected (one at a time inside its group). */
    private ToggleButton chipToggle(String text, ToggleGroup group) {
        ToggleButton t = new ToggleButton(text);
        t.setToggleGroup(group);
        Runnable restyle = () -> t.setStyle("-fx-font-size: 12px; -fx-background-radius: 14; -fx-cursor: hand; " +
                "-fx-padding: 5 14 5 14; -fx-border-radius: 14; " +
                (t.isSelected()
                        ? "-fx-background-color: " + BLUE + "; -fx-text-fill: white; -fx-border-color: " + BLUE + ";"
                        : "-fx-background-color: rgba(255,255,255,0.85); -fx-text-fill: " + NAVY + "; -fx-border-color: #d1d9ea;"));
        t.selectedProperty().addListener((obs, o, n) -> restyle.run());
        restyle.run();
        return t;
    }

    private void keepOneSelected(ToggleGroup group) {
        group.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            if (newT == null && oldT != null) oldT.setSelected(true);
        });
    }

    private int parseWhole(String s) {
        try {
            return Integer.parseInt(s == null ? "" : s.trim());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    /** Quantity field with a  -  [number]  +  stepper inside its right side. */
    private StackPane quantityField(TextField f) {
        styleInput(f);
        f.setStyle(f.getStyle().replace("-fx-padding: 0 12 0 12;", "-fx-padding: 0 120 0 12;"));

        Label count = new Label("0");
        count.setMinWidth(34);
        count.setAlignment(Pos.CENTER);
        count.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        count.setTextFill(Color.web(NAVY_DEEP));

        Button minus = stepButton("\u2212");
        Button plus = stepButton("+");
        f.textProperty().addListener((obs, o, n) -> count.setText(String.valueOf(parseWhole(n))));
        minus.setOnAction(e -> f.setText(String.valueOf(Math.max(1, parseWhole(f.getText()) - 1))));
        plus.setOnAction(e -> f.setText(String.valueOf(parseWhole(f.getText()) + 1)));

        HBox stepper = new HBox(2, minus, count, plus);
        stepper.setAlignment(Pos.CENTER);
        stepper.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        StackPane box = new StackPane(f, stepper);
        StackPane.setAlignment(stepper, Pos.CENTER_RIGHT);
        StackPane.setMargin(stepper, new Insets(0, 8, 0, 0));
        return box;
    }

    private Button stepButton(String text) {
        Button b = new Button(text);
        b.setMinSize(26, 26);
        b.setPrefSize(26, 26);
        b.setFocusTraversable(false);
        b.setStyle("-fx-background-color: #eef2ff; -fx-text-fill: " + NAVY + "; -fx-font-size: 14px; " +
                "-fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0;");
        return b;
    }

    /** Read-only location field with a map pin on the left. */
    private StackPane locationField(String text) {
        TextField loc = new TextField(text);
        loc.setEditable(false);
        loc.setFocusTraversable(false);
        styleInput(loc);
        loc.setStyle(loc.getStyle().replace("-fx-padding: 0 12 0 12;", "-fx-padding: 0 12 0 36;")
                + "-fx-background-color: #e5e9f2; -fx-text-fill: " + TEXT_MUTED + ";");
        Group pin = icon(EV_PIN, TEXT_MUTED, 16);
        pin.setMouseTransparent(true);
        StackPane box = new StackPane(loc, pin);
        StackPane.setAlignment(pin, Pos.CENTER_LEFT);
        StackPane.setMargin(pin, new Insets(0, 0, 0, 12));
        return box;
    }

    private TextField donorField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        styleInput(f);
        return f;
    }

    /** Photo that fills a box of the given height (center-cropped, never stretched), with rounded corners. */
    private StackPane croppedPhoto(String imageFile, double h, double arc) {
        StackPane box = new StackPane();
        box.setStyle("-fx-background-color: #e8eefb;"); // shows when the admin added no photo
        box.setMinSize(0, h);
        box.setPrefSize(10, h);
        box.setMaxSize(Double.MAX_VALUE, h);
        try (java.io.InputStream in = openImage(imageFile)) {
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
                    iv.setViewport(new Rectangle2D((img.getWidth() - vw) / 2, (img.getHeight() - vh) / 2, vw, vh));
                });
                box.getChildren().add(iv);
            } else if (imageFile != null && !imageFile.isBlank()) {
                System.out.println("IMAGE ERROR: /images/" + imageFile + " not found");
            }
        } catch (Exception ex) {
            System.out.println("IMAGE ERROR: " + ex.getMessage());
        }
        Rectangle clip = new Rectangle();
        clip.setArcWidth(arc);
        clip.setArcHeight(arc);
        clip.widthProperty().bind(box.widthProperty());
        clip.setHeight(h);
        box.setClip(clip);
        return box;
    }

    /** Title card used on top of the donation forms: title + subtitle on the left, Back button on the right. */
    private VBox donationHeader(Label titleLbl, String subText, Node leadingIcon) {
        Button backBtn = secondaryButton("\u2190 Back");
        backBtn.setMinWidth(Region.USE_PREF_SIZE);
        backBtn.setOnAction(e -> showPage("donations"));
        titleLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        titleLbl.setTextFill(Color.web(NAVY_DEEP));
        Label sub = new Label(subText);
        sub.setWrapText(true);
        sub.setMinHeight(Region.USE_PREF_SIZE);
        sub.setFont(Font.font("Segoe UI", 13));
        sub.setTextFill(Color.web(TEXT_MUTED));
        VBox texts = new VBox(3, titleLbl, sub);
        texts.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(texts, Priority.ALWAYS);
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        if (leadingIcon != null) row.getChildren().add(leadingIcon);
        row.getChildren().addAll(texts, backBtn);
        return glassCard(row);
    }

    private String todayText() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH));
    }

    // ---------- Donate form page (items: Education for All) ----------

    private Button cancelButton() {
        Button b = new Button("Cancel");
        String base = "-fx-background-color: rgba(255,255,255,0.85); -fx-text-fill: " + NAVY_DEEP + "; -fx-font-weight: bold; " +
                "-fx-font-size: 13px; -fx-background-radius: 10; -fx-border-color: #d1d9ea; -fx-border-radius: 10; " +
                "-fx-cursor: hand; -fx-padding: 10 20 10 20;";
        b.setStyle(base);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setOnMouseEntered(e -> b.setStyle(base.replace("0.85", "1.0")));
        b.setOnMouseExited(e -> b.setStyle(base));
        b.setOnAction(e -> showPage("donations"));
        return b;
    }

    /** Heading + subtitle at the top of the right-hand "Donation Details" column. */
    private VBox detailsHeading() {
        Label h = new Label("Donation Details");
        h.setFont(Font.font("Segoe UI", FontWeight.BOLD, 19));
        h.setTextFill(Color.web(NAVY_DEEP));
        Label sub = new Label("Tell us how many and what you will donate.");
        sub.setFont(Font.font("Segoe UI", 13));
        sub.setTextFill(Color.web(TEXT_MUTED));
        return new VBox(3, h, sub);
    }

    /** Two columns (left / right) with a thin divider, inside one centered glass card. */
    private VBox twoColumnCard(VBox left, VBox right) {
        left.setMinWidth(0);
        left.setPrefWidth(440);
        right.setMinWidth(0);
        right.setPrefWidth(440);
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);
        Separator divider = new Separator(javafx.geometry.Orientation.VERTICAL);
        divider.setOpacity(0.5);
        HBox columns = new HBox(28, left, divider, right);
        columns.setAlignment(Pos.TOP_LEFT);
        VBox card = glassCard(columns);
        card.setMaxWidth(1000);
        card.setPadding(new Insets(28));
        return card;
    }

    private Label wrapText(String text, int size, String color) {
        Label l = new Label(text);
        l.setWrapText(true);
        l.setMinHeight(Region.USE_PREF_SIZE);
        l.setFont(Font.font("Segoe UI", size));
        l.setTextFill(Color.web(color));
        return l;
    }

    /** Adds a little extra space above a form label inside a VBox. */
    private Label spaceBefore(Label l) {
        VBox.setMargin(l, new Insets(8, 0, 0, 0));
        return l;
    }

    // ---------- In-Kind donation page (items) ----------

    /** Opens the In-Kind Donation page for a donation the admin set up (re-read from PostgreSQL, so it is current). */
    private void openInKindDonation(CampaignStore.Campaign selected) {
        CampaignStore.Campaign fresh = CampaignStore.get(selected.id);
        if (fresh == null || !fresh.isActive() || !fresh.isInKind()) {
            infoAlert("This donation is no longer open.");
            showPage("donations");
            return;
        }
        inKindCampaign = fresh;
        pages.put("donate", buildInKindPage());
        showPage("donate");
    }

    /** "Select Target Fund / Campaign" drop-down; picking another donation reopens the page for it. */
    private ComboBox<CampaignStore.Campaign> fundPicker(List<CampaignStore.Campaign> open,
                                                        CampaignStore.Campaign current,
                                                        java.util.function.Consumer<CampaignStore.Campaign> onPick) {
        ComboBox<CampaignStore.Campaign> fund = new ComboBox<>(FXCollections.observableArrayList(open));
        fund.setConverter(new javafx.util.StringConverter<CampaignStore.Campaign>() {
            @Override
            public String toString(CampaignStore.Campaign c) {
                return c == null ? "" : c.name;
            }

            @Override
            public CampaignStore.Campaign fromString(String s) {
                return null;
            }
        });
        for (CampaignStore.Campaign c : open) {
            if (c.id == current.id) fund.setValue(c);
        }
        fund.setMaxWidth(Double.MAX_VALUE);
        fund.setPrefHeight(44);
        fund.setStyle("-fx-background-color: #f9fafb; -fx-background-radius: 10; -fx-border-color: #d1d5db; " +
                "-fx-border-radius: 10; -fx-font-size: 13px; -fx-cursor: hand;");
        fund.valueProperty().addListener((obs, o, n) -> {
            if (n != null && n.id != current.id) javafx.application.Platform.runLater(() -> onPick.accept(n));
        });
        return fund;
    }

    /** "Received 12 of 100 items (12%)" with a progress bar; hidden when the admin set no target. */
    private VBox inKindGoalBox(CampaignStore.Campaign camp) {
        VBox box = new VBox(6);
        if (!camp.hasGoal()) {
            box.setVisible(false);
            box.setManaged(false);
            return box;
        }
        Label goalText = new Label(camp.progressText() + "  (" + camp.percent() + "%)");
        goalText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        goalText.setTextFill(Color.web(NAVY_DEEP));
        ProgressBar goalBar = new ProgressBar(camp.percent() / 100.0);
        goalBar.setMaxWidth(Double.MAX_VALUE);
        goalBar.setStyle("-fx-accent: " + GREEN + ";");
        box.getChildren().addAll(goalText, goalBar);
        return box;
    }

    private ScrollPane buildInKindPage() {
        final CampaignStore.Campaign camp = inKindCampaign;
        VBox body = new VBox(20);
        body.setPadding(new Insets(28, 40, 28, 40));
        body.setAlignment(Pos.TOP_CENTER);

        VBox titleCard = donationHeader(new Label("In-Kind Donation"),
                "Tell us how many and what you will donate to the campaign you choose.", null);
        titleCard.setMaxWidth(1000);

        // ================= left: campaign photo, fund, location, donor =================
        StackPane photo = croppedPhoto(campaignPhotoRef(camp), 230, 24);
        Label campaign = wrapText(camp.name, 22, NAVY_DEEP);
        campaign.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        Label desc = wrapText(camp.description == null || camp.description.isBlank()
                ? "Thank you for supporting " + camp.name + "." : camp.description, 13, TEXT_MUTED);

        ComboBox<CampaignStore.Campaign> fund = fundPicker(
                CampaignStore.activeOfType(CampaignStore.TYPE_INKIND), camp, this::openInKindDonation);

        StackPane locBox = locationField(camp.walkinLocation);
        TextField donor = donorField("Enter name or contact number");

        VBox left = new VBox(10, photo, campaign, desc, inKindGoalBox(camp),
                spaceBefore(formLabel("Select Target Fund / Campaign")), fund,
                spaceBefore(formLabel("Location / Drop-off")), locBox,
                hintLabel(camp.walkinSchedule()),
                spaceBefore(formLabel("Donor Name / Contact (Optional)")), donor,
                hintLabel("For donation tracking"));

        // ================= right: donation details =================
        TextField qty = new TextField();
        qty.setPromptText("e.g., 10 items");
        StackPane qtyBox = quantityField(qty);

        TextField item = new TextField();
        item.setPromptText("e.g., what you will bring");
        styleInput(item);
        FlowPane itemChips = new FlowPane(8, 8);
        for (String s : camp.itemList()) {
            Button chip = chipButton(s);
            chip.setOnAction(e -> item.setText(s));
            itemChips.getChildren().add(chip);
        }

        ToggleGroup condGroup = new ToggleGroup();
        FlowPane condRow = new FlowPane(18, 8);
        RadioButton firstCond = null;
        for (String s : new String[]{"Brand New / Unopened", "Gently Used / Good", "Emergency Supplies"}) {
            RadioButton rb = new RadioButton(s);
            rb.setToggleGroup(condGroup);
            styleChoice(rb);
            if (firstCond == null) firstCond = rb;
            condRow.getChildren().add(rb);
        }
        firstCond.setSelected(true);

        Label errorLabel = new Label(" ");
        errorLabel.setFont(Font.font("Segoe UI", 13));
        errorLabel.setWrapText(true);
        errorLabel.setTextFill(Color.web("#9b2c2c"));

        Button submitBtn = primaryButton("Submit Donation", 15, 13, 22);
        submitBtn.setGraphic(strokeIcon(S_GIFT, "#ffffff", 17));
        submitBtn.setGraphicTextGap(8);
        submitBtn.setMaxWidth(Double.MAX_VALUE);
        submitBtn.setOnAction(e -> {
            int count = parseWhole(qty.getText());
            if (count <= 0) {
                errorLabel.setText("Please enter how many (a whole number greater than 0).");
                return;
            }
            String what = item.getText() == null ? "" : item.getText().trim();
            if (what.isEmpty()) {
                errorLabel.setText("Please enter what you will donate.");
                return;
            }

            String shown = count + " " + what;
            String date = todayText();
            String cond = ((RadioButton) condGroup.getSelectedToggle()).getText();
            String who = donor.getText() == null ? "" : donor.getText().trim();
            String noteText = camp.name + " - " + camp.walkinLocation
                    + " | Condition: " + cond
                    + (who.isEmpty() ? "" : " | Contact: " + who);

            // saved in the shared store, so the Admin dashboard sees it too
            DonationStore.Donation saved = DonationStore.add(username, "In-Kind", shown, 0, date, noteText, camp.id);
            addActivity("Donation", "In-kind donation to " + camp.name + ": " + shown, date, saved.status, saved.id, 0);
            addNotification(ICON_HEART, GREEN, GREEN_LIGHT, "Thank you! Your donation " + saved.id + " was submitted.");
            showPage("donations");
        });
        item.setOnAction(e -> submitBtn.fire());

        VBox right = new VBox(8, detailsHeading(),
                spaceBefore(formLabel("How many (Quantity)")), qtyBox, hintLabel("Enter estimated number of items"),
                spaceBefore(formLabel("What (Item)")), item);
        if (!itemChips.getChildren().isEmpty()) {
            right.getChildren().addAll(hintLabel("Items needed (click to fill in):"), itemChips);
        }
        right.getChildren().addAll(spaceBefore(formLabel("Item Condition / Details")), condRow,
                errorLabel, submitBtn);

        body.getChildren().addAll(titleCard, twoColumnCard(left, right));
        return wrapScroll(body);
    }

    // ---------- Cash donation page (GCash or Walk-in) ----------

    private ScrollPane buildGcashPage() {
        final CampaignStore.Campaign camp = cashCampaign;
        VBox body = new VBox(20);
        body.setPadding(new Insets(28, 40, 28, 40));
        body.setAlignment(Pos.TOP_CENTER);

        VBox titleCard = donationHeader(new Label("Cash Donation"),
                "Help support our community initiatives and local programs.", null);
        titleCard.setMaxWidth(1000);

        // ================= left column: campaign + amount =================
        StackPane photo = croppedPhoto(campaignPhotoRef(camp), 230, 24);

        Label campaign = new Label(camp.name);
        campaign.setWrapText(true);
        campaign.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        campaign.setTextFill(Color.web(NAVY_DEEP));
        campaign.setMaxWidth(Double.MAX_VALUE);
        campaign.setMinHeight(Region.USE_PREF_SIZE);
        HBox.setHgrow(campaign, Priority.ALWAYS);
        Label active = new Label("Active Campaign");
        active.setStyle("-fx-background-color: " + GREEN_LIGHT + "; -fx-text-fill: " + GREEN + "; -fx-font-size: 11px; " +
                "-fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 3 10 3 10;");
        HBox campaignRow = new HBox(10, campaign, active);
        campaignRow.setAlignment(Pos.TOP_LEFT);

        Label desc = new Label(camp.description == null || camp.description.isBlank()
                ? "Thank you for supporting " + camp.name + "." : camp.description);
        desc.setWrapText(true);
        desc.setMinHeight(Region.USE_PREF_SIZE);
        desc.setFont(Font.font("Segoe UI", 13));
        desc.setTextFill(Color.web(TEXT_MUTED));

        TextField amount = new TextField();
        amount.setPromptText("Enter donation amount...");
        amount.setPrefHeight(44);
        amount.setStyle("-fx-background-color: #f9fafb; -fx-background-radius: 0 10 10 0; -fx-border-color: #d1d5db; " +
                "-fx-border-radius: 0 10 10 0; -fx-font-size: 14px; -fx-padding: 0 12 0 12;");
        HBox.setHgrow(amount, Priority.ALWAYS);
        Label peso = new Label("\u20B1");
        peso.setAlignment(Pos.CENTER);
        peso.setMinWidth(46);
        peso.setPrefHeight(44);
        peso.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        peso.setTextFill(Color.web(NAVY));
        peso.setStyle("-fx-background-color: #eef2ff; -fx-background-radius: 10 0 0 10; -fx-border-color: #d1d5db; " +
                "-fx-border-radius: 10 0 0 10; -fx-border-width: 1 0 1 1;");
        HBox amountRow = new HBox(peso, amount);

        Label goalText = new Label("Raised " + camp.raisedText() + " of " + camp.goalText() + " goal  (" + camp.percent() + "%)");
        goalText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        goalText.setTextFill(Color.web(NAVY_DEEP));
        ProgressBar goalBar = new ProgressBar(camp.percent() / 100.0);
        goalBar.setMaxWidth(Double.MAX_VALUE);
        goalBar.setStyle("-fx-accent: " + BLUE + ";");
        VBox goalBox = new VBox(6, goalText, goalBar);

        ComboBox<CampaignStore.Campaign> fund = fundPicker(
                CampaignStore.activeOfType(CampaignStore.TYPE_CASH), camp, this::openCashDonation);

        VBox left = new VBox(12, photo, campaignRow, desc,
                spaceBefore(formLabel("Select Target Fund / Campaign")), fund,
                goalBox, spaceBefore(formLabel("Amount (\u20B1)")), amountRow);
        left.setMinWidth(0);
        left.setPrefWidth(440);
        HBox.setHgrow(left, Priority.ALWAYS);

        // ================= right column: payment method =================
        Label pmTitle = new Label("Payment Method");
        pmTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 19));
        pmTitle.setTextFill(Color.web(NAVY_DEEP));
        Label pmSub = new Label("Choose how you would like to give your donation.");
        pmSub.setFont(Font.font("Segoe UI", 13));
        pmSub.setTextFill(Color.web(TEXT_MUTED));

        ToggleGroup group = new ToggleGroup();
        ToggleButton gcashBtn = new ToggleButton();
        ToggleButton walkBtn = new ToggleButton();
        gcashBtn.setToggleGroup(group);
        walkBtn.setToggleGroup(group);
        gcashBtn.setSelected(true);
        keepOneSelected(group);

        Label gMark = new Label("G");
        gMark.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 17px;");
        StackPane gcashIcon = new StackPane(new Circle(17, Color.web("#0a6cf0")), gMark);
        gcashIcon.setMaxSize(34, 34);
        StackPane gcashOption = paymentOption(gcashBtn, gcashIcon, "Through GCash");
        StackPane walkOption = paymentOption(walkBtn, icon(ICON_MONEY, BLUE, 32), "Walk-in");
        HBox choiceRow = new HBox(12, gcashOption, walkOption);
        HBox.setHgrow(gcashOption, Priority.ALWAYS);
        HBox.setHgrow(walkOption, Priority.ALWAYS);

        // ----- GCash details panel -----
        Label gcTitle = new Label("GCash Payment Details");
        gcTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        gcTitle.setTextFill(Color.web(NAVY_DEEP));
        Label gcHelp = new Label("Send your donation to the GCash account above, then enter the reference number below.");
        gcHelp.setWrapText(true);
        gcHelp.setMinHeight(Region.USE_PREF_SIZE);
        gcHelp.setFont(Font.font("Segoe UI", 13));
        gcHelp.setTextFill(Color.web(TEXT_MUTED));
        VBox gcashPanel = new VBox(10, gcTitle,
                detailRow("Account Name", camp.gcashName),
                detailRow("GCash Number", camp.gcashNumber),
                gcHelp);
        gcashPanel.setStyle("-fx-background-color: #eef4ff; -fx-background-radius: 14; " +
                "-fx-border-color: #bcd1f7; -fx-border-radius: 14; -fx-padding: 16 18 16 18;");

        // ----- Walk-in panel -----
        Label wkTitle = new Label("Walk-in Donation");
        wkTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        wkTitle.setTextFill(Color.web(NAVY_DEEP));
        Label wkHelp = new Label("Bring your cash donation to the drop-off location below, then enter the amount you will give.");
        wkHelp.setWrapText(true);
        wkHelp.setMinHeight(Region.USE_PREF_SIZE);
        wkHelp.setFont(Font.font("Segoe UI", 13));
        wkHelp.setTextFill(Color.web(TEXT_MUTED));
        VBox walkPanel = new VBox(10, wkTitle, wkHelp,
                detailRow("Drop-off", camp.walkinLocation),
                detailRow("Schedule", camp.walkinSchedule()));
        walkPanel.setStyle("-fx-background-color: #eef4ff; -fx-background-radius: 14; " +
                "-fx-border-color: #bcd1f7; -fx-border-radius: 14; -fx-padding: 16 18 16 18;");

        // ----- reference number (GCash only) -----
        Label refLabel = formLabel("GCash Reference No.");
        TextField ref = new TextField();
        ref.setPromptText("Enter 13-digit GCash reference number");
        styleInput(ref);

        // ----- walk-in drop-off info (always shown) -----
        Label dropTitle = new Label("Walk-in Drop-off:");
        dropTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        dropTitle.setTextFill(Color.web(NAVY_DEEP));
        Label dropText = new Label(camp.walkinLocation + " | " + camp.walkinSchedule());
        dropText.setWrapText(true);
        dropText.setMinHeight(Region.USE_PREF_SIZE);
        dropText.setFont(Font.font("Segoe UI", 12));
        dropText.setTextFill(Color.web(TEXT_MUTED));
        HBox dropRow = new HBox(8, icon(EV_PIN, TEXT_MUTED, 15), new VBox(2, dropTitle, dropText));
        dropRow.setAlignment(Pos.TOP_LEFT);

        Label errorLabel = new Label(" ");
        errorLabel.setFont(Font.font("Segoe UI", 13));
        errorLabel.setWrapText(true);
        errorLabel.setTextFill(Color.web("#9b2c2c"));

        Runnable applyMethod = () -> {
            boolean gcash = gcashBtn.isSelected();
            gcashPanel.setVisible(gcash);
            gcashPanel.setManaged(gcash);
            walkPanel.setVisible(!gcash);
            walkPanel.setManaged(!gcash);
            refLabel.setVisible(gcash);
            refLabel.setManaged(gcash);
            ref.setVisible(gcash);
            ref.setManaged(gcash);
            errorLabel.setText(" ");
        };
        group.selectedToggleProperty().addListener((obs, o, n) -> applyMethod.run());
        applyMethod.run();

        Button donateBtn = primaryButton("Donate Now", 16, 14, 22);
        donateBtn.setGraphic(icon(ICON_HEART, "#ffffff", 17));
        donateBtn.setGraphicTextGap(8);
        donateBtn.setMaxWidth(Double.MAX_VALUE);
        donateBtn.setOnAction(e -> {
            boolean gcash = gcashBtn.isSelected();
            double amt;
            try {
                amt = Double.parseDouble(amount.getText() == null ? "" : amount.getText().trim().replace(",", ""));
            } catch (NumberFormatException ex) {
                amt = -1;
            }
            if (amt <= 0) {
                errorLabel.setText("Please enter a valid amount greater than 0.");
                return;
            }
            String refNo = ref.getText() == null ? "" : ref.getText().trim();
            if (gcash && refNo.isEmpty()) {
                errorLabel.setText("Please enter your GCash reference number.");
                return;
            }

            String shown = "\u20B1" + String.format(Locale.ENGLISH, "%,.2f", amt);
            String date = todayText();
            String noteText = (gcash ? "GCash - Ref " + refNo : "Walk-in - " + camp.walkinLocation) + " | " + camp.name;

            // saved in the shared store, so the Admin dashboard sees it too
            DonationStore.Donation saved = DonationStore.add(username, "Cash", shown, amt, date, noteText, camp.id);
            addActivity("Donation", "Cash donation to " + camp.name + ": " + shown, date, saved.status, saved.id, amt);
            addNotification(ICON_HEART, GREEN, GREEN_LIGHT, "Thank you! Your donation " + saved.id + " was submitted.");
            showPage("donations");
        });
        amount.setOnAction(e -> donateBtn.fire());

        resetCashForm = () -> {
            gcashBtn.setSelected(true);
            amount.clear();
            ref.clear();
            applyMethod.run();
        };

        VBox right = new VBox(12, pmTitle, pmSub, choiceRow,
                gcashPanel, walkPanel,
                refLabel, ref,
                new Separator(), dropRow,
                errorLabel, donateBtn);
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

        body.getChildren().addAll(titleCard, formCard);
        return wrapScroll(body);
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
        Group tick = icon(EV_TICK, "#ffffff", 13);
        StackPane indicator = new StackPane(ring, tick);
        indicator.setMaxSize(20, 20);
        indicator.setMouseTransparent(true);

        Runnable restyle = () -> {
            boolean on = tb.isSelected();
            tb.setStyle("-fx-cursor: hand; -fx-background-radius: 12; -fx-border-radius: 12; " +
                    (on ? "-fx-background-color: #eef4ff; -fx-border-color: " + BLUE + "; -fx-border-width: 2;"
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

    private Button linkButton(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: transparent; -fx-text-fill: " + NAVY_DEEP + "; -fx-font-weight: bold; " +
                "-fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 6 4 6 4;");
        return b;
    }

    /** "Label ..... value" row used inside the payment detail panels. */
    private HBox detailRow(String label, String value) {
        Label l = new Label(label);
        l.setFont(Font.font("Segoe UI", 13));
        l.setTextFill(Color.web(TEXT_MUTED));
        l.setMinWidth(150);
        Label v = new Label(value);
        v.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        v.setTextFill(Color.web(NAVY_DEEP));
        v.setWrapText(true);
        v.setMinHeight(Region.USE_PREF_SIZE);
        v.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(v, Priority.ALWAYS);
        HBox row = new HBox(10, l, v);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    // ---------- Volunteers page ----------

    private ScrollPane buildVolunteersPage() {
        VBox body = new VBox(22);
        body.setPadding(new Insets(28, 40, 32, 40));

        // ----- hero banner: photo on the left, title + subtitle on the right -----
        HBox hero = new HBox();
        hero.setAlignment(Pos.CENTER_LEFT);
        hero.setMinHeight(230);
        hero.setPrefHeight(230);
        hero.setMaxHeight(230);
        hero.setStyle("-fx-background-color: linear-gradient(to right, #e6efff, #d6e4fb); -fx-background-radius: 28; " +
                "-fx-border-color: rgba(255,255,255,0.9); -fx-border-radius: 28; -fx-border-width: 1.5;");

        StackPane photo = coverImageBox("volunteer_banner.jpg", 230);
        photo.prefWidthProperty().bind(hero.widthProperty().multiply(0.46));
        photo.setMinWidth(0);

        Label title = new Label("Volunteers");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 52));
        title.setTextFill(Color.web(NAVY_DEEP));
        Label sub = new Label("Become a volunteer and help make a difference\nin your community.");
        sub.setFont(Font.font("Segoe UI", 21));
        sub.setTextFill(Color.web(NAVY));
        VBox heroText = new VBox(10, title, sub);
        heroText.setAlignment(Pos.CENTER_LEFT);
        heroText.setPadding(new Insets(0, 0, 0, 44));
        HBox.setHgrow(heroText, Priority.ALWAYS);

        hero.getChildren().addAll(photo, heroText, heroDecoration());

        Rectangle heroClip = new Rectangle();
        heroClip.setArcWidth(56);
        heroClip.setArcHeight(56);
        heroClip.widthProperty().bind(hero.widthProperty());
        heroClip.heightProperty().bind(hero.heightProperty());
        hero.setClip(heroClip);
        // the shadow is on a wrapper, because a clipped node would cut its own shadow off
        StackPane heroWrap = new StackPane(hero);
        heroWrap.setEffect(new DropShadow(22, Color.rgb(20, 40, 90, 0.14)));

        // ----- red "not registered" alert (hidden once the user is registered) -----
        Label alertTitle = new Label("You are not registered as a volunteer yet.");
        alertTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 21));
        alertTitle.setTextFill(Color.web("#b91c1c"));
        Label alertSub = new Label("Register now to join community events and make an impact!");
        alertSub.setFont(Font.font("Segoe UI", 15));
        alertSub.setTextFill(Color.web("#dc2626"));
        HBox alert = new HBox(22, icon(ICON_WARN, "#dc2626", 44), new VBox(4, alertTitle, alertSub));
        alert.setAlignment(Pos.CENTER_LEFT);
        alert.setPadding(new Insets(22, 34, 22, 34));
        alert.setStyle("-fx-background-color: rgba(254,226,226,0.92); -fx-border-color: #fca5a5; " +
                "-fx-border-radius: 22; -fx-background-radius: 22; -fx-border-width: 1.5;");
        alert.setVisible(!volunteerRegistered);
        alert.setManaged(!volunteerRegistered);
        registerBanners.add(alert);

        // ----- How it works: 4 steps -----
        Label howTitle = new Label("HOW IT WORKS");
        howTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        howTitle.setTextFill(Color.web(NAVY_DEEP));
        StackPane listBadge = new StackPane(icon(ICON_LIST, BLUE, 24));
        listBadge.setMinSize(48, 48);
        listBadge.setMaxSize(48, 48);
        listBadge.setStyle("-fx-background-color: " + BLUE_LIGHT + "; -fx-background-radius: 24;");
        HBox howHead = new HBox(14, listBadge, howTitle);
        howHead.setAlignment(Pos.CENTER_LEFT);

        HBox stepsRow = new HBox(8);
        stepsRow.setAlignment(Pos.TOP_CENTER);
        String[][] steps = {
                {"1", ICON_FORM, "Fill Out the Registration Form", "Enter your full name, contact number, address, and skills."},
                {"2", ICON_SEND, "Submit Your Registration", "Review your information and click Submit Registration."},
                {"3", ICON_CLOCK, "Wait for Approval", "The administrator will review your submitted registration."},
                {"4", ICON_HEART, "Start Volunteering", "Once approved, you can participate in available events."}
        };
        for (int i = 0; i < steps.length; i++) {
            VBox st = howStep(steps[i][0], steps[i][1], steps[i][2], steps[i][3]);
            HBox.setHgrow(st, Priority.ALWAYS);
            stepsRow.getChildren().add(st);
            if (i < steps.length - 1) {
                StackPane chev = new StackPane(icon(ICON_CHEVRON, "#6b9cf5", 34));
                chev.setMinWidth(34);
                chev.setPadding(new Insets(48, 0, 0, 0));
                stepsRow.getChildren().add(chev);
            }
        }
        VBox how = new VBox(22, howHead, stepsRow);
        how.setPadding(new Insets(26, 30, 30, 30));
        how.setStyle("-fx-background-color: rgba(255,255,255,0.88); -fx-background-radius: 26; " +
                "-fx-border-color: rgba(255,255,255,0.95); -fx-border-radius: 26; -fx-border-width: 1.5;");
        how.setEffect(new DropShadow(20, Color.rgb(20, 40, 90, 0.10)));

        // ----- Register as Volunteer (opens the same registration form as before) -----
        Button registerBtn = primaryButton("Register as Volunteer  \u2192", 18, 17, 36, 30);
        registerBtn.setGraphic(icon(ICON_PEOPLE, "white", 26));
        registerBtn.setGraphicTextGap(12);
        registerBtn.setOnAction(e -> showPage("register"));

        // ----- Set as Inactive / Set as Active (left of Register). Only an approved volunteer can use it. -----
        Button statusBtn = new Button();
        statusBtn.setGraphicTextGap(12);
        refreshVolunteerStatusBtn = () -> {
            String st = volunteerRegistered ? VolunteerStore.statusOf(username) : null;
            boolean inactive = "Inactive".equalsIgnoreCase(st);
            boolean usable = inactive || "Active".equalsIgnoreCase(st);
            statusBtn.setText(inactive ? "Set as Active" : "Set as Inactive");
            statusBtn.setGraphic(icon(inactive ? ICON_CHECK : EV_CLOSE, "white", 26));
            statusBtn.setDisable(!usable);
            String color = inactive ? GREEN : "#dc2626", hoverColor = inactive ? "#15803d" : "#b91c1c";
            String base = "-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; "
                    + "-fx-font-size: 18px; -fx-background-radius: 30; -fx-cursor: hand; -fx-padding: 17 36 17 36;";
            statusBtn.setStyle(base);
            statusBtn.setOnMouseEntered(e -> statusBtn.setStyle(base.replace(color, hoverColor)));
            statusBtn.setOnMouseExited(e -> statusBtn.setStyle(base));
            String tip = usable ? (inactive ? "Become an active volunteer again and join events."
                    : "Pause your volunteering. You cannot join events while inactive.")
                    : !volunteerRegistered ? "Register as a volunteer first."
                    : "Available once the admin approves your registration.";
            statusBtn.setTooltip(new Tooltip(tip));
        };
        statusBtn.setOnAction(e -> {
            String st = VolunteerStore.statusOf(username);
            boolean toInactive = "Active".equalsIgnoreCase(st);
            if (!toInactive && !"Inactive".equalsIgnoreCase(st)) {   // not approved (any more): just redraw
                refreshVolunteerStatusBtn.run();
                return;
            }
            if (toInactive) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Set as Inactive");
                confirm.setHeaderText("Set yourself as inactive?");
                confirm.setContentText("You will not be able to join events until you set yourself as active again.");
                if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;
            }
            if (!VolunteerStore.setOwnStatus(username, toInactive ? "Inactive" : "Active")) {
                infoAlert("Could not change your status. Please try again.");
                return;
            }
            volunteerApproved = !toInactive;
            addNotification(ICON_CHECK, GREEN, GREEN_LIGHT, toInactive
                    ? "You are now set as inactive. You cannot join events until you set yourself as active."
                    : "You are active again. You can join events.");
            refreshVolunteerStatusBtn.run();
        });
        refreshVolunteerStatusBtn.run();

        HBox btnRow = new HBox(18, statusBtn, registerBtn);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        body.getChildren().addAll(heroWrap, alert, how, btnRow);
        return wrapScroll(body);
    }

    /** One "How it works" step: icon circle with a number badge, a bold title and a short description. */
    private VBox howStep(String num, String iconPath, String title, String desc) {
        StackPane circle = new StackPane(icon(iconPath, BLUE, 50));
        circle.setMinSize(118, 118);
        circle.setMaxSize(118, 118);
        circle.setStyle("-fx-background-color: " + BLUE_LIGHT + "; -fx-background-radius: 59;");

        Label badge = new Label(num);
        badge.setMinSize(30, 30);
        badge.setMaxSize(30, 30);
        badge.setAlignment(Pos.CENTER);
        badge.setStyle("-fx-background-color: " + BLUE + "; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 14px; -fx-background-radius: 15; -fx-border-color: white; " +
                "-fx-border-radius: 15; -fx-border-width: 2;");
        StackPane top = new StackPane(circle, badge);
        StackPane.setAlignment(badge, Pos.TOP_CENTER);
        top.setMaxSize(118, 132);
        circle.setTranslateY(10);

        Label t = new Label(title);
        t.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        t.setTextFill(Color.web(NAVY_DEEP));
        t.setWrapText(true);
        t.setTextAlignment(TextAlignment.CENTER);
        t.setAlignment(Pos.CENTER);
        Label d = new Label(desc);
        d.setFont(Font.font("Segoe UI", 15));
        d.setTextFill(Color.web(NAVY));
        d.setWrapText(true);
        d.setTextAlignment(TextAlignment.CENTER);
        d.setAlignment(Pos.CENTER);

        // a normal starting width: with a tiny one, the wrapped text is measured one letter per line
        // and the whole card becomes extremely tall
        t.setMaxWidth(Double.MAX_VALUE);
        d.setMaxWidth(Double.MAX_VALUE);
        t.setMinHeight(Region.USE_PREF_SIZE);
        d.setMinHeight(Region.USE_PREF_SIZE);

        VBox box = new VBox(10, top, t, d);
        box.setAlignment(Pos.TOP_CENTER);
        box.setMinWidth(180);
        box.setPrefWidth(260);
        return box;
    }

    /** A box that shows a photo center-cropped to fill it (never stretched); width follows the layout. */
    /** A bundled image (images folder) or a photo file chosen by the admin; null if there is none. */
    private java.io.InputStream openCoverImage(String imageFile) throws java.io.IOException {
        if (imageFile == null || imageFile.isBlank()) return null;
        java.io.File f = new java.io.File(imageFile);
        if (f.isFile()) return new java.io.FileInputStream(f);
        return getClass().getResourceAsStream("/images/" + imageFile);
    }

    private StackPane coverImageBox(String imageFile, double height) {
        StackPane box = new StackPane();
        box.setMinSize(0, height);
        box.setPrefHeight(height);
        box.setMaxHeight(height);
        try (java.io.InputStream in = openCoverImage(imageFile)) {
            if (in != null) {
                Image img = new Image(in);
                ImageView iv = new ImageView(img);
                iv.setManaged(false);
                iv.setSmooth(true);
                iv.setFitHeight(height);
                iv.fitWidthProperty().bind(box.widthProperty());
                box.widthProperty().addListener((obs, o, w) -> {
                    double bw = w.doubleValue();
                    if (bw <= 0) return;
                    double boxRatio = bw / height, imgRatio = img.getWidth() / img.getHeight();
                    double vw = imgRatio > boxRatio ? img.getHeight() * boxRatio : img.getWidth();
                    double vh = imgRatio > boxRatio ? img.getHeight() : img.getWidth() / boxRatio;
                    iv.setViewport(new Rectangle2D((img.getWidth() - vw) / 2, (img.getHeight() - vh) / 2, vw, vh));
                });
                box.getChildren().add(iv);
            } else if (imageFile != null && !imageFile.isBlank()) {
                System.out.println("IMAGE ERROR: " + imageFile + " not found");
            }
        } catch (Exception ex) {
            System.out.println("IMAGE ERROR: " + ex.getMessage());
        }
        return box;
    }

    private HBox stepRow(String num, String title, String desc) {
        Label n = new Label(num);
        n.setMinSize(38, 38);
        n.setMaxSize(38, 38);
        n.setAlignment(Pos.CENTER);
        n.setStyle("-fx-background-color: " + BLUE_LIGHT + "; -fx-text-fill: " + BLUE + "; " +
                "-fx-font-weight: bold; -fx-font-size: 15px; -fx-background-radius: 19;");
        Label t = new Label(title);
        t.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        t.setTextFill(Color.web(NAVY_DEEP));
        Label d = new Label(desc);
        d.setFont(Font.font("Segoe UI", 12));
        d.setTextFill(Color.web(TEXT_MUTED));
        HBox row = new HBox(14, n, new VBox(2, t, d));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 0, 6, 0));
        return row;
    }

    /** Yellow "register first" banner; hidden once the user is registered. */
    private HBox registerBanner(String title, String sub) {
        Label t = new Label(title);
        t.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        t.setTextFill(Color.web("#854d0e"));
        Label s = new Label(sub);
        s.setFont(Font.font("Segoe UI", 12));
        s.setTextFill(Color.web("#a16207"));

        Button registerNowBtn = primaryButton("Register Now");
        registerNowBtn.setOnAction(e -> showPage("register"));

        HBox banner = new HBox(12, icon(ICON_BELL, "#ca8a04", 22), new VBox(2, t, s), spacer(), registerNowBtn);
        banner.setAlignment(Pos.CENTER_LEFT);
        banner.setPadding(new Insets(16, 22, 16, 22));
        banner.setStyle("-fx-background-color: rgba(254,249,195,0.85); -fx-border-color: #fde68a; " +
                "-fx-border-radius: 20; -fx-background-radius: 20; -fx-border-width: 1.5;");
        banner.setVisible(!volunteerRegistered);
        banner.setManaged(!volunteerRegistered);
        registerBanners.add(banner);
        return banner;
    }

    /** Redraws the "Set as Inactive / Set as Active" button of the Volunteers page (set when the page is built). */
    private Runnable refreshVolunteerStatusBtn = () -> {};

    private void markRegistered() {
        volunteerRegistered = true;
        refreshVolunteerStatusBtn.run();
        for (Node b : registerBanners) {
            b.setVisible(false);
            b.setManaged(false);
        }
    }

    // ---------- Events page ----------

    // {title, tagline (= category), date (yyyy-MM-dd), location, percent (unused), image, category, time, organizer,
    //  about, req1, req2, req3, id, status, joined, volunteersNeeded}   - see EventStore.toUserRow
    // Events & programs are created by the admin and read from PostgreSQL (see EventStore).
    // (reloaded by refreshEventsFromDb(), so it is not final)
    private List<String[]> eventRows = EventStore.userRows();

    private ScrollPane buildEventsPage() {
        VBox body = new VBox(24);
        body.setPadding(new Insets(32, 48, 32, 48));

        Label title = new Label("Events & Programs");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        title.setTextFill(Color.web(NAVY_DEEP));

        // ----- search bar + Search button -----
        TextField search = new TextField();
        search.setPromptText("Search for events, programs, or initiatives...");
        StackPane searchBox = iconField(EV_SEARCH, search);
        search.setPrefHeight(54);
        search.setStyle(search.getStyle() + "-fx-font-size: 16px;");
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        Button searchBtn = primaryButton("Search", 16, 15, 34);
        HBox searchRow = new HBox(16, searchBox, searchBtn);
        searchRow.setAlignment(Pos.CENTER_LEFT);

        // ----- cards: 4 per row, in two sections: open for joining, and upcoming (not open yet) -----
        GridPane openGrid = newEventGrid();
        GridPane upcomingGrid = newEventGrid();
        VBox openSection = new VBox(14, eventSectionHeader("Open Events", ICON_CLOCK,
                "Events you can join now."), openGrid);
        VBox upcomingSection = new VBox(14, eventSectionHeader("Upcoming Events", ICON_CALENDAR,
                "Scheduled events. Joining opens once the admin opens registration."), upcomingGrid);
        List<VBox> cards = new ArrayList<>();

        Label none = new Label("No events found.");
        none.setFont(Font.font("Segoe UI", 16));
        none.setTextFill(Color.web(NAVY));

        // ----- filter chips: "All" + every category the admin used (rebuilt whenever the events change) -----
        String[] active = {"All"};
        Runnable[] applyFilter = new Runnable[1];
        HBox chipRow = new HBox(14);
        chipRow.setAlignment(Pos.CENTER_LEFT);
        List<Button> chips = new ArrayList<>();
        applyFilter[0] = () -> {
            String q = search.getText() == null ? "" : search.getText().trim().toLowerCase(Locale.ENGLISH);
            for (Button chip : chips) {
                boolean on = chip.getUserData().equals(active[0]);
                chip.setStyle("-fx-background-radius: 26; -fx-cursor: hand; -fx-font-size: 16px; -fx-padding: 12 34 12 34; " +
                        (on ? "-fx-background-color: " + BLUE + "; -fx-text-fill: white; -fx-font-weight: bold;"
                                : "-fx-background-color: " + BLUE_LIGHT + "; -fx-text-fill: " + NAVY + ";"));
            }
            openGrid.getChildren().clear();
            upcomingGrid.getChildren().clear();
            int shownOpen = 0, shownUpcoming = 0;
            for (VBox c : cards) {
                String[] e = (String[]) c.getUserData();
                boolean okCat = "All".equals(active[0]) || e[6].equals(active[0]);
                boolean okText = q.isEmpty()
                        || e[0].toLowerCase(Locale.ENGLISH).contains(q)
                        || e[1].toLowerCase(Locale.ENGLISH).contains(q)
                        || e[3].toLowerCase(Locale.ENGLISH).contains(q);
                if (okCat && okText) {
                    if (isUpcomingEvent(e)) {
                        upcomingGrid.add(c, shownUpcoming % 4, shownUpcoming / 4);
                        shownUpcoming++;
                    } else {
                        openGrid.add(c, shownOpen % 4, shownOpen / 4);
                        shownOpen++;
                    }
                }
            }
            openSection.setVisible(shownOpen > 0);
            openSection.setManaged(shownOpen > 0);
            upcomingSection.setVisible(shownUpcoming > 0);
            upcomingSection.setManaged(shownUpcoming > 0);
            boolean nothing = shownOpen + shownUpcoming == 0;
            none.setVisible(nothing);
            none.setManaged(nothing);
        };
        Runnable reload = () -> {
            eventJoinButtons.clear();
            cards.clear();
            for (String[] e : eventRows) cards.add(eventCard(e));
            List<String> cats = new ArrayList<>();
            cats.add("All");
            for (String[] e : eventRows) if (!e[6].isBlank() && !cats.contains(e[6])) cats.add(e[6]);
            if (!cats.contains(active[0])) active[0] = "All";
            chips.clear();
            chipRow.getChildren().clear();
            for (String cat : cats) {
                Button chip = new Button(cat);
                chip.setUserData(cat);
                chip.setOnAction(e -> {
                    active[0] = cat;
                    applyFilter[0].run();
                });
                chips.add(chip);
                chipRow.getChildren().add(chip);
            }
            applyFilter[0].run();
        };
        reloadEventsPage = reload;
        searchBtn.setOnAction(e -> applyFilter[0].run());
        search.setOnAction(e -> applyFilter[0].run());
        search.textProperty().addListener((obs, o, n) -> applyFilter[0].run());
        reload.run();

        body.getChildren().addAll(title, searchRow, chipRow, openSection, upcomingSection, none);
        return wrapScroll(body);
    }

    private GridPane newEventGrid() {
        GridPane g = new GridPane();
        g.setHgap(22);
        g.setVgap(22);
        for (int i = 0; i < 4; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(25);
            cc.setHgrow(Priority.ALWAYS);
            g.getColumnConstraints().add(cc);
        }
        return g;
    }

    private VBox eventSectionHeader(String text, String iconPath, String subtitle) {
        Label sub = new Label(subtitle);
        sub.setFont(Font.font("Segoe UI", 13));
        sub.setTextFill(Color.web(TEXT_MUTED));
        return new VBox(2, sectionTitle(text, iconPath), sub);
    }

    /** An event the admin has not opened for registration yet: it is shown, but cannot be joined. */
    private static boolean isUpcomingEvent(String[] e) {
        return "Upcoming".equalsIgnoreCase(e[14]);
    }

    /** One event card: photo, title, tagline, date + place, progress bar and a Join Event button. */
    private VBox eventCard(String[] e) {
        final double IMG_H = 175;
        LocalDate d = LocalDate.parse(e[2]);

        StackPane img = eventCoverBox(e, IMG_H);
        Rectangle clip = new Rectangle();
        clip.setArcWidth(26);
        clip.setArcHeight(26);
        clip.widthProperty().bind(img.widthProperty());
        clip.setHeight(IMG_H);
        img.setClip(clip);

        Label name = new Label(e[0]);
        name.setFont(Font.font("Segoe UI", FontWeight.BOLD, 19));
        name.setTextFill(Color.web(NAVY_DEEP));
        name.setWrapText(true);
        name.setMaxWidth(Double.MAX_VALUE);
        name.setMinHeight(Region.USE_PREF_SIZE);

        Label tag = new Label(e[1]);
        tag.setFont(Font.font("Segoe UI", 15));
        tag.setTextFill(Color.web(NAVY));
        tag.setWrapText(true);
        tag.setMaxWidth(Double.MAX_VALUE);
        tag.setMinHeight(Region.USE_PREF_SIZE);

        Label dateL = new Label(d.format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)));
        dateL.setFont(Font.font("Segoe UI", 14));
        dateL.setTextFill(Color.web(NAVY_DEEP));
        Label locL = new Label(e[3]);
        locL.setFont(Font.font("Segoe UI", 14));
        locL.setTextFill(Color.web(NAVY_DEEP));
        locL.setWrapText(true);
        VBox meta = new VBox(6, new HBox(6, icon(ICON_CALENDAR, NAVY_DEEP, 18), dateL));
        if (e[7] != null && !e[7].isBlank()) {
            Label timeL = new Label(e[7]);
            timeL.setFont(Font.font("Segoe UI", 14));
            timeL.setTextFill(Color.web(NAVY_DEEP));
            meta.getChildren().add(new HBox(6, icon(ICON_CLOCK, NAVY_DEEP, 18), timeL));
        }
        meta.getChildren().add(new HBox(6, icon(EV_PIN, NAVY_DEEP, 18), locL));
        for (Node n : meta.getChildren()) ((HBox) n).setAlignment(Pos.CENTER_LEFT);

        // the status and joined number the admin sees for this event
        String[] sc = eventStatusColors(e[14]);
        Label statusPill = new Label(e[14]);
        statusPill.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        statusPill.setTextFill(Color.web(sc[0]));
        statusPill.setPadding(new Insets(4, 12, 4, 12));
        statusPill.setStyle("-fx-background-color: " + sc[1] + "; -fx-background-radius: 14;");
        Label joinedL = new Label(joinedText(e));
        joinedL.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        joinedL.setTextFill(Color.web(NAVY_DEEP));
        HBox statusRow = new HBox(12, statusPill, joinedL);
        statusRow.setAlignment(Pos.CENTER_LEFT);

        Button join = primaryButton("Join Event", 16, 13, 20);
        join.setGraphic(icon(ICON_PEOPLE, "white", 22));
        join.setGraphicTextGap(10);
        join.setMaxWidth(Double.MAX_VALUE);
        join.setOnAction(ev -> onJoinEventClick(e));
        eventJoinButtons.put(e[13], join);
        if (joinedEvents.contains(e[13])) {
            join.setText(registrationButtonText(e[13]));
            join.setDisable(true);
        } else if (isUpcomingEvent(e)) {
            join.setText("Not Open Yet");
            join.setDisable(true);
        } else if (isEventFull(e)) {
            join.setText("Event Full");
            join.setDisable(true);
        }

        VBox info = new VBox(10, name, tag, meta, statusRow);
        if (volunteersNeeded(e) > 0) {
            ProgressBar volBar = new ProgressBar(volunteerPercent(e) / 100.0);
            volBar.setMaxWidth(Double.MAX_VALUE);
            volBar.setStyle("-fx-accent: " + GREEN + ";");
            info.getChildren().add(volBar);
        }
        info.getChildren().add(join);
        info.setPadding(new Insets(4, 8, 8, 8));

        VBox card = new VBox(10, img, info);
        card.setPadding(new Insets(10));
        card.setMaxWidth(Double.MAX_VALUE);
        card.setUserData(e);
        card.setStyle("-fx-background-color: rgba(255,255,255,0.82); -fx-background-radius: 26; " +
                "-fx-border-color: rgba(255,255,255,0.95); -fx-border-radius: 26; -fx-border-width: 1.5;");
        card.setEffect(new DropShadow(22, Color.rgb(20, 40, 90, 0.12)));
        return card;
    }

    /** The event photo, or (when the admin did not add one) the same category-coloured block the admin sees. */
    private StackPane eventCoverBox(String[] e, double height) {
        StackPane box = coverImageBox(e[5], height);
        if (!box.getChildren().isEmpty() && box.getChildren().get(0) instanceof ImageView iv
                && (iv.getImage() == null || iv.getImage().isError())) {
            box.getChildren().clear();   // the file is there but is not a readable image
        }
        if (box.getChildren().isEmpty()) {
            String[] grad = switch (e[6] == null ? "" : e[6]) {
                case "Environment" -> new String[]{"#16a34a", "#0d9488"};
                case "Health" -> new String[]{"#f87171", "#dc2626"};
                case "Relief" -> new String[]{"#fb923c", "#ea580c"};
                case "Training" -> new String[]{"#a78bfa", "#6d28d9"};
                case "Outreach" -> new String[]{"#60a5fa", "#2563eb"};
                default -> new String[]{"#3b82f6", "#7c3aed"};
            };
            box.setStyle("-fx-background-color: linear-gradient(to bottom right, " + grad[0] + ", " + grad[1] + ");");
            Group glyph = icon(ICON_CALENDAR, "#ffffff", 46);
            glyph.setOpacity(0.55);
            box.getChildren().add(glyph);
        }
        return box;
    }

    /** {text colour, background colour} of the status pill: green for open / ongoing, blue for upcoming / scheduled. */
    private String[] eventStatusColors(String status) {
        if ("Registration Open".equalsIgnoreCase(status) || "Ongoing".equalsIgnoreCase(status)) {
            return new String[]{GREEN, GREEN_LIGHT};
        }
        return new String[]{BLUE, BLUE_LIGHT};
    }

    private static boolean sameRows(List<String[]> a, List<String[]> b) {
        if (a.size() != b.size()) return false;
        for (int i = 0; i < a.size(); i++) {
            if (!java.util.Arrays.equals(a.get(i), b.get(i))) return false;
        }
        return true;
    }

    /**
     * Reads the events (and which ones this user joined) from the database again, off the UI thread,
     * and redraws the Events page only when something is different from what is on screen.
     */
    private void refreshEventsFromDb() {
        if (!eventsRefreshing.compareAndSet(false, true)) return;
        final int stampAtStart = joinStamp;
        Thread t = new Thread(() -> {
            try {
                final List<String[]> fresh = EventStore.userRowsOrNull();
                final Map<String, String> statuses = EventStore.registrationStatusesOrNull(username);
                javafx.application.Platform.runLater(() -> {
                    try {
                        applyEventsFromDb(fresh, stampAtStart == joinStamp ? statuses : null);
                    } finally {
                        eventsRefreshing.set(false);
                    }
                });
            } catch (RuntimeException ex) {
                eventsRefreshing.set(false);
                System.err.println("Events refresh failed: " + ex.getMessage());
            }
        }, "events-refresh");
        t.setDaemon(true);
        t.start();
    }

    /** Runs on the UI thread. A null argument means "could not be read", so the screen keeps what it shows. */
    private void applyEventsFromDb(List<String[]> fresh, Map<String, String> statuses) {
        boolean changed = false;
        if (fresh != null && !sameRows(fresh, eventRows)) {
            eventRows = fresh;
            changed = true;
        }
        if (statuses != null && !statuses.equals(eventRegStatus)) {
            Map<String, String> before = new HashMap<>(eventRegStatus);
            eventRegStatus.clear();
            eventRegStatus.putAll(statuses);
            joinedEvents.clear();
            joinedEvents.addAll(statuses.keySet());
            announceRegistrationChanges(before);
            syncEventActivityStatuses();
            changed = true;
        }
        if (!changed) return;
        reloadEventsPage.run();
        if (eventsAvailableLbl != null) eventsAvailableLbl.setText(String.valueOf(eventRows.size()));
        if (eventsJoinedLbl != null) eventsJoinedLbl.setText(String.valueOf(joinedCount()));
        if (statEventsLbl != null) statEventsLbl.setText(String.valueOf(joinedCount()));
    }

    /** Events this user joined that the admin has not rejected. */
    private int joinedCount() {
        int n = 0;
        for (String id : joinedEvents) {
            if (!"Rejected".equalsIgnoreCase(eventRegStatus.get(id))) n++;
        }
        return n;
    }

    private String eventNameFor(String eventId) {
        for (String[] e : eventRows) {
            if (e[13].equals(eventId)) return e[0];
        }
        return "an event";
    }

    /** Tells the user (notification bell) when the admin approved or rejected a registration. */
    private void announceRegistrationChanges(Map<String, String> before) {
        for (Map.Entry<String, String> en : eventRegStatus.entrySet()) {
            String old = before.get(en.getKey());
            if (!"Pending".equalsIgnoreCase(old)) continue;
            if ("Approved".equalsIgnoreCase(en.getValue())) {
                addNotification(ICON_CHECK, GREEN, GREEN_LIGHT,
                        "You were approved to join " + eventNameFor(en.getKey()) + "!");
            } else if ("Rejected".equalsIgnoreCase(en.getValue())) {
                addNotification(ICON_BELL, ORANGE, ORANGE_LIGHT,
                        "Your registration for " + eventNameFor(en.getKey()) + " was not approved.");
            }
        }
    }

    /** Keeps the "Joined: ..." rows of My Activities in step with what the admin decided. */
    private void syncEventActivityStatuses() {
        boolean any = false;
        for (int i = 0; i < activityRows.size(); i++) {
            String[] r = activityRows.get(i);
            if (!"Event".equals(r[0]) || !r[1].startsWith("Joined: ")) continue;
            String name = r[1].substring("Joined: ".length());
            for (String[] e : eventRows) {
                if (!e[0].equals(name)) continue;
                String st = eventRegStatus.get(e[13]);
                if (st != null && !st.equals(r[3])) {
                    String[] copy = r.clone();
                    copy[3] = st;
                    activityRows.set(i, copy);
                    any = true;
                }
                break;
            }
        }
        if (any) {
            if (refreshActivities != null) refreshActivities.run();
            if (refreshRecent != null) refreshRecent.run();
        }
    }

    private String registrationButtonText(String eventId) {
        String st = eventRegStatus.get(eventId);
        if ("Approved".equalsIgnoreCase(st)) return "Approved \u2713";
        if ("Rejected".equalsIgnoreCase(st)) return "Not Approved";
        return "Pending Approval";
    }

    /** A text field with a small icon inside its left side. */
    private StackPane iconField(String iconPath, TextField f) {
        styleInput(f);
        f.setStyle(f.getStyle() + "-fx-padding: 0 12 0 46;");
        f.setMaxWidth(Double.MAX_VALUE);
        Group ic = icon(iconPath, "#6b7a99", 20);
        StackPane box = new StackPane(f, ic);
        StackPane.setAlignment(ic, Pos.CENTER_LEFT);
        StackPane.setMargin(ic, new Insets(0, 0, 0, 16));
        ic.setMouseTransparent(true);
        return box;
    }

    /** Volunteers the admin asked for (0 = no limit). Column 16 of a user event row. */
    private static int volunteersNeeded(String[] e) {
        try {
            return e.length > 16 ? Integer.parseInt(e[16].trim()) : 0;
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    /** "1 / 20 joined (5%)" when the admin set how many volunteers are needed, otherwise "1 joined". */
    private static String joinedText(String[] e) {
        int needed = volunteersNeeded(e);
        return needed > 0 ? e[15] + " / " + needed + " joined (" + volunteerPercent(e) + "%)" : e[15] + " joined";
    }

    /** How full the event is, 0-100 (0 when the admin set no number of volunteers needed). */
    private static int volunteerPercent(String[] e) {
        int needed = volunteersNeeded(e);
        if (needed <= 0) return 0;
        try {
            return Math.min(100, (int) Math.round(Integer.parseInt(e[15].trim()) * 100.0 / needed));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private static boolean isEventFull(String[] e) {
        int needed = volunteersNeeded(e);
        try {
            return needed > 0 && Integer.parseInt(e[15].trim()) >= needed;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /** Why this user cannot join events right now: still pending, or set as inactive. */
    private String notApprovedMessage() {
        return "Inactive".equalsIgnoreCase(VolunteerStore.statusOf(username))
                ? "You are set as inactive.\nGo to Volunteers and click \"Set as Active\" to join events again."
                : PENDING_APPROVAL_MSG;
    }

    private static final String PENDING_APPROVAL_MSG =
            "Your volunteer registration is still waiting for admin approval.\n"
                    + "You can join events once the admin approves you.";

    /** Asks the database again, so an approval (or a rejection) that just happened is picked up. */
    private boolean checkVolunteerApproved() {
        volunteerApproved = VolunteerStore.isApproved(username);
        return volunteerApproved;
    }

    private void onJoinEventClick(String[] e) {
        if (isUpcomingEvent(e)) {
            infoAlert("\"" + e[0] + "\" is still upcoming. You can join once registration opens.");
            return;
        }
        if (!volunteerRegistered) {
            infoAlert("Please register as a volunteer first before joining events.");
            showPage("register");
            return;
        }
        if (!checkVolunteerApproved()) {
            infoAlert(notApprovedMessage());
            return;
        }
        if (joinedEvents.contains(e[13])) {
            String st = eventRegStatus.get(e[13]);
            infoAlert("Approved".equalsIgnoreCase(st) ? "You are approved to join \"" + e[0] + "\"."
                    : "Rejected".equalsIgnoreCase(st) ? "Your registration for \"" + e[0] + "\" was not approved."
                    : "You already joined \"" + e[0] + "\". It is waiting for admin approval.");
            return;
        }
        if (isEventFull(e)) {
            infoAlert("Sorry, \"" + e[0] + "\" already has all the volunteers it needs.");
            return;
        }
        showEventDialog(e);
    }

    /** The "Join Event" pop-up: event details on the left/top, "Your Participation" form on the right. */
    private void showEventDialog(String[] e) {
        LocalDate d = LocalDate.parse(e[2]);
        String longDate = d.format(DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH));
        String shortDate = d.format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH));
        final double LEFT_W = 420, RIGHT_W = 470;

        Stage dlg = new Stage();
        dlg.initOwner(stage);
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.initStyle(StageStyle.TRANSPARENT);

        // ===== left column: photo + badge, About, Requirements =====
        StackPane photo = eventCoverBox(e, 215);
        photo.setMinWidth(LEFT_W);
        photo.setPrefWidth(LEFT_W);
        photo.setMaxWidth(LEFT_W);
        Rectangle photoClip = new Rectangle(LEFT_W, 215);
        photoClip.setArcWidth(34);
        photoClip.setArcHeight(34);
        photo.setClip(photoClip);

        Label badgeText = new Label(e[6] == null || e[6].isBlank() ? "Event" : e[6]);
        badgeText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        badgeText.setTextFill(Color.WHITE);
        HBox badge = new HBox(8, icon(ICON_PEOPLE, "white", 18), badgeText);
        badge.setAlignment(Pos.CENTER);
        badge.setPadding(new Insets(7, 16, 7, 14));
        badge.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        badge.setStyle("-fx-background-color: " + BLUE + "; -fx-background-radius: 20;");
        StackPane photoBox = new StackPane(photo, badge);
        StackPane.setAlignment(badge, Pos.TOP_LEFT);
        StackPane.setMargin(badge, new Insets(12, 0, 0, 12));
        photoBox.setMaxWidth(LEFT_W);

        Label aboutTitle = new Label("About This Event");
        aboutTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        aboutTitle.setTextFill(Color.web(NAVY_DEEP));
        HBox aboutHead = new HBox(10, icon(ICON_FORM, BLUE, 26), aboutTitle);
        aboutHead.setAlignment(Pos.CENTER_LEFT);
        Label aboutText = new Label(e[9]);
        aboutText.setFont(Font.font("Segoe UI", 15));
        aboutText.setTextFill(Color.web(NAVY_DEEP));
        aboutText.setWrapText(true);
        aboutText.setMinHeight(Region.USE_PREF_SIZE);

        Label reqTitle = new Label("Requirements");
        reqTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        reqTitle.setTextFill(Color.web(NAVY_DEEP));
        HBox reqHead = new HBox(10, icon(EV_SHIELD, NAVY, 26), reqTitle);
        reqHead.setAlignment(Pos.CENTER_LEFT);
        VBox reqBox = new VBox(12, reqHead);
        for (int i = 10; i <= 12; i++) {
            if (e[i] == null || e[i].isBlank()) continue;
            Label r = new Label(e[i]);
            r.setFont(Font.font("Segoe UI", 15));
            r.setTextFill(Color.web(NAVY_DEEP));
            HBox row = new HBox(12, icon(ICON_CHECK, GREEN, 24), r);
            row.setAlignment(Pos.CENTER_LEFT);
            reqBox.getChildren().add(row);
        }
        reqBox.setPadding(new Insets(16, 20, 18, 20));
        reqBox.setStyle("-fx-background-color: #e4ecfb; -fx-background-radius: 18; " +
                "-fx-border-color: #c7d7f5; -fx-border-radius: 18;");

        // the admin does not enter an "about" text or requirements, so those boxes only show when there is something in them
        VBox left = new VBox(16, photoBox);
        if (e[9] != null && !e[9].isBlank()) left.getChildren().addAll(aboutHead, aboutText);
        if (reqBox.getChildren().size() > 1) left.getChildren().add(reqBox);
        left.setMinWidth(LEFT_W);
        left.setPrefWidth(LEFT_W);
        left.setMaxWidth(LEFT_W);

        // ===== right column: title, status, details, participation form =====
        Label title = new Label(e[0]);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setTextFill(Color.web(NAVY_DEEP));
        title.setWrapText(true);
        title.setMinHeight(Region.USE_PREF_SIZE);
        title.setMaxWidth(RIGHT_W - 50);

        String[] stColors = eventStatusColors(e[14]);
        Circle dot = new Circle(6, Color.web(stColors[0]));
        Label statusText = new Label(e[14]);
        statusText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        statusText.setTextFill(Color.web(stColors[0]));
        HBox status = new HBox(10, dot, statusText);
        status.setAlignment(Pos.CENTER_LEFT);
        status.setPadding(new Insets(8, 20, 8, 16));
        status.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        status.setStyle("-fx-background-color: " + stColors[1] + "; -fx-background-radius: 20;");

        Label tagline = new Label(e[1]);
        tagline.setFont(Font.font("Segoe UI", 17));
        tagline.setTextFill(Color.web(NAVY_DEEP));
        tagline.setWrapText(true);
        tagline.setMinHeight(Region.USE_PREF_SIZE);

        Region sep = new Region();
        sep.setMinHeight(1);
        sep.setPrefHeight(1);
        sep.setMaxHeight(1);
        sep.setStyle("-fx-background-color: #cfd9ee;");

        VBox details = new VBox(12, detailLine(ICON_CALENDAR, longDate));
        if (e[7] != null && !e[7].isBlank()) details.getChildren().add(detailLine(ICON_CLOCK, e[7]));
        details.getChildren().addAll(
                detailLine(EV_PIN, e[3]),
                detailLine(ICON_PEOPLE, joinedText(e)),
                detailLine(ICON_PEOPLE, "Organizer: " + e[8]));

        // --- Your Participation ---
        Label partTitle = new Label("Your Participation");
        partTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        partTitle.setTextFill(Color.web(NAVY_DEEP));
        HBox partHead = new HBox(10, icon(ICON_PEOPLE, BLUE, 30), partTitle);
        partHead.setAlignment(Pos.CENTER_LEFT);

        TextField nameField = new TextField(volunteerName);
        nameField.setPromptText("Full Name");
        StackPane nameBox = iconField(ICON_PERSON, nameField);
        TextField contactField = new TextField(volunteerContact);
        contactField.setPromptText("Contact Number");
        StackPane contactBox = iconField(EV_PHONE, contactField);

        TextArea remarks = new TextArea();
        remarks.setPromptText("Any special request or note...");
        remarks.setPrefRowCount(2);
        remarks.setWrapText(true);
        remarks.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #d1d5db; " +
                "-fx-control-inner-background: #f9fafb; -fx-font-size: 13px;");

        Label errorLabel = new Label(" ");
        errorLabel.setFont(Font.font("Segoe UI", 13));
        errorLabel.setTextFill(Color.web("#9b2c2c"));

        VBox participation = new VBox(8, partHead,
                partLabel("Full Name"), nameBox,
                partLabel("Contact Number"), contactBox,
                partLabel("Remarks (Optional)"), remarks);
        participation.setPadding(new Insets(18, 20, 20, 20));
        participation.setStyle("-fx-background-color: #eaf0fe; -fx-background-radius: 20; " +
                "-fx-border-color: #cddaf6; -fx-border-radius: 20;");

        VBox right = new VBox(12, title, status, tagline, sep, details, participation);
        right.setMinWidth(RIGHT_W);
        right.setPrefWidth(RIGHT_W);
        right.setMaxWidth(RIGHT_W);

        HBox columns = new HBox(30, left, right);
        columns.setAlignment(Pos.TOP_LEFT);
        columns.setPadding(new Insets(24, 34, 20, 34));

        ScrollPane scroll = new ScrollPane(columns);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        double maxH = Screen.getPrimary().getVisualBounds().getHeight() - 220;
        scroll.setPrefViewportHeight(Math.min(700, maxH));

        // ===== footer: Cancel / Confirm Participation (always visible) =====
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setGraphic(icon(EV_BACK, BLUE, 22));
        cancelBtn.setGraphicTextGap(10);
        cancelBtn.setPrefWidth(200);
        cancelBtn.setStyle("-fx-background-color: white; -fx-text-fill: " + BLUE + "; -fx-font-weight: bold; " +
                "-fx-font-size: 17px; -fx-border-color: " + BLUE + "; -fx-border-width: 2; -fx-border-radius: 14; " +
                "-fx-background-radius: 14; -fx-cursor: hand; -fx-padding: 13 20 13 20;");
        cancelBtn.setOnAction(ev -> dlg.close());

        Button confirmBtn = primaryButton("Confirm Participation", 17, 15, 24);
        confirmBtn.setGraphic(icon(EV_TICK, "white", 22));
        confirmBtn.setGraphicTextGap(10);
        confirmBtn.setPrefWidth(330);
        confirmBtn.setOnAction(ev -> {
            if (nameField.getText().trim().isEmpty() || contactField.getText().trim().isEmpty()) {
                errorLabel.setText("Please fill out your full name and contact number.");
                return;
            }
            if (!checkVolunteerApproved()) {
                errorLabel.setText("Your volunteer registration is not approved by the admin yet.");
                return;
            }
            String evId = e.length > 13 ? e[13] : "";
            int joinResult = EventStore.tryJoin(evId, username, nameField.getText().trim(), contactField.getText().trim());
            if (joinResult == EventStore.JOIN_FULL) {
                errorLabel.setText("Sorry, this event just became full.");
                refreshEventsFromDb();
                return;
            }
            if (joinResult != EventStore.JOIN_OK) {
                errorLabel.setText("Could not save your registration. The event may have been removed - please try again.");
                refreshEventsFromDb();
                return;
            }
            joinStamp++;
            joinedEvents.add(evId);
            eventRegStatus.put(evId, "Pending");
            addActivity("Event", "Joined: " + e[0], shortDate, "Pending");
            eventsJoinedLbl.setText(String.valueOf(joinedCount()));
            addNotification(ICON_CHECK, GREEN, GREEN_LIGHT,
                    "Your participation in " + e[0] + " is waiting for admin approval.");
            Button jb = eventJoinButtons.get(e[13]);
            if (jb != null) {
                jb.setText("Pending Approval");
                jb.setDisable(true);
            }
            showParticipationSuccess(dlg);
        });

        HBox footer = new HBox(16, errorLabel, spacer(), cancelBtn, confirmBtn);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(14, 34, 22, 34));
        Region footSep = new Region();
        footSep.setMinHeight(1);
        footSep.setPrefHeight(1);
        footSep.setMaxHeight(1);
        footSep.setStyle("-fx-background-color: #d3dcf0;");
        VBox footerBox = new VBox(footSep, footer);
        footerBox.setPadding(new Insets(0, 34, 0, 34));
        footer.setPadding(new Insets(16, 0, 22, 0));

        // ===== close (X) button =====
        Button closeBtn = new Button();
        closeBtn.setGraphic(icon(EV_CLOSE, NAVY_DEEP, 22));
        closeBtn.setMinSize(44, 44);
        closeBtn.setMaxSize(44, 44);
        closeBtn.setStyle("-fx-background-color: white; -fx-background-radius: 22; -fx-cursor: hand;");
        closeBtn.setOnAction(ev -> dlg.close());

        VBox cardContent = new VBox(scroll, footerBox);
        StackPane card = new StackPane(cardContent, closeBtn);
        StackPane.setAlignment(closeBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(closeBtn, new Insets(12, 14, 0, 0));
        card.setStyle("-fx-background-color: linear-gradient(to bottom right, #ffffff, #eef4ff); -fx-background-radius: 30;");
        card.setEffect(new DropShadow(40, Color.rgb(10, 25, 60, 0.40)));

        StackPane root = new StackPane(card);
        root.setPadding(new Insets(34));
        root.setStyle("-fx-background-color: transparent;");
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        dlg.setScene(scene);
        dlg.show();
    }

    private HBox detailLine(String iconPath, String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", 17));
        l.setTextFill(Color.web(NAVY_DEEP));
        StackPane ic = new StackPane(icon(iconPath, BLUE, 24));
        ic.setMinWidth(30);
        HBox row = new HBox(14, ic, l);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private Label partLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        l.setTextFill(Color.web(NAVY_DEEP));
        VBox.setMargin(l, new Insets(4, 0, 0, 0));
        return l;
    }

    /** "Successfully Participated!" pop-up shown on top of the event pop-up; Okay closes both. */
    private void showParticipationSuccess(Stage eventDialog) {
        Stage sd = new Stage();
        sd.initOwner(eventDialog);
        sd.initModality(Modality.APPLICATION_MODAL);
        sd.initStyle(StageStyle.TRANSPARENT);

        Circle disc = new Circle(44, Color.web("#3f9d4f"));
        StackPane badge = new StackPane(disc, icon(EV_TICK, "white", 56));

        Label head = new Label("Successfully Participated!");
        head.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        head.setTextFill(Color.web("#166534"));

        Label msg = new Label("Kindly wait for the admin's approval. We will notify you once your participation has been confirmed.");
        msg.setFont(Font.font("Segoe UI", 17));
        msg.setTextFill(Color.web("#14301c"));
        msg.setWrapText(true);
        msg.setTextAlignment(TextAlignment.CENTER);
        msg.setAlignment(Pos.CENTER);
        msg.setMinHeight(Region.USE_PREF_SIZE);

        Button okay = new Button("Okay");
        okay.setMaxWidth(Double.MAX_VALUE);
        okay.setStyle("-fx-background-color: #15803d; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; " +
                "-fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 14 20 14 20;");
        okay.setOnAction(ev -> {
            sd.close();
            eventDialog.close();
        });

        VBox box = new VBox(14, badge, head, msg, okay);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(28, 26, 26, 26));
        box.setMinWidth(560);
        box.setPrefWidth(560);
        box.setMaxWidth(560);
        box.setStyle("-fx-background-color: linear-gradient(to bottom, #e6fbea, #c8f1d1); -fx-background-radius: 22; " +
                "-fx-border-color: #22a24a; -fx-border-width: 3; -fx-border-radius: 22;");

        StackPane root = new StackPane(box);
        root.setStyle("-fx-background-color: transparent;");
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        sd.setScene(scene);

        // the green box simply sits on top of the event pop-up (which stays as it is), centered on the screen
        sd.setOnShown(ev -> {
            sd.setX(stage.getX() + (stage.getWidth() - sd.getWidth()) / 2);
            sd.setY(stage.getY() + (stage.getHeight() - sd.getHeight()) / 2);
        });
        sd.show();
    }

    /** Small green "Registration Submitted" box, centered on the screen. Runs onOkay after Okay is pressed. */
    private void showRegistrationSuccess(Runnable onOkay) {
        Stage sd = new Stage();
        sd.initOwner(stage);
        sd.initModality(Modality.APPLICATION_MODAL);
        sd.initStyle(StageStyle.TRANSPARENT);

        Circle disc = new Circle(30, Color.web("#3f9d4f"));
        StackPane badge = new StackPane(disc, icon(EV_TICK, "white", 38));

        Label head = new Label("Registration Submitted!");
        head.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        head.setTextFill(Color.web("#166534"));

        Label msg = new Label("Thank you for signing up. We will contact you soon.");
        msg.setFont(Font.font("Segoe UI", 14));
        msg.setTextFill(Color.web("#14301c"));
        msg.setWrapText(true);
        msg.setTextAlignment(TextAlignment.CENTER);
        msg.setAlignment(Pos.CENTER);
        msg.setMinHeight(Region.USE_PREF_SIZE);

        Button okay = new Button("Okay");
        okay.setMaxWidth(Double.MAX_VALUE);
        okay.setStyle("-fx-background-color: #15803d; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px; " +
                "-fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 11 20 11 20;");
        okay.setOnAction(ev -> {
            sd.close();
            if (onOkay != null) onOkay.run();
        });

        VBox box = new VBox(10, badge, head, msg, okay);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(22, 22, 20, 22));
        box.setMinWidth(380);
        box.setPrefWidth(380);
        box.setMaxWidth(380);
        box.setStyle("-fx-background-color: linear-gradient(to bottom, #e6fbea, #c8f1d1); -fx-background-radius: 20; " +
                "-fx-border-color: #22a24a; -fx-border-width: 3; -fx-border-radius: 20;");

        StackPane root = new StackPane(box);
        root.setStyle("-fx-background-color: transparent;");
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        sd.setScene(scene);
        sd.setOnShown(ev -> {
            sd.setX(stage.getX() + (stage.getWidth() - sd.getWidth()) / 2);
            sd.setY(stage.getY() + (stage.getHeight() - sd.getHeight()) / 2);
        });
        sd.show();
    }

    // ---------- My Activities page ----------

    private ScrollPane buildActivitiesPage() {
        VBox body = new VBox(20);
        body.setPadding(new Insets(28, 40, 28, 40));

        VBox titleCard = pageHeader("My Activities",
                "Your donations, volunteer registration, and joined events in one place.", new Region());

        // ----- starting rows (the user's saved donations) -----
        List<String[]> rows = new ArrayList<>();
        for (DonationStore.Donation d : DonationStore.byDonor(username)) {
            rows.add(new String[]{"Donation", d.type + " donation: " + d.item, d.date, d.status, d.id,
                    String.valueOf(d.amount)});
        }
        for (String[] r : EventStore.registrationsFor(username)) {
            rows.add(new String[]{"Event", "Joined: " + r[0], r[1], r[2],
                    String.format(Locale.ENGLISH, "EVT-%03d", ++evtTxnCounter), "0.0"});
        }
        String[] vol = VolunteerStore.latestFor(username);
        if (vol != null) {
            rows.add(new String[]{"Volunteer", "Volunteer registration submitted", vol[0], vol[1],
                    String.format(Locale.ENGLISH, "VOL-%03d", ++volTxnCounter), "0.0"});
        }
        sortNewestFirst(rows, 2, 0);
        activityRows.clear();
        activityRows.addAll(rows);

        // ----- 4 summary cards -----
        Label totalValue = new Label();
        Label eventsValue = new Label();
        Label volValue = new Label();
        Label pendingValue = new Label();
        HBox stats = new HBox(20,
                activityStatCard("Total Donations:", totalValue, ICON_MONEY, "#f59e0b", "#fef3c7"),
                activityStatCard("Events Joined:", eventsValue, ICON_CALENDAR, BLUE, BLUE_LIGHT),
                activityStatCard("Pending Requests:", pendingValue, EV_HOURGLASS, ORANGE, ORANGE_LIGHT));

        // ----- filter tabs: All / Donations / Volunteer / Events & Programs -----
        String[][] tabs = {{"All", "All"}, {"Donations", "Donation"}, {"Volunteer", "Volunteer"}, {"Events & Programs", "Event"}};
        String[] active = {"All"};
        HBox segment = new HBox(4);
        segment.setAlignment(Pos.CENTER_LEFT);
        segment.setPadding(new Insets(4));
        segment.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        segment.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius: 30; " +
                "-fx-border-color: #b9c9e8; -fx-border-width: 1.5; -fx-border-radius: 30;");
        List<Button> tabButtons = new ArrayList<>();
        Runnable[] applyView = new Runnable[1];
        for (String[] t : tabs) {
            Button tb = new Button(t[0]);
            tb.setUserData(t[1]);
            tb.setOnAction(e -> {
                active[0] = t[1];
                applyView[0].run();
            });
            tabButtons.add(tb);
            segment.getChildren().add(tb);
        }

        TextField search = new TextField();
        search.setPromptText("Search");
        styleInput(search);
        search.setPrefHeight(46);
        search.setStyle(search.getStyle() + "-fx-padding: 0 44 0 14;");
        search.setPrefWidth(280);
        search.setMaxWidth(280);
        Group searchIcon = icon(EV_SEARCH, NAVY_DEEP, 20);
        searchIcon.setMouseTransparent(true);
        StackPane searchBox = new StackPane(search, searchIcon);
        StackPane.setAlignment(searchIcon, Pos.CENTER_RIGHT);
        StackPane.setMargin(searchIcon, new Insets(0, 14, 0, 0));
        searchBox.setMaxWidth(280);

        HBox toolbar = new HBox(16, segment, spacer(), searchBox);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        // ----- the list -----
        GridPane headRow = activityGrid();
        String[] heads = {"Type", "Activity", "Date", "Status", "Transaction ID", "Action"};
        for (int i = 0; i < heads.length; i++) {
            Label h = new Label(heads[i]);
            h.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
            h.setTextFill(Color.web(NAVY));
            headRow.add(h, i, 0);
        }
        headRow.setPadding(new Insets(4, 16, 8, 16));

        VBox listBox = new VBox();
        Label none = new Label("No activities found.");
        none.setFont(Font.font("Segoe UI", 15));
        none.setTextFill(Color.web(NAVY));
        none.setPadding(new Insets(16, 16, 8, 16));

        applyView[0] = () -> {
            // the numbers on the cards
            double total = 0;
            int events = 0, vols = 0, pending = 0, donations = 0;
            for (String[] r : activityRows) {
                if ("Donation".equals(r[0])) {
                    donations++;
                    try {
                        total += Double.parseDouble(r[5]);
                    } catch (NumberFormatException ignored) {
                    }
                }
                if ("Event".equals(r[0])) events++;
                if ("Volunteer".equals(r[0])) vols++;
                if ("Pending".equalsIgnoreCase(r[3])) pending++;
            }
            if (donationsCountLbl != null) donationsCountLbl.setText(String.valueOf(donations));
            totalValue.setText("\u20B1" + String.format(Locale.ENGLISH, "%,.2f", total));
            eventsValue.setText(events + (events == 1 ? " Event" : " Events"));
            volValue.setText(vols + " Submitted");
            pendingValue.setText(pending + " Pending");

            // tabs look
            for (Button tb : tabButtons) {
                boolean on = tb.getUserData().equals(active[0]);
                tb.setStyle("-fx-background-radius: 26; -fx-cursor: hand; -fx-font-size: 14px; -fx-padding: 9 24 9 24; " +
                        (on ? "-fx-background-color: " + NAVY_DEEP + "; -fx-text-fill: white; -fx-font-weight: bold;"
                                : "-fx-background-color: transparent; -fx-text-fill: " + NAVY_DEEP + ";"));
            }

            // rows (filtered by tab + search)
            String q = search.getText() == null ? "" : search.getText().trim().toLowerCase(Locale.ENGLISH);
            listBox.getChildren().clear();
            int shown = 0;
            for (String[] r : activityRows) {
                boolean okTab = "All".equals(active[0]) || r[0].equals(active[0]);
                String hay = (r[0] + " " + r[1] + " " + r[2] + " " + r[3] + " " + r[4]).toLowerCase(Locale.ENGLISH);
                if (okTab && (q.isEmpty() || hay.contains(q))) {
                    listBox.getChildren().add(activityRowNode(r, shown % 2 == 1));
                    shown++;
                }
            }
            none.setVisible(shown == 0);
            none.setManaged(shown == 0);
        };
        search.textProperty().addListener((obs, o, n) -> applyView[0].run());
        refreshActivities = applyView[0];
        setActivityFilter = key -> {
            active[0] = key;
            search.clear();
            applyView[0].run();
        };
        applyView[0].run();

        VBox tableCard = glassCard(new VBox(14, toolbar, headRow, listBox, none));

        body.getChildren().addAll(titleCard, stats, tableCard);
        return wrapScroll(body);
    }

    /** Add a new activity on top and refresh the cards + list. */
    private void addActivity(String type, String text, String date, String status) {
        addActivity(type, text, date, status, "", 0);
    }

    private void addActivity(String type, String text, String date, String status, String txnId, double amount) {
        String txn = txnId;
        if (txn == null || txn.isEmpty()) {
            if ("Event".equals(type)) txn = String.format(Locale.ENGLISH, "EVT-%03d", ++evtTxnCounter);
            else if ("Volunteer".equals(type)) txn = String.format(Locale.ENGLISH, "VOL-%03d", ++volTxnCounter);
            else txn = "-";
        }
        activityRows.add(0, new String[]{type, text, date, status, txn, String.valueOf(amount)});
        if (refreshActivities != null) refreshActivities.run();
        if (refreshRecent != null) refreshRecent.run();
    }

    /** Same column widths for the header and every row, so they line up. */
    private GridPane activityGrid() {
        GridPane g = new GridPane();
        double[] widths = {12, 31, 14, 13, 16, 14};
        for (double w : widths) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(w);
            cc.setHgrow(Priority.ALWAYS);
            g.getColumnConstraints().add(cc);
        }
        RowConstraints rc = new RowConstraints();
        rc.setValignment(javafx.geometry.VPos.CENTER);
        g.getRowConstraints().add(rc);
        return g;
    }

    private Label activityPill(String text, String bg, String fg) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        l.setTextFill(Color.web(fg));
        l.setPadding(new Insets(4, 14, 4, 14));
        l.setStyle("-fx-background-color: " + bg + "; -fx-background-radius: 14;");
        return l;
    }

    private Label typePill(String type) {
        String bg = "Event".equals(type) ? "#1e7be0" : "Volunteer".equals(type) ? "#7c3aed" : "#22a65a";
        return activityPill(type, bg, "#ffffff");
    }

    private Label statusPill(String status) {
        String st = status == null ? "" : status.toLowerCase(Locale.ENGLISH);
        if (st.equals("pending")) return activityPill(status, ORANGE_LIGHT, ORANGE);
        if (st.equals("received") || st.equals("approved") || st.equals("confirmed") || st.equals("completed"))
            return activityPill(status, GREEN_LIGHT, GREEN);
        return activityPill(status, "#e5e7eb", NAVY);
    }

    private GridPane activityRowNode(String[] r, boolean alt) {
        GridPane row = activityGrid();
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setStyle("-fx-background-color: " + (alt ? "rgba(236,243,255,0.65)" : "rgba(255,255,255,0.65)") + "; " +
                "-fx-border-color: transparent transparent #d5def0 transparent;");

        Label activity = new Label(r[1]);
        activity.setFont(Font.font("Segoe UI", 14));
        activity.setTextFill(Color.web(NAVY_DEEP));
        Label date = new Label(r[2]);
        date.setFont(Font.font("Segoe UI", 14));
        date.setTextFill(Color.web(NAVY_DEEP));
        Label txn = new Label(r[4]);
        txn.setFont(Font.font("Segoe UI", 14));
        txn.setTextFill(Color.web(NAVY_DEEP));

        Button view = new Button("View Details");
        view.setStyle("-fx-background-color: white; -fx-text-fill: " + NAVY_DEEP + "; -fx-font-size: 13px; " +
                "-fx-border-color: " + NAVY_DEEP + "; -fx-border-width: 1.5; -fx-border-radius: 8; " +
                "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 6 16 6 16;");
        view.setOnAction(e -> showActivityDetails(r));

        row.add(typePill(r[0]), 0, 0);
        row.add(activity, 1, 0);
        row.add(date, 2, 0);
        row.add(statusPill(r[3]), 3, 0);
        row.add(txn, 4, 0);
        row.add(view, 5, 0);
        return row;
    }

    private HBox activityStatCard(String label, Label value, String iconPath, String iconColor, String iconBg) {
        Label l = new Label(label);
        l.setFont(Font.font("Segoe UI", 15));
        l.setTextFill(Color.web(NAVY));
        value.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        value.setTextFill(Color.web(NAVY_DEEP));

        StackPane ic = new StackPane(icon(iconPath, iconColor, 34));
        ic.setMinSize(62, 62);
        ic.setMaxSize(62, 62);
        ic.setStyle("-fx-background-color: " + iconBg + "; -fx-background-radius: 18;");

        HBox card = new HBox(12, new VBox(6, l, value), spacer(), ic);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20, 22, 20, 24));
        card.setMinWidth(0);
        card.setPrefWidth(10);
        card.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(card, Priority.ALWAYS);
        card.setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-background-radius: 20; " +
                "-fx-border-color: rgba(255,255,255,0.95); -fx-border-radius: 20; -fx-border-width: 1.5;");
        card.setEffect(new DropShadow(18, Color.rgb(20, 40, 90, 0.10)));
        return card;
    }

    /** "View Details" pop-up for one activity row. */
    private void showActivityDetails(String[] r) {
        Stage dlg = new Stage();
        dlg.initOwner(stage);
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.initStyle(StageStyle.TRANSPARENT);

        Label head = new Label("Activity Details");
        head.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        head.setTextFill(Color.web(NAVY_DEEP));

        Label what = new Label(r[1]);
        what.setFont(Font.font("Segoe UI", 15));
        what.setTextFill(Color.web(NAVY_DEEP));
        what.setWrapText(true);
        what.setMinHeight(Region.USE_PREF_SIZE);
        what.setMaxWidth(300);

        VBox rows = new VBox(14,
                detailPair("Type", typePill(r[0])),
                detailPair("Activity", what),
                detailPair("Date", plainValue(r[2])),
                detailPair("Status", statusPill(r[3])),
                detailPair("Transaction ID", plainValue(r[4])));
        double amt = 0;
        try {
            amt = Double.parseDouble(r[5]);
        } catch (NumberFormatException ignored) {
        }
        if ("Donation".equals(r[0]) && amt > 0) {
            rows.getChildren().add(detailPair("Amount", plainValue("\u20B1" + String.format(Locale.ENGLISH, "%,.2f", amt))));
        }

        Button close = primaryButton("Close", 16, 12, 34);
        close.setOnAction(e -> dlg.close());
        HBox closeRow = new HBox(close);
        closeRow.setAlignment(Pos.CENTER_RIGHT);

        VBox box = new VBox(18, head, rows, closeRow);
        box.setPadding(new Insets(28, 30, 26, 30));
        box.setMinWidth(500);
        box.setPrefWidth(500);
        box.setMaxWidth(500);
        box.setStyle("-fx-background-color: linear-gradient(to bottom right, #ffffff, #eef4ff); -fx-background-radius: 24;");
        box.setEffect(new DropShadow(36, Color.rgb(10, 25, 60, 0.38)));

        StackPane root = new StackPane(box);
        root.setPadding(new Insets(34));
        root.setStyle("-fx-background-color: transparent;");
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        dlg.setScene(scene);
        dlg.show();
    }

    private HBox detailPair(String key, Node value) {
        Label k = new Label(key);
        k.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        k.setTextFill(Color.web(NAVY));
        k.setMinWidth(130);
        HBox row = new HBox(12, k, value);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private Label plainValue(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", 15));
        l.setTextFill(Color.web(NAVY_DEEP));
        return l;
    }

    // ---------- Volunteer registration page ----------

    private ScrollPane buildRegisterPage() {
        // ----- background with soft decorative shapes -----
        Pane deco = new Pane();
        deco.setMouseTransparent(true);
        addBlob(deco, 0.04, 0.06, 430, 28, Color.rgb(255, 255, 255, 0.22));
        addBlob(deco, 0.80, 0.00, 470, -24, Color.rgb(255, 255, 255, 0.20));
        addBlob(deco, 0.68, 0.58, 540, 35, Color.rgb(255, 255, 255, 0.16));
        addBlob(deco, 0.00, 0.66, 390, -18, Color.rgb(255, 255, 255, 0.18));
        addBlob(deco, 0.30, 0.82, 300, 40, Color.rgb(120, 160, 235, 0.12));
        addBlob(deco, 0.52, -0.08, 300, -35, Color.rgb(120, 160, 235, 0.12));
        Rectangle decoClip = new Rectangle();
        decoClip.widthProperty().bind(deco.widthProperty());
        decoClip.heightProperty().bind(deco.heightProperty());
        deco.setClip(decoClip);

        // ----- header of the card -----
        StackPane heartBadge = new StackPane(icon(ICON_HEART, BLUE, 38));
        heartBadge.setMinSize(70, 70);
        heartBadge.setMaxSize(70, 70);
        heartBadge.setStyle("-fx-background-color: " + BLUE_LIGHT + "; -fx-background-radius: 35;");

        Label title = new Label("Volunteer Registration");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        title.setTextFill(Color.web(NAVY_DEEP));
        Label sub = new Label("Secure your spot for our upcoming community initiatives. Provide your details below.");
        sub.setFont(Font.font("Segoe UI", 15));
        sub.setTextFill(Color.web(NAVY));
        sub.setWrapText(true);
        sub.setMinHeight(Region.USE_PREF_SIZE);
        sub.setMaxWidth(600);

        Button backBtn = secondaryButton("\u2190 Back to Opportunities");
        backBtn.setOnAction(e -> showPage("volunteers"));

        HBox header = new HBox(18, heartBadge, new VBox(6, title, sub), spacer(), backBtn);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(26, 34, 24, 34));
        header.setStyle("-fx-background-color: linear-gradient(to right, #dce8ff, #f1f6ff); -fx-background-radius: 28 28 0 0;");

        // ----- left column: personal details + impact pillars -----
        TextField fullName = new TextField();
        fullName.setPromptText("Full Name");
        StackPane nameBox = iconField(ICON_PERSON, fullName);
        TextField contact = new TextField();
        contact.setPromptText("Contact Number");
        StackPane contactBox = iconField(EV_PHONE, contact);
        TextField address = new TextField();
        address.setPromptText("Home Address");
        StackPane addressBox = iconField(EV_PIN, address);

        VBox pillars = new VBox(14);
        Label pillarsTitle = new Label("VoNation Impact Pillars: Building Stronger Communities Together");
        pillarsTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
        pillarsTitle.setTextFill(Color.web(NAVY_DEEP));
        pillarsTitle.setWrapText(true);
        pillarsTitle.setTextAlignment(TextAlignment.CENTER);
        pillarsTitle.setAlignment(Pos.CENTER);
        pillarsTitle.setMaxWidth(Double.MAX_VALUE);
        pillarsTitle.setMinHeight(Region.USE_PREF_SIZE);
        pillars.getChildren().addAll(pillarsTitle,
                pillarRow(EV_STAR, "Pillar 1: Community Empowerment.",
                        "We provide tools and resources for local leaders to create sustainable change."),
                pillarRow(ICON_SETTINGS, "Pillar 2: Skill Development.",
                        "Volunteers gain valuable experience and new skills through diverse programs."),
                pillarRow(ICON_HEART, "Pillar 3: Lasting Impact.",
                        "Our initiatives focus on measurable, long-term benefits for the community."),
                pillarRow(ICON_PEOPLE, "Pillar 4: Inclusive Collaboration.",
                        "We connect individuals and organizations to work towards common goals."));
        pillars.setPadding(new Insets(18, 20, 20, 20));
        pillars.setStyle("-fx-background-color: linear-gradient(to bottom, #eaf2ff, #d9e6fb); -fx-background-radius: 16; " +
                "-fx-border-color: #9db9ee; -fx-border-width: 2; -fx-border-radius: 16;");

        VBox left = new VBox(10,
                regLabel("Full Name"), nameBox,
                regLabel("Contact Number"), contactBox,
                regLabel("Home Address"), addressBox);
        VBox.setMargin(pillars, new Insets(10, 0, 0, 0));
        left.getChildren().add(pillars);
        left.setMinWidth(380);
        left.setPrefWidth(470);
        HBox.setHgrow(left, Priority.ALWAYS);

        // ----- right column: preferences & skills -----
        Label prefTitle = new Label("Preferences & Skills");
        prefTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        prefTitle.setTextFill(Color.web(NAVY_DEEP));

        ComboBox<String> role = new ComboBox<>(FXCollections.observableArrayList(
                "Team Lead", "Logistics Support", "Mentor", "General Volunteer"));
        role.setPromptText("Select a role");
        role.setMaxWidth(Double.MAX_VALUE);
        styleInput(role);

        // the volunteer types their own skills
        TextField skillsField = new TextField();
        skillsField.setPromptText("Type your skills (e.g. First Aid, Driving, Photography)");
        styleInput(skillsField);
        skillsField.setMaxWidth(Double.MAX_VALUE);

        TextArea notes = new TextArea();
        notes.setPrefRowCount(3);
        notes.setWrapText(true);
        notes.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #d1d5db; " +
                "-fx-control-inner-background: #f9fafb; -fx-font-size: 13px;");

        CheckBox agree = new CheckBox("I agree to receive notifications and cooperate according to event guidelines.");
        agree.setWrapText(true);
        styleChoice(agree);
        agree.setMinHeight(Region.USE_PREF_SIZE);

        errorLabel = new Label(" ");
        errorLabel.setFont(Font.font("Segoe UI", 13));
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(Double.MAX_VALUE);
        errorLabel.setAlignment(Pos.CENTER);
        errorLabel.setTextAlignment(TextAlignment.CENTER);

        Button submitBtn = primaryButton("Submit Registration", 17, 15, 24);
        submitBtn.setMaxWidth(Double.MAX_VALUE);
        submitBtn.setOnAction(e -> {
            String nm = fullName.getText().trim();
            String ct = contact.getText().trim();
            if (nm.isEmpty() || ct.isEmpty()) {
                errorLabel.setTextFill(Color.web("#9b2c2c"));
                errorLabel.setText("Please fill out your full name and contact number.");
                return;
            }
            if (!agree.isSelected()) {
                errorLabel.setTextFill(Color.web("#9b2c2c"));
                errorLabel.setText("Please agree to the Privacy Policy to continue.");
                return;
            }
            errorLabel.setText(" ");
            if (volunteerRegistered) {
                infoAlert("You are already registered as a volunteer.");
                return;
            }
            if (!VolunteerStore.add(username, nm, ct, address.getText().trim(),
                    role.getValue() == null ? "General Volunteer" : role.getValue(),
                    "", skillsField.getText().trim(), notes.getText().trim())) {
                errorLabel.setTextFill(Color.web("#9b2c2c"));
                errorLabel.setText("Could not save your registration. Please try again.");
                return;
            }
            volunteerName = nm;
            volunteerContact = ct;
            markRegistered();
            addActivity("Volunteer", "Volunteer registration submitted",
                    LocalDate.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)), "Pending");
            addNotification(ICON_CHECK, GREEN, GREEN_LIGHT, "Volunteer registration submitted. We will contact you soon.");
            // what the volunteer typed stays on screen until they press Okay, then the form is cleared
            showRegistrationSuccess(() -> {
                fullName.clear();
                contact.clear();
                address.clear();
                role.getSelectionModel().clearSelection();
                skillsField.clear();
                notes.clear();
                agree.setSelected(false);
            });
        });

        VBox right = new VBox(10, prefTitle,
                regLabel("Preferred Event Role"), role,
                regLabel("Specialized Skills"), skillsField,
                regLabel("Additional Notes"), notes,
                regLabel("Privacy Policy"), agree,
                submitBtn, errorLabel);
        right.setMinWidth(380);
        right.setPrefWidth(520);
        HBox.setHgrow(right, Priority.ALWAYS);

        HBox columns = new HBox(36, left, right);
        columns.setAlignment(Pos.TOP_LEFT);
        columns.setPadding(new Insets(26, 34, 34, 34));

        VBox card = new VBox(header, columns);
        card.setMaxWidth(1100);
        card.setStyle("-fx-background-color: rgba(255,255,255,0.92); -fx-background-radius: 28; " +
                "-fx-border-color: rgba(255,255,255,0.95); -fx-border-radius: 28; -fx-border-width: 1.5;");
        card.setEffect(new DropShadow(30, Color.rgb(20, 40, 90, 0.16)));

        VBox body = new VBox(card);
        body.setAlignment(Pos.TOP_CENTER);
        body.setPadding(new Insets(30, 40, 40, 40));

        StackPane root = new StackPane(deco, body);
        ScrollPane sp = wrapScroll(root);
        // keep the decorated background covering the whole screen, even when the form is short
        sp.viewportBoundsProperty().addListener((obs, o, n) -> root.setMinHeight(n.getHeight()));
        return sp;
    }

    /** A soft rounded square used as decoration; position follows the size of the page. */
    private void addBlob(Pane layer, double fx, double fy, double size, double rotation, Color fill) {
        Rectangle r = new Rectangle(size, size);
        r.setArcWidth(size * 0.34);
        r.setArcHeight(size * 0.34);
        r.setFill(fill);
        r.setRotate(rotation);
        r.layoutXProperty().bind(layer.widthProperty().multiply(fx));
        r.layoutYProperty().bind(layer.heightProperty().multiply(fy));
        layer.getChildren().add(r);
    }

    private Label regLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        l.setTextFill(Color.web(NAVY_DEEP));
        VBox.setMargin(l, new Insets(4, 0, 0, 0));
        return l;
    }

    private void styleChoice(ButtonBase b) {
        b.setStyle("-fx-font-size: 14px; -fx-text-fill: " + NAVY_DEEP + "; -fx-cursor: hand; " +
                "-fx-accent: " + BLUE + "; -fx-mark-color: " + BLUE + ";");
    }

    /** One "Pillar" line: round icon on the left, bold title and a short description. */
    private HBox pillarRow(String iconPath, String title, String desc) {
        StackPane ic = new StackPane(icon(iconPath, BLUE, 26));
        ic.setMinSize(46, 46);
        ic.setMaxSize(46, 46);
        ic.setStyle("-fx-background-color: white; -fx-background-radius: 23; -fx-border-color: #b7cdf3; -fx-border-radius: 23;");

        Label t = new Label(title);
        t.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        t.setTextFill(Color.web(NAVY_DEEP));
        t.setWrapText(true);
        t.setMinHeight(Region.USE_PREF_SIZE);
        Label d = new Label(desc);
        d.setFont(Font.font("Segoe UI", 13));
        d.setTextFill(Color.web(NAVY));
        d.setWrapText(true);
        d.setMinHeight(Region.USE_PREF_SIZE);
        VBox text = new VBox(2, t, d);
        text.setMinWidth(0);
        HBox.setHgrow(text, Priority.ALWAYS);

        HBox row = new HBox(14, ic, text);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private Label formLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        l.setTextFill(Color.web(NAVY));
        return l;
    }

    private void styleInput(Control c) {
        c.setPrefHeight(44);
        c.setStyle("-fx-background-color: #f9fafb; -fx-background-radius: 10; " +
                "-fx-border-color: #d1d5db; -fx-border-radius: 10; -fx-font-size: 13px;");
        if (c instanceof TextField) {
            c.setStyle(c.getStyle() + "-fx-padding: 0 12 0 12;");
        }
    }

    // ---------- My Profile page ----------

    private static final String ICON_MAIL = "M20 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z";

    private String profileName, profileEmail = "", profilePhone = "", profileAddress = "";
    private final boolean[] notifPrefs = {true, true, true, false};
    private StackPane profileAvatar;
    private Label profileNameLbl, profileEmailLbl;

    private ScrollPane buildSettingsPage() {
        if (profileName == null) {
            String[] acc = lookupAccount();
            profileName = (acc != null && !acc[0].isEmpty()) ? acc[0] : username;
            profileEmail = username.contains("@") ? username : (acc != null ? acc[1] : "");
            String[] saved = UserStore.profile(username); // contact number and address saved in PostgreSQL
            if (saved != null) {
                profilePhone = saved[0];
                profileAddress = saved[1];
            }
            if (acc != null && !acc[2].isEmpty()) {
                try {
                    memberSince = java.time.LocalDateTime.parse(acc[2])
                            .format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH));
                } catch (Exception ignored) {
                    // keep the default
                }
            }
        }

        VBox body = new VBox(22);
        body.setPadding(new Insets(28, 40, 28, 40));

        Label title = new Label("My Profile");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        title.setTextFill(Color.web(NAVY_DEEP));

        // ================= left card =================
        profileAvatar = new StackPane();
        profileAvatar.setMinSize(150, 150);
        profileAvatar.setPrefSize(150, 150);
        profileAvatar.setMaxSize(150, 150);
        fillProfileAvatar();

        // camera badge on the photo (click = change photo)
        javafx.scene.shape.SVGPath camPath = new javafx.scene.shape.SVGPath();
        camPath.setContent("M9 2L7.17 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2h-3.17L15 2H9z " +
                "m3 15c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5z");
        camPath.setFillRule(javafx.scene.shape.FillRule.EVEN_ODD);
        camPath.setFill(Color.WHITE);
        camPath.setScaleX(0.62);
        camPath.setScaleY(0.62);
        StackPane camBadge = new StackPane(new Circle(15, Color.web(BLUE)), new Group(camPath));
        camBadge.setMaxSize(30, 30);
        camBadge.setStyle("-fx-cursor: hand;");
        camBadge.setOnMouseClicked(e -> choosePhoto());
        StackPane photoBox = new StackPane(profileAvatar, camBadge);
        photoBox.setMaxSize(150, 150);
        StackPane.setAlignment(camBadge, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(camBadge, new Insets(0, 8, 8, 0));

        profileNameLbl = new Label();
        profileNameLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        profileNameLbl.setTextFill(Color.web(NAVY_DEEP));
        profileNameLbl.setWrapText(true);
        profileNameLbl.setMinHeight(Region.USE_PREF_SIZE);
        profileNameLbl.setTextAlignment(TextAlignment.CENTER);
        profileNameLbl.setAlignment(Pos.CENTER);

        profileBadgeLbl = new Label();
        profileBadgeLbl.setStyle("-fx-background-color: " + GREEN_LIGHT + "; -fx-text-fill: " + GREEN + "; " +
                "-fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 4 12 4 12;");

        profileEmailLbl = new Label();
        profileEmailLbl.setFont(Font.font("Segoe UI", 13));
        profileEmailLbl.setTextFill(Color.web(TEXT_MUTED));
        profileEmailLbl.setWrapText(true);
        profileEmailLbl.setMinHeight(Region.USE_PREF_SIZE);
        profileEmailLbl.setTextAlignment(TextAlignment.CENTER);
        profileEmailLbl.setAlignment(Pos.CENTER);

        statDonationsLbl = new Label("0");
        statEventsLbl = new Label("0");
        statHoursLbl = new Label("0");
        HBox statsBox = new HBox(
                profileStat(ICON_HEART, statDonationsLbl, "Donations"), statDivider(),
                profileStat(ICON_CALENDAR, statEventsLbl, "Events Joined"), statDivider(),
                profileStat(ICON_CLOCK, statHoursLbl, "Volunteer Hrs"));
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(12, 6, 12, 6));
        statsBox.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius: 14; " +
                "-fx-border-color: #dbe4f5; -fx-border-radius: 14;");

        Button editBtn = primaryButton("Edit Profile", 14, 12, 24);
        javafx.scene.shape.SVGPath pencil = new javafx.scene.shape.SVGPath();
        pencil.setContent("M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z");
        pencil.setFill(Color.WHITE);
        pencil.setScaleX(0.62);
        pencil.setScaleY(0.62);
        editBtn.setGraphic(new Group(pencil));
        editBtn.setGraphicTextGap(6);
        editBtn.setMaxWidth(Double.MAX_VALUE);
        editBtn.setOnAction(e -> showEditProfileDialog());

        VBox leftInner = new VBox(10, sectionTitle("Profile Summary", ICON_PERSON), new Separator(),
                photoBox, profileNameLbl, profileBadgeLbl, profileEmailLbl, statsBox, editBtn);
        leftInner.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(statsBox, new Insets(6, 0, 4, 0));
        VBox leftCard = glassCard(leftInner);
        leftCard.setPadding(new Insets(26, 24, 24, 24));
        leftCard.setMinWidth(0);
        leftCard.setPrefWidth(340);
        leftCard.setMaxHeight(Double.MAX_VALUE);
        HBox.setHgrow(leftCard, Priority.ALWAYS);

        // ================= middle: Personal Information =================
        profileInfoName = new Label();
        profileInfoPhone = new Label();
        profileInfoAddress = new Label();
        profileInfoSince = new Label();
        Label prefLoc = new Label("Balayan Central Hub");
        profileAddNumberBtn = new Button("+ Add number");
        profileAddNumberBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_MUTED + "; " +
                "-fx-font-size: 11px; -fx-cursor: hand; -fx-padding: 0;");
        profileAddNumberBtn.setOnAction(e -> showEditProfileDialog());
        VBox infoList = new VBox(0,
                infoItem(ICON_PERSON, "Full Name", profileInfoName, null), softSeparator(),
                infoItem(EV_PHONE, "Contact Number", profileInfoPhone, profileAddNumberBtn), softSeparator(),
                infoItem(ICON_HOME, "Address", profileInfoAddress, null), softSeparator(),
                infoItem(ICON_CALENDAR, "Member Since", profileInfoSince, null), softSeparator(),
                infoItem(EV_PIN, "Preferred Location", prefLoc, null));
        VBox infoCard = glassCard(new VBox(10, sectionTitle("Personal Information", EV_INFO), new Separator(), infoList));
        infoCard.setPadding(new Insets(22, 26, 24, 26));
        infoCard.setMinWidth(0);
        infoCard.setPrefWidth(340);
        infoCard.setMaxHeight(Double.MAX_VALUE);
        HBox.setHgrow(infoCard, Priority.ALWAYS);

        // ================= right: Account Settings =================
        VBox rows = new VBox(10, sectionTitle("Account Settings", ICON_SETTINGS), new Separator(),
                accountRow("Change Email", ICON_MAIL, NAVY, true, this::showChangeEmailDialog),
                accountRow("Change Password", ICON_LOCK, NAVY, true, this::showChangePasswordDialog),
                accountRow("Notification Settings", ICON_BELL, NAVY, true, this::showNotificationDialog),
                accountRow("Logout", ICON_LOGOUT, "#dc2626", true, this::confirmLogout));
        VBox accCard = glassCard(rows);
        accCard.setPadding(new Insets(22, 26, 24, 26));
        accCard.setMinWidth(0);
        accCard.setPrefWidth(340);
        accCard.setMaxHeight(Double.MAX_VALUE);
        HBox.setHgrow(accCard, Priority.ALWAYS);

        HBox columns = new HBox(22, leftCard, infoCard, accCard);
        columns.setAlignment(Pos.TOP_LEFT);

        updateProfileLabels();
        body.getChildren().addAll(title, columns);
        return wrapScroll(body);
    }

    private String memberSince = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH));
    private Button profileAddNumberBtn;
    private Label profileBadgeLbl, statDonationsLbl, statEventsLbl, statHoursLbl;
    private Label profileInfoName, profileInfoPhone, profileInfoAddress, profileInfoSince;

    /** This account's saved details {full name, email, created at}, read from PostgreSQL. */
    private String[] lookupAccount() {
        return UserStore.lookup(username);
    }

    private VBox profileStat(String iconPath, Label value, String caption) {
        value.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
        value.setTextFill(Color.web(NAVY_DEEP));
        Label cap = new Label(caption);
        cap.setFont(Font.font("Segoe UI", 10));
        cap.setTextFill(Color.web(TEXT_MUTED));
        VBox v = new VBox(2, icon(iconPath, NAVY, 16), value, cap);
        v.setAlignment(Pos.CENTER);
        HBox.setHgrow(v, Priority.ALWAYS);
        v.setMaxWidth(Double.MAX_VALUE);
        v.setPrefWidth(10);
        return v;
    }

    private Separator statDivider() {
        Separator s = new Separator(javafx.geometry.Orientation.VERTICAL);
        s.setOpacity(0.5);
        s.setPrefHeight(38);
        return s;
    }

    private HBox infoCell(String iconPath, String label, Label value) {
        Label cap = new Label(label);
        cap.setFont(Font.font("Segoe UI", 11));
        cap.setTextFill(Color.web(TEXT_MUTED));
        value.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        value.setTextFill(Color.web(NAVY_DEEP));
        value.setWrapText(true);
        value.setMinHeight(Region.USE_PREF_SIZE);
        HBox cell = new HBox(12, icon(iconPath, NAVY, 18), new VBox(2, cap, value));
        cell.setAlignment(Pos.TOP_LEFT);
        return cell;
    }

    /** One row of the Personal Information list: small icon + caption, bold value, optional extra node. */
    private VBox infoItem(String iconPath, String caption, Label value, Node extra) {
        Label cap = new Label(caption);
        cap.setFont(Font.font("Segoe UI", 11));
        cap.setTextFill(Color.web(TEXT_MUTED));
        cap.setGraphic(icon(iconPath, TEXT_MUTED, 14));
        cap.setGraphicTextGap(8);
        value.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        value.setTextFill(Color.web(NAVY_DEEP));
        value.setWrapText(true);
        value.setMinHeight(Region.USE_PREF_SIZE);
        VBox v = new VBox(3, cap, value);
        if (extra != null) v.getChildren().add(extra);
        v.setPadding(new Insets(12, 0, 12, 0));
        return v;
    }

    private void updateProfileLabels() {
        if (profileNameLbl == null) return;
        profileNameLbl.setText(profileName);
        profileEmailLbl.setText(profileEmail);
        boolean showEmail = !profileEmail.isEmpty();
        profileEmailLbl.setVisible(showEmail);
        profileEmailLbl.setManaged(showEmail);
        profileBadgeLbl.setText(volunteerApproved ? "\u2713  Verified Volunteer \u2022 Balayan"
                : volunteerRegistered ? "Volunteer (Pending Approval) \u2022 Balayan" : "Member \u2022 Balayan");
        profileBadgeLbl.setStyle(profileBadgeLbl.getStyle().replaceAll("-fx-background-color: [^;]+; -fx-text-fill: [^;]+;",
                volunteerApproved ? "-fx-background-color: " + GREEN_LIGHT + "; -fx-text-fill: " + GREEN + ";"
                        : "-fx-background-color: " + BLUE_LIGHT + "; -fx-text-fill: " + BLUE + ";"));
        statDonationsLbl.setText(String.valueOf(DonationStore.byDonor(username).size()));
        statEventsLbl.setText(String.valueOf(joinedCount()));
        statHoursLbl.setText("0");
        String phone = !profilePhone.isEmpty() ? profilePhone : volunteerContact;
        profileInfoName.setText(profileName);
        profileInfoPhone.setText(phone.isEmpty() ? "Not set" : phone);
        profileInfoAddress.setText(profileAddress.isEmpty() ? "Not set" : profileAddress);
        profileInfoSince.setText(memberSince);
        profileAddNumberBtn.setVisible(phone.isEmpty());
        profileAddNumberBtn.setManaged(phone.isEmpty());
    }

    private void fillProfileAvatar() {
        if (profileAvatar == null) return;
        Circle ring = new Circle(73, Color.web("#dbe7ff"));
        ring.setStroke(Color.web("#c3d6f7"));
        ring.setStrokeWidth(5);
        if (profileImage != null) {
            profileAvatar.getChildren().setAll(ring, photoNode(profileImage, 138));
        } else {
            profileAvatar.getChildren().setAll(ring, icon(ICON_PERSON, "#9aa7c0", 84));
        }
    }

    /** One clickable row in the Account Settings card. */
    private HBox accountRow(String text, String iconPath, String color, boolean chevron, Runnable action) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        l.setTextFill(Color.web(color));
        l.setGraphic(icon(iconPath, color, 20));
        l.setGraphicTextGap(14);
        HBox row = new HBox(10, l, spacer());
        if (chevron) row.getChildren().add(icon(ICON_CHEVRON, TEXT_MUTED, 20));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(13, 18, 13, 18));
        String base = "-fx-background-color: rgba(255,255,255,0.80); -fx-background-radius: 14; " +
                "-fx-border-color: #e1e8f5; -fx-border-radius: 14; -fx-cursor: hand;";
        row.setStyle(base);
        row.setOnMouseEntered(e -> row.setStyle(base.replace("0.80", "1.0")));
        row.setOnMouseExited(e -> row.setStyle(base));
        row.setOnMouseClicked(e -> action.run());
        return row;
    }

    /** Small modal window used by the profile actions. */
    private Stage openDialog(String title, Node content) {
        Stage d = new Stage();
        d.initOwner(stage);
        d.initModality(Modality.APPLICATION_MODAL);
        d.setTitle(title);
        VBox root = new VBox(content);
        root.setPadding(new Insets(24));
        root.setPrefWidth(440);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #eaf1ff, #bcd5f5); " +
                "-fx-font-family: 'Segoe UI';");
        d.setScene(new Scene(root));
        d.setResizable(false);
        return d;
    }

    private void showEditProfileDialog() {
        TextField nameF = new TextField(profileName);
        styleInput(nameF);
        TextField phoneF = new TextField(profilePhone);
        phoneF.setPromptText("Contact number");
        styleInput(phoneF);
        TextField addressF = new TextField(profileAddress);
        addressF.setPromptText("Address");
        styleInput(addressF);

        Button changePhoto = secondaryButton("Change Photo");
        changePhoto.setOnAction(e -> choosePhoto());
        Button removePhoto = new Button("Remove");
        removePhoto.setStyle("-fx-background-color: rgba(27,42,74,0.10); -fx-text-fill: " + NAVY + "; " +
                "-fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 12; -fx-cursor: hand; " +
                "-fx-padding: 11 22 11 22;");
        removePhoto.setOnAction(e -> {
            profileImage = null;
            refreshAvatars();
            addNotification(ICON_PERSON, BLUE, BLUE_LIGHT, "Your profile photo was removed.");
        });

        Label msg = messageLabel();
        Button save = primaryButton("Save Changes");
        VBox box = new VBox(10, sectionTitle("Edit Profile", ICON_PERSON),
                formLabel("Profile Photo"), new HBox(10, changePhoto, removePhoto),
                formLabel("Full Name"), nameF,
                formLabel("Contact Number"), phoneF,
                formLabel("Address"), addressF,
                msg, save);
        Stage d = openDialog("Edit Profile", box);
        save.setOnAction(e -> {
            if (nameF.getText().trim().isEmpty()) {
                showMessage(msg, "Full name can't be empty.", false);
                return;
            }
            String newPhone = phoneF.getText().trim().isEmpty() ? "" : UserStore.normalizePhone(phoneF.getText());
            if (!newPhone.isEmpty() && !UserStore.isValidPhone(newPhone)) {
                showMessage(msg, "Enter a valid mobile number (09XXXXXXXXX or +639XXXXXXXXX).", false);
                return;
            }
            if (!UserStore.updateProfile(username, nameF.getText().trim(), newPhone, addressF.getText().trim())) {
                showMessage(msg, "Could not save your profile. Please try again.", false);
                return;
            }
            profileName = nameF.getText().trim();
            profilePhone = newPhone;
            profileAddress = addressF.getText().trim();
            greeting.setText(profileName);
            if (welcomeLbl != null) welcomeLbl.setText("Welcome back, " + profileName + "!");
            updateProfileLabels();
            addNotification(ICON_PERSON, BLUE, BLUE_LIGHT, "Your profile was updated.");
            d.close();
        });
        d.show();
    }

    private void showChangeEmailDialog() {
        TextField emailF = new TextField(profileEmail);
        emailF.setPromptText("name@example.com");
        styleInput(emailF);
        Label msg = messageLabel();
        Button save = primaryButton("Save Email");
        VBox box = new VBox(10, sectionTitle("Change Email", ICON_MAIL),
                formLabel("New Email"), emailF, msg, save);
        Stage d = openDialog("Change Email", box);
        save.setOnAction(e -> {
            String email = emailF.getText().trim();
            if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                showMessage(msg, "Please enter a valid email address.", false);
                return;
            }
            profileEmail = email;
            updateProfileLabels();
            addNotification(ICON_PERSON, BLUE, BLUE_LIGHT, "Your email was updated.");
            d.close();
        });
        d.show();
    }

    private void showChangePasswordDialog() {
        PasswordField currentF = new PasswordField();
        currentF.setPromptText("Current password");
        styleInput(currentF);
        PasswordField newF = new PasswordField();
        newF.setPromptText("New password (at least 6 characters)");
        styleInput(newF);
        PasswordField confirmF = new PasswordField();
        confirmF.setPromptText("Confirm new password");
        styleInput(confirmF);
        Label msg = messageLabel();
        Button save = primaryButton("Update Password");
        VBox box = new VBox(10, sectionTitle("Change Password", ICON_LOCK),
                formLabel("Current Password"), currentF,
                formLabel("New Password"), newF,
                formLabel("Confirm New Password"), confirmF,
                msg, save);
        Stage d = openDialog("Change Password", box);
        save.setOnAction(e -> {
            if (currentF.getText().isEmpty() || newF.getText().isEmpty() || confirmF.getText().isEmpty()) {
                showMessage(msg, "Please fill out all password fields.", false);
            } else if (newF.getText().length() < 6) {
                showMessage(msg, "New password must be at least 6 characters.", false);
            } else if (!newF.getText().equals(confirmF.getText())) {
                showMessage(msg, "New password and confirmation don't match.", false);
            } else if (newF.getText().equals(currentF.getText())) {
                showMessage(msg, "New password must be different from the current one.", false);
            } else {
                try {
                    if (!UserStore.changePassword(username, currentF.getText(), newF.getText())) {
                        showMessage(msg, "Your current password is incorrect.", false);
                        return;
                    }
                } catch (java.io.IOException ex) {
                    showMessage(msg, "Could not change your password. Please try again.", false);
                    return;
                }
                addNotification(ICON_LOCK, ORANGE, ORANGE_LIGHT, "Your password was changed.");
                d.close();
            }
        });
        d.show();
    }

    private void showNotificationDialog() {
        CheckBox cbEvents = notifBox("Event reminders", notifPrefs[0]);
        CheckBox cbReceipts = notifBox("Donation receipts", notifPrefs[1]);
        CheckBox cbAnnounce = notifBox("Announcements from the admin", notifPrefs[2]);
        CheckBox cbSummary = notifBox("Monthly activity summary", notifPrefs[3]);
        Button save = primaryButton("Save Preferences");
        VBox box = new VBox(12, sectionTitle("Notification Settings", ICON_BELL),
                cbEvents, cbReceipts, cbAnnounce, cbSummary, save);
        Stage d = openDialog("Notification Settings", box);
        save.setOnAction(e -> {
            notifPrefs[0] = cbEvents.isSelected();
            notifPrefs[1] = cbReceipts.isSelected();
            notifPrefs[2] = cbAnnounce.isSelected();
            notifPrefs[3] = cbSummary.isSelected();
            addNotification(ICON_BELL, ORANGE, ORANGE_LIGHT, "Notification preferences saved.");
            d.close();
        });
        d.show();
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

    private Label messageLabel() {
        Label l = new Label(" ");
        l.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        l.setWrapText(true);
        return l;
    }

    private void showMessage(Label label, String text, boolean success) {
        label.setTextFill(Color.web(success ? GREEN : "#b91c1c"));
        label.setText(text);
    }

    // ---------- Helpers ----------

    /** Translucent "glass" card, same look as the login card. */
    private VBox glassCard(Node content) {
        VBox card = new VBox(content);
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setStyle(GLASS);
        card.setEffect(new DropShadow(24, Color.rgb(20, 40, 90, 0.14)));
        return card;
    }

    private VBox pageHeader(String titleText, String subText, Node action) {
        Label title = new Label(titleText);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(Color.web(NAVY_DEEP));
        Label sub = new Label(subText);
        sub.setFont(Font.font("Segoe UI", 13));
        sub.setTextFill(Color.web(TEXT_MUTED));
        VBox textBox = new VBox(3, title, sub);

        HBox row = new HBox(12, textBox, spacer(), action);
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

    private Button primaryButton(String text, int fontPx, int padV, int padH) {
        return primaryButton(text, fontPx, padV, padH, 14);
    }

    private Button primaryButton(String text, int fontPx, int padV, int padH, int radius) {
        Button b = new Button(text);
        String base = "-fx-background-color: " + BLUE + "; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: " + fontPx + "px; -fx-background-radius: " + radius + "; -fx-cursor: hand; " +
                "-fx-padding: " + padV + " " + padH + " " + padV + " " + padH + ";";
        String hover = base.replace(BLUE, BLUE_HOVER);
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(base));
        return b;
    }

    private Button secondaryButton(String text) {
        Button b = new Button(text);
        String base = "-fx-background-color: " + NAVY + "; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-font-size: 13px; -fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 11 22 11 22;";
        String hover = base.replace(NAVY, NAVY_DEEP);
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

    /** Vector icon sized to `size` px. Wrapped in a Group so layout uses the scaled size. */
    private Group icon(String path, String color, double size) {
        SVGPath p = new SVGPath();
        p.setContent(path);
        p.setFill(Color.web(color));
        double s = size / 24.0;
        p.setScaleX(s);
        p.setScaleY(s);
        return new Group(p);
    }

    /** Outline (stroked) icon with round caps, used for the sidebar toggle. */
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

    private void setIconColor(Label label, String color) {
        if (label.getGraphic() instanceof Group) {
            Node n = ((Group) label.getGraphic()).getChildren().get(0);
            if (n instanceof SVGPath) ((SVGPath) n).setFill(Color.web(color));
        }
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
        TableView<String[]> table = new TableView<>();
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
            // columns share the table width equally (works on every JavaFX version)
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
        table.getItems().addAll(data);

        // soft striped rows with a hover highlight
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

        // exact height for the rows, so no scrollbars appear
        table.prefHeightProperty().bind(javafx.beans.binding.Bindings.createDoubleBinding(
                () -> Math.max(150.0, table.getItems().size() * TABLE_ROW_H + TABLE_HEADER_H + 6),
                table.getItems()));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());

        // clean header (the default gray gradient is replaced once the skin exists)
        table.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            if (newSkin != null) javafx.application.Platform.runLater(() -> styleTableHeader(table));
        });
        table.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) javafx.application.Platform.runLater(() -> styleTableHeader(table));
        });
        return table;
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

    /** Small colored pill used for the TYPE and STATUS columns. */
    private Label badge(String column, String text) {
        String t = text.trim().toLowerCase(java.util.Locale.ENGLISH);
        String fg, bg;
        if (column.equals("TYPE")) {
            if (t.equals("cash")) { fg = "#1d4ed8"; bg = "#dbeafe"; }
            else if (t.equals("in-kind") || t.equals("in kind") || t.equals("goods")) { fg = "#6d28d9"; bg = "#ede9fe"; }
            else { fg = "#475569"; bg = "#e2e8f0"; }
        } else if (t.matches("received|processed|approved|active|completed|confirmed|verified|done|joined|registered")) {
            fg = "#15803d"; bg = "#dcfce7";
        } else if (t.matches("pending|ongoing|in progress|processing|review|full")) {
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