package pacman;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class Developer {

    Timeline t;
    Button devSqr = new Button();

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

        // TODO: On Click check collision,
        // TODO: if not wall, place developer targetPoint

        t = new Timeline(new KeyFrame(Duration.millis(1), e->{
            handleMouse();
        }));
        t.setCycleCount(Animation.INDEFINITE);
        t.play();
    }

    void handleMouse() {

        devSqr.setLayoutX(Settings.hoverPoint.getX() * Settings.tileSize);
        devSqr.setLayoutY(Settings.hoverPoint.getY() * Settings.tileSize);

        int find = 0;

        for (int i = 0; i < MapGenerator.mapElements.size(); i++) {
            if (MapGenerator.mapElements.get(i).getBoundsInParent().intersects(
                    Settings.hoverPoint.getX() * Settings.tileSize + Settings.tileSize / 2, Settings.hoverPoint.getY() * Settings.tileSize + Settings.tileSize / 2,
                    1, 1
            )) {
                if (MapGenerator.mapElements.get(i).getId().equals("wall"))
                    find++;
            }
        }

        if (find == 0)
            Settings.canTarget = true;
        else
            Settings.canTarget = false;

        if (Settings.canTarget)
            devSqr.setId("targetB");
        else
            devSqr.setId("devB");

    }
}
