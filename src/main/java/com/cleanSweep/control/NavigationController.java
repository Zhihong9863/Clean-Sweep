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

import com.cleanSweep.interfaces.Sensor;
import com.cleanSweep.visualization.RobotVisualizer;
import com.cleanSweep.logging.ActivityLogger;
import com.cleanSweep.control.PowerManagementController;
import com.cleanSweep.control.DirtHandler;

public class NavigationController {

    private int currentX;
    private int currentY;
    private Sensor sensor;
    private RobotVisualizer robotVisualizer;
    private ActivityLogger activityLogger;
    private PowerManagementController powerManagementController;
    private DirtHandler dirtHandler;

    public NavigationController(int startX, int startY, Sensor sensor, RobotVisualizer robotVisualizer, ActivityLogger activityLogger, PowerManagementController powerManagementController, DirtHandler dirtHandler) {
        this.currentX = startX;
        this.currentY = startY;
        this.sensor = sensor;
        this.robotVisualizer = robotVisualizer;
        this.activityLogger = activityLogger;
        this.powerManagementController = powerManagementController;
        this.dirtHandler = dirtHandler;
    }

    public void moveUp() {
        if (!sensor.isObstacle(currentX, currentY - 1)) {
            currentY--;
            robotVisualizer.renderRobot(currentX, currentY);
            activityLogger.logMovement(currentX, currentY, "UP");
        } else {
            activityLogger.logMovement(currentX, currentY, "Obstacle detected, cannot move up.");
        }
    }

    public void moveDown() {
        if (!sensor.isObstacle(currentX, currentY + 1)) {
            currentY++;
            robotVisualizer.renderRobot(currentX, currentY);
            activityLogger.logMovement(currentX, currentY, "DOWN");
        } else {
            activityLogger.logMovement(currentX, currentY, "Obstacle detected, cannot move down.");
        }
    }

    public void moveLeft() {
        if (!sensor.isObstacle(currentX - 1, currentY)) {
            currentX--;
            robotVisualizer.renderRobot(currentX, currentY);
            activityLogger.logMovement(currentX, currentY, "LEFT");
        } else {
            activityLogger.logMovement(currentX, currentY, "Obstacle detected, cannot move left.");
        }
    }

    public void moveRight() {
        if (!sensor.isObstacle(currentX + 1, currentY)) {
            currentX++;
            robotVisualizer.renderRobot(currentX, currentY);
            activityLogger.logMovement(currentX, currentY, "RIGHT");
        } else {
            activityLogger.logMovement(currentX, currentY, "Obstacle detected, cannot move right.");
        }
    }

    public int[] getCurrentPosition() {
        return new int[]{currentX, currentY};
    }

    public int getBatteryLife() {
        return powerManagementController.getBatteryLife();
    }

    public int getCurrentDirtCapacity() {
        return dirtHandler.getCurrentDirt();
    }

    public int getMaxDirtCapacity() {
        return dirtHandler.getDirtCapacity();
    }
}
