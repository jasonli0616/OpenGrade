package com.opengrade.opengrade.controllers;

import com.opengrade.opengrade.Database;
import com.opengrade.opengrade.models.Class;
import com.opengrade.opengrade.models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class CreateClassController {
    @FXML
    private TextField classNameInput;

    @FXML
    private VBox showStudents;

    private ArrayList<Student> students = new ArrayList<Student>();

    /**
     * Method called when the "Add student" button is clicked.
     * Handles student addition to class
     */
    @FXML
    protected void handleAddStudentButton() {
        // Ask existing/new student
        ChoiceDialog<String> studentTypeChoice = new ChoiceDialog<String>();
        studentTypeChoice.setTitle("Add student");
        studentTypeChoice.setHeaderText("Add student");
        studentTypeChoice.setContentText("Please select the student type:");
        studentTypeChoice.getItems().addAll("Select existing student", "Create new student");
        Optional<String> studentTypeChoiceResult = studentTypeChoice.showAndWait();

        if (studentTypeChoiceResult.isPresent()) {
            if (studentTypeChoiceResult.get().equals("Select existing student")) {
                // TODO: Search database for existing students

            } else {
                // Insert new student to database, and add to class

                // Show dialog to ask for name
                TextInputDialog askStudentName = new TextInputDialog();
                askStudentName.setTitle("Create student");
                askStudentName.setHeaderText("Create student");
                askStudentName.setContentText("What is the student's name?");

                // If name is given, create student and add to ArrayList
                Optional<String> result = askStudentName.showAndWait();
                if (result.isPresent()) {
                    // TODO: Add to database
                    // TODO: Add to class
                }
            }
        }
    }

    /**
     * Method called when class creation button is clicked.
     * Get class information, and inserts class into database.
     *
     * @throws IOException
     */
    @FXML
    protected void handleCreateClassButton() throws IOException {
        String className = classNameInput.getText();
        if (className.isBlank()) {
            // TODO: Create error
        } else {
            // TODO: Insert class to database
            // TODO: Open class window
        }
    }

}