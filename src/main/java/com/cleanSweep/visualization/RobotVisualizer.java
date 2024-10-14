package com.cleanSweep.visualization;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.cleanSweep.control.NavigationController;

public class RobotVisualizer {

    private int cellSize;
    private NavigationController navigationController;

    public RobotVisualizer(int cellSize, NavigationController navigationController) {
        this.cellSize = cellSize;
        this.navigationController = navigationController;
    }

    public void render(GraphicsContext gc) {
        int[] position = navigationController.getCurrentPosition();
        gc.setFill(Color.BLUE);
        gc.fillOval(position[0] * cellSize, position[1] * cellSize, cellSize, cellSize);
    }
}