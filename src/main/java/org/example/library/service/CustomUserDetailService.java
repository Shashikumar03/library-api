package org.example.library.service;

import org.example.library.dto.StudentDto;
import org.example.library.entities.Admin;
//import org.example.library.entities.Role;
import org.example.library.entities.Student;
import org.example.library.exceptions.ApiException;
import org.example.library.exceptions.ResourceNotFoundException;
import org.example.library.repositories.AdminRepository;
import org.example.library.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StudentRepository studentRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = this.adminRepository.findByAdminEmail(username).orElse(null);
        Student student = this.studentRepository.findByEmail(username).orElse(null);

        if (admin != null) {
            return User.withUsername(admin.getAdminEmail())
                    .password(admin.getAdminPassword())
                    .build();
        } else if (student != null) {

            UserDetails build = User.withUsername(student.getEmail())
                    .password(student.getPassword())
                    .build();

            return  build;
        } else {
            throw new ApiException("User not found with username: " + username);
        }

    }
}
