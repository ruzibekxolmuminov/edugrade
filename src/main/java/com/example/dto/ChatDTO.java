package com.example.dto;

import com.example.entity.GroupEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.MessageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatDTO {
    private Long id;
    private String profile;
    private String group;
    private String senderName;
    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime editedAt;
    private boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    private MessageType type;
}
