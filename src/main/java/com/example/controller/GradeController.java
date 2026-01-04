package com.example.controller;

import com.example.dto.GradeCreateDTO;
import com.example.dto.GradeLogResponseDTO;
import com.example.dto.GradeUpdateDTO;
import com.example.entity.GradeEntity;
import com.example.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/grade")
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody GradeCreateDTO dto) {
        String teacherId = "current-teacher-id"; // SecurityContextHolder dan olinadi
        return ResponseEntity.ok(gradeService.createGrade(dto, teacherId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable String id, @RequestBody GradeUpdateDTO dto) {
        String modifierId = "current-user-id"; // SecurityContextHolder dan olinadi
        return ResponseEntity.ok(gradeService.updateGrade(id, dto, modifierId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<GradeEntity>> getGrades(@PathVariable String studentId,
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
}