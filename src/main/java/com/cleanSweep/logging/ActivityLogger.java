package com.cleanSweep.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityLogger {

    private static final Logger logger = LoggerFactory.getLogger(ActivityLogger.class);

    // Logs robot movements
    public static void logMovement(int x, int y) {
        logger.info("Moved to position (" + x + ", " + y + ")");
    }

    // Logs cleaning activity
    public static void logCleaning(int x, int y) {
        logger.info("Cleaned dirt at position (" + x + ", " + y + ")");
    }

    // Logs power consumption
    public static void logPowerUsage(int remainingPower) {
        logger.info("Power remaining: " + remainingPower + " units");
    }

    // Logs when the robot returns to the charging station
    public static void logReturningToBase() {
        logger.info("Returning to charging station.");
    }

    // Logs recharging activity
    public static void logRecharging() {
        logger.info("Battery recharged.");
    }
}
