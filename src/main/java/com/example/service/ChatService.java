package com.example.service;

import com.example.dto.ChatDTO;
import com.example.dto.MessageDTO;
import com.example.dto.group.GroupDTO;
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
import org.hibernate.cache.spi.entry.CacheEntry;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatRepo;
    private final ChatMemberRepository chatMemberRepository;
    private final ProfileRepository profileRepo;
    private final GroupRepository groupRepo;

    public MessageDTO processMessage(MessageDTO dto) {
//         1. Foydalanuvchi ushbu sinf a'zosi ekanligini tekshirish
        boolean isMember = chatMemberRepository.existsByProfileIdAndGroupId(
                dto.getSenderId(),
                dto.getGroupId()
        );

        if (!isMember) {
            throw new RuntimeException("Siz ushbu sinf a'zosi emassiz! Xabar yuborish taqiqlanadi.");
        }

        // 2. Agar a'zo bo'lsa, xabarni saqlash
        return saveNewMessage(dto);
    }

    private MessageDTO saveNewMessage(MessageDTO dto) {
        ChatMessage entity = new ChatMessage();

        // Bazadan obyektlarni olish
        ProfileEntity sender = profileRepo.findById(dto.getSenderId()).orElseThrow();
        GroupEntity group = groupRepo.findById(dto.getGroupId()).orElseThrow();
//
        entity.setProfileId(sender.getId());
        entity.setGroupId(group.getId());
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
    public List<ChatDTO> get(String groupId) {
        List<ChatMessage> chatMessages = chatRepo.findByGroupIdOrderByCreatedAtAsc(groupId);
        List<ChatDTO> chatDTOS = new LinkedList<>();

        for (ChatMessage chatMessage : chatMessages) {
            ChatDTO chatDTO = new ChatDTO();
            chatDTO.setId(chatMessage.getId());
            chatDTO.setContent(chatMessage.getContent());
            chatDTO.setType(chatMessage.getType());
            chatDTO.setCreatedAt(chatMessage.getCreatedAt());

            // Bu yerda senderId va senderName ni o'rnatamiz
            chatDTO.setProfile(chatMessage.getProfileId());
            if (chatMessage.getSender() != null) {
                chatDTO.setSenderName(chatMessage.getSender().getFirstname() + " " + chatMessage.getSender().getLastname());
            } else {
                chatDTO.setSenderName("Noma'lum");
            }

            chatDTOS.add(chatDTO);
        }
        return chatDTOS;
    }

    public List<GroupDTO> getUserGroups(String currentUserId) {
        // ChatMember jadvalidan foydalanuvchi a'zo bo'lgan hamma guruhlarni oladi
        // Bu yerda uning roli (ADMIN, TEACHER, MEMBER) farqi yo'q
        List<GroupEntity> groups = chatMemberRepository.findGroupsByProfileId(currentUserId);

        List<GroupDTO> groupDTOS = new LinkedList<>();
        for (GroupEntity group : groups) {
            GroupDTO groupDTO = new GroupDTO();
            groupDTO.setId(group.getId());
            groupDTO.setName(group.getName());

            // Dinamik ravishda a'zolar sonini hisoblash
            Integer count = groupRepo.getMemberCountById(group.getId());
            groupDTO.setMemberCount(count != null ? count : 0);

            groupDTOS.add(groupDTO);
        }
        return groupDTOS;
    }
}