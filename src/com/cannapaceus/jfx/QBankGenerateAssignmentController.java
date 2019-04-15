package com.cannapaceus.jfx;

import com.cannapaceus.grader.Assignment;
import com.cannapaceus.grader.Category;
import com.cannapaceus.grader.Course;
import com.cannapaceus.grader.Grade;
import com.jfoenix.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class QBankGenerateAssignmentController {
    ScreenController sc = null;
    Model md = null;

    Course selectedCourse;
    Assignment selectedAssignment;

    private HashMap<Integer, Category> hmCat;
    private HashMap<String, Assignment> hmAssignments = new HashMap<>();

    @FXML
    private JFXRadioButton rbExistingAssignment;

    @FXML
    private JFXRadioButton rbNewAssignment;

    @FXML
    private VBox vbGenerateNewAssignment;

    @FXML
    private VBox vbUseExistingAssignment;

    @FXML
    private JFXButton btnGenerate;

    @FXML
    private JFXComboBox cbAssignment;

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
    }

    public void radioSelect(ActionEvent event) {
        if (rbNewAssignment.isSelected()) {
            vbUseExistingAssignment.setVisible(false);
            vbUseExistingAssignment.setManaged(false);

            btnGenerate.setVisible(true);
            btnGenerate.setManaged(true);
            vbGenerateNewAssignment.setVisible(true);
            vbGenerateNewAssignment.setManaged(true);

            initializeNewAssignment();
        }

        if (rbExistingAssignment.isSelected()) {
            vbGenerateNewAssignment.setVisible(false);
            vbGenerateNewAssignment.setManaged(false);

            btnGenerate.setVisible(true);
            btnGenerate.setManaged(true);
            vbUseExistingAssignment.setVisible(true);
            vbUseExistingAssignment.setManaged(true);

            initializeExistingAssignment();
        }
    }

    public void initializeNewAssignment() {
        LocalDate lo = LocalDate.now();

        Assignment a = new Assignment("",lo,lo,false,100, null, null);

        md.setSelectedAssignment(a);
        md.addAssignment(a);

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

    public void initializeExistingAssignment() {
        selectedCourse = md.getSelectedCourse();
        selectedAssignment = md.getSelectedAssignment();
        String selectedKey = "";

        ObservableList<String> assignmentItems = FXCollections.observableArrayList();

        for(Assignment assignment : selectedCourse.getlAssignments()) {
            if (assignment.getDBID() == 0)
                continue;
            String key = assignment.getAssignmentName();
            if (selectedAssignment == assignment)
                selectedKey = key;
            assignmentItems.add(key);
            hmAssignments.put(key, assignment);
        }

        cbAssignment.setItems(assignmentItems);

        cbAssignment.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> selected, String oldValue, String newValue) {
                selectedAssignment = hmAssignments.get(newValue);
                md.setSelectedAssignment(selectedAssignment);
            }
        });

        if (selectedAssignment != null) {
            cbAssignment.getSelectionModel().select(selectedKey);
        }
    }

    public void generateClick(ActionEvent event) {
        if (!formValidate())
            return;

        if(rbNewAssignment.isSelected())
            generateNewAssignmentClick();
        else if(rbExistingAssignment.isSelected())
            generateExistingAssignmentClick();
        else
            return;

    }

    private void generateNewAssignmentClick() {
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
        selectedAssignment.setCategory(hmCat.get(cbCategory.getSelectionModel().getSelectedIndex()));

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

    private void generateExistingAssignmentClick() {

    }

    public void cancelClick(ActionEvent event) {
        if(rbNewAssignment.isSelected())
            cancelGenerateNewAssignmentClick();
        else if(rbExistingAssignment.isSelected())
            cancelGenerateExistingAssignmentClick();

        try {
            sc.addScreen("QBank", FXMLLoader.load(getClass().getResource("../jfxml/QBankView.fxml")));
            sc.activate("QBank");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelGenerateNewAssignmentClick() {
        if (selectedAssignment.getDBID() == 0 && !md.getNewObjects().contains(selectedAssignment)) {
            md.removeAssignment(selectedAssignment);
        }
    }

    private void cancelGenerateExistingAssignmentClick() {

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
