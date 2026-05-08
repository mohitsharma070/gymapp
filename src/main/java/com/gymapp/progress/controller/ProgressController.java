package com.gymapp.progress.controller;

import com.gymapp.common.dto.ApiResponse;
import com.gymapp.common.controller.BaseController;
import com.gymapp.progress.dto.ProgressLogRequest;
import com.gymapp.progress.dto.ProgressLogResponse;
import com.gymapp.progress.service.ProgressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/progress")
@Validated
@RequiredArgsConstructor
public class ProgressController extends BaseController {

    private final ProgressService progressService;

    @PostMapping
    public ApiResponse<ProgressLogResponse> createProgress(
            @Valid @RequestBody ProgressLogRequest request,
            Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        ProgressLogResponse response = progressService.createProgressLog(request, userId);
        return success("Progress log created successfully", response);
    }

    @GetMapping
    public ApiResponse<List<ProgressLogResponse>> getProgressLogs(Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        List<ProgressLogResponse> response = progressService.getProgressLogsByUser(userId);
        return success("Progress logs fetched successfully", response);
    }

    @GetMapping("/workouts")
    public ApiResponse<List<ProgressLogResponse>> getWorkoutProgressLogs(Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        List<ProgressLogResponse> response = progressService.getWorkoutProgressLogsByUser(userId);
        return success("Workout progress logs fetched successfully", response);
    }

    @GetMapping("/workouts/{workoutId}")
    public ApiResponse<List<ProgressLogResponse>> getWorkoutProgressLogsByWorkoutId(
            @PathVariable Long workoutId,
            Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        List<ProgressLogResponse> response = progressService.getWorkoutProgressLogsByUserAndWorkoutId(userId, workoutId);
        return success("Workout progress logs fetched successfully", response);
    }

    @PostMapping("/photos")
    public ApiResponse<ProgressLogResponse> uploadProgressPhoto(
            @RequestParam Long progressId,
            @RequestParam @NotBlank(message = "Photo URL is required") String photoUrl,
            Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        ProgressLogResponse response = progressService.uploadProgressPhoto(progressId, photoUrl, userId);
        return success("Progress photo uploaded successfully", response);
    }
}
