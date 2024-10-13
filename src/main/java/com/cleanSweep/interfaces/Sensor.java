package com.cleanSweep.interfaces;

public interface Sensor {

    boolean isObstacle(int x, int y);
    boolean isDirtPresent(int x, int y);
    void cleanDirt(int x, int y);
    int getSurfacePowerCost(int x, int y);
}
