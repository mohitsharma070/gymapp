package com.gymapp.workout.controller;

import com.gymapp.common.dto.ApiResponse;
import com.gymapp.common.controller.BaseController;
import com.gymapp.workout.dto.WorkoutPlanResponse;
import com.gymapp.workout.service.WorkoutService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController extends BaseController {

    private final WorkoutService workoutService;

    @GetMapping
    public ApiResponse<List<WorkoutPlanResponse>> getWorkouts() {
        List<WorkoutPlanResponse> response = workoutService.getAllWorkouts();
        return success("Workouts fetched successfully", response);
    }

    @GetMapping("/{id}")
    public ApiResponse<WorkoutPlanResponse> getWorkoutById(@PathVariable Long id) {
        WorkoutPlanResponse response = workoutService.getWorkoutById(id);
        return success("Workout fetched successfully", response);
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<WorkoutPlanResponse> completeWorkout(@PathVariable Long id) {
        WorkoutPlanResponse response = workoutService.completeWorkout(id);
        return success("Workout marked as complete", response);
    }
}
