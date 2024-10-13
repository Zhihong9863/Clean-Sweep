package com.cleanSweep.sensor;

import java.util.HashMap;
import java.util.Map;

public class SensorSimulator {

    private Map<String, Boolean> dirtMap;  // Keeps track of where dirt exists
    private Map<String, Integer> surfaceMap;  // Keeps track of surface types at each location (1 = bare, 2 = low-pile, 3 = high-pile)

    public SensorSimulator() {
        // Initialize dirt and surface maps
        dirtMap = new HashMap<>();
        surfaceMap = new HashMap<>();
    }

    public boolean isObstacle(int x, int y) {
        // Return whether there is an obstacle (for simplicity, treat certain cells as obstacles)
        return false;  // Simulated logic for obstacle detection
    }

    public boolean isDirtPresent(int x, int y) {
        // Check if dirt is present at the given location
        String key = x + "," + y;
        return dirtMap.getOrDefault(key, false);
    }

    public void cleanDirt(int x, int y) {
        // Mark the location as clean
        String key = x + "," + y;
        dirtMap.put(key, false);
    }

    public int getSurfacePowerCost(int x, int y) {
        // Return the power cost for moving on the surface at (x, y)
        String key = x + "," + y;
        return surfaceMap.getOrDefault(key, 1);  // Default to bare floor
    }
    
    // Initialize dirt and surfaces for testing
    public void initializeTestFloorPlan() {
        // Add some dirt locations
        dirtMap.put("2,2", true);
        dirtMap.put("3,3", true);

        // Add surface types
        surfaceMap.put("0,0", 1);  // Bare floor
        surfaceMap.put("1,1", 2);  // Low-pile carpet
        surfaceMap.put("2,2", 3);  // High-pile carpet
    }
}
