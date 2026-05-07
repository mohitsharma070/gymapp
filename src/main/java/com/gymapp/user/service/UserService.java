package com.gymapp.user.service;

import com.gymapp.common.constants.ErrorMessages;
import com.gymapp.common.exception.ResourceNotFoundException;
import com.gymapp.user.dto.UpdateProfileRequest;
import com.gymapp.user.dto.UserProfileResponse;
import com.gymapp.user.entity.User;
import com.gymapp.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        User user = findUserById(userId);
        return mapToProfileResponse(user);
    }

    @Transactional
    public UserProfileResponse updateUserProfile(Long userId, UpdateProfileRequest request) {
        User user = findUserById(userId);
        user.setFullName(request.getFullName());
        User updatedUser = userRepository.save(user);
        return mapToProfileResponse(updatedUser);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND_WITH_ID + userId));
    }

    private UserProfileResponse mapToProfileResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole());
    }
}
