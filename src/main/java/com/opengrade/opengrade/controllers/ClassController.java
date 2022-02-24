package com.opengrade.opengrade.controllers;

import com.opengrade.opengrade.Main;
import com.opengrade.opengrade.models.Class;
import com.opengrade.opengrade.models.Student;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class ClassController {
    private Class c;

    @FXML
    private Label title;

    @FXML
    private VBox studentsSection;

    @FXML
    private ListView<String> studentsList;

    @FXML
    private ListView<String> gradeList;

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
    @FXML
    protected void editStudentName() {
//        try {
//            Student selectedStudent = this.getOneSelectedStudent();
//            // Show dialog to ask for new name
//            TextInputDialog askStudentName = new TextInputDialog();
//            askStudentName.setTitle("Edit student: " + selectedStudent.fullName);
//            askStudentName.setHeaderText("Edit student: " + selectedStudent.fullName);
//            askStudentName.setContentText("What is the student's new name?");
//
//            // If name is given, change name
//            Optional<String> result = askStudentName.showAndWait();
//            if (result.isPresent()) {
//                this.c.editStudentName(selectedStudent, result.get());
//                this.refreshWindow();
//            }
//        } catch (InvalidParameterException exception) {
//            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
//        }
    }

    /**
     * Add a new student to the class.
     */
    @FXML
    protected void createStudent() {
//        // Show dialog to ask for name
//        TextInputDialog askStudentName = new TextInputDialog();
//        askStudentName.setTitle("Create student");
//        askStudentName.setHeaderText("Create student");
//        askStudentName.setContentText("What is the student's name?");
//
//        // If name is given, create student
//        Optional<String> result = askStudentName.showAndWait();
//        if (result.isPresent()) {
//            Student student = new Student(result.get());
//            try {
//                c.addStudent(student);
//            } catch (IllegalArgumentException exception) {
//                // If student already exists
//                new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
//            }
//        }
//
//        this.refreshWindow();
    }

//    /**
//     * Checks and returns the selected student.
//     *
//     * @return the selected student
//     * @throws InvalidParameterException too many students selected
//     */
//    private Student getOneSelectedStudent() throws InvalidParameterException {
//        // Get selected student
//        ObservableList<String> selectedStudents = studentsList.getSelectionModel().getSelectedItems();
//        if (selectedStudents.size() != 1) {
//            throw new InvalidParameterException("Please select at most one student.");
//        } else {
//            String selectedStudentName = selectedStudents.get(0);
//            for (Student student : this.c.students) {
//                if (student.fullName.equals(selectedStudentName)) {
//                    return student;
//                }
//            }
//        }
//        return null;
//    }

    @FXML
    protected void handleCreateAssignmentButton() {
        // Ask assignment name
        TextInputDialog askAssignmentName = new TextInputDialog();
        askAssignmentName.setTitle("Create assignment");
        askAssignmentName.setHeaderText("Create assignment");
        askAssignmentName.setContentText("What is the assignment name?");

        Optional<String> assignmentNameResult = askAssignmentName.showAndWait();



        // Ask assignment weight
        // Use while loop to enforce number input
        boolean weightIsNotDouble = true;
        double assignmentWeight = 0;

        while (weightIsNotDouble) {
            TextInputDialog askAssignmentWeight = new TextInputDialog();
            askAssignmentWeight.setTitle("Create assignment");
            askAssignmentWeight.setHeaderText("Create assignment");
            askAssignmentWeight.setContentText("What is the assignment weight?");

            Optional<String> assignmentWeightResult = askAssignmentWeight.showAndWait();

            // Get weight as double
            if (assignmentWeightResult.isPresent()) {
                try {
                    assignmentWeight = Double.parseDouble(assignmentWeightResult.get());
                    weightIsNotDouble = false;
                } catch (NumberFormatException exception) {
                    new Alert(Alert.AlertType.ERROR, String.format("%s; Please enter a number.", exception.getMessage())).showAndWait();
                }
            }
        }

        if (assignmentNameResult.isPresent()) {
            String assignmentName = assignmentNameResult.get();

            // Get each student's grade
            for (Student s : this.c.students) {

                // Use while loop to enforce number/none input
                boolean gradeIsNotDoubleOrNone = true;

                while (gradeIsNotDoubleOrNone) {

                    // Knowledge grade
                    TextInputDialog askKnowledgeGrade = new TextInputDialog();
                    askKnowledgeGrade.setTitle("Create assignment");
                    askKnowledgeGrade.setHeaderText("Create assignment");
                    askKnowledgeGrade.setContentText(String.format("What is %s's grade in knowledge?", s.fullName));
                    Optional<String> askKnowledgeResult = askKnowledgeGrade.showAndWait();

                    // Thinking grade
                    TextInputDialog askThinkingGrade = new TextInputDialog();
                    askThinkingGrade.setTitle("Create assignment");
                    askThinkingGrade.setHeaderText("Create assignment");
                    askThinkingGrade.setContentText(String.format("What is %s's grade in thinking?", s.fullName));
                    Optional<String> askThinkingResult = askThinkingGrade.showAndWait();

                    // Communication grade
                    TextInputDialog askCommunicationGrade = new TextInputDialog();
                    askCommunicationGrade.setTitle("Create assignment");
                    askCommunicationGrade.setHeaderText("Create assignment");
                    askCommunicationGrade.setContentText(String.format("What is %s's grade in communication?", s.fullName));
                    Optional<String> askCommunicationResult = askCommunicationGrade.showAndWait();

                    // Application grade
                    TextInputDialog askApplicationGrade = new TextInputDialog();
                    askApplicationGrade.setTitle("Create assignment");
                    askApplicationGrade.setHeaderText("Create assignment");
                    askApplicationGrade.setContentText(String.format("What is %s's grade in application?", s.fullName));
                    Optional<String> askApplicationResult = askApplicationGrade.showAndWait();

                    // Gather grades info
                    double knowledgeGrade = -1;
                    double thinkingGrade = -1;
                    double communicationGrade = -1;
                    double applicationGrade = -1;

                    try {
                        if (askKnowledgeResult.isPresent())
                            knowledgeGrade = Double.parseDouble(askKnowledgeResult.get());

                        if (askThinkingResult.isPresent())
                            thinkingGrade = Double.parseDouble(askThinkingResult.get());

                        if (askCommunicationResult.isPresent())
                            communicationGrade = Double.parseDouble(askCommunicationResult.get());

                        if (askApplicationResult.isPresent())
                            applicationGrade = Double.parseDouble(askApplicationResult.get());

                        gradeIsNotDoubleOrNone = false;

                        // Create assignment for student
                        s.addAssignment(this.c, assignmentName, knowledgeGrade, thinkingGrade, communicationGrade, applicationGrade, assignmentWeight);

                    } catch (NumberFormatException exception) {
                        new Alert(Alert.AlertType.ERROR, String.format("%s; Please enter a number.", exception.getMessage())).showAndWait();
                    }
                }
            }
        }
    }

    @FXML
    protected void handleDeleteClassButton() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, String.format("Are you sure you want to to delete %s? This action is non-recoverable.", this.c.className));
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            this.c.deleteClass();
            this.handleBackButton();
        }
    }

    @FXML
    protected void handleBackButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = (Stage) studentsSection.getScene().getWindow();
        stage.setTitle("OpenGrade");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Reload the class window.
     */
    private void refreshWindow() {
        try {
            Class.openClassGUI(c, (Stage) studentsList.getScene().getWindow());
        } catch (IOException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            exception.printStackTrace();
        }
    }

    /**
     * Tells the window which class this window represents.
     * Acts as window constructor, first method that runs.
     * Initiates other things (e.g. shows stuff on screen, handles closing event)
     *
     * @param c the class this window represents
     */
    public void setClass(Class c) throws IOException {
        this.c = c;

        this.drawWindow();
    }

    /**
     * Puts things on the screen.
     * This will put components on the screen that are variable,
     * and cannot be hardcoded in the FXML,
     * such as things that need the class info.
     */
    private void drawWindow() {
        this.showStudentsList();

        this.title.setText(c.className);

        for (Student s : this.c.students) {
            System.out.printf("Student: %s; Average: %f\n", s.fullName, s.getAverage(this.c));
        }
    }
}