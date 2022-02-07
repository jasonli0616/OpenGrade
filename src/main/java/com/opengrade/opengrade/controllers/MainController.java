package com.opengrade.opengrade.controllers;

import com.opengrade.opengrade.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private Button createNewClassButton;

    @FXML
    private Button openExistingClassButton;

    /**
     * Change to create class window on button click.
     */
    @FXML
    protected void handleCreateNewClassButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/create-class.fxml"));
        Stage stage = (Stage) createNewClassButton.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("OpenGrade - Create a new class");
        stage.setScene(scene);
    }

    /**
     * Change to open existing class window on button click.
     */
    @FXML
    protected void handleOpenExistingClassButton() throws IOException {
    }
}