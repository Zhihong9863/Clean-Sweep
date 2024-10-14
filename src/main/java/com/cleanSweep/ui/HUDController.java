package com.cleanSweep.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import com.cleanSweep.control.NavigationController;
import com.cleanSweep.control.PowerManagementController;
import com.cleanSweep.control.DirtHandler;
import com.cleanSweep.interfaces.Sensor;
import com.cleanSweep.logging.ActivityLogger;

public class HUDController extends VBox {

    private Label batteryLabel;
    private Label dirtCapacityLabel;
    private PowerManagementController powerManagementController;
    private DirtHandler dirtHandler;

    public HUDController(PowerManagementController powerManagementController, DirtHandler dirtHandler) {
        this.powerManagementController = powerManagementController;
        this.dirtHandler = dirtHandler;

        batteryLabel = new Label();
        dirtCapacityLabel = new Label();

        this.getChildren().addAll(batteryLabel, dirtCapacityLabel);
        update();
    }

    public void update() {
        batteryLabel.setText("Battery: " + powerManagementController.getBatteryLevel() + "%");
        dirtCapacityLabel.setText("Dirt Capacity: " + dirtHandler.getCurrentCapacity() + "/" + dirtHandler.getMaxCapacity());
    }
}
