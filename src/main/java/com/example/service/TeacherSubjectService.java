package com.example.service;

import com.example.dto.TeacherSubjectDTO;
import com.example.entity.TeacherSubjectEntity;
import com.example.repository.TeacherSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherSubjectService {
    private final TeacherSubjectRepository teacherSubjectRepository;

    public String assign(TeacherSubjectDTO dto) {
        var optional = teacherSubjectRepository.findByTeacherIdAndSubjectIdAndSchoolIdAndVisibleTrue(
                dto.getTeacherId(), dto.getSubjectId(), dto.getSchoolId());

        if (optional.isPresent()) {
            throw new RuntimeException("Ushbu o'qituvchi allaqachon bu fanga biriktirilgan!");
        }

        TeacherSubjectEntity entity = new TeacherSubjectEntity();
        entity.setTeacherId(dto.getTeacherId());
        entity.setSubjectId(dto.getSubjectId());
        entity.setSchoolId(dto.getSchoolId());
        
        teacherSubjectRepository.save(entity);
        return "Muvaffaqiyatli biriktirildi";
    }

    public String removeAssignment(Integer id) {
        TeacherSubjectEntity entity = teacherSubjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Biriktirish topilmadi yoki allaqachon o'chirilgan!"));

        if (!entity.getVisible()) {
            throw new RuntimeException("Ushbu biriktirish allaqachon bekor qilingan!");
        }

        entity.setVisible(false);
        teacherSubjectRepository.save(entity);

        return "O'qituvchi ushbu fandan muvaffaqiyatli uzildi.";
    }
}