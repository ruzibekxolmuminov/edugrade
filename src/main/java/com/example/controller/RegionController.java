package com.example.controller;

import com.example.dto.RegionDTO;
import com.example.enums.AppLanguageEnum;
import com.example.service.RegionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/region")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/moderator")
    public ResponseEntity<RegionDTO> create(@Valid @RequestBody RegionDTO dto) {
        return ResponseEntity.ok(regionService.create(dto));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PutMapping("/moderator/{id}")
    public ResponseEntity<RegionDTO> update(@PathVariable("id") Integer id,
                                            @Valid @RequestBody RegionDTO newDto) {
        return ResponseEntity.ok(regionService.update(id, newDto));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @DeleteMapping("/moderator/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(regionService.delete(id));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/moderator")
    public ResponseEntity<List<RegionDTO>> all() {
        return ResponseEntity.ok(regionService.getAll());
    }

    @GetMapping("/lang")
    public ResponseEntity<List<RegionDTO>> getByLang(@RequestHeader(name = "Accept-Language", defaultValue = "uz") AppLanguageEnum language) {
        return ResponseEntity.ok(regionService.getAllByLang(language));
    }
}
