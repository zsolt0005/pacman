package pacman.AStar;

import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import pacman.MapGenerator;
import pacman.Settings;

import java.util.ArrayList;
import java.util.List;

public class Walkable extends Button {

    public Point2D position; // Position

    public Walkable parent; // Parent node

    public int gCost; // Movement cost from the start node
    public int hCost; // Movement cost from the end node

    public int fCost(){
        return gCost + hCost; // Combined cost for decision making
    }

    public Walkable(int posX, int posY){
        this.position = new Point2D(posX, posY); // Create walkable path and store its position on the map
    }

    public List<Walkable> getNeighbours(){
        List<Walkable> neighbours = new ArrayList<>(); // New list that will contain all the neighbours

        // Loop in 3x3 space around this node
        for(int y = -1; y <= 1; y++)
            for(int x = -1; x <= 1; x++){
                if( (y == 0 && x == 0) || (y == 1 && x == 1) || (y == 1 && x == -1) || (y == -1 && x == 1) || (y == -1 && x == -1) )
                    continue; // If x=0 & y=0 skip because its this node

                // Check if inside the map
                int checkX = (int)position.getX() + x;
                int checkY = (int)position.getY() + y;

                if(checkX >= 0 && checkX < Settings.xTileCount && checkY >= 0 && checkY < Settings.yTileCount){
                    if(MapGenerator.mapElements[checkY][checkX].getId() != "wall")
                        neighbours.add((Walkable)MapGenerator.mapElements[checkY][checkX]);
                }
            }

        // Return neighbours
        return neighbours;
    }

}
