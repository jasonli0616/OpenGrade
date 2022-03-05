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
        assignment.put(AssignmentAttribute.WEIGHT.attribute, weight);

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
     * Get the student's average knowledge mark in a specified class
     *
     * @param c the class that the assignments belong in
     * @return the average knowledge mark
     */
    public double getKnowledgeAverage(Class c) {
        double average = 0;

        double assignmentsTotalWeight = 0;

        // Add each assignment to average
        for (HashMap<String, Object> assignment : assignments) {

            // Get assignments total weight
            assignmentsTotalWeight += (double) assignment.get(AssignmentAttribute.WEIGHT.attribute);

            // If in specified class
            if ((int) assignment.get(AssignmentAttribute.CLASS_ID.attribute) == c.id) {

                double assignmentWeight = (double) assignment.get(AssignmentAttribute.WEIGHT.attribute);

                // Get mark
                if ((double) assignment.get(AssignmentAttribute.KNOWLEDGE_MARK.attribute) > 0) {
                    double assignmentKnowledge = (double) assignment.get(AssignmentAttribute.KNOWLEDGE_MARK.attribute);
                    average += assignmentKnowledge * assignmentWeight;
                }
            }
        }

        // Adjust with total weight of assignments
        average /= assignmentsTotalWeight;

        return average * 20;
    }

    /**
     * Get the student's average thinking mark in a specified class
     *
     * @param c the class that the assignments belong in
     * @return the average thinking mark
     */
    public double getThinkingAverage(Class c) {
        double average = 0;

        double assignmentsTotalWeight = 0;

        // Add each assignment to average
        for (HashMap<String, Object> assignment : assignments) {

            // Get assignments total weight
            assignmentsTotalWeight += (double) assignment.get(AssignmentAttribute.WEIGHT.attribute);

            // If in specified class
            if ((int) assignment.get(AssignmentAttribute.CLASS_ID.attribute) == c.id) {

                double assignmentWeight = (double) assignment.get(AssignmentAttribute.WEIGHT.attribute);

                // Get mark
                if ((double) assignment.get(AssignmentAttribute.THINKING_MARK.attribute) > 0) {
                    double assignmentThinking = (double) assignment.get(AssignmentAttribute.THINKING_MARK.attribute);
                    average += assignmentThinking * assignmentWeight;
                }
            }
        }

        // Adjust with total weight of assignments
        average /= assignmentsTotalWeight;

        return average * 15;
    }


    /**
     * Get the student's average communication mark in a specified class
     *
     * @param c the class that the assignments belong in
     * @return the average communication mark
     */
    public double getCommunicationAverage(Class c) {
        double average = 0;

        double assignmentsTotalWeight = 0;

        // Add each assignment to average
        for (HashMap<String, Object> assignment : assignments) {

            // Get assignments total weight
            assignmentsTotalWeight += (double) assignment.get(AssignmentAttribute.WEIGHT.attribute);

            // If in specified class
            if ((int) assignment.get(AssignmentAttribute.CLASS_ID.attribute) == c.id) {

                double assignmentWeight = (double) assignment.get(AssignmentAttribute.WEIGHT.attribute);

                // Get mark
                if ((double) assignment.get(AssignmentAttribute.COMMUNICATION_MARK.attribute) > 0) {
                    double assignmentCommunication = (double) assignment.get(AssignmentAttribute.COMMUNICATION_MARK.attribute);
                    average += assignmentCommunication * assignmentWeight;
                }
            }
        }

        // Adjust with total weight of assignments
        average /= assignmentsTotalWeight;

        return average * 15;
    }

    /**
     * Get the student's average thinking mark in a specified class
     *
     * @param c the class that the assignments belong in
     * @return the average thinking mark
     */
    public double getApplicationAverage(Class c) {
        double average = 0;

        double assignmentsTotalWeight = 0;

        // Add each assignment to average
        for (HashMap<String, Object> assignment : assignments) {

            // Get assignments total weight
            assignmentsTotalWeight += (double) assignment.get(AssignmentAttribute.WEIGHT.attribute);

            // If in specified class
            if ((int) assignment.get(AssignmentAttribute.CLASS_ID.attribute) == c.id) {

                double assignmentWeight = (double) assignment.get(AssignmentAttribute.WEIGHT.attribute);

                // Get mark
                if ((double) assignment.get(AssignmentAttribute.APPLICATION_MARK.attribute) > 0) {
                    double assignmentApplication = (double) assignment.get(AssignmentAttribute.APPLICATION_MARK.attribute);
                    average += assignmentApplication * assignmentWeight;
                }
            }
        }

        // Adjust with total weight of assignments
        average /= assignmentsTotalWeight;

        return average * 20;
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
        // Return the average from all strands
        return (this.getKnowledgeAverage(c) + this.getThinkingAverage(c) + this.getCommunicationAverage(c) + this.getApplicationAverage(c)) / 70;
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