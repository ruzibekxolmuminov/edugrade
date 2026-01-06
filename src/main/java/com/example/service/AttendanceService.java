package com.example.service;

import com.example.dto.AttendanceStatsDTO;
import com.example.dto.MonthlyAttendanceDTO;
import com.example.dto.attendance.AttendanceCreateDTO;
import com.example.dto.attendance.AttendanceDTO;
import com.example.dto.attendance.AttendanceUpdateDTO;
import com.example.entity.AttendanceEntity;
import com.example.entity.GradeEntity;
import com.example.enums.AttendanceStatus;
import com.example.enums.GradeType;
import com.example.exp.AppBadException;
import com.example.exp.Exist;
import com.example.repository.AttendanceRepository;
import com.example.repository.GradeRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private GradeRepository gradeRepository;

    public AttendanceDTO setAttendance(AttendanceCreateDTO attendanceDTO) {
        // 1. Log qo'shish (Metodga kirganini bilish uchun)
        System.out.println("DEBUG: setAttendance boshlandi. StudentID: " + attendanceDTO.getStudentId());

        Optional<AttendanceEntity> isExist = attendanceRepository.findByStudentIdAndDateAndScheduleIdAndSchoolIdAndSubjectId(
                attendanceDTO.getStudentId(),
                attendanceDTO.getDate(),
                attendanceDTO.getScheduleId(),
                attendanceDTO.getSchoolId(),
                attendanceDTO.getSubjectId()
        );

        if (isExist.isPresent()) {
            System.out.println("DEBUG: Davomat allaqachon mavjud!");
            throw new Exist("Attendance already exist");
        }

        try {
            AttendanceEntity attendance = new AttendanceEntity();
            attendance.setStudentId(attendanceDTO.getStudentId());
            attendance.setDate(attendanceDTO.getDate());
            attendance.setScheduleId(attendanceDTO.getScheduleId());
            attendance.setSchoolId(attendanceDTO.getSchoolId());
            attendance.setCreatedDate(LocalDateTime.now());
            attendance.setSubjectId(attendanceDTO.getSubjectId());
            attendance.setGroupId(attendanceDTO.getGroupId());
            attendance.setReason(attendanceDTO.getReason());
            attendance.setStatus(attendanceDTO.getStatus());

            AttendanceEntity saved = attendanceRepository.save(attendance);
            System.out.println("DEBUG: Davomat saqlandi. ID: " + saved.getId());

            return toDTO(saved);
        } catch (Exception e) {
            System.err.println("DEBUG XATO: " + e.getMessage());
            e.printStackTrace(); // Aniq SQL xatosini konsolda ko'rasiz
            throw e;
        }
    }

    private @Nullable AttendanceDTO toDTO(AttendanceEntity attendance) {
        AttendanceDTO dto = new AttendanceDTO();
        dto.setId(attendance.getId());
        dto.setStudentId(attendance.getStudentId());
        dto.setSchoolId(attendance.getSchoolId());
        dto.setScheduleId(attendance.getScheduleId());
        dto.setSubjectId(attendance.getSubjectId());
        dto.setDate(attendance.getDate());
        dto.setReason(attendance.getReason());
        dto.setGroupId(attendance.getGroupId());
        dto.setStatus(attendance.getStatus());
        dto.setCreatedAt(attendance.getCreatedDate());
        return dto;
    }

    public @Nullable String update(String id, AttendanceUpdateDTO attendanceUpdateDTO) {
        AttendanceEntity attendance = getById(id);
        if (attendanceUpdateDTO.getReason() != null) {
            attendance.setStatus(attendanceUpdateDTO.getStatus());
            attendance.setReason(attendanceUpdateDTO.getReason());
        }else if (attendanceUpdateDTO.getStatus() != null) {
            attendance.setStatus(attendanceUpdateDTO.getStatus());
        }
        attendanceRepository.save(attendance);
        return "Muvaffaqiyatli o'zgartirildi!";
    }

    private AttendanceEntity getById(String id) {
        Optional<AttendanceEntity> isExist = attendanceRepository.findById(id);
        if (isExist.isEmpty()) {
            throw new Exist("Attendance does not exist");
        }
        return isExist.get();
    }

    public @Nullable String delete(String id) {
        AttendanceEntity attendance = getById(id);
        attendanceRepository.delete(attendance);
        return "Muvaffaqiyatli o'chirildi!";
    }

    public @Nullable List<AttendanceDTO> getByStudent(String id) {
        List<AttendanceEntity> attendance = attendanceRepository.getByStudentId(id);
        if (attendance.isEmpty()) {
            throw new Exist("Attendance does not exist");
        }

        List<AttendanceDTO> dtos = new ArrayList<>();
        for (AttendanceEntity attendanceEntity : attendance) {
            dtos.add(toDTO(attendanceEntity));
        }
        return dtos;
    }

    public @Nullable List<AttendanceDTO> getByGroup(String id) {
        List<AttendanceEntity> attendance = attendanceRepository.getAllByGroupId(id);
        if (attendance.isEmpty()){
            throw new AppBadException("Attendance not found!");
        }
        List<AttendanceDTO> dtos = new ArrayList<>();
        for (AttendanceEntity attendanceEntity: attendance){
            dtos.add(toDTO(attendanceEntity));
        }
        return dtos;
    }

    public AttendanceStatsDTO getStudentStats(String studentId, Integer subjectId) {
        // 1. Talabaning ushbu fandan barcha davomat tarixini olish
        List<AttendanceEntity> attendanceList = attendanceRepository
                .findAllByStudentIdAndSubjectId(studentId, subjectId);

        if (attendanceList.isEmpty()) {
            return new AttendanceStatsDTO(100.0, "Good", 0L, 0L);
        }

        long totalLessons = attendanceList.size();

        // 2. ABSENT va REASONABLE larni sanaymiz
        long absentCount = attendanceList.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.ABSENT)
                .count();

        long reasonableCount = attendanceList.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.REASONABLE)
                .count();

        long totalAbsences = absentCount + reasonableCount;

        // 3. Davomat foizini hisoblash (Faqat PRESENT bo'lganlar)
        long presentCount = attendanceList.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT)
                .count();

        double rate = ((double) presentCount / totalLessons) * 100;

        // 4. Vidjet statusini aniqlash
        String statusLabel = (rate >= 90) ? "Good" : (rate >= 70 ? "Average" : "Poor");

        return new AttendanceStatsDTO(
                Math.round(rate * 10.0) / 10.0,
                statusLabel,
                reasonableCount, // "excused absences"
                totalAbsences
        );


    }
    public List<MonthlyAttendanceDTO> getMonthlyOverview(String studentId) {
        List<MonthlyAttendanceDTO> report = new ArrayList<>();
        LocalDate now = LocalDate.now();

        // Oxirgi 5 oyni aylanamiz
        for (int i = 4; i >= 0; i--) {
            LocalDate targetMonth = now.minusMonths(i);
            int year = targetMonth.getYear();
            int monthValue = targetMonth.getMonthValue();

            // Shu oyga tegishli barcha davomatlarni olish
            List<AttendanceEntity> monthlyData = attendanceRepository
                    .findByStudentIdAndMonthAndYear(studentId, monthValue, year);

            double percentage = 0.0;
            if (!monthlyData.isEmpty()) {
                long presentCount = monthlyData.stream()
                        .filter(a -> a.getStatus() == AttendanceStatus.PRESENT)
                        .count();
                percentage = ((double) presentCount / monthlyData.size()) * 100;
            }

            report.add(new MonthlyAttendanceDTO(
                    targetMonth.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                    Math.round(percentage * 10.0) / 10.0
            ));
        }
        return report;
    }
}
