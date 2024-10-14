package com.cleanSweep.sensor;

import java.util.Random;

public class SensorSimulator {

    private boolean[][] obstacleGrid;

    public SensorSimulator(int gridWidth, int gridHeight) {
        obstacleGrid = new boolean[gridWidth][gridHeight];
        generateObstacles(gridWidth, gridHeight);
    }

    private void generateObstacles(int gridWidth, int gridHeight) {
        Random rand = new Random();
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                obstacleGrid[x][y] = rand.nextInt(10) == 0; // 10% chance of obstacle
            }
        }
    }

    public boolean isObstacle(int x, int y) {
        return obstacleGrid[x][y];
    }
}