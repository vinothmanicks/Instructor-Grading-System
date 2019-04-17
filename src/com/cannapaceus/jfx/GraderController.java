package com.cannapaceus.jfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import org.apache.pdfbox.io.IOUtils;
import org.h2.store.fs.FileUtils;
import sun.nio.ch.IOUtil;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
            sc.addScreen("Terms", FXMLLoader.load(getClass().getResource("/TermsView.fxml")));
            sc.activate("Terms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void homeClick(ActionEvent event) {
        try {
            sc.addScreen("Terms", FXMLLoader.load(getClass().getResource("/TermsView.fxml")));
            sc.activate("Terms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void qBankClick(ActionEvent event) {
        try {
            sc.addScreen("QBank", FXMLLoader.load(getClass().getResource("/QBankView.fxml")));
            sc.activate("QBank");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void settingsClick(ActionEvent event) {
        try {
            sc.addScreen("Settings", FXMLLoader.load(getClass().getResource("/SettingsView.fxml")));
            sc.activate("Settings");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void helpClick(ActionEvent event) throws IOException {
        URL url = getClass().getResource("/help.html");
        InputStream in = url.openStream();
        File temp = File.createTempFile("help", ".html");
        temp.deleteOnExit();

        FileOutputStream out = new FileOutputStream(temp);
        IOUtils.copy(in, out);

        in.close();
        out.close();

        Desktop.getDesktop().browse(temp.toURI());
    }
}
