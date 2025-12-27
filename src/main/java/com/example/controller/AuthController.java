package com.example.controller;

import com.example.dto.auth.AuthAdminCreateDTO;
import com.example.dto.auth.AuthDTO;
import com.example.dto.auth.AuthLoginDTO;
import com.example.dto.auth.AuthModeratorCreateDTO;
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
    private ResponseEntity<String> registrationByModerator(@RequestBody AuthModeratorCreateDTO profileCreateDTO) {
        return ResponseEntity.ok(profileService.registrationByModerator(profileCreateDTO));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registration/admin")
    @Operation(summary = "Registration profile", description = "Api used for registration new profile by moderator")
    private ResponseEntity<String> registrationByAdmin(@RequestBody AuthAdminCreateDTO profileCreateDTO) {
        return ResponseEntity.ok(profileService.registrationByAdmin(profileCreateDTO));
    }

    @PostMapping("/login")
    @Operation(summary = "Login profile", description = "Api used for login profile")
    private ResponseEntity<AuthDTO> login(@RequestBody AuthLoginDTO profileLoginDTO){
        return ResponseEntity.ok(profileService.login(profileLoginDTO));
    }


}
