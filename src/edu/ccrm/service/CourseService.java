package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Instructor;
import edu.ccrm.domain.Semester;
import java.util.List;
import java.util.Optional;

public interface CourseService extends Persistable {
    void addCourse(Course course);
    Optional<Course> findCourseByCode(String code);
    List<Course> getAllCourses();
    List<Course> findCoursesByDepartment(String department);
    List<Course> findCoursesByInstructor(Instructor instructor);
    List<Course> findCoursesBySemester(Semester semester);
}