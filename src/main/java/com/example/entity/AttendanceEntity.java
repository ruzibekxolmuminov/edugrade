package com.example.entity;

import com.example.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "attendance")
public class AttendanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "group_id", nullable = false)
    private String groupId;

    @Column(name = "schedule_id", nullable = false)
    private String scheduleId;

    @Column(name = "school_id", nullable = false)
    private String schoolId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AttendanceStatus status; // PRESENT, ABSENT, REASONABLE

    @Column(columnDefinition = "TEXT")
    private String reason; // Sababli bo'lsa izoh

    private LocalDate date; // Dars bo'lgan kun
    private LocalDateTime createdDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private ProfileEntity student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", insertable = false, updatable = false)
    private ScheduleEntity schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", insertable = false, updatable = false)
    private SchoolEntity school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    private GroupEntity group;
}