package com.cleanSweep.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class StatusPanel extends VBox {

    private Label batteryStatusLabel;
    private Label dirtCapacityLabel;

    public StatusPanel() {
        // Initialize status labels
        batteryStatusLabel = new Label("Battery: 100%");
        dirtCapacityLabel = new Label("Dirt Capacity: 0 / 50");

        // Add labels to the VBox layout
        this.getChildren().addAll(batteryStatusLabel, dirtCapacityLabel);
    }

    /**
     * Updates the battery status label.
     * @param batteryLife The current battery percentage.
     */
    public void updateBatteryStatus(int batteryLife) {
        batteryStatusLabel.setText("Battery: " + batteryLife + "%");
    }

    /**
     * Updates the dirt capacity label.
     * @param currentDirt The amount of dirt collected.
     * @param maxDirtCapacity The maximum dirt capacity.
     */
    public void updateDirtCapacity(int currentDirt, int maxDirtCapacity) {
        dirtCapacityLabel.setText("Dirt Capacity: " + currentDirt + " / " + maxDirtCapacity);
    }
}
