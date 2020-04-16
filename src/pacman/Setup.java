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

        Settings.load();

        // <editor-fold desc="Setup stage">

            // Base setup
        Settings.stage.setTitle(Settings.title); // Set title
        Settings.stage.setResizable(Settings.canResize); // Set resizable property
        Settings.stage.setScene(Settings.scene); // Set scene
            // Set cursor
        Settings.scene.setCursor(
                Settings.devBuild ? Cursor.DEFAULT : (Settings.hideCursor ? Cursor.NONE : Cursor.DEFAULT)
        );
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

        // Spawn ghosts
        for(int i = 0; i < Settings.ghostCount; i++)
            Settings.ghosts[i] = new Ghost();

        GuiHandler.start(); // Launch UI handler

            // Set key detection
        Settings.scene.setOnKeyPressed(e->keyDetection(e));

            // Add to root group all sub elements
        Settings.group.getChildren().addAll(Settings.groupGame, Settings.groupUi);

        // <editor-fold desc="Developer build">

        if(Settings.devBuild)
            new Developer(); // Calls for developer build

        // </editor-fold>
    }

    static void restart(){
        SaveHandler.save();
        Settings.time = 0;
        Settings.hiScore = SaveHandler.load();
        Settings.score = 0;
        Settings.isStarted = true;
        Settings.isPaused = false;
        Settings.isGameOver = false;
        if(Settings.pacman != null){
            Settings.pacman.t.stop();
            Settings.pacman.t2.stop();
            Settings.groupGame.getChildren().remove(Settings.pacman);
            Settings.pacman = null;
        }
        Settings.health = 3;

        MapGenerator.start();
        for(int i = 0; i < Settings.ghostCount; i++)
            Settings.ghosts[i] = new Ghost();
        Settings.groupGame.getChildren().remove(GuiHandler.status);
        Settings.groupGame.getChildren().add(GuiHandler.status);

        Settings.pacman = new PacMan();
        Settings.groupGame.getChildren().add(Settings.pacman);
    }
    static void respawn(){
        Settings.pacman = new PacMan();
        Settings.groupGame.getChildren().add(Settings.pacman);
        for(int i = 0; i < Settings.ghostCount; i++)
            Settings.ghosts[i] = new Ghost();
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