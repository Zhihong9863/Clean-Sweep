/* 
 * 4.1 ActivityLogger
Folder: /src/main/java/com/cleanSweep/logging/
Description: Logs every action the robot performs, such as movements, cleaning actions, and power consumption. These logs are essential for diagnostics and analysis.
Parameters:
Logger logger: SLF4J logger instance.
String logFile: The path to the log file.
Methods:
ActivityLogger(String logFile): Constructor to initialize the logger.
void logMovement(int x, int y): Logs the robot's movement to new coordinates.
void logDirtCleaned(int x, int y): Logs the cleaning of dirt at a specific location.
void logBatteryUsage(int batteryLife): Logs the current battery level.
void logRecharge(): Logs when the robot reaches the charging station and starts recharging.
 */
package com.cleanSweep.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class ActivityLogger {
    
    private static class Holder {
        private static final ActivityLogger INSTANCE = new ActivityLogger();
    }

    // Private constructor to prevent instantiation
    private ActivityLogger() {
        try {
            logWriter = new FileWriter("clean_sweep_activity_log.txt", true); // Append to log file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Public method to provide access to the singleton instance
    public static ActivityLogger getInstance() {
        return Holder.INSTANCE;
    }

    private FileWriter logWriter;

    // Log a generic message with a timestamp
    public void logEvent(String event) {
        try {
            logWriter.write(LocalDateTime.now() + " - " + event + "\n");
            logWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Log movement-related actions
    public void logMovement(int x, int y, String direction) {
        logEvent("Moved to (" + x + ", " + y + ") in direction: " + direction);
    }

    // Log cleaning-related actions
    public void logCleaning(int x, int y) {
        logEvent("Cleaned dirt at (" + x + ", " + y + ")");
    }

    // Log power consumption
    public void logPower(int remainingBattery) {
        logEvent("Battery remaining: " + remainingBattery + " units");
    }

    // Log the robot returning to the charging station
    public void logChargingEvent() {
        logEvent("Returning to charging station.");
    }

    // Log when the dirt capacity is full
    public void logDirtFull() {
        logEvent("Dirt capacity is full.");
    }

    // Ensure to close the logger when done
    public void closeLogger() {
        try {
            logWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logDirtCleaned(int x, int y) {
        logEvent("Dirt cleaned at (" + x + ", " + y + ")");
    }

    public void logNoDirtAtPosition(int x, int y) {
        logEvent("No dirt at position (" + x + ", " + y + ")");
    }

    public void logRecharge() {
        logEvent("Battery recharged to full capacity.");
    }

    public void logBatteryUsage(int batteryLife) {
        logEvent("Battery usage logged. Current battery life: " + batteryLife + "%");
    }
}
