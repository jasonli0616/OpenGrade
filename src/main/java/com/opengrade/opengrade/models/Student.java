package com.opengrade.opengrade.models;

import java.util.HashMap;

public class Student {
    public String fullName;

    public HashMap<String, Float> assignments = new HashMap<String, Float>();

    public Student(String fullName) {
        this.fullName = fullName;
    }

    public void addAssignment(String assignment, float grade) {
        this.assignments.put(assignment, grade);
    }

    public void removeAssignment(String assignment) {
        this.assignments.remove(assignment);
    }

    public void updateAssignmentName(String oldAssignment, String newAssignment) {
        float grade = assignments.get(oldAssignment);
        this.removeAssignment(oldAssignment);
        this.addAssignment(newAssignment, grade);
    }

    @Override
    public String toString() {
        return this.fullName;
    }
}
