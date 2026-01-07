package com.example.service;

import com.example.dto.profile.*;
import com.example.dto.auth.*;
import com.example.entity.ChatMember;
import com.example.entity.ProfileEntity;
import com.example.entity.ProfileRoleEntity;
import com.example.enums.profile.ProfileRoleEnum;
import com.example.enums.profile.ProfileStatus;
import com.example.exp.AppBadException;
import com.example.exp.Exist;
import com.example.exp.UserNotFound;
import com.example.repository.GroupRepository;
import com.example.repository.ProfileRepository;
import com.example.util.JwtUtil;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.util.SpringSecurityUtil.currentProfileId;

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
                throw new Exist("With this same Pinfl, PassportNumber user is existed");
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
        createProfile.setPhone(profile.getPhone());
        createProfile.setEmail(profile.getEmail());
        createProfile.setPhotoId(profile.getPhotoId());
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
        responseDTO.setEmail(profile.getEmail());
        responseDTO.setPhone(profile.getPhone());
        responseDTO.setPhotoId(profile.getPhotoId());
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
                throw new Exist("With this Pinfl, PassportNumber and PassportSeries user is existed");
            }
        }
        ProfileEntity createProfile = new ProfileEntity();
        createProfile.setPassword(bCryptPasswordEncoder.encode(profile.getPassword()));
        createProfile.setPinfl(profile.getPinfl());
        createProfile.setPassportNumber(profile.getPassportNumber());
        createProfile.setPassportSeries(profile.getPassportSeries());
        createProfile.setBirthdate(profile.getBirthDate());
        createProfile.setGender(profile.getGender());
        createProfile.setPhone(profile.getPhone());
        createProfile.setEmail(profile.getEmail());
        createProfile.setPhotoId(profile.getPhotoId());
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
        profileDTO.setId(profile.getId());
        profileDTO.setFirstName(profile.getFirstname());
        profileDTO.setLastName(profile.getLastname());
        profileDTO.setPatronymic(profile.getPatronymic());
        profileDTO.setPassportNumber(profile.getPassportNumber());
        profileDTO.setPassportSeries(profile.getPassportSeries());
        profileDTO.setBirthDate(profile.getBirthdate());
        profileDTO.setPinfl(profile.getPinfl());
        profileDTO.setPhone(profile.getPhone());
        profileDTO.setEmail(profile.getEmail());
        profileDTO.setPhotoId(profile.getPhotoId());
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
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setPhoto(entity.getPhotoId());
        dto.setName(entity.getFirstname());
        dto.setSurname(entity.getLastname());
        dto.setPatronymic(entity.getPatronymic());
        dto.setStatus(entity.getStatus());
        dto.setSchoolId(entity.getSchoolId());
        dto.setSchoolName(schoolService.getNameById(entity.getSchoolId()));
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
        if (update == null) {
            throw new AppBadException("You did not update any information!");
        } else if (update.getName() != null) {
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

    public @Nullable String updateByAdmins(String id, ProfileUpdateAdminRequestDTO update) {
        ProfileEntity entity = get(id);
        if (entity.getRoles().contains(ProfileRoleEnum.ROLE_MODERATOR)) {
            throw new AppBadException("You cannot update Moderator information! You are ADMIN");
        }
        if (update == null) {
            throw new AppBadException("You did not update any information!");
        } else if (update.getName() != null) {
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
            profileRoleService.mergeAdmin(entity.getId(), update.getRoleList());

        }
        return "Muvaffaqiyatli yangilandi!";
    }


    public ProfileEntity get(String id) {
        return profileRepository.findByIdAndVisibleIsTrue(id).orElseThrow(() -> {
            throw new AppBadException("Profile not found");
        });
    }

    public Page<ProfileInfoDTO> getProfileList(int page, int size, String schoolId, String role) {
        // 1. Sahifalash obyektini yaratish (qaysi sahifa, nechta ma'lumot va tartiblash)
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        // 2. Dinamik filter (Specification) orqali bazadan qidirish
        Specification<ProfileEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("visible"), true)); // Faqat o'chirilmaganlar

            if (schoolId != null) {
                predicates.add(cb.equal(root.get("schoolId"), schoolId));
            }

            if (role != null) {
                // ProfileRoleEntity bilan Join qilib rolni tekshirish
                Join<ProfileEntity, ProfileRoleEntity> roleJoin = root.join("roles");
                predicates.add(cb.equal(roleJoin.get("roles"), role));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // 3. Bazadan olish va DTO-ga o'tkazish (map qilish)
        return profileRepository.findAll(spec, pageable).map(this::toDTO);
    }

    public @Nullable String deleteByModerator(String id) {
        ProfileEntity entity = get(id);
        entity.setVisible(false);
        profileRepository.save(entity);
        return "Muvaffaqiyatli o'chirildi!";
    }

    public @Nullable String deleteByAdmin(String id) {
        ProfileEntity entity = get(id);
        if (entity.getSchoolId() == null){
            throw new AppBadException("You cannot delete Moderation, You are ADMIN");
        }
        entity.setVisible(false);
        profileRepository.save(entity);

        return "Muvaffaqiyatli o'chirildi!";
    }

    public @Nullable String updatePassword( PasswordUpdateDTO update) {
        ProfileEntity profile = get(currentProfileId());
        if (!bCryptPasswordEncoder.matches(update.getOldPassword(), profile.getPassword())) {
            throw new AppBadException("Wrong password");
        }
        profile.setPassword(bCryptPasswordEncoder.encode(update.getNewPassword()));
        profileRepository.save(profile);
        return "Parol muvaffaqiyatli yangilandi!";
    }


    public Page<ProfileInfoDTO> filter(ProfileFilterDTO filterDTO, int page, int size) {
        // 1. Sahifalash va tartiblash (Yaratilgan sanasi bo'yicha kamayish tartibida)
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        // 2. Dinamik Specification (WHERE shartlarini yig'ish)
        Specification<ProfileEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Faqat o'chirilmaganlarni olish
            predicates.add(cb.equal(root.get("visible"), true));

            // Matnli qidiruv (Ism, Familiya, Username)
            if (filterDTO.getQuery() != null && !filterDTO.getQuery().isBlank()) {
                String searchKeyword = "%" + filterDTO.getQuery().toLowerCase() + "%";
                Predicate firstname = cb.like(cb.lower(root.get("firstname")), searchKeyword);
                Predicate lastname = cb.like(cb.lower(root.get("lastname")), searchKeyword);
                predicates.add(cb.or(firstname, lastname));
            }

            // Maktab bo'yicha filter
            if (filterDTO.getSchoolId() != null) {
                predicates.add(cb.equal(root.get("schoolId"), filterDTO.getSchoolId()));
            }

            // Status bo'yicha filter
            if (filterDTO.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filterDTO.getStatus()));
            }

            // Rol bo'yicha filter (Join orqali)
            if (filterDTO.getRole() != null) {
                Join<ProfileEntity, ProfileRoleEntity> roleJoin = root.join("roles");
                predicates.add(cb.equal(roleJoin.get("roles"), filterDTO.getRole()));
            }

            // Sana oralig'i (From - To)
            if (filterDTO.getCreatedDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate"), filterDTO.getCreatedDateFrom().atStartOfDay()));
            }
            if (filterDTO.getCreatedDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdDate"), filterDTO.getCreatedDateTo().atTime(23, 59, 59)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<ProfileEntity> entityPage = profileRepository.findAll(spec, pageable);

        return entityPage.map(this::toDTO);
    }

    public @Nullable String update(ProfileUpdateDetailRequestDTO dto) {
        ProfileEntity profile = get(currentProfileId());
        if (dto.getEmail() != null) {
            profile.setEmail(dto.getEmail());
        }else if(dto.getPhone() != null) {
            profile.setPhone(dto.getPhone());
        }else if (dto.getPhotoId() != null) {
            profile.setPhotoId(dto.getPhotoId());
        }

        profileRepository.save(profile);

        return "Muvaffaqiyatli o'zgartirildi!";
    }
}
