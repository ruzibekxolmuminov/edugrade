package com.example.controller;

import com.example.dto.EventCreateDTO;
import com.example.dto.EventParticipantDTO;
import com.example.dto.EventResponseDTO;
import com.example.enums.GeneralStatus;
import com.example.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody EventCreateDTO dto) {
        return ResponseEntity.ok(eventService.createEvent(dto));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/register/{eventId}")
    public ResponseEntity<String> register(@PathVariable Integer eventId) {
        return ResponseEntity.ok(eventService.register(eventId));
    }

    @GetMapping("/list/{schoolId}")
    public ResponseEntity<Page<EventResponseDTO>> getList(
            @PathVariable String schoolId,
            @RequestParam(defaultValue = "ACTIVE") GeneralStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(eventService.getList(schoolId, status, page, size));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @GetMapping("/{eventId}/participants")
    public ResponseEntity<Page<EventParticipantDTO>> getParticipants(
            @PathVariable Integer eventId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(eventService.getParticipants(eventId, page, size));
    }
}