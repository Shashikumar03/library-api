package org.example.library.serviceImplementation;


import lombok.extern.slf4j.Slf4j;
import org.example.library.dto.*;
import org.example.library.entities.Attendance;
import org.example.library.entities.AttendanceSession;
import org.example.library.entities.Course;
import org.example.library.entities.Student;
import org.example.library.exceptions.ApiException;
import org.example.library.exceptions.ResourceNotFoundException;
import org.example.library.repositories.AttendanceRepository;
import org.example.library.repositories.AttendanceSessionRepository;
import org.example.library.repositories.CourseRepository;
import org.example.library.repositories.StudentRepository;
import org.example.library.service.AttendanceSessionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.MarshalledObject;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class AttendanceSessionServiceImplementation implements AttendanceSessionService {
    @Autowired
    private AttendanceSessionRepository attendanceSessionRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Override
    public AttendanceSessionDto createAttendanceSession(Long courseId, Map<Integer, Boolean> studentAttendanceMap) {
        if (studentAttendanceMap.isEmpty()) {
            throw new ApiException("For taking attendance, select the student first");
        }

        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ApiException("Course not found"));
        System.out.println("madiwala");
        Set<Student> students1 = course.getStudents();
        List<Integer> studentRoll = new ArrayList<>();
        List<Integer> studentNotInvolvedInCourse = new ArrayList<>();

        students1.forEach(student -> studentRoll.add(student.getRoll()));
        for (Map.Entry<Integer, Boolean> entry : studentAttendanceMap.entrySet()) {
            Integer roll = entry.getKey();
            if (!studentRoll.contains(roll)) {
                studentNotInvolvedInCourse.add(roll);
            }

        }

        if (!studentNotInvolvedInCourse.isEmpty()) {
//            StudentNotInvolvedInCourseDto studentNotInvolvedInCourseDto = new StudentNotInvolvedInCourseDto();
//            studentNotInvolvedInCourseDto.setListOfStudents(listOfStudentNotInvolvedInCourse);
//            studentNotInvolvedInCourseDto.setMessage("students not involved in this course, plzz added the student first");
            throw new ApiException("roll no" + " " + studentNotInvolvedInCourse.toString() + "this student are  not involved in this course, add them first");
        }
        AttendanceSession attendanceSession = new AttendanceSession();
        attendanceSession.setCourseId(courseId);
        attendanceSession.setDate(LocalDate.now());

        List<Student> students = this.studentRepository.findAllById(studentRoll);

        Map<Integer, Student> studentMap = students.stream()
                .collect(Collectors.toMap(Student::getRoll, student -> student));

        Set<Attendance> attendanceSet = new HashSet<>();

        for (Map.Entry<Integer, Boolean> entry : studentAttendanceMap.entrySet()) {
            Integer studentId = entry.getKey();
            Boolean isPresent = entry.getValue();
            Student student = studentMap.get(studentId);
            if (student == null) {
                throw new ResourceNotFoundException("Student ", " studentId", studentId);
            }

            Attendance attendance = new Attendance();
            attendance.setStudentId(studentId);
            attendance.setPresent(isPresent);
            attendance.setAttendanceSession(attendanceSession);
            attendanceSet.add(attendance);
        }

        attendanceSession.setAttendances(attendanceSet);
        AttendanceSession savedAttendanceSession = this.attendanceSessionRepository.save(attendanceSession);
        AttendanceSessionDto attendanceSessionDto = this.modelMapper.map(savedAttendanceSession, AttendanceSessionDto.class);
        attendanceSessionDto.setAttendancesDto(
                savedAttendanceSession.getAttendances().stream()
                        .map(attendance -> this.modelMapper.map(attendance, AttendanceDto.class))
                        .collect(Collectors.toSet())
        );

        Set<AttendanceDto> attendancesDto = attendanceSessionDto.getAttendancesDto();
        for (AttendanceDto attendanceDto : attendancesDto) {
            Integer studentId = attendanceDto.getStudentId();
            Student student = studentMap.get(studentId);
            if (student == null) {
                throw new ResourceNotFoundException("Student", "StudentId", studentId);
            }
            StudentDto studentDto = this.modelMapper.map(student, StudentDto.class);
            attendanceDto.setStudentDto(studentDto);
        }

        return attendanceSessionDto;

    }


    @Override
    public AttendanceSessionDto getAttendanceSessionById(Long id) {
        return null;
    }

    @Override
    public List<AttendanceSessionDto> getAttendanceSessionsByCourseId(Long courseId) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "CourseId", courseId));

        List<AttendanceSession> attendanceSession = this.attendanceSessionRepository.findAllByCourseId(courseId);

        // Get all student IDs in one go to reduce multiple DB hits
        Set<Integer> studentIds = attendanceSession.stream()
                .flatMap(session -> session.getAttendances().stream())
                .map(Attendance::getStudentId)
                .collect(Collectors.toSet());

        // Fetch all students in one query
        List<Student> students = this.studentRepository.findAllById(studentIds);
        Map<Integer, Student> studentMap = students.stream()
                .collect(Collectors.toMap(Student::getRoll, student -> student));

        // Map course to DTO
        CourseDto courseDto = this.modelMapper.map(course, CourseDto.class);

        // Map attendance sessions to DTOs
        List<AttendanceSessionDto> attendanceSessionDtos = attendanceSession.stream().map(session -> {
            // Map attendance to DTOs
            Set<AttendanceDto> attendanceDtos = session.getAttendances().stream().map(attendance -> {
                // Get student from the pre-fetched map
                Student student = studentMap.get(attendance.getStudentId());
                if (student == null) {
                    throw new RuntimeException("Student not found for ID: " + attendance.getStudentId());
                }

                // Map student and attendance to DTOs
                StudentDto studentDto = this.modelMapper.map(student, StudentDto.class);
                AttendanceDto attendanceDto = this.modelMapper.map(attendance, AttendanceDto.class);
                attendanceDto.setStudentDto(studentDto);
                return attendanceDto;
            }).collect(Collectors.toSet());

            // Map attendance session to DTO
            AttendanceSessionDto sessionDto = this.modelMapper.map(session, AttendanceSessionDto.class);
            sessionDto.setAttendancesDto(attendanceDtos);
            sessionDto.setCourseDto(courseDto);
            return sessionDto;
        }).collect(Collectors.toList());

        return attendanceSessionDtos;
    }


    @Override
    public List<AttendanceSessionDto> getAttendanceSessionsByDate(LocalDate date) {
        return List.of();
    }

    @Override
    public AttendanceSessionDto updateAttendanceSession(Long attendanceSessionId, Map<Integer, Boolean> studentAttendanceMap) {
        AttendanceSession attendanceSessions = this.attendanceSessionRepository.findById(attendanceSessionId).orElseThrow(() -> new RuntimeException("Attendance session not found"));
        System.out.println(attendanceSessions);
        Set<Attendance> attendances = attendanceSessions.getAttendances();

        for (Attendance attendance : attendances) {
            Integer studentId = attendance.getStudentId();
            boolean b = studentAttendanceMap.containsKey(studentId);
            if (b) {
                attendance.setPresent(studentAttendanceMap.get(studentId));

            }
        }
        attendanceSessions.setAttendances(attendances);
        AttendanceSession save = this.attendanceSessionRepository.save(attendanceSessions);
        System.out.println(save);
        Set<AttendanceDto> attendanceDtos = save.getAttendances().stream().map(attendance -> this.modelMapper.map(attendance, AttendanceDto.class)).collect(Collectors.toSet());
        AttendanceSessionDto attendanceSessionDto = this.modelMapper.map(save, AttendanceSessionDto.class);
        Set<AttendanceDto> collect = attendanceDtos.stream().map(a -> {
            Integer studentId = a.getStudentId();
            Student student = this.studentRepository.findById(studentId).orElse(null);
            StudentDto studentDto = this.modelMapper.map(student, StudentDto.class);
            a.setStudentDto(studentDto);
            return a;
        }).collect(Collectors.toSet());
        attendanceSessionDto.setAttendancesDto(collect);
        return attendanceSessionDto;
    }

    @Override
    public void deleteAttendanceSession(Long id) {

    }

    //    @Override
