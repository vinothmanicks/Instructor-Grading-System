package com.cannapaceus.jfx;

import com.cannapaceus.grader.*;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class CourseController {
    ScreenController sc = null;
    Model md = null;

    Course selectedCourse;

    @FXML
    private Label lblCourseName;

    @FXML
    private VBox vbCourse;

    @FXML
    private VBox vbStudents;

    @FXML
    private VBox vbAssignments;

    @FXML
    private VBox vbGrades;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        selectedCourse = md.getSelectedCourse();

        lblCourseName.setText(selectedCourse.getCourseName());

        createStudentList();
        createAssignmentList();
        createGradeList();
    }

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        sc.activate("Terms");
    }

    private void createStudentList() {
        for (Student s : selectedCourse.getlStudents()) {
            Separator tempSep = new Separator();

            HBox tempHB = new HBox();
            tempHB.setSpacing(10.0);
            tempHB.setAlignment(Pos.CENTER_LEFT);

            Label tempLabel = new Label(s.getFirstMIName() + " " + s.getLastName());
            tempLabel.setAlignment(Pos.CENTER_LEFT);

            Pane tempPane = new Pane();

            FontAwesomeIconView tempFA = new FontAwesomeIconView();
            tempFA.setGlyphName("TRASH_ALT");
            tempFA.setGlyphSize(20);
            tempFA.setGlyphStyle("-fx-fill: grey;");

            JFXButton tempDelete = new JFXButton("");
            tempDelete.setAlignment(Pos.BASELINE_CENTER);
            tempDelete.setGraphic(tempFA);
            tempDelete.setStyle("-fx-cursor: hand;");
            tempDelete.setRipplerFill(Color.WHITE);
            tempDelete.setButtonType(JFXButton.ButtonType.FLAT);

            tempHB.getChildren().addAll(tempLabel, tempPane, tempDelete);
            tempHB.setHgrow(tempPane, Priority.ALWAYS);
            vbStudents.getChildren().addAll(tempSep, tempHB);
        }
    }

    private void createAssignmentList() {
        for (Assignment a : selectedCourse.getlAssignments()) {
            Separator tempSep = new Separator();

            HBox tempHB = new HBox();
            tempHB.setSpacing(10.0);
            tempHB.setAlignment(Pos.CENTER_LEFT);

            Label tempLabel = new Label(a.getAssignmentName());
            tempLabel.setAlignment(Pos.CENTER_LEFT);

            Pane tempPane = new Pane();

            FontAwesomeIconView tempFA = new FontAwesomeIconView();
            tempFA.setGlyphName("TRASH_ALT");
            tempFA.setGlyphSize(20);
            tempFA.setGlyphStyle("-fx-fill: grey;");

            JFXButton tempDelete = new JFXButton("");
            tempDelete.setAlignment(Pos.BASELINE_CENTER);
            tempDelete.setGraphic(tempFA);
            tempDelete.setStyle("-fx-cursor: hand;");
            tempDelete.setRipplerFill(Color.WHITE);
            tempDelete.setButtonType(JFXButton.ButtonType.FLAT);

            tempHB.getChildren().addAll(tempLabel, tempPane, tempDelete);
            tempHB.setHgrow(tempPane, Priority.ALWAYS);
            vbAssignments.getChildren().addAll(tempSep, tempHB);
        }
    }

    private void createGradeList() {
        for (Grade g : selectedCourse.getlGrades()) {
            Separator tempSep = new Separator();

            HBox tempHB = new HBox();
            tempHB.setSpacing(10.0);
            tempHB.setAlignment(Pos.CENTER_LEFT);

            Label tempLabel = new Label(g.getStudentCopy().getFirstMIName() + " " +
                    g.getStudentCopy().getLastName() + " : " +
                    g.getAssignmentCopy().getAssignmentName());

            tempLabel.setAlignment(Pos.CENTER_LEFT);

            Pane tempPane = new Pane();

            FontAwesomeIconView tempFA = new FontAwesomeIconView();
            tempFA.setGlyphName("TRASH_ALT");
            tempFA.setGlyphSize(20);
            tempFA.setGlyphStyle("-fx-fill: grey;");

            JFXButton tempDelete = new JFXButton("");
            tempDelete.setAlignment(Pos.BASELINE_CENTER);
            tempDelete.setGraphic(tempFA);
            tempDelete.setStyle("-fx-cursor: hand;");
            tempDelete.setRipplerFill(Color.WHITE);
            tempDelete.setButtonType(JFXButton.ButtonType.FLAT);

            tempHB.getChildren().addAll(tempLabel, tempPane, tempDelete);
            tempHB.setHgrow(tempPane, Priority.ALWAYS);
            vbGrades.getChildren().addAll(tempSep, tempHB);
        }
    }

    @FXML
    private void expand(ActionEvent e) {
        JFXButton t = (JFXButton) e.getTarget();

        HBox p = (HBox) t.getParent();

        VBox vbTerm = (VBox) p.getParent();

        p.getChildren().remove(t);

        FontAwesomeIconView tempFA = new FontAwesomeIconView();
        tempFA.setGlyphName("CHEVRON_DOWN");
        tempFA.setGlyphSize(20);
        tempFA.setGlyphStyle("-fx-fill: grey;");

        JFXButton tempCollapse = new JFXButton("");
        tempCollapse.setAlignment(Pos.BASELINE_CENTER);
        tempCollapse.setGraphic(tempFA);
        tempCollapse.setStyle("-fx-cursor: hand;");
        tempCollapse.setRipplerFill(Color.WHITE);
        tempCollapse.setButtonType(JFXButton.ButtonType.FLAT);
        tempCollapse.setOnAction((event -> collapse(event)));

        for (Node n : vbTerm.getChildren()) {
            if (n instanceof VBox) {
                n.setVisible(true);
                n.setManaged(true);
            }
        }

        p.getChildren().add(tempCollapse);
    }

    private void collapse(ActionEvent e) {
        JFXButton t = (JFXButton) e.getTarget();

        HBox p = (HBox) t.getParent();

        VBox vBox = (VBox) p.getParent();

        p.getChildren().remove(t);

        FontAwesomeIconView tempFA = new FontAwesomeIconView();
        tempFA.setGlyphName("CHEVRON_RIGHT");
        tempFA.setGlyphSize(20);
        tempFA.setGlyphStyle("-fx-fill: grey;");

        JFXButton tempExpand = new JFXButton("");
        tempExpand.setAlignment(Pos.BASELINE_CENTER);
        tempExpand.setGraphic(tempFA);
        tempExpand.setStyle("-fx-cursor: hand;");
        tempExpand.setRipplerFill(Color.WHITE);
        tempExpand.setButtonType(JFXButton.ButtonType.FLAT);
        tempExpand.setOnAction((event -> expand(event)));

        for (Node n : vBox.getChildren()) {
            if (n instanceof VBox) {
                n.setVisible(false);
                n.setManaged(false);
            }
        }

        p.getChildren().add(tempExpand);
    }
}
