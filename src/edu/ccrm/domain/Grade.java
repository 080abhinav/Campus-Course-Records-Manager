package edu.ccrm.domain;

public enum Grade {
    S(10.0),
    A(9.0),
    B(8.0),
    C(7.0),
    D(6.0),
    E(5.0),
    F(0.0),
    NOT_GRADED(-1.0);

    public double getPoints() {

        return 0.0; // Placeholder, update as needed
    }

    private final double gradePoints;

    // Enum constructor is implicitly private
    Grade(double gradePoints) {
        this.gradePoints = gradePoints;
    }

    public double getGradePoints() {
        return gradePoints;
    }
}