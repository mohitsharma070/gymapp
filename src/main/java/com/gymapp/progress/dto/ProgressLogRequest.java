package com.gymapp.progress.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgressLogRequest {

    @NotNull(message = "Log date is required")
    private LocalDate logDate;

    @Positive(message = "Weight must be greater than 0")
    private Double weight;

    @DecimalMin(value = "0.0", inclusive = true, message = "Body fat percentage cannot be negative")
    @DecimalMax(value = "100.0", inclusive = true, message = "Body fat percentage cannot exceed 100")
    private Double bodyFatPercentage;

    @Size(max = 1000, message = "Notes can be at most 1000 characters")
    private String notes;
    private Long workoutPlanId;
}


