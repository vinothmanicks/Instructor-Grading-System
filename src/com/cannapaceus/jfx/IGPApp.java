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
            FXMLLoader loader = new FXMLLoader();
            // Path to the FXML File
            String fxmlDocPath = "./src/com/cannapaceus/jfxml/LoginView.fxml";
            FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);

            // Create the Pane and all Details
            Parent root = loader.load(fxmlStream);

            // Create the Scene
            Scene scene = new Scene(root, 800, 800);
            // Set the Scene to the Stage
            primaryStage.setScene(scene);
            // Set the Title to the Stage
            primaryStage.setTitle("Instructor Grading Program");
            // Display the Stage
            primaryStage.show();

            ScreenController sc = ScreenController.getInstance();
            sc.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
