package com.cannapaceus.jfx;

import com.cannapaceus.grader.*;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.DoubleTextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.util.Callback;

import javax.swing.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;

public class StudentController {
    ScreenController sc = null;
    Model md = null;

    Student selectedStudent;

    ObservableList<Grade> gradeObservableList;

    @FXML
    private JFXTreeTableView studentGradeTable;

    @FXML
    private Label lblStudentName;

    @FXML
    private Label lblStudentAvg;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        selectedStudent = md.getSelectedStudent();

        lblStudentName.setText(selectedStudent.getFirstMIName() + " " + selectedStudent.getLastName());
        lblStudentAvg.setText("Average: " + (Math.round(selectedStudent.getAverageGrade() * 100.0) / 100.0));

        TreeTableColumn<Grade, String> assignmentNameColumn = new JFXTreeTableColumn<>("Assignment");
        assignmentNameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getAssignmentReference().getAssignmentName()));
        assignmentNameColumn.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder());
            c.setEditable(false);
            return c;
        });

        assignmentNameColumn.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return -o1.toUpperCase().compareTo(o2.toUpperCase());
            }
        });
        assignmentNameColumn.setContextMenu(null);

        TreeTableColumn<Grade, Float> gradeFloatColumn = new JFXTreeTableColumn<>("Grade");
        gradeFloatColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, Float> param) ->
                param.getValue().getValue().getGradeProperty().asObject());
        gradeFloatColumn.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>(new DoubleTextFieldEditorBuilder());
            return c;
        });
        gradeFloatColumn.setContextMenu(null);

        TreeTableColumn<Grade, String> maxScoreColumn = new JFXTreeTableColumn<>("Out of");
        maxScoreColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, String> param) ->
                new ReadOnlyStringWrapper("" + param.getValue().getValue().getAssignmentReference().getMaxScore()));
        maxScoreColumn.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder());
            c.setEditable(false);
            return c;
        });

        maxScoreColumn.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Float.compare(Float.valueOf(o1), Float.valueOf(o2));
            }
        });
        maxScoreColumn.setContextMenu(null);

        TreeTableColumn<Grade, Boolean> overdueColumn = new JFXTreeTableColumn<>("Overdue");
        overdueColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, Boolean> param) ->
                param.getValue().getValue().getOverdueProperty());
        overdueColumn.setCellFactory((tc) -> {
            CheckBoxTreeTableCell c = new CheckBoxTreeTableCell<>();
            c.setAlignment(Pos.CENTER);
            return c;
        });
        overdueColumn.setContextMenu(null);

        TreeTableColumn<Grade, Boolean> missingColumn = new JFXTreeTableColumn<>("Missing");
        missingColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, Boolean> param) ->
                param.getValue().getValue().getMissingProperty());
        missingColumn.setCellFactory((tc) -> {
            CheckBoxTreeTableCell c = new CheckBoxTreeTableCell<>();
            c.setAlignment(Pos.CENTER);
            return c;
        });
        missingColumn.setContextMenu(null);

        TreeTableColumn<Grade, Boolean> droppedColumn = new JFXTreeTableColumn<>("Dropped");
        droppedColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, Boolean> param) ->
                param.getValue().getValue().getDroppedProperty());
        droppedColumn.setCellFactory((tc) -> {
            CheckBoxTreeTableCell c = new CheckBoxTreeTableCell<>();
            c.setAlignment(Pos.CENTER);
            return c;
        });
        droppedColumn.setContextMenu(null);

        TreeTableColumn<Grade, Boolean> submittedColumn = new JFXTreeTableColumn<>("Submitted");
        submittedColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, Boolean> param) ->
                param.getValue().getValue().getSubmittedProperty());
        submittedColumn.setCellFactory((tc) -> {
            CheckBoxTreeTableCell c = new CheckBoxTreeTableCell<>();
            c.setAlignment(Pos.CENTER);
            return c;
        });
        submittedColumn.setContextMenu(null);

        gradeObservableList = FXCollections.observableArrayList(new Callback<Grade, Observable[]>() {
            @Override
            public Observable[] call(Grade param) {
                return new Observable[] {param.getGradeProperty(),
                        param.getOverdueProperty(),
                        param.getMissingProperty(),
                        param.getDroppedProperty(),
                        param.getSubmittedProperty()};
            }
        });
        gradeObservableList.addAll(selectedStudent.getGrades());
        gradeObservableList.addListener(new ListChangeListener<Grade>() {
            @Override
            public void onChanged(Change<? extends Grade> c) {
                while (c.next()) {
                    if (c.wasUpdated()) {
                        Grade g = selectedStudent.getGrades().get(c.getFrom());
                        md.addUpdatedObject(g);

                        recalculateStats(g);
                    }
                }
            }
        });

        final TreeItem<Grade> root = new RecursiveTreeItem<>(gradeObservableList, RecursiveTreeObject::getChildren);

        studentGradeTable.setRoot(root);
        studentGradeTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        studentGradeTable.setTableMenuButtonVisible(false);
        studentGradeTable.setShowRoot(false);
        studentGradeTable.setEditable(true);
        studentGradeTable.getColumns().setAll(assignmentNameColumn, gradeFloatColumn, maxScoreColumn, overdueColumn, missingColumn, droppedColumn, submittedColumn);
    }

    public void backClick(ActionEvent actionEvent) {
        md.setSelectedStudent(null);

        try {
            sc.addScreen("Course", FXMLLoader.load(getClass().getResource("../jfxml/CourseView.fxml")));
            sc.activate("Course");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void commitClick(ActionEvent e) {
        md.commitChanges();

        initialize();
    }

    public void revertClick(ActionEvent e) {
        md.revertChanges();

        initialize();
    }

    private void recalculateStats(Grade g) {

        Course c = md.getSelectedCourse();

        c.PopulateAverages(c.getlStudents());
        c.calculateStats();

        selectedStudent.setAverageGrade(selectedStudent.getGrades());

        Assignment a = g.getAssignmentReference();

        Statistics st = a.getStAssignmentStats();

        st.calculateMean(a.getGrades());
        st.calculateMedian(a.getGrades());
        st.calculateMode(a.getGrades());
        st.calculateStandardDev(a.getGrades());

        md.addUpdatedObject(a);
        md.addUpdatedObject(selectedStudent);
        md.addUpdatedObject(c);

        lblStudentAvg.setText("Average: " + (Math.round(selectedStudent.getAverageGrade() * 100.0) / 100.0));
    }
}
