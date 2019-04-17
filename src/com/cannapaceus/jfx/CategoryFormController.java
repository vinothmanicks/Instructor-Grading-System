package com.cannapaceus.jfx;

import com.cannapaceus.grader.Category;
import com.cannapaceus.grader.Course;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class CategoryFormController {

    ScreenController sc = null;
    Model md = null;

    Category selectedCategory;
    Course selectedCourse;

    @FXML
    JFXTextField tfCatName;

    @FXML
    JFXTextField tfCatWeight;

    @FXML
    JFXTextField tfDropped;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        sc = ScreenController.getInstance();

        selectedCategory = md.getSelectedCategory();
        selectedCourse = md.getSelectedCourse();

        if (selectedCategory != null) {
            tfCatName.setText(selectedCategory.getName());
            tfCatWeight.setText("" + selectedCategory.getWeight());
            tfDropped.setText("" + selectedCategory.getDropped());
        } else {
            tfCatName.setDisable(true);
            tfCatWeight.setDisable(true);
            tfDropped.setText("" + selectedCourse.getDropUncategorized());
        }


    }


    public void saveClick(ActionEvent event) {
        if (selectedCategory != null) {
            if (!formValidate())
                return;
            selectedCategory.setCategoryName(tfCatName.getText());
            selectedCategory.setWeight(Float.valueOf(tfCatWeight.getText()));
            selectedCategory.setDropped(Integer.valueOf(tfDropped.getText()));

            for (Object o : selectedCourse.dropGrades()) {
                md.addUpdatedObject(o);
            }

            if (selectedCategory.getDBID() == 0 && !md.getNewObjects().contains(selectedCategory)) {
                md.addNewObject(selectedCategory);
            } else {
                md.addUpdatedObject(selectedCategory);
            }
        } else {
            if (!tfDropped.validate())
                return;

            selectedCourse.setDropUncategorized(Integer.valueOf(tfDropped.getText()));

            for (Object o : selectedCourse.dropGrades()) {
                md.addUpdatedObject(o);
            }

            if (selectedCourse.getDBID() == 0 && !md.getNewObjects().contains(selectedCourse)) {
                md.addNewObject(selectedCourse);
            } else {
                md.addUpdatedObject(selectedCourse);
            }
        }

        md.setSelectedCategory(null);

        try {
            sc.addScreen("Course", FXMLLoader.load(getClass().getResource("/CourseView.fxml")));
            sc.activate("Course");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelClick(ActionEvent event) {
        if (selectedCategory != null) {
            if (selectedCategory.getDBID() == 0 && !md.getNewObjects().contains(selectedCategory)) {
                md.removeCategory(selectedCategory);
        }
    }

        try {
            sc.addScreen("Course", FXMLLoader.load(getClass().getResource("/CourseView.fxml")));
            sc.activate("Course");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean formValidate() {
        boolean anyFail = true;
        if (!tfCatName.validate())
            anyFail = false;

        if (!tfCatWeight.validate())
            anyFail = false;

        return anyFail;
    }


}
