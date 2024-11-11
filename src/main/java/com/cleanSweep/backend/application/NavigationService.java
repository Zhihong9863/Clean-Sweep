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
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;

/**
 * This service implements a modified DFS (Depth-First Search) algorithm for robot navigation
 * combined with Dijkstra's algorithm for finding optimal paths to charging stations.
 * It handles the robot's movement, cleaning operations, and charging station navigation
 * while maintaining efficient path planning and battery management.
 */
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

    // 添加新的字段来存储最后清扫的位置
    private int lastCleaningX;
    private int lastCleaningY;
    private boolean isReturningFromStation = false;

    /**
     * Initializes the navigation process from a starting position.
     * Sets up initial paths to all charging stations and begins the cleaning operation.
     */
    public void startNavigation(int startX, int startY) {
        if (!isNavigationCompleted && stack.isEmpty()) {
            startX = 0;
            startY = 0;
            
            // Initialize paths from all charging stations before starting navigation
            initializeAllPaths();
            
            Cell startCell = floorMap.getCells()[startX][startY];
            stack.push(startCell);
            startCell.setVisited(true);

            currentX = startX;
            currentY = startY;
            activityLogger.logMovement(startX, startY, "Start");
        }
    }

    /**
     * Initializes the shortest paths from a charging station to all cells using BFS.
     * This method is used to pre-compute paths for efficient navigation.
     */
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
    
    /**
     * Handles the robot's movement to and from charging stations.
     * Manages charging, dirt removal, and return path navigation.
     */
    public void stationNavigation() {
        if (stationPath == null || stationPath.isEmpty() || stationIdx >= stationPath.size()) {
            stationPath = null;
            stationIdx = 0;
            return;
        }

        int[] currCell = stationPath.get(stationIdx);
        currentX = currCell[0];
        currentY = currCell[1];

        int floorPowerCost = sensorSimulatorService.getSurfacePowerCost(currentX, currentY);
        batteryService.consumePower(floorPowerCost);

        String direction = isReturningFromStation ? "Returning to cleaning position" : "Moving to charging station";
        activityLogger.logMovement(currentX, currentY, direction);

        // 到达充电站且不是在返回途中
        if (isAtAnyChargingStation() && !isReturningFromStation) {
            dirtService.removeDirt();
            batteryService.recharge();
            
            if (stack.isEmpty()) {
                isNavigationCompleted = true;
                stationPath = null;
                stationIdx = 0;
            } else {
                // 准备返回最后清扫的位置
                dirtService.setCleaningMode();
                Cell lastCleaningCell = floorMap.getCells()[lastCleaningX][lastCleaningY];
                stationPath = new ArrayList<>(lastCleaningCell.getWayToChargingStation());
                stationIdx = 0;
                isReturningFromStation = true;  // 标记正在返回
            }
        } else if (isReturningFromStation && currentX == lastCleaningX && currentY == lastCleaningY) {
            // 已返回到最后清扫的位置
            stationPath = null;
            stationIdx = 0;
            isReturningFromStation = false;
        } else if (stationIdx < stationPath.size() - 1) {
            stationIdx++;
        }
    }

    /**
     * Main navigation step function that determines whether to continue cleaning
     * or handle charging station navigation.
     */
    public void stepNavigation() {
        if (stationPath != null && !stationPath.isEmpty()) {
            stationNavigation();
        } else if (dirtService.isCleaningActive()) {
            cleaningNavigation();
        }
    }

    /**
     * Implements the cleaning navigation logic using DFS.
     * Handles dirt cleaning, battery monitoring, and path planning to charging stations.
     */
    private void cleaningNavigation() {
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

        int floorPowerCost = sensorSimulatorService.getSurfacePowerCost(currentX, currentY);
        batteryService.consumePower(floorPowerCost);
        
        if (currentCell.getDirtLevel() > 0) {
            dirtService.cleanDirt(currentX, currentY);
        }

        // 检查是否需要返回充电站
        if (dirtService.isFullDirt() || batteryService.isRechargeNeeded(getBatteryNeededToNearestStation())) {
            // 存储最后清扫的位置
            lastCleaningX = currentX;
            lastCleaningY = currentY;
            
            dirtService.stopCleaningMode();
            Cell nearestStation = findNearestChargingStationCell(currentX, currentY);
            if (nearestStation != null) {
                stationPath = new ArrayList<>(currentCell.getWayToChargingStation());
                Collections.reverse(stationPath);  // 反转路径以便前往充电站
                stationIdx = 0;
                isReturningFromStation = false;
                return;
            }
        }

        activityLogger.logMovement(currentX, currentY, "Visiting");

        Cell nextCell = getNeighborCell();
        if (nextCell != null) {
            stack.push(nextCell);
            nextCell.setVisited(true);
        } else {
            stack.pop();
        }

        if (stack.isEmpty()) {
            if (!isAtAnyChargingStation()) {
                lastCleaningX = currentX;
                lastCleaningY = currentY;
                dirtService.stopCleaningMode();
                Cell nearestStation = findNearestChargingStationCell(currentX, currentY);
                if (nearestStation != null) {
                    stationPath = new ArrayList<>(currentCell.getWayToChargingStation());
                    Collections.reverse(stationPath);
                    stationIdx = 0;
                    isReturningFromStation = false;
                }
            } else {
                activityLogger.logMovement(currentX, currentY, "All cells visited, navigation completed");
                isNavigationCompleted = true;
            }
        }
    }

    /**
     * Finds an unvisited neighboring cell for DFS navigation.
     * Returns null if no valid neighbors are available.
     */
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

    /**
     * Validates if a move to the specified coordinates is within bounds.
     */
    private boolean isValidMove(int x, int y) {
        return x >= 0 && y >= 0 && x < floorMap.getCells().length && y < floorMap.getCells()[0].length;
    }

    /**
     * Returns the current position of the robot as an array [x, y].
     */
    public int[] getCurrentPosition() {
        return new int[]{currentX, currentY};
    }

    /**
     * Checks if the navigation process is completed.
     */
    public boolean isNavigationCompleted() {
        return isNavigationCompleted;
    }

    /**
     * Finds the nearest charging station coordinates using Manhattan distance.
     */
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

    /**
     * Initializes optimal paths from all charging stations to all cells
     * using Dijkstra's algorithm.
     */
    private void initializeAllPaths() {
        // Find paths from all charging stations and update each cell with the shortest path
        List<int[]> chargingStations = findAllChargingStations();
        for (Cell[] row : floorMap.getCells()) {
            for (Cell cell : row) {
                List<int[]> shortestPath = null;
                int shortestDistance = Integer.MAX_VALUE;
                
                // Find shortest path from each charging station
                for (int[] station : chargingStations) {
                    List<int[]> pathFromStation = findShortestPath(station[0], station[1], cell.getX(), cell.getY());
                    if (pathFromStation != null && pathFromStation.size() < shortestDistance) {
                        shortestDistance = pathFromStation.size();
                        shortestPath = pathFromStation;
                    }
                }
                
                if (shortestPath != null) {
                    cell.setWayToChargingStation(shortestPath);
                }
            }
        }
    }

    /**
     * Returns a list of all charging station coordinates in the floor map.
     */
    private List<int[]> findAllChargingStations() {
        List<int[]> stations = new ArrayList<>();
        int size = floorMap.getCells().length;
        
        // Add the four corner charging stations
        stations.add(new int[]{0, 0});           // Top-left
        stations.add(new int[]{0, size-1});      // Top-right
        stations.add(new int[]{size-1, 0});      // Bottom-left
        stations.add(new int[]{size-1, size-1}); // Bottom-right
        
        return stations;
    }

    /**
     * Implements Dijkstra's algorithm to find the shortest path between two points.
     * Avoids obstacles and considers valid moves only.
     */
    private List<int[]> findShortestPath(int startX, int startY, int targetX, int targetY) {
        int rows = floorMap.getCells().length;
        int cols = floorMap.getCells()[0].length;
        
        // Initialize distances and visited array
        int[][] distances = new int[rows][cols];
        boolean[][] visited = new boolean[rows][cols];
        // Change to store parent coordinates as a 2D array of Point objects
        Point[][] parent = new Point[rows][cols];
        
        // Initialize distances to infinity
        for (int i = 0; i < rows; i++) {
            Arrays.fill(distances[i], Integer.MAX_VALUE);
        }
        
        // Priority queue to store cells to visit (distance, x, y)
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        
        // Start from charging station
        distances[startX][startY] = 0;
        pq.offer(new int[]{0, startX, startY});
        
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int x = current[1];
            int y = current[2];
            
            if (visited[x][y]) continue;
            visited[x][y] = true;
            
            // If we reached the target cell
            if (x == targetX && y == targetY) {
                return reconstructPath(parent, startX, startY, targetX, targetY);
            }
            
            // Check all four directions
            for (Direction direction : Direction.values()) {
                int newX = x + direction.getXOffset();
                int newY = y + direction.getYOffset();
                
                if (isValidMove(newX, newY) && !visited[newX][newY] && !sensorSimulatorService.isObstacle(newX, newY)) {
                    int newDist = distances[x][y] + 1;
                    
                    if (newDist < distances[newX][newY]) {
                        distances[newX][newY] = newDist;
                        parent[newX][newY] = new Point(x, y);  // Store parent coordinates as Point
                        pq.offer(new int[]{newDist, newX, newY});
                    }
                }
            }
        }
        
        return null; // No path found
    }

    /**
     * Helper class to store coordinates for path reconstruction.
     */
    private static class Point {
        int x, y;
        
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Reconstructs the path from parent pointers after pathfinding.
     */
    private List<int[]> reconstructPath(Point[][] parent, int startX, int startY, int targetX, int targetY) {
        List<int[]> path = new ArrayList<>();
        int currentX = targetX;
        int currentY = targetY;
        
        // Build path from target back to start
        while (currentX != startX || currentY != startY) {
            path.add(0, new int[]{currentX, currentY});
            Point p = parent[currentX][currentY];
            if (p == null) {
                return null; // No valid path exists
            }
            currentX = p.x;
            currentY = p.y;
        }
        
        // Add the starting point
        path.add(0, new int[]{startX, startY});
        return path;
    }

    /**
     * Checks if the robot is currently at any charging station.
     */
    private boolean isAtAnyChargingStation() {
        return floorMap.getCells()[currentX][currentY].isChargingStation();
    }

    /**
     * Calculates the battery power needed to reach the nearest charging station.
     */
    private int getBatteryNeededToNearestStation() {
        Cell nearestStation = findNearestChargingStationCell(currentX, currentY);
        if (nearestStation == null) return Integer.MAX_VALUE;
        
        List<int[]> pathToStation = floorMap.getCells()[currentX][currentY].getWayToChargingStation();
        if (pathToStation == null) return Integer.MAX_VALUE;
        
        return pathToStation.size() * 2; // 考虑路径上每个格子的基本消耗
    }

    /**
     * Returns the Cell object of the nearest charging station.
     */
    private Cell findNearestChargingStationCell(int x, int y) {
        int[] nearest = findNearestChargingStation(x, y);
        return floorMap.getCells()[nearest[0]][nearest[1]];
    }
}



