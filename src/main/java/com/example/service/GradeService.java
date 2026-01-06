package com.example.service;

import com.example.dto.*;
import com.example.entity.GradeEntity;
import com.example.entity.GradeLogEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.GradeType;
import com.example.exp.AppBadException;
import com.example.repository.GradeLogRepository;
import com.example.repository.GradeRepository;
import com.example.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.util.SpringSecurityUtil.currentProfileId;

@Service
public class GradeService {
    @Autowired
    private  GradeRepository gradeRepository;
    @Autowired
    private  GradeLogRepository gradeLogRepository;
    @Autowired
    private  ProfileRepository profileRepository;


    // 10.1 Create Grade
    public String createGrade(GradeCreateDTO dto, String teacherId) {
        Optional<GradeEntity> isExist = gradeRepository.findByStudentIdAndSubjectIdAndGradeTypeAndVisibleTrueAndLessonDate(dto.getStudentId(),dto.getSubjectId(),dto.getType(), dto.getLessonDate());

        if (isExist.isPresent()) {
            throw new AppBadException("Bugun uchun baxo qo'yilgan!");
        }
        LocalDate now = LocalDate.now();

//        if (dto.getType() == GradeType.DAILY) {
//            if (dto.getLessonDate().isAfter(now)) {
//                throw new AppBadException("Kelajakdagi dars kuni uchun baho qo'yib bo'lmaydi!");
//            }
//        }

        // 1. Final bahosi uchun tekshiruv
        if (dto.getType() == GradeType.FINAL) {
            // Midterm bahosi borligini tekshirish
            boolean hasMidterm = gradeRepository.existsByStudentIdAndSubjectIdAndGradeType(
                    dto.getStudentId(), dto.getSubjectId(), GradeType.SECOND_MIDTERM);

            if (!hasMidterm) {
                throw new AppBadException("Avval 2-Oraliq bahosini qo'yish shart!");
            }

        }
        if (dto.getType() == GradeType.SECOND_MIDTERM) {
            boolean hasMidterm = gradeRepository.existsByStudentIdAndSubjectIdAndGradeType(
                    dto.getStudentId(), dto.getSubjectId(), GradeType.FIRST_MIDTERM
            );
            if (!hasMidterm) {
                throw new AppBadException("Avval 1-Oraliq bahosini qo'yish shart!");
            }
        }

        GradeEntity entity = new GradeEntity();
        entity.setStudentId(dto.getStudentId());
        entity.setTeacherId(teacherId);
        entity.setGroupId(dto.getGroupId());
        entity.setLessonDate(dto.getLessonDate());
        entity.setSubjectId(dto.getSubjectId());
        entity.setScheduleId(dto.getScheduleId());
        entity.setSchoolId(dto.getSchoolId());
        entity.setGradeValue(dto.getGradeValue());
        entity.setComment(dto.getComment());
        entity.setGradeType(dto.getType());
        entity.setVisible(true);

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
        grade.setUpdatedBy(currentProfileId());
        grade.setUpdatedDate(LocalDateTime.now());
        grade.setComment(dto.getComment());
        gradeRepository.save(grade);

        return "Baho yangilandi va tarix saqlandi";
    }

