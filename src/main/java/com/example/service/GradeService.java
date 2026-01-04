package com.example.service;

import com.example.dto.GradeCreateDTO;
import com.example.dto.GradeLogResponseDTO;
import com.example.dto.GradeUpdateDTO;
import com.example.entity.GradeEntity;
import com.example.entity.GradeLogEntity;
import com.example.repository.GradeLogRepository;
import com.example.repository.GradeRepository;
import com.example.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {
    private final GradeRepository gradeRepository;
    private final GradeLogRepository gradeLogRepository;
    private final ProfileRepository profileRepository; // Ism-familiya olish uchun

    // 10.1 Create Grade
    public String createGrade(GradeCreateDTO dto, String teacherId) {
        GradeEntity entity = new GradeEntity();
        entity.setStudentId(dto.getStudentId());
        entity.setTeacherId(teacherId);
        entity.setSubjectId(dto.getSubjectId());
        entity.setScheduleId(dto.getScheduleId());
        entity.setSchoolId(dto.getSchoolId());
        entity.setGradeValue(dto.getGradeValue());
        entity.setWeight(dto.getWeight());
        entity.setComment(dto.getComment());
        
        gradeRepository.save(entity);
        return "Baho muvaffaqiyatli qo'yildi";
    }

    // 10.2 Update Grade with Log
    @Transactional
    public String updateGrade(String id, GradeUpdateDTO dto, String modifierId) {
        GradeEntity grade = gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Baho topilmadi"));

        // Tarixni saqlash
        GradeLogEntity log = new GradeLogEntity();
        log.setGradeId(grade.getId());
        log.setWhoChangedId(modifierId);
        log.setOldGrade(grade.getGradeValue());
        log.setNewGrade(dto.getGradeValue());
        log.setReason(dto.getReasonForChange());
        log.setSchoolId(grade.getSchoolId());
        gradeLogRepository.save(log);

        // Bahoni yangilash
        grade.setGradeValue(dto.getGradeValue());
        grade.setComment(dto.getComment());
        gradeRepository.save(grade);

        return "Baho yangilandi va tarix saqlandi";
    }

    // 10.3 Get Student Grades
    public List<GradeEntity> getStudentGrades(String studentId, Integer subjectId) {
        return gradeRepository.findAllByStudentIdAndSubjectIdAndVisibleTrue(studentId, subjectId);
    }

    // 10.4 Get Change Log
    public Page<GradeLogResponseDTO> getGradeLogs(String schoolId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("changeDate").descending());
        Page<GradeLogEntity> logs = gradeLogRepository.findAllBySchoolId(schoolId, pageable);

        return logs.map(log -> {
            GradeLogResponseDTO dto = new GradeLogResponseDTO();
            dto.setOldGrade(log.getOldGrade());
            dto.setNewGrade(log.getNewGrade());
            dto.setDate(log.getChangeDate());
            dto.setReason(log.getReason());
            // Kim o'zgartirganini ismini olish (oddiy misol)
            dto.setWhoChangedName(log.getWhoChangedId()); 
            return dto;
        });
    }
}