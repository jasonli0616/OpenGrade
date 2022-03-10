package com.opengrade.opengrade.models;

public enum AssignmentAttribute {
    /**
     * This enum is essentially just a holder for
     * constant strings that are used for the assignment
     * attributes.
     */

    ASSIGNMENT_NAME ("assignment_name"),
    KNOWLEDGE_MARK ("knowledge_mark"),
    THINKING_MARK ("thinking_mark"),
    COMMUNICATION_MARK ("communication_mark"),
    APPLICATION_MARK ("application_mark"),
    WEIGHT ("weight"),
    CLASS_ID ("class_id"),
    STUDENT_ID ("student_id");

    public final String attribute;

    AssignmentAttribute(String attribute) {
        this.attribute = attribute;
    }

    /**
     * Check if a mark is a valid input.
     * -1 to 100
     * -1 represents no mark
     *
     * @param mark the mark input
     * @return whether the mark is valid
     */
    public static boolean markIsValid(double mark) {
        return (mark >= -1 && mark <= 100);
    }
}