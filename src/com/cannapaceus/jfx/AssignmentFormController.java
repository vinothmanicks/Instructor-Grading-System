package com.cannapaceus.jfx;

import com.cannapaceus.grader.*;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class AssignmentFormController {

    ScreenController sc = null;
    Model md = null;

    Assignment selectedAssignment;

    HashMap<Integer, Category> hmCat;

    @FXML
    private JFXTextField tfAssignmentName;

    @FXML
    private JFXTextField tfMaxScore;

    @FXML
    private JFXTextField tfCustomWeight;

    @FXML
    private JFXComboBox<String> cbCategory;

    @FXML
    private JFXDatePicker dpDueDate;

    @FXML
    private JFXDatePicker dpAssignedDate;

    @FXML
    private JFXCheckBox xbUseCustom;

    @FXML
    private void initialize() {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        selectedAssignment = md.getSelectedAssignment();

        hmCat = new HashMap<>();
        int i = 0;

        ArrayList<String> catList = new ArrayList<>();
        for (Category tempCat : md.getSelectedCourse().getlCategories()) {
            catList.add(tempCat.getName());
            hmCat.put(i, tempCat);
            i++;
        }
        catList.add("Uncategorized");

        ObservableList<String> cat = FXCollections.observableArrayList(catList);
        cbCategory.setItems(cat);

        tfAssignmentName.setText(selectedAssignment.getAssignmentName());
        tfMaxScore.setText(String.valueOf(selectedAssignment.getMaxScore()));
        if (selectedAssignment.getWeight() == null)
        {
            xbUseCustom.selectedProperty().setValue(false);
        }
        else
        {
            tfCustomWeight.setText(String.valueOf(selectedAssignment.getWeight()));
            xbUseCustom.selectedProperty().setValue(true);
        }
        if(md.getSelectedCategory() != null) {
            cbCategory.getSelectionModel().select(md.getSelectedCategory().getName());
        }
        else {
            cbCategory.getSelectionModel().select(i);
        }

        dpDueDate.setValue(selectedAssignment.getDueDate());
        dpAssignedDate.setValue(selectedAssignment.getAssignedDate());
    }


    public void saveClick(ActionEvent event) {
        if (!formValidate())
            return;

        Category cat = md.getSelectedCategory();

        selectedAssignment.setAssignmentName(tfAssignmentName.getText());
        selectedAssignment.setMaxScore(Float.valueOf(tfMaxScore.getText()));
        if (xbUseCustom.isSelected())
        {
            selectedAssignment.setWeight(Float.valueOf(tfCustomWeight.getText()));
        }
        else
        {
            selectedAssignment.setWeight(null);
        }
        selectedAssignment.setDueDate(dpDueDate.getValue());
        selectedAssignment.setAssignedDate(dpAssignedDate.getValue());

        Category assignCat = hmCat.get(cbCategory.getSelectionModel().getSelectedIndex());
        if (assignCat != null)
            assignCat.addAssignment(selectedAssignment);

        selectedAssignment.setCategory(assignCat);

        md.setSelectedCategory(null);
        md.setSelectedAssignment(null);

        if (selectedAssignment.getDBID() == 0 && !md.getNewObjects().contains(selectedAssignment)) {
            md.addNewObject(selectedAssignment);
            for (Grade g : selectedAssignment.getGrades()) {
                if (!md.getNewObjects().contains(g))
                    md.addNewObject(g);
            }
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
        boolean anyFail = true;

        if (!tfAssignmentName.validate())
            anyFail = false;
        if(!tfMaxScore.validate())
            anyFail = false;
        if(xbUseCustom.isSelected()) {
            if (!tfCustomWeight.validate())
                anyFail = false;
        }
        if(!dpDueDate.validate())
            anyFail = false;
        if(!dpAssignedDate.validate())
            anyFail = false;
        return anyFail;
    }
}
