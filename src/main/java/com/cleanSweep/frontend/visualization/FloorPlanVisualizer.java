//package com.cleanSweep.frontend.visualization;
//
//import com.cleanSweep.backend.application.DirtService;
//import com.cleanSweep.backend.application.SensorSimulatorService;
//import com.cleanSweep.backend.domain.FloorMap;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//
//public class FloorPlanVisualizer {
//
//    private final SensorSimulatorService sensorSimulatorService;
//    private final DirtService dirtService;
//    private final FloorMap floorMap;
//
//    public FloorPlanVisualizer(SensorSimulatorService sensorSimulatorService, DirtService dirtService, FloorMap floorMap) {
//        this.sensorSimulatorService = sensorSimulatorService;
//        this.dirtService = dirtService;
//        this.floorMap = floorMap;
//    }
//
//    public void render(GraphicsContext gc) {
//
//        // Dynamically determine the grid size from floorMap
//        int gridWidth = floorMap.getCells().length;
//        int gridHeight = floorMap.getCells()[0].length;
//
//        // Render the grid
//        for (int x = 0; x < gridWidth; x++) {
//            for (int y = 0; y < gridHeight; y++) {
//                gc.setStroke(Color.GRAY);
//                gc.strokeRect(x * 50, y * 50, 50, 50); // Render the grid cells
//
//                // Special case for charging station at (0,0)
//                if (x == 0 && y == 0) {
//                    gc.setFill(Color.GREEN); // Set (0,0) as blue (charging station)
//                    gc.fillRect(x * 50, y * 50, 50, 50);
//                    continue; // Skip further processing for this cell
//                }
//
//                // Get dirt level and render the appropriate color
//                int dirtLevel = floorMap.getCells()[x][y].getDirtLevel();
//                if (dirtLevel > 0) {
//                    double redShade = 0.3 + (dirtLevel / 3.0) * 0.7; // Adjust color intensity
//                    gc.setFill(Color.color(redShade, 0, 0));
//                    gc.fillRect(x * 50, y * 50, 50, 50);
//                }
//
//                // Render obstacle in black
//                if (sensorSimulatorService.isObstacle(x, y)) {
//                    gc.setFill(Color.BLACK);
//                    gc.fillRect(x * 50, y * 50, 50, 50);
//                }
//            }
//        }
//
//        // Render color legend below the grid
//        renderColorLegend(gc);
//    }
//
//    // Method to render the color legend
//    private void renderColorLegend(GraphicsContext gc) {
//
//        gc.setFont(new Font(14));
//
//        // Starting coordinates for the legend (below the grid)
//        double legendStartX = 10;
//        double legendStartY = 260; // Adjusted to appear below the grid
//
//        // Draw "Light Red - Dirt Level 1"
//        gc.setFill(Color.color(1, 0.3, 0.3)); // Light red
//        gc.fillRect(legendStartX, legendStartY, 20, 20);
//        gc.setFill(Color.BLACK);
//        gc.fillText("Light Red - Dirt Level 1", legendStartX + 30, legendStartY + 15);
//
//        // Draw "Normal Red - Dirt Level 2"
//        gc.setFill(Color.color(1, 0, 0)); // Normal red
//        gc.fillRect(legendStartX, legendStartY + 30, 20, 20);
//        gc.setFill(Color.BLACK);
//        gc.fillText("Normal Red - Dirt Level 2", legendStartX + 30, legendStartY + 45);
//
//        // Draw "Dark Red - Dirt Level 3"
//        gc.setFill(Color.color(0.7, 0, 0)); // Dark red
//        gc.fillRect(legendStartX, legendStartY + 60, 20, 20);
//        gc.setFill(Color.BLACK);
//        gc.fillText("Dark Red - Dirt Level 3", legendStartX + 30, legendStartY + 75);
//
//        // Draw "Black - Obstacle"
//        gc.setFill(Color.BLACK); // Black for obstacle
//        gc.fillRect(legendStartX, legendStartY + 90, 20, 20);
//        gc.setFill(Color.BLACK);
//        gc.fillText("Black - Obstacle", legendStartX + 30, legendStartY + 105);
//
//        // Draw "Blue - Charging Station"
//        gc.setFill(Color.GREEN); // Blue for charging station
//        gc.fillRect(legendStartX, legendStartY + 120, 20, 20);
//        gc.setFill(Color.BLACK);
//        gc.fillText("Green - Charging Station", legendStartX + 30, legendStartY + 135);
//    }
//}
package com.cleanSweep.frontend.visualization;

