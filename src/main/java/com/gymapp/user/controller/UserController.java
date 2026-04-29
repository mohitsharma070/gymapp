package com.gymapp.user.controller;

import com.gymapp.common.constants.SecurityConstants;
import com.gymapp.common.dto.ApiResponse;
import com.gymapp.user.dto.UpdateProfileRequest;
import com.gymapp.user.dto.UserProfileResponse;
import com.gymapp.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserProfileResponse> getUserProfile(@PathVariable Long userId) {
        UserProfileResponse response = userService.getUserProfile(userId);
        return ApiResponse.success("User profile fetched successfully", response);
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> getMyProfile(
            @RequestHeader(SecurityConstants.USER_ID_HEADER) Long userId) {
        UserProfileResponse response = userService.getUserProfile(userId);
        return ApiResponse.success("My profile fetched successfully", response);
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserProfileResponse> updateUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateProfileRequest request) {
        UserProfileResponse response = userService.updateUserProfile(userId, request);
        return ApiResponse.success("User profile updated successfully", response);
    }
}
