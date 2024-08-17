package org.example.library.repositories;

import org.example.library.dto.StudentDto;
import org.example.library.entities.Role;
import org.example.library.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StudentRepository extends JpaRepository<Student,Integer>{

    Optional<Student> findByEmail(String username);
    boolean existsByPhoneNumber(String phone);
    boolean existsByEmail(String email);



    Optional<Student> findByphoneNumber(String phoneNumber);


}
