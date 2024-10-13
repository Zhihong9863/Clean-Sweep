package com.cleanSweep.ui;

import com.cleanSweep.control.NavigationController;
import com.cleanSweep.sensor.SensorSimulator;
import com.cleanSweep.visualization.FloorPlanVisualizer;
import com.cleanSweep.visualization.RobotVisualizer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationUI extends Application {

    private static final Logger logger = LoggerFactory.getLogger(SimulationUI.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("Starting Clean Sweep Simulation UI");

        // Create the root layout
        BorderPane root = new BorderPane();

        // Create a Canvas for drawing the grid and the robot
        Canvas canvas = new Canvas(500, 500);  // Canvas size for 10x10 grid (50x50 pixels per cell)
        root.setCenter(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Initialize the visualizers
        FloorPlanVisualizer floorPlanVisualizer = new FloorPlanVisualizer(gc, 10, 10);
        RobotVisualizer robotVisualizer = new RobotVisualizer(gc);

        // Draw the floor plan (the grid)
        floorPlanVisualizer.drawFloorPlan();

        // Initialize the NavigationController and visualize the initial robot position
        SensorSimulator sensorSimulator = new SensorSimulator();
        NavigationController navigationController = new NavigationController(0, 0, sensorSimulator, robotVisualizer);
        robotVisualizer.renderRobot(navigationController.getCurrentX(), navigationController.getCurrentY());

        // Simulate movement: For now, move the robot to the right
        navigationController.moveRight();
        robotVisualizer.renderRobot(navigationController.getCurrentX(), navigationController.getCurrentY());

        // Set up the scene and stage
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Clean Sweep Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();

        logger.info("Simulation UI initialized and rendered");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
