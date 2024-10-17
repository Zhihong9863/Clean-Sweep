package com.cleanSweep.backend.application;

import com.cleanSweep.backend.domain.Cell;
import com.cleanSweep.backend.domain.FloorMap;
import com.cleanSweep.backend.common.Direction;
import com.cleanSweep.backend.infrastructure.ActivityLogger;
import com.cleanSweep.frontend.visualization.FloorPlanVisualizer;
import com.cleanSweep.frontend.visualization.RobotVisualizer;
import javafx.scene.canvas.GraphicsContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;

//TODO: BFS algorithm (strange, because its moving looks like diagonal) -check for front end
//@Service
//public class NavigationService {
//
//    @Autowired
//    private FloorMap floorMap; // The map representing the floor layout
//
//    @Autowired
//    private DirtService dirtService; // Service responsible for cleaning dirt in cells
//
//    @Autowired
//    private BatteryService batteryService; // Service responsible for managing battery power
//
//    @Autowired
//    private SensorSimulatorService sensorSimulatorService; // Service that simulates sensors for detecting obstacles
//
//    @Autowired
//    private ActivityLogger activityLogger; // Logger to record navigation activities
//
//    private Deque<Cell> queue = new ArrayDeque<>(); // Queue to manage cells to visit in the navigation
//    private boolean isNavigationCompleted = false; // Flag to indicate if navigation is complete
//    private int currentX = 0; // Current x-coordinate of the robot
//    private int currentY = 0; // Current y-coordinate of the robot
//
//    // Start the navigation from a specified starting cell
//    public void startNavigation(int startX, int startY) {
//        if (!isNavigationCompleted && queue.isEmpty()) {
//            Cell startCell = floorMap.getCells()[startX][startY];
//            queue.add(startCell);
//            startCell.setVisited(true);
//
//            currentX = startX;
//            currentY = startY;
//            activityLogger.logMovement(startX, startY, "Start"); // Log the start of navigation
//        }
//    }
//
//    // Execute one step of navigation, updating the robot's position and cleaning if necessary
//    public void stepNavigation(GraphicsContext gc, FloorPlanVisualizer floorPlanVisualizer, RobotVisualizer robotVisualizer) {
//        if (queue.isEmpty() || isNavigationCompleted) {
//            return; // Stop if navigation is complete or there are no more cells to visit
//        }
//
//        // Get the next cell from the queue and update the current position
//        Cell currentCell = queue.poll();
//        currentX = currentCell.getX();
//        currentY = currentCell.getY();
//
//        // Check if there is an obstacle in the current cell
//        if (sensorSimulatorService.isObstacle(currentX, currentY)) {
//            activityLogger.logObstacle(currentX, currentY); // Log the obstacle
//            return;
//        }
//
//        // Clean the dirt in the current cell if present
//        int dirtLevel = currentCell.getDirtLevel();
//        if (dirtLevel > 0) {
//            dirtService.cleanDirt(currentX, currentY);
//        }
//
//        activityLogger.logMovement(currentX, currentY, "Visiting"); // Log the movement to the current cell
//
//        // Render the updated state of the floor and robot on the canvas
//        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
//        floorPlanVisualizer.render(gc);
//        robotVisualizer.render(gc);
//
//        // Add all valid neighboring cells to the queue for future visits
//        for (Direction direction : Direction.values()) {
//            int newX = currentX + direction.getXOffset();
//            int newY = currentY + direction.getYOffset();
//
//            if (isValidMove(newX, newY)) {
//                Cell neighborCell = floorMap.getCells()[newX][newY];
//                if (!neighborCell.isVisited()) {
//                    queue.add(neighborCell);
//                    neighborCell.setVisited(true);
//                }
//            }
//        }
//
//        // Check if the robot has enough battery power to continue
//        if (!batteryService.hasSufficientPower()) {
//            activityLogger.logRecharge(); // Log that the robot needs to recharge
//            return;
//        }
//
//        // If the queue is empty, all cells have been visited
//        if (queue.isEmpty()) {
//            activityLogger.logMovement(currentX, currentY, "All cells visited, navigation completed");
//            isNavigationCompleted = true; // Mark navigation as complete
//        }
//    }
//
//    // Check if a move to a given coordinate is valid (i.e., within bounds)
//    private boolean isValidMove(int x, int y) {
//        return x >= 0 && y >= 0 && x < floorMap.getCells().length && y < floorMap.getCells()[0].length;
//    }
//
//    // Get the current position of the robot
//    public int[] getCurrentPosition() {
//        return new int[]{currentX, currentY};
//    }
//
//    // Check if the navigation has been completed
//    public boolean isNavigationCompleted() {
//        return isNavigationCompleted;
//    }
//}

