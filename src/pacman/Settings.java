package pacman;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Settings {

    // GAME VARIABLES (RESET EVERY TIME TO THESE VALUES)
    public static int time = 0;
    public static int hiScore = SaveHandler.load();
    public static int score = 0;
    public static boolean isPaused = false;
    public static boolean isGameOver = false;
    public static boolean isStarted = false;
    public static PacMan pacman = null;
    public static int health = 3;

    // Game
    public static int pintForPoint = 10; // How many points you get after point collision
    public static int pintForPower = 25; // How many points you get after powerUp collision
    public static int powerUpTime = 10; // How long will powerUp last
    public static int powerUpTimeLeft = 0; // How long will powerUp last
    public static double speed = 0.1; // Moving speed

    // Resolution settings
    public static final int width = 1024; // Base width
    public static final int height = 768; // Base height

    // Root elements
    public static Stage stage; // Primary Stage
    public static final Group group = new Group(); // Primary Group
    public static final Scene scene = new Scene(group, width, height, Color.BLACK); // Primary Scene
    public static final Group groupGame = new Group();
    public static final Group groupUi = new Group();

    // Game resolution
        // Start settings
    public static final int tileSize = 24; // Each square in pixels
    public static final int xTileCount = 28; // Tiles on X
    public static final int yTileCount = 31; // Tiles on Y
        // Raw start resolution
    public static int gameWidth = xTileCount * tileSize; // Game width
    public static int gameHeight = yTileCount * tileSize; // Game Height
    public static int uiWidth = (int)( (scene.getWidth() - gameWidth - (scene.getWidth() * Settings.padding) * 3) ); // UI Width
        // Padding
    public static final double padding = 0.01; // Padding

    // Game window
    public static final String title = "Pacman";
    public static final boolean canResize = true;
    public static final boolean hideCursor = false;

    // UI
    public static int fontSize;
    public static final double fontMultiplier = 0.01;

}
