package com.gymapp.admin.dto;

import com.gymapp.diet.entity.MealEntity;

public class AdminMealDto {

    private Long id;
    private String name;
    private String mealType;
    private Integer calories;
    private Long dietPlanId;

    public static AdminMealDto from(MealEntity meal) {
        AdminMealDto dto = new AdminMealDto();
        dto.id = meal.getId();
        dto.name = meal.getName();
        dto.mealType = meal.getMealType().name();
        dto.calories = meal.getCalories();
        dto.dietPlanId = meal.getDietPlan().getId();
        return dto;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getMealType() { return mealType; }
    public Integer getCalories() { return calories; }
    public Long getDietPlanId() { return dietPlanId; }
}


