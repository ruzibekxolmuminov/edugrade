package com.example.entity;

import com.example.enums.EventType;
import com.example.enums.GeneralStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "event")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "school_id", nullable = false)
    private String schoolId;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_id")
    private String imageId;

    private LocalDateTime eventDate;

    @Enumerated(EnumType.STRING)
    private EventType type; // SPORT, EDUCATION

    @Enumerated(EnumType.STRING)
    private GeneralStatus status = GeneralStatus.ACTIVE;

    private LocalDateTime createdDate = LocalDateTime.now();
}