package com.cleanSweep.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HUDController {

    private Label batteryLabel;
    private Label statusLabel;

    public HUDController(VBox hudBox) {
        batteryLabel = new Label("Battery: 100%");
        statusLabel = new Label("Status: Idle");
        hudBox.getChildren().addAll(batteryLabel, statusLabel);
    }

    public void updateBattery(int batteryLevel) {
        batteryLabel.setText("Battery: " + batteryLevel + "%");
    }

    public void updateStatus(String status) {
        statusLabel.setText("Status: " + status);
    }
}
