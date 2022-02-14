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
        try {
            Student selectedStudent = this.getOneSelectedStudent();
            // Show dialog to ask for new name
            TextInputDialog askStudentName = new TextInputDialog();
            askStudentName.setTitle("Edit student: " + selectedStudent.fullName);
            askStudentName.setHeaderText("Edit student: " + selectedStudent.fullName);
            askStudentName.setContentText("What is the student's new name?");

            // If name is given, change name
            Optional<String> result = askStudentName.showAndWait();
            if (result.isPresent()) {
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
    @FXML
    protected void createStudent() {
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

    @FXML
    protected void handleCreateAssignmentButton() {
        // Ask assignment name
        TextInputDialog askAssignmentName = new TextInputDialog();
        askAssignmentName.setTitle("Create assignment");
        askAssignmentName.setHeaderText("Create assignment");
        askAssignmentName.setContentText("What is the assignment name?");

        Optional<String> assignmentNameResult = askAssignmentName.showAndWait();

        // Assignment name input is empty
        if (assignmentNameResult.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter an assignment name.").showAndWait();
            this.handleCreateAssignmentButton();
        }

        // Assignment already exists
        if (this.c.getAllAssignments().containsKey(assignmentNameResult.get())) {
            new Alert(Alert.AlertType.ERROR, "An assignment with this name already exists.").showAndWait();
            this.handleCreateAssignmentButton();
        }

        // Get student grades
        HashMap<Student, Float> grades = new HashMap<Student, Float>();
        for (Student student : this.c.students) {
            boolean gradeFormatIsIncorrect = true;

            // This while loop will enforce an input to be from 0-100.
            while (gradeFormatIsIncorrect) {
                TextInputDialog askStudentGrade = new TextInputDialog();
                askStudentGrade.setTitle("Create assignment");
                askStudentGrade.setHeaderText("Create assignment");
                askStudentGrade.setContentText(String.format("What is %s's grade?", student.fullName));

                Optional<String> gradeResult = askStudentGrade.showAndWait();

                if (gradeResult.isPresent()) {
                    try {
                        float grade = Float.parseFloat(gradeResult.get());

                        // Ensure grade inputted is between 0-100
                        if (!(0f <= grade && grade <= 100f))
                            throw new NumberFormatException();

                        gradeFormatIsIncorrect = false;

                        grades.put(student, grade);

                    } catch (NumberFormatException exception) {
                        // If inputted grade is not a float between 0-100
                        new Alert(Alert.AlertType.WARNING, "Please enter a number between 0 - 100.").showAndWait();
                    }
                }
            }
        }

        this.c.createAssignment(assignmentNameResult.get(), grades);
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
    }
}