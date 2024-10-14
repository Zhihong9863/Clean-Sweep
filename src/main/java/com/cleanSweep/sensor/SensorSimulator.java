package com.cleanSweep.sensor;

import com.cleanSweep.interfaces.Sensor;
import java.util.HashMap;
import java.util.Map;

public class SensorSimulator implements Sensor {

    // Maps to simulate the environment
    private Map<String, Boolean> dirtMap = new HashMap<>();
    private Map<String, Boolean> obstacleMap = new HashMap<>();
    private Map<String, String> surfaceMap = new HashMap<>();

    public SensorSimulator() {
        // Initialize maps with some example data
        dirtMap.put("0,0", true);
        dirtMap.put("2,2", true);
        dirtMap.put("3,4", true);
        dirtMap.put("5,5", true);
        dirtMap.put("7,8", true);

        obstacleMap.put("1,0", true);
        obstacleMap.put("4,4", true);
        obstacleMap.put("6,6", true);
        obstacleMap.put("8,8", true);
        obstacleMap.put("10,10", true);

        surfaceMap.put("0,0", "bare floor");
        surfaceMap.put("1,0", "low-pile carpet");
    }

    @Override
    public boolean isDirtPresent(int x, int y) {
        // Check if dirt is present at the given coordinates
        return dirtMap.getOrDefault(x + "," + y, false);
    }

    @Override
    public void cleanDirt(int x, int y) {
        // Clean dirt at the given coordinates
        dirtMap.put(x + "," + y, false);
    }

    @Override
    public boolean isObstacle(int x, int y) {
        // Check if there is an obstacle at the given coordinates
        return obstacleMap.getOrDefault(x + "," + y, false);
    }

    @Override
    public String getSurfaceType(int x, int y) {
        // Get the surface type at the given coordinates
        return surfaceMap.getOrDefault(x + "," + y, "unknown");
    }

    @Override
    public int getSurfacePowerCost(int x, int y) {
        // Determine the power cost based on the surface type
        String surfaceType = getSurfaceType(x, y);
        switch (surfaceType) {
            case "bare floor":
                return 1;
            case "low-pile carpet":
                return 2;
            case "high-pile carpet":
                return 3;
            default:
                return 0; // Unknown surface type
        }
    }
}
