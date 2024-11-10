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
    private boolean[][] chargingStationGrid;

    @Autowired
    private FloorMap floorMap;

    @Autowired
    private ActivityLogger activityLogger;

    @Value("${clean-sweep.floor-grid-size}")
    private int gridSize;

    @PostConstruct
    public void initializeObstacleAndDirt() {
        floorMap.initializeGrid(gridSize);
        this.obstacleGrid = generateObstacles(gridSize, gridSize);
        this.chargingStationGrid = generateChargingStations(gridSize, gridSize);

        Random random = new Random();
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                Cell cell = floorMap.getCells()[x][y];
                if (obstacleGrid[x][y]) {
                    cell.setObstacle(true);
                } else if (chargingStationGrid[x][y]) {
                    cell.setChargingStation(true);
                } else {
                    int floorTypeRandom = random.nextInt(3);
                    switch (floorTypeRandom) {
                        case 0:
                            cell.setFloorType(FloorType.BARE_FLOOR);
                            break;
                        case 1:
                            cell.setFloorType(FloorType.LOW_PILE_CARPET);
                            break;
                        case 2:
                            cell.setFloorType(FloorType.HIGH_PILE_CARPET);
                            break;
                    }
                    cell.setDirtLevel(random.nextInt(2));
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

    private boolean[][] generateChargingStations(int width, int height) {
        boolean[][] grid = new boolean[width][height];
        Random random = new Random();
        
        // 确保(0,0)位置是充电站
        grid[0][0] = true;
        
        // 随机生成4-5个额外的充电站
        int numStations = random.nextInt(2) + 4; // 修改这里：4-5个额外充电站
        int stationsPlaced = 0;
        
        while (stationsPlaced < numStations) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            
            // 跳过(0,0)位置，确保不会与障碍物重叠，且位置未被占用
            if (!(x == 0 && y == 0) && !obstacleGrid[x][y] && !grid[x][y]) {
                grid[x][y] = true;
                stationsPlaced++;
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

    public boolean isChargingStation(int x, int y) {
        return chargingStationGrid[x][y];
    }
}
