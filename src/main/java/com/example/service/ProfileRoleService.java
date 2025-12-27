package com.example.service;

import com.example.entity.ProfileRoleEntity;
import com.example.enums.ProfileAdminRoleEnum;
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
        entity.setRoles(role);
        profileRoleRepository.save(entity);
    }
    public void create(String profileId, List<ProfileAdminRoleEnum> roleList) {
        for (ProfileAdminRoleEnum roleEnum : roleList) {
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
