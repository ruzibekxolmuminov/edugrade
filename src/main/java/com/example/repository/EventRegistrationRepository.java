package com.example.repository;

import com.example.entity.EventRegistrationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistrationEntity, Long> {
    boolean existsByEventIdAndStudentId(Integer eventId, String studentId);
    Page<EventRegistrationEntity> findAllByEventId(Integer eventId, Pageable pageable);
}