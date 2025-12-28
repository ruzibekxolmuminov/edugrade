package com.example.service;

import com.example.dto.profile.ProfileInfoDTO;
import com.example.dto.auth.*;
import com.example.dto.profile.ProfileUpdateModeratorRequestDTO;
import com.example.entity.ProfileEntity;
import com.example.enums.ProfileRoleEnum;
import com.example.enums.ProfileStatus;
import com.example.exp.AppBadException;
import com.example.exp.UserExist;
import com.example.exp.UserNotFound;
import com.example.repository.ProfileRepository;
import com.example.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
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
    @Autowired
    private SchoolService schoolService;

    public @Nullable AuthCreateResponseDTO registrationByModerator(AuthModeratorCreateDTO profile) {
        Optional<ProfileEntity> existOptional = profileRepository.findByPinflOrPassportNumber(profile.getPinfl()
                , profile.getPassportNumber());
        if (existOptional.isPresent()) {
            ProfileEntity existProfile = existOptional.get();
            if (existProfile.getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
                profileRoleService.deleteRoleByProfileId(existProfile.getId());
                profileRepository.deleteById(existProfile.getId());
            } else {
                throw new UserExist("With this same Pinfl, PassportNumber user is existed");
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
        profileRoleService.createModerator(createProfile.getId(), profile.getRoles());

        return toResponseDTO(createProfile);
    }

    private @Nullable AuthCreateResponseDTO toResponseDTO(ProfileEntity profile) {
        AuthCreateResponseDTO responseDTO = new AuthCreateResponseDTO();
        responseDTO.setId(profile.getId());
        responseDTO.setPinfl(profile.getPinfl());
        responseDTO.setPassportNumber(profile.getPassportNumber());
        responseDTO.setPassportSeries(profile.getPassportSeries());
        responseDTO.setGender(profile.getGender());
        responseDTO.setFirstName(profile.getFirstname());
        responseDTO.setLastName(profile.getLastname());
        responseDTO.setPatronymic(profile.getPatronymic());
        responseDTO.setBirthDate(profile.getBirthdate());
        responseDTO.setRoles(profileRoleService.getByProfileId(profile.getId()));
        responseDTO.setSchoolId(profile.getSchoolId());

        return responseDTO;
    }

    public @Nullable AuthCreateResponseDTO registrationByAdmin(AuthAdminCreateDTO profile) {
        Optional<ProfileEntity> existOptional = profileRepository.findByPinflOrPassportNumber(profile.getPinfl()
                , profile.getPassportNumber());
        if (existOptional.isPresent()) {
            ProfileEntity existProfile = existOptional.get();
            if (existProfile.getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
                profileRoleService.deleteRoleByProfileId(existProfile.getId());
                profileRepository.deleteById(existProfile.getId());
            } else {
                throw new UserExist("With this Pinfl, PassportNumber and PassportSeries user is existed");
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
        profileRoleService.createAdmin(createProfile.getId(), profile.getRoles());
        return toResponseDTO(createProfile);
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


    public @Nullable ProfileInfoDTO getByIdModerator(String id) {
        Optional<ProfileEntity> profileEntity = profileRepository.findById(id);
        if (profileEntity.isEmpty()) {
            throw new UserNotFound("User not found");
        }
        ProfileEntity profile = profileEntity.get();

        return toDTO(profile);
    }

    private ProfileInfoDTO toDTO(ProfileEntity entity) {
        ProfileInfoDTO dto = new ProfileInfoDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getFirstname());
        dto.setSurname(entity.getLastname());
        dto.setPatronymic(entity.getPatronymic());
        dto.setStatus(entity.getStatus());
        dto.setSchoolId(entity.getSchoolId());
        dto.setSchoolName(schoolService.getById(entity.getSchoolId()));
        dto.setRoleList(profileRoleService.getByProfileId(entity.getId()));

        return dto;
    }

    public @Nullable ProfileInfoDTO getByIdAdmin(String id) {
        Optional<ProfileEntity> profileEntity = profileRepository.findById(id);
        if (profileEntity.isEmpty()) {
            throw new UserNotFound("User not found");
        }
        Optional<ProfileEntity> usernameOptional = profileRepository.findByIdAndVisibleIsTrue(id);
        if (usernameOptional.isEmpty()) {
            throw new AppBadException("Profile not found");
        }
        Optional<ProfileEntity> isModerator = profileRepository.isModerator(id);
        if (isModerator.isPresent()) {
            throw new UserNotFound("You can not get Moderator details! You are ADMIN");
        }
        ProfileEntity profile = profileEntity.get();

        return toDTO(profile);
    }

    public @Nullable String updateByModerator(String id, ProfileUpdateModeratorRequestDTO update) {

        ProfileEntity entity = get(id);
        if(update == null) {
            throw new AppBadException("You did not update any information!");
        }else if (update.getName() != null) {
            entity.setFirstname(update.getName());
        } else if (update.getSurname() != null) {
            entity.setLastname(update.getSurname());
        } else if (update.getStatus() != null) {
            entity.setStatus(update.getStatus());
        } else if (update.getSchoolId() != null) {
            entity.setSchoolId(update.getSchoolId());
        }
        profileRepository.save(entity); // update
        if (update.getRoleList() != null) {
            profileRoleService.merge(entity.getId(), update.getRoleList());

        }
        return "Muvaffaqiyatli yangilandi!";
    }

    public ProfileEntity get(String id) {
        return profileRepository.findByIdAndVisibleIsTrue(id).orElseThrow(() -> {
            throw new AppBadException("Profile not found");
        });
    }
}
