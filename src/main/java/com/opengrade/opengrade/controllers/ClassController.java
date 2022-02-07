package com.opengrade.opengrade.controllers;

import com.opengrade.opengrade.Database;
import com.opengrade.opengrade.models.Class;
import com.opengrade.opengrade.models.Student;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class ClassController {
    Class c;

    @FXML
    private VBox studentsList;

    /**
     * Show the list of students.
     *
     * @param students all the students
     */
    private void showStudentsList(ArrayList<Student> students) {

        if (!students.isEmpty()) {
            for (Student student : students) {
                // Student label
                Label l = new Label(student.fullName);
                l.getStyleClass().add("p");

                // Buttons
                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        c.removeStudent(student);

                        try {
                            Class.openClassGUI(c, (Stage) studentsList.getScene().getWindow());
                        } catch (IOException exception) { exception.printStackTrace(); }
                    }
                });

                HBox hBox = new HBox(l, deleteButton);
                hBox.setSpacing(10);
                studentsList.getChildren().add(hBox);
            }

            // Create new student button
            Button createNewStudentButton = new Button("Create new student");
            createNewStudentButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    // Show dialog to ask for name
                    TextInputDialog askStudentName = new TextInputDialog();
                    askStudentName.setTitle("Create student");
                    askStudentName.setHeaderText("Create student");
                    askStudentName.setContentText("What is the student's name?");

                    // If name is given, create student and add to ArrayList
                    Optional<String> result = askStudentName.showAndWait();
                    if (result.isPresent()) {
                        Student student = new Student(result.get());
                        c.addStudent(student);
                    }
                    try {
                        Class.openClassGUI(c, (Stage) studentsList.getScene().getWindow());
                    } catch (IOException exception) { exception.printStackTrace(); }
                }
            });
            studentsList.getChildren().add(createNewStudentButton);
        }
    }

    public void setClass(Class c) {
        this.c = c;

        this.showStudentsList(c.students);
    }
}
