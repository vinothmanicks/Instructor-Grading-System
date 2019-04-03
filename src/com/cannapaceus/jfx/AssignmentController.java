package com.cannapaceus.jfx;

import com.cannapaceus.grader.Assignment;
import com.cannapaceus.grader.Grade;
import com.cannapaceus.grader.Statistics;
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
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.util.Callback;

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
    private void initialize()
    {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        selectedAssignment = md.getSelectedAssignment();

        lblAssignmentName.setText(selectedAssignment.getAssignmentName());

        TreeTableColumn<Grade, String> studentNameColumn = new JFXTreeTableColumn<>("Student");
        studentNameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getStudentReference().getLastName() +
                        ", " + param.getValue().getValue().getStudentReference().getFirstMIName()));
        studentNameColumn.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder());
            c.setEditable(false);
            return c;
        });

        TreeTableColumn<Grade, Float> gradeFloatColumn = new JFXTreeTableColumn<>("Grade");
        gradeFloatColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, Float> param) ->
                param.getValue().getValue().getGradeProperty().asObject());
        gradeFloatColumn.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>(new DoubleTextFieldEditorBuilder());
            return c;
        });

        TreeTableColumn<Grade, String> maxScoreColumn = new JFXTreeTableColumn<>("Out of");
        maxScoreColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, String> param) ->
                new ReadOnlyStringWrapper("" + param.getValue().getValue().getAssignmentReference().getMaxScore()));
        maxScoreColumn.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder());
            c.setEditable(false);
            return c;
        });

        TreeTableColumn<Grade, Boolean> overdueColumn = new JFXTreeTableColumn<>("Overdue");
        overdueColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, Boolean> param) ->
                param.getValue().getValue().getOverdueProperty());
        overdueColumn.setCellFactory((tc) -> {
            CheckBoxTreeTableCell c = new CheckBoxTreeTableCell<>();
            c.setAlignment(Pos.CENTER);
            return c;
        });

        TreeTableColumn<Grade, Boolean> missingColumn = new JFXTreeTableColumn<>("Missing");
        missingColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, Boolean> param) ->
                param.getValue().getValue().getMissingProperty());
        missingColumn.setCellFactory((tc) -> {
            CheckBoxTreeTableCell c = new CheckBoxTreeTableCell<>();
            c.setAlignment(Pos.CENTER);
            return c;
        });

        TreeTableColumn<Grade, Boolean> droppedColumn = new JFXTreeTableColumn<>("Dropped");
        droppedColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, Boolean> param) ->
                param.getValue().getValue().getDroppedProperty());
        droppedColumn.setCellFactory((tc) -> {
            CheckBoxTreeTableCell c = new CheckBoxTreeTableCell<>();
            c.setAlignment(Pos.CENTER);
            return c;
        });

        TreeTableColumn<Grade, Boolean> submittedColumn = new JFXTreeTableColumn<>("Submitted");
        submittedColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Grade, Boolean> param) ->
                param.getValue().getValue().getSubmittedProperty());
        submittedColumn.setCellFactory((tc) -> {
            CheckBoxTreeTableCell c = new CheckBoxTreeTableCell<>();
            c.setAlignment(Pos.CENTER);
            return c;
        });

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
        gradeObservableList.addAll(selectedAssignment.getGrades());
        gradeObservableList.addListener(new ListChangeListener<Grade>() {
            @Override
            public void onChanged(Change<? extends Grade> c) {
                while (c.next()) {
                    if (c.wasUpdated()) {
                        Grade g = selectedAssignment.getGrades().get(c.getFrom());
                        md.addUpdatedObject(g);

                        recalculateStats();
                    }
                }
            }
        });

        final TreeItem<Grade> root = new RecursiveTreeItem<>(gradeObservableList, RecursiveTreeObject::getChildren);

        assignmentGradeTable.setRoot(root);
        assignmentGradeTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        assignmentGradeTable.setShowRoot(false);
        assignmentGradeTable.setEditable(true);
        assignmentGradeTable.getColumns().setAll(studentNameColumn, gradeFloatColumn, maxScoreColumn, overdueColumn, missingColumn, droppedColumn, submittedColumn);
    }

    public void backClick(ActionEvent actionEvent) {
        sc.activate("Course");
    }

    public void commitClick(ActionEvent e) {
        md.commitChanges();
    }

    public void revertClick(ActionEvent e) {
        md.revertChanges();
        initialize();
    }

    private void recalculateStats() {
        Statistics st = selectedAssignment.getStAssignmentStats();

        st.calculateMean(selectedAssignment.getGrades());
        st.calculateMedian(selectedAssignment.getGrades());
        st.calculateMode(selectedAssignment.getGrades());
        st.calculateStandardDev(selectedAssignment.getGrades());

        md.addUpdatedObject(selectedAssignment);

        lblMean.setText("Average: " + (Math.round(st.getMean() * 100.0) / 100.0));
        lblMedian.setText("Median: " + (Math.round(st.getMedian() * 100.0) / 100.0));
        lblMode.setText("Mode: " + (Math.round(st.getMode() * 100.0) / 100.0));
        lblStandardDev.setText("Standard Deviation: " + (Math.round(st.getStandardDev() * 100.0) / 100.0));
    }
}
