package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalStatsDTO {
    private Long totalSchools;
    private Long totalStudents;
    private Long totalTeachers;
    private Long totalEvents;
    private List<TopSchoolDTO> topSchools;
}