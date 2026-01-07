package com.example.repository;

import com.example.entity.GroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, String> {
    Optional<GroupEntity> findByNameAndFacultyIdAndMentorIdAndSchoolIdAndVisibleTrue(String groupName, String facultyId, String mentorId, String schoolId);

    Optional<GroupEntity> getByIdAndVisibleTrue(String id);

    Page<GroupEntity> findAllBySchoolIdAndVisibleTrue(String schoolId, Pageable pageable);

    // GroupRepository.java ichida
    @Query("SELECT g.memberCount FROM GroupEntity g WHERE g.id = :id")
    Integer getMemberCountById(@Param("id") String id);


    Boolean existsGroupEntityByMentorId(String mentorId);
}
