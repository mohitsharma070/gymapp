package com.gymapp.progress.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gymapp.progress.entity.ProgressLog;

public interface ProgressLogRepository extends JpaRepository<ProgressLog, Long> {

    List<ProgressLog> findByUserIdOrderByLogDateDescIdDesc(Long userId);

    List<ProgressLog> findByUserIdAndWorkoutPlanIdIsNotNullOrderByLogDateDescIdDesc(Long userId);

    List<ProgressLog> findByUserIdAndWorkoutPlanIdOrderByLogDateDescIdDesc(Long userId, Long workoutPlanId);
}
