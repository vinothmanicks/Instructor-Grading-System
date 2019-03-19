package com.cannapaceus.jfx;

import com.jfoenix.controls.JFXDrawer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class GraderController {
    ScreenController sc = null;

    @FXML
    private Pane pnContent;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        sc.setPane(pnContent);

        try {
            sc.addScreen("Terms", FXMLLoader.load(getClass().getResource("../jfxml/TermsView.fxml")));
            sc.addScreen("Course", FXMLLoader.load(getClass().getResource("../jfxml/CourseView.fxml")));
            sc.activate("Terms");
        } catch (Exception e) {

        }
    }

}
