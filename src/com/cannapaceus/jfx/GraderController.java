package com.cannapaceus.jfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class GraderController {
    ScreenController sc = null;
    Model md = null;

    @FXML
    private BorderPane bpGraderView;

    @FXML
    private void initialize()
    {
        sc = ScreenController.getInstance();
        md = md.getInstance();

        sc.setPane(bpGraderView);

        try {
            sc.addScreen("Terms", FXMLLoader.load(getClass().getResource("../jfxml/TermsView.fxml")));
            sc.activate("Terms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void commitClick(ActionEvent e) {
        md.commitChanges();
    }

    public void revertClick(ActionEvent e) {
        md.revertChanges();
    }

}
