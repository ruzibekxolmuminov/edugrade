package com.example.entity;

import com.example.enums.GroupLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "groups")
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private GroupLevel level;

    @Column(name = "visible")
    private Boolean visible = true;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "school_id")
    private String schoolId;

    @Column(name = "faculty_id")
    private String facultyId;

    @Column(name = "mentor_id", unique = true)
    private String mentorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", insertable = false, updatable = false)
    private SchoolEntity school;

    @Column(name = "member_count")
    private Integer memberCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", insertable = false, updatable = false)
    private FacultyEntity faculty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", insertable = false, updatable = false)
    private ProfileEntity mentor;
}