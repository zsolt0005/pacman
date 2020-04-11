package pacman;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MapGenerator {
    static List<Button> mapElements = new ArrayList<>();

    public static void  start(){
        // Generates the map and all map elements
        generateMap();
    }

    static void generateMap(){
            // Clear map
        Settings.groupGame.getChildren().removeAll();
        mapElements.clear();
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
                "     xpxx xxx  xxx xxpx     ",
                "xxxxxxpxx x      x xxpxxxxxx",
                "ppppppp   x      x   ppppppp",
                "xxxxxxpxx x      x xxpxxxxxx",
                "     xpxx xxxxxxxx xxpx     ",
                "     xpxx          xxpx     ",
                "     xpxx  xxxxxx  xxpx     ",
                "xxxxxxpxx  xxxxxx  xxpxxxxxx",
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

            // Apply map elements based on the blueprint
        for(int y = 0; y < mapY.length; y++){
            for(int x = 0; x < mapY[y].length(); x++){
                    // Place wall
                if(mapY[y].charAt(x) == 'x'){
                    Button b = new Button();
                    b.setPrefWidth(Settings.tileSize);
                    b.setPrefHeight(Settings.tileSize);
                    b.setLayoutX(x * Settings.tileSize);
                    b.setLayoutY(y * Settings.tileSize);
                    b.setFocusTraversable(false);
                    b.setId("wall");
                    mapElements.add(b);
                }
                    // Place point
                if(mapY[y].charAt(x) == 'p'){
                    Button p = new Button();
                    ImageView iv_cache = new ImageView(point);
                    iv_cache.setFitWidth(Settings.tileSize / 4);
                    iv_cache.setFitHeight(Settings.tileSize / 4);
                    p.setGraphic(iv_cache);
                    p.setPrefWidth(Settings.tileSize);
                    p.setPrefHeight(Settings.tileSize);
                    p.setLayoutX(x * Settings.tileSize);
                    p.setLayoutY(y * Settings.tileSize);
                    p.setFocusTraversable(false);
                    p.setId("point");
                    mapElements.add(p);
                }
                    // Place powerUp
                if(mapY[y].charAt(x) == 'u'){
                    Button u = new Button();
                    ImageView iv_cache = new ImageView(power);
                    iv_cache.setFitWidth(Settings.tileSize / 2);
                    iv_cache.setFitHeight(Settings.tileSize / 2);
                    u.setGraphic(iv_cache);
                    u.setPrefWidth(Settings.tileSize);
                    u.setPrefHeight(Settings.tileSize);
                    u.setLayoutX(x * Settings.tileSize);
                    u.setLayoutY(y * Settings.tileSize);
                    u.setFocusTraversable(false);
                    u.setId("power");
                    mapElements.add(u);
                }
            }
        }

            // Add buttons to view
        Settings.groupGame.getChildren().addAll(mapElements);
    }

}
