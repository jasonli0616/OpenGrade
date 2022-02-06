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
    public static DB connect() {
        return DBMaker.fileDB("site.db").make();
    }

    public static Class selectClass(String className) {
        DB db = connect();
        ConcurrentMap<String, HashMap<String, HashMap<String, Float>>> map = (ConcurrentMap<String, HashMap<String, HashMap<String, Float>>>) db.hashMap("map").createOrOpen();
        HashMap<String, HashMap<String, Float>> classMap = map.get(className);

        db.close();

        return Class.mapToClass(classMap, className);
    }

    public static void insertClass(Class c) {
        DB db = connect();
        ConcurrentMap<String, HashMap<String, HashMap<String, Float>>> map = (ConcurrentMap<String, HashMap<String, HashMap<String, Float>>>) db.hashMap("map").createOrOpen();
        map.put(c.className, c.toMap());

        db.close();
    }

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
