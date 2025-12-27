package com.example.service;

import com.example.dto.auth.AuthAdminCreateDTO;
import com.example.dto.auth.AuthDTO;
import com.example.dto.auth.AuthLoginDTO;
import com.example.dto.auth.AuthModeratorCreateDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileAdminRoleEnum;
import com.example.enums.ProfileRoleEnum;
import com.example.enums.ProfileStatus;
import com.example.exp.AppBadException;
import com.example.exp.UserNotFound;
import com.example.repository.ProfileRepository;
import com.example.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileRoleService profileRoleService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public @Nullable String registrationByModerator(AuthModeratorCreateDTO profile) {
        Optional<ProfileEntity> existOptional = profileRepository.findByPinflOrPassportNumberOrPassportSeries(profile.getPinfl()
                , profile.getPassportNumber()
                , profile.getPassportSeries());
        if (existOptional.isPresent()) {
            ProfileEntity existProfile = existOptional.get();
            if (existProfile.getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
                profileRoleService.deleteRoleByProfileId(existProfile.getId());
                profileRepository.deleteById(existProfile.getId());
            } else {
                log.error("With this Pinfl: {}, PassportNumber: {} or PassportSeries: {} user is existed", profile.getPinfl(), profile.getPassportNumber(), profile.getPassportSeries());
            }
        }
        ProfileEntity createProfile = new ProfileEntity();
        createProfile.setPassword(bCryptPasswordEncoder.encode(profile.getPassword()));
        createProfile.setPinfl(profile.getPinfl());
        createProfile.setPassportNumber(profile.getPassportNumber());
        createProfile.setPassportSeries(profile.getPassportSeries());
        createProfile.setBirthdate(profile.getBirthDate());
        createProfile.setGender(profile.getGender());
        createProfile.setLastname(profile.getLastName());
        createProfile.setFirstname(profile.getFirstName());
        createProfile.setSchoolId(profile.getSchoolId());
        createProfile.setPatronymic(profile.getPatronymic());
        createProfile.setStatus(ProfileStatus.ACTIVE);
        createProfile.setVisible(true);
        profileRepository.save(createProfile);
        profileRoleService.create(createProfile.getId(), ProfileRoleEnum.ROLE_ADMIN);

        return "Tasdiqlash kodi ketdi. Iltimos tekshiring!";
    }

    public @Nullable String registrationByAdmin(AuthAdminCreateDTO profile) {
        Optional<ProfileEntity> existOptional = profileRepository.findByPinflOrPassportNumberOrPassportSeries(profile.getPinfl()
                , profile.getPassportNumber()
                , profile.getPassportSeries());
        if (existOptional.isPresent()) {
            ProfileEntity existProfile = existOptional.get();
            if (existProfile.getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
                profileRoleService.deleteRoleByProfileId(existProfile.getId());
                profileRepository.deleteById(existProfile.getId());
            } else {
                log.error("With this Pinfl: {}, PassportNumber: {} or PassportSeries: {} user is existed", profile.getPinfl(), profile.getPassportNumber(), profile.getPassportSeries());
            }
        }
        ProfileEntity createProfile = new ProfileEntity();
        createProfile.setPassword(bCryptPasswordEncoder.encode(profile.getPassword()));
        createProfile.setPinfl(profile.getPinfl());
        createProfile.setPassportNumber(profile.getPassportNumber());
        createProfile.setPassportSeries(profile.getPassportSeries());
        createProfile.setBirthdate(profile.getBirthDate());
        createProfile.setGender(profile.getGender());
        createProfile.setLastname(profile.getLastName());
        createProfile.setFirstname(profile.getFirstName());
        createProfile.setSchoolId(profile.getSchoolId());
        createProfile.setPatronymic(profile.getPatronymic());
        createProfile.setStatus(ProfileStatus.ACTIVE);
        createProfile.setVisible(true);
        profileRepository.save(createProfile);
        List<ProfileAdminRoleEnum> roleEnums = profile.getRoles();

        profileRoleService.create(createProfile.getId(), profile.getRoles());


        return "Profile muvaffaqiyatli yaratildi!";
    }

    public @Nullable AuthDTO login(AuthLoginDTO profileLoginDTO) {
        Optional<ProfileEntity> profileEntity = profileRepository.findByPassportNumberAndVisibleIsTrue(profileLoginDTO.getPassportNumber());
        if (!profileEntity.isPresent()) {
            throw new UserNotFound("Passport name or password is incorrect");
        }
        ProfileEntity profile = profileEntity.get();
        List<ProfileRoleEnum> profileRoleEnums = profileRoleService.getByProfileId(profile.getId());
        if (!profileRoleEnums.contains(ProfileRoleEnum.ROLE_MODERATOR)) {
            if (!bCryptPasswordEncoder.matches(profileLoginDTO.getPassword(), profile.getPassword())) {
                System.out.println(profileLoginDTO.getPassword() + " " + profile.getPassword());
                throw new UserNotFound("Passport name or password is incorrect");
            }
        }
        if (profile.getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
            throw new AppBadException("User in wrong status");
        }

        AuthDTO profileDTO = new AuthDTO();
        profileDTO.setFirstName(profile.getFirstname());
        profileDTO.setLastName(profile.getLastname());
        profileDTO.setPatronymic(profile.getPatronymic());
        profileDTO.setPassportNumber(profile.getPassportNumber());
        profileDTO.setPassportSeries(profile.getPassportSeries());
        profileDTO.setBirthDate(profile.getBirthdate());
        profileDTO.setPinfl(profile.getPinfl());
        profileDTO.setRoles(profileRoleService.getByProfileId(profile.getId()));
        profileDTO.setGender(profile.getGender());
        profileDTO.setSchoolId(profile.getSchoolId());
        profileDTO.setJwt(JwtUtil.encode(profile.getPassportNumber(), profileDTO.getRoles()));
        return profileDTO;
    }
}
