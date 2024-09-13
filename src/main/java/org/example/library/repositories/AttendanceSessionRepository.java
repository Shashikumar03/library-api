package org.example.library.repositories;


import org.example.library.entities.AttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long> {
    List<AttendanceSession> findAllByCourseId(Long courseId);

    List<AttendanceSession> findByCourseIdAndDate(Long courseId, LocalDate date);
}