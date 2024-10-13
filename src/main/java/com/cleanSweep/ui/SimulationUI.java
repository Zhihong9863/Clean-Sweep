/*
 * 3.1 SimulationUI
Folder: /src/main/java/com/cleanSweep/ui/
Description: Handles the graphical user interface for the Clean Sweep simulation. It initializes the canvas and renders the robot, floor plan, dirt, and obstacles.
Parameters:
Canvas canvas: The drawing surface for the simulation.
GraphicsContext gc: The graphics context used to draw on the canvas.
Methods:
void start(Stage primaryStage): Initializes the UI and starts the simulation.
void setupCanvas(): Prepares the canvas for drawing the floor plan and robot.
void renderEnvironment(): Renders the entire environment, including obstacles, dirt, and the robot's position. */
 

package com.cleanSweep.ui;

import com.cleanSweep.control.NavigationController;
import com.cleanSweep.control.PowerManagementController;
import com.cleanSweep.control.DirtHandler;
import com.cleanSweep.sensor.SensorSimulator;
import com.cleanSweep.visualization.FloorPlanVisualizer;
import com.cleanSweep.visualization.RobotVisualizer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cleanSweep.logging.ActivityLogger;
import com.cleanSweep.interfaces.Sensor;


public class SimulationUI extends Application {

    private static final Logger logger = LoggerFactory.getLogger(SimulationUI.class);

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting Clean Sweep Simulation UI");

        // Root layout for the simulation
        BorderPane root = new BorderPane();

        // Create Canvas for grid and robot
        Canvas canvas = new Canvas(500, 500);  // 10x10 grid (50x50 pixels per cell)
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.setCenter(canvas);

        // Create a Status Panel
        StatusPanel statusPanel = new StatusPanel();
        root.setRight(new HBox(statusPanel));  // Status Panel on the right side

        // Initialize sensor simulator (room layout and objects)
        SensorSimulator sensorSimulator = new SensorSimulator(10, 10);  // 10x10 grid
        sensorSimulator.initializeFloorPlan();  // Initialize obstacles, surfaces, dirt locations

        // Initialize visualizers
        FloorPlanVisualizer floorPlanVisualizer = new FloorPlanVisualizer(gc, 10, 10);
        RobotVisualizer robotVisualizer = new RobotVisualizer(gc);

        // Initialize Navigation Controller for movement
        ActivityLogger activityLogger = ActivityLogger.getInstance();
        Sensor sensorSimulatorAsSensor = new SensorSimulator(10, 10);  // Use as Sensor
        PowerManagementController powerManagementController = new PowerManagementController(sensorSimulatorAsSensor, activityLogger);
        DirtHandler dirtHandler = new DirtHandler(sensorSimulatorAsSensor, activityLogger);
        NavigationController navigationController = new NavigationController(0, 0, sensorSimulatorAsSensor, robotVisualizer, activityLogger, powerManagementController, dirtHandler);

        // Draw initial floor plan and robot
        floorPlanVisualizer.drawFloorPlan(sensorSimulator);
        final int[] currentPosition = navigationController.getCurrentPosition();
        robotVisualizer.renderRobot(currentPosition[0], currentPosition[1]);

        // Set up the scene and add event listeners for movement (example: arrow keys)
        Scene scene = new Scene(root, 800, 600);
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    navigationController.moveUp();
                    break;
                case DOWN:
                    navigationController.moveDown();
                    break;
                case LEFT:
                    navigationController.moveLeft();
                    break;
                case RIGHT:
                    navigationController.moveRight();
                    break;
                default:
                    break;
            }
            // Update robot position and status panel after every move
            int[] newPosition = navigationController.getCurrentPosition();
            currentPosition[0] = newPosition[0];
            currentPosition[1] = newPosition[1];
            robotVisualizer.renderRobot(currentPosition[0], currentPosition[1]);
            statusPanel.updateBatteryStatus(navigationController.getBatteryLife());
            statusPanel.updateDirtCapacity(navigationController.getCurrentDirtCapacity(), navigationController.getMaxDirtCapacity());
        });

        primaryStage.setTitle("Clean Sweep Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();

        logger.info("Simulation UI initialized and rendered");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
