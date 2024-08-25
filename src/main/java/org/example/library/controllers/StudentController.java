package org.example.library.controllers;

import jakarta.validation.Valid;
import org.example.library.dto.StudentDto;
import org.example.library.entities.Student;
import org.example.library.repositories.StudentRepository;
import org.example.library.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/student")
public class StudentController {


    @Autowired
    private StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @PostMapping("/")
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody StudentDto studentDto){

        StudentDto studentDto1 = this.studentService.createStudent(studentDto);
        return new ResponseEntity<>(studentDto1, HttpStatus.CREATED);

    }
    @GetMapping("/{studentId}")
    public  ResponseEntity<StudentDto> getStudentById(@PathVariable("studentId") Integer studenId){
        StudentDto studentDto = this.studentService.getStudentById(studenId);
        return new ResponseEntity<>(studentDto,HttpStatus.OK);

    }
    @GetMapping("/all")
    public ResponseEntity<List<StudentDto>> getAllStudents(){
        List<StudentDto> allStudents = this.studentService.getAllStudents();
        return new ResponseEntity<>(allStudents,HttpStatus.OK);

    }

    @GetMapping("/byEmail")
    public ResponseEntity<StudentDto> getStudentByEmail(@RequestParam("email") String email){
        StudentDto studentByEmail = this.studentService.getStudentByEmail(email);
        return new ResponseEntity<>(studentByEmail,HttpStatus.OK);
    }


}
