package com.gymapp.progress.entity;

import java.time.LocalDate;

import com.gymapp.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "progress_logs")
@Getter
@Setter
public class ProgressLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate logDate;

    @Column
    private Double weight;

    @Column
    private Double bodyFatPercentage;

    @Column(length = 1000)
    private String notes;

    @Column
    private String photoUrl;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "workout_plan_id")
    private Long workoutPlanId;
}


