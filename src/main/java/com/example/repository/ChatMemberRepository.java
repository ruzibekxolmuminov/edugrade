package com.example.repository;

import com.example.entity.ChatMember;
import com.example.entity.GroupEntity;
import com.example.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    @Query("SELECT cm.group FROM ChatMember cm WHERE cm.profile.id = :profileId AND cm.group.visible = true")
    List<GroupEntity> findGroupsByProfileId(@Param("profileId") String profileId);

    boolean existsByProfileIdAndGroupId(String profileId, String groupId);
    ChatMember getByProfile(ProfileEntity byId);
}