package com.example.controller;

import com.example.dto.MessageDTO;
import com.example.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat.{groupId}")
    public void handleChat(@DestinationVariable Long groupId, @Payload MessageDTO message) {
        // 1. Kelgan xabarni service orqali qayta ishlash (Save/Edit/Delete)
        MessageDTO processed = chatService.processMessage(message);
        
        // 2. Guruhning barcha a'zolariga yuborish
        messagingTemplate.convertAndSend("/topic/group." + groupId, processed);
    }
    @MessageMapping("/chat.sendMessage")
    public void handleMessage(@Payload MessageDTO dto) {
        // DTO ichida groupId bor, shuning uchun path'da bo'lishi shart emas
        MessageDTO savedDto = chatService.processMessage(dto);

        // Xabarni guruh kanaliga yuboramiz
        messagingTemplate.convertAndSend("/topic/group." + dto.getGroupId(), savedDto);
    }
}