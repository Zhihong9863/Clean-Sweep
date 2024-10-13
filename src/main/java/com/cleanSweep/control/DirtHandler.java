/*
1.2 DirtHandler
Folder: /src/main/java/com/cleanSweep/control/
Description: Detects and cleans dirt when the robot encounters it. Keeps track of the robot's dirt collection and stops when full.
Parameters:
int currentDirt: Tracks how much dirt has been collected.
int dirtCapacity: The total dirt-carrying capacity (50 units).
SensorSimulator sensorSimulator: Reference to the simulator that detects dirt presence.
Methods:
DirtHandler(SensorSimulator sensorSimulator): Constructor to initialize the dirt handler.
void cleanDirt(int x, int y): Cleans the current location if dirt is detected.
boolean isCapacityFull(): Returns true if the robot has reached its dirt capacity.
int getCurrentDirt(): Returns the current amount of dirt collected.
*/
package com.cleanSweep.control;

import com.cleanSweep.interfaces.Sensor;
import com.cleanSweep.logging.ActivityLogger;

public class DirtHandler {

    private int currentDirt = 0;
    private final int dirtCapacity = 50;
    private Sensor sensor;
    private ActivityLogger activityLogger;

    public DirtHandler(Sensor sensor, ActivityLogger activityLogger) {
        this.sensor = sensor;
        this.activityLogger = activityLogger;
    }

    public void cleanDirt(int x, int y) {
        if (sensor.isDirtPresent(x, y) && currentDirt < dirtCapacity) {
            sensor.cleanDirt(x, y);
            currentDirt++;
            activityLogger.logCleaning(x, y); // Updated method name
        } else if (currentDirt >= dirtCapacity) {
            activityLogger.logDirtFull();   
        } else {
            activityLogger.logNoDirtAtPosition(x, y);
        }
    }

    public boolean isCapacityFull() {
        return currentDirt >= dirtCapacity;
    }

    public int getCurrentDirt() {
        return currentDirt;
    }

    public int getDirtCapacity() {
        return dirtCapacity;
    }
}
