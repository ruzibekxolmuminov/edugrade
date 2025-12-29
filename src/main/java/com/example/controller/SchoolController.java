package com.example.controller;

import com.example.dto.school.SchoolCreateDTO;
import com.example.dto.school.SchoolDTO;
import com.example.dto.school.SchoolUpdateDTO;
import com.example.service.SchoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/school")
@Tag(name = "School APIs", description = "Api for school")
public class SchoolController {
    @Autowired
    private SchoolService schoolService;

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/create/moderator")
    @Operation(summary = "School create", description = "Api used for create new school by moderator")
    public ResponseEntity<SchoolDTO> createSchool(@RequestBody SchoolCreateDTO dto){
        return ResponseEntity.ok(schoolService.createSchool(dto));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PutMapping("/update/moderator/{id}")
    @Operation(summary = "School update", description = "Api used for update school information by moderator")
    public ResponseEntity<String> updateSchool(@PathVariable("id") String id, @RequestBody SchoolUpdateDTO dto){
        return ResponseEntity.ok(schoolService.updateSchool(id, dto));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PutMapping("/update-status/moderator/{id}")
    @Operation(summary = "School update status", description = "Api used for update school status by moderator")
    public ResponseEntity<String> updateSchoolStatus(@PathVariable("id") String id, @RequestParam(defaultValue = "NOT_ACTIVE") String status){
        return ResponseEntity.ok(schoolService.updateStatus(id, status));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @DeleteMapping("/delete/moderator/{id}")
    @Operation(summary = "School delete", description = "Api used for delete school by moderator")
    public ResponseEntity<String> deleteSchool(@PathVariable("id") String id){
        return ResponseEntity.ok(schoolService.deleteById(id));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/get-by-id/moderator/{id}")
    @Operation(summary = "Get school", description = "Api used for get school by id")
    public ResponseEntity<SchoolDTO> getSchool(@PathVariable("id") String id){
        return ResponseEntity.ok(schoolService.getSchoolById(id));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/list/moderator")
    @Operation(summary = "Pagination school", description = "Api used for pagination school by moderator")
    public ResponseEntity<Page<SchoolDTO>> pagination(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(required = false) String name,
                                                      @RequestParam(required = false) String regionId,
                                                      @RequestParam(required = false) String status){
        return ResponseEntity.ok(schoolService.pagination(page,size,name,regionId,status));
    }

}
