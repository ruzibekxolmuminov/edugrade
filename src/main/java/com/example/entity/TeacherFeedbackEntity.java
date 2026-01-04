package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "teacher_feedback")
public class TeacherFeedbackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "teacher_id", nullable = false)
    private String teacherId;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(nullable = false)
    private Integer rate; // 1 dan 5 gacha

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_anonymous")
    private Boolean isAnonymous = false;

    private LocalDateTime createdDate = LocalDateTime.now();
}