//TODO: DFS algorithm 
@Service
public class NavigationService {

    @Autowired
    private FloorMap floorMap;

    @Autowired
    private DirtService dirtService;

    @Autowired
    private BatteryService batteryService;

    @Autowired
    private SensorSimulatorService sensorSimulatorService;

    @Autowired
    private ActivityLogger activityLogger;

    private Deque<Cell> stack = new ArrayDeque<>();
    private boolean isNavigationCompleted = false;
    private int currentX = 0;
    private int currentY = 0;

    public void startNavigation(int startX, int startY) {
        if (!isNavigationCompleted && stack.isEmpty()) {
            Cell startCell = floorMap.getCells()[startX][startY];
            stack.push(startCell);
            startCell.setVisited(true);

            currentX = startX;
            currentY = startY;
            activityLogger.logMovement(startX, startY, "Start");
        }
    }


    public void stepNavigation(GraphicsContext gc, FloorPlanVisualizer floorPlanVisualizer, RobotVisualizer robotVisualizer) {
        if (stack.isEmpty() || isNavigationCompleted) {
            return;
        }

        Cell currentCell = stack.peek();
        currentX = currentCell.getX();
        currentY = currentCell.getY();

        if (sensorSimulatorService.isObstacle(currentX, currentY)) {
            activityLogger.logObstacle(currentX, currentY);
            stack.pop();  // Pop the current cell if it's blocked by an obstacle
            return;
        }

        int dirtLevel = currentCell.getDirtLevel();
        if (dirtLevel > 0) {
            dirtService.cleanDirt(currentX, currentY);
        }

        activityLogger.logMovement(currentX, currentY, "Visiting");

        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        floorPlanVisualizer.render(gc);
        robotVisualizer.render(gc);

        Cell nextCell = getNeighborCell();
        if (nextCell != null) {
            stack.push(nextCell);
            nextCell.setVisited(true);
        } else {
            stack.pop(); //only pop if current node already visited all 4 directions
        }
        
        if (!batteryService.hasSufficientPower()) {
            activityLogger.logRecharge();
            return;
        }

        if (stack.isEmpty()) {
            activityLogger.logMovement(currentX, currentY, "All cells visited, navigation completed");
            isNavigationCompleted = true;
        }
    }

    private Cell getNeighborCell() {
        for (Direction direction : Direction.values()) {
            int newX = currentX + direction.getXOffset();
            int newY = currentY + direction.getYOffset();
            if (isValidMove(newX, newY)) {
                Cell neighborCell = floorMap.getCells()[newX][newY];
                if (!neighborCell.isVisited() && !sensorSimulatorService.isObstacle(newX, newY)) {
                    return neighborCell;
                }
            }
        }
        return null;
    }

    private boolean isValidMove(int x, int y) {
        return x >= 0 && y >= 0 && x < floorMap.getCells().length && y < floorMap.getCells()[0].length;
    }

    public int[] getCurrentPosition() {
        return new int[]{currentX, currentY};
    }

    public boolean isNavigationCompleted() {
        return isNavigationCompleted;
    }
}



