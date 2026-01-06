package com.example.repository;

import com.example.entity.WorkPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkPlanRepository extends JpaRepository<WorkPlanEntity, String> {
    // O'qituvchining ma'lum bir fan bo'yicha rejalarini tartib bilan olish
    List<WorkPlanEntity> findAllByTeacherIdAndSubjectIdAndVisibleTrueOrderByOrderNumberAsc(String teacherId, Integer subjectId);
    
    // Maktab bo'yicha barcha rejalarni ko'rish
    List<WorkPlanEntity> findAllBySchoolIdAndVisibleTrue(String schoolId);
}