package com.opengrade.opengrade.controllers;

import com.opengrade.opengrade.Database;
import com.opengrade.opengrade.models.AssignmentAttribute;
import com.opengrade.opengrade.models.Class;
import com.opengrade.opengrade.models.Student;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Stage;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class ViewStudentController {
    private Student student;

    private Class c;

    @FXML
    private Label title;

    @FXML
    private ListView<String> assignmentList;

    @FXML
    private ListView<String> knowledgeList;

    @FXML
    private ListView<String> thinkingList;

    @FXML
    private ListView<String> communicationList;

    @FXML
    private ListView<String> applicationList;

    @FXML
    private ListView<String> weightList;

    /**
     * Show the lists of student assignments on the screen.
     */
    private void showLists() {

        // Clear lists
        assignmentList.getItems().clear();
        knowledgeList.getItems().clear();
        thinkingList.getItems().clear();
        communicationList.getItems().clear();
        applicationList.getItems().clear();
        weightList.getItems().clear();

        for (HashMap<String, Object> assignment : this.student.assignments) {

            assignmentList.getItems().add((String) assignment.get(AssignmentAttribute.ASSIGNMENT_NAME.attribute));

            double knowledgeMark = (Double) assignment.get(AssignmentAttribute.KNOWLEDGE_MARK.attribute);
            double thinkingMark = (Double) assignment.get(AssignmentAttribute.THINKING_MARK.attribute);
            double communicationMark = (Double) assignment.get(AssignmentAttribute.COMMUNICATION_MARK.attribute);
            double applicationMark = (Double) assignment.get(AssignmentAttribute.APPLICATION_MARK.attribute);


            knowledgeList.getItems().add(knowledgeMark == -1 ? "-" : String.format("%.2f", knowledgeMark));
            thinkingList.getItems().add(thinkingMark == -1 ? "-" : String.format("%.2f", thinkingMark));
            communicationList.getItems().add(communicationMark == -1 ? "-" : String.format("%.2f", communicationMark));
            applicationList.getItems().add(applicationMark == -1 ? "-" : String.format("%.2f", applicationMark));

            weightList.getItems().add(String.format("%.2f", (Double) assignment.get(AssignmentAttribute.WEIGHT.attribute)));
        }

        assignmentList.setEditable(true);
        knowledgeList.setEditable(true);
        thinkingList.setEditable(true);
        communicationList.setEditable(true);
        applicationList.setEditable(true);
        weightList.setEditable(true);

        assignmentList.setCellFactory(TextFieldListCell.forListView());
        knowledgeList.setCellFactory(TextFieldListCell.forListView());
        thinkingList.setCellFactory(TextFieldListCell.forListView());
        communicationList.setCellFactory(TextFieldListCell.forListView());
        applicationList.setCellFactory(TextFieldListCell.forListView());
        weightList.setCellFactory(TextFieldListCell.forListView());

    }

    @FXML
    protected void updateGrades() {
        ArrayList<HashMap<String, Object>> assignments = new ArrayList<HashMap<String, Object>>();

        ObservableList<String> assignmentNames = assignmentList.getItems();
        ObservableList<String> knowledgeMarkList = knowledgeList.getItems();
        ObservableList<String> thinkingMarkList = thinkingList.getItems();
        ObservableList<String> communicationMarkList = communicationList.getItems();
        ObservableList<String> applicationMarkList = applicationList.getItems();
        ObservableList<String> assignmentWeightList = weightList.getItems();

        for (int assignmentIndex = 0; assignmentIndex < assignmentNames.size(); assignmentIndex++) {
            String assignmentName = assignmentNames.get(assignmentIndex);
            String knowledgeMarkString = knowledgeMarkList.get(assignmentIndex);
            String thinkingMarkString = thinkingMarkList.get(assignmentIndex);
            String communicationMarkString = communicationMarkList.get(assignmentIndex);
            String applicationMarkString = applicationMarkList.get(assignmentIndex);
            String weightString = assignmentWeightList.get(assignmentIndex);

            try {
                double knowledgeMark = knowledgeMarkString.equals("-") ? -1 : Double.parseDouble(knowledgeMarkString);
                double thinkingMark = thinkingMarkString.equals("-") ? -1 : Double.parseDouble(thinkingMarkString);
                double communicationMark = communicationMarkString.equals("-") ? -1 : Double.parseDouble(communicationMarkString);
                double applicationMark = applicationMarkString.equals("-") ? -1 : Double.parseDouble(applicationMarkString);
                double assignmentWeight = Double.parseDouble(weightString);

                // Make sure marks are valid entries
                if (!markIsValid(knowledgeMark) || !markIsValid(thinkingMark) || !markIsValid(communicationMark) || !markIsValid(applicationMark)) {
                    throw new NumberFormatException("Out of bounds");
                }

                HashMap<String, Object> assignment = new HashMap<String, Object>();
                assignment.put(AssignmentAttribute.ASSIGNMENT_NAME.attribute, assignmentName);
                assignment.put(AssignmentAttribute.KNOWLEDGE_MARK.attribute, knowledgeMark);
                assignment.put(AssignmentAttribute.THINKING_MARK.attribute, thinkingMark);
                assignment.put(AssignmentAttribute.COMMUNICATION_MARK.attribute, communicationMark);
                assignment.put(AssignmentAttribute.APPLICATION_MARK.attribute, applicationMark);
                assignment.put(AssignmentAttribute.WEIGHT.attribute, assignmentWeight);

                assignments.add(assignment);

                this.student.assignments = assignments;


            } catch (NumberFormatException exception) {
                new Alert(Alert.AlertType.ERROR, String.format("%s; Enter a number between 0 and 100; or \"-\" for null.", exception.getMessage())).showAndWait();
                return;
            }
        }

        // Overwrite all assignment entries in database with new assignment marks
        Database.removeStudentClassAssignments(this.student, this.c);

        for (HashMap<String, Object> assignment : assignments) {
            String assignmentName = (String) assignment.get(AssignmentAttribute.ASSIGNMENT_NAME.attribute);
            double knowledgeMark = (Double) assignment.get(AssignmentAttribute.KNOWLEDGE_MARK.attribute);
            double thinkingMark = (Double) assignment.get(AssignmentAttribute.THINKING_MARK.attribute);
            double communicationMark = (Double) assignment.get(AssignmentAttribute.COMMUNICATION_MARK.attribute);
            double applicationMark = (Double) assignment.get(AssignmentAttribute.APPLICATION_MARK.attribute);
            double weight = (Double) assignment.get(AssignmentAttribute.WEIGHT.attribute);

            Database.insertStudentAssignment(this.student, this.c, assignmentName, knowledgeMark, thinkingMark, communicationMark, applicationMark, weight);
        }

    }

    /**
     * Change the name of a student in the class.
     */
    @FXML
    protected void editName() {
        try {
            // Show dialog to ask for new name
            TextInputDialog askStudentName = new TextInputDialog();
            askStudentName.setTitle("Edit student: " + this.student.fullName);
            askStudentName.setHeaderText("Edit student: " + this.student.fullName);
            askStudentName.setContentText("What is the student's new name?");

            // If name is given, change name
            Optional<String> result = askStudentName.showAndWait();
            if (result.isPresent()) {
                this.c.editStudentName(this.student, result.get());

                this.title.setText(String.format("%s - %s", result.get(), c.className));
            }
        } catch (InvalidParameterException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
        }
    }

    /**
     * Remove student from the class.
     */
    @FXML
    protected void removeFromClass() {
        try {
            this.c.students.remove(this.student);
            Database.unassociateStudentClass(this.student, this.c);
            ((Stage) title.getScene().getWindow()).close();

        } catch (InvalidParameterException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
        }
    }

    /**
     * Create assignment for one student.
     */
    @FXML
    protected void createAssignment() {
        ArrayList<Student> students = new ArrayList<Student>();
        students.add(this.student);
        ClassController.createAssignmentDialog(students, this.c);

        this.showLists();
    }

    private boolean markIsValid(double mark) {
        return (mark >= -1 && mark <= 100);
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
}