package com.gymapp.admin.controller;

import com.gymapp.admin.service.AdminService;
import com.gymapp.common.dto.ApiResponse;
import com.gymapp.diet.entity.DietPlan;
import com.gymapp.payment.entity.Payment;
import com.gymapp.user.entity.User;
import com.gymapp.workout.entity.Exercise;
import com.gymapp.workout.entity.WorkoutPlan;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
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
    public ApiResponse<List<User>> getUsers() {
        return ApiResponse.success("Users fetched successfully", adminService.getUsers());
    }

    @GetMapping("/exercises")
    public ApiResponse<List<Exercise>> getExercises() {
        return ApiResponse.success("Exercises fetched successfully", adminService.getExercises());
    }

    @GetMapping("/workout-plans")
    public ApiResponse<List<WorkoutPlan>> getWorkoutPlans() {
        return ApiResponse.success("Workout plans fetched successfully", adminService.getWorkoutPlans());
    }

    @GetMapping("/diet-plans")
    public ApiResponse<List<DietPlan>> getDietPlans() {
        return ApiResponse.success("Diet plans fetched successfully", adminService.getDietPlans());
    }

    @GetMapping("/payments")
    public ApiResponse<List<Payment>> getPayments() {
        return ApiResponse.success("Payments fetched successfully", adminService.getPayments());
    }
}
