package com.cleanSweep.control;

import com.cleanSweep.sensor.SensorSimulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PowerManagementController {

    private static final Logger logger = LoggerFactory.getLogger(PowerManagementController.class);

    private int batteryLife = 250;  // Initial full battery capacity
    private SensorSimulator sensorSimulator;
    private boolean returningToBase = false;

    public PowerManagementController(SensorSimulator sensorSimulator) {
        this.sensorSimulator = sensorSimulator;
    }

    // Consumes power based on surface type
    public void consumePower(int x, int y) {
        if (batteryLife <= 0) {
            logger.warn("Battery drained. Shutting down.");
            return;
        }

        int powerConsumed = sensorSimulator.getSurfacePowerCost(x, y);
        batteryLife -= powerConsumed;
        logger.info("Consumed " + powerConsumed + " units of power. Battery remaining: " + batteryLife);
    }

    // Checks if it's time to return to the charging station
    public boolean shouldReturnToBase() {
        return batteryLife <= (250 / 10);  // Return to base when battery is below 10%
    }

    public int getBatteryLife() {
        return batteryLife;
    }

    public void recharge() {
        batteryLife = 250;  // Recharges to full
        logger.info("Battery recharged to full.");
    }

    public void setReturningToBase(boolean returningToBase) {
        this.returningToBase = returningToBase;
    }

    public boolean isReturningToBase() {
        return returningToBase;
    }
}
