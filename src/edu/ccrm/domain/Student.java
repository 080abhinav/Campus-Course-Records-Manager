package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Student extends Person {

    private final String regNo;
    private Status status;
    private final List<Enrollment> enrolledCourses;
    private final LocalDate registrationDate;

    public enum Status {
        ACTIVE, INACTIVE, GRADUATED
    }

    public Student(String fullName, String email, String regNo) {
        super(fullName, email);
        this.regNo = regNo;
        this.status = Status.ACTIVE;
        this.enrolledCourses = new ArrayList<>();
        this.registrationDate = LocalDate.now(); // Uses the Date/Time API.
    }

    // Getters and a setter for status
    public String getRegNo() {
        return regNo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Enrollment> getEnrolledCourses() {
        return new ArrayList<>(enrolledCourses); // Defensive copying for encapsulation
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void addEnrollment(Enrollment enrollment) {
        this.enrolledCourses.add(enrollment);
    }

    public void removeEnrollment(Enrollment enrollment) {
        this.enrolledCourses.remove(enrollment);
    }

    public double calculateGpa() {

        double totalPoints = enrolledCourses.stream()
                .filter(e -> e.getGrade() != null)
                .mapToDouble(e -> e.getGrade().getPoints())
                .sum();

        long gradedCoursesCount = enrolledCourses.stream()
                .filter(e -> e.getGrade() != null)
                .count();

        if (gradedCoursesCount == 0) {
            return 0.0;
        }

        // Return GPA rounded to 2 decimal places
        return Math.round((totalPoints / gradedCoursesCount) * 100.0) / 100.0;
    }

    @Override
    public String getProfileSummary() {
        return String.format("Student Profile | RegNo: %s | Name: %s | Status: %s | Enrolled Courses: %d",
                regNo, getFullName(), status, enrolledCourses.size());
    }

    @Override
    public String toString() {
        return getProfileSummary();
    }
}