package com.gymapp.user.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gymapp.common.controller.BaseController;
import com.gymapp.common.dto.ApiResponse;
import com.gymapp.user.dto.UpdateProfileRequest;
import com.gymapp.user.dto.UserProfileResponse;
import com.gymapp.user.service.UserService;

import jakarta.validation.Valid;

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
            Authentication authentication) {
        requireOwnership(userId, authentication, "You are not allowed to view another user's profile");
        UserProfileResponse response = userService.getUserProfile(userId);
        return success("User profile fetched successfully", response);
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> getMyProfile(Authentication authentication) {
        UserProfileResponse response = userService.getUserProfile(getCurrentUserId(authentication));
        return success("My profile fetched successfully", response);
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserProfileResponse> updateUserProfile(
            @PathVariable Long userId,
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {
        requireOwnership(userId, authentication, "You are not allowed to update another user's profile");
        UserProfileResponse response = userService.updateUserProfile(userId, request);
        return success("User profile updated successfully", response);
    }
}
