package com.opengrade.opengrade.controllers;

import com.opengrade.opengrade.Database;
import com.opengrade.opengrade.models.Class;
import com.opengrade.opengrade.models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;

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
        // Show dialog to ask for name
        TextInputDialog askStudentName = new TextInputDialog();
        askStudentName.setTitle("Create student");
        askStudentName.setHeaderText("Create student");
        askStudentName.setContentText("What is the student's name?");

        // If name is given, create student and add to ArrayList
        Optional<String> result = askStudentName.showAndWait();
        if (result.isPresent()) {
            students.add(new Student(result.get()));
            Label l = new Label(result.get());
            l.getStyleClass().add("p");
            showStudents.getChildren().add(l);
        }
    }

    /**
     * Method called when class creation button is clicked.
     * Get class information, and inserts class into database.
     */
    @FXML
    protected void handleCreateClassButton() throws IOException {
        String className = classNameInput.getText();

        Class c = new Class(className, students);

        Database.insertClass(c);
    }

    // TODO: Open class in window

}