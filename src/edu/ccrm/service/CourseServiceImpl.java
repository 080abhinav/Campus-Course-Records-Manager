package edu.ccrm.service;

import edu.ccrm.config.DataStore;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Instructor;
import edu.ccrm.domain.Semester;
import edu.ccrm.io.FileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CourseServiceImpl implements CourseService {

    private final DataStore dataStore = DataStore.getInstance();
    private final FileService<Course> fileService = new FileService<>("courses.csv");

    @Override
    public void addCourse(Course course) {
        if (dataStore.courses.containsKey(course.getCode())) {
            System.out.println("Error: Course with code " + course.getCode() + " already exists.");
            return;
        }
        dataStore.courses.put(course.getCode(), course);
        System.out.println("Successfully added course: " + course.getTitle());
    }

    @Override
    public Optional<Course> findCourseByCode(String code) {
        return Optional.ofNullable(dataStore.courses.get(code));
    }

    @Override
    public List<Course> getAllCourses() {
        return new ArrayList<>(dataStore.courses.values());
    }

    // Demonstrates Stream API and lambdas for filtering
    @Override
    public List<Course> findCoursesByDepartment(String department) {
        return dataStore.courses.values().stream()
                .filter(course -> course.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> findCoursesByInstructor(Instructor instructor) {
        return dataStore.courses.values().stream()
                .filter(course -> course.getInstructor() != null && course.getInstructor().equals(instructor))
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> findCoursesBySemester(Semester semester) {
        return dataStore.courses.values().stream()
                .filter(course -> course.getSemester() == semester)
                .collect(Collectors.toList());
    }

    @Override
    public void loadData() throws IOException {
        List<Course> loadedCourses = fileService.readData(line -> {
            String[] parts = line.split(",");
            return new Course.Builder(parts[0], parts[1], Integer.parseInt(parts[2]))
                    .department(parts[3])
                    .semester(Semester.valueOf(parts[4].toUpperCase()))
                    .build();
        });
        loadedCourses.forEach(c -> dataStore.courses.put(c.getCode(), c));
        System.out.println("Loaded " + loadedCourses.size() + " courses.");
    }

    @Override
    public void saveData() throws IOException {
        List<String> lines = dataStore.courses.values().stream()
                .map(c -> String.join(",", c.getCode(), c.getTitle(), String.valueOf(c.getCredits()), c.getDepartment(),
                        c.getSemester().name()))
                .collect(Collectors.toList());
        fileService.writeData(lines);
        System.out.println("Saved " + lines.size() + " courses.");
    }
}