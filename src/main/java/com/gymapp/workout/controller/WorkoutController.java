package com.gymapp.workout.controller;

import com.gymapp.common.constants.PagingConstants;
import com.gymapp.common.constants.ApiMessages;
import com.gymapp.common.controller.BaseController;
import com.gymapp.common.dto.ApiResponse;
import com.gymapp.common.dto.PageRequestDto;
import com.gymapp.common.dto.PageResponseDto;
import com.gymapp.common.util.PageableFactory;
import com.gymapp.common.web.annotation.CurrentUserId;
import com.gymapp.workout.dto.WorkoutPlanResponseDto;
import com.gymapp.workout.service.WorkoutService;
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
    public ApiResponse<PageResponseDto<WorkoutPlanResponseDto>> getWorkouts(
            PageRequestDto pageRequest,
            @CurrentUserId Long userId) {
        var pageable = PageableFactory.from(
                pageRequest,
                PagingConstants.DEFAULT_PAGE,
                PagingConstants.DEFAULT_SIZE,
                PagingConstants.DEFAULT_SORT,
                PagingConstants.DEFAULT_DIRECTION);
        PageResponseDto<WorkoutPlanResponseDto> response = workoutService.getAllWorkouts(pageable, userId);
        return success(String.format(ApiMessages.FETCHED_SUCCESSFULLY, "Workouts"), response);
    }

    @GetMapping("/{id}")
    public ApiResponse<WorkoutPlanResponseDto> getWorkoutById(
            @PathVariable Long id,
            @CurrentUserId Long userId) {
        WorkoutPlanResponseDto response = workoutService.getWorkoutById(id, userId);
        return success(String.format(ApiMessages.FETCHED_SUCCESSFULLY, "Workout"), response);
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ApiResponse<WorkoutPlanResponseDto> completeWorkout(
            @PathVariable Long id,
            @CurrentUserId Long userId) {
        WorkoutPlanResponseDto response = workoutService.completeWorkout(id, userId);
        return success("Workout marked as complete", response);
    }
}


