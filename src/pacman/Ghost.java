package pacman;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;
import pacman.AStar.Walkable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Ghost extends Canvas {

    GraphicsContext gc;
    Timeline t; // Timeline for draw
    Timeline t2; // Timeline for animation
    Timeline t3; // For random new direction
    public Point2D position; // Position

    int direction = 1; // 0-UP 1-RIGHT 2-DOWN 3-LEFT
    int requestedDirection = 1; // For direction change

    boolean isDead = false; // alive/dead state
    boolean isMoving = false; // moving state

    // PathFinding
    List<Walkable> path = new ArrayList<>();

    // For animation
    int animationFrame = 0;
    List<Image[]> ghosts = new ArrayList<>();

    public Ghost(){
        super(Settings.tileSize * 0.7, Settings.tileSize * 0.7);
        gc = getGraphicsContext2D();
        setId("ghost");

        // <editor-fold desc="Utilize images">

            // Random color
        int rnd = ThreadLocalRandom.current().nextInt(0, 4);
        ghosts.add(Settings.ghostsImg.get(rnd)); // Ghost
        ghosts.add(Settings.ghostsImg.get(Settings.ghostsImg.size()-1)); // Dead animation

        // </editor-fold>

        setLayoutX( (15 * Settings.tileSize) + ( (Settings.tileSize - getWidth() ) / 2) );
        setLayoutY( (11 * Settings.tileSize) + ( (Settings.tileSize - getHeight() ) / 2) );

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

        // If PathFinding is disabled
        if(Settings.difficulty == 0){

            t3 = new Timeline(new KeyFrame(Duration.millis(100),e->{
                if(direction != requestedDirection)
                    return;

                int newDirection = ThreadLocalRandom.current().nextInt(0, 4);
                if(direction == 0 || direction == 2)
                    while (newDirection == 0 || newDirection == 2)
                        newDirection = ThreadLocalRandom.current().nextInt(0, 4);

                if(direction == 1 || direction == 3)
                    while (newDirection == 1 || newDirection == 3)
                        newDirection = ThreadLocalRandom.current().nextInt(0, 4);

                requestedDirection = newDirection;
            }));
            t3.setCycleCount(Animation.INDEFINITE);
            t3.play();

        }

        // </editor-fold>

            // Add to view
        Settings.groupGame.getChildren().add(this);
    }

    void movement(){
        position = new Point2D( (int)(getLayoutX() / Settings.tileSize ), (int)(getLayoutY() / Settings.tileSize) );
        double x = 0;
        double y = 0;

        // <editor-fold desc="Move (Difficulty 0)">

        if(Settings.difficulty == 0){

            // <editor-fold desc="Set speed">
            if(direction == 0)
                y = -Settings.speed;
            if(direction == 1)
                x = Settings.speed;
            if(direction == 2)
                y = Settings.speed;
            if(direction == 3)
                x = -Settings.speed;

            // </editor-fold>

            if(collisionDetection()){
                isMoving = true;
                setLayoutX(getLayoutX() + x);
                setLayoutY(getLayoutY() + y);
            }else
                isMoving = false;
        }

        // </editor-fold>

        // <editor-fold desc="Move (Difficulty 1 or 2)">

        if(Settings.difficulty != 0){

            if(path.size() > 0){
                x = 0;
                y = 0;
                // Move
                    // Set x and y speed
                double pathX = path.get(0).realPosition.getX() + ((Settings.tileSize - getWidth()) / 2);
                double pathY = path.get(0).realPosition.getY()+ ((Settings.tileSize - getHeight()) / 2);
                double myX = (double) Math.round(getLayoutX() * 100) / 100;
                double myY = (double) Math.round(getLayoutY() * 100) / 100;

                double distX = (double) Math.round((pathX - myX) * 100) / 100;
                double distY = (double) Math.round((pathY - myY) * 100) / 100;

                if(distX > 0){
                    x = Settings.speed;
                    requestedDirection = 1;
                }
                if(distX < 0){
                    x = -Settings.speed;
                    requestedDirection = 3;
                }
                if(distY > 0){
                    y = Settings.speed;
                    requestedDirection = 2;
                }
                if(distY < 0){
                    y = -Settings.speed;
                    requestedDirection = 0;
                }

                if(x == 0 && y == 0)
                    path.remove(0);

                System.out.println(myX + " -> " + pathX + " : " + distX + "(" + x + ") -- " + direction );
                System.out.println(myY + " -> " + pathY + " : " + distY + "(" + y + ") -- " + direction );

                    // Move the ghost
                if(collisionDetection()){
                    isMoving = true;
                    setLayoutX(getLayoutX() + x);
                    setLayoutY(getLayoutY() + y);
                }else
                    isMoving = false;

            }

            // Get path
                // Developer build
            if(Settings.devBuild && Developer.lastPath.size() > 0){
                path.clear();
                for (Walkable w : Developer.lastPath)
                    path.add(w);
            }

            // TODO: Get path for difficulty 1 and 2 with no developer

        }

        // </editor-fold>

        // <editor-fold desc="Center Ghost">

        double optimalY = ( (int)(getLayoutY() / Settings.tileSize) * Settings.tileSize ) + ((Settings.tileSize - getHeight()) / 2);
        double optimalX = ( (int)(getLayoutX() / Settings.tileSize) * Settings.tileSize ) + ((Settings.tileSize - getWidth()) / 2);
        if( (direction == 0 || direction == 2) && getLayoutX() != optimalX)
            setLayoutX(optimalX);
        if( (direction == 1 || direction == 3) && getLayoutY() != optimalY)
            setLayoutY(optimalY);

        // </editor-fold>

        // <editor-fold desc="Teleport">

        if(getLayoutX() < 0 - getWidth()){
            setLayoutX(Settings.gameWidth - Settings.tileSize);
        }
        if(getLayoutX() > Settings.gameWidth){
            setLayoutX(0);
        }

        // </editor-fold>
    }

    boolean collisionDetection(){
        boolean returnData = true;
        double x = 0;
        double y = 0;
        int myX = (int)(getLayoutX() / Settings.tileSize);
        int myY = (int)(getLayoutY() / Settings.tileSize);

        // <editor-fold desc="Collision">

        // Check collision for pickup
        if(Settings.pacman != null)
            if(Settings.pacman.getBoundsInParent().intersects(getLayoutX(),getLayoutY(),getWidth(),getHeight()))
                Settings.pacman.isDead = true;

        // Predicted movement
        if(direction == 0)
            y = -((Settings.tileSize - getHeight()) / 2);
        if(direction == 2)
            y = ((Settings.tileSize - getHeight()) / 2);
        if(direction == 1)
            x = ((Settings.tileSize - getWidth()) / 2);
        if(direction == 3)
            x = -((Settings.tileSize - getWidth()) / 2);

        // Sorry for _ variables
        for(int _y = myY - 1; _y <= myY + 1; _y++){
            for(int _x = myX - 1; _x <= myX + 1; _x++){

                if(_x >= MapGenerator.mapElements[_y].length || _x < 0){
                    return true;
                }

                // Check collision for movement
                if(MapGenerator.mapElements[_y][_x].getBoundsInParent().intersects(getLayoutX() + x,getLayoutY() + y,getWidth(),getHeight()))
                    if(MapGenerator.mapElements[_y][_x].getId().equals("wall"))
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

                for(int _y = myY - 1; _y <= myY + 1; _y++) {
                    for (int _x = myX - 1; _x <= myX + 1; _x++) {
                        if(MapGenerator.mapElements[_y][_x].getBoundsInParent().intersects(getLayoutX() + x,getLayoutY() + y,getWidth(),getHeight()))
                            if(MapGenerator.mapElements[_y][_x].getId().equals("wall") || MapGenerator.mapElements[_y][_x].getId().equals("enemyWall"))
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
        if(animationFrame + 1 >= 3)
            animationFrame = 0;
        else
            animationFrame++;
    }

    void draw(){
        gc.clearRect(0,0, getWidth(), getHeight()); // Clear canvas
        gc.drawImage(ghosts.get( (isDead ? 1 : 0) )[animationFrame], 0, 0, getWidth(), getHeight()); // Draw PacMan
    }

    // TODO: A*

}
