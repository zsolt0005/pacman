package pacman.AStar;

import java.util.*;

public class Pathfinding {

    public List<Walkable> foundPath = new ArrayList<>();

    public Pathfinding(Walkable startNode, Walkable targetNode){
            // Create open and closed sets
        List<Walkable> openSet = new ArrayList<>(); // Open set is a list because it maintain the order of elements
        HashSet<Walkable> closedSet = new HashSet<>(); // Closed set is a HashSet for better performance and it dont need to be ordered

            // Add start node to open set
        openSet.add(startNode);

            // Loop for the pathFinding
        while (openSet.size() > 0){
            Walkable currentNode = openSet.get(0); // Set current node as the first element of the openSet
                // Find the lowest fCost or if equals lowest hCost of them
            for(int i = 1; i < openSet.size(); i++)
                if( (openSet.get(i).fCost() < currentNode.fCost()) || (openSet.get(i).fCost() == currentNode.fCost() && openSet.get(i).hCost < currentNode.hCost) )
                    currentNode = openSet.get(i);

            openSet.remove(currentNode); // Remove current node from the openSet
            closedSet.add(currentNode); // Add current node to the closedSet

            if(currentNode == targetNode){
                // Path find
                retracePath(startNode, targetNode);
                break;
            }

            // Loop all the neighbours of the current node
            for (Walkable neighbour : currentNode.getNeighbours()) {
                if(closedSet.contains(neighbour))
                    continue; // If the node is in the closed list skip that node

                int newMovementCostToNode = currentNode.gCost + getDistance(currentNode, neighbour);
                if(newMovementCostToNode < neighbour.gCost || !openSet.contains(neighbour)){
                    neighbour.gCost = newMovementCostToNode;
                    neighbour.hCost = getDistance(neighbour, targetNode);
                    neighbour.parent = currentNode;
                    if(!openSet.contains(neighbour))
                        openSet.add(neighbour);
                }
            }

        }
    }

    int getDistance(Walkable from, Walkable to){
            // Get distances
        int distanceX = (int)Math.abs( from.position.getX() - to.position.getX() );
        int distanceY = (int)Math.abs( from.position.getY() - to.position.getY() );

        if(distanceX > distanceY)
            return 14 * distanceY + 10 * (distanceX - distanceY);
        else
            return 14 * distanceX + 10 * (distanceY - distanceX);
    }

    void retracePath(Walkable startNode, Walkable targetNode){
        List<Walkable> path = new ArrayList<>(); // List for the path
        Walkable currentNode = targetNode; // Current node is the end node because we have to trace the path backwards

        while (currentNode != startNode){
            path.add(currentNode);
            currentNode = currentNode.parent;
        }

        Collections.reverse(path); // Because the path is traced backwards we have to reverse the path

        foundPath = path; // save path
    }
}
