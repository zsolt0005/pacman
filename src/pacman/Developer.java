package pacman;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.util.Duration;
import pacman.AStar.Pathfinding;
import pacman.AStar.Walkable;

import java.util.ArrayList;
import java.util.List;

public class Developer {

    Timeline t;
    Timeline t2; // PathFinding
    Button devSqr = new Button();
    List<Walkable> lastPath = new ArrayList<>();

    public Developer(){
        Settings.groupGame.setOnMouseMoved(e->{
            Settings.hoverPoint = new Point2D(
                    (int)( (e.getSceneX() - Settings.groupGame.getLayoutX()) / Settings.tileSize),
                    (int)( (e.getSceneY() - Settings.groupGame.getLayoutY()) / Settings.tileSize)
            );
        });
        Settings.groupGame.setOnMouseEntered(e->{
            Settings.hoverPoint = new Point2D(
                    (int)( (e.getSceneX() - Settings.groupGame.getLayoutX()) / Settings.tileSize),
                    (int)( (e.getSceneY() - Settings.groupGame.getLayoutY()) / Settings.tileSize)
            );
        });
        Settings.groupGame.setOnMouseExited(e->{
            Settings.hoverPoint = new Point2D(
                    (int)( (e.getSceneX() - Settings.groupGame.getLayoutX()) / Settings.tileSize),
                    (int)( (e.getSceneY() - Settings.groupGame.getLayoutY()) / Settings.tileSize)
            );
        });

        devSqr.setPrefWidth(Settings.tileSize);
        devSqr.setPrefHeight(Settings.tileSize);
        devSqr.setFocusTraversable(false);
        devSqr.setId("devB");

        Settings.groupGame.getChildren().add(devSqr);

        t = new Timeline(new KeyFrame(Duration.millis(1), e->{
            handleMouse();
        }));
        t.setCycleCount(Animation.INDEFINITE);
        t.play();

        t2 = new Timeline(new KeyFrame(Duration.millis(100), e->{
            if(Settings.difficulty == 0 || Settings.ghosts[0] == null)
                return;

            testPathFinding();
        }));
        t2.setCycleCount(Animation.INDEFINITE);
        t2.play();
    }

    void handleMouse() {

        if(Settings.hoverPoint.getY() > Settings.yTileCount - 1)
            return;
        if(Settings.hoverPoint.getX() > Settings.xTileCount - 1)
            return;

        devSqr.setLayoutX(Settings.hoverPoint.getX() * Settings.tileSize);
        devSqr.setLayoutY(Settings.hoverPoint.getY() * Settings.tileSize);

        int find = 0;
        if (MapGenerator.mapElements[(int)Settings.hoverPoint.getY()][(int)Settings.hoverPoint.getX()].getId().equals("wall"))
            find++;

        if (find == 0){
            Settings.canTarget = true;
            devSqr.setId("targetB");
        }
        else {
            Settings.canTarget = false;
            devSqr.setId("devB");
        }

    }

    void testPathFinding(){
        // Prepare the groups
        if(lastPath.size() > 0){
            Settings.groupGame.getChildren().removeAll(lastPath);
            lastPath.clear();
        }

        if(!Settings.canTarget)
            return;

        // Prepare visuals

        // Get ghost and hover position
        Walkable startNode = (Walkable) MapGenerator.mapElements[ ( (int)(Settings.ghosts[0].getLayoutY() / Settings.tileSize) ) ][( (int)(Settings.ghosts[0].getLayoutX() / Settings.tileSize) )];
        Walkable endNode = (Walkable) MapGenerator.mapElements[(int)Settings.hoverPoint.getY()][(int)Settings.hoverPoint.getX()];

        Pathfinding p = new Pathfinding(startNode, endNode);

        for (Walkable node : p.foundPath) {
            Walkable w = new Walkable((int)node.position.getX(), (int)node.position.getY());
            w.setId("path");
            w.setLayoutX(w.position.getX() * Settings.tileSize);
            w.setLayoutY(w.position.getY() * Settings.tileSize);
            w.setPrefWidth(Settings.tileSize);
            w.setPrefHeight(Settings.tileSize);
            lastPath.add(w);

            w.setOnMouseMoved(e->{
                Settings.hoverPoint = new Point2D(
                        (int)( (e.getSceneX() - Settings.groupGame.getLayoutX()) / Settings.tileSize),
                        (int)( (e.getSceneY() - Settings.groupGame.getLayoutY()) / Settings.tileSize)
                );
            });
        }

        // Show path
        Settings.groupGame.getChildren().addAll(lastPath);
    }
}
