package com.example.service;

import com.example.dto.TeacherFeedbackDTO;
import com.example.entity.TeacherFeedbackEntity;
import com.example.repository.ProfileRepository;
import com.example.repository.TeacherFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.util.SpringSecurityUtil.currentProfileId;

@Service
@RequiredArgsConstructor
public class TeacherFeedbackService {
    private final TeacherFeedbackRepository feedbackRepository;
    private final ProfileRepository profileRepository; // O'quvchi ismini olish uchun

    // 13.1 Post Feedback
    public String postFeedback(TeacherFeedbackDTO dto) {
        if (dto.getRate() < 1 || dto.getRate() > 5) {
            throw new RuntimeException("Baholash 1 dan 5 gacha bo'lishi kerak");
        }

        TeacherFeedbackEntity entity = new TeacherFeedbackEntity();
        entity.setTeacherId(dto.getTeacherId());
        entity.setStudentId(currentProfileId());
        entity.setRate(dto.getRate());
        entity.setContent(dto.getContent());
        entity.setIsAnonymous(dto.getIsAnonymous());

        feedbackRepository.save(entity);
        return "Fikringiz uchun rahmat!";
    }

    // 13.2 Get Teacher Feedbacks
    public Page<TeacherFeedbackDTO> getTeacherFeedbacks(String teacherId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TeacherFeedbackEntity> entities = feedbackRepository.findAllByTeacherIdOrderByCreatedDateDesc(teacherId, pageable);

        return entities.map(entity -> {
            TeacherFeedbackDTO dto = new TeacherFeedbackDTO();
            dto.setId(entity.getId());
            dto.setRate(entity.getRate());
            dto.setContent(entity.getContent());
            dto.setCreatedDate(entity.getCreatedDate());
            dto.setTeacherId(entity.getTeacherId());
            dto.setIsAnonymous(entity.getIsAnonymous());
            
            // ANONIMLIK MANTIQI
            if (entity.getIsAnonymous()) {
                dto.setStudentFullName("Anonim foydalanuvchi");
            } else {
                // Ism familiyani bazadan olish (cache ishlatish tavsiya etiladi)
                profileRepository.findById(entity.getStudentId()).ifPresent(p -> {
                    dto.setStudentFullName(p.getLastname() + " " + p.getFirstname());
                });
            }
            return dto;
        });
    }
}