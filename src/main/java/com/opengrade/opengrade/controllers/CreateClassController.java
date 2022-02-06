package com.opengrade.opengrade.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.function.UnaryOperator;

public class CreateClassController {
    @FXML
    private TextField classNameInput;

    @FXML
    private TextField amountOfStudentsInput;

    /**
     * Get class information, and handles class creation on button click
     */
    @FXML
    protected void handleCreateClassButton() throws IOException {
        String className = classNameInput.getText();
        int amountOfStudents = Integer.parseInt(amountOfStudentsInput.getText());
        // TODO: Handle class creation
    }

    /**
     * Enforces integer input for amount of students
     * Shows error alert and clears input if not integer
     */
    @FXML
    protected void validateAmountOfStudentsNumberInput() {
        try {
            int amountOfStudents = Integer.parseInt(amountOfStudentsInput.getText());
        } catch (NumberFormatException exception) {
            if (!amountOfStudentsInput.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter an integer.");
                alert.show();
                amountOfStudentsInput.setText("");
            }
        }
    }
}