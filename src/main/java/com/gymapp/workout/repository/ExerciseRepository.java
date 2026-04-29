package com.gymapp.workout.repository;

import com.gymapp.workout.entity.Exercise;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    List<Exercise> findByWorkoutPlanId(Long workoutPlanId);
}
