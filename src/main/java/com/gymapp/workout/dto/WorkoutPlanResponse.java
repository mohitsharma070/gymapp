package com.gymapp.workout.dto;

import java.time.LocalDate;
import java.util.List;

public class WorkoutPlanResponse {

    private Long id;
    private String title;
    private String goal;
    private LocalDate scheduledDate;
    private boolean completed;
    private List<ExerciseItem> exercises;

    public WorkoutPlanResponse(Long id, String title, String goal, LocalDate scheduledDate, boolean completed, List<ExerciseItem> exercises) {
        this.id = id;
        this.title = title;
        this.goal = goal;
        this.scheduledDate = scheduledDate;
        this.completed = completed;
        this.exercises = exercises;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGoal() {
        return goal;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public List<ExerciseItem> getExercises() {
        return exercises;
    }

    public static class ExerciseItem {
        private Long id;
        private String name;
        private Integer sets;
        private Integer reps;

        public ExerciseItem(Long id, String name, Integer sets, Integer reps) {
            this.id = id;
            this.name = name;
            this.sets = sets;
            this.reps = reps;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Integer getSets() {
            return sets;
        }

        public Integer getReps() {
            return reps;
        }
    }
}
