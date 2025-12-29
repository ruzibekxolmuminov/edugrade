package com.example.service;

import com.example.dto.GroupCreateDTO;
import com.example.dto.GroupDTO;
import com.example.dto.GroupUpdateDTO;
import com.example.entity.FacultyEntity;
import com.example.entity.GroupEntity;
import com.example.exp.AppBadException;
import com.example.exp.Exist;
import com.example.repository.GroupRepository;
import org.hibernate.annotations.NotFound;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    public @Nullable GroupDTO create(GroupCreateDTO group) {
        Optional<GroupEntity> isExist = groupRepository.findByNameAndFacultyIdAndMentorIdAndSchoolIdAndVisibleTrue(group.getName(),group.getFacultyId(),group.getMentorId(),group.getSchoolId());
        if (isExist.isPresent()) {
            throw new Exist("Group already exists");
        }
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName(group.getName());
        groupEntity.setCreatedDate(LocalDateTime.now());
        groupEntity.setLevel(group.getLevel());
        groupEntity.setFacultyId(group.getFacultyId());
        groupEntity.setMentorId(group.getMentorId());
        groupEntity.setSchoolId(group.getSchoolId());
        groupEntity.setVisible(true);
        groupRepository.save(groupEntity);
        return toDTO(groupEntity);

    }

    private @Nullable GroupDTO toDTO(GroupEntity groupEntity) {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(groupEntity.getId());
        groupDTO.setName(groupEntity.getName());
        groupDTO.setFacultyId(groupEntity.getFacultyId());
        groupDTO.setMentorId(groupEntity.getMentorId());
        groupDTO.setSchoolId(groupEntity.getSchoolId());
        groupDTO.setVisible(groupEntity.getVisible());
        groupDTO.setCreatedAt(groupEntity.getCreatedDate());

        return groupDTO;
    }

    public @Nullable String update(String id, GroupUpdateDTO group) {
        GroupEntity entity = getById(id);
        if (group.getName() != null) {
            entity.setName(group.getName());
        }else if (group.getFacultyId() != null) {
            entity.setFacultyId(group.getFacultyId());
        }else if (group.getMentorId() != null) {
            entity.setMentorId(group.getMentorId());
        }else if (group.getLevel() != null) {
            entity.setLevel(group.getLevel());
        }
        groupRepository.save(entity);
        return "Group yangilandi!";
    }

    private GroupEntity getById(String id) {
        Optional<GroupEntity> group = groupRepository.getByIdAndVisibleTrue(id);
        if (group.isEmpty()){
            throw new AppBadException("Group not found");
        }
        return group.get();
    }

    public @Nullable String delete(String id) {
        GroupEntity entity = getById(id);
        entity.setVisible(false);
        groupRepository.save(entity);
        return "Group o'chirildi";
    }

    public @Nullable GroupDTO getGroup(String id) {
        GroupEntity entity = getById(id);
        return toDTO(entity);
    }

    public @Nullable List<GroupDTO> getAllBySchoolId(String schoolId) {
        List<GroupEntity> entities = groupRepository.findAllBySchoolIdAndVisibleTrue(schoolId);

        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
