package com.opengrade.opengrade;

import com.opengrade.opengrade.models.Class;
import com.opengrade.opengrade.models.Student;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {

    /**
     * Connects (and create if not exists) to and returns the database.
     *
     * @return the database itself
     */
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:site.db");
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            exception.printStackTrace();
        }
        return conn;
    }

    /**
     * Create database tables, if they do not already exist.
     */
    public static void createTables() {
        String classesTableQuery = "CREATE TABLE IF NOT EXISTS classes ("
                + "                     id          INTEGER PRIMARY KEY,"
                + "                     class_name  TEXT NOT NULL"
                + "                 )";

        String studentsTableQuery = "CREATE TABLE IF NOT EXISTS students ("
                + "                      id          INTEGER PRIMARY KEY,"
                + "                      student_name  TEXT NOT NULL"
                + "                  )";

        String associateStudentClassTableQuery = "CREATE TABLE IF NOT EXISTS associate_student_class ("
                + "                                   student_id  INTEGER NOT NULL,"
                + "                                   class_id    INTEGER NOT NULL,"
                + "                                   FOREIGN KEY (student_id) REFERENCES students(id),"
                + "                                   FOREIGN KEY (class_id) REFERENCES classes(id)"
                + "                               )";

        String assignmentsTableQuery = "CREATE TABLE IF NOT EXISTS assignments ("
                + "                         id                  INTEGER PRIMARY KEY,"
                + "                         assignment_name     TEXT NOT NULL,"
                + "                         knowledge_mark      INTEGER,"
                + "                         thinking_mark       INTEGER,"
                + "                         communication_mark  INTEGER,"
                + "                         application_mark    INTEGER,"
                + "                         student_id          INTEGER NOT NULL,"
                + "                         class_id            INTEGER NOT NULL,"
                + "                         FOREIGN KEY (student_id) REFERENCES students(id),"
                + "                         FOREIGN KEY (class_id) REFERENCES classes(id)"
                + "                     )";

        Connection conn = connect();

        try {
            Statement stmt = conn.createStatement();

            stmt.execute(classesTableQuery);
            stmt.execute(studentsTableQuery);
            stmt.execute(associateStudentClassTableQuery);
            stmt.execute(assignmentsTableQuery);

            conn.close();
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            exception.printStackTrace();
        }
    }

    /**
     * Search the database for all students.
     *
     * @return ArrayList of students
     */
    public static ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<Student>();
        String query = "SELECT * FROM students";

        Connection conn = connect();

        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while (result.next()) {
                Student student = new Student(result.getString("student_name"));
                student.id = result.getInt("id");

                students.add(student);
            }

            conn.close();
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            exception.printStackTrace();
        }

        return students;
    }

    /**
     * Inserts a student into the database.
     *
     * @param student the student to insert
     * @return the id of the student in the database
     */
    public static int insertStudent(Student student) {
        String query = "INSERT INTO students (student_name)"
                + "         VALUES(?)";

        Connection conn = connect();

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, student.fullName);

            pstmt.executeUpdate();

            conn.close();
            return pstmt.getGeneratedKeys().getInt(1);
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * Find and return all the students in a class.
     *
     * @param c the class that the students are in
     * @return ArrayList of the students in the class
     */
    public static ArrayList<Student> getStudentsFromClass(Class c) {
        ArrayList<Student> students = new ArrayList<Student>();

        String getAssociationQuery = "SELECT * FROM associate_student_class"
                + "                       WHERE class_id = ?";

        String getStudentQuery = "SELECT * FROM students"
                + "                   WHERE id = ?";

        String getAssignmentQuery = "SELECT * FROM assignments"
                + "                      WHERE student_id = ?"
                + "                      AND class_id = ?";

        Connection conn = connect();

        try {
            // Get student IDs from association table
            PreparedStatement getAssociationPstmt = conn.prepareStatement(getAssociationQuery);
            getAssociationPstmt.setInt(1, c.id);
            ResultSet getAssociationRs = getAssociationPstmt.executeQuery();

            while (getAssociationRs.next()) {
                // Search student table, using IDs from association table
                PreparedStatement getStudentPstmt = conn.prepareStatement(getStudentQuery);
                getStudentPstmt.setInt(1, getAssociationRs.getInt("student_id"));
                ResultSet getStudentRs = getStudentPstmt.executeQuery();
                while (getStudentRs.next()) {
                    Student s = new Student(getStudentRs.getString("student_name"));
                    s.id = getStudentRs.getInt("id");
                    students.add(s);
                }
            }

            // Get student assignments
            for (Student s : students) {
                PreparedStatement getAssignmentPstmt = conn.prepareStatement(getAssignmentQuery);
                getAssignmentPstmt.setInt(1, s.id);
                getAssignmentPstmt.setInt(2, c.id);
                ResultSet getAssignmentRs = getAssignmentPstmt.executeQuery();
                while (getAssignmentRs.next()) {
                    // Add assignment to student
                    HashMap<String, Object> assignment = new HashMap<String, Object>();
                    assignment.put("class_id", c.id);
                    assignment.put("assignment_name", getAssignmentRs.getString("assignment_name"));
                    assignment.put("knowledge_mark", getAssignmentRs.getInt("knowledge_mark"));
                    assignment.put("thinking_mark", getAssignmentRs.getInt("thinking_mark"));
                    assignment.put("communication_mark", getAssignmentRs.getInt("communication_mark"));
                    assignment.put("application_mark", getAssignmentRs.getInt("application_mark"));

                    s.assignments.add(assignment);
                }
            }

            conn.close();
            return students;
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            exception.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Class> getAllClasses() {
        ArrayList<Class> classes = new ArrayList<Class>();
        String selectClassQuery = "SELECT * FROM classes";

        Connection conn = connect();


        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(selectClassQuery);

            while (result.next()) {
                Class c = new Class(result.getString("class_name"));
                c.id = result.getInt("id");
                c.students = getStudentsFromClass(c);

                classes.add(c);
            }

            conn.close();
            return classes;
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            exception.printStackTrace();
        }

        return null;
    }

    /**
     * Inserts a class into the database.
     *
     * @param c the class to insert
     * @returns the id of the class in the database
     */
    public static int insertClass(Class c) {
        String insertClassQuery = "INSERT INTO classes (class_name)"
                + "         VALUES (?)";

        String associateStudentClassQuery = "INSERT INTO associate_student_class (student_id, class_id)"
                + "                             VALUES (?,?)";

        Connection conn = connect();

        try {
            // Insert class
            PreparedStatement insertClassPstmt = conn.prepareStatement(insertClassQuery);
            insertClassPstmt.setString(1, c.className);
            insertClassPstmt.executeUpdate();
            c.id = insertClassPstmt.getGeneratedKeys().getInt(1);

            // For every student, associate class
            PreparedStatement associateStudentClassPstmt = conn.prepareStatement(associateStudentClassQuery);
            for (Student s : c.students) {
                associateStudentClassPstmt.setInt(1, s.id);
                associateStudentClassPstmt.setInt(2, c.id);
                associateStudentClassPstmt.executeUpdate();
            }

            conn.close();
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            exception.printStackTrace();
        }
        return 0;
    }

    public static void deleteClass(Class c) {
        String deleteClassQuery = "DELETE FROM classes"
                + "                    WHERE id = ?";

        String deleteAssociateStudentClassQuery = "DELETE FROM associate_student_class"
                + "                                    WHERE class_id = ?";

        Connection conn = connect();

        try {
            PreparedStatement deleteClassPstmt = conn.prepareStatement(deleteClassQuery);
            PreparedStatement deleteAssociateStudentClassPstmt = conn.prepareStatement(deleteAssociateStudentClassQuery);
            deleteClassPstmt.setInt(1, c.id);
            deleteAssociateStudentClassPstmt.setInt(1, c.id);
            deleteClassPstmt.executeUpdate();
            deleteAssociateStudentClassPstmt.executeUpdate();

            conn.close();
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            exception.printStackTrace();
        }
    }
}