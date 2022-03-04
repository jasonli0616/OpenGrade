package com.opengrade.opengrade.controllers;

import com.opengrade.opengrade.models.AssignmentAttribute;
import com.opengrade.opengrade.models.Class;
import com.opengrade.opengrade.models.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.HashMap;
import java.util.Map;

public class ViewStudentController {
    private Student student;

    private Class c;

    @FXML
    private Label title;

    @FXML
    private ListView<String> assignmentList;

    @FXML
    private ListView<Double> knowledgeList;

    @FXML
    private ListView<Double> thinkingList;

    @FXML
    private ListView<Double> communicationList;

    @FXML
    private ListView<Double> applicationList;

    @FXML
    private ListView<Double> weightList;

    /**
     * Show the lists of student assignments on the screen.
     */
    private void showLists() {
        for (HashMap<String, Object> assignment : this.student.assignments) {

            assignmentList.getItems().add((String) assignment.get(AssignmentAttribute.ASSIGNMENT_NAME.attribute));

            knowledgeList.getItems().add((Double) assignment.get(AssignmentAttribute.KNOWLEDGE_MARK.attribute));
            thinkingList.getItems().add((Double) assignment.get(AssignmentAttribute.THINKING_MARK.attribute));
            communicationList.getItems().add((Double) assignment.get(AssignmentAttribute.COMMUNICATION_MARK.attribute));
            applicationList.getItems().add((Double) assignment.get(AssignmentAttribute.APPLICATION_MARK.attribute));

            weightList.getItems().add((Double) assignment.get(AssignmentAttribute.WEIGHT.attribute));

        }
    }

    private void drawWindow() {
        this.title.setText(String.format("%s - %s", student.fullName, c.className));

        this.showLists();
    }

    public void setStudent(Student s, Class c) {
        this.student = s;
        this.c = c;

        this.drawWindow();
    }

    // TODO:
    // - Functionality to update grades in this window
    // Move class student buttons to this page
}