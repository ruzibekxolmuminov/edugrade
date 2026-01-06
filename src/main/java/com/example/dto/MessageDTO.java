package com.example.dto;

import com.example.enums.MessageType;
import lombok.Data;

@Data
public class MessageDTO {
    private Long id;
    private String senderId;
    private String senderName;
    private String groupId;
    private String content;
    private MessageType type;
    private String timestamp;
}