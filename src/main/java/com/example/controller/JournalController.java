package com.example.controller;

import com.example.dto.JournalTableDTO;
import com.example.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/journal")
public class JournalController {
    @Autowired
    private JournalService journalService;

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/semester-journal")
    public ResponseEntity<JournalTableDTO> getSemesterJournal(
            @RequestParam String groupId,
            @RequestParam Integer subjectId,
            @RequestParam Integer semester) { // 1 yoki 2

        return ResponseEntity.ok(journalService.getSemesterJournal(groupId, subjectId, semester));
    }
}