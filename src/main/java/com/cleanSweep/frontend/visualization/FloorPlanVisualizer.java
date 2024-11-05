package com.cleanSweep.frontend.visualization;

import com.cleanSweep.backend.application.BatteryService;
import com.cleanSweep.backend.application.DirtService;
import com.cleanSweep.backend.application.SensorSimulatorService;
import com.cleanSweep.backend.domain.FloorMap;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class FloorPlanVisualizer {

    private final SensorSimulatorService sensorSimulatorService;
    private final DirtService dirtService;
    private final BatteryService batteryService;
    private final FloorMap floorMap;
    private final int gridSize; // Injected grid size
    private final int cellSize; // Injected cell size

    public FloorPlanVisualizer(SensorSimulatorService sensorSimulatorService, DirtService dirtService,
            BatteryService batteryService,
            FloorMap floorMap, int gridSize, int cellSize) {
        this.sensorSimulatorService = sensorSimulatorService;
        this.dirtService = dirtService;
        this.batteryService = batteryService;
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
                gc.strokeRect(x * cellSize, y * cellSize, cellSize, cellSize); // Dynamically render the grid cells
                                                                               // based on size

                // Special case for charging station at (0,0)
                if (x == 0 && y == 0) {
                    gc.setFill(Color.GREEN); // Set (0,0) as blue (charging station)
                    gc.fillRect(y * cellSize, x * cellSize, cellSize, cellSize);
                    continue; // Skip further processing for this cell
                }

                // Get dirt level and render the appropriate color
                int dirtLevel = floorMap.getCells()[x][y].getDirtLevel();
                if (dirtLevel > 0) {
                    double redShade = 0.3 + ((4 - dirtLevel) / 3.0) * 0.7; // Adjust color intensity
                    gc.setFill(Color.color(redShade, 0, 0));
                    gc.fillRect(y * cellSize, x * cellSize, cellSize, cellSize);
                }

                // Render obstacle in black
                if (sensorSimulatorService.isObstacle(x, y)) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(y * cellSize, x * cellSize, cellSize, cellSize);
                }
            }
        }

        // Render color legend below the grid
        renderColorLegend(gc);
    }

    private void renderColorLegend(GraphicsContext gc) {
        gc.setFont(new Font(14));

        // Starting coordinates for the left and right parts of the legend
        double leftLegendX = 10;
        double rightLegendX = 270; // Adjust this value based on spacing
        double legendStartY = gridSize * cellSize + 20;

        // Spacing between each legend entry to ensure visibility
        double entrySpacing = 22;

        // Battery and Dirt Progress Bars (Left Side)
        double barWidth = 100; // Width of the progress bar
        double barHeight = 20; // Height of the progress bar

        // Battery Progress Bar
        double batteryPercentage = (double) batteryService.getBattery() / batteryService.getFullChargeValue();
        gc.setFill(Color.GRAY); // Background for the battery bar
        gc.fillRect(leftLegendX, legendStartY, barWidth, barHeight);
        gc.setFill(Color.GREEN); // Fill color for battery level
        gc.fillRect(leftLegendX, legendStartY, barWidth * batteryPercentage, barHeight);
        gc.setFill(Color.BLACK);
        gc.fillText("Battery: " + batteryService.getBattery() + "/" + batteryService.getFullChargeValue(),
                leftLegendX + barWidth + 10, legendStartY + barHeight - 5);

        // Dirt Progress Bar
        double dirtPercentage = (double) dirtService.getCurrentCapacity() / dirtService.getDirtCapacity();
        gc.setFill(Color.GRAY); // Background for the dirt bar
        gc.fillRect(leftLegendX, legendStartY + entrySpacing, barWidth, barHeight);
        gc.setFill(Color.BROWN); // Fill color for dirt level
        gc.fillRect(leftLegendX, legendStartY + entrySpacing, barWidth * dirtPercentage, barHeight);
        gc.setFill(Color.BLACK);
        gc.fillText("Dirt: " + dirtService.getCurrentCapacity() + "/" + dirtService.getDirtCapacity(),
                leftLegendX + barWidth + 10, legendStartY + entrySpacing + barHeight - 5);

        String txt = dirtService.isCleaningActive()
                ? "The vacuum is cleaning"
                : "The vacuum is returning to the\nstation to recharge and empty dirt";

        String[] lines = txt.split("\n");

        gc.fillText("Status:", leftLegendX, legendStartY + entrySpacing * 2 + barHeight - 5);

        double lineYOffset = legendStartY + entrySpacing * 2 + barHeight - 5; // Starting Y position
        double lineSpacing = 15; 

        for (int i = 0; i < lines.length; i++) {
            gc.fillText(lines[i], leftLegendX + 50, lineYOffset + (i * lineSpacing));
        }

        // Existing Legend Entries for Dirt Levels, Obstacles, and Charging Station
        // (Right Side)
        gc.setFill(Color.color(1, 0.3, 0.3)); // Light red for Dirt Level 1
        gc.fillRect(rightLegendX, legendStartY, 15, 15);
        gc.setFill(Color.BLACK);
        gc.fillText("Light Red - Dirt Level 1 (1 unit)", rightLegendX + 30, legendStartY + 15);

        gc.setFill(Color.color(1, 0, 0)); // Normal red for Dirt Level 2
        gc.fillRect(rightLegendX, legendStartY + entrySpacing, 15, 15);
        gc.setFill(Color.BLACK);
        gc.fillText("Normal Red - Dirt Level 2 (2 units)", rightLegendX + 30, legendStartY + entrySpacing + 15);

        gc.setFill(Color.color(0.7, 0, 0)); // Dark red for Dirt Level 3
        gc.fillRect(rightLegendX, legendStartY + entrySpacing * 2, 15, 15);
        gc.setFill(Color.BLACK);
        gc.fillText("Dark Red - Dirt Level 3 (3 units)", rightLegendX + 30, legendStartY + entrySpacing * 2 + 15);

        gc.setFill(Color.BLACK); // Black for obstacle
        gc.fillRect(rightLegendX, legendStartY + entrySpacing * 3, 15, 15);
        gc.setFill(Color.BLACK);
        gc.fillText("Black - Obstacle", rightLegendX + 30, legendStartY + entrySpacing * 3 + 15);

        gc.setFill(Color.GREEN); // Green for charging station
        gc.fillRect(rightLegendX, legendStartY + entrySpacing * 4, 15, 15);
        gc.setFill(Color.BLACK);
        gc.fillText("Green - Charging Station", rightLegendX + 30, legendStartY + entrySpacing * 4 + 15);
    }

}
