package com.gymapp.auth.service.impl;

import com.gymapp.auth.dto.AuthResponse;
import com.gymapp.auth.dto.ChangePasswordRequest;
import com.gymapp.auth.dto.ForgotPasswordRequest;
import com.gymapp.auth.dto.ForgotPasswordResponse;
import com.gymapp.auth.dto.LoginRequest;
import com.gymapp.auth.dto.RegisterRequest;
import com.gymapp.auth.dto.ResetPasswordRequest;
import com.gymapp.auth.entity.PasswordResetToken;
import com.gymapp.auth.repository.PasswordResetTokenRepository;
import com.gymapp.auth.service.AuthService;
import com.gymapp.common.constants.ErrorMessages;
import com.gymapp.common.enums.Role;
import com.gymapp.common.exception.BadRequestException;
import com.gymapp.common.service.EmailService;
import com.gymapp.security.JwtService;
import com.gymapp.security.SecurityUser;
import com.gymapp.user.entity.User;
import com.gymapp.user.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private static final long PASSWORD_RESET_EXPIRY_MINUTES = 30;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        String normalizedUsername = request.getUsername().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BadRequestException(ErrorMessages.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByUsername(normalizedUsername)) {
            throw new BadRequestException(ErrorMessages.USERNAME_ALREADY_EXISTS);
        }

        User user = new User();
        user.setFullName(request.getFullName().trim());
        user.setEmail(normalizedEmail);
        user.setUsername(normalizedUsername);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(new SecurityUser(savedUser));

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getUsername(),
                token);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String normalizedUsername = request.getUsername().trim().toLowerCase();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(normalizedUsername, request.getPassword()));
        } catch (AuthenticationException ex) {
            throw new BadRequestException(ErrorMessages.INVALID_CREDENTIALS);
        }

        User user = userRepository.findByUsername(normalizedUsername)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.INVALID_CREDENTIALS));
        String token = jwtService.generateToken(new SecurityUser(user));

        return new AuthResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getUsername(),
                token);
    }

    @Override
    @Transactional
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        Optional<User> optionalUser = userRepository.findByEmail(normalizedEmail);

        if (optionalUser.isEmpty()) {
            // Return the same message whether the email exists or not (prevents email enumeration)
            return new ForgotPasswordResponse(
                    "If an account with that email exists, a password reset link has been sent.");
        }

        User user = optionalUser.get();
        LocalDateTime now = LocalDateTime.now();
        passwordResetTokenRepository.markActiveTokensAsUsed(user.getId(), now);

        String rawToken = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
        String tokenHash = sha256(rawToken);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setTokenHash(tokenHash);
        resetToken.setExpiresAt(now.plusMinutes(PASSWORD_RESET_EXPIRY_MINUTES));
        passwordResetTokenRepository.save(resetToken);

        String resetLink = frontendUrl + "/reset-password?token=" + rawToken;
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);

        return new ForgotPasswordResponse(
                "If an account with that email exists, a password reset link has been sent.");
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        String tokenHash = sha256(request.getToken().trim());
        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByTokenHashAndUsedAtIsNullAndExpiresAtAfter(tokenHash, LocalDateTime.now())
                .orElseThrow(() -> new BadRequestException(ErrorMessages.RESET_TOKEN_INVALID_OR_EXPIRED));

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        resetToken.setUsedAt(LocalDateTime.now());
        passwordResetTokenRepository.save(resetToken);
    }

    @Override
    @Transactional
    public void changePassword(String username, ChangePasswordRequest request) {
        String normalizedUsername = username.trim().toLowerCase();
        User user = userRepository.findByUsername(normalizedUsername)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException(ErrorMessages.CURRENT_PASSWORD_INCORRECT);
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new BadRequestException(ErrorMessages.NEW_PASSWORD_MUST_BE_DIFFERENT);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 algorithm not available", ex);
        }
    }
}


