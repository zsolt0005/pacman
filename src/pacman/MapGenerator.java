package pacman;

import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pacman.AStar.Walkable;

import java.util.concurrent.ThreadLocalRandom;

public class MapGenerator {
    static Button[][] mapElements;

    public static void  start(){
        // Generates the map and all map elements
        generateMap();
    }

    static void generateMap(){
            // Clear map
        Settings.groupGame.getChildren().removeAll();
            // Cache images
        Image point = new Image("file:img/onMap/point.png");
        Image power = new Image("file:img/onMap/powerUp.png");

            // Map BLUEPRINT
        String[] mapY = {
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxx",
                "xupppppppppppxxppppppppppppx",
                "xpxxxxpxxxxxpxxpxxxxxpxxxxpx",
                "xpxxxxpxxxxxpxxpxxxxxpxxxxpx",
                "xpxxxxpxxxxxpxxpxxxxxpxxxxpx",
                "xppppppppppppppppppppppppppx",
                "xpxxxxpxxpxxxxxxxxpxxpxxxxpx",
                "xpxxxxpxxpxxxxxxxxpxxpxxxxpx",
                "xppppppxxpppuxxppppxxpppppux",
                "xxxxxxpxxxxxpxxpxxxxxpxxxxxx",
                "     xpxxxxxpxxpxxxxxpx     ",
                "     xpx             px     ",
                "     xpx xxx xxx xxx xx     ",
                "xxxxxxpx x x x x xp  pxxxxxx",
                "pppppppp xxx xxx xpxxppppppp",
                "xxxxxxpx xpp xpx xp  pxxxxxx",
                "     xpx xpx xpx xxx xx     ",
                "     xpx             px     ",
                "     xpxx xxxxxxxx xxpx     ",
                "xxxxxxpxx xxxxxxxx xxpxxxxxx",
                "xpppppppppppuxxppppppppppppx",
                "xpxxxxpxxxxxpxxpxxxxxpxxxxpx",
                "xpxxxxpxxxxxpxxpxxxxxpxxxxpx",
                "xpppxxppppppppppppppppxxpppx",
                "xxxpxxpxxpxxxxxxxxpxxpxxpxxx",
                "xxxpxxpxxpxxxxxxxxpxxpxxpxxx",
                "xupppppxxppppxxppppxxppppppx",
                "xpxxxxxxxxxxpxxpxxxxxxxxxxpx",
                "xpxxxxxxxxxxpxxpxxxxxxxxxxpx",
                "xpppppppppppppppppppppppppux",
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxx"
        };

        mapElements = new Button[mapY.length][mapY[0].length()];

            // Apply map elements based on the blueprint
        for(int y = 0; y < mapY.length; y++){
            for(int x = 0; x < mapY[y].length(); x++){
                Button b = new Button();
                    // Place wall
                if(mapY[y].charAt(x) == 'x'){
                    b.setPrefWidth(Settings.tileSize);
                    b.setPrefHeight(Settings.tileSize);
                    b.setLayoutX(x * Settings.tileSize);
                    b.setLayoutY(y * Settings.tileSize);
                    b.setFocusTraversable(false);
                    b.setId("wall");
                    mapElements[y][x] = b;
                    Settings.groupGame.getChildren().add(b);
                }
                    // Place point
                if(mapY[y].charAt(x) == 'p'){
                    ImageView iv_cache = new ImageView(point);
                    iv_cache.setFitWidth(Settings.tileSize / 4);
                    iv_cache.setFitHeight(Settings.tileSize / 4);
                    b.setGraphic(iv_cache);
                    b.setPrefWidth(Settings.tileSize);
                    b.setPrefHeight(Settings.tileSize);
                    b.setLayoutX(x * Settings.tileSize);
                    b.setLayoutY(y * Settings.tileSize);
                    b.setFocusTraversable(false);
                    b.setId("point");
                    mapElements[y][x] = b;
                    Settings.groupGame.getChildren().add(b);
                }
                    // Place powerUp
                if(mapY[y].charAt(x) == 'u'){
                    ImageView iv_cache = new ImageView(power);
                    iv_cache.setFitWidth(Settings.tileSize / 2);
                    iv_cache.setFitHeight(Settings.tileSize / 2);
                    b.setGraphic(iv_cache);
                    b.setPrefWidth(Settings.tileSize);
                    b.setPrefHeight(Settings.tileSize);
                    b.setLayoutX(x * Settings.tileSize);
                    b.setLayoutY(y * Settings.tileSize);
                    b.setFocusTraversable(false);
                    b.setId("power");
                    mapElements[y][x] = b;
                    Settings.groupGame.getChildren().add(b);
                }

                // Place enemy walkable wall
                if(mapY[y].charAt(x) == ' '){
                    b = new Walkable();
                    b.setPrefWidth(Settings.tileSize);
                    b.setPrefHeight(Settings.tileSize);
                    b.setLayoutX(x * Settings.tileSize);
                    b.setLayoutY(y * Settings.tileSize);
                    b.setFocusTraversable(false);
                    b.setId("empty");
                    mapElements[y][x] = b;
                    Settings.groupGame.getChildren().add(b);
                }

                // For developer build
                if(Settings.devBuild){
                    b.setOnMouseMoved(e->{
                        Settings.hoverPoint = new Point2D(
                                (int)( (e.getSceneX() - Settings.groupGame.getLayoutX()) / Settings.tileSize),
                                (int)( (e.getSceneY() - Settings.groupGame.getLayoutY()) / Settings.tileSize)
                        );
                    });
                }

            }
        }
    }

    static Walkable generateMapElement(int x, int y){

        Walkable b = new Walkable();
        b.setPrefWidth(Settings.tileSize);
        b.setPrefHeight(Settings.tileSize);
        b.setLayoutX(x * Settings.tileSize);
        b.setLayoutY(y * Settings.tileSize);
        b.setFocusTraversable(false);
        b.setId("empty");

        return b;
    }
}
