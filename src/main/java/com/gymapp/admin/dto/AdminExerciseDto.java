package com.gymapp.admin.dto;

import com.gymapp.workout.entity.Exercise;

public class AdminExerciseDto {

    private Long id;
    private String name;
    private Integer sets;
    private Integer reps;
    private Long workoutPlanId;

    public static AdminExerciseDto from(Exercise exercise) {
        AdminExerciseDto dto = new AdminExerciseDto();
        dto.id = exercise.getId();
        dto.name = exercise.getName();
        dto.sets = exercise.getSets();
        dto.reps = exercise.getReps();
        dto.workoutPlanId = exercise.getWorkoutPlan() != null ? exercise.getWorkoutPlan().getId() : null;
        return dto;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Integer getSets() { return sets; }
    public Integer getReps() { return reps; }
    public Long getWorkoutPlanId() { return workoutPlanId; }
}
