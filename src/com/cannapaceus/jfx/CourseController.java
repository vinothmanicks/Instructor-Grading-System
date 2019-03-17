package com.cannapaceus.jfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class CourseController {
    ScreenController sc = null;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
    }

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        sc.activate("Terms");
    }
}
