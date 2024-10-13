package com.cleanSweep.control;

import com.cleanSweep.sensor.SensorSimulator;
import com.cleanSweep.visualization.RobotVisualizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NavigationController {
    private static final Logger logger = LoggerFactory.getLogger(NavigationController.class);

    private int currentX;
    private int currentY;
    private SensorSimulator sensorSimulator;
    private RobotVisualizer robotVisualizer;
    
    // Constructor: initializes at the starting position
    public NavigationController(int startX, int startY, SensorSimulator sensorSimulator, RobotVisualizer robotVisualizer) {
        this.currentX = startX;
        this.currentY = startY;
        this.sensorSimulator = sensorSimulator;
        this.robotVisualizer = robotVisualizer;
    }

    // Getter for currentX
    public int getCurrentX() {
        return currentX;
    }

    // Getter for currentY
    public int getCurrentY() {
        return currentY;
    }

    // Movement in four directions (up, down, left, right)
    public void moveUp() {
        if (!sensorSimulator.isObstacle(currentX, currentY - 1)) {
            currentY--;
            logger.info("Moved up to position (" + currentX + ", " + currentY + ")");
            robotVisualizer.renderRobot(currentX, currentY);
        } else {
            logger.warn("Obstacle detected. Cannot move up.");
        }
    }

    public void moveDown() {
        if (!sensorSimulator.isObstacle(currentX, currentY + 1)) {
            currentY++;
            logger.info("Moved down to position (" + currentX + ", " + currentY + ")");
            robotVisualizer.renderRobot(currentX, currentY);
        } else {
            logger.warn("Obstacle detected. Cannot move down.");
        }
    }

    public void moveLeft() {
        if (!sensorSimulator.isObstacle(currentX - 1, currentY)) {
            currentX--;
            logger.info("Moved left to position (" + currentX + ", " + currentY + ")");
            robotVisualizer.renderRobot(currentX, currentY);
        } else {
            logger.warn("Obstacle detected. Cannot move left.");
        }
    }

    public void moveRight() {
        if (!sensorSimulator.isObstacle(currentX + 1, currentY)) {
            currentX++;
            logger.info("Moved right to position (" + currentX + ", " + currentY + ")");
            robotVisualizer.renderRobot(currentX, currentY);
        } else {
            logger.warn("Obstacle detected. Cannot move right.");
        }
    }
    
    // Gets the current position of the robot
    public int[] getCurrentPosition() {
        return new int[] {currentX, currentY};
    }
}
