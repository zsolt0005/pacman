package pacman;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class Setup {

    static Timeline mainTimer;

    public static void setup(){

        // <editor-fold desc="Setup stage">

            // Base setup
        Settings.stage.setTitle(Settings.title); // Set title
        Settings.stage.setResizable(Settings.canResize); // Set resizable property
        Settings.stage.setScene(Settings.scene); // Set scene
        Settings.scene.setCursor(Settings.hideCursor ? Cursor.NONE : Cursor.DEFAULT); // Set cursor
            // Icon
        Settings.stage.getIcons().add(new Image("file:img/icons/pacman.png"));
            // Show stage
        Settings.stage.show();

        // </editor-fold>

        // <editor-fold desc="Setup group and scene">

        Settings.group.getChildren().removeAll(); // Remove all old elements
        Settings.scene.getStylesheets().removeAll(); // Remove all stylesheets
        Settings.scene.getStylesheets().add("file:css/global.css"); // Set new global CSS

        // </editor-fold>

        // <editor-fold desc="Setup and launch timers">

        mainTimer = new Timeline(new KeyFrame(Duration.millis(50), e->{
            updateWindow(); // Updates game and UI frame size based on the main scene size
        }));
        mainTimer.setCycleCount(Animation.INDEFINITE);
        mainTimer.play();

        // </editor-fold>

        MapGenerator.start(); // Launch map generator
        GuiHandler.start(); // Launch UI handler

        // Set key detection
        Settings.scene.setOnKeyPressed(e->keyDetection(e));

        // TODO: enemy

            // Add to root group all sub elements
        Settings.group.getChildren().addAll(Settings.groupGame, Settings.groupUi);
    }

    static void restart(){
        // TODO: Restart game
    }

    static void keyDetection(KeyEvent e){
            // Start game
        if(e.getCode() == KeyCode.ENTER && !Settings.isStarted){
            Settings.isStarted = true;
            Settings.pacman = new PacMan();
            Settings.groupGame.getChildren().add(Settings.pacman);
        }
            // Restart game
        if(e.getCode() == KeyCode.ENTER && Settings.isGameOver)
            restart();
            // Exit
        if(e.getCode() == KeyCode.ESCAPE)
            System.exit(0); // Exit
            // Pause
        if(e.getCode() == KeyCode.P)
            Settings.isPaused = !Settings.isPaused; // Pause/Unpause the game

            // Apply direction
        if(Settings.pacman != null && e.getCode() == KeyCode.W)
            Settings.pacman.requestedDirection = 0;
        if(Settings.pacman != null && e.getCode() == KeyCode.D)
            Settings.pacman.requestedDirection = 1;
        if(Settings.pacman != null && e.getCode() == KeyCode.S)
            Settings.pacman.requestedDirection = 2;
        if(Settings.pacman != null && e.getCode() == KeyCode.A)
            Settings.pacman.requestedDirection = 3;
    }
    static void updateWindow(){
            // Apply width and height
        Settings.groupGame.prefHeight(Settings.gameHeight);
        Settings.groupUi.prefHeight(Settings.gameHeight);
        Settings.groupGame.prefWidth(Settings.gameWidth);
        Settings.groupUi.prefWidth(Settings.uiWidth);
            // Set positions
        Settings.groupGame.setLayoutX( (Settings.scene.getWidth() - (Settings.gameWidth + Settings.uiWidth)) / 2 );
        Settings.groupUi.setLayoutX( Settings.groupGame.getLayoutX() + Settings.gameWidth + (Settings.scene.getWidth() * Settings.padding) );
        Settings.groupGame.setLayoutY( (Settings.scene.getHeight() - Settings.gameHeight) / 2 );
        Settings.groupUi.setLayoutY( (Settings.scene.getHeight() - Settings.gameHeight) / 2 );
    }

}

    // <editor-fold desc="">
    // </editor-fold>