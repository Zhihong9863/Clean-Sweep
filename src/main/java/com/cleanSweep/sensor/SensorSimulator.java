package com.cleanSweep.sensor;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SensorSimulator {

    private Map<String, Boolean> dirtMap;
    private Map<String, Boolean> obstacleMap;
    private Map<String, String> surfaceMap;
    private Random random;
    private int gridWidth;
    private int gridHeight;

    public SensorSimulator() {
        this(20, 20); // Call the parameterized constructor with default values
    }

    public SensorSimulator(int gridWidth, int gridHeight) {
        dirtMap = new HashMap<>();
        obstacleMap = new HashMap<>();
        surfaceMap = new HashMap<>();
        random = new Random();
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        initializeTestFloorPlan();
    }

    public void initializeTestFloorPlan() {
        // Add dirt and obstacles
        for (int i = 0; i < 50; i++) {
            int x = random.nextInt(gridWidth - 2) + 1;
            int y = random.nextInt(gridHeight - 2) + 1;
            dirtMap.put(x + "," + y, true);
            if (i < 20) obstacleMap.put(x + "," + y, true);
        }

        // Define surface types
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                if (x < 7) {
                    surfaceMap.put(x + "," + y, "carpet");
                } else if (x < 14) {
                    surfaceMap.put(x + "," + y, "hardwood");
                } else {
                    surfaceMap.put(x + "," + y, "tile");
                }
            }
        }

        // Add walls around the grid
        for (int x = 0; x < gridWidth; x++) {
            obstacleMap.put(x + ",0", true);
            obstacleMap.put(x + "," + (gridHeight - 1), true);
        }
        for (int y = 0; y < gridHeight; y++) {
            obstacleMap.put("0," + y, true);
            obstacleMap.put((gridWidth - 1) + "," + y, true);
        }
    }

    public boolean isDirtPresent(int x, int y) {
        return dirtMap.getOrDefault(x + "," + y, false);
    }

    public void cleanDirt(int x, int y) {
        dirtMap.put(x + "," + y, false);
    }

    public boolean isObstacle(int x, int y) {
        return obstacleMap.getOrDefault(x + "," + y, false);
    }

    public String getSurfaceType(int x, int y) {
        return surfaceMap.getOrDefault(x + "," + y, "unknown");
    }

    public int getSurfacePowerCost(int x, int y) {
        String surfaceType = getSurfaceType(x, y);
        switch (surfaceType) {
            case "carpet":
                return 5; // Example power cost for carpet
            case "hardwood":
                return 3; // Example power cost for hardwood
            case "tile":
                return 2; // Example power cost for tile
            default:
                return 1; // Default power cost for unknown surfaces
        }
    }

    public boolean isObstacleAhead(String position) {
        // Use Boolean.FALSE as the default value
        return obstacleMap.getOrDefault(position, Boolean.FALSE);
    }
}
