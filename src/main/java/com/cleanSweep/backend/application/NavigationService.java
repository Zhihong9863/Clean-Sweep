package com.cleanSweep.backend.application;

import com.cleanSweep.backend.domain.Cell;
import com.cleanSweep.backend.domain.FloorMap;
import com.cleanSweep.backend.common.Direction;
import com.cleanSweep.backend.infrastructure.ActivityLogger;
import com.cleanSweep.frontend.visualization.FloorPlanVisualizer;
import com.cleanSweep.frontend.visualization.RobotVisualizer;
import javafx.scene.canvas.GraphicsContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

//TODO: DFS algorithm 
@Service
public class NavigationService {

    @Autowired
    private FloorMap floorMap;

    @Autowired
    private DirtService dirtService;

    @Autowired
    private BatteryService batteryService;

    @Autowired
    private SensorSimulatorService sensorSimulatorService;

    @Autowired
    private ActivityLogger activityLogger;

    private Deque<Cell> stack = new ArrayDeque<>();
    private List<int[]> stationPath = new ArrayList<>();
    private int stationIdx;
    private int stationDist;

    private boolean isNavigationCompleted = false;
    private int currentX = 0;
    private int currentY = 0;

    public void startNavigation(int startX, int startY) {
        if (!isNavigationCompleted && stack.isEmpty()) {
            startX = 0;
            startY = 0;
            
            Cell startCell = floorMap.getCells()[startX][startY];
            stack.push(startCell);
            startCell.setVisited(true);
            initializeWayToChargingStation(startX, startY);

            currentX = startX;
            currentY = startY;
            activityLogger.logMovement(startX, startY, "Start");
        }
    }

    // Find all shortest paths from (0,0) to all cells
    public void initializeWayToChargingStation(int startX, int startY) {
        Cell startCell = floorMap.getCells()[startX][startY];
        List<int[]> startPath = new ArrayList<>();
        startPath.add(new int[]{startX, startY});
        startCell.setWayToChargingStation(startPath);
        boolean[][] visit = new boolean[floorMap.getCells().length][floorMap.getCells()[0].length];
        visit[startX][startY] = true;
        Deque<Cell> queue = new ArrayDeque<>();
        queue.add(startCell);
        while (!queue.isEmpty()){
            Cell cell = queue.poll();
            int x = cell.getX();
            int y = cell.getY();
            List<int[]> path = cell.getWayToChargingStation();
            for (Direction direction : Direction.values()) {
                int newX = x + direction.getXOffset();
                int newY = y + direction.getYOffset();
                if (isValidMove(newX, newY) && !visit[newX][newY] && !sensorSimulatorService.isObstacle(newX, newY)) {
                    Cell neighborCell = floorMap.getCells()[newX][newY];
                    visit[newX][newY] = true;
                    List<int[]> newPath = new ArrayList<>(path);
                    newPath.add(new int[]{newX, newY});
                    neighborCell.setWayToChargingStation(newPath);
                    queue.add(neighborCell);
                }
            }
        }
    }
    
    //Moving to the charging location to recharge battery or remove dirt
    public void stationNavigation(GraphicsContext gc, FloorPlanVisualizer floorPlanVisualizer, RobotVisualizer robotVisualizer) {
        // 添加安全检查
        if (stationPath == null || stationPath.isEmpty() || stationIdx >= stationPath.size() || stationIdx < 0) {
            return;
        }

        int[] currCell = stationPath.get(stationIdx);
        currentX = currCell[0];
        currentY = currCell[1];

        int floorPowerCost = sensorSimulatorService.getSurfacePowerCost(currentX, currentY);
        batteryService.consumePower(floorPowerCost);

        //Update UX/UI
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        floorPlanVisualizer.render(gc);
        robotVisualizer.render(gc);

        activityLogger.logMovement(currentX, currentY, "Visiting");

        if (currentX == 0 && currentY == 0) {
            dirtService.removeDirt();
            batteryService.recharge();
            
            if (stack.isEmpty()) {
                isNavigationCompleted = true;
            } else {
                if (stationDist == -1) {
                    stationDist = 1;
                }
            }
        } else if (stationDist == 1 && stationIdx == stationPath.size() - 1) {
            dirtService.setCleaningMode();
        }
        
        // 添加边界检查
        if (!isNavigationCompleted && ((stationDist == 1 && stationIdx < stationPath.size() - 1) || 
            (stationDist == -1 && stationIdx > 0))) {
            stationIdx += stationDist;
        }
    }


