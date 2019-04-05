package com.cannapaceus.jfx;

import com.cannapaceus.services.EmailService;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javax.swing.*;
import java.util.Collections;

public class SettingsController {

    ScreenController sc;
    Model md;


    @FXML
    JFXTextField tfInstructorEmail;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        //selectedInstructor = md.getSelectedCourse();

        tfInstructorEmail.setText("BubbaDubba420@gmail.com");
    }

    @FXML
    public void cancelClick(ActionEvent event) {

    }

    public void saveClick(ActionEvent event) {
        if(formValidate())
            return;
    }

    private boolean formValidate() {
        boolean isAllValid = true;
        if (!tfInstructorEmail.validate())
            isAllValid = false;

        if(!EmailService.checkEmail(tfInstructorEmail.getText()))
            isAllValid = false;

        return true;
    }


}
