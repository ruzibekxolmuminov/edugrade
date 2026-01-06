package com.example.repository;

import com.example.entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    boolean existsByProfileIdAndGroupId(String profile_id, String group_id);
}