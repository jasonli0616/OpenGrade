package com.opengrade.opengrade.controllers;

import com.opengrade.opengrade.Database;
import com.opengrade.opengrade.Main;
import com.opengrade.opengrade.models.Class;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class MainController {
    @FXML
    private Button createNewClassButton;

    @FXML
    private Button openExistingClassButton;

    /**
     * Change to create class window on button click.
     *
     * @throws IOException
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
     *
     * @throws IOException
     */
    @FXML
    protected void handleOpenExistingClassButton() throws IOException {

        ChoiceDialog<String> choiceDialog = new ChoiceDialog<String>();
        choiceDialog.setTitle("Select class");
        choiceDialog.setHeaderText("Select class");
        choiceDialog.setContentText("Please select a class:");

        // TODO: Search database for classes, and insert to choice dialog
        // TODO: Handle user selection, and open class page
    }
}