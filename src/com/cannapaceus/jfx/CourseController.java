package com.cannapaceus.jfx;

import com.cannapaceus.grader.*;
import com.cannapaceus.services.EmailService;
import com.cannapaceus.services.PDFService;
import com.cannapaceus.services.PrinterService;
import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.HashMap;

public class CourseController {
    ScreenController sc = null;
    Model md = null;

    Course selectedCourse;

    HashMap<String, Student> hmStudent;
    HashMap<String, Assignment> hmAssignment;
    HashMap<String, Category> hmCategory;
    HashMap<String, Float> hmStatistics;

    @FXML
    private StackPane spDialogPane;

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
    private JFXButton expandStatistics;

    @FXML
    private VBox vbCourse;

    @FXML
    private VBox vbStudents;

    @FXML
    private VBox vbAssignments;

    @FXML
    private VBox vbCats;

    @FXML
    private VBox vbStats;

    @FXML
    private HBox hbStudents;

    @FXML
    private HBox hbAssignments;

    @FXML
    private HBox hbStatistics;

    @FXML
    private VBox vbOptionsMenuPane;

    @FXML
    private JFXButton btnOpenOptionsMenu;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        md = Model.getInstance();

        selectedCourse = md.getSelectedCourse();

        hmStudent = new HashMap<>();
        hmAssignment = new HashMap<>();
        hmCategory = new HashMap<>();
        hmStatistics = new HashMap<>();

        hmCategory.put("0", null);

        lblCourseName.setText(selectedCourse.getCourseName());

        hbStudents.setOnMouseClicked((event) -> expandStudent.fire());
        hbStatistics.setOnMouseClicked((event) -> expandStatistics.fire());
        hbAssignments.setOnMouseClicked((event) -> expandAssignment.fire());

