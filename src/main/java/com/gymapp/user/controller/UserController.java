package com.gymapp.user.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gymapp.common.dto.ApiResponse;
import com.gymapp.security.SecurityUser;
import com.gymapp.user.dto.UpdateProfileRequest;
import com.gymapp.user.dto.UserProfileResponse;
import com.gymapp.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserProfileResponse> getUserProfile(
            @PathVariable Long userId,
            Authentication authentication) {
        SecurityUser principal = (SecurityUser) authentication.getPrincipal();
        if (!principal.getId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to view another user's profile");
        }
        UserProfileResponse response = userService.getUserProfile(userId);
        return ApiResponse.success("User profile fetched successfully", response);
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> getMyProfile(Authentication authentication) {
        SecurityUser principal = (SecurityUser) authentication.getPrincipal();
        UserProfileResponse response = userService.getUserProfile(principal.getId());
        return ApiResponse.success("My profile fetched successfully", response);
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserProfileResponse> updateUserProfile(
            @PathVariable Long userId,
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {
        SecurityUser principal = (SecurityUser) authentication.getPrincipal();
        if (!principal.getId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to update another user's profile");
        }
        UserProfileResponse response = userService.updateUserProfile(userId, request);
        return ApiResponse.success("User profile updated successfully", response);
    }
}
