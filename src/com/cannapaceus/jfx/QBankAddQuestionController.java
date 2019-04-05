package com.cannapaceus.jfx;

import com.cannapaceus.qbank.QuestionBank;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class QBankAddQuestionController {

    ScreenController sc = null;
    Model md = null;

    @FXML
    private VBox vbTerms;

    @FXML
    private JFXTextField tfScore;

    @FXML
    private  JFXTextField tfTimeToDo;

    @FXML
    private void initialize() {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        tfTimeToDo.textProperty().addListener((o, oldVal, newVal) -> {
            tfTimeToDo.validate();
        });

        tfScore.textProperty().addListener((o, oldVal, newVal) -> {
            tfScore.validate();
        });
    }

    public void saveClick(ActionEvent event) {
        if (!formValidate())
            return;
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
        if(!tfTimeToDo.validate())
            return false;
        if (!tfScore.validate())
            return false;
        return true;
    }
}
