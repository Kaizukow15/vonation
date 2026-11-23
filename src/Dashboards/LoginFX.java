import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableBooleanValue;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

/**
 * VoNation sign-in screen.
 *
 * Layout: branding on the left, a floating glass card on the right.
 * The card also hosts the registration form (same card, swapped content).
 */
public class LoginFX extends Application {

    // ---------- palette ----------
    private static final String NAVY = "#1b2a4a";
    private static final String NAVY_HOVER = "#141f38";
    private static final String INK = "#0f1b33";
    private static final String MUTED = "#64748b";
    private static final String SOFT = "#8b9bb4";
    private static final String LINE = "#e3e9f2";
    private static final String BLUE = "#2563eb";
    private static final String BLUE_HOVER = "#1d4ed8";

    // ---------- inline icons (24x24 stroke paths) ----------
    private static final String ICON_MAIL =
            "M4 5h16a2 2 0 0 1 2 2v10a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V7a2 2 0 0 1 2-2z M22 7l-10 6.5L2 7";
    private static final String ICON_LOCK =
            "M5 11h14v10H5z M8 11V7a4 4 0 0 1 8 0v4";
    private static final String ICON_USER =
            "M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2 M12 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8z";
    private static final String ICON_PHONE =
            "M22 16.9v3a2 2 0 0 1-2.2 2 19.8 19.8 0 0 1-8.6-3.1 19.5 19.5 0 0 1-6-6A19.8 19.8 0 0 1 2.1 4.2 2 2 0 0 1 4.1 2h3a2 2 0 0 1 2 1.7c.1 1 .4 1.9.7 2.8a2 2 0 0 1-.5 2.1L8.1 9.9a16 16 0 0 0 6 6l1.3-1.3a2 2 0 0 1 2.1-.4c.9.3 1.8.6 2.8.7a2 2 0 0 1 1.7 2z";
    private static final String ICON_CALENDAR =
            "M8 2v4 M16 2v4 M3 10h18 M5 4h14a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2z";
    private static final String ICON_EYE =
            "M1 12s4-7.5 11-7.5S23 12 23 12s-4 7.5-11 7.5S1 12 1 12z M12 15a3 3 0 1 0 0-6 3 3 0 0 0 0 6z";
    private static final String ICON_EYE_OFF =
            "M17.9 17.9A10 10 0 0 1 12 19.5C5 19.5 1 12 1 12a18.5 18.5 0 0 1 5-5.9 M9.9 4.2A9.1 9.1 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.2 3.2 M14.1 14.1a3 3 0 1 1-4.2-4.2 M1 1l22 22";
    private static final String ICON_ARROW =
            "M5 12h14 M12 5l7 7-7 7";
    private static final String ICON_SHIELD =
            "M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z";

    private static final String FIELD_TEXT_STYLE =
            "-fx-background-color: transparent;"
                    + "-fx-border-color: transparent;"
                    + "-fx-padding: 0;"
                    + "-fx-font-size: 20px;"
                    + "-fx-text-fill: " + INK + ";"
                    + "-fx-prompt-text-fill: #9aa8bd;"
                    + "-fx-focus-color: transparent;"
                    + "-fx-faint-focus-color: transparent;";

    private static boolean splashShown = false;

    private boolean transitioning = false;

    // sign-in
    private TextField userField;
    private PasswordField passHidden;
    private TextField passPlain;
    private Label errorLabel;
    private Label noticeLabel;
    private Button userBtn;
    private Button registerLink;

    // registration
    private TextField regName;
    private TextField regEmail;
    private TextField regPhone;
    private DatePicker regBirth;
    private PasswordField regPass;
    private PasswordField regConfirm;
    private Label regError;
    private Button regBtn;

    // shell
    private VBox card;
    private StackPane rightPane;
    private StackPane leftPane;
    private VBox brandBox;

    @Override
    public void start(Stage primaryStage) {
        StackPane rootStack = new StackPane();

        // Base wash: pale porcelain blue fading into a deeper blue
        LinearGradient bg = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#fbfdff")),
                new Stop(0.48, Color.web("#eef5fe")),
                new Stop(1, Color.web("#dfeafb")));
        rootStack.setBackground(new Background(new BackgroundFill(bg, CornerRadii.EMPTY, Insets.EMPTY)));

        // Soft colour blooms that keep the background from feeling flat
        Region bloomTop = bloom("#9dc2ff", 0.22, 700);
        StackPane.setAlignment(bloomTop, Pos.TOP_RIGHT);
        StackPane.setMargin(bloomTop, new Insets(-260, -220, 0, 0));

        Region bloomLeft = bloom("#bcd6ff", 0.28, 640);
        StackPane.setAlignment(bloomLeft, Pos.CENTER_LEFT);
        StackPane.setMargin(bloomLeft, new Insets(0, 0, 0, -220));

        Region bloomBottom = bloom("#c7d2fe", 0.20, 620);
        StackPane.setAlignment(bloomBottom, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(bloomBottom, new Insets(0, -160, -260, 0));

        // Layered waves along the bottom edge
        Canvas waves = new Canvas();
        waves.widthProperty().bind(rootStack.widthProperty());
        waves.heightProperty().bind(rootStack.heightProperty());
        waves.setMouseTransparent(true);
        waves.widthProperty().addListener(o -> paintWaves(waves));
        waves.heightProperty().addListener(o -> paintWaves(waves));

        rootStack.getChildren().addAll(bloomTop, bloomLeft, bloomBottom, waves);

        GridPane loginContainer = buildLoginLayout(primaryStage);
        rootStack.getChildren().add(loginContainer);

        // Intro splash only on the first launch
        IntroSplash splash = null;
        if (!splashShown) {
            splashShown = true;
            loginContainer.setOpacity(0.0);

            splash = new IntroSplash(() -> {
                rootStack.getChildren().removeIf(node -> node instanceof IntroSplash);
                reveal(loginContainer);
            });
            rootStack.getChildren().add(splash);
        } else {
            reveal(loginContainer);
        }

        Scene scene = new Scene(rootStack, 1440, 880);
        primaryStage.setTitle("VoNation - Sign in");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(850);
        primaryStage.setMaximized(true);
        primaryStage.show();

        if (splash != null) {
            IntroSplash finalSplash = splash;
            Platform.runLater(finalSplash::play);
        }
    }

