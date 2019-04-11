package com.cannapaceus.jfx;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import com.cannapaceus.grader.*;

import java.util.ArrayList;
import java.util.HashMap;

public class GradingCopyController {

    ScreenController sc = null;
    Model md = null;

    Course selectedCourse;

    HashMap<Integer,Course> allCourses;

    @FXML
    JFXCheckBox xbScale;
    @FXML
    JFXCheckBox xbCategories;
    @FXML
    JFXCheckBox xbAssignments;
    @FXML
    JFXComboBox cbCourses;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        md = md.getInstance();
        selectedCourse = md.selectedCourse;

        allCourses = new HashMap();

        ArrayList<String> courseNames = new ArrayList<>();

        int b = 0;
        for(Term tTerm: md.getTerms())
        {
            for (Course cCourse : tTerm.getCourses())
            {
                if(cCourse.getDBID() != md.selectedCourse.getDBID()) {
                    allCourses.put(b, cCourse);
                    courseNames.add(tTerm.getSeason().toString() + " " + tTerm.getYear() + " " + cCourse.getCourseName());
                    b++;
                }
            }
        }

        cbCourses.setItems(FXCollections.observableArrayList(courseNames));
        xbScale.selectedProperty().setValue(false);
        xbAssignments.selectedProperty().setValue(false);
        xbCategories.selectedProperty().setValue(false);
    }


    @FXML
    public void saveClick(ActionEvent event) {
        if(cbCourses.getItems().isEmpty())
        {
            return;
        }

        Course courseToCopy = allCourses.get(cbCourses.getSelectionModel().getSelectedIndex());

        if(xbScale.isSelected())
        {
            md.selectedCourse.setScale(courseToCopy.getScale());
        }
        if(xbCategories.isSelected())
        {
            if(xbAssignments.isSelected()) {
                for (Category refCat : courseToCopy.getlCategories()) {
                    Category cat = new Category(refCat);
                    md.setSelectedCategory(cat);
                    md.selectedCourse.addCategory(cat);
                    if (md.selectedCategory.getDBID() == 0 && !md.getNewObjects().contains(md.selectedCategory)) {
                        md.addNewObject(md.selectedCategory);
                    } else {
                        md.addUpdatedObject(md.selectedCategory);
                    }
                }

                for(Category cat: md.selectedCourse.getlCategories())
                {
                    for(Assignment refAssignment: cat.getAssignments())
                    {
                        Assignment aAssignment = new Assignment(refAssignment);
                        cat.removeAssigment(refAssignment);
                        cat.addAssignment(aAssignment);
                        md.setSelectedAssignment(aAssignment);
                        md.addAssignment(md.selectedAssignment);
                        for(Grade gGrade: md.selectedAssignment.getGrades()) {
                            md.removeGrade(gGrade);
                        }
                        if (md.selectedAssignment.getDBID() == 0 && !md.getNewObjects().contains(md.selectedAssignment)) {
                            md.addNewObject(md.selectedAssignment);
                        }
                    }
                }
            }
            else
            {
                for (Category refCat : courseToCopy.getlCategories()) {
                    Category cat = new Category(refCat);
                    for(Assignment aAssignment:cat.getAssignments()) {
                        cat.removeAssigment(aAssignment);
                    }
                    md.selectedCourse.addCategory(cat);
                    md.setSelectedCategory(cat);
                    if (md.selectedCategory.getDBID() == 0 && !md.getNewObjects().contains(md.selectedCategory)) {
                        md.addNewObject(md.selectedCategory);
                    } else {
                        md.addUpdatedObject(md.selectedCategory);
                    }
                }
            }
        }
        else if(xbAssignments.isSelected())
        {
            for(Assignment refAssignment: courseToCopy.getlAssignments())
            {
                Assignment aAssignment = new Assignment(refAssignment);
                md.setSelectedAssignment(aAssignment);
                md.setSelectedCategory(null);
                md.selectedAssignment.setCategory(md.selectedCategory);
                md.addAssignment(md.selectedAssignment);
                md.addAssignment(md.selectedAssignment);
                for(Grade gGrade: md.selectedAssignment.getGrades()) {
                    md.removeGrade(gGrade);
                }
                if (md.selectedAssignment.getDBID() == 0 && !md.getNewObjects().contains(md.selectedAssignment)) {
                    md.addNewObject(md.selectedAssignment);
                }
            }
        }

        try {
            sc.addScreen("Course", FXMLLoader.load(getClass().getResource("../jfxml/CourseView.fxml")));
            sc.activate("Course");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cancelClick(ActionEvent event) {

        try {
            sc.addScreen("Course", FXMLLoader.load(getClass().getResource("../jfxml/CourseView.fxml")));
            sc.activate("Course");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
