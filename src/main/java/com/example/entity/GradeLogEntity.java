package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "grade_change_log")
public class GradeLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gradeId;
    private String whoChangedId; // Admin yoki Teacher ID
    private Integer oldGrade;
    private Integer newGrade;
    private String reason;
    private LocalDateTime changeDate = LocalDateTime.now();
    private String schoolId;
}