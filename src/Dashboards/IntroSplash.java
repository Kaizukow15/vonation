import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class IntroSplash extends StackPane {

    // How far off to the side the hands start before sliding in
    private static final double HAND_START_OFFSET = 700;

    private ParallelTransition fullIntro;
    private Runnable onFinishedCallback;
    private boolean isFinished = false;

    public IntroSplash(Runnable onFinishedCallback) {
        this.onFinishedCallback = onFinishedCallback;

        // 1. Background gradient (same as the login screen)
        LinearGradient bgGradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#eaf1ff")),
                new Stop(0.55, Color.web("#a9c8f5")),
                new Stop(1, Color.web("#6fa6e6")));
        setBackground(new Background(new BackgroundFill(bgGradient, CornerRadii.EMPTY, Insets.EMPTY)));

        // 2. Big logo: the three layers share one canvas, so they MUST share one size to line up
        ImageView globe = loadImageView("/images/Logo_globe.png");
        ImageView leftHand = loadImageView("/images/Logo_left_hand.png");
        ImageView rightHand = loadImageView("/images/Logo_right_hand.png");

        NumberBinding logoWidth = Bindings.min(
                Bindings.min(780, widthProperty().multiply(0.5)),
                heightProperty().multiply(0.62));
        globe.fitWidthProperty().bind(logoWidth);
        leftHand.fitWidthProperty().bind(logoWidth);
        rightHand.fitWidthProperty().bind(logoWidth);

        // Globe in the middle, hands on top of it
        StackPane logoContainer = new StackPane(globe, leftHand, rightHand);
        logoContainer.setAlignment(Pos.CENTER);

        // 3. Texts
        Label titleLabel = new Label("VoNation");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 58));
        titleLabel.setTextFill(Color.web("#1b2a4a"));

        Label subtitleLabel = new Label("DONATE  •  VOLUNTEER");
        subtitleLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 21));
        subtitleLabel.setTextFill(Color.web("#1b2a4a"));

        VBox textContainer = new VBox(8, titleLabel, subtitleLabel);
        textContainer.setAlignment(Pos.CENTER);

        VBox centerBox = new VBox(16, logoContainer, textContainer);
        centerBox.setAlignment(Pos.CENTER);

        // 4. Loading section at the very bottom (soft, thin look)
        Label loadingLabel = new Label("Preparing your community hub...");
        loadingLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        loadingLabel.setTextFill(Color.web("#6b7c96"));

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(360);
        progressBar.setPrefHeight(4);
        progressBar.setStyle(
                "-fx-accent: #6b93d6; " +
                        "-fx-control-inner-background: #d8e5fa; " +
                        "-fx-background-color: transparent; " +
                        "-fx-background-insets: 0;");

        VBox bottomLoadingBox = new VBox(8, loadingLabel, progressBar);
        bottomLoadingBox.setAlignment(Pos.CENTER);
        // Without these, the VBox stretches to the full window height and its content ends up centered
        bottomLoadingBox.setMaxHeight(Region.USE_PREF_SIZE);
        bottomLoadingBox.setMaxWidth(Region.USE_PREF_SIZE);
        StackPane.setAlignment(bottomLoadingBox, Pos.BOTTOM_CENTER);
        StackPane.setMargin(bottomLoadingBox, new Insets(0, 0, 36, 0));

        // 5. Skip hint
        Label skipLabel = new Label("Click anywhere to skip");
        skipLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        skipLabel.setTextFill(Color.web("#3d5075"));
        StackPane.setAlignment(skipLabel, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(skipLabel, new Insets(0, 24, 18, 0));

        getChildren().addAll(centerBox, bottomLoadingBox, skipLabel);

        // =========================================================================
        // START STATE: everything invisible BEFORE the first frame is drawn
        // =========================================================================
        globe.setOpacity(0.0);
        globe.setScaleX(0.92);
        globe.setScaleY(0.92);

        leftHand.setOpacity(0.0);
        leftHand.setTranslateX(-HAND_START_OFFSET);

        rightHand.setOpacity(0.0);
        rightHand.setTranslateX(HAND_START_OFFSET);

        titleLabel.setOpacity(0.0);
        subtitleLabel.setOpacity(0.0);
        bottomLoadingBox.setOpacity(0.0);
        skipLabel.setOpacity(0.6);

        // =========================================================================
        // ANIMATION: slow, ghost-like fade-ins
        // =========================================================================

        // Loading bar appears at the bottom
        FadeTransition fadeLoading = new FadeTransition(Duration.millis(450), bottomLoadingBox);
        fadeLoading.setFromValue(0.0);
        fadeLoading.setToValue(1.0);

        // Globe slowly appears in the center, like a ghost
        FadeTransition globeFade = new FadeTransition(Duration.millis(2000), globe);
        globeFade.setFromValue(0.0);
        globeFade.setToValue(1.0);
        globeFade.setInterpolator(Interpolator.EASE_BOTH);

        ScaleTransition globeScale = new ScaleTransition(Duration.millis(2400), globe);
        globeScale.setFromX(0.92);
        globeScale.setFromY(0.92);
        globeScale.setToX(1.0);
        globeScale.setToY(1.0);
        globeScale.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition globeAnim = new ParallelTransition(globeFade, globeScale);

        // Left hand glides in from the far left, right hand from the far right, fading in slowly
        FadeTransition leftFade = new FadeTransition(Duration.millis(2200), leftHand);
        leftFade.setFromValue(0.0);
        leftFade.setToValue(1.0);
        leftFade.setInterpolator(Interpolator.EASE_BOTH);

        TranslateTransition leftSlide = new TranslateTransition(Duration.millis(2400), leftHand);
        leftSlide.setFromX(-HAND_START_OFFSET);
        leftSlide.setToX(0);
        leftSlide.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition rightFade = new FadeTransition(Duration.millis(2200), rightHand);
        rightFade.setFromValue(0.0);
        rightFade.setToValue(1.0);
        rightFade.setInterpolator(Interpolator.EASE_BOTH);

        TranslateTransition rightSlide = new TranslateTransition(Duration.millis(2400), rightHand);
        rightSlide.setFromX(HAND_START_OFFSET);
        rightSlide.setToX(0);
        rightSlide.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition handsAnim = new ParallelTransition(leftFade, leftSlide, rightFade, rightSlide);

        // Globe and hands overlap: the hands start a little after the globe begins to appear
        ParallelTransition logoAnim = new ParallelTransition(
                globeAnim,
                new SequentialTransition(new PauseTransition(Duration.millis(600)), handsAnim));

        // Title and tagline fade in slowly
        FadeTransition titleFade = new FadeTransition(Duration.millis(1200), titleLabel);
        titleFade.setFromValue(0.0);
        titleFade.setToValue(1.0);

        FadeTransition subFade = new FadeTransition(Duration.millis(1200), subtitleLabel);
        subFade.setFromValue(0.0);
        subFade.setToValue(1.0);

        ParallelTransition textAnim = new ParallelTransition(titleFade, subFade);

        SequentialTransition sequence = new SequentialTransition(
                new PauseTransition(Duration.millis(200)),
                fadeLoading,
                logoAnim,
                textAnim,
                new PauseTransition(Duration.millis(700)));

        // Progress bar fills over the whole intro
        Timeline progressTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(5.3), new KeyValue(progressBar.progressProperty(), 1)));

        this.fullIntro = new ParallelTransition(sequence, progressTimeline);
        this.fullIntro.setOnFinished(e -> finishSplash());

        this.setCursor(Cursor.HAND);
        this.setOnMouseClicked(e -> skipSplash());
    }

    public void play() {
        if (fullIntro != null) {
            fullIntro.play();
        }
    }

    private void skipSplash() {
        if (isFinished) return;
        if (fullIntro != null) {
            fullIntro.stop();
        }
        finishSplash();
    }

    private void finishSplash() {
        if (isFinished) return;
        isFinished = true;

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), this);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            if (onFinishedCallback != null) {
                onFinishedCallback.run();
            }
        });
        fadeOut.play();
    }

    private ImageView loadImageView(String path) {
        ImageView iv = new ImageView();
        try {
            var stream = getClass().getResourceAsStream(path);
            if (stream != null) {
                iv.setImage(new Image(stream));
            }
        } catch (Exception ignored) {
        }
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        return iv;
    }
}
