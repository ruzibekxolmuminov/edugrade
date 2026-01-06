package com.example.entity;

import com.example.enums.GradeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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
    @Column(name = "group_id") // QO'SHILDI
    private String groupId;

    @Column(name = "subject_id")
    private Integer subjectId;

    @Column(name = "lesson_date")
    private LocalDate lessonDate;

    @Column(name = "schedule_id")
    private String scheduleId;

    @Column(name = "school_id")
    private String schoolId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private ProfileEntity student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", insertable = false, updatable = false)
    private ProfileEntity teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    private GroupEntity group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", insertable = false, updatable = false)
    private SubjectEntity subject;

    private Integer gradeValue; // Masalan: 5, 85, 100

    @Enumerated(EnumType.STRING)
    private GradeType gradeType; // DAILY, MIDTERM, FINAL

    @Column(columnDefinition = "TEXT")
    private String comment;
    private LocalDateTime createdDate = LocalDateTime.now();
    
    // Audit: Kim tomonidan o'zgartirildi
    private String updatedBy;
    private LocalDateTime updatedDate;
    private Boolean visible;
}