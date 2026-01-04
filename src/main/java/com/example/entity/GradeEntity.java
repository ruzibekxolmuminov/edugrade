package com.example.entity;

import com.example.enums.GradeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "grade")
public class GradeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "teacher_id")
    private String teacherId;

    @Column(name = "subject_id")
    private Integer subjectId;

    @Column(name = "schedule_id")
    private String scheduleId;

    @Column(name = "school_id")
    private String schoolId;

    private Integer gradeValue; // Masalan: 5, 85, 100

    @Enumerated(EnumType.STRING)
    private GradeType gradeType; // DAILY, MIDTERM, FINAL

    private Integer weight; // Bahoning og'irlik darajasi

    @Column(columnDefinition = "TEXT")
    private String comment;
    private LocalDateTime createdDate = LocalDateTime.now();
    
    // Audit: Kim tomonidan o'zgartirildi
    private String updatedBy;
    private LocalDateTime updatedDate;
    private Boolean visible;
}