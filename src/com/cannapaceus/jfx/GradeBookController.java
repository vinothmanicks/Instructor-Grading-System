package com.cannapaceus.jfx;

import com.cannapaceus.grader.Assignment;
import com.cannapaceus.grader.Course;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
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
        TableColumn lastNameCol = new TableColumn("Last Name");
        TableColumn emailCol = new TableColumn("Email");

        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);

        lAssignments = selectedCourse.getlAssignments();

        iLAssignmentSize = lAssignments.size();

        for(int i = 0; i < iLAssignmentSize; i++)
        {
            TableColumn assignmentColumn = new TableColumn(lAssignments.get(i).getAssignmentName());
            table.getColumns().add(assignmentColumn);
        }

        VBox tempVB = new VBox();
        tempVB.setSpacing(10.0);
        tempVB.setStyle("-fx-padding: 10, 10, 10, 10; -fx-background-color: white;");

        DropShadow ds = new DropShadow();
        ds.setColor(Color.GREY);
        ds.setOffsetX(4);
        ds.setOffsetY(4);

        tempVB.setEffect(ds);

        tempVB.getChildren().addAll(table);

        vbTerms.getChildren().addAll(tempVB);
    }

    public void backClick(ActionEvent actionEvent) {
        sc.activate("Course");
    }
}