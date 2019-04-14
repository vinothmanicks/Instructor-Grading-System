package com.cannapaceus.jfx;

import com.cannapaceus.grader.DBService;
import com.cannapaceus.services.EmailService;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class SettingsController {
    ScreenController sc;
    DBService db;
    EmailService em;


    @FXML
    JFXTextField tfInstructorEmail;

    @FXML
    JFXPasswordField tfInstructorEmailPass;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        db = DBService.getInstance();
        em = EmailService.getInstance();

        tfInstructorEmail.setText(em.getsFromAddress());
        tfInstructorEmailPass.setText(em.getsPassword());
    }

    public void saveClick(ActionEvent event) {
        if(!formValidate())
            return;

        db.updateEmail(tfInstructorEmail.getText());
        db.updateEmailPass(tfInstructorEmailPass.getText());

        em.setsFromAddress(tfInstructorEmail.getText());
        em.setsPassword(tfInstructorEmailPass.getText());
    }

    private boolean formValidate() {
        boolean isAllValid = true;
        if (!tfInstructorEmail.validate())
            isAllValid = false;

        if (!tfInstructorEmailPass.validate())
            isAllValid = false;

        return isAllValid;
    }


}
