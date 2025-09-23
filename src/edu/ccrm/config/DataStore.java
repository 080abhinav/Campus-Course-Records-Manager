package edu.ccrm.config;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Instructor;
import edu.ccrm.domain.Student;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {

    private static DataStore instance;

    // Using ConcurrentHashMap for thread safety, though not strictly necessary for
    // this single-threaded CLI app.
    public final Map<String, Student> students = new ConcurrentHashMap<>(); // Key: regNo
    public final Map<String, Course> courses = new ConcurrentHashMap<>(); // Key: courseCode
    public final Map<String, Instructor> instructors = new ConcurrentHashMap<>(); // Key: instructorId
    public final Map<String, Enrollment> enrollments = new ConcurrentHashMap<>(); // Key: studentId + "_" + courseCode

    // Private constructor to prevent instantiation
    private DataStore() {
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    public void clearAll() {
        students.clear();
        courses.clear();
        instructors.clear();
        enrollments.clear();
    }
}