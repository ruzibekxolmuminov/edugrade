package com.example.service;

import com.example.dto.JournalRowDTO;
import com.example.dto.JournalStudentDTO;
import com.example.dto.JournalTableDTO;
import com.example.entity.AttendanceEntity;
import com.example.entity.GradeEntity;
import com.example.entity.ProfileEntity;
import com.example.entity.ScheduleEntity;
import com.example.repository.AttendanceRepository;
import com.example.repository.GradeRepository;
import com.example.repository.ProfileRepository;
import com.example.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JournalService {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private ProfileRepository profileRepository;

//    public JournalTableDTO getFullJournal(String groupId, Integer subjectId, int year, int month) {
//        LocalDate startDate = LocalDate.of(year, month, 1);
//        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
//
//        // 1. Haftalik jadvaldan dars kunlarini aniqlash (Dushanba, Chorshanba va h.k.)
//        List<ScheduleEntity> weeklySchedules = scheduleRepository.findAllByGroupIdAndSubjectIdAndVisibleTrue(groupId, subjectId);
//        Set<DayOfWeek> lessonDayOfWeeks = weeklySchedules.stream()
//                .map(s -> DayOfWeek.valueOf(s.getWeekDay().name()))
//                .collect(Collectors.toSet());
//
//        // 2. Oydagi barcha dars sanalarini hisoblash
//        List<LocalDate> allLessonDates = new ArrayList<>();
//        LocalDate tempDate = startDate;
//        while (!tempDate.isAfter(endDate)) {
//            if (lessonDayOfWeeks.contains(tempDate.getDayOfWeek())) {
//                allLessonDates.add(tempDate);
//            }
//            tempDate = tempDate.plusDays(1);
//        }
//
//        // 3. Talabalar ro'yxatini olish
//        List<ProfileEntity> students = profileRepository.findAllByGroupIdAndVisibleTrue(groupId);
//
//        // 4. Barcha baho va davomatlarni bitta so'rovda yuklash
//        List<GradeEntity> grades = gradeRepository.findAllByGroupIdAndSubjectIdAndLessonDateBetweenAndVisibleTrue(groupId, subjectId, startDate, endDate);
//        List<AttendanceEntity> attendances = attendanceRepository.findAllByGroupIdAndDateBetween(groupId, startDate, endDate);
//
//        // 5. Ma'lumotlarni Map-ga joylash (StudentId -> (Date -> Value))
//        Map<String, Map<LocalDate, String>> dataMap = new HashMap<>();
//        for (GradeEntity g : grades) {
//            dataMap.computeIfAbsent(g.getStudentId(), k -> new HashMap<>())
//                    .put(g.getLessonDate(), g.getGradeValue().toString());
//        }
//        for (AttendanceEntity a : attendances) {
//            if ("ABSENT".equals(a.getStatus().name())) { // Agar o'quvchi kelmagan bo'lsa
//                dataMap.computeIfAbsent(a.getStudentId(), k -> new HashMap<>())
//                        .put(a.getDate(), "NB");
//            }
//        }
//
//        // 6. Response uchun qatorlarni yig'ish
//        List<JournalRowDTO> rows = new ArrayList<>();
//        for (ProfileEntity student : students) {
//            JournalRowDTO row = new JournalRowDTO();
//            row.setStudentId(student.getId());
//            Map<LocalDate, String> dailyData = new HashMap<>();
//
//            for (LocalDate date : allLessonDates) {
//                dailyData.put(date, dataMap.getOrDefault(student.getId(), new HashMap<>()).getOrDefault(date, ""));
//            }
//            row.setDailyData(dailyData);
//            rows.add(row);
//        }
//
//        return new JournalTableDTO(
//                students.stream().map(s -> new JournalStudentDTO(s.getId(), s.getFirstname() + " " + s.getLastname())).toList(),
//                allLessonDates,
//                rows
//        );
//    }
public JournalTableDTO getSemesterJournal(String groupId, Integer subjectId, Integer semester) {
    LocalDate now = LocalDate.now();
    int currentYear = now.getYear();

    LocalDate startDate;
    LocalDate endDate;

    if (semester == 1) {
        // 1-Semestr: O'tgan yil yoki shu yil sentabrdan - 25-dekabrgacha
        // Agar hozir yanvar bo'lsa, 1-semestr o'tgan yil sentabrda bo'lgan
        int startYear = (now.getMonthValue() < 9) ? currentYear - 1 : currentYear;
        startDate = LocalDate.of(startYear, 9, 1);
        endDate = LocalDate.of(startYear, 12, 25);
    } else {
        // 2-Semestr: 12-yanvardan - 30-iyungacha
        int startYear = (now.getMonthValue() >= 9) ? currentYear + 1 : currentYear;
        startDate = LocalDate.of(startYear, 1, 12);
        endDate = LocalDate.of(startYear, 6, 30);
    }

    // 1. Haftalik jadvalni olish
    List<ScheduleEntity> weeklySchedules = scheduleRepository.findAllByGroupIdAndSubjectIdAndVisibleTrue(groupId, subjectId);
    Set<DayOfWeek> lessonDays = weeklySchedules.stream()
            .map(s -> DayOfWeek.valueOf(s.getWeekDay().name()))
            .collect(Collectors.toSet());

    // 2. Semestr davomidagi barcha dars kunlarini hisoblash
    List<LocalDate> allLessonDates = new ArrayList<>();
    LocalDate tempDate = startDate;
    while (!tempDate.isAfter(endDate)) {
        if (lessonDays.contains(tempDate.getDayOfWeek())) {
            allLessonDates.add(tempDate);
        }
        tempDate = tempDate.plusDays(1);
    }

    // 3. Talabalar, Baholar va Davomatni yuklash (Semestr oralig'ida)
    List<ProfileEntity> students = profileRepository.findAllByGroupIdAndVisibleTrue(groupId);
    List<GradeEntity> grades = gradeRepository.findAllByGroupIdAndSubjectIdAndLessonDateBetweenAndVisibleTrue(
            groupId, subjectId, startDate, endDate);
    List<AttendanceEntity> attendances = attendanceRepository.findAllByGroupIdAndDateBetween(
            groupId, startDate, endDate);

    // 4. Ma'lumotlarni Map-ga yig'ish
    Map<String, Map<LocalDate, String>> dataMap = new LinkedHashMap<>();
    for (GradeEntity g : grades) {
        dataMap.computeIfAbsent(g.getStudentId(), k -> new HashMap<>())
                .put(g.getLessonDate(), g.getGradeValue().toString());
    }
    for (AttendanceEntity a : attendances) {
        if ("ABSENT".equals(a.getStatus().name())) {
            dataMap.computeIfAbsent(a.getStudentId(), k -> new HashMap<>())
                    .put(a.getDate(), "NB");
        }
    }

    // 5. Response Rows
    List<JournalRowDTO> rows = new ArrayList<>();
    for (ProfileEntity student : students) {
        JournalRowDTO row = new JournalRowDTO();
        row.setStudentId(student.getId());
        Map<LocalDate, String> dailyData = new LinkedHashMap<>();
        for (LocalDate date : allLessonDates) {
            dailyData.put(date, dataMap.getOrDefault(student.getId(), new HashMap<>()).getOrDefault(date, ""));
        }
        row.setDailyData(dailyData);
        rows.add(row);
    }

    return new JournalTableDTO(
            students.stream().map(s -> new JournalStudentDTO(s.getId(), s.getFirstname() + " " + s.getLastname())).toList(),
            allLessonDates,
            rows
    );
}
}