package com.example.service;

import com.example.entity.ProfileRoleEntity;
import com.example.enums.ProfileAdminRoleEnum;
import com.example.enums.ProfileModeratorEnum;
import com.example.enums.ProfileRoleEnum;
import com.example.repository.ProfileRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileRoleService {
    @Autowired
    private ProfileRoleRepository profileRoleRepository;

    public void create(String profileId, ProfileRoleEnum role) {
        ProfileRoleEntity entity = new ProfileRoleEntity();
        entity.setProfileId(profileId);
        entity.setRoles(ProfileRoleEnum.valueOf(role.name()));
        profileRoleRepository.save(entity);
    }
    public void merge(String profileId, List<ProfileRoleEnum> newList) {
        List<ProfileRoleEnum> oldList = profileRoleRepository.getRoleListByProfileId(profileId);
        newList.stream().filter(n -> !oldList.contains(n)).forEach(pe -> create(profileId, pe)); // create
        oldList.stream().filter(old -> !newList.contains(old)).forEach(pe -> profileRoleRepository.deleteByIdAndRoleEnum(profileId, pe));
    }
    public void createAdmin(String profileId, List<ProfileAdminRoleEnum> roleList) {
        for (ProfileAdminRoleEnum roleEnum : roleList) {
            ProfileRoleEntity entity = new ProfileRoleEntity();
            entity.setProfileId(profileId);
            entity.setRoles(ProfileRoleEnum.valueOf(roleEnum.name()));
            profileRoleRepository.save(entity);
        }
    }

    public void createModerator(String profileId, List<ProfileModeratorEnum> roleList) {
        for (ProfileModeratorEnum roleEnum : roleList) {
            ProfileRoleEntity entity = new ProfileRoleEntity();
            entity.setProfileId(profileId);
            entity.setRoles(ProfileRoleEnum.valueOf(roleEnum.name()));
            profileRoleRepository.save(entity);
        }
    }

    public void deleteRoleByProfileId(String id) {
        profileRoleRepository.deleteByProfileId(id);
    }

    public List<ProfileRoleEnum> getByProfileId(String id) {
        return profileRoleRepository.getRoleListByProfileId(id);
    }
}
