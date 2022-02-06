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

    public HashMap<String, HashMap<String, Float>> toMap() {
        HashMap<String, HashMap<String, Float>> map = new HashMap<String, HashMap<String, Float>>();

        for (Student student : students) {
            map.put(student.toString(), student.assignments);
        }

        return map;
    }

    public static Class mapToClass(HashMap<String, HashMap<String, Float>> map, String className) {

        ArrayList<Student> students = new ArrayList<Student>();
        // Add all students
        for (Map.Entry<String, HashMap<String, Float>> entry : map.entrySet()) {
            String studentName = entry.getKey();
            HashMap<String, Float> studentAssignments = entry.getValue();

            Student student = new Student(studentName);

            // Add assignments to student
            for (Map.Entry<String, Float> entry1 : studentAssignments.entrySet()) {
                String assignment = entry1.getKey();
                float grade = entry1.getValue();
                student.addAssignment(assignment, grade);
            }

            students.add(student);
        }

        Class c = new Class(className);
        for (Student student : students) {
            c.addStudent(student);
        }

        return c;
    }
}
