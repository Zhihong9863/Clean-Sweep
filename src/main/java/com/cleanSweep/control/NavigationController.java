/*
 * 1.1 NavigationController
Folder: /src/main/java/com/cleanSweep/control/
Description: Responsible for managing the navigation of the Clean Sweep robot across the floor plan. It handles movement, obstacle detection, and pathfinding.
Parameters:
int currentX, currentY: Robot’s current position on the grid.
SensorSimulator sensorSimulator: Instance of the sensor simulator to check for obstacles.
RobotVisualizer robotVisualizer: Visualizer used to update the robot’s position on the UI.
Methods:
NavigationController(int startX, int startY, SensorSimulator sensorSimulator, RobotVisualizer robotVisualizer): Initializes the controller at the starting position.
void moveUp(): Moves the robot one unit up (if no obstacle).
void moveDown(): Moves the robot one unit down (if no obstacle).
void moveLeft(): Moves the robot one unit left (if no obstacle).
void moveRight(): Moves the robot one unit right (if no obstacle).
int[] getCurrentPosition(): Returns the current (x, y) coordinates of the robot.
void avoidObstacle(): Automatically re-routes the robot when an obstacle is detected.
 */
package com.cleanSweep.control;

import com.cleanSweep.sensor.SensorSimulator;
import com.cleanSweep.visualization.RobotVisualizer;
import com.cleanSweep.logging.ActivityLogger;

public class NavigationController {

    private int currentX;
    private int currentY;
    private SensorSimulator sensorSimulator;
    private RobotVisualizer robotVisualizer;
    private ActivityLogger activityLogger; // Declare the activityLogger
    private static final int GRID_WIDTH = 20;  // Assuming a 20x20 grid
    private static final int GRID_HEIGHT = 20;
    private boolean[][] visitedGrid = new boolean[GRID_WIDTH][GRID_HEIGHT];

    public NavigationController(int startX, int startY, SensorSimulator sensorSimulator, RobotVisualizer robotVisualizer, ActivityLogger activityLogger) {
        this.currentX = startX;
        this.currentY = startY;
        this.sensorSimulator = sensorSimulator;
        this.robotVisualizer = robotVisualizer;
        this.activityLogger = activityLogger; // Initialize here
    }

    public void moveUp() {
        if (currentY > 0 && !sensorSimulator.isObstacle(currentX, currentY - 1) && !sensorSimulator.isStair(currentX, currentY - 1)) {
            currentY--;
            updateGrid(currentX, currentY);
            robotVisualizer.renderRobot(currentX, currentY);
            activityLogger.logMovement(currentX, currentY, "up");
        } else {
            activityLogger.logObstacleDetected(true);
            checkShutdown();
        }
    }

    public void moveDown() {
        if (currentY < GRID_HEIGHT - 1 && !sensorSimulator.isObstacle(currentX, currentY + 1) && !sensorSimulator.isStair(currentX, currentY + 1)) {
            currentY++;
            updateGrid(currentX, currentY);
            robotVisualizer.renderRobot(currentX, currentY);
            activityLogger.logMovement(currentX, currentY, "down");
        } else {
            activityLogger.logObstacleDetected(true);
            checkShutdown();
        }
    }

    public void moveLeft() {
        if (currentX > 0 && !sensorSimulator.isObstacle(currentX - 1, currentY) && !sensorSimulator.isStair(currentX - 1, currentY)) {
            currentX--;
            updateGrid(currentX, currentY);
            robotVisualizer.renderRobot(currentX, currentY);
            activityLogger.logMovement(currentX, currentY, "left");
        } else {
            activityLogger.logObstacleDetected(true);
            checkShutdown();
        }
    }

    public void moveRight() {
        if (currentX < GRID_WIDTH - 1 && !sensorSimulator.isObstacle(currentX + 1, currentY) && !sensorSimulator.isStair(currentX + 1, currentY)) {
            currentX++;
            updateGrid(currentX, currentY);
            robotVisualizer.renderRobot(currentX, currentY);
            activityLogger.logMovement(currentX, currentY, "right");
        } else {
            activityLogger.logObstacleDetected(true);
            checkShutdown();
        }
    }

    public int[] getCurrentPosition() {
        return new int[]{currentX, currentY};
    }

    private void checkShutdown() {
        boolean canMove = (currentY > 0 && !sensorSimulator.isObstacle(currentX, currentY - 1) && !sensorSimulator.isStair(currentX, currentY - 1)) ||
                          (currentY < GRID_HEIGHT - 1 && !sensorSimulator.isObstacle(currentX, currentY + 1) && !sensorSimulator.isStair(currentX, currentY + 1)) ||
                          (currentX > 0 && !sensorSimulator.isObstacle(currentX - 1, currentY) && !sensorSimulator.isStair(currentX - 1, currentY)) ||
                          (currentX < GRID_WIDTH - 1 && !sensorSimulator.isObstacle(currentX + 1, currentY) && !sensorSimulator.isStair(currentX + 1, currentY));

        if (!canMove) {
            shutdown();
        }
    }

    private void shutdown() {
        System.out.println("Shutting down: No available moves.");
        // Additional shutdown logic
    }

    private void updateGrid(int x, int y) {
        visitedGrid[x][y] = true;
        // You can add more logic here to update the state of the cell (e.g., obstacle, stair)
    }
}
