package org.example.library.dto;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.library.entities.Book;
import org.example.library.entities.Role;
//import org.example.library.entities.Role;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {



    private Integer roll;
    @NotBlank(message = "name should not be blank")
    @Size(min = 3, max = 12, message = "name should be 3 to 12 character")
    private String name;

    @Email(message = "email should be unique")
    @Column(unique = true)
    @NotBlank(message = "plz give proper email")
    private String email;

    @NotBlank(message = "password should not be blank")
    @Size(min = 3, max = 12, message = "password should be 3 to 12 character")
    private String password;


    @NotBlank(message = "Enter Valid Mobile Number")
    @Pattern(regexp = "(^$|[0-9]{10})")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Select gender")
    private String gender;
//
    @NotBlank(message = "Enter Address")
    private String address;
    @NotNull(message = "Enter Semester")
    private int semester;

    @NotBlank(message = "Enter department")
    private String department;

    @NotNull(message = "no of issue book can't be empty")
    private int noOfBookIssue;

    private Set<Role> roles = new HashSet<>();
    private List<BookDto> booksDto= new ArrayList<>();

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password=password;
    }



}