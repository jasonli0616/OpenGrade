package com.opengrade.opengrade.controllers;

import com.opengrade.opengrade.Database;
import com.opengrade.opengrade.models.Class;
import com.opengrade.opengrade.models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class CreateClassController {
    @FXML
    private TextField classNameInput;

    @FXML
    private VBox showStudents;

    private ArrayList<Student> addedStudents = new ArrayList<Student>();

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
                ChoiceDialog<Student> chooseStudentDialog = new ChoiceDialog<Student>();
                chooseStudentDialog.setTitle("Select a student");
                chooseStudentDialog.setHeaderText("Select a student");
                chooseStudentDialog.setContentText("Please select a student:");

                for (Student student : Database.getAllStudents()) {
                    chooseStudentDialog.getItems().add(student);
                }

                Optional<Student> chooseStudentResult = chooseStudentDialog.showAndWait();

                if (chooseStudentResult.isPresent()) {
                    this.addedStudents.add(chooseStudentResult.get());
                }

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
                    Student student = new Student(result.get());
                    student.id = Database.insertStudent(student);

                    this.addedStudents.add(student);
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
            new Alert(Alert.AlertType.ERROR, "No name chosen for class.").showAndWait();
        } else {
            Class c = new Class(className, addedStudents);
            Database.insertClass(c);
            // TODO: Open class window
        }
    }

}