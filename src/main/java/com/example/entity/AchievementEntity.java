package com.example.entity;

import com.example.enums.AchievementType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "achievement")
public class AchievementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "attach_id")
    private String attachId; // Fayl (rasm yoki PDF)

    private LocalDate achievementDate;

    @Enumerated(EnumType.STRING)
    private AchievementType type;

    private LocalDateTime createdDate = LocalDateTime.now();
    private Boolean visible = true;
}