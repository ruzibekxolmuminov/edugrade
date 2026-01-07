package com.example.controller;

import com.example.dto.ChatDTO;
import com.example.dto.MessageDTO;
import com.example.dto.group.GroupDTO;
import com.example.entity.ChatMessage;
import com.example.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.util.SpringSecurityUtil.currentProfileId;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    public void handleMessage(@Payload MessageDTO dto) {
        // 1. Service orqali DBga saqlash va ism-familiyani to'ldirish
        MessageDTO savedDto = chatService.processMessage(dto);

        // 2. Guruh kanaliga tarqatish
        // MUHIM: Xabar yuborilayotgan manzil frontenddagi subscribe bilan bir xil bo'lishi kerak
        messagingTemplate.convertAndSend("/topic/group." + dto.getGroupId(), savedDto);
    }

    @GetMapping("/v1/chat/history/{groupId}")
    @ResponseBody
    public ResponseEntity<List<ChatDTO>> getHistory(@PathVariable String groupId) {
        return ResponseEntity.ok(chatService.get(groupId));
    }

    @GetMapping("/api/v1/chat/my-groups")
    @ResponseBody
    public ResponseEntity<List<GroupDTO>> getMyGroups() {
        String currentUserId = currentProfileId();
        return ResponseEntity.ok(chatService.getUserGroups(currentUserId));
    }
}