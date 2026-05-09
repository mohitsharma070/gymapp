package com.gymapp.admin.controller;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gymapp.admin.dto.AdminDietPlanDto;
import com.gymapp.admin.dto.AdminDietPlanCreateRequest;
import com.gymapp.admin.dto.AdminExerciseDto;
import com.gymapp.admin.dto.AdminMealDto;
import com.gymapp.admin.dto.AdminPaymentDto;
import com.gymapp.admin.dto.AdminRoleUpdateRequest;
import com.gymapp.admin.dto.AdminUserDto;
import com.gymapp.admin.dto.AdminWorkoutPlanDto;
import com.gymapp.admin.service.AdminService;
import com.gymapp.common.constants.ApiPaths;
import com.gymapp.common.constants.ApiMessages;
import com.gymapp.common.constants.PagingConstants;
import com.gymapp.common.controller.BaseController;
import com.gymapp.common.dto.ApiResponse;
import com.gymapp.common.dto.PageRequestDto;
import com.gymapp.common.dto.PageResponseDto;
import com.gymapp.common.util.PageableFactory;
import com.gymapp.common.web.annotation.CurrentUserId;
import com.gymapp.diet.dto.MealRequestDto;
import jakarta.validation.Valid;

@RestController
@RequestMapping(ApiPaths.ADMIN_BASE)
@PreAuthorize("hasRole('ADMIN')")
public class AdminController extends BaseController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public ApiResponse<Map<String, Object>> getDashboardSummary() {
        Map<String, Object> response = adminService.getDashboardSummary();
        return success("Admin dashboard fetched successfully", response);
    }

    @GetMapping("/users")
    public ApiResponse<PageResponseDto<AdminUserDto>> getUsers(PageRequestDto pageRequest) {
        var pageable = PageableFactory.from(
                pageRequest,
                PagingConstants.DEFAULT_PAGE,
                PagingConstants.DEFAULT_SIZE,
                PagingConstants.DEFAULT_SORT,
                PagingConstants.DEFAULT_DIRECTION);
        return success("Users fetched successfully", adminService.getUsers(pageable));
    }

    @PutMapping("/users/{id}/role")
    public ApiResponse<AdminUserDto> updateUserRole(
            @PathVariable Long id,
            @CurrentUserId Long actorUserId,
            @Valid @RequestBody AdminRoleUpdateRequest request) {
        AdminUserDto updated = adminService.updateUserRole(id, request.getRole(), actorUserId);
        return success("User role updated successfully", updated);
    }

    @PostMapping("/users/{userId}/diet-plan/{dietPlanId}")
    public ApiResponse<Void> assignDietPlanToUser(
            @PathVariable Long userId,
            @PathVariable Long dietPlanId) {
        adminService.assignDietPlanToUser(userId, dietPlanId);
        return success("Diet plan assigned to user successfully");
    }

    @GetMapping("/exercises")
    public ApiResponse<PageResponseDto<AdminExerciseDto>> getExercises(PageRequestDto pageRequest) {
        var pageable = PageableFactory.from(
                pageRequest,
                PagingConstants.DEFAULT_PAGE,
                PagingConstants.DEFAULT_SIZE,
                PagingConstants.DEFAULT_SORT,
                PagingConstants.DEFAULT_DIRECTION);
        return success(String.format(ApiMessages.FETCHED_SUCCESSFULLY, "Exercises"), adminService.getExercises(pageable));
    }

    @GetMapping("/workout-plans")
    public ApiResponse<PageResponseDto<AdminWorkoutPlanDto>> getWorkoutPlans(PageRequestDto pageRequest) {
        var pageable = PageableFactory.from(
                pageRequest,
                PagingConstants.DEFAULT_PAGE,
                PagingConstants.DEFAULT_SIZE,
                PagingConstants.DEFAULT_SORT,
                PagingConstants.DEFAULT_DIRECTION);
        return success(String.format(ApiMessages.FETCHED_SUCCESSFULLY, "Workout plans"), adminService.getWorkoutPlans(pageable));
    }

    @GetMapping("/diet-plans")
    public ApiResponse<PageResponseDto<AdminDietPlanDto>> getDietPlans(PageRequestDto pageRequest) {
        var pageable = PageableFactory.from(
                pageRequest,
                PagingConstants.DEFAULT_PAGE,
                PagingConstants.DEFAULT_SIZE,
                PagingConstants.DEFAULT_SORT,
                PagingConstants.DEFAULT_DIRECTION);
        return success(String.format(ApiMessages.FETCHED_SUCCESSFULLY, "Diet plans"), adminService.getDietPlans(pageable));
    }

    @PostMapping("/diet-plans")
    public ApiResponse<AdminDietPlanDto> createDietPlan(@Valid @RequestBody AdminDietPlanCreateRequest request) {
        AdminDietPlanDto created = adminService.createDietPlan(request);
        return success(String.format(ApiMessages.CREATED_SUCCESSFULLY, "Diet plan"), created);
    }

    @PutMapping("/diet-plans/{id}")
    public ApiResponse<AdminDietPlanDto> updateDietPlan(
            @PathVariable Long id,
            @Valid @RequestBody AdminDietPlanCreateRequest request) {
        AdminDietPlanDto updated = adminService.updateDietPlan(id, request);
        return success(String.format(ApiMessages.UPDATED_SUCCESSFULLY, "Diet plan"), updated);
    }

    @DeleteMapping("/diet-plans/{id}")
    public ApiResponse<Void> deleteDietPlan(@PathVariable Long id) {
        adminService.deleteDietPlan(id);
        return success(String.format(ApiMessages.DELETED_SUCCESSFULLY, "Diet plan"));
    }

    @PostMapping("/diet-plans/{dietPlanId}/meals")
    public ApiResponse<AdminMealDto> addMealToDietPlan(
            @PathVariable Long dietPlanId,
            @Valid @RequestBody MealRequestDto request) {
        AdminMealDto created = adminService.addMealToDietPlan(dietPlanId, request);
        return success(String.format(ApiMessages.CREATED_SUCCESSFULLY, "Meal"), created);
    }

    @PutMapping("/meals/{id}")
    public ApiResponse<AdminMealDto> updateMeal(
            @PathVariable Long id,
            @Valid @RequestBody MealRequestDto request) {
        AdminMealDto updated = adminService.updateMeal(id, request);
        return success(String.format(ApiMessages.UPDATED_SUCCESSFULLY, "Meal"), updated);
    }

    @DeleteMapping("/meals/{id}")
    public ApiResponse<Void> deleteMeal(@PathVariable Long id) {
        adminService.deleteMeal(id);
        return success(String.format(ApiMessages.DELETED_SUCCESSFULLY, "Meal"));
    }

    @GetMapping("/payments")
    public ApiResponse<PageResponseDto<AdminPaymentDto>> getPayments(PageRequestDto pageRequest) {
        var pageable = PageableFactory.from(
                pageRequest,
                PagingConstants.DEFAULT_PAGE,
                PagingConstants.DEFAULT_SIZE,
                PagingConstants.DEFAULT_SORT,
                PagingConstants.DEFAULT_DIRECTION);
        return success("Payments fetched successfully", adminService.getPayments(pageable));
    }
}


