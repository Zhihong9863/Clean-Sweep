package com.cleanSweep.frontend.visualization;

import com.cleanSweep.backend.application.BatteryService;
import com.cleanSweep.backend.application.DirtService;
import com.cleanSweep.backend.application.SensorSimulatorService;
import com.cleanSweep.backend.domain.Cell;
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
        // Render the grid
        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                Cell currentCell = floorMap.getCells()[x][y];

                // First render floor type
                switch (currentCell.getFloorType()) {
                    case BARE_FLOOR:
                        gc.setFill(Color.LIGHTGRAY);
                        break;
                    case LOW_PILE_CARPET:
                        gc.setFill(Color.KHAKI);
                        break;
                    case HIGH_PILE_CARPET:
                        gc.setFill(Color.SANDYBROWN);
                        break;
                }
                gc.fillRect(y * cellSize, x * cellSize, cellSize, cellSize);

                // Then render charging stations
                if (currentCell.isChargingStation()) {
                    gc.setFill(Color.GREEN);
                    gc.fillRect(y * cellSize, x * cellSize, cellSize, cellSize);
                }

                // Then render dirt on top if present
                if (currentCell.getDirtLevel() > 0) {
                    gc.setFill(Color.RED);
                    gc.fillRect(y * cellSize, x * cellSize, cellSize, cellSize);
                }

                // Finally render obstacles
                if (sensorSimulatorService.isObstacle(x, y)) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(y * cellSize, x * cellSize, cellSize, cellSize);
                }

                // Draw grid lines last to ensure they're always visible
                gc.setStroke(Color.GRAY);
                gc.strokeRect(y * cellSize, x * cellSize, cellSize, cellSize);
            }
        }

        renderColorLegend(gc);
    }

    private void renderColorLegend(GraphicsContext gc) {
        gc.setFont(new Font(14));

        double leftLegendX = 10;
        double rightLegendX = 270;
        double legendStartY = gridSize * cellSize + 20;
        double entrySpacing = 22;
        double barWidth = 100;
        double barHeight = 20;

        // Battery Progress Bar
        double batteryPercentage = (double) batteryService.getBattery() / batteryService.getFullChargeValue();
        
        // 绘制电池进度条背景
        gc.setFill(Color.GRAY);
        gc.fillRect(leftLegendX, legendStartY, barWidth, barHeight);
        
        // 根据电量百分比设置颜色
        Color batteryColor;
        if (batteryPercentage > 0.75) {
            batteryColor = Color.GREEN;  // 75-100% 绿色
        } else if (batteryPercentage > 0.5) {
            batteryColor = Color.ORANGE;  // 50-75% 橙色
        } else if (batteryPercentage > 0.25) {
            batteryColor = Color.YELLOW;  // 25-50% 黄色
        } else {
            batteryColor = Color.RED;  // 0-25% 红色
        }
        
        gc.setFill(batteryColor);
        gc.fillRect(leftLegendX, legendStartY, barWidth * batteryPercentage, barHeight);
        gc.setFill(Color.BLACK);
        gc.fillText("Battery: " + batteryService.getBattery() + "/" + batteryService.getFullChargeValue(),
                leftLegendX + barWidth + 10, legendStartY + barHeight - 5);

        // Dirt Progress Bar
        double dirtPercentage = (double) dirtService.getCurrentCapacity() / dirtService.getDirtCapacity();
        
        // 绘制垃圾进度条背景
        gc.setFill(Color.GRAY);
        gc.fillRect(leftLegendX, legendStartY + entrySpacing, barWidth, barHeight);
        
        // 根据垃圾容量百分比设置颜色
        Color dirtColor;
        if (dirtPercentage < 0.25) {
            dirtColor = Color.GREEN;  // 0-25% 绿色
        } else if (dirtPercentage < 0.5) {
            dirtColor = Color.YELLOW;  // 25-50% 黄色
        } else if (dirtPercentage < 0.75) {
            dirtColor = Color.ORANGE;  // 50-75% 橙色
        } else {
            dirtColor = Color.RED;  // 75-100% 红色
        }
        
        gc.setFill(dirtColor);
        gc.fillRect(leftLegendX, legendStartY + entrySpacing, barWidth * dirtPercentage, barHeight);
        gc.setFill(Color.BLACK);
        gc.fillText("Dirt: " + dirtService.getCurrentCapacity() + "/" + dirtService.getDirtCapacity(),
                leftLegendX + barWidth + 10, legendStartY + entrySpacing + barHeight - 5);

        // Status text
        String txt = dirtService.isCleaningActive()
                ? "The vacuum is cleaning"
                : "The vacuum is returning to the\nstation to recharge and empty dirt";

        String[] lines = txt.split("\n");
        gc.fillText("Status:", leftLegendX, legendStartY + entrySpacing * 2 + barHeight - 5);
        double lineYOffset = legendStartY + entrySpacing * 2 + barHeight - 5;
        double lineSpacing = 15;
        for (int i = 0; i < lines.length; i++) {
            gc.fillText(lines[i], leftLegendX + 50, lineYOffset + (i * lineSpacing));
        }

        // Floor Types Legend (Right Side)
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(rightLegendX, legendStartY, 15, 15);
        gc.setFill(Color.BLACK);
        gc.fillText("Bare Floor (1 power unit)", rightLegendX + 30, legendStartY + 15);

        gc.setFill(Color.KHAKI);
        gc.fillRect(rightLegendX, legendStartY + entrySpacing, 15, 15);
        gc.setFill(Color.BLACK);
        gc.fillText("Low Pile Carpet (2 power units)", rightLegendX + 30, legendStartY + entrySpacing + 15);

        gc.setFill(Color.SANDYBROWN);
        gc.fillRect(rightLegendX, legendStartY + entrySpacing * 2, 15, 15);
        gc.setFill(Color.BLACK);
        gc.fillText("High Pile Carpet (3 power units)", rightLegendX + 30, legendStartY + entrySpacing * 2 + 15);

        gc.setFill(Color.RED);
        gc.fillRect(rightLegendX, legendStartY + entrySpacing * 3, 15, 15);
        gc.setFill(Color.BLACK);
        gc.fillText("Dirt", rightLegendX + 30, legendStartY + entrySpacing * 3 + 15);

        gc.setFill(Color.BLACK);
        gc.fillRect(rightLegendX, legendStartY + entrySpacing * 4, 15, 15);
        gc.setFill(Color.BLACK);
        gc.fillText("Obstacle", rightLegendX + 30, legendStartY + entrySpacing * 4 + 15);

        gc.setFill(Color.GREEN);
        gc.fillRect(rightLegendX, legendStartY + entrySpacing * 5, 15, 15);
        gc.setFill(Color.BLACK);
        gc.fillText("Charging Station", rightLegendX + 30, legendStartY + entrySpacing * 5 + 15);
    }

}
