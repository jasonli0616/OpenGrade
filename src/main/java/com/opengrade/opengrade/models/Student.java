package com.opengrade.opengrade.models;

import com.opengrade.opengrade.Database;

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
     * Return the student's average in a class, for a subject strand.
     *
     * @param c      the class that the student is in
     * @param strand the strand to calculate
     * @return the student's average in a strand
     */
    public double getStrandAverage(Class c, AssignmentAttribute strand) {
        double average = -1;

        double assignmentsTotalWeight = 0;

        // Add each assignment to average
        for (HashMap<String, Object> assignment : assignments) {

            // Get assignments total weight
            assignmentsTotalWeight += (double) assignment.get(AssignmentAttribute.WEIGHT.attribute);

            // If in specified class
            if ((int) assignment.get(AssignmentAttribute.CLASS_ID.attribute) == c.id) {

                double assignmentWeight = (double) assignment.get(AssignmentAttribute.WEIGHT.attribute);

                // Get mark
                if ((double) assignment.get(strand.attribute) >= 0) {
                    double assignmentStrand = (double) assignment.get(strand.attribute);
                    average = assignmentStrand * assignmentWeight;
                }
            }
        }

        // Adjust with total weight of assignments
        if (average != -1)
            average /= assignmentsTotalWeight;

        return average;
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
        double knowledgeAverage = this.getStrandAverage(c, AssignmentAttribute.KNOWLEDGE_MARK);
        double thinkingAverage = this.getStrandAverage(c, AssignmentAttribute.THINKING_MARK);
        double communicationMark = this.getStrandAverage(c, AssignmentAttribute.COMMUNICATION_MARK);
        double applicationMark = this.getStrandAverage(c, AssignmentAttribute.APPLICATION_MARK);

        return getAverage(knowledgeAverage, thinkingAverage, communicationMark, applicationMark);
    }

    /**
     * Static method to get the average of K, T, C, A.
     * Knowledge- 20
     * Thinking- 15
     * Communication- 15
     * Application- 20
     *
     *
     * @param knowledge     knowledge mark of the assignment
     * @param thinking      thinking mark of the assignment
     * @param communication communication mark of the assignment
     * @param application   application mark of the assignment
     * @return the calculated and weighted strand average
     */
    public static double getAverage(double knowledge, double thinking, double communication, double application) {
        double strandWeights = 0;

        if (knowledge > -1)
            strandWeights += 20;
        else
            knowledge = 0;

        if (thinking > -1)
            strandWeights += 15;
        else
            thinking = 0;

        if (communication > -1)
            strandWeights += 15;
        else
            communication = 0;

        if (application > -1)
            strandWeights += 20;
        else
            application = 0;

        if (strandWeights == 0)
            return -1;

        // Return the average from all strands
        return (
                (knowledge * 20) + (thinking * 15) + (communication * 15) + (application * 20)
        ) / strandWeights;
    }

    /**
     * Change the name of the student,
     * as the object and in the database.
     *
     * @param newName the student's new name
     */
    public void changeName(String newName) {
        this.fullName = newName;
        Database.editStudentName(this, newName);
    }

    /**
     * Student toString() will return the name of the student.
     *
     * @return the student's name
     */
    @Override
    public String toString() {
        return this.fullName;
    }

    /**
     * Check if two students are the same, using the SQL id.
     *
     * @param o the other student
     * @return whether they are the same student
     */
    @Override
    public boolean equals(Object o) {
        try {
            return ((Student) o).id == this.id;
        } catch (Exception ignored) {}
        return false;
    }
}