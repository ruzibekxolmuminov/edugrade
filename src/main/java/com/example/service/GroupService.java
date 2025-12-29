package com.example.service;

import com.example.dto.GroupProfileDTO;
import com.example.dto.group.GroupCreateDTO;
import com.example.dto.group.GroupDTO;
import com.example.dto.group.GroupUpdateDTO;
import com.example.dto.profile.ProfileInfoDTO;
import com.example.entity.GroupEntity;
import com.example.entity.ProfileEntity;
import com.example.exp.AppBadException;
import com.example.exp.Exist;
import com.example.repository.GroupRepository;
import com.example.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ProfileRepository profileRepository;

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
        groupDTO.setLevel(groupEntity.getLevel());
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

    public String assignStudentToGroup(String studentId, String groupId) {
        checkGroupExists(groupId);
        ProfileEntity student = profileRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("O'quvchi topilmadi"));

        student.setGroupId(groupId);
        profileRepository.save(student);
        return "O'quvchi guruhga biriktirildi";
    }

    // 6.7 Bulk Assign Students
    @Transactional
    public String bulkAssign(List<String> studentIds, String groupId) {
        checkGroupExists(groupId);

        for (String studentId : studentIds) {
            profileRepository.findById(studentId).ifPresent(student -> {
                student.setGroupId(groupId);
                profileRepository.save(student);
            });
        }
        return studentIds.size() + " ta o'quvchi muvaffaqiyatli guruhga qo'shildi";
    }

    // 6.8 Get Students by Group
    public List<GroupProfileDTO> getStudentsByGroup(String groupId) {
        checkGroupExists(groupId);
        List<ProfileEntity> entities = profileRepository.findAllByGroupIdAndVisibleTrue(groupId);

        return entities.stream().map(entity -> {
            GroupProfileDTO dto = new GroupProfileDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getFirstname());
            dto.setSurname(entity.getLastname());
            dto.setPatronymic(entity.getPatronymic());
            return dto;
        }).collect(Collectors.toList());
    }

    private void checkGroupExists(String groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new RuntimeException("Guruh topilmadi!");
        }
    }

    public PageImpl<GroupDTO> getAllBySchoolId(String schoolId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<GroupEntity> pageObj = groupRepository.findAllBySchoolIdAndVisibleTrue(schoolId, pageable);

        List<GroupDTO> dtoList = pageObj.getContent().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, pageObj.getTotalElements());
    }
}
