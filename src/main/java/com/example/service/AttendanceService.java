package com.example.service;

import com.example.dto.attendance.AttendanceCreateDTO;
import com.example.dto.attendance.AttendanceDTO;
import com.example.dto.attendance.AttendanceUpdateDTO;
import com.example.entity.AttendanceEntity;
import com.example.exp.AppBadException;
import com.example.exp.Exist;
import com.example.repository.AttendanceRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    public @Nullable AttendanceDTO setAttendance(AttendanceCreateDTO attendanceDTO) {
        Optional<AttendanceEntity> isExist = attendanceRepository.findByStudentIdAndDateAndScheduleIdAndSchoolId(attendanceDTO.getStudentId(),attendanceDTO.getDate(),attendanceDTO.getScheduleId(),attendanceDTO.getSchoolId());
        if (isExist.isPresent()) {
            throw new Exist("Attendance already exist");
        }
        AttendanceEntity attendance = new AttendanceEntity();
        attendance.setStudentId(attendanceDTO.getStudentId());
        attendance.setDate(attendanceDTO.getDate());
        attendance.setScheduleId(attendanceDTO.getScheduleId());
        attendance.setSchoolId(attendanceDTO.getSchoolId());
        attendance.setCreatedDate(LocalDateTime.now());
        attendance.setGroupId(attendanceDTO.getGroupId());
        attendance.setReason(attendanceDTO.getReason());
        attendance.setStatus(attendanceDTO.getStatus());
        attendance.setDate(attendanceDTO.getDate());
        attendanceRepository.save(attendance);
        return toDTO(attendance);
    }

    private @Nullable AttendanceDTO toDTO(AttendanceEntity attendance) {
        AttendanceDTO dto = new AttendanceDTO();
        dto.setId(attendance.getId());
        dto.setStudentId(attendance.getStudentId());
        dto.setSchoolId(attendance.getSchoolId());
        dto.setScheduleId(attendance.getScheduleId());
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

    public @Nullable AttendanceDTO getByStudent(String id) {
        Optional<AttendanceEntity> attendance = attendanceRepository.getByStudentId(id);
        if (attendance.isEmpty()) {
            throw new Exist("Attendance does not exist");
        }

        return toDTO(attendance.get());
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
}
