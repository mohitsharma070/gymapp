package com.gymapp.diet.controller;

import com.gymapp.common.constants.ApiPaths;
import com.gymapp.common.constants.ApiMessages;
import com.gymapp.common.constants.PagingConstants;
import com.gymapp.common.controller.BaseController;
import com.gymapp.common.dto.ApiResponse;
import com.gymapp.common.dto.PageRequestDto;
import com.gymapp.common.dto.PageResponseDto;
import com.gymapp.common.util.PageableFactory;
import com.gymapp.common.web.annotation.CurrentUserId;
import com.gymapp.diet.dto.DietPlanResponseDto;
import com.gymapp.diet.dto.MealProgressResponseDto;
import com.gymapp.diet.service.DietService;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DietController extends BaseController {

    private final DietService dietService;

    public DietController(DietService dietService) {
        this.dietService = dietService;
    }

    @GetMapping({ApiPaths.DIET_BASE + "/plans", "/api/diet-plans"})
    public ApiResponse<List<DietPlanResponseDto>> getDietPlans(@RequestParam(required = false) String goal) {
        List<DietPlanResponseDto> response = dietService.getAllDietPlans(goal);
        return success(String.format(ApiMessages.FETCHED_SUCCESSFULLY, "Diet plans"), response);
    }

    @GetMapping({ApiPaths.DIET_BASE + "/plans/{id}", "/api/diet-plans/{id}"})
    public ApiResponse<DietPlanResponseDto> getDietPlanById(@PathVariable Long id) {
        DietPlanResponseDto response = dietService.getDietPlanById(id);
        return success(String.format(ApiMessages.FETCHED_SUCCESSFULLY, "Diet plan"), response);
    }

    @GetMapping({ApiPaths.DIET_BASE + "/plans/today", "/api/diet-plans/today"})
    public ApiResponse<DietPlanResponseDto> getTodayDietPlan() {
        DietPlanResponseDto response = dietService.getTodayDietPlan();
        return success("Today's diet plan fetched successfully", response);
    }

    @GetMapping({ApiPaths.DIET_BASE + "/meals", "/api/meals"})
    public ApiResponse<PageResponseDto<DietPlanResponseDto.MealItem>> getMeals(PageRequestDto pageRequest) {
        var pageable = PageableFactory.from(
                pageRequest,
                PagingConstants.DEFAULT_PAGE,
                PagingConstants.DEFAULT_SIZE,
                PagingConstants.DEFAULT_SORT,
                Sort.Direction.ASC);
        PageResponseDto<DietPlanResponseDto.MealItem> response = dietService.getAllMeals(pageable);
        return success(String.format(ApiMessages.FETCHED_SUCCESSFULLY, "Meals"), response);
    }

    @PostMapping({ApiPaths.DIET_BASE + "/meals/{mealId}/complete", "/api/meals/{mealId}/complete"})
    public ApiResponse<MealProgressResponseDto> completeMeal(
            @PathVariable Long mealId,
            @CurrentUserId Long userId) {
        MealProgressResponseDto response = dietService.completeMeal(mealId, userId);
        return success("Meal marked as completed", response);
    }
}


