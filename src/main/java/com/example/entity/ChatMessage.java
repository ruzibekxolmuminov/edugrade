package com.example.entity;

import com.example.enums.MessageType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", insertable = false, updatable = false)
    private ProfileEntity sender;

    @Column(name = "sender_id")
    private String profileId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    private GroupEntity group;

    @Column(name = "group_id")
    private String groupId;

    @Column(columnDefinition = "TEXT")
    private String content;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime editedAt;
    private boolean isDeleted = false;
    
    @Enumerated(EnumType.STRING)
    private MessageType type;

    // Getters & Setters
}