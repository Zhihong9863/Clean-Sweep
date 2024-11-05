package com.cleanSweep.backend.application;

import com.cleanSweep.backend.domain.Cell;
import com.cleanSweep.backend.domain.FloorMap;
import com.cleanSweep.backend.infrastructure.ActivityLogger;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class DirtService {

    @Autowired
    private SensorSimulatorService sensorSimulatorService;

    @Autowired
    private FloorMap floorMap;

    @Autowired
    private BatteryService batteryService;

    @Autowired
    private ActivityLogger activityLogger;

    @Value("${clean-sweep.dirt.capacity}")
    private int dirtCapacity;

    private int currentCapacity = 0;

    private int mode = 0; // 0 is cleaning, 1 is stop cleaning

    public void cleanDirt(int x, int y) {
        Cell cell = floorMap.getCells()[x][y];
        int dirtLevel = cell.getDirtLevel();

        if (dirtLevel > 0 && currentCapacity < dirtCapacity) {
            // Reduce dirt
            cell.reduceDirtLevel();  // Clean up dirt
            currentCapacity++;
            activityLogger.logCleaning(x, y);

            if (currentCapacity >= dirtCapacity) {
                activityLogger.logDirtFull();
            }
        }
    }

    public void removeDirt(){
        currentCapacity = 0;
    }

    public void setCleaningMode(){
        mode = 0;
    }

    public void stopCleaningMode(){
        mode = 1;
    }

    public boolean isCleaningActive(){
        return mode == 0;
    }

    public boolean isFullDirt(){
        return currentCapacity>=dirtCapacity;
    }

    public boolean isDirtPresent(int x, int y) {
        return sensorSimulatorService.isDirtPresent(x, y);
    }
}


