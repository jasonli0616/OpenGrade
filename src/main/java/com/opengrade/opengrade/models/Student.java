package com.opengrade.opengrade.models;

import java.util.HashMap;

public class Student {
    public String fullName;

    public HashMap<String, Float> assignments = new HashMap<String, Float>();

    public Student(String fullName) {
        this.fullName = fullName;
    }

    public Student(String fullName, HashMap<String, Float> assignments) {
        this.fullName = fullName;
        this.assignments = assignments;
    }

    /**
     * Add an assignment to the student.
     * This method should only be called when creating an assignment for the entire class,
     * not for an individual student.
     *
     * @param assignment the name of the assignment
     * @param grade the grade the student received on the assignment
     */
    public void addAssignment(String assignment, float grade) {
        this.assignments.put(assignment, grade);
    }

    /**
     * Remove the assignment from the student
     * This method should only be called when removing an assignment for the entire class,
     * not for an individual student.
     *
     * @param assignment the assignment to remove from the student
     */
    public void removeAssignment(String assignment) {
        this.assignments.remove(assignment);
    }

    /**
     * Update an assignment name
     * This method should only be called when updating the name of an assignment for the entire class,
     * not for an individual student.
     *
     * @param oldAssignment the old name of the assignment
     * @param newAssignment the new name of the assignment
     */
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
