package com.example.controller;

import com.example.dto.AttendanceStatsDTO;
import com.example.dto.MonthlyAttendanceDTO;
import com.example.dto.attendance.AttendanceCreateDTO;
import com.example.dto.attendance.AttendanceDTO;
import com.example.dto.attendance.AttendanceUpdateDTO;
import com.example.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/attendance")
@Tag(name = "Attendance APIs", description = "Api for attendance control")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/set-attendance/teacher")
    @Operation(summary = "Set attendance", description = "Api used for set attendance by teacher")
    public ResponseEntity<AttendanceDTO> setAttendance(@RequestBody AttendanceCreateDTO attendanceDTO) {
        return ResponseEntity.ok(attendanceService.setAttendance(attendanceDTO));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/update/teacher/{id}")
    @Operation(summary = "UPDATE attendance", description = "Api used for update attendance by teacher")
    public ResponseEntity<String> update(@PathVariable String id, @RequestBody AttendanceUpdateDTO attendanceUpdateDTO) {
        return ResponseEntity.ok(attendanceService.update(id, attendanceUpdateDTO));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/delete/teacher/{id}")
    @Operation(summary = "DELETE attendance", description = "Api used for delete attendance by teacher")
    public ResponseEntity<String> delete(@PathVariable String id) {
        return ResponseEntity.ok(attendanceService.delete(id));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/get-by-student/{id}")
    @Operation(summary = "GET attendance", description = "Api used for get attendance by student id")
    public ResponseEntity<List<AttendanceDTO>> getByStudent(@PathVariable String id) {
        return ResponseEntity.ok(attendanceService.getByStudent(id));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/get-by-group/{id}")
    @Operation(summary = "GET attendance", description = "Api used for get attendance by group id")
    public ResponseEntity<List<AttendanceDTO>> getByGroup(@PathVariable String id) {
        return ResponseEntity.ok(attendanceService.getByGroup(id));
    }

    @GetMapping("/stats/{studentId}")
    public ResponseEntity<AttendanceStatsDTO> getStudentStats(
            @PathVariable String studentId,
            @RequestParam Integer subjectId) {

        return ResponseEntity.ok(attendanceService.getStudentStats(studentId, subjectId));
    }
    @GetMapping("/monthly-overview/{studentId}")
    public ResponseEntity<List<MonthlyAttendanceDTO>> getMonthlyOverview(
            @PathVariable String studentId) {

        return ResponseEntity.ok(attendanceService.getMonthlyOverview(studentId));
    }
}
