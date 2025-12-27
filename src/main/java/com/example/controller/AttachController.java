package com.example.controller;

import com.example.dto.AttachDTO;
import com.example.service.AttachService;
import com.example.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/attach")
@Tag(name = "Attach APIs", description = "Api for attaching")
public class AttachController {
    @Autowired
    private AttachService attachService;

    @PostMapping("/upload")
    @Operation(summary = "Upload new attach", description = "Api used for upload attaches")
    public ResponseEntity<AttachDTO> create(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(attachService.upload(file));
    }

    @GetMapping("/open/{fileId}")
    @Operation(summary = "Open attach", description = "Api used for open attaches")
    public ResponseEntity<Resource> open(@PathVariable String fileId) {
        return attachService.open(fileId);
    }

    @GetMapping("/download/{fileId}")
    @Operation(summary = "Download attach", description = "Api used for download attaches")
    public ResponseEntity<Resource> download(@PathVariable String fileId) {
        return attachService.download(fileId);
    }

    @DeleteMapping("/{fileId}")
    @Operation(summary = "Delete attach", description = "Api used for delete attaches")
    public ResponseEntity<Boolean> delete(@PathVariable String fileId) {
        return ResponseEntity.ok(attachService.delete(fileId));
    }

    @GetMapping("")
    public ResponseEntity<Page<AttachDTO>> getAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(attachService.pagination(PageUtil.page(page), size));
    }
}

