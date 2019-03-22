package com.cannapaceus.jfx;

import com.cannapaceus.grader.Course;
import com.cannapaceus.grader.eSeason;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.util.Collections;

public class CourseFormController {
    ScreenController sc = null;
    Model md = null;

    Course selectedCourse;

    @FXML
    private JFXTextField tfCourseName;

    @FXML
    private JFXTextField tfCourseID;

    @FXML
    private JFXTextField tfCourseDept;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        sc.setBottomVisibility(false);

        selectedCourse = md.getSelectedCourse();

        tfCourseName.setText(selectedCourse.getCourseName());
        tfCourseID.setText(selectedCourse.getCourseID());
        tfCourseDept.setText(selectedCourse.getDepartment());

        tfCourseName.textProperty().addListener((o, oldVal, newVal) -> {
            tfCourseName.validate();
        });

        tfCourseID.textProperty().addListener((o, oldVal, newVal) -> {
            tfCourseID.validate();
        });

        tfCourseDept.textProperty().addListener((o, oldVal, newVal) -> {
            tfCourseDept.validate();
        });
    }

    public void cancelClick(ActionEvent event) {
        if (selectedCourse.getDBID() == 0 && !md.getNewObjects().contains(selectedCourse)) {
            md.removeCourse(selectedCourse);
        }

        try {
            sc.addScreen("Terms", FXMLLoader.load(getClass().getResource("../jfxml/TermsView.fxml")));
            sc.activate("Terms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveClick(ActionEvent event) {
        if (!formValidate())
            return;

        selectedCourse.setCourseName(tfCourseName.getText());
        selectedCourse.setCourseID(tfCourseID.getText());
        selectedCourse.setDepartment(tfCourseDept.getText());

        if (selectedCourse.getDBID() == 0 && !md.getNewObjects().contains(selectedCourse)) {
            md.addNewObject(selectedCourse);
        } else {
            md.addUpdatedObject(selectedCourse);
        }

        Collections.sort(md.getSelectedTerm().getCourses());

        try {
            sc.addScreen("Terms", FXMLLoader.load(getClass().getResource("../jfxml/TermsView.fxml")));
            sc.activate("Terms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean formValidate() {
        if (!tfCourseName.validate())
            return false;

        if (!tfCourseID.validate())
            return false;

        if (!tfCourseDept.validate())
            return false;

        return true;
    }
}
