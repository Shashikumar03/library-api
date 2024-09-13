package org.example.library.dto;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

    private Long id;
    private String subject;
    private Integer semester;

    private String teacher;

    //    @JsonManagedReference
    private Set<StudentDto> studentsDtos;


}