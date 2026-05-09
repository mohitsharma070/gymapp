package com.gymapp.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gymapp.common.controller.BaseController;
import com.gymapp.common.constants.ApiMessages;
import com.gymapp.common.dto.ApiResponse;
import com.gymapp.common.web.annotation.CurrentUserId;
import com.gymapp.diet.dto.DietPlanResponseDto;
import com.gymapp.diet.dto.MealProgressResponseDto;
import com.gymapp.user.dto.UpdateProfileRequest;
import com.gymapp.user.dto.UserProfileResponse;
import com.gymapp.user.service.UserService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserProfileResponse> getUserProfile(
            @PathVariable Long userId,
            @CurrentUserId Long currentUserId) {
        if (!userId.equals(currentUserId)) {
            denyAccess();
        }
        UserProfileResponse response = userService.getUserProfile(userId);
        return success(String.format(ApiMessages.FETCHED_SUCCESSFULLY, "User profile"), response);
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> getMyProfile(@CurrentUserId Long userId) {
        UserProfileResponse response = userService.getUserProfile(userId);
        return success(String.format(ApiMessages.FETCHED_SUCCESSFULLY, "My profile"), response);
    }

    @GetMapping("/me/diet-plan")
    public ApiResponse<DietPlanResponseDto> getMyDietPlan(@CurrentUserId Long userId) {
        DietPlanResponseDto response = userService.getAssignedDietPlan(userId);
        return success(String.format(ApiMessages.FETCHED_SUCCESSFULLY, "My diet plan"), response);
    }

    @GetMapping("/me/meal-progress")
    public ApiResponse<List<MealProgressResponseDto>> getMyMealProgress(@CurrentUserId Long userId) {
        List<MealProgressResponseDto> response = userService.getMealProgressByUser(userId);
        return success(String.format(ApiMessages.FETCHED_SUCCESSFULLY, "My meal progress"), response);
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserProfileResponse> updateUserProfile(
            @PathVariable Long userId,
            @CurrentUserId Long currentUserId,
            @Valid @RequestBody UpdateProfileRequest request) {
        if (!userId.equals(currentUserId)) {
            denyAccess();
        }
        UserProfileResponse response = userService.updateUserProfile(userId, request);
        return success(String.format(ApiMessages.UPDATED_SUCCESSFULLY, "User profile"), response);
    }
}


