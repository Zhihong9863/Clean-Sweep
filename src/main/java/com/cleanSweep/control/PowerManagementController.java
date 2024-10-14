/*
 * 1.3 PowerManagementController
Folder: /src/main/java/com/cleanSweep/control/
Description: Manages the power consumption of the Clean Sweep robot. It tracks battery life and determines when to return to the charging station.
Parameters:
int batteryLife: The current battery level (starts at 250 units).
SensorSimulator sensorSimulator: Used to determine the power cost of moving across different surface types.
Methods:
PowerManagementController(SensorSimulator sensorSimulator): Constructor to initialize power management.
void consumePower(int x, int y): Consumes battery power based on the surface at the given coordinates.
boolean shouldReturnToBase(): Returns true if the battery level is low (below 10%).
int getBatteryLife(): Returns the remaining battery life.
void recharge(): Recharges the battery to full (250 units).
 */
package com.cleanSweep.control;

public class PowerManagementController {

    private int batteryLevel = 100;

    public void consumePower() {
        batteryLevel--;
        System.out.println("Battery level: " + batteryLevel + "%");
        if (batteryLevel <= 0) {
            System.out.println("Battery depleted. Returning to charging station.");
            // Implement return to charging logic
        }
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }
}