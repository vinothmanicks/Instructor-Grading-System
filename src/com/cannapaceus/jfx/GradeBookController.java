package com.cannapaceus.jfx;

import com.cannapaceus.grader.Assignment;
import com.cannapaceus.grader.Course;
import com.cannapaceus.grader.Grade;
import com.cannapaceus.grader.Student;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Set;

public class GradeBookController{
    ScreenController sc = null;
    Model md = null;

    Course selectedCourse;
    ArrayList<Assignment> lAssignments;
    int iLAssignmentSize;

    private TableView table = new TableView();

    @FXML
    private VBox vbTerms;

    @FXML
    private Label lblGradeBookName;

    @FXML
    private void initialize() {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        selectedCourse = md.getSelectedCourse();

        lblGradeBookName.setText(selectedCourse.getCourseName() + " Grade Book");

        createTable();
    }

    private void createTable() {
        table.setEditable(true);

        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("firstMIName"));
        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));
        TableColumn idCol = new TableColumn("Student ID");
        idCol.setCellValueFactory(new PropertyValueFactory<Student, String>("studentID"));
        TableColumn emailCol = new TableColumn("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<Student, String>("studentEmail"));

        table.getColumns().addAll(firstNameCol, lastNameCol, idCol, emailCol);

        lAssignments = selectedCourse.getlAssignments();

        iLAssignmentSize = lAssignments.size();

        for(int i = 0; i < iLAssignmentSize; i++)
        {
            TableColumn assignmentColumn = new TableColumn(lAssignments.get(i).getAssignmentName());
            //enableColumnEdit(assignmentColumn, cellFactory);
            table.getColumns().add(assignmentColumn);
        }

        populateTable();

        VBox tempVB = new VBox();

        tempVB.getChildren().addAll(table);

        vbTerms.getChildren().addAll(tempVB);
    }

    private void populateTable() {
        ObservableList<Student> data = FXCollections.observableArrayList(selectedCourse.getlStudents());
        table.setItems(data);
        ObservableList<Grade> gradeData = FXCollections.observableArrayList(selectedCourse.getlGrades());
        //table.setItems(gradeData);
    }

    public void backClick(ActionEvent actionEvent) {
        sc.activate("Course");
    }
}