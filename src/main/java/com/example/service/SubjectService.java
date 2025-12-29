package com.example.service;

import com.example.dto.SubjectDTO;
import com.example.dto.SubjectFullInfoDTO;
import com.example.entity.GroupEntity;
import com.example.entity.SubjectEntity;
import com.example.exp.AppBadException;
import com.example.exp.Exist;
import com.example.repository.SubjectRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;

    public @Nullable SubjectFullInfoDTO create(SubjectDTO subjectDTO) {
        Optional<SubjectEntity> isExist = subjectRepository.findByName(subjectDTO.getName());
        if (isExist.isPresent()) {
            throw new Exist("Subject with name " + subjectDTO.getName() + " already exists");
        }

        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setName(subjectDTO.getName());
        subjectEntity.setDescription(subjectDTO.getDescription());
        subjectEntity.setCreatedDate(LocalDateTime.now());
        subjectEntity.setCategory(subjectDTO.getCategory());
        subjectRepository.save(subjectEntity);
        return toDTO(subjectEntity);
    }

    private @Nullable SubjectFullInfoDTO toDTO(SubjectEntity subjectEntity) {
        SubjectFullInfoDTO dto = new SubjectFullInfoDTO();
        dto.setId(subjectEntity.getId());
        dto.setName(subjectEntity.getName());
        dto.setDescription(subjectEntity.getDescription());
        dto.setCategory(subjectEntity.getCategory());
        dto.setCreatedDate(subjectEntity.getCreatedDate());

        return dto;
    }


    public @Nullable String update(Integer id, SubjectDTO subjectDTO) {
        SubjectEntity subjectEntity = getById(id);
        if (subjectDTO.getName() != null) {
            subjectEntity.setName(subjectDTO.getName());
        }else if(subjectDTO.getDescription() != null) {
            subjectEntity.setDescription(subjectDTO.getDescription());
        }else if(subjectDTO.getCategory() != null) {
            subjectEntity.setCategory(subjectDTO.getCategory());
        }
        subjectRepository.save(subjectEntity);
        return "Fan o'zgartirildi!";
    }

    private SubjectEntity getById(Integer id) {
        Optional<SubjectEntity> isExist = subjectRepository.findById(id);
        if (isExist.isEmpty()) {
            throw new AppBadException("Subject with id " + id + " not found");
        }
        return isExist.get();
    }

    public @Nullable String delete(Integer id) {
        subjectRepository.deleteById(id);
        return "Fan o'chirildi!";
    }

    public @Nullable List<SubjectFullInfoDTO> getAll() {
        List<SubjectEntity> entities = subjectRepository.findAll();

        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
