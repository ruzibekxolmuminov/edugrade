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
    private ProfileEntity sender;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private GroupEntity group;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime editedAt;
    private boolean isDeleted = false;
    
    @Enumerated(EnumType.STRING)
    private MessageType type;

    // Getters & Setters
}