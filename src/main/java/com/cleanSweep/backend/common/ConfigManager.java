package com.cleanSweep.backend.common;

import lombok.Getter;

@Getter
public class ConfigManager {

    private static final String FLOOR_PLAN_FILE_PATH = "src/main/resources/templates/floorPlan.json";

    public static String getFloorPlanFilePath() {
        return FLOOR_PLAN_FILE_PATH;
    }
}
