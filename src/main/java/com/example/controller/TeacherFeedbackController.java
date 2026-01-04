package com.example.controller;

import com.example.dto.TeacherFeedbackDTO;
import com.example.service.TeacherFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/feedback")
@RequiredArgsConstructor
public class TeacherFeedbackController {
    private final TeacherFeedbackService feedbackService;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/post")
    public ResponseEntity<String> postFeedback(@RequestBody TeacherFeedbackDTO dto) {
        return ResponseEntity.ok(feedbackService.postFeedback(dto));
    }

    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<Page<TeacherFeedbackDTO>> getFeedbacks(
            @PathVariable String teacherId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(feedbackService.getTeacherFeedbacks(teacherId, page, size));
    }
}