package com.example.repository;

import com.example.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, String> {
    Optional<AttendanceEntity> findByStudentIdAndDateAndScheduleIdAndSchoolId(String studentId, LocalDate date, String scheduleId, String schoolId);

    List<AttendanceEntity> getByStudentId(String id);

    List<AttendanceEntity> getAllByGroupId(String id);

    @Query("SELECT (COUNT(CASE WHEN a.status = 'PRESENT' THEN 1 END) * 100.0 / COUNT(a)) " +
            "FROM AttendanceEntity a " +
            "JOIN ProfileRoleEntity pr ON a.studentId = pr.profileId " +
            "WHERE a.schoolId = :schoolId AND pr.roles = 'ROLE_STUDENT'")
    Double getAttendancePercentageBySchool(@Param("schoolId") String schoolId);

    List<AttendanceEntity> findAllByGroupIdAndDateBetween(
            String groupId, LocalDate start, LocalDate end);

    List<AttendanceEntity> findAllByStudentIdAndSubjectId(String studentId, Integer subjectId);

    Optional<AttendanceEntity> findByStudentIdAndDateAndScheduleIdAndSchoolIdAndSubjectId(String studentId, LocalDate date, String scheduleId, String schoolId, Integer subjectId);

    @Query("SELECT a FROM AttendanceEntity a " +
            "WHERE a.studentId = :studentId " +
            "AND EXTRACT(MONTH FROM a.date) = :monthValue " +
            "AND EXTRACT(YEAR FROM a.date) = :year")
    List<AttendanceEntity> findByStudentIdAndMonthAndYear(
            @Param("studentId") String studentId,
            @Param("monthValue") int monthValue,
            @Param("year") int year);
}