    public void stepNavigation(GraphicsContext gc, FloorPlanVisualizer floorPlanVisualizer, RobotVisualizer robotVisualizer) {
        if (!dirtService.isCleaningActive()){
            stationNavigation(gc, floorPlanVisualizer, robotVisualizer);
        } else {
            cleaningNavigation(gc, floorPlanVisualizer, robotVisualizer);  // Step navigation and update visuals
        }
    }

    public void cleaningNavigation(GraphicsContext gc, FloorPlanVisualizer floorPlanVisualizer, RobotVisualizer robotVisualizer) {

        if (stack.isEmpty() || isNavigationCompleted) {
            return;
        }

        Cell currentCell = stack.peek();
        currentX = currentCell.getX();
        currentY = currentCell.getY();

        if (sensorSimulatorService.isObstacle(currentX, currentY)) {
            activityLogger.logObstacle(currentX, currentY);
            stack.pop();
            return;
        }

        // 获取地板类型的电量消耗
        int floorPowerCost = sensorSimulatorService.getSurfacePowerCost(currentX, currentY);
        
        // 无论是否有灰尘，移动都消耗地板类型对应的电量
        batteryService.consumePower(floorPowerCost);
        
        // 如果有灰尘，清理灰尘
        if (currentCell.getDirtLevel() > 0) {
            dirtService.cleanDirt(currentX, currentY);
        }

        int batteryToReachStation = currentCell.getWayToChargingStation().size()-1;
        
        if (dirtService.isFullDirt() || batteryService.isRechargeNeeded(batteryToReachStation)){
            dirtService.stopCleaningMode();
            stationPath = currentCell.getWayToChargingStation();
            stationIdx = stationPath.size()-1;
            stationDist = -1;
            if (batteryService.isRechargeNeeded(batteryToReachStation)) {
                activityLogger.logLowBattery();
            }
            return;
        }

        activityLogger.logMovement(currentX, currentY, "Visiting");

        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        floorPlanVisualizer.render(gc);
        robotVisualizer.render(gc);

        Cell nextCell = getNeighborCell();
        if (nextCell != null) {
            stack.push(nextCell);
            nextCell.setVisited(true);
        } else {
            stack.pop();
        }
        
        if (!batteryService.hasSufficientPower()) {
            activityLogger.logRecharge();
            return;
        }

        if (stack.isEmpty()) {
            if (currentX != 0 || currentY != 0) {
                dirtService.stopCleaningMode();
                stationPath = currentCell.getWayToChargingStation();
                stationIdx = stationPath.size()-1;
                stationDist = -1;
            } else {
                activityLogger.logMovement(currentX, currentY, "All cells visited, navigation completed");
                isNavigationCompleted = true;
            }
        }
    }

    private Cell getNeighborCell() {
        for (Direction direction : Direction.values()) {
            int newX = currentX + direction.getXOffset();
            int newY = currentY + direction.getYOffset();
            if (isValidMove(newX, newY)) {
                Cell neighborCell = floorMap.getCells()[newX][newY];
                if (!neighborCell.isVisited() && !sensorSimulatorService.isObstacle(newX, newY)) {
                    return neighborCell;
                }
            }
        }
        return null;
    }

    private boolean isValidMove(int x, int y) {
        return x >= 0 && y >= 0 && x < floorMap.getCells().length && y < floorMap.getCells()[0].length;
    }

    public int[] getCurrentPosition() {
        return new int[]{currentX, currentY};
    }

    public boolean isNavigationCompleted() {
        return isNavigationCompleted;
    }

    private int[] findNearestChargingStation(int x, int y) {
        int minDistance = Integer.MAX_VALUE;
        int[] nearest = new int[]{0, 0};
        
        for (int i = 0; i < floorMap.getCells().length; i++) {
            for (int j = 0; j < floorMap.getCells()[0].length; j++) {
                if (floorMap.getCells()[i][j].isChargingStation()) {
                    int distance = Math.abs(x - i) + Math.abs(y - j);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearest[0] = i;
                        nearest[1] = j;
                    }
                }
            }
        }
        return nearest;
    }
}



