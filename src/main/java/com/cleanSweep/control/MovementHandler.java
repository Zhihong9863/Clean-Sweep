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

import com.cleanSweep.control.DirtHandler;
import com.cleanSweep.control.PowerManagementController;

public class MovementHandler {

    private NavigationController navigationController;
    private DirtHandler dirtHandler;
    private PowerManagementController powerManagementController;

    public MovementHandler(NavigationController navigationController, DirtHandler dirtHandler, PowerManagementController powerManagementController) {
        this.navigationController = navigationController;
        this.dirtHandler = dirtHandler;
        this.powerManagementController = powerManagementController;
    }

    public void moveRight() {
        navigationController.moveRight();
        postMoveActions();
    }

    public void moveLeft() {
        navigationController.moveLeft();
        postMoveActions();
    }

    public void moveDown() {
        navigationController.moveDown();
        postMoveActions();
    }

    private void postMoveActions() {
        int[] position = navigationController.getCurrentPosition();
        dirtHandler.cleanDirt(position[0], position[1]);
        powerManagementController.consumePower();
    }
}
