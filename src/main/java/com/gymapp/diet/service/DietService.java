package com.gymapp.diet.service;

import com.gymapp.common.exception.BadRequestException;
import com.gymapp.common.exception.ResourceNotFoundException;
import com.gymapp.common.dto.PageResponseDto;
import com.gymapp.common.util.RepositoryHelper;
import com.gymapp.diet.constants.DietErrorMessages;
import com.gymapp.diet.dto.DietPlanResponseDto;
import com.gymapp.diet.dto.MealProgressResponseDto;
import com.gymapp.diet.entity.DietPlanEntity;
import com.gymapp.diet.entity.MealEntity;
import com.gymapp.diet.entity.MealProgressEntity;
import com.gymapp.diet.enums.MealProgressStatus;
import com.gymapp.diet.mapper.DietResponseMapper;
import com.gymapp.diet.repository.DietPlanRepository;
import com.gymapp.diet.repository.MealRepository;
import com.gymapp.diet.repository.MealProgressRepository;
import com.gymapp.user.entity.User;
import com.gymapp.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DietService {

    private final DietPlanRepository dietPlanRepository;
    private final MealRepository mealRepository;
    private final MealProgressRepository mealProgressRepository;
    private final UserRepository userRepository;
    private final DietResponseMapper dietResponseMapper;
    private final DietGoalCatalogService dietGoalCatalogService;

    public DietService(
            DietPlanRepository dietPlanRepository,
            MealRepository mealRepository,
            MealProgressRepository mealProgressRepository,
            UserRepository userRepository,
            DietResponseMapper dietResponseMapper,
            DietGoalCatalogService dietGoalCatalogService) {
        this.dietPlanRepository = dietPlanRepository;
        this.mealRepository = mealRepository;
        this.mealProgressRepository = mealProgressRepository;
        this.userRepository = userRepository;
        this.dietResponseMapper = dietResponseMapper;
        this.dietGoalCatalogService = dietGoalCatalogService;
    }

    @Transactional(readOnly = true)
    public List<DietPlanResponseDto> getAllDietPlans(String goal) {
        String normalizedGoal = normalizeGoal(goal);
        List<DietPlanEntity> plans = normalizedGoal == null
                ? dietPlanRepository.findAllWithMeals()
                : dietPlanRepository.findByGoalCodeWithMeals(normalizedGoal);

        return plans.stream()
                .map(dietResponseMapper::toDietPlanResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponseDto<DietPlanResponseDto.MealItem> getAllMeals(Pageable pageable) {
        Page<DietPlanResponseDto.MealItem> page = mealRepository.findAll(pageable).map(dietResponseMapper::toMealItem);
        return PageResponseDto.from(page);
    }

    @Transactional(readOnly = true)
    public DietPlanResponseDto getDietPlanById(Long id) {
        DietPlanEntity plan = RepositoryHelper.getOrThrow(
                dietPlanRepository.findByIdWithMeals(id),
                () -> new ResourceNotFoundException(DietErrorMessages.DIET_PLAN_NOT_FOUND_WITH_ID + id));

        return dietResponseMapper.toDietPlanResponse(plan);
    }

    @Transactional(readOnly = true)
    public DietPlanResponseDto getTodayDietPlan() {
        LocalDate today = LocalDate.now();
        DietPlanEntity plan = dietPlanRepository.findByPlanDateWithMealsOrderByIdDesc(today).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(DietErrorMessages.DIET_PLAN_NOT_FOUND_FOR_DATE + today));

        return dietResponseMapper.toDietPlanResponse(plan);
    }

    @Transactional
    public MealProgressResponseDto completeMeal(Long mealId, Long userId) {
        User user = RepositoryHelper.getOrThrow(
                userRepository.findById(userId),
                () -> new ResourceNotFoundException("User not found with id: " + userId));
        MealEntity meal = RepositoryHelper.getOrThrow(
                mealRepository.findById(mealId),
                () -> new ResourceNotFoundException(DietErrorMessages.MEAL_NOT_FOUND_WITH_ID + mealId));
        if (user.getAssignedDietPlan() == null) {
            throw new BadRequestException(DietErrorMessages.NO_DIET_PLAN_ASSIGNED_FOR_USER_ID + userId);
        }
        if (!meal.getDietPlan().getId().equals(user.getAssignedDietPlan().getId())) {
            throw new BadRequestException(DietErrorMessages.MEAL_NOT_IN_ASSIGNED_PLAN);
        }

        MealProgressEntity progress = mealProgressRepository.findByUserIdAndMealId(userId, mealId)
                .orElseGet(MealProgressEntity::new);

        progress.setUserId(userId);
        progress.setMeal(meal);
        progress.setStatus(MealProgressStatus.COMPLETED);
        progress.setCompletedAt(LocalDateTime.now());

        MealProgressEntity saved = mealProgressRepository.save(progress);
        return dietResponseMapper.toMealProgressResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<MealProgressResponseDto> getMealProgressByUser(Long userId) {
        return mealProgressRepository.findByUserIdOrderByCompletedAtDescIdDesc(userId).stream()
                .map(dietResponseMapper::toMealProgressResponse)
                .toList();
    }

    private String normalizeGoal(String goal) {
        if (goal == null || goal.isBlank()) {
            return null;
        }
        return dietGoalCatalogService.normalizeCode(goal);
    }
}


