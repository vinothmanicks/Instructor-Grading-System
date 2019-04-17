package com.cannapaceus.jfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;

public class IGPApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Create the FXMLLoader
//            FXMLLoader loader = new FXMLLoader();
            // Path to the FXML File
//            String fxmlDocPath = "./src/com/cannapaceus/jfxml/LoginView.fxml";
//            FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);

            // Create the Pane and all Details
            Parent root = FXMLLoader.load(getClass().getResource("/LoginView.fxml"));

            // Create the Scene
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/IGP.css").toExternalForm());
            // Set the Scene to the Stage
            primaryStage.setScene(scene);
            // Set the Title to the Stage
            primaryStage.setTitle("Instructor Grading Program");

            primaryStage.setMaximized(true);

            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            // Display the Stage
            primaryStage.show();

            ScreenController sc = ScreenController.getInstance();
            sc.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