        createStudentList();
        if (!selectedCourse.getlStudents().isEmpty()) {
            createStatisticsList();
        }
        createAssignmentList();
    }

    public void backClick(ActionEvent actionEvent) {
        md.setSelectedCourse(null);
        md.setSelectedTerm(null);

        try {
            sc.addScreen("Terms", FXMLLoader.load(getClass().getResource("../jfxml/TermsView.fxml")));
            sc.activate("Terms");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            sc.addScreen("Assignment", FXMLLoader.load(getClass().getResource("../jfxml/AssignmentView.fxml")));
            sc.activate("Assignment");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createStudentList() {
        int numStu = 0;

        for (Student s : selectedCourse.getlStudents()) {
            Separator tempSep = new Separator();

            HBox tempHB = new HBox();
            tempHB.setSpacing(10.0);
            tempHB.setAlignment(Pos.CENTER_LEFT);
            if (md.isChanged(s)) {
                tempHB.setStyle("-fx-border-color: #4CAF50;\n" +
                        "    -fx-border-insets: 5;\n" +
                        "    -fx-border-width: 3;\n");
            }

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

            tempHB.setId("" + numStu);
            hmStudent.put("" + numStu, s);

            ++numStu;

            vbStudents.getChildren().addAll(tempSep, tempHB);
        }
    }

    private void createAssignmentList() {
        int numCat = 0;
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
            if (md.isChanged(cat)) {
                catHB.setStyle("-fx-border-color: #4CAF50;\n" +
                        "    -fx-border-insets: 5;\n" +
                        "    -fx-border-width: 3;\n");
            }

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
                if (md.isChanged(a)) {
                    tempHB.setStyle("-fx-border-color: #4CAF50;\n" +
                            "    -fx-border-insets: 5;\n" +
                            "    -fx-border-width: 3;\n");
                }

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

                tempHB.setId("" + numAssign);
                hmAssignment.put("" + numAssign, a);

                ++numAssign;

                tempSep = new Separator();

                catAssignmentVB.getChildren().addAll(tempSep, tempHB);
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

            catAssignmentVB.setId("" + numCat);
            catHB.setId("" + numCat);
            hmCategory.put("" + numCat, cat);

            ++numCat;

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
            if (md.isChanged(a)) {
                tempHB.setStyle("-fx-border-color: #4CAF50;\n" +
                        "    -fx-border-insets: 5;\n" +
                        "    -fx-border-width: 3;\n");
            }

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

        catAssignmentVB.setId("0");

        tempSep = new Separator();

        vbAssignments.getChildren().addAll(tempSep, catHB, catAssignmentVB);
    }

    private void createStatisticsList() {
        for (Student s : selectedCourse.getlStudents()) {
            s.setAverageGrade(s.getGrades());
        }
        selectedCourse.PopulateAverages(selectedCourse.getlStudents());
        selectedCourse.calculateStats();
        Statistics st = selectedCourse.getStatistics();

        Separator tempSep = new Separator();

        HBox tempHB = new HBox();
        tempHB.setSpacing(10.0);
        tempHB.setAlignment(Pos.CENTER);

        Pane tempPane = new Pane();

        Label meanLabel = new Label("Mean: " + String.format("%.1f", st.getMean()));
        meanLabel.setAlignment(Pos.CENTER_LEFT);
        Label medianLabel = new Label("Median: " + String.format("%.1f", st.getMedian()));
        medianLabel.setAlignment(Pos.CENTER_LEFT);
        Label modeLabel = new Label("Mode: " + String.format("%.1f", st.getMode()));
        modeLabel.setAlignment(Pos.CENTER_LEFT);
        Label stddevLabel = new Label("Standard Deviation: " + String.format("%.2f", st.getStandardDev()));
        stddevLabel.setAlignment(Pos.CENTER_LEFT);

        tempHB.getChildren().addAll(meanLabel, medianLabel, modeLabel, stddevLabel);
        tempHB.setHgrow(tempPane, Priority.ALWAYS);

        tempHB.setId("" + st.getDBID());
        hmStatistics.put("" + st.getDBID(), st.getMean());
        hmStatistics.put("" + st.getDBID(), st.getMedian());
        hmStatistics.put("" + st.getDBID(), st.getMode());
        hmStatistics.put("" + st.getDBID(), st.getStandardDev());

        vbStats.getChildren().addAll(tempSep, tempHB);
    }

    @FXML
    private void expand(ActionEvent e) {
        JFXButton t = (JFXButton) e.getTarget();

        boolean newVal = false;

        switch (t.getId()) {
            case "expandStudent":
                newVal = !btnAddStudent.isVisible();
                btnAddStudent.setVisible(newVal);
                btnAddStudent.setManaged(newVal);
                break;
            case "expandAssignment":
                newVal = !btnAddCategory.isVisible();
                btnAddCategory.setVisible(newVal);
                btnAddCategory.setManaged(newVal);
                break;
            case "expandStatistics":
                newVal = !vbStats.isVisible();
                break;
        }

        HBox p = (HBox) t.getParent();

        VBox vbTerm = (VBox) p.getParent();

        FontAwesomeIconView tempFA = new FontAwesomeIconView();
        tempFA.setGlyphName("CHEVRON_DOWN");
        tempFA.setGlyphSize(20);
        tempFA.setGlyphStyle("-fx-fill: grey;");

        if (newVal) {
            tempFA.setGlyphName("CHEVRON_DOWN");
        } else {
            tempFA.setGlyphName("CHEVRON_RIGHT");
        }



        t.setGraphic(tempFA);

        for (Node n : vbTerm.getChildren()) {
            if (n instanceof VBox) {
                n.setVisible(newVal);
                n.setManaged(newVal);
            }
        }
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
    private void addCategory(ActionEvent event) {
        Category c = new Category("",1.0f);

        md.addCategory(c);
        md.setSelectedCategory(c);

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

        String id = ((Node) event.getTarget()).getParent().getId();
        Category cat = hmCategory.get(id);

        Assignment a = new Assignment("",lo,lo,false,100,cat,0);

        md.setSelectedCategory(cat);
        md.setSelectedAssignment(a);
        md.addAssignment(a);

        try {
            sc.addScreen("AssignmentForm", FXMLLoader.load(getClass().getResource("../jfxml/AssignmentFormView.fxml")));
            sc.activate("AssignmentForm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void commitClick(ActionEvent e) {
        md.commitChanges();
        reloadView();
    }

    public void revertClick(ActionEvent e) {
        md.revertChanges();
        reloadView();
    }

    private void reloadView() {
        vbStudents.getChildren().clear();
        hmStudent.clear();

        vbAssignments.getChildren().clear();
        hmAssignment.clear();

        vbStats.getChildren().clear();
        hmStatistics.clear();

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

    public void operateMenu(ActionEvent event) {
        if(vbOptionsMenuPane.isVisible()) {
            vbOptionsMenuPane.setManaged(false);
            vbOptionsMenuPane.setVisible(false);

            FontAwesomeIconView tempFA = new FontAwesomeIconView();
            tempFA.setGlyphName("BARS"); //ELLIPSIS_V
            tempFA.setGlyphSize(30);
            tempFA.setGlyphStyle("-fx-fill: grey;");
            btnOpenOptionsMenu.setGraphic(tempFA);
        }

        else {
            vbOptionsMenuPane.setManaged(true);
            vbOptionsMenuPane.setVisible(true);

            FontAwesomeIconView tempFA = new FontAwesomeIconView();
            tempFA.setGlyphName("TIMES_CIRCLE");
            tempFA.setGlyphSize(30);
            tempFA.setGlyphStyle("-fx-fill: grey;");
            btnOpenOptionsMenu.setGraphic(tempFA);
        }
    }

    public void printList(ActionEvent event) {
        PDFService service = PDFService.getInstance();
        service.printList(selectedCourse);
    }

    public void printGrades(ActionEvent event) {
        PDFService service = PDFService.getInstance();
        service.printGrades(selectedCourse);
    }

    public void emailGrades(ActionEvent event) {
        //TODO: Implement database retrieval of instructor email and make sure email is set in settings before sending.'
        selectedCourse.PopulateAverages(selectedCourse.getlStudents());
        selectedCourse.calculateStats();
        selectedCourse.scaleFinalAverages(selectedCourse.getScale());

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Sending Emails..."));
        content.setBody(new Text("Emails are being sent to all students in " + selectedCourse.getCourseName()));

        JFXDialog dialog = new JFXDialog(spDialogPane, content, JFXDialog.DialogTransition.CENTER);

        dialog.show();

        EmailService emailService = EmailService.getInstance("service.cannapaceus@gmail.com", "bIQ!9C13!6JC#rlA5Dqy");
        boolean noice = emailService.email(selectedCourse);

        dialog.close();
    }
}
