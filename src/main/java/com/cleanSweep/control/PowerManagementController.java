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

import com.cleanSweep.interfaces.Sensor;
import com.cleanSweep.logging.ActivityLogger;

public class PowerManagementController {

    private int batteryLife = 250;
    private Sensor sensor;
    private ActivityLogger activityLogger;

    public PowerManagementController(Sensor sensor, ActivityLogger activityLogger) {
        this.sensor = sensor;
        this.activityLogger = activityLogger;
    }

    public void consumePower(int x, int y) {
        int powerConsumed = sensor.getSurfacePowerCost(x, y);
        batteryLife -= powerConsumed;
        activityLogger.logBatteryUsage(batteryLife);
    }

    public boolean shouldReturnToBase() {
        return batteryLife <= (250 / 10);  // Return to base when battery is below 10%
    }

    public int getBatteryLife() {
        return batteryLife;
    }

    public void recharge() {
        batteryLife = 250;
        activityLogger.logRecharge();
    }
}
