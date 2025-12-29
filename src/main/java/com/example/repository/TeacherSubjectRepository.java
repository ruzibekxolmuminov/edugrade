package com.example.repository;

import com.example.entity.TeacherSubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TeacherSubjectRepository extends JpaRepository<TeacherSubjectEntity, Integer> {
    
    Optional<TeacherSubjectEntity> findByTeacherIdAndSubjectIdAndSchoolIdAndVisibleTrue(
            String teacherId, Integer subjectId, String schoolId);
}