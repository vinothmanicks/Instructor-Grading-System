package com.cannapaceus.jfx;

import com.cannapaceus.grader.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.DoubleTextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

import java.util.Comparator;

public class AssignmentController {
    ScreenController sc = null;
    Model md = null;

    Assignment selectedAssignment;

    ObservableList<Grade> gradeObservableList;

    @FXML
    private Label lblAssignmentName;

    @FXML
    private Label lblMean;

    @FXML
    private Label lblMedian;

    @FXML
    private Label lblMode;

    @FXML
    private Label lblStandardDev;

    @FXML
    private JFXTreeTableView assignmentGradeTable;

    @FXML
    private JFXButton btnSubmitAll;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        selectedAssignment = md.getSelectedAssignment();
        Statistics st = selectedAssignment.getStAssignmentStats();

        lblAssignmentName.setText(selectedAssignment.getAssignmentName());

        lblMean.setText("Average: " + (Math.round(st.getMean() * 100.0) / 100.0));
        lblMedian.setText("Median: " + (Math.round(st.getMedian() * 100.0) / 100.0));
        lblMode.setText("Mode: " + (Math.round(st.getMode() * 100.0) / 100.0));
        lblStandardDev.setText("Standard Deviation: " + (Math.round(st.getStandardDev() * 100.0) / 100.0));

        TreeTableColumn<Grade, String> studentNameColumn = new JFXTreeTableColumn<>("Student");
        studentNameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getStudentReference().getLastName() +
                        ", " + param.getValue().getValue().getStudentReference().getFirstMIName()));
        studentNameColumn.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder());
            c.setFocusTraversable(false);
            c.setEditable(false);
            return c;
        });

        studentNameColumn.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.toUpperCase().compareTo(o2.toUpperCase());
            }
        });
        studentNameColumn.setContextMenu(null);

        TreeTableColumn<Grade, Float> gradeFloatColumn = new JFXTreeTableColumn<>("Grade");
        gradeFloatColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, Float> param) ->
                param.getValue().getValue().getGradeProperty().asObject());
        gradeFloatColumn.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>(new DoubleTextFieldEditorBuilder());
            c.setFocusTraversable(true);
            return c;
        });
        gradeFloatColumn.setContextMenu(null);

        TreeTableColumn<Grade, String> maxScoreColumn = new JFXTreeTableColumn<>("Out of");
        maxScoreColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, String> param) ->
                new ReadOnlyStringWrapper("" + param.getValue().getValue().getAssignmentReference().getMaxScore()));
        maxScoreColumn.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder());
            c.setFocusTraversable(false);
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
            c.setFocusTraversable(false);
            c.setAlignment(Pos.CENTER);
            return c;
        });
        overdueColumn.setContextMenu(null);

        TreeTableColumn<Grade, Boolean> missingColumn = new JFXTreeTableColumn<>("Missing");
        missingColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, Boolean> param) ->
                param.getValue().getValue().getMissingProperty());
        missingColumn.setCellFactory((tc) -> {
            CheckBoxTreeTableCell c = new CheckBoxTreeTableCell<>();
            c.setFocusTraversable(false);
            c.setAlignment(Pos.CENTER);
            return c;
        });
        missingColumn.setContextMenu(null);

        TreeTableColumn<Grade, Boolean> droppedColumn = new JFXTreeTableColumn<>("Dropped");
        droppedColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, Boolean> param) ->
                param.getValue().getValue().getDroppedProperty());
        droppedColumn.setCellFactory((tc) -> {
            CheckBoxTreeTableCell c = new CheckBoxTreeTableCell<>();
            c.setFocusTraversable(false);
            c.setAlignment(Pos.CENTER);
            return c;
        });
        droppedColumn.setContextMenu(null);

        TreeTableColumn<Grade, Boolean> submittedColumn = new JFXTreeTableColumn<>("Submitted");
        submittedColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, Boolean> param) ->
                param.getValue().getValue().getSubmittedProperty());
        submittedColumn.setCellFactory((tc) -> {
            CheckBoxTreeTableCell c = new CheckBoxTreeTableCell<>();
            c.setFocusTraversable(false);
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

        ListChangeListener<Grade> gradeListener = new ListChangeListener<Grade>() {
            @Override
            public void onChanged(Change<? extends Grade> c) {
                while (c.next()) {
                    if (c.wasUpdated()) {
                        Grade g = selectedAssignment.getGrades().get(c.getFrom());

                        gradeObservableList.removeListener(this);

                        recalculateStats(g);

                        gradeObservableList.addListener(this);
                    }
                }
            }
        };

        gradeObservableList.addAll(selectedAssignment.getGrades());
        gradeObservableList.addListener(gradeListener);

        btnSubmitAll.setOnAction(event -> {
            for (Grade g : gradeObservableList) {
                g.setSubmitted(true);
            }
        });

        final TreeItem<Grade> root = new RecursiveTreeItem<>(gradeObservableList, RecursiveTreeObject::getChildren);

        assignmentGradeTable.setRoot(root);
        assignmentGradeTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        assignmentGradeTable.setTableMenuButtonVisible(false);
        assignmentGradeTable.setShowRoot(false);
        assignmentGradeTable.setEditable(true);
        assignmentGradeTable.getColumns().setAll(studentNameColumn, gradeFloatColumn, maxScoreColumn, overdueColumn, missingColumn, droppedColumn, submittedColumn);
    }

    public void backClick(ActionEvent actionEvent) {
        md.setSelectedAssignment(null);
        md.setSelectedCategory(null);

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

        Student s = g.getStudentReference();
        s.setAverageGrade(s.getGrades(),md.selectedCourse.getScale());

        c.PopulateAverages(c.getlStudents());
        c.calculateStats();

        Statistics st = selectedAssignment.getStAssignmentStats();

        st.calculateMean(selectedAssignment.getGrades());
        st.calculateMedian(selectedAssignment.getGrades());
        st.calculateMode(selectedAssignment.getGrades());
        st.calculateStandardDev(selectedAssignment.getGrades());

        md.addUpdatedObject(selectedAssignment);
        md.addUpdatedObject(s);
        md.addUpdatedObject(c);
        md.addUpdatedObject(g);

        lblMean.setText("Average: " + (Math.round(st.getMean() * 100.0) / 100.0));
        lblMedian.setText("Median: " + (Math.round(st.getMedian() * 100.0) / 100.0));
        lblMode.setText("Mode: " + (Math.round(st.getMode() * 100.0) / 100.0));
        lblStandardDev.setText("Standard Deviation: " + (Math.round(st.getStandardDev() * 100.0) / 100.0));
    }

    private JFXTreeTableView getTableView() {
        return assignmentGradeTable;
    }
}
