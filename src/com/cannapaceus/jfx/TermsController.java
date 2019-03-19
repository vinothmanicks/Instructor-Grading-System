package com.cannapaceus.jfx;

import com.cannapaceus.grader.DBService;
import com.cannapaceus.grader.Term;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

class TermCell extends ListCell<Term> {
    VBox vbox = new VBox();
    HBox hbox = new HBox();
    Label label = new Label("");
    Pane hbPane = new Pane();
    JFXButton btnDelete = new JFXButton("Delete");
    JFXButton btnAdd = new JFXButton("Add Course");

    public TermCell() {
        super();

        hbox.getChildren().addAll(label, hbPane, btnDelete);
        hbox.setHgrow(hbPane, Priority.ALWAYS);
        vbox.getChildren().addAll(hbox, btnAdd);
    }

    public void updateItem(Term t, boolean empty) {
        super.updateItem(t, empty);
        setText(null);
        setGraphic(null);

        if (t != null && !empty) {
            label.setText(t.getSeason().toString() + " " + t.getYear());
            setGraphic(vbox);
        }
    }
}

public class TermsController {
    ScreenController sc = null;
    DBService db = null;

//    ObservableList<Term> lTerms;
    ArrayList<Term> lTerms;

    @FXML
    private JFXListView<Term> lvTerms;

    @FXML
    private VBox vbTerms;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        db = DBService.getInstance();

//        lTerms = FXCollections.observableArrayList(db.retrieveTerms());

//        lvTerms.setItems(lTerms);
//        lvTerms.setCellFactory(param -> new TermCell());

        lTerms = db.retrieveTerms();

        for (Term t : lTerms) {
            Separator tempSep = new Separator();

            VBox tempVB = new VBox();
            HBox tempHB = new HBox();
            tempHB.setAlignment(Pos.CENTER_LEFT);
            Label tempLabel = new Label(t.getSeason().toString() + " " + t.getYear());
            tempLabel.setAlignment(Pos.CENTER_LEFT);
            Pane tempPane = new Pane();
            JFXButton tempDelete = new JFXButton("Delete");
            tempDelete.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
            tempDelete.setButtonType(JFXButton.ButtonType.RAISED);

            tempHB.getChildren().addAll(tempLabel, tempPane, tempDelete);
            tempHB.setHgrow(tempPane, Priority.ALWAYS);
            tempVB.getChildren().add(tempHB);

            vbTerms.getChildren().addAll(tempSep, tempVB);
        }
    }

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        sc.activate("Course");
    }

    private void createTermList() {

    }
}
