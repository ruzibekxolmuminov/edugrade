package com.example.repository;

import com.example.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, String> {
    Optional<AttendanceEntity> findByStudentIdAndDateAndScheduleIdAndSchoolId(String studentId, LocalDate date, String scheduleId, String schoolId);

    Optional<AttendanceEntity> getByStudentId(String id);

    List<AttendanceEntity> getAllByGroupId(String id);
}
