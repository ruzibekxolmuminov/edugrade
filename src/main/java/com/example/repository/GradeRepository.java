package com.example.repository;

import com.example.entity.GradeEntity;
import com.example.enums.GradeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<GradeEntity, String> {
    List<GradeEntity> findAllByStudentIdAndSubjectIdAndVisibleTrue(String studentId, Integer subjectId);

    @Query("SELECT AVG(g.gradeValue) FROM GradeEntity g " +
            "JOIN ProfileRoleEntity pr ON g.studentId = pr.profileId " +
            "WHERE g.schoolId = :schoolId AND pr.roles = 'ROLE_STUDENT'")
    Double getAverageGpaBySchool(@Param("schoolId") String schoolId);

    List<GradeEntity> findAllByGroupIdAndSubjectIdAndLessonDateBetweenAndVisibleTrue(
            String groupId, Integer subjectId, LocalDate start, LocalDate end);

    List<GradeEntity> findAllByGroupIdAndSubjectIdAndLessonDateBetween(
            String groupId, Integer subjectId, LocalDate start, LocalDate end);

    Optional<GradeEntity> findByStudentIdAndSubjectIdAndGradeTypeAndVisibleTrue(String studentId, Integer subjectId, GradeType type);

    Optional<GradeEntity> findByStudentIdAndSubjectIdAndGradeTypeAndVisibleTrueAndLessonDate(String studentId, Integer subjectId, GradeType type, LocalDate date);

    boolean existsByStudentIdAndSubjectIdAndGradeType(String studentId, Integer subjectId, GradeType gradeType);
}