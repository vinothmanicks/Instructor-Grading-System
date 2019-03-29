package com.cannapaceus.jfx;

import com.cannapaceus.grader.*;
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
import java.util.HashMap;

public class CourseController {
    ScreenController sc = null;
    Model md = null;

    Course selectedCourse;

    HashMap<String, Student> hmStudent;
    HashMap<String, Assignment> hmAssignment;

    @FXML
    private Label lblCourseName;

    @FXML
    private JFXButton btnAddStudent;

    @FXML
    private JFXButton btnAddAssignment;

    @FXML
    private JFXButton btnAddGrade;

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

        sc.setBottomVisibility(true);

        selectedCourse = md.getSelectedCourse();

        hmStudent = new HashMap<>();
        hmAssignment = new HashMap<>();

        lblCourseName.setText(selectedCourse.getCourseName());

        createStudentList();
        createAssignmentList();
        createGradeList();
    }

    public void backClick(ActionEvent actionEvent) {
        sc.activate("Terms");
    }

    private void studentClick(MouseEvent event) {
        md.setSelectedStudent(hmStudent.get(((Node) event.getSource()).getId()));

        try {
            sc.addScreen("Student", FXMLLoader.load(getClass().getResource("../jfxml/StudentView.fxml")));
            sc.activate("Student");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void assignmentClick(MouseEvent event) {
        md.setSelectedAssignment(hmAssignment.get(((Node) event.getSource()).getId()));

        try {
            sc.addScreen("Student", FXMLLoader.load(getClass().getResource("../jfxml/AssignmentView.fxml")));
            sc.activate("Student");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gradeClick(MouseEvent event) {
        //Does nothing at the moment
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

            tempHB.setOnMouseClicked((event -> studentClick(event)));

            tempHB.setId("" + s.getDBID());
            hmStudent.put("" + s.getDBID(), s);

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

            tempHB.setOnMouseClicked((event -> assignmentClick(event)));

            tempHB.setId("" + a.getDBID());
            hmAssignment.put("" + a.getDBID(), a);

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

            tempHB.setOnMouseClicked((event -> gradeClick(event)));

            vbGrades.getChildren().addAll(tempSep, tempHB);
        }
    }

    @FXML
    private void expand(ActionEvent e) {
        JFXButton t = (JFXButton) e.getTarget();

        String s = "";

        switch (t.getId()) {
            case "expandStudent":
                s = "collapseStudent";
                btnAddStudent.setVisible(true);
                btnAddStudent.setManaged(true);
                break;
            case "expandAssignment":
                s = "collapseAssignment";
                btnAddAssignment.setVisible(true);
                btnAddAssignment.setManaged(true);
                break;
            case "expandGrade":
                s = "collapseGrade";
                btnAddGrade.setVisible(true);
                btnAddGrade.setManaged(true);
                break;
        }

        HBox p = (HBox) t.getParent();

        VBox vbTerm = (VBox) p.getParent();

        p.getChildren().remove(t);

        FontAwesomeIconView tempFA = new FontAwesomeIconView();
        tempFA.setGlyphName("CHEVRON_DOWN");
        tempFA.setGlyphSize(20);
        tempFA.setGlyphStyle("-fx-fill: grey;");

        JFXButton tempCollapse = new JFXButton("");
        tempCollapse.setId(s);
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

    @FXML
    private void collapse(ActionEvent e) {
        JFXButton t = (JFXButton) e.getTarget();

        String s = "";

        switch (t.getId()) {
            case "collapseStudent":
                s = "expandStudent";
                btnAddStudent.setVisible(false);
                btnAddStudent.setManaged(false);
                break;
            case "collapseAssignment":
                s = "expandAssignment";
                btnAddAssignment.setVisible(false);
                btnAddAssignment.setManaged(false);
                break;
            case "collapseGrade":
                s = "expandGrade";
                btnAddGrade.setVisible(false);
                btnAddGrade.setManaged(false);
                break;
        }

        HBox p = (HBox) t.getParent();

        VBox vBox = (VBox) p.getParent();

        p.getChildren().remove(t);

        FontAwesomeIconView tempFA = new FontAwesomeIconView();
        tempFA.setGlyphName("CHEVRON_RIGHT");
        tempFA.setGlyphSize(20);
        tempFA.setGlyphStyle("-fx-fill: grey;");

        JFXButton tempExpand = new JFXButton("");
        tempExpand.setId(s);
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

    @FXML
    private void addStudent(ActionEvent event)
    {
        Student s = new Student("","","","");

        md.addStudent(s);
        md.setSelectedStudent(s);

        try {
            sc.addScreen("StudentForm", FXMLLoader.load(getClass().getResource("../jfxml/StudentFormView.fxml")));
            sc.activate("StudentForm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
