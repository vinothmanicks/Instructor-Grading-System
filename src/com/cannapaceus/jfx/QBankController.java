package com.cannapaceus.jfx;

import com.cannapaceus.grader.*;
import com.cannapaceus.qbank.Question;
import com.cannapaceus.qbank.eQuestionAssignmentType;
import com.cannapaceus.qbank.eQuestionLevel;
import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class QBankController {

    ScreenController sc = null;
    Model md = null;
    DBService db = null;

    private HashMap<String, Course> hmCourses = new HashMap<>();
    private HashMap<String, Term> hmTerms = new HashMap<>();
    private HashMap<String, Question> hmQuestion = new HashMap<>();

    private ArrayList<Question> qList = new ArrayList<>();
    private ArrayList<Question> selectedQuestions = new ArrayList<>();

    private Course selectedCourse = null;
    private Term selectedTerm = null;
    private ObservableList<Question> questionObservableList;

    @FXML
    private VBox vbCourse;

    @FXML
    private JFXComboBox cbCourse;

    @FXML
    private JFXTreeTableView questionTable;

    @FXML
    private JFXButton btnDeleteQuestion;

    @FXML
    private JFXButton btnEditQuestion;

    @FXML
    private JFXButton btnAddQuestion;

    @FXML
    private JFXButton btnGenerate;

    @FXML
    private void initialize() {
        sc = ScreenController.getInstance();
        md = Model.getInstance();
        db = DBService.getInstance();

        ObservableList<String> courseItems = FXCollections.observableArrayList();

        selectedCourse = md.getSelectedCourse();
        String selectedKey = "";

        for (Term t : md.getTerms()) {
            if (t.getDBID() == 0)
                continue;
            String s = t.getSeason().toString() + " " + t.getYear();
            for (Course c : t.getCourses()) {
                if (c.getDBID() == 0)
                    continue;
                String key = s + " " + c.getCourseName();
                if (selectedCourse == c)
                    selectedKey = key;
                courseItems.add(key);
                hmCourses.put(key, c);
                hmTerms.put(key, t);
            }
        }

        cbCourse.setItems(courseItems);
        cbCourse.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> selected, String oldValue, String newValue) {
                selectedCourse = hmCourses.get(newValue);
                selectedTerm = hmTerms.get(newValue);
                md.setSelectedCourse(selectedCourse);
                md.setSelectedTerm(selectedTerm);
                btnAddQuestion.setDisable(false);
                questionObservableList.clear();
                populateTable();
            }
        });

        createTable();

        if (selectedCourse != null) {
            cbCourse.getSelectionModel().select(selectedKey);
        }
    }

    private void createTable() {
        TreeTableColumn<Question, String> questionCol = new JFXTreeTableColumn<>("Question");
        questionCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Question, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getQuestion()));
        questionCol.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>();
            c.setWrapText(true);
            c.setFocusTraversable(false);
            c.setEditable(false);
            return c;
        });
        questionCol.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.toUpperCase().compareTo(o2.toUpperCase());
            }
        });
        questionCol.setMaxWidth(400);
        questionCol.setContextMenu(null);

        TreeTableColumn<Question, String> questionTypeCol = new JFXTreeTableColumn<>("Type");
        questionTypeCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Question, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getQuestionType().toString()));
        questionTypeCol.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>();
            c.setFocusTraversable(false);
            c.setEditable(false);
            return c;
        });
        questionTypeCol.setContextMenu(null);

        TreeTableColumn<Question, String> questionLevelCol = new JFXTreeTableColumn<>("Difficulty");
        questionLevelCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Question, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getQuestionLevel().toString()));
        questionLevelCol.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>();
            c.setFocusTraversable(false);
            c.setEditable(false);
            return c;
        });
        questionLevelCol.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.compare(eQuestionLevel.fromStr(o1).getInt(), eQuestionLevel.fromStr(o2).getInt());
            }
        });
        questionLevelCol.setContextMenu(null);

        TreeTableColumn<Question, String> questionAssignmentTypeCol = new JFXTreeTableColumn<>("Assignment Type");
        questionAssignmentTypeCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Question, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getQuestionAssignmentType().toString()));
        questionAssignmentTypeCol.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>();
            c.setFocusTraversable(false);
            c.setEditable(false);
            return c;
        });
        questionAssignmentTypeCol.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.compare(eQuestionAssignmentType.fromStr(o1).getInt(),
                        eQuestionAssignmentType.fromStr(o2).getInt());
            }
        });
        questionAssignmentTypeCol.setContextMenu(null);

        TreeTableColumn<Question, String> timeCol = new JFXTreeTableColumn<>("Time to Complete");
        timeCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Question, String> param) ->
                new ReadOnlyStringWrapper("" + param.getValue().getValue().getToDoTime()));
        timeCol.setCellFactory((tc) -> {
            GenericEditableTreeTableCell c = new GenericEditableTreeTableCell<>();
            c.setFocusTraversable(false);
            c.setEditable(false);
            return c;
        });
        timeCol.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Float.compare(Float.valueOf(o1), Float.valueOf(o2));
            }
        });
        timeCol.setContextMenu(null);

        TreeTableColumn<Question, Boolean> selectedCol = new JFXTreeTableColumn<>("Selected");
        selectedCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Question, Boolean> param) -> {
            BooleanProperty b = new SimpleBooleanProperty();
            b.addListener(((observable, oldValue, newValue) -> {
                if (newValue) {
                    selectedQuestions.add(param.getValue().getValue());
                } else {
                    selectedQuestions.remove(param.getValue().getValue());
                }

                btnGenerate.setDisable(selectedQuestions.isEmpty());
            }));
            return b;
        });
        selectedCol.setCellFactory((tc) -> {
            CheckBoxTreeTableCell c = new CheckBoxTreeTableCell<>();
            c.setFocusTraversable(false);
            c.setAlignment(Pos.CENTER);
            return c;
        });
        selectedCol.setSortable(false);
        selectedCol.setContextMenu(null);

        questionObservableList = FXCollections.observableArrayList();

        final TreeItem<Question> root = new RecursiveTreeItem<>(questionObservableList, RecursiveTreeObject::getChildren);

        questionTable.setRoot(root);
        questionTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        questionTable.setTableMenuButtonVisible(false);
        questionTable.setShowRoot(false);
        questionTable.setEditable(true);
        questionTable.getColumns().setAll(selectedCol, questionCol, questionTypeCol, questionLevelCol, questionAssignmentTypeCol, timeCol);

        questionTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue selected, Object oldValue, Object newValue) {
                TreeItem<Question> selectedItem = (TreeItem<Question>)newValue;
                if (selectedItem != null) {
                    Question q = selectedItem.getValue();
                    md.setSelectedQuestion(q);
                    if (q == null) {
                        btnDeleteQuestion.setDisable(true);
                        btnEditQuestion.setDisable(true);
                    } else {
                        btnDeleteQuestion.setDisable(false);
                        btnEditQuestion.setDisable(false);
                    }
                } else {
                    btnDeleteQuestion.setDisable(false);
                    btnEditQuestion.setDisable(false);
                }
            }
        });
    }

    private void populateTable() {
        if (selectedCourse != null) {
            questionObservableList.addAll(db.retrieveQuestions(selectedCourse.getDBID()));
        }
    }

    public void generateAssignment(ActionEvent event) {
        try {
            md.saveSelectedQuestions(selectedQuestions);
            sc.addScreen("GenerateAssignment", FXMLLoader.load(getClass().getResource("../jfxml/QBankGenerateAssignmentView.fxml")));
            sc.activate("GenerateAssignment");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editQuestion(ActionEvent event) {
        try {
            sc.addScreen("AddQuestion", FXMLLoader.load(getClass().getResource("../jfxml/QBankAddQuestionView.fxml")));
            sc.activate("AddQuestion");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteQuestion(ActionEvent event) {
        db.deleteQuestion(md.getSelectedQuestion());
        md.setSelectedQuestion(null);
        questionObservableList.clear();
        populateTable();
        btnDeleteQuestion.setDisable(true);
        btnEditQuestion.setDisable(true);
    }

    public void addQuestion(ActionEvent event) {
        md.setSelectedQuestion(null);

        try {
            sc.addScreen("AddQuestion", FXMLLoader.load(getClass().getResource("../jfxml/QBankAddQuestionView.fxml")));
            sc.activate("AddQuestion");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
