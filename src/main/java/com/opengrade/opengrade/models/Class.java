package com.opengrade.opengrade.models;

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

    public void addStudent(Student s) {
        this.students.add(s);
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
}
