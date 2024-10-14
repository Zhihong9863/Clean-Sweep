package com.cleanSweep.visualization;

import com.cleanSweep.sensor.SensorSimulator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FloorPlanVisualizer {
    private static final int CELL_SIZE = 25;  // Common cell size
    private static final int GRID_WIDTH = 20;
    private static final int GRID_HEIGHT = 20;
    private GraphicsContext gc;
    private SensorSimulator sensorSimulator;

    public FloorPlanVisualizer(GraphicsContext gc, SensorSimulator sensorSimulator) {
        this.gc = gc;
        this.sensorSimulator = sensorSimulator;
    }

    public void drawFloorPlan() {
        System.out.println("Drawing floor plan");
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                String surfaceType = sensorSimulator.getSurfaceType(x, y);
                switch (surfaceType) {
                    case "carpet":
                        gc.setFill(Color.BEIGE);
                        break;
                    case "hardwood":
                        gc.setFill(Color.SADDLEBROWN);
                        break;
                    case "tile":
                        gc.setFill(Color.LIGHTGRAY);
                        break;
                    default:
                        gc.setFill(Color.WHITE);
                        break;
                }

                if (sensorSimulator.isStair(x, y)) {
                    gc.setFill(Color.ORANGE); // Color for stairs
                } else if (sensorSimulator.isObstacle(x, y)) {
                    gc.setFill(Color.GRAY);
                } else if (sensorSimulator.isDirtPresent(x, y)) {
                    gc.setFill(Color.RED);
                }

                gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }
}
