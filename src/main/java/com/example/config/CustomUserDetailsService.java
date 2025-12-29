package com.example.config;

import com.example.entity.ProfileEntity;
import com.example.enums.profile.ProfileRoleEnum;
import com.example.exp.AppBadException;
import com.example.repository.ProfileRepository;
import com.example.repository.ProfileRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileRoleRepository profileRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String passportNumber) throws UsernameNotFoundException {
        Optional<ProfileEntity> optional = profileRepository.findByPassportNumberAndVisibleIsTrue(passportNumber);
        if (optional.isEmpty()) {
            throw new AppBadException("User name not found");
        }
        ProfileEntity profile = optional.get();
        List<ProfileRoleEnum> roleList = profileRoleRepository.getRoleListByProfileId(profile.getId());
        return new CustomUserDetails(profile.getId(),
                profile.getPassportNumber(),
                profile.getPassword(),
                profile.getStatus(),
                roleList);
    }
}
