package com.cannapaceus.jfx;

import com.cannapaceus.grader.*;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class AssignmentFormController {

    ScreenController sc = null;
    Model md = null;

    Assignment selectedAssignment;

    @FXML
    private JFXTextField tfAssignmentName;

    @FXML
    private JFXTextField tfMaxScore;

    @FXML
    private JFXTextField tfCustomWeight;

    @FXML
    private JFXComboBox<String> cbAssignmentCategory;

    @FXML
    private JFXDatePicker dpDueDate;

    @FXML
    private JFXDatePicker dpAssignedDate;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        sc = ScreenController.getInstance();

        selectedAssignment = md.getSelectedAssignment();

        //TODO: Add proper categories
        ObservableList<String> list = FXCollections.observableArrayList("Unassigned");

        cbAssignmentCategory.setItems(list);
    }


    public void saveClick(ActionEvent event) {
        if (!formValidate())
            return;

        selectedAssignment.setAssignmentName(tfAssignmentName.getText());
        selectedAssignment.setMaxScore(Integer.valueOf(tfMaxScore.getText()));
        if(!tfCustomWeight.getText().isEmpty())
            selectedAssignment.setWeight(Integer.valueOf(tfCustomWeight.getText()));
        selectedAssignment.setDueDate(dpDueDate.getValue());
        selectedAssignment.setAssignedDate(dpAssignedDate.getValue());
        //TODO: Required to save to database
        //selectedAssignment.setCategory();

        if (selectedAssignment.getDBID() == 0 && !md.getNewObjects().contains(selectedAssignment)) {
            md.addNewObject(selectedAssignment);
        } else {
            md.addUpdatedObject(selectedAssignment);
        }

        try {
            sc.addScreen("Course", FXMLLoader.load(getClass().getResource("../jfxml/CourseView.fxml")));
            sc.activate("Course");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelClick(ActionEvent event) {
        if (selectedAssignment.getDBID() == 0 && !md.getNewObjects().contains(selectedAssignment)) {
            md.removeAssignment(selectedAssignment);
        }

        try {
            sc.addScreen("Course", FXMLLoader.load(getClass().getResource("../jfxml/CourseView.fxml")));
            sc.activate("Course");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean formValidate() {
        if (!tfAssignmentName.validate())
            return false;
        if(!tfMaxScore.validate())
            return false;
        if(!tfCustomWeight.validate())
            return false;
        if(!dpDueDate.validate())
            return false;
        if(!dpAssignedDate.validate())
            return false;
        return true;
    }
}
