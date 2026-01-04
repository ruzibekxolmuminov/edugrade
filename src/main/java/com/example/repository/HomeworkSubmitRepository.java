package com.example.repository;

import com.example.entity.HomeworkSubmitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HomeworkSubmitRepository extends JpaRepository<HomeworkSubmitEntity, Integer> {
    Optional<HomeworkSubmitEntity> findByHomeworkIdAndStudentId(String homeworkId, String studentId);
}
