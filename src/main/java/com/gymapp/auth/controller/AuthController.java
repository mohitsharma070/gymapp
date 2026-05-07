package com.gymapp.auth.controller;

import com.gymapp.auth.dto.AuthResponse;
import com.gymapp.auth.dto.ChangePasswordRequest;
import com.gymapp.auth.dto.ForgotPasswordRequest;
import com.gymapp.auth.dto.ForgotPasswordResponse;
import com.gymapp.auth.dto.LoginRequest;
import com.gymapp.auth.dto.RegisterRequest;
import com.gymapp.auth.dto.ResetPasswordRequest;
import com.gymapp.auth.service.AuthService;
import com.gymapp.common.constants.ApiPaths;
import com.gymapp.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.AUTH_BASE)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(ApiPaths.REGISTER)
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ApiResponse.success("User registered successfully", response);
    }

    @PostMapping(ApiPaths.LOGIN)
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ApiResponse.success("Login successful", response);
    }

    @PostMapping(ApiPaths.FORGOT_PASSWORD)
    public ApiResponse<ForgotPasswordResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        ForgotPasswordResponse response = authService.forgotPassword(request);
        return ApiResponse.success("If the account exists, password reset instructions were generated", response);
    }

    @PostMapping(ApiPaths.RESET_PASSWORD)
    public ApiResponse<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ApiResponse.success("Password reset successful", null);
    }

    @PostMapping(ApiPaths.CHANGE_PASSWORD)
    public ApiResponse<Void> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(authentication.getName(), request);
        return ApiResponse.success("Password changed successfully", null);
    }
}
