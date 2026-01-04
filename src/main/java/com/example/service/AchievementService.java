package com.example.service;

import com.example.dto.AchievementDTO;
import com.example.entity.AchievementEntity;
import com.example.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.util.SpringSecurityUtil.currentProfileId;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;

    // 12.1 Create Achievement
    public AchievementDTO create(AchievementDTO dto) {
        AchievementEntity entity = new AchievementEntity();
        entity.setStudentId(currentProfileId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setAttachId(dto.getAttachId());
        entity.setAchievementDate(dto.getAchievementDate());
        entity.setType(dto.getType());

        achievementRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    // 12.2 Update Achievement
    public String update(String id, AchievementDTO dto) {
        AchievementEntity entity = achievementRepository.findByIdAndVisibleTrue(id)
                .orElseThrow(() -> new RuntimeException("Yutuq topilmadi"));

        // Faqat o'ziga tegishli yutuqni tahrirlashi mumkin
        if (!entity.getStudentId().equals(currentProfileId())) {
            throw new RuntimeException("Sizda ruxsat yo'q!");
        }

        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setAttachId(dto.getAttachId());
        
        achievementRepository.save(entity);
        return "Yutuq yangilandi";
    }

    // 12.3 Delete Achievement (Soft Delete)
    public String delete(String id) {
        AchievementEntity entity = achievementRepository.findByIdAndVisibleTrue(id)
                .orElseThrow(() -> new RuntimeException("Yutuq topilmadi"));

        if (!entity.getStudentId().equals(currentProfileId())) {
            throw new RuntimeException("Sizda ruxsat yo'q!");
        }

        entity.setVisible(false);
        achievementRepository.save(entity);
        return "Yutuq o'chirildi";
    }

    // 12.4 Get Portfolio By Student Id
    public List<AchievementDTO> getPortfolioByStudentId() {
        return achievementRepository.findAllByStudentIdAndVisibleTrueOrderByAchievementDateDesc(currentProfileId())
                .stream()
                .map(entity -> {
                    AchievementDTO dto = new AchievementDTO();
                    dto.setId(entity.getId());
                    dto.setTitle(entity.getTitle());
                    dto.setDescription(entity.getDescription());
                    dto.setAttachId(entity.getAttachId());
                    dto.setAchievementDate(entity.getAchievementDate());
                    dto.setType(entity.getType());
                    return dto;
                }).collect(Collectors.toList());
    }
}