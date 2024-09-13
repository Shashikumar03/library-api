package org.example.library.service;
import org.example.library.dto.AttendanceDto;

import java.util.List;

public interface AttendanceService {


    List<AttendanceDto> recordAttendance(Long attendanceSessionId, List<AttendanceDto> attendances);

    List<AttendanceDto> getAttendanceBySession(Long attendanceSessionId);


    AttendanceDto getAttendanceByStudentAndSession(Long attendanceSessionId, Long studentId);


    AttendanceDto updateAttendance(Long attendanceId, AttendanceDto updatedAttendance);

    List<AttendanceDto> getAttendanceByStudentId(Long studentId);

//    List<AttendenceDtoV2> getAttendanceOfStudentById(Long studentId);

    void deleteAttendance(Long attendanceId);
}