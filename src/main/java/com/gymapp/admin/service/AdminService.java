package com.gymapp.admin.service;

import com.gymapp.admin.dto.AdminDietPlanDto;
import com.gymapp.admin.dto.AdminDietPlanCreateRequest;
import com.gymapp.admin.dto.AdminExerciseDto;
import com.gymapp.admin.dto.AdminMealDto;
import com.gymapp.admin.dto.AdminPaymentDto;
import com.gymapp.admin.dto.AdminUserDto;
import com.gymapp.admin.dto.AdminWorkoutPlanDto;
import com.gymapp.common.constants.ErrorMessages;
import com.gymapp.common.dto.PageResponseDto;
import com.gymapp.common.enums.Role;
import com.gymapp.common.exception.BadRequestException;
import com.gymapp.common.exception.ResourceNotFoundException;
import com.gymapp.common.util.RepositoryHelper;
import com.gymapp.common.validation.StringValidators;
import com.gymapp.diet.constants.DietErrorMessages;
import com.gymapp.diet.entity.DietPlanEntity;
import com.gymapp.diet.entity.MealEntity;
import com.gymapp.diet.dto.MealRequestDto;
import com.gymapp.diet.repository.DietPlanRepository;
import com.gymapp.diet.repository.MealRepository;
import com.gymapp.diet.service.DietGoalCatalogService;
import com.gymapp.payment.repository.PaymentRepository;
import com.gymapp.user.entity.User;
import com.gymapp.user.repository.UserRepository;
import com.gymapp.workout.repository.ExerciseRepository;
import com.gymapp.workout.repository.WorkoutPlanRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final DietPlanRepository dietPlanRepository;
    private final MealRepository mealRepository;
    private final PaymentRepository paymentRepository;
    private final DietGoalCatalogService dietGoalCatalogService;

    public AdminService(
            UserRepository userRepository,
            ExerciseRepository exerciseRepository,
            WorkoutPlanRepository workoutPlanRepository,
            DietPlanRepository dietPlanRepository,
            MealRepository mealRepository,
            PaymentRepository paymentRepository,
            DietGoalCatalogService dietGoalCatalogService) {
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
        this.workoutPlanRepository = workoutPlanRepository;
        this.dietPlanRepository = dietPlanRepository;
        this.mealRepository = mealRepository;
        this.paymentRepository = paymentRepository;
        this.dietGoalCatalogService = dietGoalCatalogService;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalUsers", userRepository.count());
        summary.put("totalExercises", exerciseRepository.count());
        summary.put("totalWorkoutPlans", workoutPlanRepository.count());
        summary.put("totalDietPlans", dietPlanRepository.count());
        summary.put("totalPayments", paymentRepository.count());
        return summary;
    }

    @Transactional(readOnly = true)
    public PageResponseDto<AdminUserDto> getUsers(Pageable pageable) {
        Page<AdminUserDto> page = userRepository.findAll(pageable).map(AdminUserDto::from);
        return PageResponseDto.from(page);
    }

    @Transactional
    public AdminUserDto updateUserRole(Long userId, Role newRole, Long actorUserId) {
        if (userId.equals(actorUserId)) {
            throw new BadRequestException(ErrorMessages.CANNOT_CHANGE_OWN_ROLE);
        }

        User user = RepositoryHelper.getOrThrow(
                userRepository.findById(userId),
                () -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND_WITH_ID + userId));

        if (user.getRole() == Role.ADMIN && newRole != Role.ADMIN) {
            long activeAdminCount = userRepository.countByRoleAndIsActiveTrue(Role.ADMIN);
            if (activeAdminCount <= 1) {
                throw new BadRequestException(ErrorMessages.CANNOT_DEMOTE_LAST_ADMIN);
            }
        }

        user.setRole(newRole);
        return AdminUserDto.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public PageResponseDto<AdminExerciseDto> getExercises(Pageable pageable) {
        Page<AdminExerciseDto> page = exerciseRepository.findAll(pageable).map(AdminExerciseDto::from);
        return PageResponseDto.from(page);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<AdminWorkoutPlanDto> getWorkoutPlans(Pageable pageable) {
        Page<AdminWorkoutPlanDto> page = workoutPlanRepository.findAll(pageable).map(AdminWorkoutPlanDto::from);
        return PageResponseDto.from(page);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<AdminDietPlanDto> getDietPlans(Pageable pageable) {
        Page<AdminDietPlanDto> page = dietPlanRepository.findAll(pageable).map(AdminDietPlanDto::from);
        return PageResponseDto.from(page);
    }

    @Transactional
    public AdminDietPlanDto createDietPlan(AdminDietPlanCreateRequest request) {
        DietPlanEntity plan = new DietPlanEntity();
        plan.setTitle(StringValidators.requireNotBlankTrimmed(request.getTitle(), "Title is required"));
        plan.setGoal(dietGoalCatalogService.resolveOrCreate(request.getGoal()));
        plan.setPlanDate(request.getPlanDate());

        return AdminDietPlanDto.from(dietPlanRepository.save(plan));
    }

    @Transactional
    public AdminMealDto addMealToDietPlan(Long dietPlanId, MealRequestDto request) {
        DietPlanEntity plan = RepositoryHelper.getOrThrow(
                dietPlanRepository.findById(dietPlanId),
                () -> new ResourceNotFoundException(DietErrorMessages.DIET_PLAN_NOT_FOUND_WITH_ID + dietPlanId));

        MealEntity meal = new MealEntity();
        meal.setName(StringValidators.requireNotBlankTrimmed(request.getName(), "Meal name is required"));
        meal.setMealType(request.getMealType());
        meal.setCalories(request.getCalories());
        meal.setDietPlan(plan);

        return AdminMealDto.from(mealRepository.save(meal));
    }

    @Transactional
    public void assignDietPlanToUser(Long userId, Long dietPlanId) {
        User user = RepositoryHelper.getOrThrow(
                userRepository.findById(userId),
                () -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND_WITH_ID + userId));
        DietPlanEntity dietPlan = RepositoryHelper.getOrThrow(
                dietPlanRepository.findById(dietPlanId),
                () -> new ResourceNotFoundException(DietErrorMessages.DIET_PLAN_NOT_FOUND_WITH_ID + dietPlanId));

        user.setAssignedDietPlan(dietPlan);
        userRepository.save(user);
    }

    @Transactional
    public AdminDietPlanDto updateDietPlan(Long id, AdminDietPlanCreateRequest request) {
        DietPlanEntity plan = RepositoryHelper.getOrThrow(
                dietPlanRepository.findById(id),
                () -> new ResourceNotFoundException(DietErrorMessages.DIET_PLAN_NOT_FOUND_WITH_ID + id));

        plan.setTitle(StringValidators.requireNotBlankTrimmed(request.getTitle(), "Title is required"));
        plan.setGoal(dietGoalCatalogService.resolveOrCreate(request.getGoal()));
        plan.setPlanDate(request.getPlanDate());

        return AdminDietPlanDto.from(dietPlanRepository.save(plan));
    }

    @Transactional
    public void deleteDietPlan(Long id) {
        DietPlanEntity plan = RepositoryHelper.getOrThrow(
                dietPlanRepository.findById(id),
                () -> new ResourceNotFoundException(DietErrorMessages.DIET_PLAN_NOT_FOUND_WITH_ID + id));
        userRepository.clearAssignedDietPlanByPlanId(id);
        dietPlanRepository.delete(plan);
    }

    @Transactional
    public AdminMealDto updateMeal(Long id, MealRequestDto request) {
        MealEntity meal = RepositoryHelper.getOrThrow(
                mealRepository.findById(id),
                () -> new ResourceNotFoundException("Meal not found with id: " + id));

        meal.setName(StringValidators.requireNotBlankTrimmed(request.getName(), "Meal name is required"));
        meal.setMealType(request.getMealType());
        meal.setCalories(request.getCalories());

        return AdminMealDto.from(mealRepository.save(meal));
    }

    @Transactional
    public void deleteMeal(Long id) {
        MealEntity meal = RepositoryHelper.getOrThrow(
                mealRepository.findById(id),
                () -> new ResourceNotFoundException("Meal not found with id: " + id));
        mealRepository.delete(meal);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<AdminPaymentDto> getPayments(Pageable pageable) {
        Page<AdminPaymentDto> page = paymentRepository.findAll(pageable).map(AdminPaymentDto::from);
        return PageResponseDto.from(page);
    }
}


