package com.cleanSweep.control;

import com.cleanSweep.interfaces.Sensor;

import java.util.HashMap;
import java.util.Map;

public class SensorSimulator implements Sensor {

    private Map<String, Boolean> dirtMap = new HashMap<>();
    private Map<String, Integer> surfaceMap = new HashMap<>();
    private Map<String, Boolean> obstacleMap = new HashMap<>();

    public SensorSimulator() {
        initializeTestFloorPlan();
    }

    @Override
    public boolean isObstacle(int x, int y) {
        return obstacleMap.getOrDefault(x + "," + y, false);
    }

    @Override
    public boolean isDirtPresent(int x, int y) {
        return dirtMap.getOrDefault(x + "," + y, false);
    }

    @Override
    public void cleanDirt(int x, int y) {
        dirtMap.put(x + "," + y, false);
    }

    @Override
    public int getSurfacePowerCost(int x, int y) {
        return surfaceMap.getOrDefault(x + "," + y, 1);
    }

    public void initializeTestFloorPlan() {
        // Sample setup
        obstacleMap.put("1,2", true);
        dirtMap.put("2,3", true);
        surfaceMap.put("2,2", 3);
    }
}
