package com.cannapaceus.jfx;

import com.cannapaceus.grader.Assignment;
import com.cannapaceus.grader.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AssignmentController {
    ScreenController sc = null;
    Model md = null;

    Assignment selectedAssignment;

    @FXML
    private Label lblAssignmentName;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        selectedAssignment = md.getSelectedAssignment();

        lblAssignmentName.setText(selectedAssignment.getAssignmentName());
    }

    public void backClick(ActionEvent actionEvent) {
        sc.activate("Course");
    }
}
