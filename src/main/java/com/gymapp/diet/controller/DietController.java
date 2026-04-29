package com.gymapp.diet.controller;

import com.gymapp.common.dto.ApiResponse;
import com.gymapp.diet.dto.DietPlanResponse;
import com.gymapp.diet.service.DietService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DietController {

    private final DietService dietService;

    public DietController(DietService dietService) {
        this.dietService = dietService;
    }

    @GetMapping("/api/diet-plans")
    public ApiResponse<List<DietPlanResponse>> getDietPlans() {
        List<DietPlanResponse> response = dietService.getAllDietPlans();
        return ApiResponse.success("Diet plans fetched successfully", response);
    }

    @GetMapping("/api/meals")
    public ApiResponse<List<DietPlanResponse.MealItem>> getMeals() {
        List<DietPlanResponse.MealItem> response = dietService.getAllMeals();
        return ApiResponse.success("Meals fetched successfully", response);
    }
}
