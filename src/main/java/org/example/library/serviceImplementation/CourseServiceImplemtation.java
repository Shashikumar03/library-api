package org.example.library.serviceImplementation;

import org.example.library.dto.CourseDto;
import org.example.library.dto.StudentDto;
import org.example.library.entities.Course;
import org.example.library.entities.Student;
import org.example.library.exceptions.ApiException;
import org.example.library.exceptions.ResourceNotFoundException;
import org.example.library.repositories.CourseRepository;
import org.example.library.repositories.StudentRepository;
import org.example.library.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CourseServiceImplemtation  implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CourseDto addCourse(CourseDto courseDto) {
        Course course = modelMapper.map(courseDto, Course.class);

        Set<Integer> studentRollSet = courseDto.getStudentsDtos().stream()
                .map(StudentDto::getRoll)
                .collect(Collectors.toSet());

        List<Student> allExistingStudents = this.studentRepository.findAllById(studentRollSet);
        Set<Integer> allExistingStudentRolls = allExistingStudents.stream()
                .map(Student::getRoll)
                .collect(Collectors.toSet());

        // Find non-registered students directly
        Set<Integer> studentNotRegistered = studentRollSet.stream()
                .filter(roll -> !allExistingStudentRolls.contains(roll))
                .collect(Collectors.toSet());

        // If any students are not registered, throw an exception
        if (!studentNotRegistered.isEmpty()) {
            throw new ResourceNotFoundException("Student", "StudentId(s): " + studentNotRegistered, 0);
        }

        // Create a set of students and assign them to the course
        course.setStudents(new HashSet<>(allExistingStudents));

        Course savedCourse = courseRepository.save(course);
        CourseDto savedCourseDto = modelMapper.map(savedCourse, CourseDto.class);

        savedCourseDto.setStudentsDtos(savedCourse.getStudents().stream()
                .map(student -> modelMapper.map(student, StudentDto.class))
                .collect(Collectors.toSet()));

        return savedCourseDto;
    }

    @Override
    public CourseDto updateCourse(CourseDto courseDto) {
        return null;
    }

    @Override
    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course ","CourseId", id));
        CourseDto courseDto = modelMapper.map(course, CourseDto.class);
        courseDto.setStudentsDtos(course.getStudents().stream().map(studentDto -> modelMapper.map(studentDto, StudentDto.class)).collect(Collectors.toSet()));
        return courseDto;
    }

    @Override
    public CourseDto addStudentToExistingCourse(Long courseId, Set<Integer> studentRoll) {
        if(studentRoll.isEmpty()){
            throw  new ApiException("There are no students to add to this course");
        }
        Course course = this.courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        // student already involved or present in this course
        Set<Student> studentsInvolvedInCourse = course.getStudents();
        int size1=studentsInvolvedInCourse.size();

        List<Student> allExistingStudentList = this.studentRepository.findAllById(studentRoll);
        List<Integer> rollOfAllExistingStudent= new ArrayList<>();
        allExistingStudentList.forEach(student -> rollOfAllExistingStudent.add(student.getRoll()));
        // checking any roll is unregister.
        boolean checkStudent=rollOfAllExistingStudent.containsAll(studentRoll);
        List<Integer>unRegisterStudentRoll= new ArrayList<>();

        if(!checkStudent){
            studentRoll.forEach(roll ->{
                boolean b=rollOfAllExistingStudent.contains(roll);
                if(!b){
                    unRegisterStudentRoll.add(roll);
                }
            } );
        }
        if(!unRegisterStudentRoll.isEmpty()){
            throw new ApiException("this student are not registered "+ unRegisterStudentRoll.toString());
        }

        List<Integer> rollNumberOfAllStudentPresentInCourse = new ArrayList<>();
        studentsInvolvedInCourse.stream().forEach(student -> rollNumberOfAllStudentPresentInCourse.add(student.getRoll()));
        if(rollNumberOfAllStudentPresentInCourse.containsAll(rollOfAllExistingStudent)){
            throw new ApiException("this students are already involved in this course"+ rollOfAllExistingStudent.toString());
        }
            allExistingStudentList.stream().forEach(student -> studentsInvolvedInCourse.add(student));
        course.setStudents(studentsInvolvedInCourse);
        System.out.println(studentsInvolvedInCourse);
        Course savedCourse = courseRepository.save(course);

        CourseDto savedCourseDto = modelMapper.map(savedCourse, CourseDto.class);
        savedCourseDto.setStudentsDtos(savedCourse.getStudents().stream().map(student -> modelMapper.map(student, StudentDto.class)).collect(Collectors.toSet()));
        return savedCourseDto;
    }
    @Override
    public CourseDto removeStudentsFromExistingCourse(Long courseId, Set<Integer> removeStudentsSet) {
        System.out.println(removeStudentsSet);

        if(removeStudentsSet.isEmpty()){
            throw  new ApiException("Please select  student to remove");
        }
        //hitting the database
        Course course = this.courseRepository.findById(
                courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found","courseId",courseId));

        Set<Student> existingStudents = course.getStudents();
        Set<Integer> existingStudentsRoll= new HashSet<>();
        existingStudents.forEach(student->existingStudentsRoll.add(student.getRoll()));
        System.out.println(existingStudentsRoll.containsAll(removeStudentsSet)+"shashikumar");
        List<Integer> studentNotExist= new ArrayList<>();
        if(!existingStudentsRoll.containsAll(removeStudentsSet)){
            removeStudentsSet.forEach(roll->{
               boolean b=existingStudentsRoll.contains(roll);
               System.out.println(b+"true or false");
               if(!b){
                   studentNotExist.add(roll);
               }
           });
        }
        System.out.println(studentNotExist+"shashikumar");
        if(!studentNotExist.isEmpty()){
            throw new ApiException("this student are not registered in this course"+ studentNotExist.toString());
        }

        // hitting database
        List<Student> studentToBeRemoved = this.studentRepository.findAllById(removeStudentsSet);
        existingStudents.removeAll(studentToBeRemoved);

        course.setStudents(existingStudents);
        Course savedCourse = courseRepository.save(course);
        CourseDto savedCourseDto = modelMapper.map(savedCourse, CourseDto.class);
        savedCourseDto.setStudentsDtos(savedCourse.getStudents().stream().map(student -> modelMapper.map(student, StudentDto.class)).collect(Collectors.toSet()));
        return savedCourseDto;

    }

    @Override
    public void deleteCourse(int id) {

    }
}