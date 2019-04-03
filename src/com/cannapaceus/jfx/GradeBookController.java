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

    ArrayList<StudentGradeBookData> lGradeBookData;

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

        lGradeBookData = new ArrayList<>();

        createTable();
    }

    private void createTable() {
        table.setEditable(true);

        reCastData();

        buildTable();

        populateTable();

        VBox tempVB = new VBox();

        tempVB.getChildren().addAll(table);

        vbTerms.getChildren().addAll(tempVB);
    }

    private void buildTable() {
        TableColumn firstNameCol = new TableColumn("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<StudentGradeBookData, String>("firstMIName"));
        TableColumn lastNameCol = new TableColumn("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<StudentGradeBookData, String>("lastName"));
        TableColumn idCol = new TableColumn("Student ID");
        idCol.setCellValueFactory(new PropertyValueFactory<StudentGradeBookData, String>("studentID"));
        TableColumn emailCol = new TableColumn("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<StudentGradeBookData, String>("studentEmail"));

        table.getColumns().addAll(firstNameCol, lastNameCol, idCol, emailCol);

        lAssignments = selectedCourse.getlAssignments();

        iLAssignmentSize = lAssignments.size();

        for(int i = 0; i < iLAssignmentSize; i++)
        {
            TableColumn assignmentColumn = new TableColumn(lAssignments.get(i).getAssignmentName());
            assignmentColumn.setCellValueFactory(new PropertyValueFactory<StudentGradeBookData, String>(null));
            //enableColumnEdit(assignmentColumn, cellFactory);
            table.getColumns().add(assignmentColumn);
        }
    }

    private void populateTable() {
        ObservableList<StudentGradeBookData> data = FXCollections.observableArrayList(lGradeBookData);
        ObservableList<Grade> gradeData = FXCollections.observableArrayList(selectedCourse.getlGrades());
        table.setItems(data);
        //table.setItems(gradeData);

        //table.setItems(gradeData);
    }

    public void backClick(ActionEvent actionEvent) {
        sc.activate("Course");
    }

    public class StudentGradeBookData{
        ArrayList<String> sStudentData;

        StudentGradeBookData(Student student) {
            sStudentData = new ArrayList<>();
            sStudentData.add(student.getFirstMIName());
            sStudentData.add(student.getLastName());
            sStudentData.add(student.getStudentID());
            sStudentData.add(student.getStudentEmail());

            ArrayList<Grade> lGrades = student.getGrades();
            int iSize = lGrades.size();

            for(int i = 0; i< iSize; i++) {
                sStudentData.add(String.valueOf(lGrades.get(i).getGrade()));
            }
        }
    }

    private void reCastData(){
        ArrayList<Student> lStudents = selectedCourse.getlStudents();
        int iSize = lStudents.size();

        for(int i = 0; i< iSize; i++) {
            lGradeBookData.add(new StudentGradeBookData(lStudents.get(i)));
        }
    }
}