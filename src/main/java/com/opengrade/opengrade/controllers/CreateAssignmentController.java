package com.opengrade.opengrade.controllers;

import com.opengrade.opengrade.models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CreateAssignmentController {
    private Student student;
    @FXML
    private Label title;

    public void setAssignment(Student s, String assignmentName, double assignmentWeight) {
        this.title.setText(String.format("%s: %s", this.title.getText(), assignmentName));
    }

    // TODO: Use window to input user grades

}