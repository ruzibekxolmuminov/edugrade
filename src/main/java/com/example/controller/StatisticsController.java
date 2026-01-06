package com.example.controller;

import com.example.dto.GlobalStatsDTO;
import com.example.dto.SchoolStatsDTO;
import com.example.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/statistics")
@Tag(name = "Statistics Controller", description = "Admin va Moderatorlar uchun tahliliy ma'lumotlar")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @Operation(summary = "Maktab statistikasi", description = "Faqat maktab admini o'z maktabi bo'yicha tahlillarni ko'ra oladi")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/school/{schoolId}")
    public ResponseEntity<SchoolStatsDTO> getSchoolStats(@PathVariable String schoolId) {
        return ResponseEntity.ok(statisticsService.getSchoolStats(schoolId));
    }

    @Operation(summary = "Global statistika", description = "Faqat tizim moderatori barcha maktablar bo'yicha umumiy statistikani ko'ra oladi")
    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/global")
    public ResponseEntity<GlobalStatsDTO> getGlobalStats() {
        return ResponseEntity.ok(statisticsService.getGlobalStats());
    }
}