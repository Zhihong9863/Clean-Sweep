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
        int[] currCell = stationPath.get(stationIdx);
        currentX = currCell[0];
        currentY = currCell[1];

        batteryService.consumePower(1);

        //Update UX/UI
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        floorPlanVisualizer.render(gc);
        robotVisualizer.render(gc);

        activityLogger.logMovement(currentX, currentY, "Visiting");

        if (stationIdx ==0){
            // remove dirt and charge the battery at the charging station, then come back to latest location
            dirtService.removeDirt();
            // If battery is lower than 20% of max battery capacity, recharge
            if (batteryService.getBattery()<batteryService.getFullChargeValue()/5){
                batteryService.recharge();
            }
            stationDist = 1;
        } else if (stationIdx== stationPath.size()-1 && stationDist==1){
            // re-enable cleaning mode when come back to the latest location
            dirtService.setCleaningMode();
        }
        stationIdx += stationDist;
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
            stack.pop();  // Pop the current cell if it's blocked by an obstacle
            return;
        }

        // The moving unit is 1, the cleaning unit equals to dirt level
        int dirtLevel = currentCell.getDirtLevel();
        if (dirtLevel > 0) {
            batteryService.consumePower(dirtLevel+1);
            dirtService.cleanDirt(currentX, currentY);
        } else {
            batteryService.consumePower(1);
        }

        int batteryToReachStation = currentCell.getWayToChargingStation().size()-1;
        
        // get the path back to charging station from the current location when full dirt capacity or low battery
        if (dirtService.isFullDirt() || batteryService.isRechargeNeeded(batteryToReachStation)){
            //stop cleaning
            dirtService.stopCleaningMode();
            //get the path to charging station from current location
            stationPath = currentCell.getWayToChargingStation();
            stationIdx = stationPath.size()-1;
            stationDist = -1;
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
            stack.pop(); //only pop if current node already visited all 4 directions
        }
        
        if (!batteryService.hasSufficientPower()) {
            activityLogger.logRecharge();
            return;
        }

        if (stack.isEmpty()) {
            activityLogger.logMovement(currentX, currentY, "All cells visited, navigation completed");
            isNavigationCompleted = true;
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
}



