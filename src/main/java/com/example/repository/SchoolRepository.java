package com.example.repository;

import com.example.entity.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<SchoolEntity, String> {
    Optional<SchoolEntity> findByNameAndNumberAndAddress(String schoolName, Integer number, String address);
}
