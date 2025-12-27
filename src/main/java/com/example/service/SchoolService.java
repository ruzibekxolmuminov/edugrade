package com.example.service;

import com.example.dto.SchoolCreateDTO;
import com.example.dto.SchoolDTO;
import com.example.entity.SchoolEntity;
import com.example.exp.UserExist;
import com.example.repository.SchoolRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SchoolService {
    @Autowired
    private SchoolRepository schoolRepository;


    public @Nullable SchoolDTO createSchool(SchoolCreateDTO dto) {
        Optional<SchoolEntity> school = schoolRepository.findByNameAndNumberAndAddress(dto.getName(), dto.getNumber(), dto.getAddress());
        if (school.isPresent()) {
            throw new UserExist("School with this information is exist");
        }

        SchoolEntity schoolEntity = new SchoolEntity();
        schoolEntity.setAddress(dto.getAddress());
        schoolEntity.setNumber(dto.getNumber());
        schoolEntity.setCreatedDate(LocalDateTime.now());
        schoolEntity.setName(dto.getName());
        schoolEntity.setVisible(true);
        schoolEntity.setRegionId(dto.getRegionId());
        schoolEntity.setAttachId(dto.getAttachId());
        schoolRepository.save(schoolEntity);

        return toDTO(schoolEntity);
    }

    private @Nullable SchoolDTO toDTO(SchoolEntity schoolEntity) {
        SchoolDTO dto = new SchoolDTO();
        dto.setId(schoolEntity.getId());
        dto.setName(schoolEntity.getName());
        dto.setAddress(schoolEntity.getAddress());
        dto.setRegionId(schoolEntity.getRegionId());
        dto.setAttachId(schoolEntity.getAttachId());
        dto.setCreatedAt(schoolEntity.getCreatedDate());
        dto.setVisible(schoolEntity.getVisible());
        dto.setNumber(schoolEntity.getNumber());
        return dto;
    }
}
