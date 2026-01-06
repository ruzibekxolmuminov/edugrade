package com.example.controller;

import com.example.dto.*;
import com.example.entity.GradeEntity;
import com.example.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.example.util.SpringSecurityUtil.currentProfileId;

@RestController
@RequestMapping("/v1/grade")
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody GradeCreateDTO dto) {
        String teacherId = currentProfileId();
        return ResponseEntity.ok(gradeService.createGrade(dto, teacherId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable String id, @RequestBody GradeUpdateDTO dto) {
        String modifierId = currentProfileId();
        return ResponseEntity.ok(gradeService.updateGrade(id, dto, modifierId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<GradeDTO>> getGrades(@PathVariable String studentId,
                                                    @RequestParam Integer subjectId) {
        return ResponseEntity.ok(gradeService.getStudentGrades(studentId, subjectId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/log/{schoolId}")
    public ResponseEntity<Page<GradeLogResponseDTO>> getLogs(@PathVariable String schoolId,
                                                             @RequestParam int page,
                                                             @RequestParam int size) {
        return ResponseEntity.ok(gradeService.getGradeLogs(schoolId, page, size));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/grade-book")
    public ResponseEntity<GradebookTableDTO> getGradebook(
            @RequestParam String groupId,
            @RequestParam Integer subjectId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(gradeService.getGradebook(groupId, subjectId, startDate, endDate));
    }
}