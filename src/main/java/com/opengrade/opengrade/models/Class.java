package com.opengrade.opengrade.models;

import com.opengrade.opengrade.Database;
import com.opengrade.opengrade.Main;
import com.opengrade.opengrade.controllers.ClassController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Class {
    public String className;

    public ArrayList<Student> students = new ArrayList<Student>();

    public Class(String className) {
        this.className = className;
    }

    public Class (String className, ArrayList<Student> students) {
        this.className = className;
        this.students = students;
    }

    /**
     * Add a new student to the class.
     *
     * @param newStudent the new student to add
     * @throws IllegalArgumentException if a student with the same name already exists
     */
    public void addStudent(Student newStudent) throws IllegalArgumentException {
        for (Student existingStudent : students) {
            if (existingStudent.fullName.equals(newStudent.fullName))
                throw new IllegalArgumentException("A student with the same name already exists in the class.");
        }
        this.students.add(newStudent);
        Database.insertClass(this);
    }

    public void editStudentName(Student student, String newName) {
        student.fullName = newName;
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
        Database.insertClass(this);
    }

    public void createAssignment(String assignmentName, HashMap<Student, Float> grades) {
        // For every student
        for (Map.Entry<Student, Float> entry : grades.entrySet()) {
            Student student = entry.getKey();
            float grade = entry.getValue();

            // Find student and add assignment
            for (Student existingStudent : students) {
                if (existingStudent.fullName.equals(student.fullName)) {
                    existingStudent.addAssignment(assignmentName, grade);
                    break;
                }
            }
        }
    }

    /**
     * Return all assignments, as well as students' grades.
     *
     * @return ArrayList containing HashMaps, containing the assignment name and a HashMap, containing the student and their grade
     */
    public HashMap<String, HashMap<Student, Float>> getAllAssignments() {
        HashMap<String, HashMap<Student, Float>> assignments = new HashMap<String, HashMap<Student, Float>>();

        // Put all students' assignments into HashMap
        for (Student student : students) {
            for (Map.Entry<String, Float> entry : student.assignments.entrySet()) {
                String assignmentName = entry.getKey();
                float grade = entry.getValue();

                if (assignments.containsKey(assignmentName))
                    // If assignment is already in HashMap, add student to assignment
                    assignments.get(assignmentName).put(student, grade);
                else {
                    // Else, add new assignment to HashMap
                    HashMap<Student, Float> studentAssignment = new HashMap<Student, Float>();
                    studentAssignment.put(student, grade);
                    assignments.put(assignmentName, studentAssignment);
                }
            }
        }
        return assignments;
    }

    public void deleteClass() {
        Database.deleteClass(this);
    }

    /**
     * Converts the class to a HashMap.
     * This method is called when inserting the class into the database.
     *
     * @return the class represented as a HashMap
     */
    public HashMap<String, HashMap<String, Float>> toMap() {
        // Create HashMap
        HashMap<String, HashMap<String, Float>> map = new HashMap<String, HashMap<String, Float>>();

        // Add all class info to HashMap
        for (Student student : students) {
            map.put(student.toString(), student.assignments);
        }

        return map;
    }

    /**
     * Converts a HashMap to a class.
     * This method is called when selecting a class from the database.
     *
     * @param map the class represented as a HashMap
     * @param className the name of the class
     * @return the class represented as the Class object
     */
    public static Class mapToClass(HashMap<String, HashMap<String, Float>> map, String className) {

        ArrayList<Student> students = new ArrayList<Student>();
        // Add all students
        for (Map.Entry<String, HashMap<String, Float>> entry : map.entrySet()) {
            String studentName = entry.getKey();
            HashMap<String, Float> assignments = entry.getValue();

            Student student = new Student(studentName, assignments);

            students.add(student);
        }

        return new Class(className, students);
    }

    /**
     * Open a class in the window.
     *
     * @param c the class to open
     * @param stage the stage to open the class in
     * @throws IOException
     */
    public static void openClassGUI(Class c, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/class.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("OpenGrade - " + c.className);
        stage.setScene(scene);
        stage.setMaximized(true);
        ClassController controller = fxmlLoader.getController();
        controller.setClass(c);
    }
}