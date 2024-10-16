package com.cleanSweep.backend.application;

import com.cleanSweep.backend.infrastructure.ActivityLogger;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BatteryService {

    @Getter
    private int batteryLevel;

    @Value("${clean-sweep.battery.low-threshold}")
    private int lowBatteryThreshold;

    @Value("${clean-sweep.battery.full-charge}")
    private int fullChargeValue;

    @Autowired
    private ActivityLogger activityLogger;

    @PostConstruct
    public void init() {
        this.batteryLevel = fullChargeValue;  // Initialize after dependency injection is completed
    }

    public void consumePower(int dirtLevel) {
        if (batteryLevel > dirtLevel) {
            batteryLevel -= dirtLevel;
            activityLogger.logBatteryUsage(batteryLevel);
        } else {
            System.out.println("Battery depleted. Returning to charging station.");
            recharge();
        }
    }

    public boolean isBatteryDepleted() {
        return batteryLevel <= 0;
    }

    public boolean hasSufficientPower() {
        return batteryLevel > lowBatteryThreshold;
    }

    public void recharge() {
        batteryLevel = fullChargeValue;
        activityLogger.logRecharge();
    }
}

