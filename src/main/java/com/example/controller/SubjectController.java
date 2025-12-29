package com.example.controller;

import com.example.dto.SubjectDTO;
import com.example.dto.SubjectFullInfoDTO;
import com.example.dto.TeacherSubjectDTO;
import com.example.service.SubjectService;
import com.example.service.TeacherSubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/subject")
@Tag(name = "Subject APIs", description = "Api for subjects")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private TeacherSubjectService teacherSubjectService;

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/create/moderator")
    @Operation(summary = "Create subject", description = "Api used for create subject by admin")
    public ResponseEntity<SubjectFullInfoDTO> create(@RequestBody SubjectDTO subjectDTO) {
        return ResponseEntity.ok(subjectService.create(subjectDTO));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PutMapping("/update/moderator/{id}")
    @Operation(summary = "Update subject", description = "Api used for update subject by moderator")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody SubjectDTO subjectDTO) {
        return ResponseEntity.ok(subjectService.update(id, subjectDTO));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @DeleteMapping("/delete/moderator/{id}")
    @Operation(summary = "Delete subject", description = "Api used for delete subject by moderator")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(subjectService.delete(id));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/get-list/moderator")
    @Operation(summary = "Subject list", description = "Api used for get subject list by moderator")
    public ResponseEntity<List<SubjectFullInfoDTO>> getAll() {
        return ResponseEntity.ok(subjectService.getAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assign-teacher/admin")
    @Operation(summary = "Assign subject to teacher", description = "API used for assign subjects to teachers by admin")
    public ResponseEntity<String> assignTeacher(@RequestBody TeacherSubjectDTO dto) {
        return ResponseEntity.ok(teacherSubjectService.assign(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/remove/admin/{id}")
    @Operation(summary = "Remove assign from teacher", description = "Api used for remove assign from teacher")
    public ResponseEntity<String> remove(@PathVariable Integer id) {
        String result = teacherSubjectService.removeAssignment(id);
        return ResponseEntity.ok(result);
    }
}
