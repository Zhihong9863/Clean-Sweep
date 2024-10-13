/* 3.3 RobotVisualizer
Folder: /src/main/java/com/cleanSweep/visualization/
Description: Responsible for drawing the robot on the grid and updating its position based on movement.
Parameters:
GraphicsContext gc: Graphics context to draw the robot.
int CELL_SIZE: The size of each grid cell (default 50x50 pixels).
Methods:
RobotVisualizer(GraphicsContext gc): Constructor to initialize the robot visualizer.
void renderRobot(int x, int y): Draws the robot at the specified coordinates on the grid.
void clearRobot(int x, int y): Clears the previous robot position before rendering the new one.
*/
package com.cleanSweep.visualization;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RobotVisualizer {

    private static final int CELL_SIZE = 50;  // 50x50 pixels per grid cell
    private GraphicsContext gc;

    public RobotVisualizer(GraphicsContext gc) {
        this.gc = gc;
    }

    /**
     * Draws the robot (Clean Sweep) at the specified grid coordinates.
     * @param x The x-coordinate of the robot's position.
     * @param y The y-coordinate of the robot's position.
     */
    public void renderRobot(int x, int y) {
        // Clear previous robot position
        gc.clearRect(0, 0, CELL_SIZE * 10, CELL_SIZE * 10);  // Assuming a 10x10 grid for now

        // Draw the robot as a blue square at the new position
        gc.setFill(Color.BLUE);
        gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }
}
