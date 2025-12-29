package com.example.repository;

import com.example.entity.FacultyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<FacultyEntity, String> {

    Optional<FacultyEntity> findByIdAndVisibleTrue(String id);

    List<FacultyEntity> findAllBySchoolIdAndVisibleTrue(String schoolId);

    Optional<FacultyEntity> findByNameAndSchoolIdAndVisibleTrue(String name, String schoolId);
}
