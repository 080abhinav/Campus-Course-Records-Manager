package edu.ccrm.service;

import edu.ccrm.config.DataStore;
import edu.ccrm.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EnrollmentService {

    private static final int MAX_CREDITS_PER_SEMESTER = 18;
    private final DataStore dataStore = DataStore.getInstance();

    public void enrollStudent(String regNo, String courseCode)
            throws MaxCreditLimitExceededException, DuplicateEnrollmentException {

        Student student = dataStore.students.get(regNo);
        Course course = dataStore.courses.get(courseCode);

        if (student == null || course == null) {
            System.out.println("Error: Invalid student registration number or course code.");
            return;
        }

        // Rule 1: Check for duplicate enrollment
        boolean alreadyEnrolled = student.getEnrolledCourses().stream()
                .anyMatch(e -> e.getCourseCode().equals(courseCode));
        if (alreadyEnrolled) {
            throw new DuplicateEnrollmentException("Student " + regNo + " is already enrolled in course " + courseCode);
        }

        // Rule 2: Check for max credit limit
        int currentCredits = student.getEnrolledCourses().stream()
                .map(e -> dataStore.courses.get(e.getCourseCode()))
                .filter(c -> c != null && c.getSemester() == course.getSemester())
                .mapToInt(Course::getCredits)
                .sum();

        if (currentCredits + course.getCredits() > MAX_CREDITS_PER_SEMESTER) {
            throw new MaxCreditLimitExceededException("Enrollment failed. Exceeds max credit limit of "
                    + MAX_CREDITS_PER_SEMESTER + " for the semester.");
        }

        Enrollment enrollment = new Enrollment(student.getId(), course.getCode());
        student.addEnrollment(enrollment);
        dataStore.enrollments.put(student.getId() + "_" + course.getCode(), enrollment);
        System.out.println("Enrollment successful.");
    }

    public void assignGrade(String regNo, String courseCode, Grade grade) {
        Student student = dataStore.students.get(regNo);
        if (student == null) {
            System.out.println("Error: Student not found.");
            return;
        }

        Optional<Enrollment> enrollmentOpt = student.getEnrolledCourses().stream()
                .filter(e -> e.getCourseCode().equals(courseCode))
                .findFirst();

        if (enrollmentOpt.isPresent()) {
            enrollmentOpt.get().setGrade(grade);
            System.out.println("Grade assigned successfully.");
        } else {
            System.out.println("Error: Student is not enrolled in this course.");
        }
    }

    public void printTranscript(String regNo) {

        System.out.println("Transcript printing not yet implemented for student: " + regNo);
    }
    // Existing methods and fields

    public void recordGrade(String regNo, String courseCode, Grade grade) {

        System.out.println("Grade " + grade + " recorded for student " + regNo + " in course " + courseCode);
    }

    public double calculateGpa(String regNo) {
        Student student = dataStore.students.get(regNo);
        if (student == null)
            return 0.0;

        List<Enrollment> gradedEnrollments = student.getEnrolledCourses().stream()
                .filter(e -> e.getGrade().getGradePoints() >= 0)
                .collect(Collectors.toList());

        if (gradedEnrollments.isEmpty())
            return 0.0;

        double totalGradePoints = 0.0;
        int totalCredits = 0;

        for (Enrollment enrollment : gradedEnrollments) {
            Course course = dataStore.courses.get(enrollment.getCourseCode());
            if (course != null) {
                totalGradePoints += enrollment.getGrade().getGradePoints() * course.getCredits();
                totalCredits += course.getCredits();
            }
        }

        return (totalCredits == 0) ? 0.0 : totalGradePoints / totalCredits;
    }

    public String generateTranscript(String regNo) {
        Optional<Student> studentOpt = Optional.ofNullable(dataStore.students.get(regNo));
        if (studentOpt.isEmpty()) {
            return "Transcript Generation Failed: Student not found.";
        }

        Student student = studentOpt.get();
        StringBuilder transcript = new StringBuilder();
        transcript.append("========================================\n");
        transcript.append("      ACADEMIC TRANSCRIPT\n");
        transcript.append("========================================\n");
        transcript.append(String.format("Student Name: %s\n", student.getFullName()));
        transcript.append(String.format("Reg. Number: %s\n", student.getRegNo()));
        transcript.append("----------------------------------------\n");
        transcript.append(String.format("%-10s %-30s %-8s %-5s\n", "Code", "Course Title", "Credits", "Grade"));
        transcript.append("----------------------------------------\n");

        // This loop demonstrates polymorphism. The `toString()` method of each object
        // (Course, Grade) is called implicitly to build the string.
        for (Enrollment enrollment : student.getEnrolledCourses()) {
            Course course = dataStore.courses.get(enrollment.getCourseCode());
            if (course != null) {
                transcript.append(String.format("%-10s %-30s %-8d %-5s\n",
                        course.getCode(),
                        course.getTitle(),
                        course.getCredits(),
                        enrollment.getGrade().name()));
            }
        }

        transcript.append("----------------------------------------\n");
        transcript.append(String.format("Cumulative GPA: %.2f\n", calculateGpa(regNo)));
        transcript.append("========================================\n");

        return transcript.toString();
    }
}