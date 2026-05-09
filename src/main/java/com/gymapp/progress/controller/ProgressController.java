package com.gymapp.progress.controller;

import com.gymapp.common.constants.PagingConstants;
import com.gymapp.common.constants.ApiMessages;
import com.gymapp.common.controller.BaseController;
import com.gymapp.common.dto.ApiResponse;
import com.gymapp.common.dto.PageRequestDto;
import com.gymapp.common.dto.PageResponseDto;
import com.gymapp.common.util.PageableFactory;
import com.gymapp.common.web.annotation.CurrentUserId;
import com.gymapp.progress.dto.ProgressLogRequest;
import com.gymapp.progress.dto.ProgressLogResponse;
import com.gymapp.progress.service.ProgressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
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
            @CurrentUserId Long userId) {
        ProgressLogResponse response = progressService.createProgressLog(request, userId);
        return success("Progress log created successfully", response);
    }

    @GetMapping
    public ApiResponse<PageResponseDto<ProgressLogResponse>> getProgressLogs(
            @CurrentUserId Long userId,
            PageRequestDto pageRequest) {
        var pageable = PageableFactory.from(
                pageRequest,
                PagingConstants.DEFAULT_PAGE,
                PagingConstants.DEFAULT_SIZE,
                "logDate",
                PagingConstants.DEFAULT_DIRECTION);
        PageResponseDto<ProgressLogResponse> response = progressService.getProgressLogsByUser(userId, pageable);
        return success(String.format(ApiMessages.FETCHED_SUCCESSFULLY, "Progress logs"), response);
    }

    @GetMapping("/workouts")
    public ApiResponse<PageResponseDto<ProgressLogResponse>> getWorkoutProgressLogs(
            @CurrentUserId Long userId,
            PageRequestDto pageRequest) {
        var pageable = PageableFactory.from(
                pageRequest,
                PagingConstants.DEFAULT_PAGE,
                PagingConstants.DEFAULT_SIZE,
                "logDate",
                PagingConstants.DEFAULT_DIRECTION);
        PageResponseDto<ProgressLogResponse> response = progressService.getWorkoutProgressLogsByUser(userId, pageable);
        return success(String.format(ApiMessages.FETCHED_SUCCESSFULLY, "Workout progress logs"), response);
    }

    @GetMapping("/workouts/{workoutId}")
    public ApiResponse<PageResponseDto<ProgressLogResponse>> getWorkoutProgressLogsByWorkoutId(
            @PathVariable Long workoutId,
            @CurrentUserId Long userId,
            PageRequestDto pageRequest) {
        var pageable = PageableFactory.from(
                pageRequest,
                PagingConstants.DEFAULT_PAGE,
                PagingConstants.DEFAULT_SIZE,
                "logDate",
                PagingConstants.DEFAULT_DIRECTION);
        PageResponseDto<ProgressLogResponse> response = progressService
                .getWorkoutProgressLogsByUserAndWorkoutId(userId, workoutId, pageable);
        return success(String.format(ApiMessages.FETCHED_SUCCESSFULLY, "Workout progress logs"), response);
    }

    @PostMapping("/photos")
    public ApiResponse<ProgressLogResponse> uploadProgressPhoto(
            @RequestParam Long progressId,
            @RequestParam
            @NotBlank(message = "Photo URL is required")
            @Pattern(regexp = "^https?://.+", message = "Photo URL must start with http:// or https://")
            String photoUrl,
            @CurrentUserId Long userId) {
        ProgressLogResponse response = progressService.uploadProgressPhoto(progressId, photoUrl, userId);
        return success("Progress photo uploaded successfully", response);
    }
}


