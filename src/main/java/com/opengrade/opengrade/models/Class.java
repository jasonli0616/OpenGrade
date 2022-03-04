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

public class Class {
    public int id;

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
    }

    public void editStudentName(Student student, String newName) {
        student.fullName = newName;
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
        Database.insertClass(this);
    }


    public void deleteClass() {
        Database.deleteClass(this);
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

    @Override
    public String toString() {
        return this.className;
    }
}