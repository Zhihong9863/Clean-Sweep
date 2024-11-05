package com.cleanSweep.backend.application;

import com.cleanSweep.backend.domain.FloorMap;
import com.cleanSweep.backend.domain.Cell;
import com.cleanSweep.backend.common.FloorType;
import com.cleanSweep.backend.infrastructure.ActivityLogger;
import com.cleanSweep.backend.application.interfaces.Sensor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SensorSimulatorService implements Sensor {

    private boolean[][] obstacleGrid;

    @Autowired
    private FloorMap floorMap;

    @Autowired
    private ActivityLogger activityLogger;

    @Value("${clean-sweep.floor-grid-size}")
    private int gridSize;

    @PostConstruct
    public void initializeObstacleAndDirt() {
        floorMap.initializeGrid(gridSize); // Initialize the grid of FloorMap
        this.obstacleGrid = generateObstacles(gridSize, gridSize); // Obstacle grid generation

        Random random = new Random();
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                if(x == 0 && y == 0) continue; //(0,0) will always be the charging station
                Cell cell = floorMap.getCells()[x][y];
                if (obstacleGrid[x][y]){
                    cell.setObstacle(obstacleGrid[x][y]); // lay down a screen
                } else {
                    cell.setDirtLevel(random.nextInt(4)); // Randomly generate dirt levels of 0-3
                }
            }
        }
    }

    /**
     * Randomly generate obstacle grids.
     * @param width Width
     * @param height Grid height
     * @return Randomly arrange a two-dimensional array of obstacles
     */
    private boolean[][] generateObstacles(int width, int height) {
        boolean[][] grid = new boolean[width][height];
        Random random = new Random();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = random.nextInt(7) == 0;
            }
        }
        return grid;
    }

    @Override
    public boolean isDirtPresent(int x, int y) {
        return floorMap.getCells()[x][y].getDirtLevel() > 0;
    }

    @Override
    public boolean isObstacle(int x, int y) {
        return obstacleGrid[x][y]==true;
    }

    @Override
    public void cleanDirt(int x, int y) {
        Cell cell = floorMap.getCells()[x][y];
        if (cell.getDirtLevel() > 0) {
            cell.reduceDirtLevel();
            activityLogger.logCleaning(x, y);
        } else {
            activityLogger.logNoDirtAtPosition(x, y);
        }
    }

    @Override
    public String getSurfaceType(int x, int y) {
        return floorMap.getCells()[x][y].getFloorType().toString();
    }

    @Override
    public int getSurfacePowerCost(int x, int y) {
        FloorType floorType = floorMap.getCells()[x][y].getFloorType();
        switch (floorType) {
            case BARE_FLOOR:
                return 1;
            case LOW_PILE_CARPET:
                return 2;
            case HIGH_PILE_CARPET:
                return 3;
            default:
                return 1;
        }
    }
}
