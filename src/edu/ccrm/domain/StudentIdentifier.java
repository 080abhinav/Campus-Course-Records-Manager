package edu.ccrm.domain;

public final class StudentIdentifier { // final class prevents subclassing

    private final String studentId;
    private final String registrationNumber;

    public StudentIdentifier(String studentId, String registrationNumber) {

        assert studentId != null && !studentId.trim().isEmpty() : "Student ID cannot be null or empty";
        assert registrationNumber != null && !registrationNumber.trim().isEmpty()
                : "Registration number cannot be null or empty";

        this.studentId = studentId;
        this.registrationNumber = registrationNumber;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @Override
    public String toString() {
        return "StudentIdentifier [studentId=" + studentId + ", registrationNumber=" + registrationNumber + "]";
    }

    // It's good practice to override equals and hashCode for value objects.
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((studentId == null) ? 0 : studentId.hashCode());
        result = prime * result + ((registrationNumber == null) ? 0 : registrationNumber.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StudentIdentifier other = (StudentIdentifier) obj;
        if (studentId == null) {
            if (other.studentId != null)
                return false;
        } else if (!studentId.equals(other.studentId))
            return false;
        if (registrationNumber == null) {
            if (other.registrationNumber != null)
                return false;
        } else if (!registrationNumber.equals(other.registrationNumber))
            return false;
        return true;
    }
}