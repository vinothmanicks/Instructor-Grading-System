package com.cannapaceus.jfx;

import java.net.URL;

import com.cannapaceus.grader.DBService;
import com.jfoenix.controls.*;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class LoginController {
    ScreenController sc = null;
    DBService db = null;

    @FXML
    private JFXTextField inUsername;

    @FXML
    public JFXPasswordField inPassword;

    @FXML
    public StackPane spDialogPane;

    @FXML
    private void initialize()
    {
        db = DBService.getInstance();
        sc = ScreenController.getInstance();

        inUsername.textProperty().addListener((o, oldVal, newVal) -> {
            inUsername.validate();
        });

        inPassword.textProperty().addListener((o, oldVal, newVal) -> {
            inPassword.validate();
        });
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
        if (!formValidate())
            return;
        String errMessage = db.loginDB(inUsername.getText(), inPassword.getText());
        if (errMessage == null){
            try {
                sc.setRoot(FXMLLoader.load(getClass().getResource("../jfxml/GraderView.fxml")));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        else if (errMessage == "Database does not exist") {
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Failed to login"));
            content.setBody(new Text(errMessage + ". Would you like to create a database using the credentials provided?"));

            JFXButton yes = new JFXButton("Yes");

            JFXButton no = new JFXButton("No");

            content.setActions(yes, no);

            JFXDialog dialog = new JFXDialog(spDialogPane, content, JFXDialog.DialogTransition.CENTER);

            yes.setOnAction(event -> {
                db.databaseSetup();
                try {
                    sc.setRoot(FXMLLoader.load(getClass().getResource("../jfxml/GraderView.fxml")));
                } catch(Exception e) {
                    e.printStackTrace();
                }
                dialog.close();
            });

            no.setOnAction(event -> {
                dialog.close();
            });

            dialog.show();
        }
        else {
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Failed to login"));
            content.setBody(new Text(errMessage));

            JFXButton okay = new JFXButton("Okay");

            content.setActions(okay);

            JFXDialog dialog = new JFXDialog(spDialogPane, content, JFXDialog.DialogTransition.CENTER);

            okay.setOnAction(event -> {
                dialog.close();
            });

            dialog.show();
        }
    }

    private boolean formValidate() {
        boolean anyFail = true;

        if (!inUsername.validate())
            anyFail = false;
        if(!inPassword.validate())
            anyFail = false;

        return anyFail;
    }
}
