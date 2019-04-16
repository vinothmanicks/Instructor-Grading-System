package com.cannapaceus.jfx;

import com.cannapaceus.grader.*;
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
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
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

    ObservableList<Grade> lGradeData;

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
        selectedCourse = md.getSelectedCourse();

        TreeTableColumn<Student, String> nameColumn = new JFXTreeTableColumn<>("Student");
        nameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getLastName() +
                        ", " + param.getValue().getValue().getFirstMIName()));
        nameColumn.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder());
            c.setEditable(false);
            return c;
        });

        nameColumn.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.toUpperCase().compareTo(o2.toUpperCase());
            }
        });
        nameColumn.setContextMenu(null);

        TreeTableColumn<Student, String> studentIDColumn = new JFXTreeTableColumn<>("Student ID");
        studentIDColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getStudentID()));
        studentIDColumn.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder());
            c.setEditable(false);
            return c;
        });
        studentIDColumn.setContextMenu(null);

        TreeTableColumn<Student, Float> averageColumn = new JFXTreeTableColumn<>("Average");
        averageColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, Float> param) ->
                param.getValue().getValue().getfAverageGradeProperty().asObject());
        averageColumn.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder());
            c.setFocusTraversable(false);
            c.setEditable(false);
            return c;
        });
        averageColumn.setContextMenu(null);

        ArrayList<Assignment> lAssignments = selectedCourse.getlAssignments();
        int iSize = lAssignments.size();

        for(int i = 0; i<iSize; i++) {
            TreeTableColumn<Student, Float> assignmentColumn = new JFXTreeTableColumn<>(lAssignments.get(i).getAssignmentName() +
                    "\n/" + lAssignments.get(i).getMaxScore());
            final int index = i;
            assignmentColumn.setCellValueFactory(param ->
                    param.getValue().getValue().getGrades().get(index).getGradeProperty().asObject()
            );
            assignmentColumn.setCellFactory((tc) -> {
                GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>(new DoubleTextFieldEditorBuilder());
                c.setAlignment(Pos.CENTER);
                return c;
            });
            assignmentColumn.setContextMenu(null);

            lAssignmentColumns.add(assignmentColumn);
        }

        lGradeBookData = FXCollections.observableArrayList(new Callback<Student, Observable[]>() {
            @Override
            public Observable[] call(Student param) {
                return new Observable[] {param.getStudentEmailProperty()};
            }
        });

        lGradeBookData.addAll(selectedCourse.getlStudents());

        lGradeBookData.addListener(new ListChangeListener<Student>() {
            @Override
            public void onChanged(Change<? extends Student> c) {
                while (c.next()) {
                    if (c.wasUpdated()) {
                        Student s = selectedCourse.getlStudents().get(c.getFrom());

                        md.addUpdatedObject(s);
                    }
                }
            }
        });

        lGradeData = FXCollections.observableArrayList(new Callback<Grade, Observable[]>() {
            @Override
            public Observable[] call(Grade param) {
                return new Observable[] {param.getGradeProperty()};
            }
        });

        lGradeData.addAll(selectedCourse.getlGrades());

        lGradeData.addListener(new ListChangeListener<Grade>() {
            @Override
            public void onChanged(Change<? extends Grade> c) {
                while (c.next()) {
                    if (c.wasUpdated()) {
                        Grade g = selectedCourse.getlGrades().get(c.getFrom());

                        lGradeData.removeListener(this);

                        recalculateStats(g);

                        lGradeData.addListener(this);
                    }
                }
            }
        });

        final TreeItem<Student> root = new RecursiveTreeItem<>(lGradeBookData, RecursiveTreeObject::getChildren);

        gradeBookTable.setColumnResizePolicy(TreeTableView.UNCONSTRAINED_RESIZE_POLICY);
        gradeBookTable.setTableMenuButtonVisible(false);
        gradeBookTable.setShowRoot(false);
        gradeBookTable.setEditable(true);
        gradeBookTable.getColumns().setAll(nameColumn, studentIDColumn, averageColumn);

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

    private void recalculateStats(Grade g) {
        Course c = md.getSelectedCourse();

        for (Object o : c.dropGrades()) {
            md.addUpdatedObject(o);
        }

        Student s = g.getStudentReference();
        s.setAverageGrade(s.getGrades(),md.selectedCourse.getScale());

        c.PopulateAverages(c.getlStudents());
        c.calculateStats();

        Assignment a = g.getAssignmentReference();
        Statistics st = a.getStAssignmentStats();
        st.calculateMean(a.getGrades());
        st.calculateMedian(a.getGrades());
        st.calculateMode(a.getGrades());
        st.calculateStandardDev(a.getGrades());

        md.addUpdatedObject(a);
        md.addUpdatedObject(s);
        md.addUpdatedObject(c);
        md.addUpdatedObject(g);
    }

    public void commitClick(ActionEvent event) {
        md.commitChanges();
        lAssignmentColumns.clear();
        createTable();
    }

    public void revertClick(ActionEvent event) {
        md.revertChanges();
        lAssignmentColumns.clear();
        createTable();
    }
}