package com.example.util;

import com.example.config.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityUtil {

//    public static String currentProfileId() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
//        return user.getId();
//    }

    public static String currentProfileId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CustomUserDetails) {

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getId(); // Login response'dagi UUID id
        }

        // Agar foydalanuvchi tanilmasa, xato berish yoki null qaytarish
        throw new RuntimeException("Foydalanuvchi topilmadi yoki token xato!");
    }
}
