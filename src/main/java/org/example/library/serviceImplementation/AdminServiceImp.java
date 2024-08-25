package org.example.library.serviceImplementation;

import org.example.library.dto.AdminDto;
import org.example.library.dto.BookDto;
//import org.example.library.dto.RoleDto;
import org.example.library.dto.StudentDto;
import org.example.library.entities.Admin;
import org.example.library.entities.Book;

import org.example.library.entities.Role;
import org.example.library.entities.Student;
import org.example.library.exceptions.ApiException;
import org.example.library.exceptions.ResourceNotFoundException;
import org.example.library.repositories.AdminRepository;
import org.example.library.repositories.BookRepository;
//import org.example.library.repositories.RoleRepository;
import org.example.library.repositories.RoleRepository;
import org.example.library.repositories.StudentRepository;
import org.example.library.service.AdminService;
import org.example.library.service.EmailSenderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AdminServiceImp implements AdminService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private RoleRepository roleRepository;



    @Override
    public AdminDto createAdmin(AdminDto adminDto) {
        if(this.studentRepository.existsByEmail(adminDto.getAdminEmail())){
            throw  new ApiException("email is already exits :"+ adminDto.getAdminEmail());
        }
        try {
            Role role = this.roleRepository.findById(1).orElseThrow(() -> new ResourceNotFoundException("role", "roleId", 1));
            Admin admin = modelMapper.map(adminDto, Admin.class);

            admin.setAdminPassword(passwordEncoder.encode(admin.getAdminPassword()));
            admin.getRoles().add(role);
            Admin saveAmin = adminRepository.save(admin);
            return modelMapper.map(saveAmin, AdminDto.class);

        } catch (Exception e) {
            throw new ApiException("Admin is already exits");
        }

    }

    @Override
    public AdminDto getAdmin(Integer id) {
        Admin admin = this.adminRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Admin", "id", id));
        System.out.println(admin.getRoles()+" admin roles"+id);

        return modelMapper.map(admin, AdminDto.class);
    }

    @Override
    public boolean MatchingAdminName(String adminName) {
        return false;
    }

    @Override
    public boolean MatchingAdminPassword(String password) {
        return false;
    }

    @Override
    public StudentDto issueBook(Integer id, BookDto bookDto) {

        Student student = this.studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("student not found ", "student roll", id));
        int count = (int) student.getBooks().stream()
                .filter(book -> book.getDateOfSubmission() == null)
                .count();
        Book book = modelMapper.map(bookDto, Book.class);
        if (this.bookRepository.existsByBookId(book.getBookId())) {
            Book book1 = this.bookRepository.findById(book.getBookId()).orElseThrow(() -> new ResourceNotFoundException("book", "bookId", book.getBookId()));
            if (book1.getDateOfIssue() != null && book1.getDateOfSubmission() == null) {
                throw new ApiException("Book is already assigned to  roll no " + book1.getStudent().getRoll() + "student name " + book1.getStudent().getName());
            } else {
                book1.setDateOfIssue(LocalDate.now());
                book1.setStudent(student);
                book1.setDateOfSubmission(null);
                count = count + 1;
                student.setNoOfBookIssue(count);
                if (count <= 10) {
                    Book savedBook = this.bookRepository.save(book1);
                    BookDto bb = this.modelMapper.map(savedBook, BookDto.class);
                    List<BookDto> bookDtos = student.getBooks().stream().map(book2 -> this.modelMapper.map(book2, BookDto.class)).collect(Collectors.toList());
                    StudentDto studentDto = modelMapper.map(student, StudentDto.class);
                    studentDto.setBooksDto(bookDtos);
//                    this.emailSenderService.sendSimpleEmail(
//                            studentDto.getEmail(),
//                            "Book id: " + bb.getBookId() + "\n" +
//                                    "Student Name: " + studentDto.getName() + "\n" +
//                                    "Book Name: " + bb.getBookName() + "\n" +
//                                    "Book semester: " + book.getBookSemester() + "\n" +
//                                    "Date of Issue: " + bb.getDateOfIssue() + "\n\n" +
//                                    "Thanks and Regards,\n" +
//                                    "Shashi Kumar\n" +
//                                    "Mob: 7073052300 \n",
//                            "Book Issued to Roll No " + student.getRoll()
//                    );


                    return studentDto;
                } else {
                    throw new ApiException("Already 10 books is issued to this student");
                }


            }

        } else {

            book.setStudent(student);
            book.setDateOfIssue(LocalDate.now());
            count = count + 1;
            student.setNoOfBookIssue(count);
            if (count > 10) {
                throw new ApiException("already 10 book is issued to this student so this book will not issue");
            }
            Book save = this.bookRepository.save(book);
            System.out.println(save);
            System.out.println(save.getStudent().getNoOfBookIssue());
            List<BookDto> bookDtos = student.getBooks().stream().map(book2 -> this.modelMapper.map(book2, BookDto.class)).collect(Collectors.toList());
            StudentDto studentDto = modelMapper.map(student, StudentDto.class);
            BookDto bb = this.modelMapper.map(save, BookDto.class);
            bookDtos.add(bb);
            studentDto.setBooksDto(bookDtos);
//            this.emailSenderService.sendSimpleEmail(
//                    studentDto.getEmail(),
//                    "Book id: " + bb.getBookId() + "\n" +
//                            "Student Name: " + studentDto.getName() + "\n" +
//                            "Book Name: " + bb.getBookName() + "\n" +
//                            "Book semester: " + book.getBookSemester() + "\n" +
//                            "Date of Issue: " + bb.getDateOfIssue() + "\n\n" +
//                            "Thanks and Regards,\n" +
//                            "Shashi Kumar\n" +
//                            "Mob: 7073052300 \n",
//                    "Book Issued to Roll No " + student.getRoll()
//            );
            System.out.println("email is successfully send");

            return studentDto;

        }

    }

    @Override
    public StudentDto getStudentDetails(Integer roll) {
        Student student = this.studentRepository.findById(roll)
                .orElseThrow(() -> new ResourceNotFoundException("student ", "student id", roll));

        return modelMapper.map(student, StudentDto.class);


    }

    @Override
    public StudentDto submitBook(Integer bookId, Integer roll) {
        if(bookId==null || roll==null || bookId<1 || roll<1) {
            throw  new ApiException("Invalid request, plz give proper details");
        }
        Book book = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("book", "bookId", bookId));

        Student student = this.studentRepository.findById(roll).orElseThrow(() -> new ResourceNotFoundException("student", "studendId", roll));
        int count = (int) student.getBooks().stream()
                .filter(book1 -> book1.getDateOfSubmission() == null)
                .count();
        try {
            Student student1 = book.getStudent();

            if (Objects.equals(student1.getRoll(), student.getRoll())) {
                if (book.getDateOfSubmission() != null) {
                    throw new ApiException("book is already submitted");
                } else {
                    book.setDateOfSubmission(LocalDate.now());
                    student.setNoOfBookIssue(count - 1);
//                    this.emailSenderService.sendSimpleEmail(
//                            student.getEmail(),
//                            "Book id: " + book.getBookId() + "\n" +
//                                    "Student Name: " + student.getName() + "\n" +
//                                    "Book Name: " + book.getBookName() + "\n" +
//                                    "Total days of hiring: " + book.getTotalDaysOfIssueBook() + "\n" +
//                                    "Book semester: " + book.getBookSemester() + "\n" +
//                                    "fine charge: " + book.getFine() + "\n" +
//                                    "Date of Issue: " + book.getDateOfIssue() + "\n" +
//                                    "Date of Submission: " + book.getDateOfSubmission() + "\n\n" +
//                                    "Thanks and Regards,\n" +
//                                    "Shashi Kumar\n" +
//                                    "Mob: 7073052300 \n",
//                            "Book submitted by Roll No " + student.getRoll()
//                    );
                    System.out.println("email is successfully send");

                    book.setTotalDaysOfIssueBook(0);
                    book.setFine("0");
                    Book book1 = this.bookRepository.save(book);

                }

            } else {
                throw new Exception("this book is not associated with this student");
            }

        } catch (Exception e) {
            throw new ApiException("error in submitting book. " + e.getMessage());
        }
        List<BookDto> bookDtos = student.getBooks().stream().map(book1 -> this.modelMapper.map(book1, BookDto.class)).collect(Collectors.toList());

        StudentDto studentDto = modelMapper.map(student, StudentDto.class);
        studentDto.setBooksDto(bookDtos);
        return studentDto;

    }

    @Override
    public List<AdminDto> getAllAdmins() {

        List<AdminDto> adminDto = this.adminRepository.findAll()
                .stream().map((admin) -> this.modelMapper.map(admin, AdminDto.class)).collect(Collectors.toList());
        System.out.println(adminDto);
        return adminDto;
    }

    @Override
    public BookDto issueAvailableBook(Integer bookId, Integer roll) {
        Book book = this.bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("book", "bookId", bookId));
        if(book.getDateOfIssue() !=null && book.getDateOfSubmission()== null){
            throw new ApiException("this book is not available to issue, already assigned to student name:"+book.getStudent().getName()+" roll no: "+book.getStudent().getRoll().toString());
        }else {


            Student student = this.studentRepository.findById(roll).orElseThrow(() -> new ResourceNotFoundException("student", "studendId", roll));


            int count = (int) student.getBooks().stream()
                    .filter(book1 -> book1.getDateOfSubmission() == null)
                    .count();

            count = count + 1;
            if (count > 10) {
                throw new ApiException("already 10 book is issued to this student so this book will not issue");
            }
            student.setNoOfBookIssue(count);
            book.setStudent(student);
            book.setDateOfIssue(LocalDate.now());
            book.setTotalDaysOfIssueBook(0);
            book.setFine(null);
            book.setDateOfSubmission(null);

            Book book1 = this.bookRepository.save(book);
            BookDto bookDto = this.modelMapper.map(book1, BookDto.class);
            bookDto.setStudentDto(modelMapper.map(student, StudentDto.class));
            return bookDto;
        }

    }
}
