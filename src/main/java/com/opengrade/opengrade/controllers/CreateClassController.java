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

    @FXML
    protected void handleAddStudentButton() {
        TextInputDialog askStudentName = new TextInputDialog();
        askStudentName.setTitle("Create student");
        askStudentName.setHeaderText("Create student");
        askStudentName.setContentText("What is the student's name?");

        Optional<String> result = askStudentName.showAndWait();
        if (result.isPresent()) {
            students.add(new Student(result.get()));
            Label l = new Label(result.get());
            l.getStyleClass().add("p");
            showStudents.getChildren().add(l);
        }
    }

    /**
     * Get class information, and handles class creation on button click
     */
    @FXML
    protected void handleCreateClassButton() throws IOException {
        String className = classNameInput.getText();

        Class c = new Class(className);
        for (Student student : students) {
            c.addStudent(student);
        }

        Database.insertClass(c);
    }

}