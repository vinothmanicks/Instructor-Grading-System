package com.cannapaceus.jfx;

import com.cannapaceus.grader.Category;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class CategoryFormController {

    ScreenController sc = null;
    Model md = null;

    Category selectedCategory;

    @FXML
    JFXTextField tfCatName;

    @FXML
    JFXTextField tfCatWeight;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        sc = ScreenController.getInstance();

        selectedCategory = md.getSelectedCategory();
    }


    public void saveClick(ActionEvent event) {
        if (!formValidate())
            return;

        selectedCategory.setCategoryName(tfCatName.getText());
        selectedCategory.setWeight(Float.valueOf(tfCatWeight.getText()));

        if (selectedCategory.getDBID() == 0 && !md.getNewObjects().contains(selectedCategory)) {
            md.addNewObject(selectedCategory);
        } else {
            md.addUpdatedObject(selectedCategory);
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
        if (selectedCategory.getDBID() == 0 && !md.getNewObjects().contains(selectedCategory)) {
            md.removeCategory(selectedCategory);
    }

        try {
            sc.addScreen("Course", FXMLLoader.load(getClass().getResource("../jfxml/CourseView.fxml")));
            sc.activate("Course");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean formValidate() {
        if (!tfCatName.validate())
            return false;

        if (!tfCatWeight.validate())
            return false;

        return true;
    }


}
