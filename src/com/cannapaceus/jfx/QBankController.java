package com.cannapaceus.jfx;

import com.cannapaceus.grader.Assignment;
import com.cannapaceus.grader.Course;
import com.cannapaceus.grader.Grade;
import com.cannapaceus.grader.Student;
import com.cannapaceus.qbank.QuestionBank;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class QBankController {

    ScreenController sc = null;
    Model md = null;

    QuestionBank questionBank;
    int iLAssignmentSize;

    private TableView table = new TableView();

    @FXML
    private VBox vbTerms;

    @FXML
    private void initialize() {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        questionBank.getInstance();

        createTable();
    }

    private void createTable() {
        table.setEditable(true);

        TableColumn courseNameCol = new TableColumn("Course Name");
        courseNameCol.setCellValueFactory(new PropertyValueFactory<QuestionBank, String>("courseName"));
        TableColumn questionCol = new TableColumn("Question");
        questionCol.setCellValueFactory(new PropertyValueFactory<QuestionBank, String>("question"));
        TableColumn questionTypeCol = new TableColumn("Question Type");
        questionTypeCol.setCellValueFactory(new PropertyValueFactory<QuestionBank, String>("questionType"));
        TableColumn questionLevelCol = new TableColumn("Question Level");
        questionLevelCol.setCellValueFactory(new PropertyValueFactory<QuestionBank, String>("questionLevel"));
        TableColumn questionAssignmentTypeCol = new TableColumn("Question Assignment Type");
        questionAssignmentTypeCol.setCellValueFactory(new PropertyValueFactory<QuestionBank, String>("questionAssignmentType"));
        TableColumn timeCol = new TableColumn("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<QuestionBank, String>("toDoTime"));

        table.getColumns().addAll(courseNameCol, questionCol, questionTypeCol, questionLevelCol, questionAssignmentTypeCol, timeCol);

        populateTable();

        VBox tempVB = new VBox();

        tempVB.getChildren().addAll(table);

        vbTerms.getChildren().addAll(tempVB);
    }

    private void populateTable() {
        /*ObservableList<Student> data = FXCollections.observableArrayList(selectedCourse.getlStudents());
        table.setItems(data);
        ObservableList<Grade> gradeData = FXCollections.observableArrayList(selectedCourse.getlGrades());
        //table.setItems(gradeData);*/
    }

    public void addQuestion(ActionEvent event) {
        try {
            sc.addScreen("AddQuestion", FXMLLoader.load(getClass().getResource("../jfxml/QBankAddQuestionView.fxml")));
            sc.activate("AddQuestion");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
