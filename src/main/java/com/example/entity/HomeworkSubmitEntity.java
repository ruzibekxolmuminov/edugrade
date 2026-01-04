package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "homework_submit")
public class HomeworkSubmitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "homework_id")
    private String homeworkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homework_id", insertable = false,updatable = false)
    private HomeworkEntity homework;

    @Column(name = "student_id")
    private String studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", insertable = false,updatable = false)
    private ProfileEntity profile;

    @Column(name = "grade_id")
    private String gradeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id", insertable = false,updatable = false)
    private GradeEntity grade;
    private String feedback;

    @Column(name = "content")
    private String content;

    @Column(name = "attach_id")
    private String attachId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private AttachEntity attach;
}
