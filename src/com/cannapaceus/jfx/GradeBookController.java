package com.cannapaceus.jfx;

import com.cannapaceus.grader.Assignment;
import com.cannapaceus.grader.Course;
import com.cannapaceus.grader.Grade;
import com.cannapaceus.grader.Student;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.DoubleTextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.util.Comparator;
import java.util.Set;

public class GradeBookController{
    ScreenController sc = null;
    Model md = null;

    Course selectedCourse;
    ArrayList<TreeTableColumn<Student, Float>> lAssignmentColumns;

    ObservableList<Student> lGradeBookData;

    @FXML
    private JFXTreeTableView gradeBookTable;

    @FXML
    private Label lblGradeBookName;

    @FXML
    private void initialize() {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        selectedCourse = md.getSelectedCourse();

        lblGradeBookName.setText(selectedCourse.getCourseName() + " Grade Book");

        lGradeBookData = FXCollections.observableArrayList();
        lAssignmentColumns = new ArrayList<TreeTableColumn<Student, Float>>();

        createTable();
    }

    private void createTable() {

        TreeTableColumn<Student, String> firstNameColumn = new JFXTreeTableColumn<>("First Name");
        firstNameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getFirstMIName()));
        firstNameColumn.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder());
            c.setEditable(false);
            return c;
        });

        firstNameColumn.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return -o1.toUpperCase().compareTo(o2.toUpperCase());
            }
        });
        firstNameColumn.setContextMenu(null);

        TreeTableColumn<Student, String> lastNameColumn = new JFXTreeTableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getLastName()));
        lastNameColumn.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder());
            c.setEditable(false);
            return c;
        });
        lastNameColumn.setContextMenu(null);

        TreeTableColumn<Student, String> studentIDColumn = new JFXTreeTableColumn<>("Student ID");
        studentIDColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getStudentID()));
        studentIDColumn.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder());
            c.setEditable(false);
            return c;
        });
        studentIDColumn.setContextMenu(null);

        ArrayList<Assignment> lAssignments = selectedCourse.getlAssignments();
        int iSize = lAssignments.size();

        for(int i = 0; i<iSize; i++) {
            TreeTableColumn<Student, Float> assignmentColumn = new JFXTreeTableColumn<>(lAssignments.get(i).getAssignmentName());
            final Assignment tempRef = lAssignments.get(i);
            assignmentColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, Float> param) -> {
                    ArrayList<Grade> lGrades= param.getValue().getValue().getGrades();
                    for(Grade gGrades: lGrades) {
                        if (gGrades.getAssignmentReference() == tempRef) {
                            return gGrades.getGradeProperty().asObject();
                        }
                    }
                    return null;
                }
            );
            assignmentColumn.setCellFactory((tc) -> {
                GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>(new DoubleTextFieldEditorBuilder());
                return c;
            });
            assignmentColumn.setContextMenu(null);

            lAssignmentColumns.add(assignmentColumn);
        }

        lGradeBookData = FXCollections.observableArrayList(new Callback<Student, Observable[]>() {
            @Override
            public Observable[] call(Student param) {
                ArrayList<Observable> o = new ArrayList<>();

                o.add(param.getStudentEmailProperty());

                for (Grade g : param.getGrades()) {
                    o.add(g.getGradeProperty().asObject());
                }

                Observable[] arrO = (Observable[]) o.toArray();

                return arrO;
            }
        });

        lGradeBookData.addListener(new ListChangeListener<Student>() {
            @Override
            public void onChanged(Change<? extends Student> c) {
                while (c.next()) {
                    if (c.wasUpdated()) {
                        //Grade g = selectedCourse.getlStudents().get(c.getFrom());
                        //md.addUpdatedObject(g);
                    }
                }
            }
        });

        final TreeItem<Student> root = new RecursiveTreeItem<>(lGradeBookData, RecursiveTreeObject::getChildren);

        gradeBookTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        gradeBookTable.setTableMenuButtonVisible(false);
        gradeBookTable.setShowRoot(false);
        gradeBookTable.setEditable(true);
        gradeBookTable.getColumns().setAll(firstNameColumn, lastNameColumn, studentIDColumn);

        gradeBookTable.getColumns().addAll(lAssignmentColumns);

        gradeBookTable.setRoot(root);
    }

    public void backClick(ActionEvent actionEvent) {
        try {
            sc.addScreen("Course", FXMLLoader.load(getClass().getResource("../jfxml/CourseView.fxml")));
            sc.activate("Course");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}