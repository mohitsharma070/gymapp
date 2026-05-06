package com.gymapp.admin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gymapp.admin.dto.AdminDietPlanDto;
import com.gymapp.admin.dto.AdminExerciseDto;
import com.gymapp.admin.dto.AdminPaymentDto;
import com.gymapp.admin.dto.AdminRoleUpdateRequest;
import com.gymapp.admin.dto.AdminUserDto;
import com.gymapp.admin.dto.AdminWorkoutPlanDto;
import com.gymapp.admin.service.AdminService;
import com.gymapp.common.constants.ApiPaths;
import com.gymapp.common.dto.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(ApiPaths.ADMIN_BASE)
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public ApiResponse<Map<String, Object>> getDashboardSummary() {
        Map<String, Object> response = adminService.getDashboardSummary();
        return ApiResponse.success("Admin dashboard fetched successfully", response);
    }

    @GetMapping("/users")
    public ApiResponse<List<AdminUserDto>> getUsers() {
        return ApiResponse.success("Users fetched successfully", adminService.getUsers());
    }

    @PutMapping("/users/{id}/role")
    public ApiResponse<AdminUserDto> updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody AdminRoleUpdateRequest request) {
        AdminUserDto updated = adminService.updateUserRole(id, request.getRole());
        return ApiResponse.success("User role updated successfully", updated);
    }

    @GetMapping("/exercises")
    public ApiResponse<List<AdminExerciseDto>> getExercises() {
        return ApiResponse.success("Exercises fetched successfully", adminService.getExercises());
    }

    @GetMapping("/workout-plans")
    public ApiResponse<List<AdminWorkoutPlanDto>> getWorkoutPlans() {
        return ApiResponse.success("Workout plans fetched successfully", adminService.getWorkoutPlans());
    }

    @GetMapping("/diet-plans")
    public ApiResponse<List<AdminDietPlanDto>> getDietPlans() {
        return ApiResponse.success("Diet plans fetched successfully", adminService.getDietPlans());
    }

    @GetMapping("/payments")
    public ApiResponse<List<AdminPaymentDto>> getPayments() {
        return ApiResponse.success("Payments fetched successfully", adminService.getPayments());
    }
}
