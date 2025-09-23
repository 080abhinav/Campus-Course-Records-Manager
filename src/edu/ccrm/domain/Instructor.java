package edu.ccrm.domain;

import java.time.LocalDate;

public class Instructor extends Person {

    private String department;
    private final LocalDate hireDate;

    public Instructor(String fullName, String email, String department) {
        super(fullName, email);
        this.department = department;
        this.hireDate = LocalDate.now();
    }

    // Getters and setters
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    @Override
    public String getProfileSummary() {
        return String.format("Instructor Profile | Name: %s | Department: %s | Hired: %s",
                getFullName(), department, hireDate);
    }

    @Override
    public String toString() {
        return getProfileSummary();
    }
}