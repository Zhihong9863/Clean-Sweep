//package com.cleanSweep.frontend.visualization;
//
//import com.cleanSweep.backend.application.NavigationService;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.paint.Color;
//
//public class RobotVisualizer {
//
//    private final NavigationService navigationService;
//
//    public RobotVisualizer(NavigationService navigationService) {
//        this.navigationService = navigationService;
//    }
//
//    public void render(GraphicsContext gc) {
//        int[] position = navigationService.getCurrentPosition();
//        gc.setFill(Color.BLUE);
//        gc.fillOval(position[0] * 50, position[1] * 50, 50, 50);
//    }
//}
//
package com.cleanSweep.frontend.visualization;

import com.cleanSweep.backend.application.NavigationService;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RobotVisualizer {

    private final NavigationService navigationService;
    private final int cellSize; // Injected cell size

    public RobotVisualizer(NavigationService navigationService, int cellSize) {
        this.navigationService = navigationService;
        this.cellSize = cellSize;
    }

    public void render(GraphicsContext gc) {
        int[] position = navigationService.getCurrentPosition();
        gc.setFill(Color.BLUE);
        gc.fillOval(position[0] * cellSize, position[1] * cellSize, cellSize, cellSize);
    }
}
