package com.cannapaceus.jfx;

import com.cannapaceus.grader.Term;
import com.cannapaceus.grader.eSeason;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.util.Collections;

public class TermFormController {
    ScreenController sc = null;
    Model md = null;

    Term selectedTerm;

    @FXML
    private JFXComboBox<String> cbSeason;

    @FXML
    private JFXTextField tfYear;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        selectedTerm = md.getSelectedTerm();

        ObservableList<String> list = FXCollections.observableArrayList("Winter", "Spring", "Summer", "Fall");

        cbSeason.setItems(list);
        cbSeason.getSelectionModel().select(selectedTerm.getSeason().toString());

        tfYear.setText("" + selectedTerm.getYear());

        tfYear.textProperty().addListener((o, oldVal, newVal) -> {
            tfYear.validate();
        });
    }

    public void cancelClick(ActionEvent event) {
        if (selectedTerm.getDBID() == 0 && !md.getNewObjects().contains(selectedTerm)) {
            md.removeTerm(selectedTerm);
        }

        try {
            sc.addScreen("Terms", FXMLLoader.load(getClass().getResource("/TermsView.fxml")));
            sc.activate("Terms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveClick(ActionEvent event) {
        if (!formValidate())
            return;

        selectedTerm.setCourseSeason(eSeason.valueOf(cbSeason.getValue().toUpperCase()));
        selectedTerm.setYear(Integer.valueOf(tfYear.getText()));

        if (selectedTerm.getDBID() == 0 && !md.getNewObjects().contains(selectedTerm)) {
            md.addNewObject(selectedTerm);
        } else {
            md.addUpdatedObject(selectedTerm);
        }

        Collections.sort(md.getTerms(), Term.termComparator);

        try {
            sc.addScreen("Terms", FXMLLoader.load(getClass().getResource("/TermsView.fxml")));
            sc.activate("Terms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean formValidate() {
        if (!tfYear.validate())
            return false;

        return true;
    }
}
