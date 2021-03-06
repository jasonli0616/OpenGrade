package com.opengrade.opengrade.controllers;

import com.opengrade.opengrade.Database;
import com.opengrade.opengrade.Main;
import com.opengrade.opengrade.models.Class;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
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

        ArrayList<Class> classes = Database.getAllClasses();

        if (classes.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "There are no existing classes.").showAndWait();
        } else {
            ChoiceDialog<Class> choiceDialog = new ChoiceDialog<Class>();
            choiceDialog.setTitle("Select class");
            choiceDialog.setHeaderText("Select class");
            choiceDialog.setContentText("Please select a class:");
            choiceDialog.getItems().setAll(classes);

            Optional<Class> result = choiceDialog.showAndWait();
            if (result.isPresent()) {
                Class.openClassGUI(result.get(), (Stage) openExistingClassButton.getScene().getWindow());
            }
        }
    }
}