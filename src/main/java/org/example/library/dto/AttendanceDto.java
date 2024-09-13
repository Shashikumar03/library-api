package org.example.library.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@ToString
public class AttendanceDto {

    private Long id;
    private Integer studentId;
    private StudentDto studentDto;
    private boolean present;
}