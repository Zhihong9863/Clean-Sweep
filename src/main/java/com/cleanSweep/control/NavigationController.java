package com.cleanSweep.control;

import com.cleanSweep.logging.ActivityLogger;
import com.cleanSweep.sensor.SensorSimulator;

public class NavigationController {

    private int currentX;
    private int currentY;
    private ActivityLogger activityLogger;

    public NavigationController(int startX, int startY) {
        this.currentX = startX;
        this.currentY = startY;
        this.activityLogger = new ActivityLogger();
    }

    public void moveRight() {
        currentX++;
        updatePosition("right");
    }

    public void moveLeft() {
        currentX--;
        updatePosition("left");
    }

    public void moveDown() {
        currentY++;
        updatePosition("down");
    }

    public int[] getCurrentPosition() {
        return new int[]{currentX, currentY};
    }

    private void updatePosition(String direction) {
        activityLogger.logMovement(currentX, currentY, "Moved " + direction);
    }

    public boolean canMoveRight(int gridWidth, SensorSimulator sensorSimulator) {
        return currentX < gridWidth - 1 && !sensorSimulator.isObstacle(currentX + 1, currentY);
    }

    public boolean canMoveLeft(SensorSimulator sensorSimulator) {
        return currentX > 0 && !sensorSimulator.isObstacle(currentX - 1, currentY);
    }

    public boolean canMoveDown(int gridHeight, SensorSimulator sensorSimulator) {
        return currentY < gridHeight - 1 && !sensorSimulator.isObstacle(currentX, currentY + 1);
    }
}
