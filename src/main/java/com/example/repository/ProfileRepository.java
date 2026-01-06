package com.example.repository;

import com.example.entity.ProfileEntity;
import com.example.enums.profile.ProfileRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, String>, JpaSpecificationExecutor<ProfileEntity> {

    Optional<ProfileEntity> findByPassportNumberAndVisibleIsTrue(String username);
    Optional<ProfileEntity> findByPinflOrPassportNumber(String pinfl, String passportNumber);
    Optional<ProfileEntity> findByIdAndVisibleIsTrue(String id);

    @Query("SELECT p FROM ProfileEntity p JOIN ProfileRoleEntity pr ON p.id = pr.profileId WHERE p.id = :id AND pr.roles = com.example.enums.profile.ProfileRoleEnum.ROLE_MODERATOR")
    Optional<ProfileEntity> isModerator(@Param("id") String id);

    List<ProfileEntity> findAllByGroupIdAndVisibleTrue(String groupId);

    @Query("SELECT COUNT(p) FROM ProfileEntity p " +
            "JOIN ProfileRoleEntity pr ON p.id = pr.profileId " +
            "WHERE p.schoolId = :schoolId AND pr.roles = :role AND p.visible = true")
    Long countBySchoolIdAndRole(@Param("schoolId") String schoolId, @Param("role") ProfileRoleEnum role);

    @Query("SELECT COUNT(p) FROM ProfileEntity p " +
            "JOIN ProfileRoleEntity pr ON p.id = pr.profileId " +
            "WHERE pr.roles = :role AND p.visible = true")
    Long countByRole(@Param("role") ProfileRoleEnum role);

    @Query(value = """
        SELECT 
            s.id as schoolId, 
            s.name as schoolName,
            COALESCE(AVG(g.grade_value), 0) as averageGrade,
            COALESCE((COUNT(CASE WHEN att.status = 'PRESENT' THEN 1 END) * 100.0) / NULLIF(COUNT(att.id), 0), 0) as attendancePercentage
        FROM school s
        LEFT JOIN grade g ON g.school_id = s.id
        LEFT JOIN attendance att ON att.school_id = s.id
        GROUP BY s.id, s.name
        ORDER BY averageGrade DESC, attendancePercentage DESC
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> getTopSchoolsNative();

    @Query("SELECT COUNT(s) FROM SchoolEntity s")
    Long countTotalSchools();

    // TO'G'RILANGAN QISM: String o'rniga Enum ishlatildi
    @Query("SELECT COUNT(p) FROM ProfileEntity p JOIN ProfileRoleEntity pr ON p.id = pr.profileId WHERE pr.roles = com.example.enums.profile.ProfileRoleEnum.ROLE_STUDENT AND p.visible = true")
    Long countTotalStudents();

    // TO'G'RILANGAN QISM: String o'rniga Enum ishlatildi
    @Query("SELECT COUNT(p) FROM ProfileEntity p JOIN ProfileRoleEntity pr ON p.id = pr.profileId WHERE pr.roles = com.example.enums.profile.ProfileRoleEnum.ROLE_TEACHER AND p.visible = true")
    Long countTotalTeachers();
}
