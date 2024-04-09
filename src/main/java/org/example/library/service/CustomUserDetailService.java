package org.example.library.service;

import org.example.library.dto.StudentDto;
import org.example.library.entities.Admin;
//import org.example.library.entities.Role;
import org.example.library.entities.Role;
import org.example.library.entities.Student;
import org.example.library.exceptions.ApiException;
import org.example.library.exceptions.ResourceNotFoundException;
import org.example.library.repositories.AdminRepository;
import org.example.library.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = this.adminRepository.findByAdminEmail(username).orElse(null);
        Student student = this.studentRepository.findByEmail(username).orElse(null);

        if (admin != null) {

            UserDetails userDetails = User.withUsername(admin.getAdminEmail())
                    .password(admin.getAdminPassword())
                    .authorities(getAuthorities(admin.getRoles()))
                    .build();
            System.out.println(userDetails);
            return userDetails;
        } else if (student != null) {
            UserDetails build= User.withUsername(student.getEmail())
                    .password(student.getPassword())
                    .authorities(getAuthorities(student.getRoles()))
                    .build();
            System.out.println(build);
            return build;
        } else {
            throw new ApiException("User not found with username: " + username);
        }

    }
    private Set<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }
}
