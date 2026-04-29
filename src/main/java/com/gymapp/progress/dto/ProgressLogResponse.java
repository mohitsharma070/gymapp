package com.gymapp.progress.dto;

import java.time.LocalDate;

public class ProgressLogResponse {

    private Long id;
    private LocalDate logDate;
    private Double weight;
    private Double bodyFatPercentage;
    private String notes;
    private String photoUrl;

    public ProgressLogResponse(
            Long id,
            LocalDate logDate,
            Double weight,
            Double bodyFatPercentage,
            String notes,
            String photoUrl) {
        this.id = id;
        this.logDate = logDate;
        this.weight = weight;
        this.bodyFatPercentage = bodyFatPercentage;
        this.notes = notes;
        this.photoUrl = photoUrl;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public Double getWeight() {
        return weight;
    }

    public Double getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public String getNotes() {
        return notes;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
