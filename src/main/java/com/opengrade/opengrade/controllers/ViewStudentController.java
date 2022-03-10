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

    @FXML
    private ListView<String> averageList;

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
        averageList.getItems().clear();

        for (HashMap<String, Object> assignment : this.student.assignments) {

            assignmentList.getItems().add((String) assignment.get(AssignmentAttribute.ASSIGNMENT_NAME.attribute));

            double knowledgeMark = (Double) assignment.get(AssignmentAttribute.KNOWLEDGE_MARK.attribute);
            double thinkingMark = (Double) assignment.get(AssignmentAttribute.THINKING_MARK.attribute);
            double communicationMark = (Double) assignment.get(AssignmentAttribute.COMMUNICATION_MARK.attribute);
            double applicationMark = (Double) assignment.get(AssignmentAttribute.APPLICATION_MARK.attribute);
            double averageMark = Student.getAverage(knowledgeMark, thinkingMark, communicationMark, applicationMark);

            knowledgeList.getItems().add(knowledgeMark == -1 ? "-" : String.format("%.2f", knowledgeMark));
            thinkingList.getItems().add(thinkingMark == -1 ? "-" : String.format("%.2f", thinkingMark));
            communicationList.getItems().add(communicationMark == -1 ? "-" : String.format("%.2f", communicationMark));
            applicationList.getItems().add(applicationMark == -1 ? "-" : String.format("%.2f", applicationMark));
            averageList.getItems().add(averageMark == -1 ? "-" : String.format("%.2f", averageMark));

            weightList.getItems().add(String.format("%.2f", (Double) assignment.get(AssignmentAttribute.WEIGHT.attribute)));
        }

        // Make lists editable

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

    /**
     * Get all grades from the screen, and overwrite database grades data.
     */
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

            // If assignment name has been changed to blank, remove assignment
            if (assignmentName.isEmpty()) {
                if (assignmentNames.size() <= 1)
                    this.student.assignments = assignments;

                continue;
            }

            try {
                // Get marks from view inputs
                // If mark is "-" or blank, ignore
                double knowledgeMark = knowledgeMarkString.equals("-") || knowledgeMarkList.isEmpty() ? -1 : Double.parseDouble(knowledgeMarkString);
                double thinkingMark = thinkingMarkString.equals("-") || thinkingMarkList.isEmpty() ? -1 : Double.parseDouble(thinkingMarkString);
                double communicationMark = communicationMarkString.equals("-") || communicationMarkList.isEmpty() ? -1 : Double.parseDouble(communicationMarkString);
                double applicationMark = applicationMarkString.equals("-") || applicationMarkString.isEmpty() ? -1 : Double.parseDouble(applicationMarkString);
                double assignmentWeight = Double.parseDouble(weightString);

                // Make sure marks are valid entries
                if (!AssignmentAttribute.markIsValid(knowledgeMark) || !AssignmentAttribute.markIsValid(thinkingMark) || !AssignmentAttribute.markIsValid(communicationMark) || !AssignmentAttribute.markIsValid(applicationMark)) {
                    throw new NumberFormatException("Out of bounds");
                }

                HashMap<String, Object> assignment = new HashMap<String, Object>();
                assignment.put(AssignmentAttribute.CLASS_ID.attribute, this.c.id);
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

            new Alert(Alert.AlertType.CONFIRMATION, "Assignments have been updated").showAndWait();
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

        this.showLists();

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
                this.student.changeName(result.get());

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
            this.c.removeStudent(this.student);
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

    /**
     * Puts components to the window/edit components on the window
     */
    private void drawWindow() {
        this.title.setText(String.format("%s - %s", student.fullName, c.className));

        this.showLists();
    }

    /**
     * Set the student and class for the window
     *
     * @param s the student to set
     * @param c the class to set
     */
    public void setStudent(Student s, Class c) {
        this.student = s;
        this.c = c;

        this.drawWindow();
    }
}