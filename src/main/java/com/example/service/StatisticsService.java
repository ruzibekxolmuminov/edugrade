package com.example.service;

import com.example.dto.GlobalStatsDTO;
import com.example.dto.SchoolStatsDTO;
import com.example.dto.TopSchoolDTO;
import com.example.enums.profile.ProfileRoleEnum;
import com.example.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private EventRepository eventRepository;

    /**
     * 16.1 Get School Stats (ADMIN)
     * Maktab darajasidagi statistikani hisoblash
     */
    public SchoolStatsDTO getSchoolStats(String schoolId) {
        // 1. O'quvchilar sonini hisoblash (JOIN orqali)
        Long studentCount = profileRepository.countBySchoolIdAndRole(schoolId, ProfileRoleEnum.ROLE_STUDENT);

        // 2. O'qituvchilar sonini hisoblash (JOIN orqali)
        Long teacherCount = profileRepository.countBySchoolIdAndRole(schoolId, ProfileRoleEnum.ROLE_TEACHER);

        // 3. Maktab bo'yicha o'rtacha GPA (SQL AVG funksiyasi orqali)
        Double averageGPA = gradeRepository.getAverageGpaBySchool(schoolId);

        // 4. Davomat foizi (SQL CASE WHEN orqali)
        Double attendancePercentage = attendanceRepository.getAttendancePercentageBySchool(schoolId);

        // DTO ga ma'lumotlarni joylashtiramiz (null bo'lsa 0.0 qaytaramiz)
        return new SchoolStatsDTO(
                studentCount != null ? studentCount : 0L,
                teacherCount != null ? teacherCount : 0L,
                averageGPA != null ? Math.round(averageGPA * 100.0) / 100.0 : 0.0,
                attendancePercentage != null ? Math.round(attendancePercentage * 10.0) / 10.0 : 0.0
        );
    }

    /**
     * 16.2 Get Global Stats (MODERATOR)
     * Tizimdagi barcha maktablar bo'yicha umumiy statistika
     */
    public GlobalStatsDTO getGlobalStats() {
        Long schools = profileRepository.countTotalSchools();
        Long students = profileRepository.countTotalStudents();
        Long teachers = profileRepository.countTotalTeachers();
        Long events = eventRepository.count(); // Taxminiy

        List<Object[]> topSchoolsRaw = profileRepository.getTopSchoolsNative();
        List<TopSchoolDTO> topSchools = topSchoolsRaw.stream()
                .map(row -> new TopSchoolDTO(
                        (String) row[0],
                        (String) row[1],
                        ((Number) row[2]).doubleValue(),
                        ((Number) row[3]).doubleValue()
                ))
                .collect(Collectors.toList());

        return new GlobalStatsDTO(schools, students, teachers, events, topSchools);
    }
}