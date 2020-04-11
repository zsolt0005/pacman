package sample;

// JavaFX
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

// My imports
import pacman.Settings;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
            // Reference to primaryStage
        Settings.stage = primaryStage;
            // Load base font
        Font.loadFont("file:fonts/Joystix.TTF", 12);
            // Call start method
        pacman.Setup.setup();
    }

    public static void main(String[] args) {
        launch(args);
    }
}