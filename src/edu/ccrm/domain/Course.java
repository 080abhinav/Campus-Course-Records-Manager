package edu.ccrm.domain;

public class Course {

    private final String code;
    private final String title;
    private final int credits;
    private Instructor instructor;
    private final Semester semester;
    private final String department;

    // Private constructor to be used by the Builder
    private Course(Builder builder) {
        this.code = builder.code;
        this.title = builder.title;
        this.credits = builder.credits;
        this.instructor = builder.instructor;
        this.semester = builder.semester;
        this.department = builder.department;
    }

    // Getters (no setters to make core properties immutable after creation)
    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public int getCredits() {
        return credits;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public Semester getSemester() {
        return semester;
    }

    public String getDepartment() {
        return department;
    }

    // A setter for the instructor, as this can change
    public void assignInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    @Override
    public String toString() {
        return String.format("Code: %-10s | Title: %-30s | Credits: %d | Dept: %-25s | Semester: %s",
                code, title, credits, department, semester);
    }

    // --- Builder Pattern Implementation (Static Nested Class) ---
    public static class Builder {
        // Required parameters
        private final String code;
        private final String title;
        private final int credits;

        // Optional parameters - initialized to default values
        private Instructor instructor = null;
        private Semester semester = Semester.FALL; // Default semester
        private String department = "General"; // Default department

        public Builder(String code, String title, int credits) {
            this.code = code;
            this.title = title;
            this.credits = credits;
        }

        public Builder instructor(Instructor instructor) {
            this.instructor = instructor;
            return this;
        }

        public Builder semester(Semester semester) {
            this.semester = semester;
            return this;
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Course build() {
            return new Course(this);
        }
    }

    public class CourseNotes {
        private String note;

        public void setNote(String note) {
            this.note = note;
        }

        public String getNoteWithCourseContext() {
            // Accessing a field from the outer Course instance
            return "Note for " + Course.this.title + ": " + this.note;
        }
    }
}