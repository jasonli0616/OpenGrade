package com.opengrade.opengrade.models;

public enum AssignmentAttribute {
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
}