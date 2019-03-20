package com.cannapaceus.jfx;

import com.cannapaceus.grader.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CourseController {
    ScreenController sc = null;
    Model md = null;

    @FXML
    private Label lblCourseName;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        lblCourseName.setText(md.getSelectedCourse().getCourseName());
    }

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        sc.activate("Terms");
    }
}
