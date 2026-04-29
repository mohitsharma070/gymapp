package com.gymapp.auth.controller;

import com.gymapp.auth.dto.AuthResponse;
import com.gymapp.auth.dto.LoginRequest;
import com.gymapp.auth.dto.SignupRequest;
import com.gymapp.auth.service.AuthService;
import com.gymapp.common.constants.ApiPaths;
import com.gymapp.common.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.AUTH_BASE)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(ApiPaths.REGISTER)
    public ApiResponse<AuthResponse> register(@Valid @RequestBody SignupRequest request) {
        AuthResponse response = authService.register(request);
        return ApiResponse.success("User registered successfully", response);
    }

    @PostMapping(ApiPaths.SIGNUP)
    public ApiResponse<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        return register(request);
    }

    @PostMapping(ApiPaths.LOGIN)
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ApiResponse.success("Login successful", response);
    }
}
