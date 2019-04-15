package com.cannapaceus.jfx;

import com.cannapaceus.grader.Course;
import com.cannapaceus.grader.DBService;
import com.cannapaceus.qbank.Question;
import com.cannapaceus.qbank.QuestionBank;
import com.cannapaceus.qbank.eQuestionLevel;
import com.cannapaceus.qbank.eQuestionAssignmentType;
import com.cannapaceus.qbank.eQuestionType;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class QBankAddQuestionController {

    ScreenController sc = null;
    Model md = null;
    DBService db = null;

    private Question selectedQuestion = null;

    private eQuestionType qt = null;
    private eQuestionAssignmentType qat = null;
    private eQuestionLevel ql = null;

    @FXML
    private VBox vbTerms;

    @FXML
    private JFXTextArea taQuestion;

    @FXML
    private JFXTextField tfScore;

    @FXML
    private JFXTextField tfTimeToDo;

    @FXML
    private ToggleGroup levelGroup;

    @FXML
    private ToggleGroup typeGroup;

    @FXML
    private JFXComboBox cbType;

    @FXML
    private JFXComboBox cbLevel;

    @FXML
    private JFXComboBox cbAssignType;

    @FXML
    private void initialize() {
        sc = ScreenController.getInstance();
        md = Model.getInstance();
        db = DBService.getInstance();

        selectedQuestion = md.getSelectedQuestion();

        taQuestion.textProperty().addListener((o, oldVal, newVal) -> {
            taQuestion.validate();
        });

        tfTimeToDo.textProperty().addListener((o, oldVal, newVal) -> {
            tfTimeToDo.validate();
        });

        tfScore.textProperty().addListener((o, oldVal, newVal) -> {
            tfScore.validate();
        });

        cbType.setItems(FXCollections.observableArrayList(
                "Multiple Choice",
                "Fill in the Blank",
                "True or False",
                "Short Answer",
                "Long Answer"));

        cbLevel.setItems(FXCollections.observableArrayList(
                "Easy",
                "Medium",
                "Hard",
                "Higher Order Thinking Skills"));

        cbAssignType.setItems(FXCollections.observableArrayList(
                "Test",
                "Quiz",
                "Homework",
                "Other"));

        if (selectedQuestion != null) {
            taQuestion.setText(selectedQuestion.getQuestion());
            tfTimeToDo.setText("" + selectedQuestion.getToDoTime());
            tfScore.setText("" + selectedQuestion.getScore());
            cbType.getSelectionModel().select(selectedQuestion.getQuestionType().getInt());
            cbLevel.getSelectionModel().select(selectedQuestion.getQuestionLevel().getInt());
            cbAssignType.getSelectionModel().select(selectedQuestion.getQuestionAssignmentType().getInt());
        }
    }

    public void saveClick(ActionEvent event) {
        if (!formValidate())
            return;

        if (selectedQuestion != null) {
            eQuestionType qt = eQuestionType.fromStr(cbType.getSelectionModel().getSelectedItem().toString());
            eQuestionAssignmentType qat = eQuestionAssignmentType.fromStr(cbAssignType.getSelectionModel().getSelectedItem().toString());
            eQuestionLevel ql = eQuestionLevel.fromStr(cbLevel.getSelectionModel().getSelectedItem().toString());

            float fTimeToDo = Float.valueOf(tfTimeToDo.getText());

            ArrayList<String> arrAnswers = new ArrayList<>();
            if (qt == eQuestionType.MULTIPLECHOICE) {

            }

            selectedQuestion.setQuestion(taQuestion.getText());
            selectedQuestion.setfScore(Float.valueOf(tfScore.getText()).floatValue());
            selectedQuestion.setAnwsers(arrAnswers);
            selectedQuestion.setQuestionType(qt);
            selectedQuestion.setQuestionAssignmentType(qat);
            selectedQuestion.setQlQuestionLevel(ql);
            selectedQuestion.setfToDoTime(fTimeToDo);

            db.updateQuestion(selectedQuestion);
        } else {
            Course selectedCourse = md.getSelectedCourse();
            String courseName = selectedCourse.getCourseName();

            eQuestionType qt = eQuestionType.fromStr(cbType.getSelectionModel().getSelectedItem().toString());
            eQuestionAssignmentType qat = eQuestionAssignmentType.fromStr(cbAssignType.getSelectionModel().getSelectedItem().toString());
            eQuestionLevel ql = eQuestionLevel.fromStr(cbLevel.getSelectionModel().getSelectedItem().toString());

            float fTimeToDo = Float.valueOf(tfTimeToDo.getText());

            ArrayList<String> arrAnswers = new ArrayList<>();
            if (qt == eQuestionType.MULTIPLECHOICE) {

            }

            Question temp = new Question(taQuestion.getText(),
                    Float.valueOf(tfScore.getText()).floatValue(),
                    arrAnswers,
                    qt,
                    qat,
                    ql,
                    fTimeToDo);

            db.storeQuestion(temp, selectedCourse.getDBID());
        }

        try {
            sc.addScreen("QBank", FXMLLoader.load(getClass().getResource("../jfxml/QBankView.fxml")));
            sc.activate("QBank");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backClick(ActionEvent actionEvent) {
        try {
            sc.addScreen("QBank", FXMLLoader.load(getClass().getResource("../jfxml/QBankView.fxml")));
            sc.activate("QBank");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean formValidate() {
        if(!taQuestion.validate())
            return false;
        if(!tfTimeToDo.validate())
            return false;
        if (!tfScore.validate())
            return false;
        return true;
    }
}
