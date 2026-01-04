package com.example.service;

import com.example.dto.EventCreateDTO;
import com.example.dto.EventParticipantDTO;
import com.example.dto.EventResponseDTO;
import com.example.entity.EventEntity;
import com.example.entity.EventRegistrationEntity;
import com.example.enums.GeneralStatus;
import com.example.repository.EventRegistrationRepository;
import com.example.repository.EventRepository;
import com.example.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.util.SpringSecurityUtil.currentProfileId;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventRegistrationRepository registrationRepository;
    @Autowired
    private ProfileRepository profileRepository;


    // 14.1 Create Event (Admin)
    public String createEvent(EventCreateDTO dto) {
        EventEntity entity = new EventEntity();
        entity.setSchoolId(dto.getSchoolId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setImageId(dto.getImageId());
        entity.setEventDate(dto.getEventDate());
        entity.setType(dto.getType());

        eventRepository.save(entity);
        return "Tadbir muvaffaqiyatli yaratildi";
    }

    // 14.2 Register For Event (Student)
    @Transactional
    public String register(Integer eventId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Tadbir topilmadi"));

        if (!event.getStatus().equals(GeneralStatus.ACTIVE)) {
            throw new RuntimeException("Ushbu tadbirga ro'yxatdan o'tish tugagan");
        }

        if (registrationRepository.existsByEventIdAndStudentId(eventId, currentProfileId())) {
            throw new RuntimeException("Siz allaqachon ro'yxatdan o'tgansiz");
        }

        EventRegistrationEntity reg = new EventRegistrationEntity();
        reg.setEventId(eventId);
        reg.setStudentId(currentProfileId());
        registrationRepository.save(reg);

        return "Tadbirga muvaffaqiyatli ro'yxatdan o'tdingiz";
    }

    // 14.3 Get Event List
    public Page<EventResponseDTO> getList(String schoolId, GeneralStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("eventDate").ascending());
        Page<EventEntity> entities = eventRepository.findAllBySchoolIdAndStatus(schoolId, status, pageable);

        return entities.map(entity -> {
            EventResponseDTO dto = new EventResponseDTO();
            dto.setId(entity.getId());
            dto.setTitle(entity.getTitle());
            dto.setDescription(entity.getDescription());
            dto.setImageId(entity.getImageId());
            dto.setEventDate(entity.getEventDate());
            dto.setType(entity.getType());
            dto.setStatus(entity.getStatus());
            return dto;
        });
    }

    public Page<EventParticipantDTO> getParticipants(Integer eventId, int page, int size) {
        // 1. Sahifalash sozlamalarini yaratish (vaqti bo'yicha teskari tartibda)
        Pageable pageable = PageRequest.of(page, size, Sort.by("registrationDate").descending());

        // 2. Bazadan sahifalangan ro'yxatni olish
        Page<EventRegistrationEntity> registrationPage = registrationRepository.findAllByEventId(eventId, pageable);

        // 3. Entity Page-ni DTO Page-ga map qilish
        return registrationPage.map(reg -> {
            EventParticipantDTO dto = new EventParticipantDTO();
            dto.setStudentId(reg.getStudentId());
            dto.setRegistrationDate(reg.getRegistrationDate());

            profileRepository.findById(reg.getStudentId()).ifPresent(p -> {
                dto.setFullName(p.getLastname() + " " + p.getFirstname());
                dto.setPhone(p.getPhone());
            });

            return dto;
        });
    }
}