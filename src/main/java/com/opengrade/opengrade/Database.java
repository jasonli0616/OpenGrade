package com.opengrade.opengrade;

import com.opengrade.opengrade.models.Class;
import com.opengrade.opengrade.models.Student;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class Database {

    /**
     * Connects (and create if not exists) to and returns the database.
     *
     * @return the database itself
     */
    public static DB connect() {
        return DBMaker.fileDB("site.db").make();
    }

    /**
     * Inserts a class into the database.
     *
     * @param c the class to insert
     */
    public static void insertClass(Class c) {
        DB db = connect();
        ConcurrentMap<String, HashMap<String, HashMap<String, Float>>> map = (ConcurrentMap<String, HashMap<String, HashMap<String, Float>>>) db.hashMap("map").createOrOpen();
        map.put(c.className, c.toMap());

        db.close();
    }

    /**
     * Search and returns whether a class exists in the database (by name).
     *
     * @param className the name of the class
     * @return whether the class exists
     */
    public static boolean classExists(String className) {
        ArrayList<Class> classes = getAllClasses();

        for (Class c : classes) {
            if (c.className.equals(className))
                return true;
        }

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
        ArrayList<Class> classes = getAllClasses();

        for (Class c : classes) {
            if (c.className.equals(className)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Query the database for all classes.
     *
     * @return ArrayList of all classes
     */
    public static ArrayList<Class> getAllClasses() {
        DB db = connect();
        ConcurrentMap<String, HashMap<String, HashMap<String, Float>>> map = (ConcurrentMap<String, HashMap<String, HashMap<String, Float>>>) db.hashMap("map").createOrOpen();

        ArrayList<Class> classes = new ArrayList<Class>();

        for (Map.Entry<String, HashMap<String, HashMap<String, Float>>> entry : map.entrySet()) {
            String className = entry.getKey();
            HashMap<String, HashMap<String, Float>> classMap = entry.getValue();

            Class c = Class.mapToClass(classMap, className);
            classes.add(c);
        }
        db.close();

        return classes;
    }
}
