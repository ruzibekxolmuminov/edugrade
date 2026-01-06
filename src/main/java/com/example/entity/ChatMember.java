package com.example.entity;

import com.example.enums.ChatRole;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "chat_member")
public class ChatMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private ProfileEntity profile;
    
    @ManyToOne
    private GroupEntity group;
    
    @Enumerated(EnumType.STRING)
    private ChatRole role; // ADMIN (Ustoz), MEMBER (O'quvchi)
}