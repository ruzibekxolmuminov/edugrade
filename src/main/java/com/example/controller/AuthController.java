package com.example.controller;

import com.example.dto.profile.*;
import com.example.dto.auth.*;
import com.example.entity.ProfileEntity;
import com.example.entity.ProfileRoleEntity;
import com.example.service.AuthService;
import com.example.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "Profile APIs", description = "Api for profile controls")
public class AuthController {
    @Autowired
    private AuthService profileService;

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/registration/moderator")
    @Operation(summary = "Registration profile", description = "Api used for registration new profile by moderator")
    public ResponseEntity<AuthCreateResponseDTO> registrationByModerator(@RequestBody AuthModeratorCreateDTO profileCreateDTO) {
        return ResponseEntity.ok(profileService.registrationByModerator(profileCreateDTO));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registration/admin")
    @Operation(summary = "Registration profile", description = "Api used for registration new profile by moderator")
    public ResponseEntity<AuthCreateResponseDTO> registrationByAdmin(@RequestBody AuthAdminCreateDTO profileCreateDTO) {
        return ResponseEntity.ok(profileService.registrationByAdmin(profileCreateDTO));
    }

    @PostMapping("/login")
    @Operation(summary = "Login profile", description = "Api used for login profile")
    public ResponseEntity<AuthDTO> login(@RequestBody AuthLoginDTO profileLoginDTO){
        return ResponseEntity.ok(profileService.login(profileLoginDTO));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/get/moderator/{id}")
    @Operation(summary = "Get profile by Id", description = "Api used for get profile by id by moderator")
    public ResponseEntity<ProfileInfoDTO> getByIdModerator(@PathVariable String id){
        return ResponseEntity.ok(profileService.getByIdModerator(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get/admin/{id}")
    @Operation(summary = "Get profile by Id", description = "Api used for get profile by id by admin")
    public ResponseEntity<ProfileInfoDTO> getByIdAdmin(@PathVariable String id){
        return ResponseEntity.ok(profileService.getByIdAdmin(id));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PutMapping("/update/moderator/{id}")
    @Operation(summary = "Update profile by moderator", description = "Api used for update profile details by moderator")
    public ResponseEntity<String> updateByModerator(@PathVariable("id") String id, @RequestBody ProfileUpdateModeratorRequestDTO update){
        return ResponseEntity.ok(profileService.updateByModerator(id,update));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/admin/{id}")
    @Operation(summary = "Update profile by admin", description = "Api used for update profile details by admin")
    public ResponseEntity<String> updateByAdmin(@PathVariable("id") String id, @RequestBody ProfileUpdateAdminRequestDTO update){
        return ResponseEntity.ok(profileService.updateByAdmins(id,update));
    }

    @PreAuthorize("hasAllRoles('MODERATOR', 'ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<Page<ProfileInfoDTO>> getProfileList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String schoolId,
            @RequestParam(required = false) String role) {

        return ResponseEntity.ok(profileService.getProfileList(page, size, schoolId, role));
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @DeleteMapping("/moderator/delete/{id}")
    public ResponseEntity<String> deleteByModerator(@PathVariable("id") String id) {
        return ResponseEntity.ok(profileService.deleteByModerator(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> deleteByAdmin(@PathVariable("id") String id) {
        return ResponseEntity.ok(profileService.deleteByAdmin(id));
    }

    @PutMapping("/update/password")
    @Operation(summary = "Update password", description = "Api used for update password")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateDTO update){
        return ResponseEntity.ok(profileService.updatePassword(update));
    }

    @PreAuthorize("hasAllRoles('MODERATOR')")
    @PostMapping("/filter")
    public ResponseEntity<Page<ProfileInfoDTO>> filter(
            @RequestBody ProfileFilterDTO filterDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(profileService.filter(filterDTO, page, size));
    }
}
