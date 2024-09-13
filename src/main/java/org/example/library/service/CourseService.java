package org.example.library.service;

import org.example.library.dto.CourseDto;
import org.example.library.dto.StudentDto;

import java.util.Map;
import java.util.Set;

public interface CourseService {
    CourseDto addCourse(CourseDto courseDto);
    CourseDto updateCourse(CourseDto courseDto);
    CourseDto getCourseById(Long id);
    CourseDto addStudentToExistingCourse(Long courseId, Set<Integer> studentRoll);

    CourseDto removeStudentsFromExistingCourse(Long courseId, Set<Integer> removeStudentsMap);
    void deleteCourse(int id);
}
