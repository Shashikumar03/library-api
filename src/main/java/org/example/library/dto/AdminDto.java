package org.example.library.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.library.entities.Role;
//import org.example.library.entities.Role;
//import org.example.library.entities.Role;

import java.util.HashSet;
import java.util.Set;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {

    private  int id;
    @NotEmpty(message = "please give admin email")
    @Email
    private String adminEmail;

    @NotEmpty(message = "give give password")
    @Size(min = 3, max = 12, message = "password shoulbd be 3 to 12")
    private  String adminPassword;

    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    public String getAdminPassword() {
        return adminPassword;
    }

    @JsonProperty
    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }





}
