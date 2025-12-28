package com.example.repository;

import com.example.entity.ProfileEntity;
import com.example.enums.ProfileStatus;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, String> {
    Optional<ProfileEntity> findByPassportNumberAndVisibleIsTrue(String username);
    Optional<ProfileEntity> findByPinflOrPassportNumber(String pinfl, String passportNumber);
    Optional<ProfileEntity> findByIdAndVisibleIsTrue(String id);

    @Query("from ProfileEntity as p join ProfileRoleEntity as pr on p.id = pr.profileId where p.id = ?1 and pr.roles = 'ROLE_MODERATOR'")
    Optional<ProfileEntity> isModerator(String id);
}
