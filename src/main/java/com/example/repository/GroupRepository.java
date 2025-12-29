package com.example.repository;

import com.example.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, String> {
    Optional<GroupEntity> findByNameAndFacultyIdAndMentorIdAndSchoolIdAndVisibleTrue(String groupName, String facultyId, String mentorId, String schoolId);

    Optional<GroupEntity> getByIdAndVisibleTrue(String id);

    List<GroupEntity> findAllBySchoolIdAndVisibleTrue(String schoolId);
}
