package com.gymapp.workout.repository;

import com.gymapp.workout.entity.WorkoutPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlanEntity, Long> {
}


