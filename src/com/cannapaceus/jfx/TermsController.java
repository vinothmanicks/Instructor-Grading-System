package com.cannapaceus.jfx;

import com.cannapaceus.grader.Course;
import com.cannapaceus.grader.Term;
import com.cannapaceus.grader.eSeason;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.scene.input.MouseEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class TermsController {
    ScreenController sc = null;
    Model md = null;

    ArrayList<Term> lTerms;

    HashMap<String, Course> hmCourse;
    HashMap<String, Term> hmTerm;

    @FXML
    private VBox vbTerms;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        lTerms = md.getTerms();

        hmCourse = new HashMap<>();
        hmTerm = new HashMap<>();

        createTermList();
    }

    public void courseClick(MouseEvent event) {
        Term targetTerm = hmTerm.get(((Node) event.getSource()).getParent().getParent().getId());
        Course targetCourse = hmCourse.get(((Node) event.getSource()).getId());

        md.setSelectedTerm(targetTerm);
        md.setSelectedCourse(targetCourse);

        try {
            sc.addScreen("Course", FXMLLoader.load(getClass().getResource("../jfxml/CourseView.fxml")));
            sc.activate("Course");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTerm(ActionEvent event) {
        eSeason s;
        int m = LocalDateTime.now().getMonth().getValue();
        int y = LocalDateTime.now().getYear();

        if (m < 2) {
            s = eSeason.WINTER;
        } else if (m < 5) {
            s = eSeason.SPRING;
        } else if (m < 8) {
            s = eSeason.SUMMER;
        } else if (m < 11) {
            s = eSeason.FALL;
        } else {
            s = eSeason.WINTER;
        }

        Term t = new Term(y, s);

        md.addTerm(t);
        md.setSelectedTerm(t);

        try {
            sc.addScreen("TermForm", FXMLLoader.load(getClass().getResource("../jfxml/TermFormView.fxml")));
            sc.activate("TermForm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTermList() {
        for (Term t : lTerms) {
            Separator tempSep = new Separator();

            VBox tempVB = new VBox();
            tempVB.setSpacing(10.0);
            HBox tempHB = new HBox();
            tempHB.setAlignment(Pos.CENTER_LEFT);

            Label tempLabel = new Label(t.getSeason().toString() + " " + t.getYear());
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

            tempFA = new FontAwesomeIconView();
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

            VBox vbCourses = new VBox();
            vbCourses.setVisible(false);
            vbCourses.setManaged(false);

            for (Course c : t.getCourses()) {
                HBox hbCourse = new HBox();
                hbCourse.setSpacing(10.0);
                hbCourse.setStyle("-fx-padding: 10,10,10,10;");

                Label lblCourseName = new Label(c.getCourseName());
                Label lblCourseDept = new Label(c.getDepartment());

                Pane spanPane = new Pane();

                tempFA = new FontAwesomeIconView();
                tempFA.setGlyphName("TRASH_ALT");
                tempFA.setGlyphSize(20);
                tempFA.setGlyphStyle("-fx-fill: grey;");

                JFXButton courseDelete = new JFXButton("");
                courseDelete.setAlignment(Pos.BASELINE_CENTER);
                courseDelete.setGraphic(tempFA);
                courseDelete.setStyle("-fx-cursor: hand;");
                courseDelete.setRipplerFill(Color.LIGHTGREY);
                courseDelete.setButtonType(JFXButton.ButtonType.FLAT);

                hbCourse.setId("" + c.getDBID());
                hmCourse.put("" + c.getDBID(), c);

                hbCourse.setOnMouseClicked((event -> courseClick(event)));

                hbCourse.getChildren().addAll(lblCourseName, lblCourseDept, spanPane, courseDelete);
                hbCourse.setHgrow(spanPane, Priority.ALWAYS);
                vbCourses.getChildren().add(hbCourse);
            }

            tempVB.setId("" + t.getDBID());
            hmTerm.put("" + t.getDBID(), t);

            tempHB.getChildren().addAll(tempLabel, tempPane, tempDelete, tempExpand);
            tempHB.setHgrow(tempPane, Priority.ALWAYS);
            tempVB.getChildren().addAll(tempHB, vbCourses);

            vbTerms.getChildren().addAll(tempSep, tempVB);
        }
    }

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

        VBox vbTerm = (VBox) p.getParent();

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

        for (Node n : vbTerm.getChildren()) {
            if (n instanceof VBox) {
                n.setVisible(false);
                n.setManaged(false);
            }
        }

        p.getChildren().add(tempExpand);
    }
}
