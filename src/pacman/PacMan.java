package pacman;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class PacMan extends Canvas {

    GraphicsContext gc;
    Timeline t; // Timeline for draw
    Timeline t2; // Timeline for animation

    int direction = 1; // 0-UP 1-RIGHT 2-DOWN 3-LEFT
    int requestedDirection = 1; // For direction change

    boolean isDead = false; // alive/dead state
    boolean isMoving = false; // moving state

    // For animation
    int animationFrame = 0;
    Image[] pacmans = {
            new Image("file:img/PacMan/1.png"),
            new Image("file:img/PacMan/2.png"),
            new Image("file:img/PacMan/3.png"),
            new Image("file:img/PacMan/4.png"),
            new Image("file:img/PacMan/5.png"),
            new Image("file:img/PacMan/6.png"),
            new Image("file:img/PacMan/7.png")
    };

    public PacMan(){
        super(Settings.tileSize * 0.7, Settings.tileSize * 0.7);
        gc = getGraphicsContext2D();

        setLayoutX( (14 * Settings.tileSize) + ( (Settings.tileSize - getWidth() ) / 2) );
        setLayoutY( (23 * Settings.tileSize) + ( (Settings.tileSize - getHeight() ) / 2) );

        // <editor-fold desc="Timers">

        t = new Timeline(new KeyFrame(Duration.millis(1), e->{
            if(Settings.isPaused)
                return;

            movement();
            draw();
        }));
        t.setCycleCount(Animation.INDEFINITE);
        t.play();

        t2 = new Timeline(new KeyFrame(Duration.millis(150), e->{
            if(Settings.isPaused)
                return;

            animation();
        }));
        t2.setCycleCount(Animation.INDEFINITE);
        t2.play();

        // </editor-fold>

    }

    void movement(){
        double x = 0;
        double y = 0;

        // <editor-fold desc="Rotate to direction and set speed">

        if(direction == 0){
            setRotate(-90);
            y = -Settings.speed;
        }
        if(direction == 1) {
            setRotate(0);
            x = Settings.speed;
        }
        if(direction == 2) {
            setRotate(90);
            y = Settings.speed;
        }
        if(direction == 3) {
            setRotate(-180);
            x = -Settings.speed;
        }

        // </editor-fold>

        // <editor-fold desc="Center PacMan">

        double optimalY = ( (int)(getLayoutY() / Settings.tileSize) * Settings.tileSize ) + ((Settings.tileSize - getHeight()) / 2);
        double optimalX = ( (int)(getLayoutX() / Settings.tileSize) * Settings.tileSize ) + ((Settings.tileSize - getWidth()) / 2);
        if( (direction == 0 || direction == 2) && getLayoutX() != optimalX)
            setLayoutX(optimalX);
        if( (direction == 1 || direction == 3) && getLayoutY() != optimalY)
            setLayoutY(optimalY);

        // </editor-fold>

        // <editor-fold desc="Move">

        if(collisionDetection()){
            isMoving = true;
            setLayoutX(getLayoutX() + x);
            setLayoutY(getLayoutY() + y);
        }else
            isMoving = false;

        // </editor-fold>

        // <editor-fold desc="Teleport">

        if(getLayoutX() < 0 - getWidth()){
            setLayoutX(Settings.gameWidth);
        }
        if(getLayoutX() > Settings.gameWidth){
            setLayoutX(0 - getWidth());
        }

        // </editor-fold>
    }
    boolean collisionDetection(){
        boolean returnData = true;
        double x = 0;
        double y = 0;

        // <editor-fold desc="Collision and pickup">

        for(int i = 0; i < MapGenerator.mapElements.size(); i++){
            // Predicted movement
            if(direction == 0)
                y = -((Settings.tileSize - getHeight()) / 2);
            if(direction == 2)
                y = ((Settings.tileSize - getHeight()) / 2);
            if(direction == 1)
                x = ((Settings.tileSize - getWidth()) / 2);
            if(direction == 3)
                x = -((Settings.tileSize - getWidth()) / 2);

            // Check collision for pickup
            if(MapGenerator.mapElements.get(i).getBoundsInParent().intersects(getLayoutX(),getLayoutY(),getWidth(),getHeight())){
                if(MapGenerator.mapElements.get(i).getId().equals("point")){
                    Settings.score += Settings.pintForPoint; // Add points
                    Settings.groupGame.getChildren().remove(MapGenerator.mapElements.get(i)); // Remove from view
                    MapGenerator.mapElements.remove(i); // Remove from list
                }else if(MapGenerator.mapElements.get(i).getId().equals("power")){
                    Settings.score += Settings.pintForPower;// Add points
                    Settings.powerUpTimeLeft = Settings.powerUpTime; // Set PowerUp
                    Settings.groupGame.getChildren().remove(MapGenerator.mapElements.get(i)); // Remove from view
                    MapGenerator.mapElements.remove(i); // Remove from list
                }
            }

            // Check collision for movement
            if(MapGenerator.mapElements.get(i).getBoundsInParent().intersects(getLayoutX() + x,getLayoutY() + y,getWidth(),getHeight())){
                if(MapGenerator.mapElements.get(i).getId().equals("wall") || MapGenerator.mapElements.get(i).getId().equals("enemyWall"))
                    returnData = false;
            }
        }

        // </editor-fold>

        // <editor-fold desc="Change direction">

        x = 0;
        y = 0;
        if(direction != requestedDirection){
            if(!isMoving || requestedDirection == 0 && direction == 2 || requestedDirection == 2 && direction == 0 ||
                    requestedDirection == 1 && direction == 3 || requestedDirection == 3 && direction == 1){
                direction = requestedDirection;
            }else{
                if(requestedDirection == 0)
                    y = -((Settings.tileSize - getHeight()) / 2);
                if(requestedDirection == 2)
                    y = ((Settings.tileSize - getHeight()) / 2);
                if(requestedDirection == 1)
                    x = ((Settings.tileSize - getWidth()) / 2);
                if(requestedDirection == 3)
                    x = -((Settings.tileSize - getWidth()) / 2);

                int find = 0;
                for(int i = 0; i < MapGenerator.mapElements.size(); i++){
                    if(MapGenerator.mapElements.get(i).getBoundsInParent().intersects(getLayoutX() + x,getLayoutY() + y,getWidth(),getHeight())){
                        if(MapGenerator.mapElements.get(i).getId().equals("wall") || MapGenerator.mapElements.get(i).getId().equals("enemyWall"))
                            find++;
                    }
                }
                if(find == 0)
                    direction = requestedDirection;
            }
        }

        // </editor-fold>

        return returnData;
    }
    void animation(){
        if(!isMoving)
            return;

        if(!isDead){
            if(animationFrame + 1 >= 3)
                animationFrame = 0;
            else
                animationFrame++;
        }else{
            if(animationFrame + 1 >= 7){
                    // Destroy PacMan
                Settings.groupGame.getChildren().remove(this);
                Settings.pacman = null;
                t.stop();
                t2.stop();
                GuiHandler.endGame();
            }
            else
                animationFrame++;
        }
    }
    void draw(){
        gc.clearRect(0,0, getWidth(), getHeight()); // Clear canvas
        gc.drawImage(pacmans[animationFrame], 0, 0, getWidth(), getHeight()); // Draw PacMan
    }

}
