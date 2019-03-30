package com.cannapaceus.jfx;

import com.cannapaceus.grader.*;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

public class CourseController {
    ScreenController sc = null;
    Model md = null;

    Course selectedCourse;

    HashMap<String, Student> hmStudent;
    HashMap<String, Assignment> hmAssignment;
    HashMap<String, Category> hmCategory;

    @FXML
    private Label lblCourseName;

    @FXML
    private JFXButton btnAddStudent;

    @FXML
    private JFXButton btnAddAssignment;

    @FXML
    private JFXButton btnAddCategory;

    @FXML
    private JFXButton expandStudent;

    @FXML
    private JFXButton expandAssignment;

    @FXML
    private VBox vbCourse;

    @FXML
    private VBox vbStudents;

    @FXML
    private VBox vbAssignments;

    @FXML
    private VBox vbCats;

    @FXML
    private HBox hbStudents;

    @FXML
    private HBox hbAssignments;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        selectedCourse = md.getSelectedCourse();

        hmStudent = new HashMap<>();
        hmAssignment = new HashMap<>();
        hmCategory = new HashMap<>();

        lblCourseName.setText(selectedCourse.getCourseName());

        hbStudents.setOnMouseClicked((event) -> expandStudent.fire());
        hbAssignments.setOnMouseClicked((event) -> expandAssignment.fire());

        createStudentList();
        createAssignmentList();
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
        int numAssign = 0;

        for (Category cat : selectedCourse.getlCategories()) {
            Separator tempSep;

            VBox catAssignmentVB = new VBox();
            catAssignmentVB.setSpacing(10.0);
            catAssignmentVB.setFillWidth(true);
            catAssignmentVB.setStyle("-fx-background-color: white;");
            catAssignmentVB.setPadding(new Insets(10, 10, 10, 30));

            HBox catHB = new HBox();
            catHB.setAlignment(Pos.CENTER_LEFT);

            Label catLabel = new Label(cat.getName());
            catLabel.setAlignment(Pos.CENTER_LEFT);

            FontAwesomeIconView catFA = new FontAwesomeIconView();
            catFA.setGlyphName("EDIT");
            catFA.setGlyphSize(20);
            catFA.setGlyphStyle("-fx-fill: grey;");

            JFXButton catEdit = new JFXButton("");
            catEdit.setAlignment(Pos.BASELINE_CENTER);
            catEdit.setGraphic(catFA);
            catEdit.setStyle("-fx-cursor: hand;");
            catEdit.setRipplerFill(Color.WHITE);
            catEdit.setButtonType(JFXButton.ButtonType.FLAT);
            catEdit.setOnAction((event -> editCategoryClick(event)));

            Pane catSpanPane = new Pane();

            catFA = new FontAwesomeIconView();
            catFA.setGlyphName("TRASH_ALT");
            catFA.setGlyphSize(20);
            catFA.setGlyphStyle("-fx-fill: grey;");

            JFXButton catDelete = new JFXButton("");
            catDelete.setAlignment(Pos.BASELINE_CENTER);
            catDelete.setGraphic(catFA);
            catDelete.setStyle("-fx-cursor: hand;");
            catDelete.setRipplerFill(Color.WHITE);
            catDelete.setButtonType(JFXButton.ButtonType.FLAT);
            catDelete.setOnAction((event -> deleteCategoryClick(event)));

            catHB.getChildren().addAll(catLabel, catSpanPane, catEdit, catDelete);
            catHB.setHgrow(catSpanPane, Priority.ALWAYS);

            for (Assignment a : cat.getAssignments()) {

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

                tempSep = new Separator();

                catAssignmentVB.getChildren().addAll(tempSep, tempHB);
                ++numAssign;
            }

            FontAwesomeIconView catAddIcon = new FontAwesomeIconView();
            catAddIcon.setGlyphName("PLUS");
            catAddIcon.setGlyphSize(20);
            catAddIcon.setGlyphStyle("-fx-fill: white;");

            JFXButton catAddAssignment = new JFXButton("Add Assignment");
            catAddAssignment.setAlignment(Pos.BASELINE_CENTER);
            catAddAssignment.setGraphic(catAddIcon);
            catAddAssignment.setStyle("-fx-cursor: hand; -fx-background-color: #4CAF50; -fx-text-fill: white;");
            catAddAssignment.setButtonType(JFXButton.ButtonType.FLAT);
            catAddAssignment.setOnAction((event -> addAssignment(event)));

            tempSep = new Separator();

            catAssignmentVB.getChildren().addAll(tempSep, catAddAssignment);

            tempSep = new Separator();

            catHB.setId("" + cat.getDBID());
            hmCategory.put("" + cat.getDBID(), cat);

            vbAssignments.getChildren().addAll(tempSep, catHB, catAssignmentVB);
        }

        Separator tempSep;

        VBox catAssignmentVB = new VBox();
        catAssignmentVB.setSpacing(10.0);
        catAssignmentVB.setStyle("-fx-background-color: white;");
        catAssignmentVB.setPadding(new Insets(10, 10, 10, 30));

        HBox catHB = new HBox();
        catHB.setAlignment(Pos.CENTER_LEFT);

