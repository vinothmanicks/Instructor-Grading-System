package com.cannapaceus.services;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ErrorMessage
{
        /**
         * Creates a popup containing error without any pretty things for a graphical way to show errors
         * @param error Error string to show
         */
        public static void showError(String error) {
            if (Platform.isFxApplicationThread()) {
                Stage s = new Stage();
                VBox v = new VBox();
                v.getChildren().add(new Label("Error [" + error + ']'));
                Scene sc = new Scene(v);
                s.setTitle("Fail");
                s.setScene(sc);
                s.show();
            } else {
                Platform.runLater(() -> {
                    Stage s = new Stage();
                    VBox v = new VBox();
                    v.getChildren().add(new Label("Error [" + error + ']'));
                    Scene sc = new Scene(v);
                    s.setTitle("Fail");
                    s.setScene(sc);
                    s.show();
                });
            }
        }
}
