package com.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "homework")
public class HomeworkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "teacher_id")
    private String teacherId;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "subject_id")
    private Integer subjectId;

    @Column(name = "school_id")
    private String schoolId;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "attach_id")
    private String attachId;

    private LocalDateTime deadlineDate;
    private LocalDateTime createdDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private AttachEntity attachment;
}