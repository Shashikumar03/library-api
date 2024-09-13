package org.example.library.repositories;


import org.example.library.entities.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository  extends JpaRepository<Attendance,Long> {

    List<Attendance> findByStudentId(Integer studentId);
}