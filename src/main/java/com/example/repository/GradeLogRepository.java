package com.example.repository;

import com.example.entity.GradeLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeLogRepository extends JpaRepository<GradeLogEntity, Long> {
    Page<GradeLogEntity> findAllBySchoolId(String schoolId, Pageable pageable);
}