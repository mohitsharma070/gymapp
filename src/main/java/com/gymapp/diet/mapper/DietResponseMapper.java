package com.gymapp.diet.mapper;

import com.gymapp.common.mapper.BaseMapper;
import com.gymapp.diet.dto.DietPlanResponseDto;
import com.gymapp.diet.dto.MealProgressResponseDto;
import com.gymapp.diet.entity.DietPlanEntity;
import com.gymapp.diet.entity.MealEntity;
import com.gymapp.diet.entity.MealProgressEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DietResponseMapper implements BaseMapper<DietPlanEntity, DietPlanResponseDto> {

    @Override
    public DietPlanResponseDto toDto(DietPlanEntity plan) {
        return toDietPlanResponse(plan);
    }

    public DietPlanResponseDto toDietPlanResponse(DietPlanEntity plan) {
        return toDietPlanResponse(plan, new ArrayList<>(plan.getMeals()));
    }

    public DietPlanResponseDto toDietPlanResponse(DietPlanEntity plan, List<MealEntity> meals) {
        List<DietPlanResponseDto.MealItem> mealItems = meals.stream()
                .map(this::toMealItem)
                .toList();

        return new DietPlanResponseDto(
                plan.getId(),
                plan.getTitle(),
                plan.getGoal().getDisplayName(),
                plan.getPlanDate(),
                mealItems);
    }

    public DietPlanResponseDto.MealItem toMealItem(MealEntity meal) {
        return new DietPlanResponseDto.MealItem(
                meal.getId(),
                meal.getName(),
                meal.getMealType().name(),
                meal.getCalories());
    }

    public MealProgressResponseDto toMealProgressResponse(MealProgressEntity progress) {
        return new MealProgressResponseDto(
                progress.getMeal().getId(),
                progress.getStatus().name(),
                progress.getCompletedAt());
    }
}


