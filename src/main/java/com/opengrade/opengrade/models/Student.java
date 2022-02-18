package com.opengrade.opengrade.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Student {
    public int id;

    public String fullName;

    public ArrayList<HashMap<String, Object>> assignments = new ArrayList<HashMap<String, Object>>();

    public Student(String fullName) {
        this.fullName = fullName;
    }

    public Student(String fullName, ArrayList<HashMap<String, Object>> assignments) {
        this.fullName = fullName;
        this.assignments = assignments;
    }

    /**
     * Add an assignment to the student, and insert into the database.
     *
     * @param c the class that this assignment is part of
     * @param assignmentName the name of the assignment
     * @param knowledgeGrade strand: knowledge
     * @param thinkingGrade strand: thinking
     * @param communicationGrade strand: communication
     * @param applicationGrade strand: application
     */
    public void addAssignment(Class c, String assignmentName, float knowledgeMark, float thinkingMark, float communicationMark, float applicationMark) {
        HashMap<String, Object> assignment = new HashMap<String, Object>();
        assignment.put("class_id", c.id);
        assignment.put("assignment_name", assignmentName);
        assignment.put("knowledge_mark", knowledgeMark);
        assignment.put("thinking_mark", thinkingMark);
        assignment.put("communication_mark", communicationMark);
        assignment.put("application_mark", applicationMark);

        this.assignments.add(assignment);

        // TODO: Insert into database
    }

    /**
     * Remove the assignment from the student
     * This method should only be called when removing an assignment for the entire class,
     * not for an individual student.
     *
     * @param assignment the assignment to remove from the student
     */
    public void removeAssignment(String assignment) {
        // TODO
    }

    @Override
    public String toString() {
        return this.fullName;
    }
}