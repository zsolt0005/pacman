package pacman;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


public class GuiHandler{

    static List<Label> labels = new ArrayList<>();
    static Timeline t_timeHandle;
    static VBox uiHolder = new VBox();
    static HBox healthHolder = new HBox();
    static Label status; // Status label (Press ENTER to start, Paused!....)

    static Image pac = new Image("file:img/PacMan/3.png");

    static int timeHelper = 0; // Because with this method i dont have to create new timeline with 1 sec duration

    public static void start(){

        prepare(); // Prepare UI

        // <editor-fold desc="Setup timer">

        t_timeHandle = new Timeline(new KeyFrame(Duration.millis(100), e->{
            timeHandle();
            styleHandle();
        }));
        t_timeHandle.setCycleCount(Animation.INDEFINITE);
        t_timeHandle.play();

        // </editor-fold>
    }

    public static void endGame(){
        Settings.isPaused = true;
        Settings.isGameOver = true;
    }

    // Handle actions related to time
    static void timeHandle(){
            // Time handler
        timeHelper++;
        if(timeHelper > 10){
            if( Settings.isStarted && !Settings.isGameOver && !Settings.isPaused )
                Settings.time++;
            timeHelper = 0;
        }
            // Blinking status text
        if(timeHelper > 5)
            status.setVisible(false);
        else
            status.setVisible(true);

            // Update all UI text
        labels.get(1).setText(Settings.hiScore + "");
        labels.get(3).setText(Settings.score + "");
        labels.get(5).setText(Settings.time + "");

            // Status label handler
        if(!Settings.isStarted){
            status.setText("Press ENTER to start");
            status.setLayoutX( (Settings.gameWidth / 2.0) - (status.getBoundsInParent().getWidth() / 2) );
            status.setLayoutY( (Settings.gameHeight / 2.15) - (status.getBoundsInParent().getHeight() / 2) );
        }
        else if(Settings.isPaused && !Settings.isGameOver){
            status.setText("Paused!");
            status.setLayoutX( (Settings.gameWidth / 2.0) - (status.getBoundsInParent().getWidth() / 2) );
            status.setLayoutY( (Settings.gameHeight / 2.15) - (status.getBoundsInParent().getHeight() / 2) );
        }
        else if(Settings.isGameOver){
            status.setText("Press ENTER to restart");
            status.setLayoutX( (Settings.gameWidth / 2.0) - (status.getBoundsInParent().getWidth() / 2) );
            status.setLayoutY( (Settings.gameHeight / 2.15) - (status.getBoundsInParent().getHeight() / 2) );
        }
        else
            status.setText("");

        // Health indicator
        if(healthHolder.getChildren().size() > Settings.health)
            healthHolder.getChildren().remove(healthHolder.getChildren().get(healthHolder.getChildren().size() - 1));

        // EndGame handler
        if(Settings.health <= 0)
            endGame();
        if(Settings.score >= 2670){

            int pointsLeft = 0;
            for(int y = 0; y < MapGenerator.mapElements.length; y++) {
                for (int x = 0; x < MapGenerator.mapElements[y].length; x++) {
                    if (MapGenerator.mapElements[y][x].getId() == "point" || MapGenerator.mapElements[y][x].getId() == "power")
                        pointsLeft++;
                }
            }
            if(pointsLeft == 0)
                endGame();

        }
    }

    // Update UI size
    static void styleHandle(){
            // Calculate font
        Settings.fontSize = 12 + (int) ((Settings.scene.getWidth() * Settings.fontMultiplier));
            // Set UIHolder size
        uiHolder.setPrefWidth(Settings.uiWidth);
        uiHolder.setPrefHeight(Settings.gameHeight);
            // Apply style
        for(int i = 0; i < labels.size(); i++){
            if(i % 2 == 0){
                labels.get(i).setPadding(new Insets(Settings.gameHeight * 0.1, 0, 0, 0));
                labels.get(i).setTextFill(Color.RED);
            }
            if(i == labels.size() - 1)
                labels.get(i).setStyle("-fx-font-size: " + Settings.fontSize / 1.5);
            else
                labels.get(i).setStyle("-fx-font-size: " + Settings.fontSize);
        }

            // Set status label size
        status.setStyle("-fx-text-fill: red; -fx-font-size: " + Settings.fontSize);

            // Health indicator
        healthHolder.setAlignment(Pos.BOTTOM_CENTER);
        healthHolder.setPrefWidth(Settings.uiWidth);
        healthHolder.setLayoutY(Settings.gameHeight * 0.9);

        for(int i = 0; i < Settings.health; i++){
            if(healthHolder.getChildren().size() < Settings.health){
                healthHolder.getChildren().add(new ImageView(pac));
                i = 0;
            }else{
                ImageView iv = new ImageView(pac);
                iv.setPreserveRatio(true);
                iv.setFitWidth(Settings.uiWidth * 0.05);
                healthHolder.getChildren().set(i, iv);
            }
        }

    }

    // Prepare UI elements
    static void prepare(){
            // Calc & apply UI width
        Settings.uiWidth = (int)( (Settings.scene.getWidth() - Settings.gameWidth - (Settings.scene.getWidth() * Settings.padding) * 3) ); // UI width
        Settings.groupUi.prefWidth(Settings.uiWidth);
        uiHolder.setAlignment(Pos.CENTER);

            // Get font Size
        Settings.fontSize = (int) ((Settings.scene.getWidth() * Settings.fontMultiplier));

            // Prepare labels
        labels.add(new Label("HI-SCORE"));
        labels.add(new Label("0"));
        labels.add(new Label("SCORE"));
        labels.add(new Label("0"));
        labels.add(new Label("TIME"));
        labels.add(new Label("0"));
        labels.add(new Label("CONTROLS"));
        labels.add(new Label("Movement: WASD\nPause: P\nExit: ESC"));

            // Prepare status label
        status = new Label("Press ENTER to start");
        status.setAlignment(Pos.CENTER);

        // Add to view
        Settings.groupGame.getChildren().add(status);
        uiHolder.getChildren().addAll(labels);
        Settings.groupUi.getChildren().addAll(uiHolder, healthHolder);
        for(int i = 0; i < Settings.health; i++)
            healthHolder.getChildren().add(new ImageView(pac));
    }

}
