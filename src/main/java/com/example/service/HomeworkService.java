package com.example.service;

import com.example.dto.GradeCreateDTO;
import com.example.dto.GradeSubmissionDTO;
import com.example.dto.HomeworkSubmit;
import com.example.dto.SubmissionDTO;
import com.example.dto.homework.HomeworkCreateDTO;
import com.example.dto.homework.HomeworkDTO;
import com.example.dto.homework.HomeworkUpdateDTO;
import com.example.entity.HomeworkEntity;
import com.example.entity.HomeworkSubmitEntity;
import com.example.exp.AppBadException;
import com.example.repository.HomeworkRepository;
import com.example.repository.HomeworkSubmitRepository;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.util.SpringSecurityUtil.currentProfileId;

@Service
public class HomeworkService {
    @Autowired
    private HomeworkRepository homeworkRepository;
    @Autowired
    private HomeworkSubmitRepository homeworkSubmitRepository;
    @Autowired
    private GradeService gradeService;

    public @Nullable HomeworkDTO create(HomeworkCreateDTO homeworkDTO) {
        Optional<HomeworkEntity> isExist = homeworkRepository.findByTeacherIdAndSubjectIdAndGroupIdAndContentAndVisibleTrue(homeworkDTO.getTeacherId(),homeworkDTO.getSubjectId(), homeworkDTO.getGroupId(), homeworkDTO.getContent());
        if (isExist.isPresent()) {
            throw new AppBadException("Homework already exists");
        }
        HomeworkEntity homework = new HomeworkEntity();
        homework.setTeacherId(homeworkDTO.getTeacherId());
        homework.setSubjectId(homeworkDTO.getSubjectId());
        homework.setGroupId(homeworkDTO.getGroupId());
        homework.setContent(homeworkDTO.getContent());
        homework.setSchoolId(homeworkDTO.getSchoolId());
        homework.setContent(homeworkDTO.getContent());
        homework.setDeadlineDate(homeworkDTO.getDeadline());
        homework.setAttachId(homeworkDTO.getAttachId());
        homework.setTitle(homeworkDTO.getTitle());
        homeworkRepository.save(homework);
        return toDTO(homework);
    }

    private @Nullable HomeworkDTO toDTO(HomeworkEntity homework) {
        HomeworkDTO homeworkDTO = new HomeworkDTO();
        homeworkDTO.setId(homework.getId());
        homeworkDTO.setTeacherId(homework.getTeacherId());
        homeworkDTO.setSubjectId(homework.getSubjectId());
        homeworkDTO.setGroupId(homework.getGroupId());
        homeworkDTO.setContent(homework.getContent());
        homeworkDTO.setAttachId(homework.getAttachId());
        homeworkDTO.setTitle(homework.getTitle());
        homeworkDTO.setSchoolId(homework.getSchoolId());
        homeworkDTO.setTitle(homework.getTitle());
        homeworkDTO.setCreateDate(homework.getCreatedDate());

        return homeworkDTO;
    }


    public @Nullable String update(HomeworkUpdateDTO homeworkUpdateDTO, String id) {
        HomeworkEntity homework = getById(id);
        if (homeworkUpdateDTO.getAttachId() != null) {
            homework.setAttachId(homeworkUpdateDTO.getAttachId());
        }else if(homeworkUpdateDTO.getTitle() != null) {
            homework.setTitle(homeworkUpdateDTO.getTitle());
        }else if(homeworkUpdateDTO.getContent() != null) {
            homework.setContent(homeworkUpdateDTO.getContent());
        }else if(homeworkUpdateDTO.getDeadline() != null) {
            homework.setDeadlineDate(homeworkUpdateDTO.getDeadline());
        }
        homeworkRepository.save(homework);
        return "Homework updated!";
    }

    private HomeworkEntity getById(String id) {
        Optional<HomeworkEntity> isExist = homeworkRepository.findByIdAndVisibleTrue(id);
        if (isExist.isEmpty()){
            throw new AppBadException("Homework not found");
        }
        return isExist.get();
    }


    public String submitHomework(SubmissionDTO dto) {
        HomeworkEntity hw = homeworkRepository.findById(dto.getHomeworkId()).orElseThrow();

        if (LocalDateTime.now().isAfter(hw.getDeadlineDate())) {
            throw new RuntimeException("Topshirish muddati o'tib ketgan!");
        }

        HomeworkSubmitEntity submission = new HomeworkSubmitEntity();
        submission.setHomeworkId(dto.getHomeworkId());
        submission.setStudentId(currentProfileId());
        submission.setContent(dto.getContent());
        submission.setAttachId(dto.getAttachId());

        homeworkSubmitRepository.save(submission);
        return "Vazifa topshirildi";
    }

    // 11.4 Grade Submission (Teacher)
    @Transactional
    public String gradeSubmission(GradeSubmissionDTO dto) {
        HomeworkSubmitEntity submission = homeworkSubmitRepository.findById(dto.getSubmissionId()).orElseThrow();
        HomeworkEntity hw = homeworkRepository.findById(submission.getHomeworkId()).orElseThrow();

        // 1. Grade moduliga bahoni saqlash
        GradeCreateDTO gradeDto = new GradeCreateDTO();
        gradeDto.setStudentId(submission.getStudentId());
        gradeDto.setGradeValue(dto.getGradeValue());
        gradeDto.setSubjectId(hw.getSubjectId());
        gradeDto.setSchoolId(hw.getSchoolId());
        gradeDto.setWeight(1); // Homework odatda joriy baho

        String gradeId = gradeService.createGrade(gradeDto, currentProfileId()); // Grade modulidagi metod

        // 2. Submission-ni yangilash
        submission.setGradeId(gradeId);
        submission.setFeedback(dto.getFeedback());
        homeworkSubmitRepository.save(submission);

        return "Vazifa baholandi";
    }
}
