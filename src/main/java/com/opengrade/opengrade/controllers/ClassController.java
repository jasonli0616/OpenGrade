package com.opengrade.opengrade.controllers;

import com.opengrade.opengrade.Database;
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
        for (Student student : students) {
            // Add student to ListView
            studentsList.getItems().add(student.fullName);

            Button viewStudentButton = new Button("View student");
        }
    }

    /**
     * Shows the list of students' average grade.
     */
    private void showGradeList() {
        for (Student student : this.c.students) {
            gradeList.getItems().add(String.format("%.2f%%", student.getAverage(this.c)));
        }
    }

    /**
     * Shows the student's assignments info.
     * (marks, etc.)
     */
    @FXML
    protected void viewSelectedStudentAssignments() {
        // TODO:
        // * Calculate and retrieve student marks,
        // * Display window of marks
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
        // Ask existing/new student
        ChoiceDialog<String> studentTypeChoice = new ChoiceDialog<String>();
        studentTypeChoice.setTitle("Add student");
        studentTypeChoice.setHeaderText("Add student");
        studentTypeChoice.setContentText("Please select the student type:");
        studentTypeChoice.getItems().addAll("Select existing student", "Create new student");
        Optional<String> studentTypeChoiceResult = studentTypeChoice.showAndWait();

        if (studentTypeChoiceResult.isPresent()) {
            if (studentTypeChoiceResult.get().equals("Select existing student")) {

                if (Database.getAllStudents().isEmpty()) {
                    new Alert(Alert.AlertType.ERROR, "There are no existing students.").showAndWait();
                    this.createStudent();
                } else {
                    ChoiceDialog<Student> chooseStudentDialog = new ChoiceDialog<Student>();
                    chooseStudentDialog.setTitle("Select a student");
                    chooseStudentDialog.setHeaderText("Select a student");
                    chooseStudentDialog.setContentText("Please select a student:");

                    for (Student student : Database.getAllStudents()) {
                        chooseStudentDialog.getItems().add(student);
                    }

                    Optional<Student> chooseStudentResult = chooseStudentDialog.showAndWait();

                    if (chooseStudentResult.isPresent()) {
                        Student chosenStudent = chooseStudentResult.get();
                        if (this.c.students.contains(chosenStudent))
                            new Alert(Alert.AlertType.ERROR, String.format("Student %s already exists in this class.", chosenStudent)).showAndWait();
                        else {
                            this.c.students.add(chosenStudent);
                            Database.associateStudentClass(chosenStudent, this.c);
                        }
                    }
                }

            } else {
                // Insert new student to database, and add to class

                // Show dialog to ask for name
                TextInputDialog askStudentName = new TextInputDialog();
                askStudentName.setTitle("Create student");
                askStudentName.setHeaderText("Create student");
                askStudentName.setContentText("What is the student's name?");

                // If name is given, create student and add to ArrayList
                Optional<String> result = askStudentName.showAndWait();
                if (result.isPresent()) {
                    Student student = new Student(result.get());
                    student.id = Database.insertStudent(student);

                    this.c.students.add(student);

                    Database.associateStudentClass(student, this.c);
                }
            }
        }

        this.refreshWindow();
    }

    @FXML
    protected void deleteStudent() {
        try {
            Student student = this.getOneSelectedStudent();

            this.c.students.remove(student);
            Database.unassociateStudentClass(student, this.c);

            this.refreshWindow();
        } catch (InvalidParameterException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
        }
    }

    @FXML
    protected void addStudentAssignment() {
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
            Student s = this.getOneSelectedStudent();

            this.askStudentAssignmentGrades(s, assignmentName, assignmentWeight);
        }
    }

    /**
     * Ask user for student grades, and insert assignment into database.
     *
     * @param s the student who completed the assignment
     */
    private void askStudentAssignmentGrades(Student s, String assignmentName, double assignmentWeight) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/create-assignment.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 300);
            Stage stage = new Stage();
            stage.setTitle("Create assignment: " + assignmentName);
            stage.setScene(scene);
            CreateAssignmentController controller = fxmlLoader.getController();
            controller.setAssignment(s, assignmentName, assignmentWeight, this.c);
            stage.showAndWait();

        } catch (IOException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            exception.printStackTrace();
        }
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
            throw new InvalidParameterException("Please select exactly one student.");
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

            // Get each student's grade and insert assignment
            for (Student s : this.c.students) {
                this.askStudentAssignmentGrades(s, assignmentName, assignmentWeight);
            }

            this.refreshWindow();
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
        this.showGradeList();

        this.title.setText(c.className);
    }
}