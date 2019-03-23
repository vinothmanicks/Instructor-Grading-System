package com.cannapaceus.jfx;

import java.net.URL;

import com.cannapaceus.grader.DBService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController {
    ScreenController sc = null;
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

    public void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            login();
        }
    }

    public void loginClick(ActionEvent actionEvent) {
        login();
    }

    private void login() {
        if (db.loginDB(inUsername.getText(), inPassword.getText())){
            try {
                sc.setRoot(FXMLLoader.load(getClass().getResource("../jfxml/GraderView.fxml")));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        else {
            //login error message
        }
    }
}
