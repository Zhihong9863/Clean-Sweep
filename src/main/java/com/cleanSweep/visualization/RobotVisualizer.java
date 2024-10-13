package com.cleanSweep.visualization;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RobotVisualizer {

    private static final int CELL_SIZE = 50;  // Each cell is 50x50 pixels
    private GraphicsContext gc;

    public RobotVisualizer(GraphicsContext gc) {
        this.gc = gc;
    }

    // Draws the robot at the given grid coordinates (x, y)
    public void renderRobot(int x, int y) {
        // Clear the previous robot's position (clear the entire canvas)
        gc.clearRect(0, 0, CELL_SIZE * 10, CELL_SIZE * 10);  // Assumes a 10x10 grid for now

        // Draw the robot as a blue square at the new position
        gc.setFill(Color.BLUE);
        gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }
}
