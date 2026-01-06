package com.example.service;


import com.example.dto.WorkPlanCreateDTO;
import com.example.dto.WorkPlanDTO;
import com.example.entity.WorkPlanEntity;
import com.example.repository.WorkPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkPlanService {
    private final WorkPlanRepository workPlanRepository;

    public WorkPlanDTO create(WorkPlanCreateDTO dto, String teacherId) {
        WorkPlanEntity entity = new WorkPlanEntity();
        entity.setSubjectId(dto.getSubjectId());
        entity.setTeacherId(teacherId);
        entity.setSchoolId(dto.getSchoolId());
        entity.setTitle(dto.getTitle());
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);

        workPlanRepository.save(entity);
        return toDTO(entity);
    }

    public List<WorkPlanDTO> getMyWorkPlan(String teacherId, Integer subjectId) {
        return workPlanRepository.findAllByTeacherIdAndSubjectIdAndVisibleTrueOrderByOrderNumberAsc(teacherId, subjectId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public String delete(String id) {
        WorkPlanEntity entity = workPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Work plan not found"));
        entity.setVisible(false);
        workPlanRepository.save(entity);
        return "Work plan item deleted";
    }

    private WorkPlanDTO toDTO(WorkPlanEntity entity) {
        WorkPlanDTO dto = new WorkPlanDTO();
        dto.setId(entity.getId());
        dto.setSubjectId(entity.getSubjectId());
        dto.setTeacherId(entity.getTeacherId());
        dto.setSchoolId(entity.getSchoolId());
        dto.setTitle(entity.getTitle());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
}