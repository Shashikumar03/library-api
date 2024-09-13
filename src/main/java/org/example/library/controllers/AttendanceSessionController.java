package org.example.library.controllers;

import org.example.library.dto.AttendanceSessionDto;
import org.example.library.service.AttendanceSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/attendance-session")
public class AttendanceSessionController {

    @Autowired
    private AttendanceSessionService attendanceSessionService;


    @PostMapping("/{courseId}")
    public ResponseEntity<AttendanceSessionDto> createAttendanceSession(@PathVariable("courseId") Long courseId, @RequestBody Map<Integer,Boolean> attendance){
        AttendanceSessionDto attendanceSession = this.attendanceSessionService.createAttendanceSession(courseId, attendance);
        return new ResponseEntity<>(attendanceSession, HttpStatus.CREATED);
    }
    @PutMapping("/update/{attendanceSessionId}")
    public ResponseEntity<AttendanceSessionDto> updateAttendanceSession(@PathVariable("attendanceSessionId") Long attendanceSessionId, @RequestBody Map<Integer, Boolean> attendance){
        AttendanceSessionDto attendanceSessionDto = this.attendanceSessionService.updateAttendanceSession(attendanceSessionId, attendance);
        return new ResponseEntity<>(attendanceSessionDto, HttpStatus.OK);
    }
    @GetMapping("/course/{courseId}")
    public  ResponseEntity<List<AttendanceSessionDto>> getAttendanceSessionByCourseId(@PathVariable("courseId") Long courseId){
        List<AttendanceSessionDto> attendanceSessionsByCourseId = this.attendanceSessionService.getAttendanceSessionsByCourseId(courseId);
        return new ResponseEntity<>(attendanceSessionsByCourseId,HttpStatus.OK);
    }

    @GetMapping("/{courseId}/{Date}")
    public  ResponseEntity<List<AttendanceSessionDto>> getAttendanceSessionByCourseIdAndDate(@PathVariable("courseId") Long courseId, @PathVariable("Date") LocalDate date){
        List<AttendanceSessionDto> attendanceSessionByCourseIdAndDate = this.attendanceSessionService.getAttendanceSessionByCourseIdAndDate(courseId, date);
        return new ResponseEntity<>(attendanceSessionByCourseIdAndDate,HttpStatus.OK);
    }
}
