package com.gymapp.workout.service;

import com.gymapp.common.exception.ResourceNotFoundException;
import com.gymapp.workout.dto.WorkoutPlanResponse;
import com.gymapp.workout.entity.Exercise;
import com.gymapp.workout.entity.WorkoutPlan;
import com.gymapp.workout.repository.ExerciseRepository;
import com.gymapp.workout.repository.WorkoutPlanRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final ExerciseRepository exerciseRepository;

    @Transactional(readOnly = true)
    public List<WorkoutPlanResponse> getAllWorkouts() {
        List<WorkoutPlan> plans = workoutPlanRepository.findAll();
        if (plans.isEmpty()) {
            return List.of();
        }

        List<Long> planIds = plans.stream().map(WorkoutPlan::getId).toList();
        List<Exercise> exercises = exerciseRepository.findByWorkoutPlanIdIn(planIds);
        Map<Long, List<Exercise>> exercisesByPlanId = exercises.stream()
                .collect(Collectors.groupingBy(exercise -> exercise.getWorkoutPlan().getId()));

        return plans.stream()
                .map(plan -> toResponse(plan, exercisesByPlanId.getOrDefault(plan.getId(), Collections.emptyList())))
                .toList();
    }

    @Transactional(readOnly = true)
    public WorkoutPlanResponse getWorkoutById(Long id) {
        WorkoutPlan plan = findById(id);
        return toResponse(plan);
    }

    @Transactional
    public WorkoutPlanResponse completeWorkout(Long id) {
        WorkoutPlan plan = findById(id);
        if (plan.isCompleted()) {
            return toResponse(plan);
        }
        plan.setCompleted(true);
        WorkoutPlan saved = workoutPlanRepository.save(plan);
        return toResponse(saved);
    }

    private WorkoutPlan findById(Long id) {
        return workoutPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout plan not found with id: " + id));
    }

    private WorkoutPlanResponse toResponse(WorkoutPlan plan) {
        List<Exercise> exercises = exerciseRepository.findByWorkoutPlanId(plan.getId());
        return toResponse(plan, exercises);
    }

    private WorkoutPlanResponse toResponse(WorkoutPlan plan, List<Exercise> exercises) {
        List<WorkoutPlanResponse.ExerciseItem> items = exercises.stream()
                .map(ex -> new WorkoutPlanResponse.ExerciseItem(ex.getId(), ex.getName(), ex.getSets(), ex.getReps()))
                .toList();

        return new WorkoutPlanResponse(
                plan.getId(),
                plan.getTitle(),
                plan.getGoal(),
                plan.getScheduledDate(),
                plan.isCompleted(),
                items);
    }
}
