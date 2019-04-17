package com.cannapaceus.jfx;

import com.cannapaceus.grader.*;
import com.cannapaceus.services.CSVService;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.FileFilter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class TermsController {
    ScreenController sc = null;
    Model md = null;
    CSVService csvService;

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

        csvService = new CSVService();

        lTerms = md.getTerms();

        hmCourse = new HashMap<>();
        hmTerm = new HashMap<>();

        createTermList();
    }

    private void createTermList() {
        int numTerm = 0;
        int numCourse = 0;

        for (Term t : lTerms) {
            VBox tempVB = new VBox();
            tempVB.setSpacing(10.0);
            tempVB.setStyle("-fx-background-color: white;");

            DropShadow ds = new DropShadow();
            ds.setColor(Color.GREY);
            ds.setOffsetX(4);
            ds.setOffsetY(4);

            tempVB.setEffect(ds);

            HBox tempHB = new HBox();
            tempHB.setAlignment(Pos.CENTER_LEFT);
            tempHB.setStyle("-fx-padding: 10, 10, 10, 10;");

            Label termLabel = new Label(t.getSeason().toString() + " " + t.getYear());
            termLabel.setAlignment(Pos.CENTER_LEFT);

            FontAwesomeIconView tempFA = new FontAwesomeIconView();
            tempFA.setGlyphName("EDIT");
            tempFA.setGlyphSize(20);
            tempFA.setGlyphStyle("-fx-fill: grey;");

            JFXButton termEdit = new JFXButton("");
            termEdit.setAlignment(Pos.BASELINE_CENTER);
            termEdit.setGraphic(tempFA);
            termEdit.setStyle("-fx-cursor: hand;");
            termEdit.setRipplerFill(Color.WHITE);
            termEdit.setButtonType(JFXButton.ButtonType.FLAT);
            termEdit.setOnAction((event -> editTermClick(event)));

            Pane termSpanPane = new Pane();

            tempFA = new FontAwesomeIconView();
            tempFA.setGlyphName("TRASH_ALT");
            tempFA.setGlyphSize(20);
            tempFA.setGlyphStyle("-fx-fill: grey;");

            JFXButton termDelete = new JFXButton("");
            termDelete.setAlignment(Pos.BASELINE_CENTER);
            termDelete.setGraphic(tempFA);
            termDelete.setStyle("-fx-cursor: hand;");
            termDelete.setRipplerFill(Color.WHITE);
            termDelete.setButtonType(JFXButton.ButtonType.FLAT);
            termDelete.setOnAction((event -> deleteTermClick(event)));

            tempFA = new FontAwesomeIconView();
            tempFA.setGlyphName("CHEVRON_RIGHT");
            tempFA.setGlyphSize(20);
            tempFA.setGlyphStyle("-fx-fill: grey;");

            JFXButton termExpand = new JFXButton("");
            termExpand.setPrefWidth(40.0);
            termExpand.setAlignment(Pos.BASELINE_CENTER);
            termExpand.setGraphic(tempFA);
            termExpand.setStyle("-fx-cursor: hand;");
            termExpand.setRipplerFill(Color.WHITE);
            termExpand.setButtonType(JFXButton.ButtonType.FLAT);
            termExpand.setOnAction((event -> expand(event)));

            VBox vbCourses = new VBox();
            vbCourses.setVisible(false); 
            vbCourses.setManaged(false);

            vbCourses.setSpacing(10.0);
            vbCourses.setStyle("-fx-padding: 0 10 10 10;");

            tempFA = new FontAwesomeIconView();
            tempFA.setGlyphName("PLUS");
            tempFA.setGlyphSize(20);
            tempFA.setGlyphStyle("-fx-fill: white;");

            JFXButton termAddCourse = new JFXButton("Add Course");
            termAddCourse.setAlignment(Pos.BASELINE_CENTER);
            termAddCourse.setGraphic(tempFA);
            termAddCourse.setStyle("-fx-cursor: hand; -fx-background-color: #4CAF50; -fx-text-fill: white;");
            termAddCourse.setButtonType(JFXButton.ButtonType.FLAT);
            termAddCourse.setOnAction((event -> addCourseClick(event)));

            tempFA = new FontAwesomeIconView();
            tempFA.setGlyphName("PLUS");
            tempFA.setGlyphSize(20);
            tempFA.setGlyphStyle("-fx-fill: white;");

            Separator tempSep;

            for (Course c : t.getCourses()) {
                tempSep = new Separator();

                HBox hbCourse = new HBox();
                hbCourse.setSpacing(10.0);
                hbCourse.setAlignment(Pos.CENTER_LEFT);

                Label lblCourseName = new Label(c.getCourseName());

                tempFA = new FontAwesomeIconView();
                tempFA.setGlyphName("EDIT");
                tempFA.setGlyphSize(20);
                tempFA.setGlyphStyle("-fx-fill: grey;");

                JFXButton courseEdit = new JFXButton("");
                courseEdit.setAlignment(Pos.BASELINE_CENTER);
                courseEdit.setGraphic(tempFA);
                courseEdit.setStyle("-fx-cursor: hand;");
                courseEdit.setRipplerFill(Color.WHITE);
                courseEdit.setButtonType(JFXButton.ButtonType.FLAT);
                courseEdit.setOnAction((event -> editCourseClick(event)));

                Pane courseSpanPane = new Pane();

                tempFA = new FontAwesomeIconView();
                tempFA.setGlyphName("TRASH_ALT");
                tempFA.setGlyphSize(20);
                tempFA.setGlyphStyle("-fx-fill: grey;");

                JFXButton courseDelete = new JFXButton("");
                courseDelete.setAlignment(Pos.BASELINE_CENTER);
                courseDelete.setGraphic(tempFA);
                courseDelete.setStyle("-fx-cursor: hand;");
                courseDelete.setRipplerFill(Color.WHITE);
                courseDelete.setButtonType(JFXButton.ButtonType.FLAT);
                courseDelete.setOnAction((event -> deleteCourseClick(event)));


                hbCourse.setId("" + numCourse);
                hmCourse.put("" + numCourse, c);

                ++numCourse;

                hbCourse.setOnMouseClicked((event -> courseClick(event)));

                hbCourse.getChildren().addAll(lblCourseName, courseSpanPane, courseEdit, courseDelete);
                hbCourse.setHgrow(courseSpanPane, Priority.ALWAYS);
                vbCourses.getChildren().addAll(tempSep, hbCourse);
            }

            tempSep = new Separator();

            vbCourses.getChildren().addAll(tempSep, termAddCourse);

            tempVB.setId("" + numTerm);
            hmTerm.put("" + numTerm, t);

            ++numTerm;

            tempHB.getChildren().addAll(termLabel, termSpanPane, termEdit, termDelete, termExpand);
            tempHB.setHgrow(termSpanPane, Priority.ALWAYS);

            tempHB.setOnMouseClicked((event) -> termExpand.fire());

            tempVB.getChildren().addAll(tempHB, vbCourses);

            vbTerms.getChildren().addAll(tempVB);
        }
    }

    private void expand(ActionEvent e) {
        JFXButton t = (JFXButton) e.getTarget();

        boolean collapsed;

        FontAwesomeIconView tempFA = (FontAwesomeIconView) t.getGraphic();

        collapsed = tempFA.getGlyphName() == "CHEVRON_RIGHT";

        HBox p = (HBox) t.getParent();

        VBox vbTerm = (VBox) p.getParent();

        if (collapsed) {
            tempFA.setGlyphName("CHEVRON_DOWN");
        } else {
            tempFA.setGlyphName("CHEVRON_RIGHT");
        }

        for (Node n : vbTerm.getChildren()) {
            if (n instanceof VBox) {
                n.setVisible(collapsed);
                n.setManaged(collapsed);
            }
        }
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

    public void addTermClick(ActionEvent event) {
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

    public void addCourseClick(ActionEvent event) {
        Term targetTerm = hmTerm.get(((Node) event.getSource()).getParent().getParent().getId());
        Course targetCourse = new Course("", "", "");
        targetCourse.getStatistics().setMean(0.0f);
        targetCourse.getStatistics().setMedian(0.0f);
        targetCourse.getStatistics().setMode(0.0f);
        targetCourse.getStatistics().setStandardDev(0.0f);

        md.setSelectedTerm(targetTerm);
        md.addCourse(targetCourse);

        md.setSelectedCourse(targetCourse);

        try {
            sc.addScreen("CourseForm", FXMLLoader.load(getClass().getResource("../jfxml/CourseFormView.fxml")));
            sc.activate("CourseForm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteTermClick(ActionEvent event) {
        Node ndTerm = ((Node) event.getTarget()).getParent().getParent();

        ndTerm.setVisible(false);
        ndTerm.setManaged(false);

        Term t = hmTerm.get(ndTerm.getId());

        md.removeTerm(t);
        md.addRemovedObject(t);
    }

    private void deleteCourseClick(ActionEvent event) {
        Node ndCourse = ((Node) event.getTarget()).getParent();
        Node ndTerm = ndCourse.getParent().getParent();

        ndCourse.setVisible(false);
        ndCourse.setManaged(false);

        Term t = hmTerm.get(ndTerm.getId());
        Course c = hmCourse.get(ndCourse.getId());

        md.setSelectedTerm(t);

        md.removeCourse(c);
        md.addRemovedObject(c);
    }

    private void editTermClick(ActionEvent event) {
        Term targetTerm = hmTerm.get(((Node) event.getSource()).getParent().getParent().getId());

        md.setSelectedTerm(targetTerm);

        try {
            sc.addScreen("TermForm", FXMLLoader.load(getClass().getResource("../jfxml/TermFormView.fxml")));
            sc.activate("TermForm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editCourseClick(ActionEvent event) {
        Term targetTerm = hmTerm.get(((Node) event.getSource()).getParent().getParent().getParent().getId());
        Course targetCourse = hmCourse.get(((Node) event.getSource()).getParent().getId());

        md.setSelectedTerm(targetTerm);
        md.setSelectedCourse(targetCourse);

        try {
            sc.addScreen("CourseForm", FXMLLoader.load(getClass().getResource("../jfxml/CourseFormView.fxml")));
            sc.activate("CourseForm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void commitClick(ActionEvent e) {
        md.commitChanges();

        vbTerms.getChildren().clear();
        hmTerm.clear();
        hmCourse.clear();

        initialize();
    }

    public void revertClick(ActionEvent e) {
        md.revertChanges();

        vbTerms.getChildren().clear();
        hmTerm.clear();
        hmCourse.clear();

        initialize();
    }
}
