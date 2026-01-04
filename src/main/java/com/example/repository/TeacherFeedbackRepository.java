package com.example.repository;

import com.example.entity.TeacherFeedbackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherFeedbackRepository extends JpaRepository<TeacherFeedbackEntity, Long> {
    // Muayyan o'qituvchiga berilgan baholarni olish
    Page<TeacherFeedbackEntity> findAllByTeacherIdOrderByCreatedDateDesc(String teacherId, Pageable pageable);
}