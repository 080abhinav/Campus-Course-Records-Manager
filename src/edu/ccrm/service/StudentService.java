package edu.ccrm.service;

import edu.ccrm.domain.Student;
import java.util.List;
import java.util.Optional;

public interface StudentService extends Persistable {
    void addStudent(String fullName, String email, String regNo);
    Optional<Student> findStudentByRegNo(String regNo);
    List<Student> getAllStudents();
    List<Student> getTopStudents(int count);
    boolean updateStudent(String regNo, String newFullName, String newEmail);
    void deactivateStudent(String regNo);
}