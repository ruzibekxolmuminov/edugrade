package com.example.repository;

import com.example.entity.EventEntity;
import com.example.enums.GeneralStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Integer> {
    Page<EventEntity> findAllBySchoolIdAndStatus(String schoolId, GeneralStatus status, Pageable pageable);

    @Query("SELECT COUNT(e) FROM EventEntity e WHERE e.status = 'ACTIVE'")
    Long countActiveEvents();
}