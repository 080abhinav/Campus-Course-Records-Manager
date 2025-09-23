package edu.ccrm.service;

import edu.ccrm.config.DataStore;
import edu.ccrm.domain.Student;
import edu.ccrm.io.FileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentServiceImpl implements StudentService {

    private final DataStore dataStore = DataStore.getInstance();
    private final FileService<Student> fileService = new FileService<>("students.csv");

    @Override
    public void addStudent(String fullName, String email, String regNo) {
        if (dataStore.students.containsKey(regNo)) {
            System.out.println("Error: Student with registration number " + regNo + " already exists.");
            return;
        }
        Student student = new Student(fullName, email, regNo);
        dataStore.students.put(regNo, student);
        System.out.println("Successfully added student: " + student.getFullName());
    }

    @Override
    public Optional<Student> findStudentByRegNo(String regNo) {
        return Optional.ofNullable(dataStore.students.get(regNo));
    }

    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(dataStore.students.values());
    }

    @Override
    public boolean updateStudent(String regNo, String newFullName, String newEmail) {
        Student student = dataStore.students.get(regNo);
        if (student != null) {
            student.setFullName(newFullName);
            student.setEmail(newEmail);
            return true;
        }
        return false;
    }

    @Override
    public void deactivateStudent(String regNo) {
        Student student = dataStore.students.get(regNo);
        if (student != null) {
            student.setStatus(Student.Status.INACTIVE);
            System.out.println("Student " + regNo + " has been deactivated.");
        } else {
            System.out.println("Student not found.");
        }
    }

    @Override
    public void loadData() throws IOException {
        List<Student> loadedStudents = fileService.readData(line -> {
            String[] parts = line.split(",");
            return new Student(parts[1], parts[2], parts[0]); // fullName, email, regNo
        });
        loadedStudents.forEach(s -> dataStore.students.put(s.getRegNo(), s));
        System.out.println("Loaded " + loadedStudents.size() + " students.");
    }

    @Override
    public List<Student> getTopStudents(int count) {
        return dataStore.students.values().stream()
                .sorted((s1, s2) -> Double.compare(s2.calculateGpa(), s1.calculateGpa())) // Sort descending by GPA
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData() throws IOException {
        List<String> lines = dataStore.students.values().stream()
                .map(s -> String.join(",", s.getRegNo(), s.getFullName(), s.getEmail()))
                .collect(Collectors.toList());
        fileService.writeData(lines);
        System.out.println("Saved " + lines.size() + " students.");
    }
}