package pacman;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Settings {

    // GAME VARIABLES (RESET EVERY TIME TO THESE VALUES)
    public static int time = 0; // Timer
    public static int hiScore = SaveHandler.load(); // Loaded high score from file
    public static int score = 0; // Current score
    public static boolean isPaused = false; // Game pause (Key: P)
    public static boolean isGameOver = false; // Game over (Win/Lose)
    public static boolean isStarted = false; // First launch
    public static PacMan pacman = null; // PacMan cahracter
    public static int ghostCount = 4; // Ghosts count
    public static Ghost[] ghosts = new Ghost[ghostCount]; // PacMan cahracter
    public static int health = 1; // Health
    public static int difficulty = 1; // 0 - random location | 1 - PathFinding | 2 - PathFinding with Wallhack

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
    public static final Group groupGame = new Group(); // Group for the game area
    public static final Group groupUi = new Group(); // Group for UI

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
    public static final String title = "PacMan"; // Game windows title
    public static final boolean canResize = true; // Window resizable property
    public static final boolean hideCursor = true; // Cursor visibility property

    // UI
    public static int fontSize; // Calculated font size for the resolution
    public static final double fontMultiplier = 0.01; // Font responsive multiplier

    // DEVELOPER
    public static final boolean devBuild = true; // Developer build enabled/disabled (Enables cursor too)
    public static boolean canTarget = false; // Can be target targeted ?
    public static Point2D hoverPoint = new Point2D(0.0,0.0); // Developer target point

    // CACHE
    public static Image[] pacmans = {
            new Image("file:img/PacMan/1.png"),
            new Image("file:img/PacMan/2.png"),
            new Image("file:img/PacMan/3.png"),
            new Image("file:img/PacMan/4.png"),
            new Image("file:img/PacMan/5.png"),
            new Image("file:img/PacMan/6.png"),
            new Image("file:img/PacMan/7.png")
    };
    public static List<Image[]> ghostsImg = new ArrayList<>();

    public static void load(){
        ghostsImg.add(new Image[]{
                new Image("file:img/Ghost/Alive/ghost-blue-01.png"),
                new Image("file:img/Ghost/Alive/ghost-blue-02.png"),
                new Image("file:img/Ghost/Alive/ghost-blue-03.png"),
                new Image("file:img/Ghost/Alive/ghost-blue-04.png")
        });
        ghostsImg.add(new Image[]{
                new Image("file:img/Ghost/Alive/ghost-green-01.png"),
                new Image("file:img/Ghost/Alive/ghost-green-02.png"),
                new Image("file:img/Ghost/Alive/ghost-green-03.png"),
                new Image("file:img/Ghost/Alive/ghost-green-04.png")
        });
        ghostsImg.add(new Image[]{
                new Image("file:img/Ghost/Alive/ghost-pink-01.png"),
                new Image("file:img/Ghost/Alive/ghost-pink-02.png"),
                new Image("file:img/Ghost/Alive/ghost-pink-03.png"),
                new Image("file:img/Ghost/Alive/ghost-pink-04.png")
        });
        ghostsImg.add(new Image[]{
                new Image("file:img/Ghost/Alive/ghost-yellow-01.png"),
                new Image("file:img/Ghost/Alive/ghost-yellow-02.png"),
                new Image("file:img/Ghost/Alive/ghost-yellow-03.png"),
                new Image("file:img/Ghost/Alive/ghost-yellow-04.png")
        });
        ghostsImg.add(new Image[]{
                new Image("file:img/Ghost/Dead/ghost-dead-01.png"),
                new Image("file:img/Ghost/Dead/ghost-dead-02.png"),
                new Image("file:img/Ghost/Dead/ghost-dead-03.png"),
                new Image("file:img/Ghost/Dead/ghost-dead-04.png")
        });
    }
}
