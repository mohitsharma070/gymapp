package com.gymapp.progress.controller;

import com.gymapp.common.dto.ApiResponse;
import com.gymapp.progress.dto.ProgressLogRequest;
import com.gymapp.progress.dto.ProgressLogResponse;
import com.gymapp.progress.service.ProgressService;
import com.gymapp.security.SecurityUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/progress")
@Validated
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @PostMapping
    public ApiResponse<ProgressLogResponse> createProgress(
            @Valid @RequestBody ProgressLogRequest request,
            Authentication authentication) {
        Long userId = ((SecurityUser) authentication.getPrincipal()).getId();
        ProgressLogResponse response = progressService.createProgressLog(request, userId);
        return ApiResponse.success("Progress log created successfully", response);
    }

    @GetMapping
    public ApiResponse<List<ProgressLogResponse>> getProgressLogs(Authentication authentication) {
        Long userId = ((SecurityUser) authentication.getPrincipal()).getId();
        List<ProgressLogResponse> response = progressService.getProgressLogsByUser(userId);
        return ApiResponse.success("Progress logs fetched successfully", response);
    }

    @PostMapping("/photos")
    public ApiResponse<ProgressLogResponse> uploadProgressPhoto(
            @RequestParam Long progressId,
            @RequestParam @NotBlank(message = "Photo URL is required") String photoUrl,
            Authentication authentication) {
        Long userId = ((SecurityUser) authentication.getPrincipal()).getId();
        ProgressLogResponse response = progressService.uploadProgressPhoto(progressId, photoUrl, userId);
        return ApiResponse.success("Progress photo uploaded successfully", response);
    }
}
