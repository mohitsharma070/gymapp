package com.gymapp.diet.service;

import com.gymapp.diet.dto.DietPlanResponse;
import com.gymapp.diet.entity.Meal;
import com.gymapp.diet.repository.DietPlanRepository;
import com.gymapp.diet.repository.MealRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DietService {

    private final DietPlanRepository dietPlanRepository;
    private final MealRepository mealRepository;

    public DietService(DietPlanRepository dietPlanRepository, MealRepository mealRepository) {
        this.dietPlanRepository = dietPlanRepository;
        this.mealRepository = mealRepository;
    }

    @Transactional(readOnly = true)
    public List<DietPlanResponse> getAllDietPlans() {
        return dietPlanRepository.findAll().stream().map(plan -> {
            List<Meal> meals = mealRepository.findByDietPlanId(plan.getId());
            List<DietPlanResponse.MealItem> mealItems = meals.stream()
                    .map(meal -> new DietPlanResponse.MealItem(
                            meal.getId(),
                            meal.getName(),
                            meal.getMealType(),
                            meal.getCalories()))
                    .toList();

            return new DietPlanResponse(
                    plan.getId(),
                    plan.getTitle(),
                    plan.getGoal(),
                    plan.getPlanDate(),
                    mealItems);
        }).toList();
    }

    @Transactional(readOnly = true)
    public List<DietPlanResponse.MealItem> getAllMeals() {
        return mealRepository.findAll().stream()
                .map(meal -> new DietPlanResponse.MealItem(
                        meal.getId(),
                        meal.getName(),
                        meal.getMealType(),
                        meal.getCalories()))
                .toList();
    }
}
