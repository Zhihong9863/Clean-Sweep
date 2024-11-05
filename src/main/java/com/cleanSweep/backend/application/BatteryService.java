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
    private int battery;

    @Value("${clean-sweep.battery.low-threshold}")
    private int lowBatteryThreshold;

    @Getter
    @Value("${clean-sweep.battery.full-charge}")
    private int fullChargeValue;

    @Autowired
    private ActivityLogger activityLogger;

    @PostConstruct
    public void init() {
        this.battery = fullChargeValue;  // Initialize after dependency injection is completed
    }

    public void consumePower(int units) {
        if (battery > units) {
            battery -= units;
            activityLogger.logBatteryUsage(battery);
        } else {
            System.out.println("Battery depleted. Returning to charging station.");
            recharge();
        }
    }

    // need to recharge battery if current battery <= battery needed to reach the charging station + 4
    public boolean isRechargeNeeded(int batteryToReachStation){
        return battery<= batteryToReachStation + 4;
    }


    public boolean isBatteryDepleted() {
        return battery <= 0;
    }

    public boolean hasSufficientPower() {
        return battery > lowBatteryThreshold;
    }

    public void recharge() {
        battery = fullChargeValue;
        activityLogger.logRecharge();
    }
}

