package com.opengrade.opengrade;

import com.opengrade.opengrade.models.Class;
import com.opengrade.opengrade.models.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

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
        } catch (SQLException exception) {
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

        } catch (SQLException exception) {
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
            return pstmt.getGeneratedKeys().getInt(1);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return 0;
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
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static void deleteClass(Class c) {
//        DB db = connect();
//        ConcurrentMap<String, HashMap<String, HashMap<String, Float>>> map = (ConcurrentMap<String, HashMap<String, HashMap<String, Float>>>) db.hashMap("map").createOrOpen();
//        map.remove(c.className);
//        db.close();
    }

    /**
     * Search and returns whether a class exists in the database (by name).
     *
     * @param className the name of the class
     * @return whether the class exists
     */
    public static boolean classExists(String className) {
//        ArrayList<Class> classes = getAllClasses();
//
//        for (Class c : classes) {
//            if (c.className.equals(className))
//                return true;
//        }

        return false;
    }

    /**
     * Search and return a class in the database.
     * This should only be called when you know 100% that this class exists. (see classExists())
     *
     * @param className the name of the class
     * @return the class itself
     */
    public static Class findClass(String className) {
//        ArrayList<Class> classes = getAllClasses();
//
//        for (Class c : classes) {
//            if (c.className.equals(className)) {
//                return c;
//            }
//        }
        return null;
    }

    /**
     * Query the database for all classes.
     *
     * @return ArrayList of all classes
     */
    public static ArrayList<Class> getAllClasses() {
//        DB db = connect();
//        ConcurrentMap<String, HashMap<String, HashMap<String, Float>>> map = (ConcurrentMap<String, HashMap<String, HashMap<String, Float>>>) db.hashMap("map").createOrOpen();
//
//        ArrayList<Class> classes = new ArrayList<Class>();
//
//        for (Map.Entry<String, HashMap<String, HashMap<String, Float>>> entry : map.entrySet()) {
//            String className = entry.getKey();
//            HashMap<String, HashMap<String, Float>> classMap = entry.getValue();
//
//            Class c = Class.mapToClass(classMap, className);
//            classes.add(c);
//        }
//
//        db.close();
//        return classes;
        return null;
    }
}