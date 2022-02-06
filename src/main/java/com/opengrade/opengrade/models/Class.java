package com.opengrade.opengrade.models;

import java.util.ArrayList;

public class Class {
    public String className;

    ArrayList<Student> students = new ArrayList<Student>();

    public Class(String className) {
        this.className = className;
    }

    public void addStudent(Student s) {
        this.students.add(s);
    }
}
