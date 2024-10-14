package com.cleanSweep.ui;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import com.cleanSweep.control.NavigationController;
import com.cleanSweep.control.PowerManagementController;
import com.cleanSweep.control.DirtHandler;
import com.cleanSweep.sensor.SensorSimulator;
import com.cleanSweep.visualization.FloorPlanVisualizer;
import com.cleanSweep.visualization.RobotVisualizer;

public class SimulationUI extends Application {

    private static final int GRID_WIDTH = 10;
    private static final int GRID_HEIGHT = 10;
    private static final int CELL_SIZE = 50;

    private Canvas canvas;
    private GraphicsContext gc;

    private SensorSimulator sensorSimulator;
    private NavigationController navigationController;
    private DirtHandler dirtHandler;
    private PowerManagementController powerManagementController;

    private FloorPlanVisualizer floorPlanVisualizer;
    private RobotVisualizer robotVisualizer;

    private AnimationTimer timer;
    private long lastUpdate = 0;

    private boolean movingRight = true;

    @Override
    public void start(Stage primaryStage) {
        // Initialize components
        sensorSimulator = new SensorSimulator(GRID_WIDTH, GRID_HEIGHT);
        navigationController = new NavigationController(0, 0);
        dirtHandler = new DirtHandler(GRID_WIDTH, GRID_HEIGHT);
        powerManagementController = new PowerManagementController();

        floorPlanVisualizer = new FloorPlanVisualizer(GRID_WIDTH, GRID_HEIGHT, CELL_SIZE, sensorSimulator, dirtHandler);
        robotVisualizer = new RobotVisualizer(CELL_SIZE, navigationController);

        // Set up canvas
        canvas = new Canvas(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE);
        gc = canvas.getGraphicsContext2D();

        // Set up controls
        Button startButton = new Button("Start");
        Button stopButton = new Button("Stop");

        startButton.setOnAction(e -> timer.start());
        stopButton.setOnAction(e -> timer.stop());

        HBox controls = new HBox(10, startButton, stopButton);
        controls.setPadding(new Insets(10));

        // Layout
        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        root.setBottom(controls);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Clean Sweep Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start animation
        startAnimation();
    }

    private void startAnimation() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 500_000_000) { // 500 milliseconds
                    update();
                    render();
                    lastUpdate = now;
                }
            }
        };
    }

    private void update() {
        int[] position = navigationController.getCurrentPosition();

        if (movingRight) {
            if (navigationController.canMoveRight(GRID_WIDTH, sensorSimulator)) {
                navigationController.moveRight();
            } else {
                movingRight = false;
                if (navigationController.canMoveDown(GRID_HEIGHT, sensorSimulator)) {
                    navigationController.moveDown();
                } else if (navigationController.canMoveLeft(sensorSimulator)) {
                    navigationController.moveLeft();
                }
            }
        } else {
            if (navigationController.canMoveLeft(sensorSimulator)) {
                navigationController.moveLeft();
            } else {
                movingRight = true;
                if (navigationController.canMoveDown(GRID_HEIGHT, sensorSimulator)) {
                    navigationController.moveDown();
                } else if (navigationController.canMoveRight(GRID_WIDTH, sensorSimulator)) {
                    navigationController.moveRight();
                }
            }
        }

        // Clean the current position
        dirtHandler.cleanDirt(position[0], position[1]);

        // Update power management
        powerManagementController.consumePower();
    }

    private void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw environment
        floorPlanVisualizer.render(gc);

        // Draw robot
        robotVisualizer.render(gc);
    }

    public static void main(String[] args) {
        launch(args);
    }
}