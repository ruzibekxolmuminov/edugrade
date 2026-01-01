package com.example.controller;

import com.example.dto.schedule.ScheduleDTO;
import com.example.dto.schedule.ScheduleCreateDTO;
import com.example.dto.schedule.ScheduleUpdateDTO;
import com.example.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/schedule")
@Tag(name = "Schedule APIs", description = "Api for schedule control")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create/admin")
    @Operation(summary = "Create schedule", description = "Api used for create schedule by admin")
    public ResponseEntity<ScheduleDTO> createSchedule(@RequestBody ScheduleCreateDTO scheduleDTO) {
        return ResponseEntity.ok(scheduleService.create(scheduleDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/admin/{id}")
    @Operation(summary = "Update schedule", description = "Api used for update schedule by admin")
    public ResponseEntity<String> updateSchedule(@PathVariable("id") String id, @RequestBody ScheduleUpdateDTO scheduleUpdateDTO) {
        return ResponseEntity.ok(scheduleService.update(id, scheduleUpdateDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/admin/{id}")
    @Operation(summary = "Delete schedule", description = "Api used for delete schedule by admin")
    public ResponseEntity<String> deleteSchedule(@PathVariable("id") String id) {
        return ResponseEntity.ok(scheduleService.delete(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-by-group-id/admin/{id}")
    @Operation(summary = "Get schedule", description = "Api used for get schedule by group id")
    public ResponseEntity<ScheduleDTO> getSchedule(@PathVariable("id") String id) {
        return ResponseEntity.ok(scheduleService.getByGroupId(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-by-teacher-id/admin/{id}")
    @Operation(summary = "Get schedule", description = "Api used for get schedule by teacher id")
    public ResponseEntity<ScheduleDTO> getTeacherSchedule(@PathVariable("id") String id) {
        return ResponseEntity.ok(scheduleService.getByTeacherId(id));
    }
}
