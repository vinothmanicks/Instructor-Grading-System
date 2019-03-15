package com.cannapaceus.jfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class TermsController {
    ScreenController sc;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        System.out.println("TermsController");
    }

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        sc.activate("Login");
    }
}
