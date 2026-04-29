package com.gymapp.progress.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ProgressLogRequest {

    @NotNull(message = "Log date is required")
    private LocalDate logDate;

    private Double weight;
    private Double bodyFatPercentage;
    private String notes;

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(Double bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
