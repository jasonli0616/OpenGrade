package com.opengrade.opengrade.controllers;

import com.opengrade.opengrade.models.Class;
import com.opengrade.opengrade.models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateAssignmentController {
    private Student student;

    private String assignmentName;

    private double assignmentWeight;

    private Class c;

    @FXML
    private Label title;

    @FXML
    private Label studentName;

    @FXML
    private Label weightLabel;

    @FXML
    private TextField knowledgeMarkInput;

    @FXML
    private TextField thinkingMarkInput;

    @FXML
    private TextField communicationMarkInput;

    @FXML
    private TextField applicationMarkInput;

    @FXML
    protected void handleInputGrades() {
        try {
            // Get marks
            double knowledgeMark;
            double thinkingMark;
            double communicationMark;
            double applicationMark;

            // Check if marks are empty
            if (knowledgeMarkInput.getText().isEmpty()) knowledgeMark = -1;
            else knowledgeMark = Double.parseDouble(knowledgeMarkInput.getText());

            if (thinkingMarkInput.getText().isEmpty()) thinkingMark = -1;
            else thinkingMark = Double.parseDouble(thinkingMarkInput.getText());

            if (communicationMarkInput.getText().isEmpty()) communicationMark = -1;
            else communicationMark = Double.parseDouble(communicationMarkInput.getText());

            if (applicationMarkInput.getText().isEmpty()) applicationMark = -1;
            else applicationMark = Double.parseDouble(applicationMarkInput.getText());

            this.student.addAssignment(this.c, this.assignmentName, knowledgeMark, thinkingMark, communicationMark, applicationMark, this.assignmentWeight);

            ((Stage) this.title.getScene().getWindow()).close();
        } catch (NumberFormatException exception) {
            new Alert(Alert.AlertType.ERROR, "Please input numbers only, or leave blank.").showAndWait();
        }
    }

    /**
     * Put the custom class items onto the screen.
     */
    private void drawWindow() {
        this.title.setText(String.format("%s: %s\n\n", this.title.getText(), this.assignmentName));

        this.studentName.setText(String.format("Student: %s", this.student.fullName));

        this.weightLabel.setText(String.format("Weight: %.2f", this.assignmentWeight));
    }

    /**
     * Set the assignment attributes.
     *
     * @param s                the student
     * @param assignmentName   the name of the assignment
     * @param assignmentWeight the weight of the assignment
     */
    public void setAssignment(Student s, String assignmentName, double assignmentWeight, Class c) {
        this.student = s;
        this.assignmentName = assignmentName;
        this.assignmentWeight = assignmentWeight;
        this.c = c;

        this.drawWindow();
    }

}