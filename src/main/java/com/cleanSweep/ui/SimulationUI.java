package com.cleanSweep.ui;

import com.cleanSweep.control.DirtHandler;
import com.cleanSweep.control.NavigationController;
import com.cleanSweep.sensor.SensorSimulator;
import com.cleanSweep.visualization.RobotVisualizer;
import com.cleanSweep.logging.ActivityLogger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SimulationUI extends Application {

    private Canvas canvas;
    private GraphicsContext gc;
    private SensorSimulator sensorSimulator;
    private DirtHandler dirtHandler;
    private NavigationController navigationController;
    private RobotVisualizer robotVisualizer;
    private ActivityLogger activityLogger;
    private boolean movingRight;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Clean Sweep Simulation");

        // Initialize components
        setupCanvas();
        sensorSimulator = new SensorSimulator();
        robotVisualizer = new RobotVisualizer(gc);
        activityLogger = new ActivityLogger();

        // Ensure SensorSimulator implements Sensor
        dirtHandler = new DirtHandler(sensorSimulator, activityLogger);

        navigationController = new NavigationController(7, 5, sensorSimulator, robotVisualizer);

        // Create a layout and add the canvas
        StackPane root = new StackPane();
        root.getChildren().add(canvas);

        // Set up the scene
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start the animation loop
        startAnimation();
    }

    private void setupCanvas() {
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
    }

    private void startAnimation() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                renderEnvironment();
            }
        };
        timer.start();
    }

    private void update() {
        int[] position = navigationController.getCurrentPosition();
        
        if (movingRight) {
            if (position[0] < GRID_WIDTH - 1 && !sensorSimulator.isObstacle(position[0] + 1, position[1])) {
                navigationController.moveRight();
            } else {
                movingRight = false;
                if (position[1] < GRID_HEIGHT - 1 && !sensorSimulator.isObstacle(position[0], position[1] + 1)) {
                    navigationController.moveDown();
                } else {
                    movingRight = true; // Change direction if blocked
                }
            }
        } else {
            if (position[0] > 0 && !sensorSimulator.isObstacle(position[0] - 1, position[1])) {
                navigationController.moveLeft();
            } else {
                movingRight = true;
                if (position[1] < GRID_HEIGHT - 1 && !sensorSimulator.isObstacle(position[0], position[1] + 1)) {
                    navigationController.moveDown();
                } else {
                    movingRight = false; // Change direction if blocked
                }
            }
        }

        // Clean the current position
        dirtHandler.cleanDirt(position[0], position[1]);

        // Add a delay to slow down the movement
        try {
            Thread.sleep(500); // 500 milliseconds delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void renderEnvironment() {
        // Clear the canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw a complete grid
        gc.setStroke(Color.LIGHTGRAY);
        for (int i = 0; i <= canvas.getWidth(); i += 50) { // Include right edge
            gc.strokeLine(i, 0, i, canvas.getHeight());
        }
        for (int i = 0; i <= canvas.getHeight(); i += 50) { // Include bottom edge
            gc.strokeLine(0, i, canvas.getWidth(), i);
        }

        // Draw obstacles
        gc.setFill(Color.RED);
        gc.fillRect(150, 150, 50, 50); // Example obstacle
        gc.fillRect(300, 400, 50, 50); // Example obstacle

        // Draw dirt
        gc.setFill(Color.BROWN);
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                if (sensorSimulator.isDirtPresent(x, y)) {
                    gc.fillOval(x * 50 + 15, y * 50 + 15, 20, 20);
                }
            }
        }

        // Draw the robot
        int[] position = navigationController.getCurrentPosition();
        gc.setFill(Color.BLUE);
        gc.fillOval(position[0] * 50, position[1] * 50, 50, 50);
    }

    private static final int GRID_WIDTH = 20;  // Adjust based on your grid size
    private static final int GRID_HEIGHT = 20;
}
