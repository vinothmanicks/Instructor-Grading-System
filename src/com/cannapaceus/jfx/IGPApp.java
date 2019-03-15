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
            Scene scene = new Scene(root);
            // Set the Scene to the Stage
            primaryStage.setScene(scene);
            // Set the Title to the Stage
            primaryStage.setTitle("A simple FXML Example");
            // Display the Stage
            primaryStage.show();

            ScreenController sc = ScreenController.getInstance();
            sc.setMain(scene);
            sc.addScreen("Login", FXMLLoader.load(getClass().getResource("../jfxml/LoginView.fxml")));
            sc.addScreen("Terms", FXMLLoader.load(getClass().getResource("../jfxml/TermsView.fxml")));
            sc.activate("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
