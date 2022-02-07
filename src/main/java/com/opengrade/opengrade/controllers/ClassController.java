package com.opengrade.opengrade.controllers;

import com.opengrade.opengrade.Database;
import com.opengrade.opengrade.models.Class;
import com.opengrade.opengrade.models.Student;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Optional;

public class ClassController {
    private Class c;

    @FXML
    private VBox studentsSection;

    @FXML
    private ListView<String> studentsList;

    /**
     * Show the list of students.
     */
    private void showStudentsList() {
        ArrayList<Student> students = this.c.students;
        if (!students.isEmpty()) {
            for (Student student : students) {
                // Add student to ListView
                studentsList.getItems().add(student.fullName);
            }
        }
    }

    /**
     * Change the name of a student in the class.
     */
    public void editStudentName() {
        try {
            Student selectedStudent = this.getOneSelectedStudent();
            // Show dialog to ask for new name
            TextInputDialog askStudentName = new TextInputDialog();
            askStudentName.setTitle("Edit student");
            askStudentName.setHeaderText("Edit student");
            askStudentName.setContentText("What is the student's new name?");

            // If name is given, change name
            Optional<String> result = askStudentName.showAndWait();
            if (result.isPresent()) {
                assert selectedStudent != null;
                this.c.editStudentName(selectedStudent, result.get());
                this.refreshWindow();
            }
        } catch (InvalidParameterException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
        }
    }

    /**
     * Add a new student to the class.
     */
    public void createStudent() {
        // Show dialog to ask for name
        TextInputDialog askStudentName = new TextInputDialog();
        askStudentName.setTitle("Create student");
        askStudentName.setHeaderText("Create student");
        askStudentName.setContentText("What is the student's name?");

        // If name is given, create student
        Optional<String> result = askStudentName.showAndWait();
        if (result.isPresent()) {
            Student student = new Student(result.get());
            try {
                c.addStudent(student);
            } catch (IllegalArgumentException exception) {
                // If student already exists
                new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            }
        }

        this.refreshWindow();
    }

    /**
     * Checks and returns the selected student.
     *
     * @return the selected student
     * @throws InvalidParameterException too many students selected
     */
    private Student getOneSelectedStudent() throws InvalidParameterException {
        // Get selected student
        ObservableList<String> selectedStudents = studentsList.getSelectionModel().getSelectedItems();
        if (selectedStudents.size() != 1) {
            throw new InvalidParameterException("Please select at most one student.");
        } else {
            String selectedStudentName = selectedStudents.get(0);
            for (Student student : this.c.students) {
                if (student.fullName.equals(selectedStudentName)) {
                    return student;
                }
            }
        }
        return null;
    }

    /**
     * Reload the class window.
     */
    public void refreshWindow() {
        try {
            Class.openClassGUI(c, (Stage) studentsList.getScene().getWindow());
        } catch (IOException exception) { exception.printStackTrace(); }
    }

    public void setClass(Class c) {
        this.c = c;

        this.showStudentsList();
    }
}