/*
1.2 DirtHandler
Folder: /src/main/java/com/cleanSweep/control/
Description: Detects and cleans dirt when the robot encounters it. Keeps track of the robot's dirt collection and stops when full.
Parameters:
int currentDirt: Tracks how much dirt has been collected.
int dirtCapacity: The total dirt-carrying capacity (50 units).
SensorSimulator sensorSimulator: Reference to the simulator that detects dirt presence.
Methods:
DirtHandler(SensorSimulator sensorSimulator): Constructor to initialize the dirt handler.
void cleanDirt(int x, int y): Cleans the current location if dirt is detected.
boolean isCapacityFull(): Returns true if the robot has reached its dirt capacity.
int getCurrentDirt(): Returns the current amount of dirt collected.
*/
package com.cleanSweep.control;


import java.util.Random;

public class DirtHandler {

    private boolean[][] dirtGrid;
    private int maxCapacity = 100;
    private int currentCapacity = 0;

    public DirtHandler(int gridWidth, int gridHeight) {
        dirtGrid = new boolean[gridWidth][gridHeight];
        generateDirt(gridWidth, gridHeight);
    }

    private void generateDirt(int gridWidth, int gridHeight) {
        Random rand = new Random();
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                dirtGrid[x][y] = rand.nextBoolean();
            }
        }
    }

    public boolean isDirty(int x, int y) {
        return dirtGrid[x][y];
    }

    public void cleanDirt(int x, int y) {
        if (dirtGrid[x][y] && currentCapacity < maxCapacity) {
            dirtGrid[x][y] = false;
            currentCapacity++;
            System.out.println("Cleaned dirt at (" + x + ", " + y + ")");
        }
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
}