import com.cleanSweep.backend.application.DirtService;
import com.cleanSweep.backend.application.SensorSimulatorService;
import com.cleanSweep.backend.domain.FloorMap;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class FloorPlanVisualizer {

    private final SensorSimulatorService sensorSimulatorService;
    private final DirtService dirtService;
    private final FloorMap floorMap;
    private final int gridSize; // Injected grid size
    private final int cellSize; // Injected cell size

    public FloorPlanVisualizer(SensorSimulatorService sensorSimulatorService, DirtService dirtService, FloorMap floorMap, int gridSize, int cellSize) {
        this.sensorSimulatorService = sensorSimulatorService;
        this.dirtService = dirtService;
        this.floorMap = floorMap;
        this.gridSize = gridSize;
        this.cellSize = cellSize;
    }

    public void render(GraphicsContext gc) {
        // Dynamically determine the grid size from injected parameters

        // Render the grid
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                gc.setStroke(Color.GRAY);
                gc.strokeRect(x * cellSize, y * cellSize, cellSize, cellSize); // Dynamically render the grid cells based on size

                // Special case for charging station at (0,0)
                if (x == 0 && y == 0) {
                    gc.setFill(Color.GREEN); // Set (0,0) as blue (charging station)
                    gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    continue; // Skip further processing for this cell
                }

                // Get dirt level and render the appropriate color
                int dirtLevel = floorMap.getCells()[x][y].getDirtLevel();
                if (dirtLevel > 0) {
                    double redShade = 0.3 + (dirtLevel / 3.0) * 0.7; // Adjust color intensity
                    gc.setFill(Color.color(redShade, 0, 0));
                    gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                }

                // Render obstacle in black
                if (sensorSimulatorService.isObstacle(x, y)) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                }
            }
        }

        // Render color legend below the grid
        renderColorLegend(gc);
    }

    private void renderColorLegend(GraphicsContext gc) {
        gc.setFont(new Font(14));

        // Starting coordinates for the legend (below the grid)
        double legendStartX = 10;
        double legendStartY = gridSize * cellSize + 10; // Adjusted based on the dynamic grid size

        // Draw "Light Red - Dirt Level 1"
        gc.setFill(Color.color(1, 0.3, 0.3)); // Light red
        gc.fillRect(legendStartX, legendStartY, 20, 20);
        gc.setFill(Color.BLACK);
        gc.fillText("Light Red - Dirt Level 1", legendStartX + 30, legendStartY + 15);

        // Draw "Normal Red - Dirt Level 2"
        gc.setFill(Color.color(1, 0, 0)); // Normal red
        gc.fillRect(legendStartX, legendStartY + 30, 20, 20);
        gc.setFill(Color.BLACK);
        gc.fillText("Normal Red - Dirt Level 2", legendStartX + 30, legendStartY + 45);

        // Draw "Dark Red - Dirt Level 3"
        gc.setFill(Color.color(0.7, 0, 0)); // Dark red
        gc.fillRect(legendStartX, legendStartY + 60, 20, 20);
        gc.setFill(Color.BLACK);
        gc.fillText("Dark Red - Dirt Level 3", legendStartX + 30, legendStartY + 75);

        // Draw "Black - Obstacle"
        gc.setFill(Color.BLACK); // Black for obstacle
        gc.fillRect(legendStartX, legendStartY + 90, 20, 20);
        gc.setFill(Color.BLACK);
        gc.fillText("Black - Obstacle", legendStartX + 30, legendStartY + 105);

        // Draw "Green - Charging Station"
        gc.setFill(Color.GREEN); // Green for charging station
        gc.fillRect(legendStartX, legendStartY + 120, 20, 20);
        gc.setFill(Color.BLACK);
        gc.fillText("Green - Charging Station", legendStartX + 30, legendStartY + 135);
    }
}
