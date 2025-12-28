package com.example.controller;

import com.example.dto.profile.ProfileInfoDTO;
import com.example.dto.auth.*;
import com.example.dto.profile.ProfileUpdateModeratorRequestDTO;
import com.example.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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

}
