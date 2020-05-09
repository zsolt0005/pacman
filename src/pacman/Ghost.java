package pacman;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;
import pacman.AStar.Pathfinding;
import pacman.AStar.Walkable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Ghost extends Canvas {

    GraphicsContext gc;
    Timeline t; // Timeline for draw
    Timeline t2; // Timeline for animation
    Timeline t3; // For random new direction or for difficulty 2
    public Point2D position; // Position

    int direction = 1; // 0-UP 1-RIGHT 2-DOWN 3-LEFT
    int requestedDirection = 1; // For direction change

    double speed = Settings.speed;

    boolean isDead = false; // alive/dead state
    boolean haveDeadPath = false;
    boolean isMoving = false; // moving state

    // PathFinding
    List<Walkable> path = new ArrayList<>();
    int beforeCenter = 200;
    Walkable startNode;
    Walkable endNode;
    double percent = 0;

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
        ghosts.add(Settings.ghostsImg.get(Settings.ghostsImg.size()-2)); // Scared animation
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
        if(Settings.difficulty == 0 && !Settings.devBuild){

            t3 = new Timeline(new KeyFrame(Duration.millis(100),e->{

                if(isDead && !haveDeadPath){
                    if(speed != Settings.speed)
                        speed = Settings.speed;

                    path.clear();
                    // If dead, go back to spawn location
                    // Get ghost and spawn position
                    Walkable startNode = (Walkable) MapGenerator.mapElements[ ( (int)(position.getY()) ) ][( (int)(position.getX()) )];
                    Walkable endNode = (Walkable) MapGenerator.mapElements[11][15];

                    Pathfinding p = new Pathfinding(startNode, endNode);

                    path = p.foundPath;

                    haveDeadPath = true;
                    return;
                }

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

        }else if(Settings.difficulty == 1 && !Settings.devBuild){
            // Difficulty 1
            t3 = new Timeline(new KeyFrame(Duration.millis(500),e->{
                if(Settings.isPaused || isDead || Settings.devBuild)
                    return;

                if(!Settings.pacman.hasPowerUp){

                    if(percent > 20 && percent <= 50)
                        checkForPlayer(1);

                    if(percent > 50 && percent <= 90)
                        checkForPlayer(2);

                    if(percent > 90)
                        checkForPlayer(3);

                    return;
                }

                if(speed == Settings.speed)
                    speed = speed / 2;

                if(path.size() > 0)
                    return;

                // Random pick position
                int minX = (int)position.getX();
                int minY = (int)position.getY();
                int multiplierX = 0;
                int multiplierY = 0;
                int wantX = 0;
                int wantY = 0;

                // Get min

                if(getLayoutX() < Settings.pacman.getLayoutX())
                    multiplierX = -1;
                else
                    multiplierX = 1;

                if(getLayoutY() < Settings.pacman.getLayoutY())
                    multiplierY = -1;
                else
                    multiplierY = 1;

                int iterations = 0;
                while( !(MapGenerator.mapElements[wantY][wantX] instanceof Walkable)){
                    iterations++;
                    if(multiplierY > 0){
                        wantY = ThreadLocalRandom.current().nextInt(minY, MapGenerator.mapElements.length - 1);
                    }
                    else{
                        wantY = ThreadLocalRandom.current().nextInt(0, minY);
                    }

                    if(multiplierX > 0)
                        wantX = ThreadLocalRandom.current().nextInt(minX, MapGenerator.mapElements[0].length - 1);
                    else
                        wantX = ThreadLocalRandom.current().nextInt(0, minX);

                    if(iterations > MapGenerator.mapElements.length * 2){
                        wantX = 1;
                        wantY = 1;
                    }
                }

                if(endNode != null)
                    if(endNode.realPosition.getX() == wantX && endNode.realPosition.getY() == wantY)
                        return;

                startNode = (Walkable) MapGenerator.mapElements[ ( (int)Math.round(position.getY()) ) ][( (int)Math.round(position.getX()) )];
                endNode = (Walkable) MapGenerator.mapElements[wantY][wantX];

                Pathfinding p = new Pathfinding(startNode, endNode);
                path = p.foundPath;

            }));
            t3.setCycleCount(Animation.INDEFINITE);
            t3.play();

        }

        // </editor-fold>

            // Add to view
        Settings.groupGame.getChildren().add(this);
    }

    void movement(){
        position = new Point2D( (int)Math.round(getLayoutX() / Settings.tileSize ), (int)Math.round(getLayoutY() / Settings.tileSize) );
        double x = 0;
        double y = 0;

        // Follow path
        if( (Settings.difficulty != 0 && path.size() > 0) || (isDead && haveDeadPath && path.size() > 0) ){
            System.out.println("Following path");
            x = 0;
            y = 0;
            // Get path, position and distance
            double pathX = path.get(0).realPosition.getX() + ((Settings.tileSize - getWidth()) / 2);
            double pathY = path.get(0).realPosition.getY()+ ((Settings.tileSize - getHeight()) / 2);
            double myX = (double) Math.round(getLayoutX() * 100) / 100;
            double myY = (double) Math.round(getLayoutY() * 100) / 100;
            double distX = (double) Math.round((pathX - myX) * 100) / 100;
            double distY = (double) Math.round((pathY - myY) * 100) / 100;

            // Set speed
            if(distX > 0){
                x = speed;
                requestedDirection = 1;
            }
            if(distX < 0){
                x = -speed;
                requestedDirection = 3;
            }
            if(distY > 0){
                y = speed;
                requestedDirection = 2;
            }
            if(distY < 0){
                y = -speed;
                requestedDirection = 0;
            }

            // If cant move, center the closest point (x or y)
            if(x != 0 && y != 0){
                beforeCenter--;
                if(beforeCenter <= 0){
                    if(Math.abs(distX) > Math.abs(distY) ){
                        y = 0;
                        setLayoutY(( (int)(getLayoutY() / Settings.tileSize) * Settings.tileSize ) + ((Settings.tileSize - getHeight()) / 2));
                    }
                    else{
                        x = 0;
                        setLayoutX(( (int)(getLayoutX() / Settings.tileSize) * Settings.tileSize ) + ((Settings.tileSize - getWidth()) / 2));
                    }
                    beforeCenter = 200;
                }
            }

            System.out.println(x + " " + y);
            // Move the ghost
            if(collisionDetection(0)){
                isMoving = true;
                System.out.println("Moving....");
                setLayoutX(getLayoutX() + x);
                setLayoutY(getLayoutY() + y);
            }else{
                isMoving = false;
                System.out.println("Not moving");
                if(x != 0 || y != 0){
                    // Prevent stuck while dead (I don't know why it happened but it removes this bug)
                    setLayoutY(( (int)(getLayoutY() / Settings.tileSize) * Settings.tileSize ) + ((Settings.tileSize - getHeight()) / 2));
                    setLayoutX(( (int)(getLayoutX() / Settings.tileSize) * Settings.tileSize ) + ((Settings.tileSize - getWidth()) / 2));
                }
            }

            if(x == 0 && y == 0){
                path.remove(0);
                if(path.size() == 0)
                    if(isDead){
                        isDead = false;
                        haveDeadPath = false;
                    }
            }
        }

        // If dead
        if(isDead && !haveDeadPath){
            if(speed != Settings.speed)
                speed = Settings.speed;

            path.clear();
            // If dead, go back to spawn location
            // Get ghost and spawn position
            Walkable startNode = (Walkable) MapGenerator.mapElements[ ( (int)(position.getY()) ) ][( (int)(position.getX()) )];
            Walkable endNode = (Walkable) MapGenerator.mapElements[11][15];

            Pathfinding p = new Pathfinding(startNode, endNode);

            path = p.foundPath;

            haveDeadPath = true;
        }

        // Developer build
        if(!isDead && Settings.devBuild && Developer.lastPath.size() > 0){
            path.clear();
            path.addAll(Developer.lastPath);
        }

        // <editor-fold desc="Move (Difficulty 0)">

        if(Settings.difficulty == 0){

            // <editor-fold desc="Set speed">
            if(direction == 0)
                y = -speed;
            if(direction == 1)
                x = speed;
            if(direction == 2)
                y = speed;
            if(direction == 3)
                x = -speed;

            // </editor-fold>

            if(collisionDetection(0)){
                isMoving = true;
                setLayoutX(getLayoutX() + x);
                setLayoutY(getLayoutY() + y);
            }else
                isMoving = false;
        }

        // </editor-fold>

        // <editor-fold desc="Move (Difficulty 1)">

        if(Settings.difficulty != 0){

            // Get path
            if(!isDead && Settings.difficulty == 1 && !Settings.pacman.hasPowerUp && !Settings.devBuild){
                if(speed != Settings.speed)
                    speed = Settings.speed;

                // Pick location based how many % of all points player picked up
                // 0-20% -> Only random location pick
                // 21-50% -> If inline with player follow to the last seen point
                // 51-90% -> If player in range (12 blocks), follow through walls | or inline
                // 90 - 100% -> Follow with WallHack

                // <editor-fold desc="get points picked up">

                // Get % (All points: 269)
                percent = 0;
                for(int _y = 0; _y < MapGenerator.mapElements.length; _y++)
                    for(int _x = 0; _x < MapGenerator.mapElements[_y].length; _x++)
                        if(MapGenerator.mapElements[_y][_x].getId().equals("point") || MapGenerator.mapElements[_y][_x].getId().equals("power")){
                            percent = percent +  (100.0 / 269.0); // Percent of one point from all
                        }
                percent = 100 - percent; // To get % of points picked up

                // </editor-fold>

                // <editor-fold desc="get path">

                if(percent <= 90 && path.size() == 0){
                    // Random pick position
                    int wantX = 0;
                    int wantY = 0;

                    while( !(MapGenerator.mapElements[wantY][wantX] instanceof Walkable)){
                        wantY = ThreadLocalRandom.current().nextInt(0, MapGenerator.mapElements.length - 1);
                        wantX = ThreadLocalRandom.current().nextInt(0, MapGenerator.mapElements[0].length - 1);
                    }

                    startNode = (Walkable) MapGenerator.mapElements[ ( (int)Math.round(position.getY()) ) ][( (int)Math.round(position.getX()) )];
                    endNode = (Walkable) MapGenerator.mapElements[wantY][wantX];

                    Pathfinding p = new Pathfinding(startNode, endNode);
                    path = p.foundPath;
                }

                // </editor-fold>
            }

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

        collisionDetection(1);
    }

    boolean collisionDetection(int type){
        boolean returnData = true;
        double x = 0;
        double y = 0;
        int myX = (int)(getLayoutX() / Settings.tileSize);
        int myY = (int)(getLayoutY() / Settings.tileSize);

        if(type == 0){

            // <editor-fold desc="Collision">

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
        }

        if(type == 1){
            // Check collision with PacMan
            if(Settings.pacman != null && !isDead)
                if(Settings.pacman.getBoundsInParent().intersects(getLayoutX(),getLayoutY(),getWidth(),getHeight()))
                    if(Settings.pacman.hasPowerUp){
                        isDead = true;
                        path.clear();
                    }
                    else
                        Settings.pacman.isDead = true;
        }

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
        if(isDead)
            gc.drawImage(ghosts.get(2)[animationFrame], 0, 0, getWidth(), getHeight()); // Draw PacMan
        else
            gc.drawImage(ghosts.get( (Settings.pacman.hasPowerUp ? 1 : 0) )[animationFrame], 0, 0, getWidth(), getHeight()); // Draw PacMan

    }

    void checkForPlayer(int precision){

        boolean foundPath = false;

        // 21-50% -> If inline with player follow to the last seen point
        if(precision == 1 || precision == 2){

            // Check all directions while not wall or player
            // Filtrate direction based player position (If players position is lower than this objects, dont check anything higher)

            if(getLayoutX() < Settings.pacman.getLayoutX()){
                // Check right

                int offset = 1; // 1 because 0 is the same spot as the ghosts position

                while(true){

                    if(position.getX() + offset > MapGenerator.mapElements[0].length)
                        break;

                    if(MapGenerator.mapElements[(int) position.getY()][(int) position.getX() + offset].getId().equals("wall"))
                        break;

                    if(MapGenerator.mapElements[(int)position.getY()][(int)position.getX() + offset].getBoundsInParent().intersects(
                            Settings.pacman.getBoundsInParent()
                    )){
                        startNode = (Walkable) MapGenerator.mapElements[ ( (int)(position.getY()) ) ][( (int)(position.getX()) )];
                        endNode = (Walkable) MapGenerator.mapElements[(int)position.getY()][(int)position.getX() + offset];

                        Pathfinding p = new Pathfinding(startNode, endNode);
                        path = p.foundPath;

                        foundPath = true;

                        break;
                    }

                    offset++;
                }

            }else{
                // Check left

                int offset = 1; // 1 because 0 is the same spot as the ghosts position

                while(true){

                    if(position.getX() - offset < 0)
                        break;

                    if(MapGenerator.mapElements[(int) position.getY()][(int) position.getX() - offset].getId().equals("wall"))
                        break;

                    if(MapGenerator.mapElements[(int)position.getY()][(int)position.getX() - offset].getBoundsInParent().intersects(
                            Settings.pacman.getBoundsInParent()
                    )){
                        startNode = (Walkable) MapGenerator.mapElements[ ( (int)(position.getY()) ) ][( (int)(position.getX()) )];
                        endNode = (Walkable) MapGenerator.mapElements[(int)position.getY()][(int)position.getX() - offset];

                        Pathfinding p = new Pathfinding(startNode, endNode);
                        path = p.foundPath;
                        foundPath = true;
                        break;
                    }

                    offset++;
                }
            }

            if(getLayoutY() > Settings.pacman.getLayoutY() && !foundPath){
                // Check UP

                int offset = 1; // 1 because 0 is the same spot as the ghosts position

                while(true){

                    if(position.getY() - offset < 0 )
                        break;

                    if(MapGenerator.mapElements[(int) position.getY() - offset][(int) position.getX()].getId().equals("wall"))
                        break;

                    if(MapGenerator.mapElements[(int)position.getY() - offset][(int)position.getX()].getBoundsInParent().intersects(
                            Settings.pacman.getBoundsInParent()
                    )){
                        startNode = (Walkable) MapGenerator.mapElements[ ( (int)(position.getY()) ) ][( (int)(position.getX()) )];
                        endNode = (Walkable) MapGenerator.mapElements[(int)position.getY() - offset][(int)position.getX()];

                        Pathfinding p = new Pathfinding(startNode, endNode);
                        path = p.foundPath;

                        break;
                    }

                    offset++;
                }

            }else{
                // Check DOWN

                int offset = 1; // 1 because 0 is the same spot as the ghosts position

                while(true){

                    if(position.getY() + offset > MapGenerator.mapElements.length)
                        break;

                    if(MapGenerator.mapElements[(int) position.getY() + offset][(int) position.getX()].getId().equals("wall"))
                        break;

                    if(MapGenerator.mapElements[(int)position.getY() + offset][(int)position.getX()].getBoundsInParent().intersects(
                            Settings.pacman.getBoundsInParent()
                    )){
                        startNode = (Walkable) MapGenerator.mapElements[ ( (int)(position.getY()) ) ][( (int)(position.getX()) )];
                        endNode = (Walkable) MapGenerator.mapElements[(int)position.getY() + offset][(int)position.getX()];

                        Pathfinding p = new Pathfinding(startNode, endNode);
                        path = p.foundPath;

                        break;
                    }

                    offset++;
                }
            }
        }

        // 51-90% -> If player in range (12 blocks), follow through walls || or inline
        if(precision == 2 && !foundPath){

            for(int x = -6; x <= 6; x++){
                for(int y = -6; y <= 6; y++){
                    if(position.getX() + x > MapGenerator.mapElements[0].length - 1 || position.getX() + x < 0 ||
                       position.getY() + y > MapGenerator.mapElements.length - 1 || position.getY() + y < 0)
                        continue;

                    if(x == 0 && y == 0)
                        continue; // Dont check itself

                    if(MapGenerator.mapElements[(int)position.getY() + y][(int)position.getX() + x].getBoundsInParent().intersects(
                            Settings.pacman.getBoundsInParent()
                    )){
                        startNode = (Walkable) MapGenerator.mapElements[ ( (int)(position.getY()) ) ][( (int)(position.getX()) )];
                        endNode = (Walkable) MapGenerator.mapElements[(int)position.getY() + y][(int)position.getX() + x];

                        Pathfinding p = new Pathfinding(startNode, endNode);
                        path = p.foundPath;

                        break;
                    }
                }
            }

        }

        // Follow player everywhere
        if(precision == 3){
            path.clear();
            startNode = (Walkable) MapGenerator.mapElements[ ( (int)(position.getY()) ) ][( (int)(position.getX()) )];
            endNode = (Walkable) MapGenerator.mapElements[(int)Math.round(Settings.pacman.getLayoutY() / Settings.tileSize)][(int)Math.round(Settings.pacman.getLayoutX() / Settings.tileSize)];

            Pathfinding p = new Pathfinding(startNode, endNode);
            path = p.foundPath;
        }
    }

}

// TODO: If dead and collides again, ghost stops moving