package edu.ccrm.domain;

import java.time.LocalDate;

public class Enrollment {

    private final String studentId;
    private final String courseCode;
    private Grade grade;
    private final LocalDate enrollmentDate;

    public Enrollment(String studentId, String courseCode) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.enrollmentDate = LocalDate.now();
        this.grade = Grade.NOT_GRADED; // Default grade
    }

    // Getters
    public String getStudentId() {
        return studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public Grade getGrade() {
        return grade;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    // Setter for grade
    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return String.format("Enrollment",
                studentId, courseCode, grade, enrollmentDate);
    }
}