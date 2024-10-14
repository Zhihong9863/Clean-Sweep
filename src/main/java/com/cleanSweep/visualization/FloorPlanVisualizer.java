package com.cleanSweep.visualization;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.cleanSweep.control.DirtHandler;
import com.cleanSweep.sensor.SensorSimulator;


public class FloorPlanVisualizer {

    private int gridWidth;
    private int gridHeight;
    private int cellSize;
    private SensorSimulator sensorSimulator;
    private DirtHandler dirtHandler;

    public FloorPlanVisualizer(int gridWidth, int gridHeight, int cellSize, SensorSimulator sensorSimulator, DirtHandler dirtHandler) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.cellSize = cellSize;
        this.sensorSimulator = sensorSimulator;
        this.dirtHandler = dirtHandler;
    }

    public void render(GraphicsContext gc) {
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                gc.setStroke(Color.GRAY);
                gc.strokeRect(x * cellSize, y * cellSize, cellSize, cellSize);

                // Draw dirt
                if (dirtHandler.isDirty(x, y)) {
                    gc.setFill(Color.BROWN);
                    gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                }

                // Draw obstacles
                if (sensorSimulator.isObstacle(x, y)) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                }
            }
        }
    }
}