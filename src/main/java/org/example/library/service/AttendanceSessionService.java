package org.example.library.service;

import org.example.library.dto.AttendanceSessionDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AttendanceSessionService {

    AttendanceSessionDto createAttendanceSession(Long courseId, Map<Integer,Boolean> studentAttendance);
    AttendanceSessionDto getAttendanceSessionById(Long id);
    List<AttendanceSessionDto> getAttendanceSessionsByCourseId(Long courseId);
    List<AttendanceSessionDto> getAttendanceSessionsByDate(LocalDate date);
    AttendanceSessionDto updateAttendanceSession(Long id, Map<Integer,Boolean> studentAttendance);
    void deleteAttendanceSession(Long id);

    List<AttendanceSessionDto> getAttendanceSessionByCourseIdAndDate(Long courseId, LocalDate date);
}