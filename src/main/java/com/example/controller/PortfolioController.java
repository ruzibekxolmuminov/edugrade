package com.example.controller;

import com.example.dto.AchievementDTO;
import com.example.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/portfolio")
@RequiredArgsConstructor
public class PortfolioController {
    private final AchievementService achievementService;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/achievement")
    public ResponseEntity<AchievementDTO> create(@RequestBody AchievementDTO dto) {
        return ResponseEntity.ok(achievementService.create(dto));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/achievement/{id}")
    public ResponseEntity<String> update(@PathVariable String id, @RequestBody AchievementDTO dto) {
        return ResponseEntity.ok(achievementService.update(id, dto));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @DeleteMapping("/achievement/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        return ResponseEntity.ok(achievementService.delete(id));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/")
    public ResponseEntity<List<AchievementDTO>> getPortfolio() {
        return ResponseEntity.ok(achievementService.getPortfolioByStudentId());
    }
}