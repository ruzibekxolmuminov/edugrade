package com.example.controller;


import com.example.dto.WorkPlanCreateDTO;
import com.example.dto.WorkPlanDTO;
import com.example.service.WorkPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.util.SpringSecurityUtil.currentProfileId;

@RestController
@RequestMapping("/v1/work-plan")
@RequiredArgsConstructor
public class WorkPlanController {
    private final WorkPlanService workPlanService;

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/create")
    public ResponseEntity<WorkPlanDTO> create(@RequestBody WorkPlanCreateDTO dto) {
        String teacherId = currentProfileId();
        return ResponseEntity.ok(workPlanService.create(dto, teacherId));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/my-plan/{subjectId}")
    public ResponseEntity<List<WorkPlanDTO>> getMyPlan(@PathVariable Integer subjectId) {
        String teacherId = currentProfileId();
        return ResponseEntity.ok(workPlanService.getMyWorkPlan(teacherId, subjectId));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        return ResponseEntity.ok(workPlanService.delete(id));
    }
}