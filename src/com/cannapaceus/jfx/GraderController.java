package com.cannapaceus.jfx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class GraderController {
    ScreenController sc = null;

    @FXML
    private BorderPane bpGraderView;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        sc.setPane(bpGraderView);

        try {
            sc.addScreen("Terms", FXMLLoader.load(getClass().getResource("../jfxml/TermsView.fxml")));
            sc.addScreen("Course", FXMLLoader.load(getClass().getResource("../jfxml/CourseView.fxml")));
            sc.activate("Terms");
        } catch (Exception e) {

        }
    }

}
