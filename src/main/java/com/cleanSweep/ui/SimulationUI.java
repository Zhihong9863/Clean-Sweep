package com.cleanSweep.ui;

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
    private double robotX = 375;
    private double robotY = 275;
    private double robotSpeed = 1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Clean Sweep Simulation");

        // Initialize the canvas
        setupCanvas();

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
        // Simple movement logic: move right, then down, then left, then up
        if (robotX < 750 && robotY == 275) {
            robotX += robotSpeed;
        } else if (robotX >= 750 && robotY < 525) {
            robotY += robotSpeed;
        } else if (robotX > 375 && robotY >= 525) {
            robotX -= robotSpeed;
        } else if (robotX <= 375 && robotY > 275) {
            robotY -= robotSpeed;
        }
    }

    private void renderEnvironment() {
        // Clear the canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw a simple grid
        gc.setStroke(Color.LIGHTGRAY);
        for (int i = 0; i < canvas.getWidth(); i += 50) {
            gc.strokeLine(i, 0, i, canvas.getHeight());
        }
        for (int i = 0; i < canvas.getHeight(); i += 50) {
            gc.strokeLine(0, i, canvas.getWidth(), i);
        }

        // Draw obstacles
        gc.setFill(Color.RED);
        gc.fillRect(150, 150, 50, 50); // Example obstacle
        gc.fillRect(300, 400, 50, 50); // Example obstacle

        // Draw dirt
        gc.setFill(Color.BROWN);
        gc.fillOval(200, 200, 20, 20); // Example dirt
        gc.fillOval(500, 300, 20, 20); // Example dirt

        // Draw the robot
        gc.setFill(Color.BLUE);
        gc.fillOval(robotX, robotY, 50, 50); // Draw a circle representing the robot
    }
}
