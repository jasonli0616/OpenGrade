package com.opengrade.opengrade.controllers;

import com.opengrade.opengrade.Database;
import com.opengrade.opengrade.models.Class;
import com.opengrade.opengrade.models.Student;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class CreateClassController {
    @FXML
    private TextField classNameInput;

    @FXML
    private ListView<Student> addedStudentsList;

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

                if (Database.getAllStudents().isEmpty()) {
                    new Alert(Alert.AlertType.ERROR, "There are no existing students.").showAndWait();
                    this.handleAddStudentButton();
                } else {
                    ChoiceDialog<Student> chooseStudentDialog = new ChoiceDialog<Student>();
                    chooseStudentDialog.setTitle("Select a student");
                    chooseStudentDialog.setHeaderText("Select a student");
                    chooseStudentDialog.setContentText("Please select a student:");

                    for (Student student : Database.getAllStudents()) {
                        chooseStudentDialog.getItems().add(student);
                    }

                    Optional<Student> chooseStudentResult = chooseStudentDialog.showAndWait();

                    if (chooseStudentResult.isPresent()) {
                        Student chosenStudent = chooseStudentResult.get();
                        if (this.addedStudents.contains(chosenStudent))
                            new Alert(Alert.AlertType.ERROR, String.format("Student %s already exists in this class.", chosenStudent)).showAndWait();
                        else
                            this.addedStudents.add(chosenStudent);
                    }
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
            this.addedStudentsList.getItems().setAll(this.addedStudents);
        }
    }

    /**
     * Remove a selected student from the class.
     */
    @FXML
    protected void removeSelectedStudent() {
        ObservableList<Student> selectedStudents = this.addedStudentsList.getSelectionModel().getSelectedItems();
        if (selectedStudents.size() != 1) {
            new Alert(Alert.AlertType.ERROR, "Please select exactly one student.").showAndWait();
        } else {
            Student selectedStudent = selectedStudents.get(0);
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, String.format("Are you sure you want to remove %s from the class?", selectedStudent.fullName));
            Optional<ButtonType> confirmationResult = confirmation.showAndWait();
            if (confirmationResult.isPresent() && confirmationResult.get() == ButtonType.OK) {
                this.addedStudents.remove(selectedStudent);
                this.addedStudentsList.getItems().setAll(this.addedStudents);
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

            Class.openClassGUI(c, (Stage) classNameInput.getScene().getWindow());
        }
    }

}