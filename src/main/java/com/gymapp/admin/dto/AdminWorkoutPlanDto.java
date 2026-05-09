package com.gymapp.admin.dto;

import com.gymapp.workout.entity.WorkoutPlanEntity;
import java.time.LocalDate;

public class AdminWorkoutPlanDto {

    private Long id;
    private String title;
    private String goal;
    private LocalDate scheduledDate;
    private boolean completed;

    public static AdminWorkoutPlanDto from(WorkoutPlanEntity plan) {
        AdminWorkoutPlanDto dto = new AdminWorkoutPlanDto();
        dto.id = plan.getId();
        dto.title = plan.getTitle();
        dto.goal = plan.getGoal();
        dto.scheduledDate = plan.getScheduledDate();
        dto.completed = plan.isCompleted();
        return dto;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getGoal() { return goal; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public boolean isCompleted() { return completed; }
}


