package com.example.controller;

import com.example.dto.GradeSubmissionDTO;
import com.example.dto.HomeworkSubmit;
import com.example.dto.SubmissionDTO;
import com.example.dto.homework.HomeworkCreateDTO;
import com.example.dto.homework.HomeworkDTO;
import com.example.dto.homework.HomeworkUpdateDTO;
import com.example.service.HomeworkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/homework")
@Tag(name = "Homework APIs", description = "Api for homework controller")
public class HomeworkController {
    @Autowired
    private HomeworkService homeworkService;

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/create")
    @Operation(summary = "Create homework", description = "Api used for create homework by teacher")
    public ResponseEntity<HomeworkDTO> createHomework(@RequestBody HomeworkCreateDTO homeworkDTO) {
        return ResponseEntity.ok(homeworkService.create(homeworkDTO));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/update/{id}")
    @Operation(summary = "Update homework", description = "Api used for update homework by teacher")
    public ResponseEntity<String> updateHomework(@RequestBody HomeworkUpdateDTO homeworkUpdateDTO, @PathVariable String id) {
        return ResponseEntity.ok(homeworkService.update(homeworkUpdateDTO, id));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/submit")
    @Operation(summary = "Submit homework", description = "Api used for submit homework by student")
    public ResponseEntity<String> submitHomework(@RequestBody SubmissionDTO submit) {
        return ResponseEntity.ok(homeworkService.submitHomework(submit));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/grade-homework")
    @Operation(summary = "Grade submit homework", description = "Api used for grade submit homework by teacher")
    public ResponseEntity<String> gradeHomework(@RequestBody GradeSubmissionDTO submit) {
        return ResponseEntity.ok(homeworkService.gradeSubmission(submit));
    }

}
