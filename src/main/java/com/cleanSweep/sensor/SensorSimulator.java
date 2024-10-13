/*
 * 2.1 SensorSimulator
Folder: /src/main/java/com/cleanSweep/sensor/
Description: Simulates the environment and provides data to the control system about dirt presence, surface types, obstacles, and charging stations.
Parameters:
Map<String, Boolean> dirtMap: Tracks the locations of dirt in the grid.
Map<String, Integer> surfaceMap: Tracks surface types (bare floor, low-pile, high-pile).
Map<String, Boolean> obstacleMap: Tracks obstacles on the grid.
Map<String, Boolean> chargingStationMap: Tracks locations of charging stations.
Methods:
SensorSimulator(): Constructor to initialize the dirt, surface, and obstacle maps.
boolean isObstacle(int x, int y): Returns true if an obstacle exists at the specified coordinates.
boolean isDirtPresent(int x, int y): Checks if dirt is present at the given location.
void cleanDirt(int x, int y): Cleans the dirt at the specified location.
int getSurfacePowerCost(int x, int y): Returns the power cost for moving on the surface at the given coordinates.
boolean isChargingStation(int x, int y): Returns true if there is a charging station at the given location.
void initializeTestFloorPlan(): Initializes the environment with dirt, surface types, and obstacles for testing.
 */

 package com.cleanSweep.sensor;

import java.util.HashMap;
import java.util.Map;

import com.cleanSweep.interfaces.Sensor;

/**
 * The SensorSimulator class simulates the input from the sensors on the Clean Sweep.
 * This includes detecting obstacles, surfaces, dirt presence, and edge detection.
 */
public class SensorSimulator implements Sensor {
    
    // Maps to simulate grid properties
    private Map<String, Boolean> obstacleMap;   // Map to track obstacles in the grid
    private Map<String, Integer> surfaceMap;    // Map to track surface types (1 = bare, 2 = low-pile, 3 = high-pile)
    private Map<String, Boolean> dirtMap;       // Map to track presence of dirt in a grid cell
    private int gridWidth;
    private int gridHeight;

    public static final int BARE_FLOOR = 1;
    public static final int LOW_PILE_CARPET = 2;
    public static final int HIGH_PILE_CARPET = 3;

    public SensorSimulator(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        obstacleMap = new HashMap<>();
        surfaceMap = new HashMap<>();
        dirtMap = new HashMap<>();
    }

    /**
     * Initializes the sensor data for the floor plan.
     * This method should be called once to set up the environment.
     */
    public void initializeFloorPlan() {
        // Set obstacles
        setObstacle(3, 3, true);  // Sample obstacle

        // Set surface types
        setSurfaceType(0, 0, BARE_FLOOR);
        setSurfaceType(1, 1, LOW_PILE_CARPET);
        setSurfaceType(2, 2, HIGH_PILE_CARPET);

        // Set dirt
        setDirt(2, 2, true);  // Dirt present at (2, 2)
    }

    /**
     * Checks if there is an obstacle in the specified grid cell.
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     * @return true if there is an obstacle, false otherwise.
     */
    @Override
    public boolean isObstacle(int x, int y) {
        String key = getGridKey(x, y);
        return obstacleMap.getOrDefault(key, false);  // Returns true if there's an obstacle
    }

    /**
     * Gets the surface type of the specified grid cell.
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     * @return The surface type (1 = bare, 2 = low-pile, 3 = high-pile).
     */
    public int getSurfaceType(int x, int y) {
        String key = getGridKey(x, y);
        return surfaceMap.getOrDefault(key, BARE_FLOOR);  // Default to bare floor
    }

    /**
     * Checks if there is dirt in the specified grid cell.
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     * @return true if dirt is present, false otherwise.
     */
    @Override
    public boolean isDirtPresent(int x, int y) {
        String key = getGridKey(x, y);
        return dirtMap.getOrDefault(key, false);
    }

    /**
     * Marks the grid cell as clean by removing the dirt.
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     */
    @Override
    public void cleanDirt(int x, int y) {
        String key = getGridKey(x, y);
        dirtMap.put(key, false);  // Marks the cell as clean
    }

    /**
     * Simulates edge detection for cliffs or stairs.
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     * @return true if the cell is at the edge or a cliff, false otherwise.
     */
    public boolean isCliff(int x, int y) {
        // If the vacuum is at the boundary of the grid, treat it as a cliff.
        return (x < 0 || y < 0 || x >= gridWidth || y >= gridHeight);
    }

    // --- Helper Methods for Setting Up the Simulation ---
    
    /**
     * Sets an obstacle at a specific grid location.
     * @param x The x-coordinate of the grid cell.
     * @param y The y-coordinate of the grid cell.
     * @param isObstacle True if there is an obstacle, false otherwise.
     */
    public void setObstacle(int x, int y, boolean isObstacle) {
        String key = getGridKey(x, y);
        obstacleMap.put(key, isObstacle);
    }

    /**
     * Sets the surface type at a specific grid location.
     * @param x The x-coordinate of the grid cell.
     * @param y The y-coordinate of the grid cell.
     * @param surfaceType The type of surface (1 = bare, 2 = low-pile, 3 = high-pile).
     */
    public void setSurfaceType(int x, int y, int surfaceType) {
        String key = getGridKey(x, y);
        surfaceMap.put(key, surfaceType);
    }

    /**
     * Sets the presence of dirt at a specific grid location.
     * @param x The x-coordinate of the grid cell.
     * @param y The y-coordinate of the grid cell.
     * @param hasDirt True if there is dirt present, false otherwise.
     */
    public void setDirt(int x, int y, boolean hasDirt) {
        String key = getGridKey(x, y);
        dirtMap.put(key, hasDirt);
    }

    /**
     * Helper method to generate a unique key for each grid cell.
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     * @return A string key for the cell.
     */
    private String getGridKey(int x, int y) {
        return x + "," + y;
    }

    @Override
    public int getSurfacePowerCost(int x, int y) {
        int surfaceType = getSurfaceType(x, y);
        switch (surfaceType) {
            case BARE_FLOOR:
                return 1;
            case LOW_PILE_CARPET:
                return 2;
            case HIGH_PILE_CARPET:
                return 3;
            default:
                return 1; // Default to bare floor cost if unknown
        }
    }
}
