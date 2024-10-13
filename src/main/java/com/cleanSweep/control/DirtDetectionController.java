package com.cleanSweep.control;

import com.cleanSweep.interfaces.Sensor;
import com.cleanSweep.logging.ActivityLogger;

public class DirtDetectionController {

    private Sensor sensor;
    private DirtHandler dirtHandler;
    private ActivityLogger activityLogger;

    public DirtDetectionController(Sensor sensor, DirtHandler dirtHandler, ActivityLogger activityLogger) {
        this.sensor = sensor;
        this.dirtHandler = dirtHandler;
        this.activityLogger = activityLogger;
    }

    public void checkAndCleanDirt(int x, int y) {
        if (sensor.isDirtPresent(x, y)) {
            dirtHandler.cleanDirt(x, y);
            activityLogger.logDirtCleaned(x, y);
        } else {
            activityLogger.logNoDirtAtPosition(x, y);
        }
    }
}
