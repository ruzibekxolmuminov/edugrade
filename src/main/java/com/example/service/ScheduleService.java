package com.example.service;

import com.example.dto.schedule.ScheduleDTO;
import com.example.dto.schedule.ScheduleCreateDTO;
import com.example.dto.schedule.ScheduleUpdateDTO;
import com.example.entity.ChatMember;
import com.example.entity.GroupEntity;
import com.example.entity.ProfileEntity;
import com.example.entity.ScheduleEntity;
import com.example.enums.ChatRole;
import com.example.exp.AppBadException;
import com.example.repository.ChatMemberRepository;
import com.example.repository.GroupRepository;
import com.example.repository.ProfileRepository;
import com.example.repository.ScheduleRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ChatMemberRepository chatMemberRepository;
    @Autowired
    private GroupRepository groupRepository;

    public @Nullable ScheduleDTO create(ScheduleCreateDTO scheduleDTO) {
        Optional<ScheduleEntity> isExist = scheduleRepository.findByRoomNumberAndStartTimeAndEndTimeAndWeekDay(scheduleDTO.getRoomNumber(), scheduleDTO.getStartTime(), scheduleDTO.getEndTime(), scheduleDTO.getWeekDay());
        if (isExist.isPresent()) {
            throw new AppBadException("The schedule already exists!");
        }
        Optional<ScheduleEntity> isTeacherBusy = scheduleRepository.findByTeacherIdAndSubjectIdAndWeekDayAndStartTimeAndEndTime(scheduleDTO.getTeacherId(), scheduleDTO.getSubjectId(), scheduleDTO.getWeekDay(), scheduleDTO.getStartTime(), scheduleDTO.getEndTime());
        if (isTeacherBusy.isPresent()) {
            throw new AppBadException("Teacher is busy at that moment!");
        }
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setRoomNumber(scheduleDTO.getRoomNumber());
        scheduleEntity.setStartTime(scheduleDTO.getStartTime());
        scheduleEntity.setEndTime(scheduleDTO.getEndTime());
        scheduleEntity.setWeekDay(scheduleDTO.getWeekDay());
        scheduleEntity.setGroupId(scheduleDTO.getGroupId());
        scheduleEntity.setTeacherId(scheduleDTO.getTeacherId());
        scheduleEntity.setSubjectId(scheduleDTO.getSubjectId());
        scheduleEntity.setSchoolId(scheduleDTO.getSchoolId());
        scheduleEntity.setPairNumber(scheduleDTO.getPairNumber());
        scheduleEntity.setVisible(true);
        scheduleEntity.setCreatedDate(LocalDateTime.now());
        scheduleRepository.save(scheduleEntity);
        boolean isAlreadyMember = chatMemberRepository.existsByProfileIdAndGroupId(scheduleDTO.getTeacherId(), scheduleDTO.getGroupId());
        if (!isAlreadyMember){
            ChatMember teacherMember = new ChatMember();
            teacherMember.setGroup(scheduleEntity.getGroup());
            teacherMember.setProfile(profileRepository.getById(scheduleDTO.getTeacherId()));
            teacherMember.setRole(ChatRole.TEACHER);
            chatMemberRepository.save(teacherMember);

            GroupEntity group = groupRepository.getById(scheduleDTO.getGroupId());
            group.setMemberCount(group.getMemberCount() + 1);
            groupRepository.save(group);
        }

        return toDTO(scheduleEntity);
    }

    private @Nullable ScheduleDTO toDTO(ScheduleEntity scheduleEntity) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(scheduleEntity.getId());
        scheduleDTO.setRoomNumber(scheduleEntity.getRoomNumber());
        scheduleDTO.setStartTime(scheduleEntity.getStartTime());
        scheduleDTO.setEndTime(scheduleEntity.getEndTime());
        scheduleDTO.setWeekDay(scheduleEntity.getWeekDay());
        scheduleDTO.setGroupId(scheduleEntity.getGroupId());
        scheduleDTO.setTeacherId(scheduleEntity.getTeacherId());
        scheduleDTO.setSubjectId(scheduleEntity.getSubjectId());
        scheduleDTO.setSchoolId(scheduleEntity.getSchoolId());
        scheduleDTO.setPairNumber(scheduleEntity.getPairNumber());
        scheduleDTO.setVisible(scheduleEntity.getVisible());
        scheduleDTO.setCreatedDate(scheduleEntity.getCreatedDate());
        return scheduleDTO;

    }

    public @Nullable String update(String id, ScheduleUpdateDTO scheduleUpdateDTO) {
        ScheduleEntity schedule = getById(id);
        if (scheduleUpdateDTO.getStartTime() != null) {
            schedule.setStartTime(scheduleUpdateDTO.getStartTime());
        } else if (scheduleUpdateDTO.getEndTime() != null) {
            schedule.setEndTime(scheduleUpdateDTO.getEndTime());
        } else if (scheduleUpdateDTO.getWeekDay() != null) {
            schedule.setWeekDay(scheduleUpdateDTO.getWeekDay());
        } else if (scheduleUpdateDTO.getGroupId() != null) {
            schedule.setGroupId(scheduleUpdateDTO.getGroupId());
        } else if (scheduleUpdateDTO.getTeacherId() != null) {
            schedule.setTeacherId(scheduleUpdateDTO.getTeacherId());
            ChatMember group = chatMemberRepository.getByProfile(profileRepository.getById(scheduleUpdateDTO.getTeacherId()));
            group.setProfile(profileRepository.getById(scheduleUpdateDTO.getTeacherId()));
            chatMemberRepository.save(group);
        } else if (scheduleUpdateDTO.getSubjectId() != null) {
            schedule.setSubjectId(scheduleUpdateDTO.getSubjectId());
        } else if (scheduleUpdateDTO.getPairNumber() != null) {
            schedule.setPairNumber(scheduleUpdateDTO.getPairNumber());
        }
        scheduleRepository.save(schedule);
        return "Muvaffaqiyatli o'zgartirildi!";
    }

    private ScheduleEntity getById(String id) {
        Optional<ScheduleEntity> isExist = scheduleRepository.findByIdAndVisibleTrue(id);
        if (isExist.isEmpty()) {
            throw new AppBadException("Schedule not found!");
        }
        return isExist.get();
    }

    public @Nullable String delete(String id) {
        ScheduleEntity schedule = getById(id);
        schedule.setVisible(false);
        scheduleRepository.delete(schedule);
        return "Muvaffaqiyatli o'chirildi!";
    }

    public List<ScheduleDTO> getByGroupId(String id) {
        // 1. Guruhga tegishli barcha darslarni olish
        List<ScheduleEntity> scheduleList = scheduleRepository.getByGroupId(id);

        if (scheduleList.isEmpty()) {
            throw new AppBadException("Schedule with group Id not found!");
        }

        // 2. Entity ro'yxatini DTO ro'yxatiga o'girish
        return scheduleList.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public @Nullable List<ScheduleDTO> getByTeacherId(String id) {
        List<ScheduleEntity> scheduleList = scheduleRepository.getByTeacherId(id);

        if (scheduleList.isEmpty()) {
            throw new AppBadException("Schedule with group Id not found!");
        }

        // 2. Entity ro'yxatini DTO ro'yxatiga o'girish
        return scheduleList.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    public @Nullable List<ScheduleDTO> getBySubjectId(String id) {
        Optional<ProfileEntity> profile = profileRepository.getGroupIdById(id);
        if (profile.isEmpty()) {
            throw new AppBadException("Profile with group Id not found!");
        }
        return getByGroupId(profile.get().getGroupId());
    }
}
