package com.gymapp.admin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.gymapp.admin.dto.AdminDietPlanDto;
import com.gymapp.admin.dto.AdminExerciseDto;
import com.gymapp.admin.dto.AdminPaymentDto;
import com.gymapp.admin.dto.AdminRoleUpdateRequest;
import com.gymapp.admin.dto.AdminUserDto;
import com.gymapp.admin.dto.AdminWorkoutPlanDto;
import com.gymapp.admin.service.AdminService;
import com.gymapp.common.constants.ApiPaths;
import com.gymapp.common.controller.BaseController;
import com.gymapp.common.dto.ApiResponse;
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
    public ApiResponse<List<AdminUserDto>> getUsers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction) {
        Pageable pageable = buildPageable(page, size, sortBy, direction, 0, 20, "id", Sort.Direction.DESC);
        return success("Users fetched successfully", adminService.getUsers(pageable));
    }

    @PutMapping("/users/{id}/role")
    public ApiResponse<AdminUserDto> updateUserRole(
            @PathVariable Long id,
            Authentication authentication,
            @Valid @RequestBody AdminRoleUpdateRequest request) {
        Long actorUserId = getCurrentUserId(authentication);
        AdminUserDto updated = adminService.updateUserRole(id, request.getRole(), actorUserId);
        return success("User role updated successfully", updated);
    }

    @GetMapping("/exercises")
    public ApiResponse<List<AdminExerciseDto>> getExercises(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction) {
        Pageable pageable = buildPageable(page, size, sortBy, direction, 0, 20, "id", Sort.Direction.DESC);
        return success("Exercises fetched successfully", adminService.getExercises(pageable));
    }

    @GetMapping("/workout-plans")
    public ApiResponse<List<AdminWorkoutPlanDto>> getWorkoutPlans(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction) {
        Pageable pageable = buildPageable(page, size, sortBy, direction, 0, 20, "id", Sort.Direction.DESC);
        return success("Workout plans fetched successfully", adminService.getWorkoutPlans(pageable));
    }

    @GetMapping("/diet-plans")
    public ApiResponse<List<AdminDietPlanDto>> getDietPlans(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction) {
        Pageable pageable = buildPageable(page, size, sortBy, direction, 0, 20, "id", Sort.Direction.DESC);
        return success("Diet plans fetched successfully", adminService.getDietPlans(pageable));
    }

    @GetMapping("/payments")
    public ApiResponse<List<AdminPaymentDto>> getPayments(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction) {
        Pageable pageable = buildPageable(page, size, sortBy, direction, 0, 20, "id", Sort.Direction.DESC);
        return success("Payments fetched successfully", adminService.getPayments(pageable));
    }
}
