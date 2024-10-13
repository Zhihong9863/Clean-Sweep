package com.cleanSweep.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityLogger {

    private static final Logger logger = LoggerFactory.getLogger(ActivityLogger.class);

    // Logs robot movements
    public void logMovement(int x, int y) {
        logger.info("Moved to position (" + x + ", " + y + ")");
    }

    // Logs battery usage
    public void logBatteryUsage(int batteryLife) {
        logger.info("Battery remaining: " + batteryLife + " units");
    }

    // Logs when the battery is recharged
    public void logRecharge() {
        logger.info("Battery recharged to full capacity");
    }

    // Logs cleaning activity
    public void logCleaning(int x, int y) {
        logger.info("Cleaned dirt at (" + x + ", " + y + ")");
    }

    // Logs when the dirt capacity is full
    public void logDirtFull() {
        logger.info("Dirt capacity is full");
    }

    // Logs when no dirt is found at a position
    public void logNoDirtAtPosition(int x, int y) {
        logger.info("No dirt found at position (" + x + ", " + y + ")");
    }

    // Logs when dirt is cleaned
    public void logDirtCleaned(int x, int y) {
        logger.info("Dirt cleaned at position (" + x + ", " + y + ")");
    }
}
