package pacman;

import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
                "xxxxxxpxxxxx xx xxxxxpxxxxxx",
                "     xpxxxxx xx xxxxxpx     ",
                "     xpxx          xxpx     ",
                "     xpxx xxxggxxx xxpx     ",
                "xxxxxxpxx x      x xxpxxxxxx",
                "ppppppp   x      x   ppppppp",
                "xxxxxxpxx x      x xxpxxxxxx",
                "     xpxx xxxxxxxx xxpx     ",
                "     xpxx          xxpx     ",
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
                    // Place enemy walkable wall
                if(mapY[y].charAt(x) == 'g'){
                    b.setPrefWidth(Settings.tileSize);
                    b.setPrefHeight(Settings.tileSize);
                    b.setLayoutX(x * Settings.tileSize);
                    b.setLayoutY(y * Settings.tileSize);
                    b.setFocusTraversable(false);
                    b.setId("enemyWall");
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

    static Button generateMapElement(int x, int y, String id){

        Button b = new Button();
        b.setPrefWidth(Settings.tileSize);
        b.setPrefHeight(Settings.tileSize);
        b.setLayoutX(x * Settings.tileSize);
        b.setLayoutY(y * Settings.tileSize);
        b.setFocusTraversable(false);
        b.setId(id);

        return b;
    }
}
