package com.example.repository;

import com.example.entity.ScheduleEntity;
import com.example.enums.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;


public interface ScheduleRepository extends JpaRepository<ScheduleEntity, String> {
    Optional<ScheduleEntity> findByRoomNumberAndStartTimeAndEndTimeAndWeekDay(String roomNumber, LocalTime startTime, LocalTime endTime, WeekDay weekDay);

    Optional<ScheduleEntity> findByTeacherIdAndSubjectIdAndWeekDayAndStartTimeAndEndTime(String teacherId, Integer subjectId, WeekDay weekDay, LocalTime startTime, LocalTime endTime);

    Optional<ScheduleEntity> findByIdAndVisibleTrue(String id);

    Optional<ScheduleEntity> getByGroupId(String id);
}
