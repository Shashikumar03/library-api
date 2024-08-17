package org.example.library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {


    @NotEmpty(message = "please give  email")
    @Email
    private String email;
    @NotEmpty(message = "please enter name")
    private String name;
}