    // 10.3 Get Student Grades
    public List<GradeDTO> getStudentGrades(String studentId, Integer subjectId) {
        List<GradeEntity> gradeEntities =  gradeRepository.findAllByStudentIdAndSubjectIdAndVisibleTrue(studentId, subjectId);
        List<GradeDTO> gradeDTOS = new ArrayList<>();
        for (GradeEntity gradeEntity : gradeEntities) {
            GradeDTO gradeDTO = new GradeDTO();
            gradeDTO.setStudentId(gradeEntity.getStudentId());
            gradeDTO.setId(gradeEntity.getId());
            gradeDTO.setGroupId(gradeEntity.getGroupId());
            gradeDTO.setScheduleId(gradeEntity.getScheduleId());
            gradeDTO.setTeacherId(gradeEntity.getTeacherId());
            gradeDTO.setSchoolId(gradeEntity.getSchoolId());
            gradeDTO.setSubjectId(gradeEntity.getScheduleId());
            gradeDTO.setComment(gradeEntity.getComment());
            gradeDTO.setType(gradeEntity.getGradeType());
            gradeDTO.setDate(gradeEntity.getLessonDate());
            gradeDTO.setGrade(gradeEntity.getGradeValue());
            gradeDTOS.add(gradeDTO);
        }
        return gradeDTOS;
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

    public GradebookTableDTO getGradebook(String groupId, Integer subjectId, LocalDate start, LocalDate end) {
        // 1. Talabalar va baholarni bazadan olish
        List<ProfileEntity> students = profileRepository.findAllByGroupIdAndVisibleTrue(groupId);
        List<GradeEntity> allGrades = gradeRepository.findAllByGroupIdAndSubjectIdAndLessonDateBetween(groupId, subjectId, start, end);

        // 2. Dinamik ustunlar (Columns) ro'yxatini shakllantirish
        // TreeSet ishlatamizki, sanalar avtomatik tartib bilan (chronological) tursin
        Set<LocalDate> lessonDates = allGrades.stream()
                .filter(g -> g.getGradeType() == GradeType.DAILY)
                .map(GradeEntity::getLessonDate)
                .collect(Collectors.toCollection(TreeSet::new));

        List<String> columns = new ArrayList<>();
        columns.add("Student Name");
        lessonDates.forEach(date -> columns.add(date.toString())); // Har bir dars sanasi alohida ustun
        columns.add("MIDTERM");
        columns.add("FINAL");
        columns.add("TOTAL (%)");

        // 3. Talaba ID bo'yicha baholarni guruhlash
        Map<String, List<GradeEntity>> studentGradesMap = allGrades.stream()
                .collect(Collectors.groupingBy(GradeEntity::getStudentId));

        List<GradebookRowDTO> rows = new ArrayList<>();
        for (ProfileEntity student : students) {
            GradebookRowDTO row = new GradebookRowDTO();
            row.setStudentId(student.getId());
            row.setStudentName(student.getLastname() + ", " + student.getFirstname());

            // LinkedHashMap ishlatamizki, JSON'da sanalar chalkashib ketmasin
            Map<String, String> dailyGrades = new LinkedHashMap<>();

            List<GradeEntity> sGrades = studentGradesMap.getOrDefault(student.getId(), Collections.emptyList());

            // Har bir ustun (sana) bo'yicha bahoni tekshirib chiqamiz
            for (LocalDate date : lessonDates) {
                String gradeVal = sGrades.stream()
                        .filter(g -> g.getGradeType() == GradeType.DAILY && g.getLessonDate().equals(date))
                        .map(g -> g.getGradeValue().toString())
                        .findFirst()
                        .orElse(""); // Baho yo'q bo'lsa bo'sh katak
                dailyGrades.put(date.toString(), gradeVal);
            }

            // Midterm va Final baholarini alohida joylashtiramiz
            dailyGrades.put("FIRST MIDTERM", getGradeByType(sGrades, GradeType.FIRST_MIDTERM));
            dailyGrades.put("SECOND MIDTERM", getGradeByType(sGrades, GradeType.SECOND_MIDTERM));
            dailyGrades.put("FINAL", getGradeByType(sGrades, GradeType.FINAL));

            row.setGrades(dailyGrades);
            row.setTotalPercentage(calculateTotalPercentage(sGrades));
            rows.add(row);
        }

        // 4. Natijani qaytarish
        GradebookTableDTO table = new GradebookTableDTO();
        table.setColumns(columns); // Endi null bo'lmaydi!
        table.setRows(rows);
        return table;
    }

    // Yordamchi metodlar
    private String getGradeByType(List<GradeEntity> grades, GradeType type) {
        return grades.stream()
                .filter(g -> g.getGradeType() == type)
                .map(g -> g.getGradeValue().toString())
                .findFirst()
                .orElse("-"); // Imtihon topshirilmagan bo'lsa chiziq
    }

    private Double calculateTotalPercentage(List<GradeEntity> grades) {
        if (grades.isEmpty()) return 0.0;
        double sum = grades.stream().mapToDouble(GradeEntity::getGradeValue).sum();
        return (sum / (grades.size() * 5.0)) * 100; // Namuna: 10 ballik tizimda
    }
}