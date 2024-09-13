package org.example.library.controllers;

import org.example.library.dto.CourseDto;
import org.example.library.dto.StudentDto;
import org.example.library.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/")
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto) {

        CourseDto courseDto1 = this.courseService.addCourse(courseDto);

        return new ResponseEntity<>(courseDto1, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable("id") Long id) {
        CourseDto courseDto = this.courseService.getCourseById(id);
        return new ResponseEntity<>(courseDto,HttpStatus.OK);
    }

    @PutMapping("/add/student-existing-course/{courseId}")
    public ResponseEntity<CourseDto> addStudentsInExistingCourse(@PathVariable("courseId") Long courseId, @RequestBody Set<Integer> studentRoll) {
        CourseDto courseDto = this.courseService.addStudentToExistingCourse(courseId, studentRoll);
        return new ResponseEntity<>(courseDto,HttpStatus.OK);
    }
    @PutMapping("/remove/students/{courseId}")
    public ResponseEntity<CourseDto> removeStudentFromExistingCourse(@PathVariable("courseId") Long courseId, @RequestBody Set<Integer> removeStudentMap) {
        CourseDto courseDto = this.courseService.removeStudentsFromExistingCourse(courseId, removeStudentMap);
        return new ResponseEntity<>(courseDto,HttpStatus.OK);
    }

}
