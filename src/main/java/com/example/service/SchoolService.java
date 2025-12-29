package com.example.service;

import com.example.dto.school.SchoolCreateDTO;
import com.example.dto.school.SchoolDTO;
import com.example.dto.school.SchoolUpdateDTO;
import com.example.entity.ProfileEntity;
import com.example.entity.SchoolEntity;
import com.example.enums.SchoolStatus;
import com.example.exp.AppBadException;
import com.example.exp.UserExist;
import com.example.repository.SchoolRepository;
import jakarta.persistence.criteria.Predicate;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        schoolEntity.setStatus(SchoolStatus.ACTIVE);
        schoolEntity.setRegionId(dto.getRegionId());
        schoolEntity.setAttachId(dto.getAttachId());
        schoolRepository.save(schoolEntity);

        return toSchoolDTO(schoolEntity);
    }

    private @Nullable SchoolDTO toSchoolDTO(SchoolEntity schoolEntity) {
        SchoolDTO dto = new SchoolDTO();
        dto.setId(schoolEntity.getId());
        dto.setName(schoolEntity.getName());
        dto.setAddress(schoolEntity.getAddress());
        dto.setRegionId(schoolEntity.getRegionId());
        dto.setAttachId(schoolEntity.getAttachId());
        dto.setStatus(schoolEntity.getStatus());
        dto.setCreatedAt(schoolEntity.getCreatedDate());
        dto.setVisible(schoolEntity.getVisible());
        dto.setNumber(schoolEntity.getNumber());
        return dto;
    }

    public String getNameById(String schoolId) {
        String name = schoolRepository.getNameByIdAndVisibleIsTrue(schoolId);
        if (name == null) {
            throw new AppBadException("School with this information is not exist");
        }
        return name;
    }


    public @Nullable String updateSchool(String id, SchoolUpdateDTO dto) {
        SchoolEntity school = getById(id);
        if (dto == null){
            throw new AppBadException("You did not change any information");
        }else if (dto.getName() != null){
            school.setName(dto.getName());
        }else if (dto.getAddress() != null){
            school.setAddress(dto.getAddress());
        }else if (dto.getRegionId() != null){
            school.setRegionId(dto.getRegionId());
        }else if (dto.getAttachId() != null){
            school.setAttachId(dto.getAttachId());
        }else if (dto.getNumber() != null){
            school.setNumber(dto.getNumber());
        }
        schoolRepository.save(school);
        return "Maktab muvaffaqiyatli yangilandi!";

    }

    private SchoolEntity getById(String schoolId) {
        Optional<SchoolEntity> school = schoolRepository.findByIdAndVisibleIsTrue(schoolId);
        if (school.isEmpty()){
            throw new AppBadException("School with this information is not exist");
        }
        return school.get();
    }

    public @Nullable String updateStatus(String id, String status) {
        SchoolEntity school = getById(id);
        school.setStatus(SchoolStatus.valueOf(status));
        schoolRepository.save(school);
        return "Status yangilandi!";
    }

    public @Nullable String deleteById(String id) {
        SchoolEntity school = getById(id);
        school.setVisible(false);
        schoolRepository.save(school);
        return "Maktab o'chirildi!";
    }

    public @Nullable SchoolDTO getSchoolById(String id) {
        SchoolEntity school = getById(id);
        return toSchoolDTO(school);
    }

    public Page<SchoolDTO> pagination(int page, int size, String name, String regionId, String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Specification<SchoolEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("visible"), true));

            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (regionId != null) {
                predicates.add(cb.equal(root.get("regionId"), regionId)); // Entity field nomiga e'tibor bering (region_id emas, regionId)
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // 3. Bazadan olish va SchoolDTO ga o'tkazish
        return schoolRepository.findAll(spec, pageable).map(this::toSchoolDTO);
    }
}
