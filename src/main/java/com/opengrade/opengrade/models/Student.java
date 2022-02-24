package com.opengrade.opengrade.models;

import com.opengrade.opengrade.Database;

import java.text.DecimalFormat;
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
     * If a strand is not applicable
     * (e.g. if communication does not count for this assignment),
     * the program will remember the grade as -1.
     * -1 will be ignored by the program when calculating and displaying grades.
     *
     * @param c                 the class that this assignment is part of
     * @param assignmentName    the name of the assignment
     * @param knowledgeMark     strand: knowledge - 20
     * @param thinkingMark      strand: thinking - 15
     * @param communicationMark strand: communication - 15
     * @param applicationMark   strand: application - 20
     */
    public void addAssignment(Class c, String assignmentName, double knowledgeMark, double thinkingMark, double communicationMark, double applicationMark, double weight) {
        HashMap<String, Object> assignment = new HashMap<String, Object>();
        assignment.put(AssignmentAttribute.CLASS_ID.attribute, c.id);
        assignment.put(AssignmentAttribute.ASSIGNMENT_NAME.attribute, assignmentName);
        assignment.put(AssignmentAttribute.KNOWLEDGE_MARK.attribute, knowledgeMark);
        assignment.put(AssignmentAttribute.THINKING_MARK.attribute, thinkingMark);
        assignment.put(AssignmentAttribute.COMMUNICATION_MARK.attribute, communicationMark);
        assignment.put(AssignmentAttribute.APPLICATION_MARK.attribute, applicationMark);

        this.assignments.add(assignment);

        Database.insertStudentAssignment(this, c, assignmentName, knowledgeMark, thinkingMark, communicationMark, applicationMark, weight);
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

    /**
     * Get the student's average in a specified class
     * Knowledge- 20
     * Thinking- 15
     * Communication- 15
     * Application- 20
     *
     * @param c the class that the assignments belong in
     * @return  the average mark
     */
    public double getAverage(Class c) {
        double knowledgeAverage = 0;
        double thinkingAverage = 0;
        double communicationAverage = 0;
        double applicationAverage = 0;

        // Amount of assignments that has the specified strand
        // E.g. if an assignment doesn't count in one strand, it won't count as 0%
        int knowledgeAssignmentsLength = 0;
        int thinkingAssignmentsLength = 0;
        int communicationAssignmentsLength = 0;
        int applicationAssignmentsLength = 0;

        // Get assignments total weight
        double assignmentsTotalWeight = 0;
        for (HashMap<String, Object> assignment : assignments) {
            assignmentsTotalWeight += (double) assignment.get(AssignmentAttribute.WEIGHT.attribute);
        }


        // Add each assignment to average
        for (HashMap<String, Object> assignment : assignments) {
            // If in specified class
            if ((int) assignment.get(AssignmentAttribute.CLASS_ID.attribute) == c.id) {

                double assignmentWeight = (double) assignment.get(AssignmentAttribute.WEIGHT.attribute);

                // Knowledge mark
                if ((double) assignment.get(AssignmentAttribute.KNOWLEDGE_MARK.attribute) > 0) {
                    double assignmentKnowledge = (double) assignment.get(AssignmentAttribute.KNOWLEDGE_MARK.attribute);
                    knowledgeAverage += assignmentKnowledge * assignmentWeight;
                    knowledgeAssignmentsLength += 1;
                }

                // Thinking mark
                if ((double) assignment.get(AssignmentAttribute.THINKING_MARK.attribute) > 0) {
                    double assignmentThinking = (double) assignment.get(AssignmentAttribute.THINKING_MARK.attribute);
                    thinkingAverage += assignmentThinking * assignmentWeight;
                    thinkingAssignmentsLength += 1;
                }

                // Communication mark
                if ((double) assignment.get(AssignmentAttribute.COMMUNICATION_MARK.attribute) > 0) {
                    double assignmentCommunication = (double) assignment.get(AssignmentAttribute.COMMUNICATION_MARK.attribute);
                    communicationAverage += assignmentCommunication * assignmentWeight;
                    communicationAssignmentsLength += 1;
                }

                // Application mark
                if ((double) assignment.get(AssignmentAttribute.APPLICATION_MARK.attribute) > 0) {
                    double assignmentApplication = (double) assignment.get(AssignmentAttribute.APPLICATION_MARK.attribute);
                    applicationAverage += assignmentApplication * assignmentWeight;
                    applicationAssignmentsLength += 1;
                }

            }
        }

        // Adjust with total weight of assignments
        knowledgeAverage /= assignmentsTotalWeight;
        thinkingAverage /= assignmentsTotalWeight;
        communicationAverage /= assignmentsTotalWeight;
        applicationAverage /= assignmentsTotalWeight;

        return (knowledgeAverage * 20 + thinkingAverage * 15 + communicationAverage * 15 + applicationAverage * 20) / 70;
    }

    @Override
    public String toString() {
        return this.fullName;
    }

    @Override
    public boolean equals(Object o) {
        try {
            return ((Student) o).id == this.id;
        } catch (Exception ignored) {}
        return false;
    }
}