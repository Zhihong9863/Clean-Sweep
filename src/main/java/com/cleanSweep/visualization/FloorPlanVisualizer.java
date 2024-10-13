/*3.2 FloorPlanVisualizer
Folder: /src/main/java/com/cleanSweep/visualization/
Description: Responsible for drawing the floor plan, including the grid, surfaces, obstacles, and charging stations.
Parameters:
GraphicsContext gc: Graphics context to draw the grid.
int gridWidth, gridHeight: Dimensions of the floor grid.
Methods:
FloorPlanVisualizer(GraphicsContext gc, int gridWidth, int gridHeight): Constructor to initialize the floor plan visualizer.
void drawFloorPlan(): Draws the floor plan grid based on the current environment (surfaces, obstacles, charging stations).
void highlightChargingStations(): Highlights charging station cells on the grid.    
 */
package com.cleanSweep.visualization;

import com.cleanSweep.sensor.SensorSimulator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FloorPlanVisualizer {

    private static final int CELL_SIZE = 50;  // 50x50 pixels per cell
    private int gridWidth;
    private int gridHeight;
    private GraphicsContext gc;

    public FloorPlanVisualizer(GraphicsContext gc, int gridWidth, int gridHeight) {
        this.gc = gc;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }

    /**
     * Draws the entire floor plan based on sensor simulation data.
     * @param sensorSimulator The sensor simulator providing obstacle, surface, and dirt data.
     */
    public void drawFloorPlan(SensorSimulator sensorSimulator) {
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                drawCell(x, y, sensorSimulator);
            }
        }
    }

    /**
     * Draws a specific cell of the grid based on its properties (obstacle, surface, dirt).
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     * @param sensorSimulator The sensor simulator providing grid data.
     */
    private void drawCell(int x, int y, SensorSimulator sensorSimulator) {
        // Set cell color based on surface type
        int surfaceType = sensorSimulator.getSurfaceType(x, y);
        switch (surfaceType) {
            case SensorSimulator.BARE_FLOOR:
                gc.setFill(Color.LIGHTGRAY);
                break;
            case SensorSimulator.LOW_PILE_CARPET:
                gc.setFill(Color.BEIGE);
                break;
            case SensorSimulator.HIGH_PILE_CARPET:
                gc.setFill(Color.BROWN);
                break;
        }

        // Draw the surface
        gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        // Draw the obstacle
        if (sensorSimulator.isObstacle(x, y)) {
            gc.setFill(Color.BLACK);
            gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);  // Mark obstacle cells
        }

        // Draw the dirt (if present)
        if (sensorSimulator.isDirtPresent(x, y)) {
            gc.setFill(Color.DARKRED);
            gc.fillOval(x * CELL_SIZE + 15, y * CELL_SIZE + 15, 20, 20);  // Small circle to indicate dirt
        }

        // Draw grid lines
        gc.setStroke(Color.GRAY);
        gc.strokeRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }
}
