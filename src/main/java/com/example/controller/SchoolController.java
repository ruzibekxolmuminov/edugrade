package com.example.controller;

import com.example.dto.SchoolCreateDTO;
import com.example.dto.SchoolDTO;
import com.example.service.SchoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/school")
@Tag(name = "School APIs", description = "Api for school")
public class SchoolController {
    @Autowired
    private SchoolService schoolService;

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/create/moderator")
    @Operation(summary = "School create", description = "Api used for create new school by moderator")
    private ResponseEntity<SchoolDTO> createSchool(@RequestBody SchoolCreateDTO dto){
        return ResponseEntity.ok(schoolService.createSchool(dto));
    }
}
