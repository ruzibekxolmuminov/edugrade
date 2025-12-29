package com.example.controller;

import com.example.dto.faculty.FacultyCreateDTO;
import com.example.dto.faculty.FacultyDTO;
import com.example.dto.faculty.FacultyFullInfoDTO;
import com.example.dto.faculty.FacultyUpdateDTO;
import com.example.service.FacultyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/faculty")
@Tag(name = "Faculty APIs", description = "API for faculty")
public class FacultyController {
    @Autowired
    private FacultyService facultyService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create/admin")
    @Operation(summary = "Faculty create", description = "Api used for create faculty by admin")
    public ResponseEntity<FacultyCreateDTO> createFaculty(@RequestBody FacultyDTO facultyDTO) {
        return ResponseEntity.ok(facultyService.create(facultyDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/admin/{id}")
    @Operation(summary = "Faculty update", description = "Api used for update faculty by admin")
    public ResponseEntity<String> updateFaculty(@PathVariable("id") String id, @RequestBody FacultyUpdateDTO facultyDTO) {
        return ResponseEntity.ok(facultyService.update(id, facultyDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/admin/{id}")
    @Operation(summary = "Faculty delete", description = "Api used for delte faculty by admin")
    public ResponseEntity<String> deleteFaculty(@PathVariable("id") String id) {
        return ResponseEntity.ok(facultyService.delete(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-list/admin/{schoolId}")
    @Operation(summary = "Get list faculty", description = "Api used for get list faculty by school by admin")
    public ResponseEntity<List<FacultyFullInfoDTO>> getFacultyList(@PathVariable("schoolId") String schoolId) {
        return ResponseEntity.ok(facultyService.getList(schoolId));
    }
}