        Label catLabel = new Label("Uncategorized");
        catLabel.setAlignment(Pos.CENTER_LEFT);

        Pane catSpanPane = new Pane();

        catHB.getChildren().addAll(catLabel, catSpanPane);
        catHB.setHgrow(catSpanPane, Priority.ALWAYS);

        for (Assignment a : selectedCourse.getlAssignments()) {
            if (a.getCategoryCopy() != null) {
                continue;
            }

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

            tempSep = new Separator();

            catAssignmentVB.getChildren().addAll(tempSep, tempHB);
            ++numAssign;
        }

        tempSep = new Separator();

        FontAwesomeIconView catAddIcon = new FontAwesomeIconView();
        catAddIcon.setGlyphName("PLUS");
        catAddIcon.setGlyphSize(20);
        catAddIcon.setGlyphStyle("-fx-fill: white;");

        JFXButton catAddAssignment = new JFXButton("Add Assignment");
        catAddAssignment.setAlignment(Pos.BASELINE_CENTER);
        catAddAssignment.setGraphic(catAddIcon);
        catAddAssignment.setStyle("-fx-cursor: hand; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        catAddAssignment.setButtonType(JFXButton.ButtonType.FLAT);
        catAddAssignment.setOnAction((event -> addAssignment(event)));

        catAssignmentVB.getChildren().addAll(tempSep, catAddAssignment);

        tempSep = new Separator();

        vbAssignments.getChildren().addAll(tempSep, catHB, catAssignmentVB);
    }

    @FXML
    private void expand(ActionEvent e) {
        JFXButton t = (JFXButton) e.getTarget();

        HBox hbTemp = null;

        String s = "";

        switch (t.getId()) {
            case "expandStudent":
                s = "collapseStudent";
                btnAddStudent.setVisible(true);
                btnAddStudent.setManaged(true);
                hbTemp = hbStudents;
                break;
            case "expandAssignment":
                s = "collapseAssignment";
                btnAddCategory.setVisible(true);
                btnAddCategory.setManaged(true);
                hbTemp = hbAssignments;
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

        if (hbTemp != null)
            hbTemp.setOnMouseClicked(event -> tempCollapse.fire());

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

        HBox hbTemp = null;

        String s = "";

        switch (t.getId()) {
            case "collapseStudent":
                s = "expandStudent";
                btnAddStudent.setVisible(false);
                btnAddStudent.setManaged(false);
                hbTemp = hbStudents;
                break;
            case "collapseAssignment":
                s = "expandAssignment";
                btnAddCategory.setVisible(false);
                btnAddCategory.setManaged(false);
                hbTemp = hbAssignments;
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

        if (hbTemp != null)
            hbTemp.setOnMouseClicked(event -> tempExpand.fire());

        for (Node n : vBox.getChildren()) {
            if (n instanceof VBox) {
                n.setVisible(false);
                n.setManaged(false);
            }
        }

        p.getChildren().add(tempExpand);
    }

    private void deleteCategoryClick(ActionEvent event) {
        Node ndCat = ((Node) event.getTarget()).getParent();

        ndCat.setVisible(false);
        ndCat.setManaged(false);

        Category cat = hmCategory.get(ndCat.getId());

        md.removeCategory(cat);
        md.addRemovedObject(cat);

        for (Assignment a : cat.getAssignments()) {
            a.setCategory(null);
        }

        vbStudents.getChildren().clear();
        vbAssignments.getChildren().clear();

        hmStudent.clear();
        hmAssignment.clear();
        hmCategory.clear();

        initialize();
    }

    private void editCategoryClick(ActionEvent event) {
        Category targetCategory = hmCategory.get(((Node) event.getSource()).getParent().getId());

        md.setSelectedCategory(targetCategory);

        try {
            sc.addScreen("CategoryForm", FXMLLoader.load(getClass().getResource("../jfxml/CategoryFormView.fxml")));
            sc.activate("CategoryForm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addStudent(ActionEvent event) {
        Student s = new Student("", "", "", "");

        md.addStudent(s);
        md.setSelectedStudent(s);

        try {
            sc.addScreen("StudentForm", FXMLLoader.load(getClass().getResource("../jfxml/StudentFormView.fxml")));
            sc.activate("StudentForm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addAssignment(ActionEvent event)
    {
        LocalDate lo = LocalDate.now();
        Category catUnassigned = new Category("Unassigned",100);
        Assignment a = new Assignment("",lo,lo,false,100,catUnassigned,0);

        md.addAssignment(a);
        md.setSelectedAssignment(a);

        try {
            sc.addScreen("AssignmentForm", FXMLLoader.load(getClass().getResource("../jfxml/AssignmentFormView.fxml")));
            sc.activate("AssignmentForm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void commitClick(ActionEvent e) {
        md.commitChanges();
    }

    public void revertClick(ActionEvent e) {
        md.revertChanges();

        vbStudents.getChildren().clear();
        hmStudent.clear();

        vbAssignments.getChildren().clear();
        hmAssignment.clear();

        initialize();
    }

    public void openGradeBook(ActionEvent event) {
        try {
            sc.addScreen("GradeBook", FXMLLoader.load(getClass().getResource("../jfxml/GradeBookView.fxml")));
            sc.activate("GradeBook");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
