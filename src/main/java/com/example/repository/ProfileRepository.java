package com.example.repository;

import com.example.entity.ProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, String>, JpaSpecificationExecutor<ProfileEntity> {
    Optional<ProfileEntity> findByPassportNumberAndVisibleIsTrue(String username);
    Optional<ProfileEntity> findByPinflOrPassportNumber(String pinfl, String passportNumber);
    Optional<ProfileEntity> findByIdAndVisibleIsTrue(String id);
    @Query("From ProfileEntity p inner join fetch p.roles where p.visible = true")
    Page<ProfileEntity> findAllWithRoles(Pageable pageable);
    @Query("from ProfileEntity as p join ProfileRoleEntity as pr on p.id = pr.profileId where p.id = ?1 and pr.roles = 'ROLE_MODERATOR'")
    Optional<ProfileEntity> isModerator(String id);

    @Query("from ProfileEntity as p join ProfileRoleEntity as pr on p.id = pr.profileId where p.id = ?1 and pr.roles = 'ROLE_ADMIN'")
    Optional<ProfileEntity> isAdmin(String s);
}
