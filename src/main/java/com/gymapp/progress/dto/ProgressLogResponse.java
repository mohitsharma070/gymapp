package com.gymapp.progress.dto;

import java.time.LocalDate;

public record ProgressLogResponse(
        Long id,
        LocalDate logDate,
        Double weight,
        Double bodyFatPercentage,
        String notes,
        String photoUrl,
        Long workoutPlanId) {}


