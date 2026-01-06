package com.example.repository;

import com.example.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // Guruhning oxirgi xabarlarini olish (Tarix)
    List<ChatMessage> findByGroupIdOrderByCreatedAtAsc(String group_id);
}