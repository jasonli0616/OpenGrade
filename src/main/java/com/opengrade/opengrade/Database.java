package com.opengrade.opengrade;

import com.opengrade.opengrade.models.AssignmentAttribute;
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
                + "                      id           INTEGER PRIMARY KEY,"
                + "                      student_name TEXT NOT NULL"
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
                + "                         knowledge_mark      REAL,"
                + "                         thinking_mark       REAL,"
                + "                         communication_mark  REAL,"
                + "                         application_mark    REAL,"
                + "                         weight              REAL,"
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
//            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
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
     * @return        the id of the student in the database
     */
    public static int insertStudent(Student student) {
        String query = "INSERT INTO students (student_name)"
                + "         VALUES(?)";

        Connection conn = connect();

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, student.fullName);

            pstmt.executeUpdate();

            int studentID = pstmt.getGeneratedKeys().getInt(1);

            conn.close();
            return studentID;
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * Insert an assignment into the database.
     *
     * @param student           the student who completed this assignment
     * @param c                 the class that this assignment was completed in
     * @param assignmentName    the name of the assignment
     * @param knowledgeMark     knowledge mark - 20
     * @param thinkingMark      thinking mark - 15
     * @param communicationMark communication mark - 15
     * @param applicationMark   application mark - 15
     * @param weight            the weight of the assignment
     */
    public static void insertStudentAssignment(Student student, Class c, String assignmentName, double knowledgeMark, double thinkingMark, double communicationMark, double applicationMark, double weight) {
        String query = "INSERT INTO assignments (assignment_name, knowledge_mark, thinking_mark, communication_mark, application_mark, weight, student_id, class_id)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = connect();

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, assignmentName);
            pstmt.setDouble(2, knowledgeMark);
            pstmt.setDouble(3, thinkingMark);
            pstmt.setDouble(4, communicationMark);
            pstmt.setDouble(5, applicationMark);
            pstmt.setDouble(6, weight);
            pstmt.setInt(7, student.id);
            pstmt.setInt(8, c.id);

            pstmt.executeUpdate();

            conn.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Get all assignments of a student.
     *
     * @param student the student that the assignments belong to
     * @return ArrayList of the student's assignments
     */
    public static ArrayList<HashMap<String, Object>> getStudentAssignments(Student student) {
        ArrayList<HashMap<String, Object>> assignments = new ArrayList<HashMap<String, Object>>();

        String query = "SELECT * FROM assignments"
                + "         WHERE student_id = ?";

        Connection conn = connect();

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, student.id);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                HashMap<String, Object> assignment = new HashMap<String, Object>();
                assignment.put(AssignmentAttribute.CLASS_ID.attribute, rs.getInt(AssignmentAttribute.CLASS_ID.attribute));
                assignment.put(AssignmentAttribute.ASSIGNMENT_NAME.attribute, rs.getString(AssignmentAttribute.ASSIGNMENT_NAME.attribute));
                assignment.put(AssignmentAttribute.KNOWLEDGE_MARK.attribute, rs.getDouble(AssignmentAttribute.KNOWLEDGE_MARK.attribute));
                assignment.put(AssignmentAttribute.THINKING_MARK.attribute, rs.getDouble(AssignmentAttribute.THINKING_MARK.attribute));
                assignment.put(AssignmentAttribute.COMMUNICATION_MARK.attribute, rs.getDouble(AssignmentAttribute.COMMUNICATION_MARK.attribute));
                assignment.put(AssignmentAttribute.APPLICATION_MARK.attribute, rs.getDouble(AssignmentAttribute.APPLICATION_MARK.attribute));
                assignment.put(AssignmentAttribute.WEIGHT.attribute, rs.getDouble(AssignmentAttribute.WEIGHT.attribute));

                assignments.add(assignment);
            }

            conn.close();
            return assignments;
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            exception.printStackTrace();
        }
        return null;
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
                    assignment.put(AssignmentAttribute.CLASS_ID.attribute, getAssignmentRs.getInt(AssignmentAttribute.CLASS_ID.attribute));
                    assignment.put(AssignmentAttribute.ASSIGNMENT_NAME.attribute, getAssignmentRs.getString(AssignmentAttribute.ASSIGNMENT_NAME.attribute));
                    assignment.put(AssignmentAttribute.KNOWLEDGE_MARK.attribute, getAssignmentRs.getDouble(AssignmentAttribute.KNOWLEDGE_MARK.attribute));
                    assignment.put(AssignmentAttribute.THINKING_MARK.attribute, getAssignmentRs.getDouble(AssignmentAttribute.THINKING_MARK.attribute));
                    assignment.put(AssignmentAttribute.COMMUNICATION_MARK.attribute, getAssignmentRs.getDouble(AssignmentAttribute.COMMUNICATION_MARK.attribute));
                    assignment.put(AssignmentAttribute.APPLICATION_MARK.attribute, getAssignmentRs.getDouble(AssignmentAttribute.APPLICATION_MARK.attribute));
                    assignment.put(AssignmentAttribute.WEIGHT.attribute, getAssignmentRs.getDouble(AssignmentAttribute.WEIGHT.attribute));

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
     * Insert an existing student into an existing class.
     *
     * @param s the student to insert into the class
     * @param c the class that the student will be inserted into
     */
    public static void associateStudentClass(Student s, Class c) {
        String query = "INSERT INTO associate_student_class (student_id, class_id)"
                + "     VALUES (?,?)";

        Connection conn = connect();

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, s.id);
            pstmt.setInt(2, c.id);
            pstmt.executeUpdate();

            conn.close();
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            exception.printStackTrace();
        }
    }

    public static void unassociateStudentClass(Student s, Class c) {
        String query = "DELETE FROM associate_student_class"
                + "     WHERE student_id = ?"
                + "     AND class_id = ?";

        Connection conn = connect();

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, s.id);
            pstmt.setInt(2, c.id);
            pstmt.executeUpdate();

            conn.close();
        } catch (SQLException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
            exception.printStackTrace();
        }
    }

    /**
     * Inserts a class into the database.
     *
     * @param c the class to insert
     * @return the id of the class in the database
     */
    public static int insertClass(Class c) {
        String query = "INSERT INTO classes (class_name)"
                + "     VALUES (?)";

        Connection conn = connect();

        try {
            // Insert class
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, c.className);
            pstmt.executeUpdate();
            c.id = pstmt.getGeneratedKeys().getInt(1);

            // For every student, associate class
            for (Student s : c.students) {
                associateStudentClass(s, c);
            }

            conn.close();

            return c.id;
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