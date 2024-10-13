package com.cleanSweep.ui;

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

        // Render the initial environment
        renderEnvironment();
    }

    private void setupCanvas() {
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
    }

    private void renderEnvironment() {
        // Clear the canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Example rendering: Draw a simple grid
        gc.setStroke(Color.LIGHTGRAY);
        for (int i = 0; i < canvas.getWidth(); i += 50) {
            gc.strokeLine(i, 0, i, canvas.getHeight());
        }
        for (int i = 0; i < canvas.getHeight(); i += 50) {
            gc.strokeLine(0, i, canvas.getWidth(), i);
        }

        // Example rendering: Draw a robot
        gc.setFill(Color.BLUE);
        gc.fillOval(375, 275, 50, 50); // Draw a circle representing the robot
    }
}
