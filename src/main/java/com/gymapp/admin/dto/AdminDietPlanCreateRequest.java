package com.gymapp.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AdminDietPlanCreateRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Goal is required")
    private String goal;

    @NotNull(message = "Plan date is required")
    private LocalDate planDate;

    public String getTitle() {
        return title;
    }

    public String getGoal() {
        return goal;
    }

    public LocalDate getPlanDate() {
        return planDate;
    }
}


