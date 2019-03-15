package com.cannapaceus.jfx;

import java.net.URL;

import com.cannapaceus.grader.DBService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController {
    ScreenController sc;
    DBService db = null;

    @FXML
    private TextField inUsername;

    @FXML
    public TextField inPassword;

    @FXML
    private void initialize()
    {
        db = DBService.getInstance();

        sc = ScreenController.getInstance();
    }

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        if (db.loginDB(inUsername.getText(), inPassword.getText())){
            sc.activate("Terms");
        }
        else {
            //login error message
        }
    }
}
