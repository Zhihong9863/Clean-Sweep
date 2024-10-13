package com.cleanSweep.control;

import com.cleanSweep.sensor.SensorSimulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirtDetectionController {

    private static final Logger logger = LoggerFactory.getLogger(DirtDetectionController.class);
    private SensorSimulator sensorSimulator;
    private int dirtCapacity = 50;  // Max capacity for dirt storage
    private int currentDirt = 0;    // Amount of dirt collected

    public DirtDetectionController(SensorSimulator sensorSimulator) {
        this.sensorSimulator = sensorSimulator;
    }

    // Cleans the current location if dirt is detected
    public void cleanLocation(int x, int y) {
        if (sensorSimulator.isDirtPresent(x, y)) {
            if (currentDirt < dirtCapacity) {
                sensorSimulator.cleanDirt(x, y);
                currentDirt++;
                logger.info("Dirt cleaned at (" + x + ", " + y + "). Current dirt capacity: " + currentDirt);
            } else {
                logger.warn("Dirt capacity full! Returning to base.");
            }
        } else {
            logger.info("No dirt found at (" + x + ", " + y + ")");
        }
    }

    public boolean isCapacityFull() {
        return currentDirt >= dirtCapacity;
    }

    public int getCurrentDirtCapacity() {
        return currentDirt;
    }
}
