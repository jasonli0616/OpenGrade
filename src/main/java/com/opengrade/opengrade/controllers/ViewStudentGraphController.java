package com.opengrade.opengrade.controllers;

import com.opengrade.opengrade.models.AssignmentAttribute;
import com.opengrade.opengrade.models.Class;
import com.opengrade.opengrade.models.Student;
import javafx.scene.chart.*;

import java.util.HashMap;

public class ViewStudentGraphController {
    Student student;
    Class c;

    public ViewStudentGraphController(Student student, Class c) {
        this.student = student;
        this.c = c;
    }

    private void displayAssignmentGraph() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(0, 100, 1);
        LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);

        xAxis.setLabel("Assignment");
        yAxis.setLabel("Grade");

        XYChart.Series<String, Number> knowledgeSeries = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> thinkingSeries = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> communicationSeries = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> applicationSeries = new XYChart.Series<String, Number>();

        for (HashMap<String, Object> assignment : this.student.assignments) {
            String assignmentName = (String) assignment.get(AssignmentAttribute.ASSIGNMENT_NAME.attribute);
            double knowledgeMark = (Double) assignment.get(AssignmentAttribute.KNOWLEDGE_MARK.attribute);
            double thinkingMark = (Double) assignment.get(AssignmentAttribute.THINKING_MARK.attribute);
            double communcationMark = (Double) assignment.get(AssignmentAttribute.COMMUNICATION_MARK.attribute);
            double applicationMark = (Double) assignment.get(AssignmentAttribute.APPLICATION_MARK.attribute);

            if (knowledgeMark > 0)
                knowledgeSeries.getData().add(new XYChart.Data<String, Number>(assignmentName, knowledgeMark));
            if (thinkingMark > 0)
                thinkingSeries.getData().add(new XYChart.Data<String, Number>(assignmentName, thinkingMark));
            if (communcationMark > 0)
                knowledgeSeries.getData().add(new XYChart.Data<String, Number>(assignmentName, communcationMark));
            if (applicationMark > 0)
                knowledgeSeries.getData().add(new XYChart.Data<String, Number>(assignmentName, applicationMark));
        }

        lineChart.getData().addAll(knowledgeSeries, thinkingSeries, communicationSeries, applicationSeries);
    }
}