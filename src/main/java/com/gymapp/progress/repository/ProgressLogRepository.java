package com.gymapp.progress.repository;

import java.util.List;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gymapp.progress.entity.ProgressLog;

public interface ProgressLogRepository extends JpaRepository<ProgressLog, Long> {

    Page<ProgressLog> findByUserIdOrderByLogDateDescIdDesc(Long userId, Pageable pageable);

    Page<ProgressLog> findByUserIdAndWorkoutPlanIdIsNotNullOrderByLogDateDescIdDesc(Long userId, Pageable pageable);

    Page<ProgressLog> findByUserIdAndWorkoutPlanIdOrderByLogDateDescIdDesc(Long userId, Long workoutPlanId, Pageable pageable);

    boolean existsByUserIdAndWorkoutPlanId(Long userId, Long workoutPlanId);

    boolean existsByUserIdAndWorkoutPlanIdAndLogDate(Long userId, Long workoutPlanId, LocalDate logDate);

    @Query("select distinct p.workoutPlanId from ProgressLog p where p.userId = :userId and p.workoutPlanId in :workoutPlanIds")
    List<Long> findCompletedWorkoutPlanIdsByUserIdAndWorkoutPlanIdIn(
            @Param("userId") Long userId,
            @Param("workoutPlanIds") List<Long> workoutPlanIds);
}


