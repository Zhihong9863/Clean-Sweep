package com.cleanSweep.backend.application.interfaces;

public interface Sensor {
    boolean isDirtPresent(int x, int y);
    void cleanDirt(int x, int y);
    boolean isObstacle(int x, int y);
    String getSurfaceType(int x, int y);
    int getSurfacePowerCost(int x, int y);
}
