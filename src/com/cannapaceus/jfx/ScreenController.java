package com.cannapaceus.jfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class ScreenController {
    private static ScreenController instance = null;

    private HashMap<String, Pane> screenMap = new HashMap<>();
    private Scene scMain;
    private BorderPane pnMain;

    public static ScreenController getInstance() {
        if (instance == null) {
            instance = new ScreenController();
        }

        return instance;
    }

    private ScreenController() { }

    public void setScene(Scene sc) {
        this.scMain = sc;
    }

    public void setRoot(Pane pn) {
        this.scMain.setRoot(pn);
    }

    public void setPane(BorderPane pn) {
        this.pnMain= pn;
    }

    protected void addScreen(String name, Pane pane){
        screenMap.put(name, pane);
    }

    protected void removeScreen(String name){
        screenMap.remove(name);
    }

    protected void activate(String name){
        Pane pane = screenMap.get(name);

        pnMain.setCenter(pane);
    }

    public void setBottomVisibility(boolean b) {
        pnMain.getBottom().setVisible(b);
        pnMain.getBottom().setManaged(b);
    }
}
