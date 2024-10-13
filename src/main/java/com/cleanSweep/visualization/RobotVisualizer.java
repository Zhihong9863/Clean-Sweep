package com.cleanSweep.visualization;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RobotVisualizer {

    private static final int CELL_SIZE = 25;  // Match the cell size used in FloorPlanVisualizer
    private GraphicsContext gc;

    public RobotVisualizer(GraphicsContext gc) {
        this.gc = gc;
    }

    // Draws the robot at the given grid coordinates (x, y)
    public void renderRobot(int x, int y) {
        System.out.println("Rendering robot at: (" + x + ", " + y + ")");
        System.out.println("Canvas size: " + gc.getCanvas().getWidth() + "x" + gc.getCanvas().getHeight());
        gc.setFill(Color.BLUE);
        gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE); // Simplified rendering logic
    }

    // Add this method to render the charging station
    public void renderChargingStation(int x, int y) {
        gc.setFill(Color.GREEN); // Assuming green for the charging station
        gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }
}
