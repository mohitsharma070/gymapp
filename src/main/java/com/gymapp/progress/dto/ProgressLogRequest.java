package com.gymapp.progress.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgressLogRequest {

    @NotNull(message = "Log date is required")
    private LocalDate logDate;

    private Double weight;
    private Double bodyFatPercentage;
    private String notes;
    private Long workoutPlanId;
}
