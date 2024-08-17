package org.example.library.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "student")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Student implements  UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roll", nullable = false)
    private Integer roll;

    private String name;

    @Column(unique = true)
    @NotBlank(message = "email should not be blank")
    @Email(message = "email should be unique")
    private String email;


    @NotBlank(message = "password should not be blank")
    private String password;

    @Column(unique = true)
    @NotBlank(message = "Enter Valid Mobile Number")
    @Pattern(regexp = "(^$|[0-9]{10})")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Select gender")
    private String gender;
    @NotBlank(message = "Enter Address")
    private String address;

    @NotNull(message = "Enter your semester")
    private int semester;

    @NotBlank(message = "Enter department")
    private String department;

    @NotNull(message = "please enter number of book issue")
    private int noOfBookIssue;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    private List<Book> books= new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public String toString() {
        return "Student{" +
                "roll=" + roll +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}