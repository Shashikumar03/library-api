package org.example.library.repositories;

import org.example.library.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository  extends JpaRepository<Admin,Integer> {
    Optional<Admin> findByAdminEmail(String email);

    boolean existsByAdminEmail(String email);

}
