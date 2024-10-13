package com.cleanSweep.visualization;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FloorPlanVisualizer {

    private static final int CELL_SIZE = 50;  // Each cell is 50x50 pixels
    private int gridWidth;
    private int gridHeight;

    private GraphicsContext gc;

    public FloorPlanVisualizer(GraphicsContext gc, int gridWidth, int gridHeight) {
        this.gc = gc;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }

    // Draws the grid-based floor plan
    public void drawFloorPlan() {
        gc.setStroke(Color.GRAY);  // Set grid lines color to gray

        // Draw the grid (10x10 cells for now)
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                gc.strokeRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);  // Draw each cell
            }
        }
    }
}