//    public List<AttendanceSessionDto> getAttendanceSessionByCourseIdAndDate(Long courseId, LocalDate date) {
//        List<AttendanceSession> attendanceSessionsByCourseIdAndDate = this.attendanceSessionRepository.findByCourseIdAndDate(courseId, date);
//        System.out.println(attendanceSessionsByCourseIdAndDate);
//        log.info("fetching data correctly");
//
//        List<AttendanceSessionDto> collect = attendanceSessionsByCourseIdAndDate.stream().map(session -> {
//            Long courseId1 = session.getCourseId();
//            Course course = this.courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course", "courseId", courseId1));
//            CourseDto courseDto = this.modelMapper.map(course, CourseDto.class);
//
//            Set<Attendance> attendances = session.getAttendances();
//            Set<AttendanceDto> attendanceDtos = attendances.stream().map(attendance -> {
//                Integer studentId = attendance.getStudentId();
//                Student student = this.studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student", "studentId", studentId));
//                StudentDto studentDto = this.modelMapper.map(student, StudentDto.class);
//
//                AttendanceDto attendanceDto = this.modelMapper.map(attendance, AttendanceDto.class);
//                attendanceDto.setStudentDto(studentDto);
//                return attendanceDto;
//
//
//            }).collect(Collectors.toSet());
//
//
//            AttendanceSessionDto attendanceSession = this.modelMapper.map(session, AttendanceSessionDto.class);
//            attendanceSession.setCourseDto(courseDto);
//            attendanceSession.setAttendancesDto(attendanceDtos);
//            return attendanceSession;
//
//
//        }).collect(Collectors.toList());
//        return collect;
//
//    }
    @Override
    public List<AttendanceSessionDto> getAttendanceSessionByCourseIdAndDate(Long courseId, LocalDate date) {
        List<AttendanceSession> attendanceSessionsByCourseIdAndDate = this.attendanceSessionRepository.findByCourseIdAndDate(courseId, date);

        // Step 1: Collect all student IDs from attendances
        Set<Integer> studentIds = attendanceSessionsByCourseIdAndDate.stream()
                .flatMap(session -> session.getAttendances().stream())
                .map(Attendance::getStudentId)
                .collect(Collectors.toSet());

        // Step 2: Fetch all students in one query
        Map<Integer, Student> studentsMap = this.studentRepository.findAllById(studentIds)
                .stream()
                .collect(Collectors.toMap(Student::getRoll, student -> student));

        // Step 3: Create DTOs using pre-fetched students
        List<AttendanceSessionDto> attendanceSessionDtos = attendanceSessionsByCourseIdAndDate.stream().map(session -> {
            Course course = this.courseRepository.findById(courseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Course", "courseId", courseId));
            CourseDto courseDto = this.modelMapper.map(course, CourseDto.class);

            Set<AttendanceDto> attendanceDtos = session.getAttendances().stream().map(attendance -> {
                // Fetch the student from the map
                Student student = studentsMap.get(attendance.getStudentId());
                if (student == null) {
                    throw new ResourceNotFoundException("Student", "studentId", attendance.getStudentId());
                }

                StudentDto studentDto = this.modelMapper.map(student, StudentDto.class);
                AttendanceDto attendanceDto = this.modelMapper.map(attendance, AttendanceDto.class);
                attendanceDto.setStudentDto(studentDto);

                return attendanceDto;
            }).collect(Collectors.toSet());

            AttendanceSessionDto attendanceSessionDto = this.modelMapper.map(session, AttendanceSessionDto.class);
            attendanceSessionDto.setCourseDto(courseDto);
            attendanceSessionDto.setAttendancesDto(attendanceDtos);

            return attendanceSessionDto;
        }).collect(Collectors.toList());

        return attendanceSessionDtos;
    }


}