    /** Fades the whole screen in and lets the card rise gently into place. */
    private void reveal(GridPane container) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(520), container);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setInterpolator(Interpolator.EASE_OUT);

        TranslateTransition rise = new TranslateTransition(Duration.millis(620), card);
        rise.setFromY(26);
        rise.setToY(0);
        rise.setInterpolator(Interpolator.EASE_OUT);

        fadeIn.play();
        rise.play();
        Platform.runLater(userField::requestFocus);
    }

    // ---------- background helpers ----------

    /** A soft radial light, used as a background bloom. */
    private Region bloom(String color, double alpha, double size) {
        RadialGradient rg = new RadialGradient(
                0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web(color, alpha)),
                new Stop(0.55, Color.web(color, alpha * 0.45)),
                new Stop(1, Color.web(color, 0)));
        Region r = new Region();
        r.setMinSize(size, size);
        r.setPrefSize(size, size);
        r.setMaxSize(size, size);
        r.setBackground(new Background(new BackgroundFill(rg, new CornerRadii(size / 2), Insets.EMPTY)));
        r.setMouseTransparent(true);
        return r;
    }

    /** Draws the three stacked wave bands across the bottom of the window. */
    private void paintWaves(Canvas c) {
        double w = c.getWidth();
        double h = c.getHeight();
        if (w <= 0 || h <= 0) return;

        GraphicsContext g = c.getGraphicsContext2D();
        g.clearRect(0, 0, w, h);

        g.setFill(Color.web("#ffffff", 0.42));
        g.beginPath();
        g.moveTo(0, h * 0.795);
        g.bezierCurveTo(w * 0.18, h * 0.720, w * 0.36, h * 0.900, w * 0.56, h * 0.850);
        g.bezierCurveTo(w * 0.74, h * 0.805, w * 0.86, h * 0.930, w, h * 0.885);
        g.lineTo(w, h);
        g.lineTo(0, h);
        g.closePath();
        g.fill();

        g.setFill(Color.web("#c9dcfb", 0.60));
        g.beginPath();
        g.moveTo(0, h * 0.870);
        g.bezierCurveTo(w * 0.20, h * 0.800, w * 0.38, h * 0.960, w * 0.58, h * 0.910);
        g.bezierCurveTo(w * 0.76, h * 0.865, w * 0.88, h * 0.985, w, h * 0.945);
        g.lineTo(w, h);
        g.lineTo(0, h);
        g.closePath();
        g.fill();

        g.setFill(Color.web("#a8c6f4", 0.55));
        g.beginPath();
        g.moveTo(0, h * 0.935);
        g.bezierCurveTo(w * 0.22, h * 0.880, w * 0.40, h * 1.010, w * 0.62, h * 0.965);
        g.bezierCurveTo(w * 0.80, h * 0.930, w * 0.90, h * 1.020, w, h * 1.000);
        g.lineTo(w, h);
        g.lineTo(0, h);
        g.closePath();
        g.fill();
    }

    // ---------- layout ----------

    private GridPane buildLoginLayout(Stage stage) {
        GridPane grid = new GridPane();

        ColumnConstraints leftCol = new ColumnConstraints();
        leftCol.setPercentWidth(48);
        ColumnConstraints rightCol = new ColumnConstraints();
        rightCol.setPercentWidth(52);
        grid.getColumnConstraints().addAll(leftCol, rightCol);

        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100);
        grid.getRowConstraints().add(row);

        // ---------- left: brand ----------
        brandBox = new VBox(0);
        brandBox.setAlignment(Pos.CENTER);
        brandBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        // Nudge the whole logo and branding block slightly to the right
        brandBox.setTranslateX(55);

        ImageView logo = new ImageView();
        try {
            var stream = getClass().getResourceAsStream("/images/Logo_ni_vonation.png");
            if (stream != null) {
                logo.setImage(new Image(stream));
            }
        } catch (Exception ignored) {
        }
        logo.setPreserveRatio(true);
        logo.setSmooth(true);
        // Grows to fill the brand column, but never wider than the column or taller than the pane.
        // The cap is tuned so the whole brand block ends up as tall as the sign-in card,
        // which keeps the left and right columns visually level.
        logo.fitWidthProperty().bind(Bindings.min(800,
                Bindings.min(grid.widthProperty().multiply(0.45).subtract(110),
                        grid.heightProperty().subtract(310))));

        // Soft halo behind the logo. Bound to the logo HEIGHT so the circle never grows
        // taller than the logo itself - otherwise the invisible glow adds dead space
        // above and below the logo and pushes the wordmark too far down.
        Circle halo = new Circle();
        halo.radiusProperty().bind(logo.fitHeightProperty().multiply(0.48));
        halo.setFill(Color.web("#ffffff", 0.62));
        halo.setEffect(new GaussianBlur(48));

        StackPane logoStack = new StackPane(halo, logo);
        logoStack.setAlignment(Pos.CENTER);

        Label brandTitle = new Label("VoNation");
        brandTitle.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 68));
        brandTitle.setTextFill(Color.web(NAVY));

        Region rule = new Region();
        rule.setPrefSize(78, 4.2);
        rule.setMaxSize(78, 4.2);
        rule.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#93c5fd", 0.0)),
                        new Stop(0.5, Color.web("#3b82f6", 0.9)),
                        new Stop(1, Color.web("#93c5fd", 0.0))),
                new CornerRadii(3), Insets.EMPTY)));

        Label brandTagline = new Label("DONATE   •   VOLUNTEER");
        brandTagline.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 18));
        brandTagline.setTextFill(Color.web("#5b6b86"));

        Label brandNote = new Label("");
        brandNote.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 20));
        brandNote.setTextFill(Color.web(SOFT));

        VBox brandText = new VBox(14, brandTitle, rule, brandTagline);
        brandText.setAlignment(Pos.CENTER);

        VBox.setMargin(brandNote, new Insets(26, 0, 0, 0));
        brandBox.getChildren().addAll(logoStack, brandText, brandNote);
        VBox.setMargin(brandText, new Insets(26, 0, 0, 0));

        leftPane = new StackPane(brandBox);
        leftPane.setAlignment(Pos.CENTER);
        leftPane.setPadding(new Insets(48, 12, 48, 108));

        // ---------- right: card (slightly enlarged) ----------
        card = new VBox(22);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(56, 60, 48, 60));
        card.setPrefWidth(625);
        card.setMinWidth(625);
        card.setMaxWidth(625);
        card.setMaxHeight(Region.USE_PREF_SIZE);
        card.setBackground(new Background(new BackgroundFill(Color.web("#ffffff", 0.96), new CornerRadii(34), Insets.EMPTY)));
        card.setBorder(new Border(new BorderStroke(Color.web("#e6ecf6"), BorderStrokeStyle.SOLID, new CornerRadii(34), new BorderWidths(1))));

        DropShadow cardShadow = new DropShadow();
        cardShadow.setColor(Color.web("#1e3a8a", 0.14));
        cardShadow.setRadius(56);
        cardShadow.setSpread(0.02);
        cardShadow.setOffsetY(26);
        card.setEffect(cardShadow);

        rightPane = new StackPane(card);
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setPadding(new Insets(40));

        // ---------- sign-in / register content ----------
        List<Node> signInNodes = buildSignInNodes(stage);
        card.getChildren().setAll(signInNodes);

        Runnable showSignIn = () -> {
            clearRegisterForm();
            applyMode(false);
            card.getChildren().setAll(signInNodes);
            Platform.runLater(userField::requestFocus);
        };

        Consumer<String> onRegistered = registeredEmail -> {
            showSignIn.run();
            userField.setText(registeredEmail);
            passHidden.clear();
            passPlain.clear();
            hideMessage(errorLabel);
            showMessage(noticeLabel, "Account created — please sign in.");
            Platform.runLater(passHidden::requestFocus);
        };

        List<Node> registerNodes = buildRegisterNodes(showSignIn, onRegistered);
        registerLink.setOnAction(ev -> {
            clearRegisterForm();
            hideMessage(errorLabel);
            hideMessage(noticeLabel);
            applyMode(true);
            card.getChildren().setAll(registerNodes);
            Platform.runLater(regName::requestFocus);
        });

        grid.add(leftPane, 0, 0);
        grid.add(rightPane, 1, 0);
        return grid;
    }

    /** Switches the card between the sign-in and the wider registration layout. */
    private void applyMode(boolean register) {
        leftPane.setVisible(!register);
        leftPane.setManaged(!register);
        GridPane.setColumnIndex(rightPane, register ? 0 : 1);
        GridPane.setColumnSpan(rightPane, register ? 2 : 1);

        card.setPrefWidth(register ? 730 : 625);
        card.setMinWidth(register ? 730 : 625);
        card.setMaxWidth(register ? 730 : 625);
        card.setSpacing(register ? 17 : 22);
        card.setPadding(register ? new Insets(46, 54, 40, 54) : new Insets(56, 60, 48, 60));

        FadeTransition ft = new FadeTransition(Duration.millis(240), card);
        ft.setFromValue(0.35);
        ft.setToValue(1.0);
        ft.play();
    }

    // ---------- sign-in content ----------

    private List<Node> buildSignInNodes(Stage stage) {
        List<Node> nodes = new ArrayList<>();

        Label heading = new Label("Hello, good to meet you");
        heading.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 40));
        heading.setTextFill(Color.web(NAVY));
        heading.setWrapText(true);

        Label sub = new Label("Please log in with your email and password to continue.");
        sub.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 18.5));
        sub.setTextFill(Color.web(MUTED));
        sub.setWrapText(true);

        VBox header = new VBox(13, pill("SIGN IN"), heading, sub);
        nodes.add(header);

        userField = new TextField();
        userField.setPromptText("Enter your email or username");
        userField.setStyle(FIELD_TEXT_STYLE);
        FieldIcon mailIcon = icon(ICON_MAIL, 24);
        HBox emailShell = shell(mailIcon, userField, userField.focusedProperty(), null);
        VBox emailBox = new VBox(11, fieldLabel("Email or Username"), emailShell);

        passHidden = new PasswordField();
        passHidden.setPromptText("Enter your password");
        passHidden.setStyle(FIELD_TEXT_STYLE);

        passPlain = new TextField();
        passPlain.setPromptText("Enter your password");
        passPlain.setStyle(FIELD_TEXT_STYLE);
        passPlain.setVisible(false);
        passPlain.textProperty().bindBidirectional(passHidden.textProperty());

        StackPane passStack = new StackPane(passHidden, passPlain);
        passStack.setAlignment(Pos.CENTER_LEFT);
        passStack.setMinHeight(68);

        FieldIcon eyeIcon = icon(ICON_EYE, 24);
        Button eyeBtn = new Button();
        eyeBtn.setGraphic(eyeIcon);
        eyeBtn.setFocusTraversable(false);
        eyeBtn.setCursor(Cursor.HAND);
        String eyeBase = "-fx-background-color: transparent; -fx-padding: 4 2 4 8; -fx-opacity: 0.72;";
        String eyeOver = "-fx-background-color: transparent; -fx-padding: 4 2 4 8; -fx-opacity: 1.0;";
        eyeBtn.setStyle(eyeBase);
        eyeBtn.setOnMouseEntered(e -> eyeBtn.setStyle(eyeOver));
        eyeBtn.setOnMouseExited(e -> eyeBtn.setStyle(eyeBase));
        eyeBtn.setOnAction(e -> {
            boolean show = !passPlain.isVisible();
            passPlain.setVisible(show);
            passHidden.setVisible(!show);
            eyeIcon.setPath(show ? ICON_EYE_OFF : ICON_EYE);
            TextField active = show ? passPlain : passHidden;
            active.requestFocus();
            active.positionCaret(active.getText() == null ? 0 : active.getText().length());
        });

        ObservableBooleanValue passFocus = passHidden.focusedProperty().or(passPlain.focusedProperty());
        FieldIcon lockIcon = icon(ICON_LOCK, 24);
        HBox passShell = shell(lockIcon, passStack, passFocus, eyeBtn);

        VBox passBox = new VBox(11, fieldLabel("Password"), passShell);

        errorLabel = banner(true);
        noticeLabel = banner(false);

        userBtn = primaryButton("Log In", ICON_ARROW);
        userBtn.setMaxWidth(Double.MAX_VALUE);

        HBox buttons = new HBox(userBtn);
        HBox.setHgrow(userBtn, Priority.ALWAYS);
        VBox.setMargin(buttons, new Insets(8, 0, 0, 0));

        Region divider = new Region();
        divider.setMinHeight(1);
        divider.setPrefHeight(1);
        divider.setMaxWidth(Double.MAX_VALUE);
        divider.setBackground(new Background(new BackgroundFill(Color.web("#eef2f8"), CornerRadii.EMPTY, Insets.EMPTY)));
        VBox.setMargin(divider, new Insets(8, 0, 0, 0));

        Label newHere = new Label("New to VoNation?");
        newHere.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16.5));
        newHere.setTextFill(Color.web(MUTED));

        registerLink = linkButton("Create an account");

        HBox footer = new HBox(7, newHere, registerLink);
        footer.setAlignment(Pos.CENTER);

        userBtn.setOnAction(e -> handleLogin(stage));
        userField.setOnAction(e -> passHidden.requestFocus());
        passHidden.setOnAction(e -> submitFromKeyboard(stage));
        passPlain.setOnAction(e -> submitFromKeyboard(stage));

        nodes.add(emailBox);
        nodes.add(passBox);
        nodes.add(noticeLabel);
        nodes.add(errorLabel);
        nodes.add(buttons);
        nodes.add(divider);
        nodes.add(footer);
        return nodes;
    }

    private void submitFromKeyboard(Stage stage) {
        handleLogin(stage);
    }

    /** One Log In for everyone. The account's role in the database decides: Admin opens the Admin dashboard. */
    private void handleLogin(Stage loginStage) {
        if (transitioning) return;
        String user = userField.getText() == null ? "" : userField.getText().trim();
        String pass = passHidden.getText() == null ? "" : passHidden.getText();

        if (user.isEmpty() || pass.trim().isEmpty()) {
            showMessage(errorLabel, "Please enter both your email/username and password.");
            return;
        }

        hideMessage(errorLabel);
        hideMessage(noticeLabel);

        // Sign-in: check the account in the database (in the background, so the window never freezes)
        transitioning = true;
        Task<UserStore.User> task = new Task<UserStore.User>() {
            @Override
            protected UserStore.User call() throws Exception {
                return UserStore.authenticate(user, pass);
            }
        };
        task.setOnSucceeded(ev -> {
            UserStore.User found = task.getValue();
            if (found == null) {
                transitioning = false;
                showMessage(errorLabel, "Incorrect email/username or password.");
            } else {
                openDashboard(loginStage, found.email, UserStore.isAdmin(found));
            }
        });
        task.setOnFailed(ev -> {
            transitioning = false;
            showMessage(errorLabel, describe(task.getException()));
        });
        Thread worker = new Thread(task, "sign-in");
        worker.setDaemon(true);
        worker.start();
    }

    private void openDashboard(Stage loginStage, String username, boolean asAdmin) {
        transitioning = true;
        try {
            // The dashboard fades itself in; meanwhile the login window fades out (smooth crossfade)
            if (asAdmin) {
                new AdminDashboardFX(username).show();
            } else {
                new UserDashboardFX(username).show();
            }

            Timeline fadeOut = new Timeline(new KeyFrame(Duration.millis(600),
                    new KeyValue(loginStage.opacityProperty(), 0.0, Interpolator.EASE_BOTH)));
            fadeOut.setOnFinished(e -> loginStage.close());
            fadeOut.play();
        } catch (Exception ex) {
            transitioning = false;
            ex.printStackTrace();
        }
    }

    // ---------- register form ----------

    private List<Node> buildRegisterNodes(Runnable onBack, Consumer<String> onRegistered) {
        List<Node> nodes = new ArrayList<>();

        Label heading = new Label("Create your account");
        heading.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 37));
        heading.setTextFill(Color.web(NAVY));
        heading.setWrapText(true);

        Label sub = new Label("Register to donate and volunteer with VoNation.");
        sub.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 18));
        sub.setTextFill(Color.web(MUTED));
        sub.setWrapText(true);

        nodes.add(new VBox(12, pill("CREATE ACCOUNT"), heading, sub));

        regName = new TextField();
        regName.setPromptText("Enter your full name");
        regName.setStyle(FIELD_TEXT_STYLE);
        HBox nameShell = shell(icon(ICON_USER, 24), regName, regName.focusedProperty(), null);

        regEmail = new TextField();
        regEmail.setPromptText("Enter your email");
        regEmail.setStyle(FIELD_TEXT_STYLE);
        HBox emailShell = shell(icon(ICON_MAIL, 24), regEmail, regEmail.focusedProperty(), null);

        nodes.add(fieldBox("Full name", nameShell));
        nodes.add(fieldBox("Email", emailShell));

        regPhone = new TextField();
        regPhone.setPromptText("09XX XXX XXXX");
        regPhone.setStyle(FIELD_TEXT_STYLE);
        HBox phoneShell = shell(icon(ICON_PHONE, 24), regPhone, regPhone.focusedProperty(), null);

        regBirth = new DatePicker();
        styleDatePicker(regBirth);
        FieldIcon calendarIcon = icon(ICON_CALENDAR, 24);
        calendarIcon.makeClickable(regBirth::show);
        HBox birthShell = shell(calendarIcon, regBirth, regBirth.getEditor().focusedProperty(), null);

        HBox contactRow = new HBox(18, fieldBox("Contact number", phoneShell), fieldBox("Birthday", birthShell));
        nodes.add(contactRow);

        regPass = new PasswordField();
        regPass.setPromptText("At least 8 characters");
        regPass.setStyle(FIELD_TEXT_STYLE);
        HBox passShell = shell(icon(ICON_LOCK, 24), regPass, regPass.focusedProperty(), null);

        regConfirm = new PasswordField();
        regConfirm.setPromptText("Repeat password");
        regConfirm.setStyle(FIELD_TEXT_STYLE);
        HBox confirmShell = shell(icon(ICON_LOCK, 24), regConfirm, regConfirm.focusedProperty(), null);

        HBox passRow = new HBox(18, fieldBox("Password", passShell), fieldBox("Confirm password", confirmShell));
        nodes.add(passRow);

        regError = banner(true);
        nodes.add(regError);

        regBtn = primaryButton("Create account", ICON_ARROW);
        regBtn.setPrefHeight(68);
        nodes.add(regBtn);

        Button backLink = linkButton("Back to sign in");
        HBox backRow = new HBox(backLink);
        backRow.setAlignment(Pos.CENTER);
        nodes.add(backRow);

        regBtn.setOnAction(ev -> doRegister(onRegistered));
        backLink.setOnAction(ev -> onBack.run());
        regName.setOnAction(ev -> regEmail.requestFocus());
        regEmail.setOnAction(ev -> regPhone.requestFocus());
        regPhone.setOnAction(ev -> regBirth.getEditor().requestFocus());
        regPass.setOnAction(ev -> regConfirm.requestFocus());
        regConfirm.setOnAction(ev -> doRegister(onRegistered));
        return nodes;
    }

    private void doRegister(Consumer<String> onRegistered) {
        String name = regName.getText() == null ? "" : regName.getText().trim();
        String email = regEmail.getText() == null ? "" : regEmail.getText().trim();
        String phone = regPhone.getText() == null ? "" : regPhone.getText().trim();
        String birthText = regBirth.getEditor().getText() == null ? "" : regBirth.getEditor().getText().trim();
        LocalDate birth = readBirthDate();
        String pass = regPass.getText() == null ? "" : regPass.getText();
        String confirm = regConfirm.getText() == null ? "" : regConfirm.getText();

        String problem = null;
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || birthText.isEmpty()
                || pass.isEmpty() || confirm.isEmpty()) {
            problem = "Please fill in all fields.";
        } else if (name.length() > 100) {
            problem = "Full name is too long.";
        } else if (!UserStore.isValidEmail(email)) {
            problem = "Please enter a valid email address.";
        } else if (!UserStore.isValidPhone(phone)) {
            problem = "Please enter a valid mobile number (e.g. 09171234567).";
        } else if (birth == null) {
            problem = "Please enter your birthday as MM/DD/YYYY.";
        } else if (birth.isAfter(LocalDate.now()) || birth.getYear() < 1900) {
            problem = "Please enter a valid birthday.";
        } else if (pass.length() < UserStore.MIN_PASSWORD_LENGTH) {
            problem = "Password must be at least " + UserStore.MIN_PASSWORD_LENGTH + " characters.";
        } else if (!pass.equals(confirm)) {
            problem = "Passwords do not match.";
        }
        if (problem != null) {
            showMessage(regError, problem);
            return;
        }
        hideMessage(regError);

        regBtn.setDisable(true);
        Task<UserStore.User> task = new Task<UserStore.User>() {
            @Override
            protected UserStore.User call() throws Exception {
                return UserStore.register(name, email, phone, birth, pass);
            }
        };
        task.setOnSucceeded(ev -> {
            regBtn.setDisable(false);
            onRegistered.accept(task.getValue().email);
        });
        task.setOnFailed(ev -> {
            regBtn.setDisable(false);
            showMessage(regError, describe(task.getException()));
        });
        Thread worker = new Thread(task, "register");
        worker.setDaemon(true);
        worker.start();
    }

    private void clearRegisterForm() {
        regName.clear();
        regEmail.clear();
        regPhone.clear();
        regBirth.setValue(null);
        regBirth.getEditor().clear();
        regPass.clear();
        regConfirm.clear();
        hideMessage(regError);
        regBtn.setDisable(false);
    }

    // ---------- small builders ----------

    /** The little eyebrow label above a heading ("SIGN IN", "CREATE ACCOUNT"). */
    private Label pill(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13.5));
        l.setTextFill(Color.web(BLUE));
        l.setPadding(new Insets(7, 14, 7, 14));
        l.setBackground(new Background(new BackgroundFill(Color.web("#eef4ff"), new CornerRadii(99), Insets.EMPTY)));
        l.setBorder(new Border(new BorderStroke(Color.web("#dbe7ff"), BorderStrokeStyle.SOLID, new CornerRadii(99), new BorderWidths(1))));
        l.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        return l;
    }

    private Label fieldLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        l.setTextFill(Color.web("#3c4c66"));
        return l;
    }

    private VBox fieldBox(String labelText, Node control) {
        VBox box = new VBox(9, fieldLabel(labelText), control);
        box.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    /** Rounded input shell: icon on the left, field in the middle, optional action on the right. */
    private HBox shell(Node icon, Region field, ObservableBooleanValue focused, Node action) {
        HBox box = new HBox(14);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(0, 16, 0, 20));
        box.setMinHeight(72);
        box.setPrefHeight(72);
        box.setMaxHeight(72);
        box.setMaxWidth(Double.MAX_VALUE);
        box.getChildren().add(icon);
        HBox.setHgrow(field, Priority.ALWAYS);
        box.getChildren().add(field);
        if (action != null) box.getChildren().add(action);

        applyShellState(box, icon, false);
        focused.addListener((obs, oldV, newV) -> applyShellState(box, icon, newV));
        return box;
    }

    private void applyShellState(HBox box, Node icon, boolean focused) {
        box.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(18), Insets.EMPTY)));
        box.setBorder(new Border(new BorderStroke(
                Color.web(focused ? BLUE : LINE),
                BorderStrokeStyle.SOLID,
                new CornerRadii(18),
                new BorderWidths(focused ? 1.6 : 1.2))));
        if (focused) {
            DropShadow glow = new DropShadow();
            glow.setColor(Color.web(BLUE, 0.20));
            glow.setRadius(14);
            glow.setOffsetY(2);
            box.setEffect(glow);
        } else {
            box.setEffect(null);
        }
        if (icon instanceof FieldIcon) {
            ((FieldIcon) icon).setColor(Color.web(focused ? BLUE : "#9fb0c7"));
        }
    }

    /** A crisp stroke icon that keeps its 24x24 geometry and can be recoloured. */
    private FieldIcon icon(String path, double size) {
        return new FieldIcon(path, size);
    }

    private static final class FieldIcon extends StackPane {
        private final SVGPath path;
        private final double size;

        FieldIcon(String data, double size) {
            this.size = size;
            this.path = new SVGPath();
            path.setContent(data);
            path.setFill(Color.TRANSPARENT);
            path.setStroke(Color.web("#9fb0c7"));
            path.setStrokeWidth(1.7);
            path.setStrokeLineCap(StrokeLineCap.ROUND);
            path.setStrokeLineJoin(StrokeLineJoin.ROUND);
            double scale = size / 24.0;
            path.setScaleX(scale);
            path.setScaleY(scale);
            getChildren().add(new Group(path));
            setMinSize(size, size);
            setPrefSize(size, size);
            setMaxSize(size, size);
            setMouseTransparent(true);
        }

        void setColor(Color color) {
            path.setStroke(color);
        }

        void setPath(String data) {
            path.setContent(data);
        }

        /** Turns the icon into a small click target (used by the birthday calendar). */
        void makeClickable(Runnable action) {
            setMouseTransparent(false);
            setCursor(Cursor.HAND);
            setOnMouseClicked(e -> action.run());
        }
    }

    /** Soft rounded banner used for errors and success notices. */
    private Label banner(boolean error) {
        Label l = new Label();
        l.setWrapText(true);
        l.setMaxWidth(Double.MAX_VALUE);
        l.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16));
        l.setPadding(new Insets(13, 18, 13, 18));
        l.setBackground(new Background(new BackgroundFill(
                Color.web(error ? "#fef2f2" : "#f0fdf4"), new CornerRadii(12), Insets.EMPTY)));
        l.setBorder(new Border(new BorderStroke(
                Color.web(error ? "#fecaca" : "#bbf7d0"),
                BorderStrokeStyle.SOLID, new CornerRadii(12), new BorderWidths(1))));
        l.setTextFill(Color.web(error ? "#b91c1c" : "#15803d"));
        l.setVisible(false);
        l.setManaged(false);
        return l;
    }

    /** Filled blue call-to-action with a soft glow and a small lift on hover. */
    private Button primaryButton(String text, String trailingIcon) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setPrefHeight(70);
        b.setCursor(Cursor.HAND);
        b.setFocusTraversable(false);
        b.setFont(Font.font("Segoe UI", FontWeight.BOLD, 19));
        b.setContentDisplay(ContentDisplay.RIGHT);
        b.setGraphicTextGap(12);
        b.setGraphic(icon(trailingIcon, 21));

        String normal = "-fx-background-color: linear-gradient(to bottom right, #3b82f6, #2563eb);"
                + "-fx-background-radius: 18;"
                + "-fx-text-fill: white;"
                + "-fx-effect: dropshadow(gaussian, rgba(37,99,235,0.32), 16, 0.15, 0, 6);";
        String hover = "-fx-background-color: linear-gradient(to bottom right, #2563eb, #1d4ed8);"
                + "-fx-background-radius: 18;"
                + "-fx-text-fill: white;"
                + "-fx-effect: dropshadow(gaussian, rgba(37,99,235,0.42), 22, 0.18, 0, 9);";
        b.setStyle(normal);
        b.setOnMouseEntered(e -> {
            if (!b.isDisabled()) {
                b.setStyle(hover);
                lift(b, -1.5);
            }
        });
        b.setOnMouseExited(e -> {
            b.setStyle(normal);
            lift(b, 0);
        });
        return b;
    }

    /** Solid navy secondary action. */
    private Button darkButton(String text, String leadingIcon) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setPrefHeight(70);
        b.setCursor(Cursor.HAND);
        b.setFocusTraversable(false);
        b.setFont(Font.font("Segoe UI", FontWeight.BOLD, 19));
        b.setContentDisplay(ContentDisplay.LEFT);
        b.setGraphicTextGap(12);
        b.setGraphic(icon(leadingIcon, 21));

        String normal = "-fx-background-color: " + NAVY + ";"
                + "-fx-background-radius: 18;"
                + "-fx-text-fill: white;"
                + "-fx-effect: dropshadow(gaussian, rgba(27,42,74,0.28), 16, 0.12, 0, 6);";
        String hover = "-fx-background-color: " + NAVY_HOVER + ";"
                + "-fx-background-radius: 18;"
                + "-fx-text-fill: white;"
                + "-fx-effect: dropshadow(gaussian, rgba(27,42,74,0.36), 22, 0.15, 0, 9);";
        b.setStyle(normal);
        b.setOnMouseEntered(e -> {
            if (!b.isDisabled()) {
                b.setStyle(hover);
                lift(b, -1.5);
            }
        });
        b.setOnMouseExited(e -> {
            b.setStyle(normal);
            lift(b, 0);
        });
        return b;
    }

    private void lift(Button b, double y) {
        TranslateTransition t = new TranslateTransition(Duration.millis(140), b);
        t.setToY(y);
        t.setInterpolator(Interpolator.EASE_OUT);
        t.play();
    }

    /** Small text-style button (used for "Create an account" / "Back to sign in"). */
    private Button linkButton(String text) {
        Button b = new Button(text);
        b.setCursor(Cursor.HAND);
        b.setFocusTraversable(false);
        b.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16.5));
        String base = "-fx-background-color: transparent; -fx-padding: 2 2 2 2; "
                + "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; ";
        String normal = base + "-fx-text-fill: " + BLUE + ";";
        String over = base + "-fx-text-fill: " + BLUE_HOVER + "; -fx-underline: true;";
        b.setStyle(normal);
        b.setOnMouseEntered(ev -> b.setStyle(over));
        b.setOnMouseExited(ev -> b.setStyle(normal));
        return b;
    }

    /** Date picker that blends into the shared input shell; the date can be typed or picked. */
    private void styleDatePicker(DatePicker dp) {
        dp.setEditable(true);
        dp.setPromptText("MM/DD/YYYY");
        dp.setPrefHeight(70);
        dp.setMaxWidth(Double.MAX_VALUE);
        dp.setBackground(Background.EMPTY);
        dp.setBorder(null);
        dp.setPadding(Insets.EMPTY);
        dp.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; "
                + "-fx-background-insets: 0; -fx-padding: 0;");

        // The editor is a TextField handled by the DatePicker skin, so it is flattened directly
        dp.getEditor().setBackground(Background.EMPTY);
        dp.getEditor().setBorder(null);
        dp.getEditor().setPadding(Insets.EMPTY);
        dp.getEditor().setStyle(FIELD_TEXT_STYLE);

        // the "/" is added automatically while typing, so only the digits need to be typed
        applyDateMask(dp.getEditor());

        // clicking the month or the year in the calendar opens a list to pick from
        dp.showingProperty().addListener((obs, was, now) -> {
            if (now) Platform.runLater(() -> makeMonthYearPickable(dp));
        });

        dp.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date == null ? "" : BIRTH_SHOW.format(date);
            }

            @Override
            public LocalDate fromString(String text) {
                if (text == null || text.isBlank()) return null;
                try {
                    return LocalDate.parse(text.trim(), BIRTH_READ);
                } catch (RuntimeException e) {
                    return null;
                }
            }
        });

        // Re-flatten once the skin exists, and calm down the built-in calendar arrow
        dp.skinProperty().addListener((obs, oldSkin, newSkin) -> Platform.runLater(() -> {
            dp.getEditor().setBackground(Background.EMPTY);
            dp.getEditor().setBorder(null);
            dp.getEditor().setPadding(Insets.EMPTY);
            Node arrowButton = dp.lookup(".arrow-button");
            if (arrowButton instanceof Region) {
                ((Region) arrowButton).setBackground(Background.EMPTY);
                ((Region) arrowButton).setPadding(new Insets(0, 10, 0, 2));
            }
            Node arrow = dp.lookup(".arrow");
            if (arrow instanceof Region) {
                ((Region) arrow).setBackground(new Background(new BackgroundFill(
                        Color.web("#9fb0c7"), CornerRadii.EMPTY, Insets.EMPTY)));
                arrow.setOpacity(0);
            }
        }));
    }

    /** Keeps the field in the MM/DD/YYYY shape: digits only, and the "/" is inserted by itself. */
    private static void applyDateMask(TextField tf) {
        tf.setTextFormatter(new TextFormatter<String>(change -> {
            if (!change.isContentChange()) return change;
            String old = change.getControlText();
            String digits = change.getControlNewText().replaceAll("\\D", "");
            // backspacing over a "/" removes the digit before it instead of doing nothing
            if (change.isDeleted() && !change.isAdded()
                    && digits.length() == old.replaceAll("\\D", "").length()) {
                int before = old.substring(0, Math.min(change.getRangeStart(), old.length()))
                        .replaceAll("\\D", "").length();
                if (before > 0) digits = digits.substring(0, before - 1) + digits.substring(before);
            }
            if (digits.length() > 8) digits = digits.substring(0, 8);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digits.length(); i++) {
                if (i == 2 || i == 4) sb.append('/');
                sb.append(digits.charAt(i));
            }
            String out = sb.toString();
            change.setRange(0, old.length());
            change.setText(out);
            change.selectRange(out.length(), out.length());
            return change;
        }));
    }

    /** Makes the month and the year shown on top of the calendar clickable (they open a pick list). */
    private void makeMonthYearPickable(DatePicker dp) {
        if (!(dp.getSkin() instanceof DatePickerSkin)) return;
        Node content = ((DatePickerSkin) dp.getSkin()).getPopupContent();
        if (content == null) return;
        Node pane = content.lookup(".month-year-pane");
        if (!(pane instanceof Parent)) return;

        List<Label> labels = new ArrayList<>();
        for (Node spinner : ((Parent) pane).getChildrenUnmodifiable()) {
            Label l = findLabel(spinner);
            if (l != null) labels.add(l);
        }
        if (labels.size() < 2) return;
        Label monthLbl = labels.get(0);
        Label yearLbl = labels.get(1);

        if (monthLbl.getProperties().containsKey("vo-pickable")) return;   // already hooked
        monthLbl.getProperties().put("vo-pickable", true);
        yearLbl.getProperties().put("vo-pickable", true);
        monthLbl.setCursor(Cursor.HAND);
        yearLbl.setCursor(Cursor.HAND);

        monthLbl.setOnMouseClicked(ev -> {
            List<String> names = new ArrayList<>();
            for (Month m : Month.values()) names.add(m.getDisplayName(TextStyle.FULL, Locale.getDefault()));
            int current = Math.max(0, names.indexOf(monthLbl.getText()));
            pickFromList(monthLbl, names, current, idx -> jumpTo(dp, idx + 1, parseYear(yearLbl)));
        });

        yearLbl.setOnMouseClicked(ev -> {
            int thisYear = LocalDate.now().getYear();
            List<String> years = new ArrayList<>();
            for (int y = thisYear; y >= 1900; y--) years.add(String.valueOf(y));
            int current = Math.max(0, years.indexOf(yearLbl.getText().trim()));
            pickFromList(yearLbl, years, current, idx -> {
                int month = 1;
                for (Month m : Month.values()) {
                    if (m.getDisplayName(TextStyle.FULL, Locale.getDefault()).equals(monthLbl.getText())) {
                        month = m.getValue();
                    }
                }
                jumpTo(dp, month, thisYear - idx);
            });
        });
    }

    private static Label findLabel(Node n) {
        if (n instanceof Label) return (Label) n;
        if (n instanceof Parent) {
            for (Node c : ((Parent) n).getChildrenUnmodifiable()) {
                Label l = findLabel(c);
                if (l != null) return l;
            }
        }
        return null;
    }

    private static int parseYear(Label yearLbl) {
        try {
            return Integer.parseInt(yearLbl.getText().trim());
        } catch (NumberFormatException e) {
            return LocalDate.now().getYear();
        }
    }

    /** Shows the calendar of the chosen month and year (keeps the picked day when it still exists). */
    private static void jumpTo(DatePicker dp, int month, int year) {
        YearMonth ym = YearMonth.of(year, month);
        int day = dp.getValue() == null ? 1 : Math.min(dp.getValue().getDayOfMonth(), ym.lengthOfMonth());
        dp.setValue(ym.atDay(day));
    }

    private static void pickFromList(Label anchor, List<String> items, int selected,
                                     java.util.function.IntConsumer onPick) {
        ListView<String> list = new ListView<>();
        list.getItems().addAll(items);
        list.setPrefWidth(anchor.getWidth() < 110 ? 110 : anchor.getWidth() + 20);
        list.setPrefHeight(Math.min(items.size(), 9) * 26 + 4);
        list.getSelectionModel().select(selected);
        list.scrollTo(Math.max(0, selected - 3));

        CustomMenuItem item = new CustomMenuItem(list, false);
        item.setHideOnClick(false);
        ContextMenu menu = new ContextMenu(item);
        list.setOnMouseClicked(ev -> {
            int idx = list.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                menu.hide();
                onPick.accept(idx);
            }
        });
        menu.show(anchor, Side.BOTTOM, 0, 0);
    }

    private void showMessage(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
        label.setManaged(true);
    }

    private void hideMessage(Label label) {
        label.setVisible(false);
        label.setManaged(false);
    }

    private String describe(Throwable t) {
        if (t instanceof UserStore.DuplicateEmailException) {
            return "That email is already registered. Try signing in instead.";
        }
        if (t instanceof java.io.IOException) {
            return "Could not reach the PostgreSQL database. Check db.properties and that PostgreSQL is running.";
        }
        return "Something went wrong. Please try again.";
    }

    private static final DateTimeFormatter BIRTH_SHOW = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter BIRTH_READ =
            DateTimeFormatter.ofPattern("M/d/uuuu").withResolverStyle(ResolverStyle.STRICT);

    /** The birthday typed or picked in the form, or null if it is empty or not a real date. */
    private LocalDate readBirthDate() {
        String text = regBirth.getEditor().getText();
        if (text == null || text.isBlank()) return null;
        try {
            return LocalDate.parse(text.trim(), BIRTH_READ);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}