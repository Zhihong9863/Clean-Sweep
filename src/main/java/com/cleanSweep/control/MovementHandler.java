/*1.4 MovementHandler
Folder: /src/main/java/com/cleanSweep/control/
Description: Handles the actual movement of the robot across the grid. It updates the robot's position and handles directional commands.
Parameters:
int deltaX, deltaY: Changes in the x and y coordinates for movement.
NavigationController navigationController: Reference to the navigation controller.
Methods:
MovementHandler(NavigationController navigationController): Constructor to initialize movement handler.
void move(int deltaX, int deltaY): Moves the robot by the specified deltaX and deltaY.
boolean canMove(int x, int y): Checks if the robot can move to the specified coordinates (no obstacle). */

package com.cleanSweep.control;

import com.cleanSweep.interfaces.Sensor;

public class MovementHandler {

    private NavigationController navigationController;
    private DirtHandler dirtHandler;
    private PowerManagementController powerController;
    private Sensor sensor;

    public MovementHandler(NavigationController navigationController, DirtHandler dirtHandler, PowerManagementController powerController) {
        this.navigationController = navigationController;
        this.dirtHandler = dirtHandler;
        this.powerController = powerController;
    }

    public void moveAndClean() {
        try {
            // Example: Move right, clean the location, consume power
            navigationController.moveRight();
            int[] position = navigationController.getCurrentPosition();
            dirtHandler.cleanDirt(position[0], position[1]);
            powerController.consumePower(position[0], position[1]);

            if (dirtHandler.isCapacityFull() || powerController.shouldReturnToBase()) {
                returnToBase();
            }

            // Add a delay to slow down the movement
            Thread.sleep(500); // 500 milliseconds delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void returnToBase() {
        // Logic to navigate back to base when required
        // (This would involve more sophisticated logic, like pathfinding)
    }
}
