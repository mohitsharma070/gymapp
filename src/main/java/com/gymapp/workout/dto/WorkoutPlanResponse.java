package com.gymapp.workout.dto;

import java.time.LocalDate;
import java.util.List;

public record WorkoutPlanResponse(
        Long id,
        String title,
        String goal,
        LocalDate scheduledDate,
        boolean completed,
        List<ExerciseItem> exercises) {

    public record ExerciseItem(Long id, String name, Integer sets, Integer reps) {}
}
