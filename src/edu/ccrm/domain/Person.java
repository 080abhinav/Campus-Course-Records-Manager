package edu.ccrm.domain;

import java.util.UUID;

public abstract class Person {

    private final String id;
    private String fullName;
    private String email;

    public Person(String fullName, String email) {
        this.id = UUID.randomUUID().toString(); // A simple way to generate a unique ID.
        this.fullName = fullName;
        this.email = email;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public abstract String getProfileSummary();

    @Override
    public String toString() {
        return "Person [id=" + id + ", fullName=" + fullName + ", email=" + email + "]";
    }
}