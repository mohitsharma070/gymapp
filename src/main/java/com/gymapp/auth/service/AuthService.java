package com.gymapp.auth.service;

import com.gymapp.auth.dto.AuthResponse;
import com.gymapp.auth.dto.LoginRequest;
import com.gymapp.auth.dto.SignupRequest;
import com.gymapp.common.constants.ErrorMessages;
import com.gymapp.common.exception.BadRequestException;
import com.gymapp.common.enums.Role;
import com.gymapp.security.JwtService;
import com.gymapp.security.SecurityUser;
import com.gymapp.user.entity.User;
import com.gymapp.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(SignupRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BadRequestException(ErrorMessages.EMAIL_ALREADY_EXISTS);
        }

        User user = new User();
        user.setFullName(request.getFullName().trim());
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(new SecurityUser(savedUser));

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                token);
    }

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        return register(request);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(normalizedEmail, request.getPassword()));
        } catch (AuthenticationException ex) {
            throw new BadRequestException(ErrorMessages.INVALID_CREDENTIALS);
        }

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.INVALID_CREDENTIALS));
        String token = jwtService.generateToken(new SecurityUser(user));

        return new AuthResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                token);
    }
}
