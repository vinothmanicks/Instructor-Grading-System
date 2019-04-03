package com.cannapaceus.jfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.awt.*;
import java.io.File;
import java.io.IOException;

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

    public void homeClick(ActionEvent event) {
        try {
            sc.addScreen("Terms", FXMLLoader.load(getClass().getResource("../jfxml/TermsView.fxml")));
            sc.activate("Terms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void qBankClick(ActionEvent event) {
        try {
            sc.addScreen("QBank", FXMLLoader.load(getClass().getResource("../jfxml/QBankView.fxml")));
            sc.activate("QBank");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void settingsClick(ActionEvent event) {
        try {
            sc.addScreen("Settings", FXMLLoader.load(getClass().getResource("../jfxml/SettingsView.fxml")));
            sc.activate("Settings");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void helpClick(ActionEvent event) throws IOException {
        File file = new File("res/help.html");
        Desktop.getDesktop().browse(file.toURI());
    }
}
