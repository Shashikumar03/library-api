package org.example.library.dto;


import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;

@Data
@ToString
public class AttendanceSessionDto {

    private Long id;
    private LocalDate date;
    private Long courseId;
    private CourseDto courseDto;

    private Set<AttendanceDto> attendancesDto;
}