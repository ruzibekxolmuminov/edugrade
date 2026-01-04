package com.example.repository;

import com.example.entity.GradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<GradeEntity, String> {
    List<GradeEntity> findAllByStudentIdAndSubjectIdAndVisibleTrue(String studentId, Integer subjectId);
}