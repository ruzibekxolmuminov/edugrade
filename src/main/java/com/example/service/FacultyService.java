package com.example.service;

import com.example.dto.faculty.FacultyCreateDTO;
import com.example.dto.faculty.FacultyDTO;
import com.example.dto.faculty.FacultyFullInfoDTO;
import com.example.dto.faculty.FacultyUpdateDTO;
import com.example.entity.FacultyEntity;
import com.example.exp.AppBadException;
import com.example.exp.Exist;
import com.example.repository.FacultyRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    @Autowired
    private FacultyRepository facultyRepository;

    public @Nullable FacultyCreateDTO create(FacultyDTO facultyDTO) {
        Optional<FacultyEntity> isExist = facultyRepository.findByNameAndSchoolIdAndVisibleTrue(facultyDTO.getName(),facultyDTO.getSchool_id());
        if(isExist.isPresent()) {
            throw new Exist("This faculty already exists in your school!");
        }
        FacultyEntity facultyEntity = new FacultyEntity();
        facultyEntity.setName(facultyDTO.getName());
        facultyEntity.setSchoolId(facultyDTO.getSchool_id());
        facultyEntity.setDescription(facultyDTO.getDescription());
        facultyEntity.setVisible(true);
        facultyRepository.save(facultyEntity);

        return toDTO(facultyEntity);
    }

    private @Nullable FacultyCreateDTO toDTO(FacultyEntity facultyEntity) {
        FacultyCreateDTO facultyCreateDTO = new FacultyCreateDTO();
        facultyCreateDTO.setId(facultyEntity.getId());
        facultyCreateDTO.setName(facultyEntity.getName());
        facultyCreateDTO.setDescription(facultyEntity.getDescription());
        facultyCreateDTO.setVisible(facultyEntity.getVisible());
        facultyCreateDTO.setSchoolId(facultyEntity.getSchoolId());

        return facultyCreateDTO;
    }

    public @Nullable String update(String id, FacultyUpdateDTO facultyDTO) {
        FacultyEntity faculty = getById(id);
        if (facultyDTO == null){
            throw new AppBadException("You did not enter any update information!");
        } else if (facultyDTO.getFacultyName() != null) {
            faculty.setName(facultyDTO.getFacultyName());
        } else if (facultyDTO.getDescription() != null) {
            faculty.setDescription(facultyDTO.getDescription());
        }

        facultyRepository.save(faculty);

        return "Muvaffaqiyatli yangilandi";
    }

    private FacultyEntity getById(String id) {
        Optional<FacultyEntity> faculty = facultyRepository.findByIdAndVisibleTrue(id);
        if (faculty.isEmpty()){
            throw new AppBadException("Faculty not found!");
        }
        return faculty.get();
    }

    public @Nullable String delete(String id) {
        FacultyEntity faculty = getById(id);
        faculty.setVisible(false);
        facultyRepository.save(faculty);
        return "Faculty O'chirildi";
    }
    public List<FacultyFullInfoDTO> getList(String schoolId) {
        List<FacultyEntity> entities = facultyRepository.findAllBySchoolIdAndVisibleTrue(schoolId);

        return entities.stream()
                .map(this::toFullDTO)
                .collect(Collectors.toList());
    }

    private FacultyFullInfoDTO toFullDTO(FacultyEntity entity){
        FacultyFullInfoDTO dto = new FacultyFullInfoDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setSchoolId(entity.getSchoolId());
        dto.setVisible(entity.getVisible());
        return dto;
    }
}
