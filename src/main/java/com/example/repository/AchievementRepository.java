package com.example.repository;

import com.example.entity.AchievementEntity;
import com.example.enums.AchievementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<AchievementEntity, String> {
    List<AchievementEntity> findAllByStudentIdAndVisibleTrueOrderByAchievementDateDesc(String studentId);
    
    Optional<AchievementEntity> findByIdAndVisibleTrue(String id);

    Optional<AchievementEntity> findByAchievementDateAndTitleAndDescriptionAndTypeAndVisibleTrue(LocalDate achievementDate, String title, String description, AchievementType type);
}