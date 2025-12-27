package com.example.repository;

import com.example.entity.ProfileEntity;
import com.example.enums.ProfileStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, String> {
    Optional<ProfileEntity> findByPassportNumberAndVisibleIsTrue(String username);
    Optional<ProfileEntity> findByPinflOrPassportNumberOrPassportSeries(String pinfl, String passportNumber, String passportSeries);
    @Transactional
    @Modifying
    @Query("update ProfileEntity set status =?1 where passportNumber =?2")
    void setStatusByPassportNumber(ProfileStatus status, String passportNumber);
}
