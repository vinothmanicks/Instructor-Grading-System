package com.cannapaceus.jfx;

import com.cannapaceus.grader.*;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.util.Collections;

public class StudentFormController {

    ScreenController sc = null;
    Model md = null;

    Student selectedStudent;

    @FXML
    private JFXTextField tfStudentFMName;

    @FXML
    private JFXTextField tfStudentLName;

    @FXML
    private JFXTextField tfStudentID;

    @FXML
    private JFXTextField tfStudentEmail;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        sc = ScreenController.getInstance();

        selectedStudent = md.getSelectedStudent();
    }


    public void saveClick(ActionEvent event) {
        if (!formValidate())
            return;

        selectedStudent.setFirstMIName(tfStudentFMName.getText());
        selectedStudent.setLastName(tfStudentLName.getText());
        selectedStudent.setStudentID(tfStudentID.getText());
        selectedStudent.setStudentEmail(tfStudentEmail.getText());

        if (selectedStudent.getDBID() == 0 && !md.getNewObjects().contains(selectedStudent)) {
            md.addNewObject(selectedStudent);
            for (Grade g : selectedStudent.getGrades()) {
                if (!md.getNewObjects().contains(g))
                    md.addNewObject(g);
            }
        } else {
            md.addUpdatedObject(selectedStudent);
        }

        //Might need this for students
        //Collections.sort(md.getTerms());

        try {
            sc.addScreen("Course", FXMLLoader.load(getClass().getResource("../jfxml/CourseView.fxml")));
            sc.activate("Course");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelClick(ActionEvent event) {
        if (selectedStudent.getDBID() == 0 && !md.getNewObjects().contains(selectedStudent)) {
            md.removeStudent(selectedStudent);
        }

        try {
            sc.addScreen("Course", FXMLLoader.load(getClass().getResource("../jfxml/CourseView.fxml")));
            sc.activate("Course");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean formValidate() {
        if (!tfStudentFMName.validate())
            return false;

        if (!tfStudentLName.validate())
            return false;

        if(!tfStudentID.validate())
            return false;

        if(!tfStudentEmail.validate())
            return false;

        return true;
    }
}
