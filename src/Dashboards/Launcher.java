import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        NeonSync.start();
        Application.launch(LoginFX.class, args);
    }
}