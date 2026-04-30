package com.gymapp.auth.service;

import com.gymapp.auth.dto.AuthResponse;
import com.gymapp.auth.dto.ChangePasswordRequest;
import com.gymapp.auth.dto.ForgotPasswordResponse;
import com.gymapp.auth.dto.ForgotPasswordRequest;
import com.gymapp.auth.dto.LoginRequest;
import com.gymapp.auth.dto.RegisterRequest;
import com.gymapp.auth.dto.ResetPasswordRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    void changePassword(String username, ChangePasswordRequest request);
}
