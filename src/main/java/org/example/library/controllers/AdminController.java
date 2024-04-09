package org.example.library.controllers;

import jakarta.validation.Valid;
import org.example.library.dto.AdminDto;
import org.example.library.dto.BookDto;
import org.example.library.dto.StudentDto;
import org.example.library.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @PostMapping("/")
    public ResponseEntity<AdminDto> createAdmin(@Valid @RequestBody AdminDto adminDto) {
        AdminDto createAdminDto = adminService.createAdmin(adminDto);
        return new ResponseEntity<>(createAdminDto, HttpStatus.CREATED);

    }

    @GetMapping("/{adminId}")
    public ResponseEntity<AdminDto> getAdmin(@PathVariable("adminId") Integer adminId) {
        AdminDto getAdmin = this.adminService.getAdmin(adminId);
        return new ResponseEntity<>(getAdmin, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/issue")
    public  ResponseEntity<StudentDto> issueBook(@Valid @RequestBody BookDto bookDto, @RequestParam("roll") Integer id){
        StudentDto studentDto = this.adminService.issueBook(id, bookDto);
        return new ResponseEntity<>(studentDto,HttpStatus.CREATED);


    }
    @GetMapping("/student/details")
    public  ResponseEntity<StudentDto> getStudentDetails(@RequestParam("roll") Integer roll){
        StudentDto studentDetails = this.adminService.getStudentDetails(roll);
        return new ResponseEntity<>(studentDetails,HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/submit/book")
    public  ResponseEntity<StudentDto> submitBook(@RequestParam("bookId")Integer bookId, @RequestParam("roll") Integer roll){
        StudentDto studentDto = this.adminService.submitBook(bookId, roll);
        return  new ResponseEntity<>(studentDto,HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<AdminDto>> getAllAdmins(){
        List<AdminDto> allAdmins = this.adminService.getAllAdmins();
        return new ResponseEntity<>(allAdmins,HttpStatus.OK);
    }
}