package com.gymapp.user.service;

import com.gymapp.common.constants.ErrorMessages;
import com.gymapp.common.exception.ResourceNotFoundException;
import com.gymapp.common.util.RepositoryHelper;
import com.gymapp.diet.dto.DietPlanResponseDto;
import com.gymapp.diet.dto.MealProgressResponseDto;
import com.gymapp.diet.entity.DietPlanEntity;
import com.gymapp.diet.mapper.DietResponseMapper;
import com.gymapp.diet.repository.DietPlanRepository;
import com.gymapp.diet.repository.MealProgressRepository;
import com.gymapp.user.dto.UpdateProfileRequest;
import com.gymapp.user.dto.UserProfileResponse;
import com.gymapp.user.entity.User;
import com.gymapp.user.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DietPlanRepository dietPlanRepository;
    private final MealProgressRepository mealProgressRepository;
    private final DietResponseMapper dietResponseMapper;

    public UserService(
            UserRepository userRepository,
            DietPlanRepository dietPlanRepository,
            MealProgressRepository mealProgressRepository,
            DietResponseMapper dietResponseMapper) {
        this.userRepository = userRepository;
        this.dietPlanRepository = dietPlanRepository;
        this.mealProgressRepository = mealProgressRepository;
        this.dietResponseMapper = dietResponseMapper;
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

    @Transactional(readOnly = true)
    public DietPlanResponseDto getAssignedDietPlan(Long userId) {
        User user = findUserById(userId);
        if (user.getAssignedDietPlan() == null) {
            throw new ResourceNotFoundException("No diet plan assigned for user id: " + userId);
        }
        Long dietPlanId = user.getAssignedDietPlan().getId();
        DietPlanEntity plan = RepositoryHelper.getOrThrow(
                dietPlanRepository.findByIdWithMeals(dietPlanId),
                () -> new ResourceNotFoundException("Diet plan not found with id: " + dietPlanId));
        return dietResponseMapper.toDietPlanResponse(plan);
    }

    @Transactional(readOnly = true)
    public List<MealProgressResponseDto> getMealProgressByUser(Long userId) {
        findUserById(userId);
        return mealProgressRepository.findByUserIdOrderByCompletedAtDescIdDesc(userId).stream()
                .map(dietResponseMapper::toMealProgressResponse)
                .toList();
    }

    private User findUserById(Long userId) {
        return RepositoryHelper.getOrThrow(
                userRepository.findById(userId),
                () -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND_WITH_ID + userId));
    }

    private UserProfileResponse mapToProfileResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole());
    }
}


