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

public class NavigationController {

    private int currentX;
    private int currentY;
    private SensorSimulator sensorSimulator;
    private RobotVisualizer robotVisualizer;
    private static final int GRID_WIDTH = 20;  // Assuming a 20x20 grid
    private static final int GRID_HEIGHT = 20;

    public NavigationController(int startX, int startY, SensorSimulator sensorSimulator, RobotVisualizer robotVisualizer) {
        this.currentX = startX;
        this.currentY = startY;
        this.sensorSimulator = sensorSimulator;
        this.robotVisualizer = robotVisualizer;
    }

    public void moveUp() {
        if (currentY > 0 && !sensorSimulator.isObstacle(currentX, currentY - 1)) {
            currentY--;
            robotVisualizer.renderRobot(currentX, currentY);
        }
    }

    public void moveDown() {
        if (currentY < GRID_HEIGHT - 1 && !sensorSimulator.isObstacle(currentX, currentY + 1)) {
            currentY++;
            robotVisualizer.renderRobot(currentX, currentY);
        }
    }

    public void moveLeft() {
        if (currentX > 0 && !sensorSimulator.isObstacle(currentX - 1, currentY)) {
            currentX--;
            robotVisualizer.renderRobot(currentX, currentY);
        }
    }

    public void moveRight() {
        if (currentX < GRID_WIDTH - 1 && !sensorSimulator.isObstacle(currentX + 1, currentY)) {
            currentX++;
            robotVisualizer.renderRobot(currentX, currentY);
        }
    }

    public int[] getCurrentPosition() {
        return new int[]{currentX, currentY};
    }
}
