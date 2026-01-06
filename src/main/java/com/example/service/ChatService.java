package com.example.service;

import com.example.dto.MessageDTO;
import com.example.entity.ChatMessage;
import com.example.entity.GroupEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.MessageType;
import com.example.repository.ChatMemberRepository;
import com.example.repository.ChatMessageRepository;
import com.example.repository.GroupRepository;
import com.example.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatRepo;
    private final ChatMemberRepository chatMemberRepository;
    private final ProfileRepository profileRepo;
    private final GroupRepository groupRepo;

    public MessageDTO processMessage(MessageDTO dto) {
        // 1. Foydalanuvchi ushbu sinf a'zosi ekanligini tekshirish
//        boolean isMember = chatMemberRepository.existsByProfileIdAndGroupId(
//                dto.getSenderId(),
//                dto.getGroupId()
//        );
//
//        if (!isMember) {
//            throw new RuntimeException("Siz ushbu sinf a'zosi emassiz! Xabar yuborish taqiqlanadi.");
//        }

        // 2. Agar a'zo bo'lsa, xabarni saqlash
        return saveNewMessage(dto);
    }

    private MessageDTO saveNewMessage(MessageDTO dto) {
        ChatMessage entity = new ChatMessage();

        // Bazadan obyektlarni olish
        ProfileEntity sender = profileRepo.findById(dto.getSenderId()).orElseThrow();
        GroupEntity group = groupRepo.findById(dto.getGroupId()).orElseThrow();
//
        entity.setSender(sender);
        entity.setGroup(group);
        entity.setContent(dto.getContent());
        entity.setType(dto.getType());

        chatRepo.save(entity);

        dto.setId(entity.getId());
        dto.setTimestamp(entity.getCreatedAt().toString());
        // ChatService.java ichida
        dto.setSenderName(sender.getFirstname() + " " + sender.getLastname());
        return dto;
    }

    private MessageDTO deleteMessage(MessageDTO dto) {
        ChatMessage msg = chatRepo.findById(dto.getId()).orElseThrow();
        msg.setDeleted(true);
        msg.setContent("Xabar o'chirildi");
        msg.setType(MessageType.DELETE);
        chatRepo.save(msg);
        return dto;
    }
}