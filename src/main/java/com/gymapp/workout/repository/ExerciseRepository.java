package com.gymapp.workout.repository;

import com.gymapp.workout.entity.ExerciseEntity;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<ExerciseEntity, Long> {

    List<ExerciseEntity> findByWorkoutPlanId(Long workoutPlanId);

    List<ExerciseEntity> findByWorkoutPlanIdIn(Collection<Long> workoutPlanIds);
}


