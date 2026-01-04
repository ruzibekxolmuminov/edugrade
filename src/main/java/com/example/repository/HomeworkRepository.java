package com.example.repository;

import com.example.entity.HomeworkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HomeworkRepository extends JpaRepository<HomeworkEntity, String> {

    Optional<HomeworkEntity> findByTeacherIdAndSubjectIdAndGroupIdAndContentAndVisibleTrue(String teacherId, Integer subjectId, String groupId, String content);

    Optional<HomeworkEntity> findByIdAndVisibleTrue(String id);
}
