package com.cannapaceus.jfx;

import com.cannapaceus.grader.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StudentController {
    ScreenController sc = null;
    Model md = null;

    Student selectedStudent;

    @FXML
    private Label lblStudentName;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        selectedStudent = md.getSelectedStudent();

        lblStudentName.setText(selectedStudent.getFirstMIName() + " " + selectedStudent.getLastName());
    }

    public void backClick(ActionEvent actionEvent) {
        sc.activate("Course");
    }

    public void commitClick(ActionEvent e) {
        md.commitChanges();
    }

    public void revertClick(ActionEvent e) {
        md.revertChanges();
    }
}
