package edu.ccrm.cli;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Grade;
import edu.ccrm.domain.Semester;
import edu.ccrm.domain.Student;
import edu.ccrm.service.*;
import edu.ccrm.util.BackupService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CliManager {

    private final Scanner scanner = new Scanner(System.in);
    private final StudentService studentService = new StudentServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();
    private final EnrollmentService enrollmentService = new EnrollmentService();
    private final BackupService backupService = new BackupService();

    public void start() {
        while (true) {
            showMainMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    handleStudentManagement();
                    break;
                case 2:
                    handleCourseManagement();
                    break;
                case 3:
                    handleEnrollmentManagement();
                    break;
                case 4:
                    handleFileOperations();
                    break;
                case 5:
                    handleReports();
                    break;
                case 0:

                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleReports() {
        while (true) {
            System.out.println("\n--- View Reports ---");
            System.out.println("1. View Top 3 Students by GPA");
            System.out.println("2. View GPA Distribution");
            System.out.println("0. Return to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            if (choice == 0)
                break;

            switch (choice) {
                case 1:
                    System.out.println("\n--- Top 3 Students ---");
                    List<Student> topStudents = studentService.getTopStudents(3);
                    if (topStudents.isEmpty()) {
                        System.out.println("No student data available to generate a report.");
                    } else {
                        topStudents.forEach(s -> System.out.printf("Reg No: %-10s | Name: %-20s | GPA: %.2f\n",
                                s.getRegNo(), s.getFullName(), s.calculateGpa()));
                    }
                    break;
                case 2:
                    System.out.println("\n--- Grade Distribution ---");
                    // This stream pipeline calculates how many students received each grade
                    Map<Grade, Long> gradeDistribution = studentService.getAllStudents().stream()
                            .flatMap(student -> student.getEnrolledCourses().stream()) // Use getEnrolledCourses
                            .filter(enrollment -> enrollment.getGrade() != null)
                            .collect(Collectors.groupingBy(
                                    enrollment -> enrollment.getGrade(),
                                    Collectors.counting()));

                    if (gradeDistribution.isEmpty()) {
                        System.out.println("No grade data available to generate a report.");
                    } else {
                        System.out.println("Number of enrollments per grade:");
                        gradeDistribution.forEach(
                                (grade, count) -> System.out.printf("Grade %s: %d enrollment(s)\n", grade, count));
                    }
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n* Welcome to Campus Course & Records Manager (CCRM) *");
        System.out.println("******************************************************");
        System.out.println("--- Main Menu ---");
        System.out.println("1. Manage Students");
        System.out.println("2. Manage Courses");
        System.out.println("3. Manage Enrollments & Grades");
        System.out.println("4. File Operations");
        System.out.println("5. View Reports");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    // --- ENROLLMENT MANAGEMENT ---
    private void handleEnrollmentManagement() {
        while (true) {
            System.out.println("\n--- Enrollments & Grades ---");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. Record Grade for Student");
            System.out.println("0. Return to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            if (choice == 0)
                break;

            switch (choice) {
                case 1:
                    System.out.print("Enter Student Registration Number: ");
                    String regNoToEnroll = scanner.nextLine();
                    System.out.print("Enter Course Code: ");
                    String courseCodeToEnroll = scanner.nextLine();
                    try {
                        enrollmentService.enrollStudent(regNoToEnroll, courseCodeToEnroll);
                    } catch (MaxCreditLimitExceededException | DuplicateEnrollmentException e) {
                        System.out.println("Enrollment failed: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.print("Enter Student Registration Number: ");
                    String regNoForGrade = scanner.nextLine();
                    System.out.print("Enter Course Code: ");
                    String courseCodeForGrade = scanner.nextLine();
                    System.out.print("Enter Letter Grade (S, A, B, C, D, E, F): ");
                    try {
                        Grade grade = Grade.valueOf(scanner.nextLine().toUpperCase());
                        enrollmentService.recordGrade(regNoForGrade, courseCodeForGrade, grade);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid grade entered. Please use a valid letter grade.");
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Student, Course, and File methods remain the same as before
    private void handleStudentManagement() {
        while (true) {
            System.out.println("\n--- Student Management ---");
            System.out.println("1. Add New Student");
            System.out.println("2. List All Students");
            System.out.println("3. Find Student by Registration Number");
            System.out.println("4. View Student Transcript");
            System.out.println("0. Return to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            if (choice == 0)
                break;

            switch (choice) {
                case 1:
                    System.out.print("Enter Full Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter Registration Number: ");
                    String regNo = scanner.nextLine();
                    studentService.addStudent(name, email, regNo);
                    break;
                case 2:
                    System.out.println("\n--- All Students ---");
                    List<Student> students = studentService.getAllStudents();
                    if (students.isEmpty()) {
                        System.out.println("No students found in the system.");
                    } else {
                        students.forEach(student -> System.out.println(student.getProfileSummary()));
                    }
                    break;
                case 3:
                    System.out.print("Enter Registration Number: ");
                    String regNoToFind = scanner.nextLine();
                    Optional<Student> studentOpt = studentService.findStudentByRegNo(regNoToFind);
                    studentOpt.ifPresentOrElse(
                            student -> System.out.println("Found: " + student.getProfileSummary()),
                            () -> System.out.println("No student found with Registration Number: " + regNoToFind));
                    break;
                case 4:
                    System.out.print("Enter Registration Number to view transcript: ");
                    String regNoForTranscript = scanner.nextLine();
                    Optional<Student> studentForTranscript = studentService.findStudentByRegNo(regNoForTranscript);
                    studentForTranscript.ifPresentOrElse(
                            student -> {
                                System.out.println(
                                        "Transcript for " + student.getFullName() + " (" + student.getRegNo() + "):");
                                // Print transcript: list enrolled courses and grades
                                enrollmentService.printTranscript(student.getRegNo());
                            },
                            () -> System.out.println("No student found with that Registration Number."));
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleCourseManagement() {
        while (true) {
            System.out.println("\n--- Course Management ---");
            System.out.println("1. Add New Course");
            System.out.println("2. List All Courses");
            System.out.println("0. Return to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();
            if (choice == 0)
                break;

            switch (choice) {
                case 1:
                    try {
                        System.out.print("Enter Course Code (e.g., CS101): ");
                        String code = scanner.nextLine();

                        System.out.print("Enter Course Title: ");
                        String title = scanner.nextLine();

                        System.out.print("Enter Credits: ");
                        int credits = Integer.parseInt(scanner.nextLine());

                        System.out.print("Enter Department: ");
                        String department = scanner.nextLine();

                        System.out.print("Enter Semester (FALL, SPRING, SUMMER): ");
                        Semester semester = Semester.valueOf(scanner.nextLine().toUpperCase());

                        // Use the Builder to create the course
                        Course newCourse = new Course.Builder(code, title, credits)
                                .department(department)
                                .semester(semester)
                                .build();

                        courseService.addCourse(newCourse);

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Credits must be a number.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid semester. Please use FALL, SPRING, or SUMMER.");
                    }
                    break;
                case 2:
                    System.out.println("\n--- All Courses ---");
                    List<Course> courses = courseService.getAllCourses();
                    if (courses.isEmpty()) {
                        System.out.println("No courses found in the system.");
                    } else {
                        courses.forEach(System.out::println);
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleFileOperations() {
        while (true) {
            System.out.println("\n--- File Operations Menu ---");
            System.out.println("1. Import All Data (from .csv files)");
            System.out.println("2. Export All Data (to .csv files)");
            System.out.println("3. Create a Backup");
            System.out.println("0. Return to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    try {
                        System.out.println("Importing data...");
                        studentService.loadData();
                        courseService.loadData();
                        System.out.println("Data import successful!");
                    } catch (IOException e) {
                        System.out.println("Error importing data: " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        System.out.println("Exporting data...");
                        studentService.saveData();
                        courseService.saveData();
                        System.out.println("Data export successful!");
                    } catch (IOException e) {
                        System.out.println("Error exporting data: " + e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        System.out.println("Creating backup...");
                        backupService.backupData();
                    } catch (IOException e) {
                        System.out.println("Error creating backup: " + e.getMessage());
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private int getUserChoice() {
        int choice = -1;
        try {
            String input = scanner.nextLine();
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            // Handled by the default case in the switch
        }
        return choice;
    }
}