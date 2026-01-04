package com.example.dto;

import com.example.enums.AchievementType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AchievementDTO {
    private String id;
    private String title;
    private String description;
    private String attachId;
    private LocalDate achievementDate;
    private AchievementType type;
}