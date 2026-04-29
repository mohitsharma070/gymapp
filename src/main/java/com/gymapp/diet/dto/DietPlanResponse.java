package com.gymapp.diet.dto;

import java.time.LocalDate;
import java.util.List;

public class DietPlanResponse {

    private Long id;
    private String title;
    private String goal;
    private LocalDate planDate;
    private List<MealItem> meals;

    public DietPlanResponse(Long id, String title, String goal, LocalDate planDate, List<MealItem> meals) {
        this.id = id;
        this.title = title;
        this.goal = goal;
        this.planDate = planDate;
        this.meals = meals;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGoal() {
        return goal;
    }

    public LocalDate getPlanDate() {
        return planDate;
    }

    public List<MealItem> getMeals() {
        return meals;
    }

    public static class MealItem {
        private Long id;
        private String name;
        private String mealType;
        private Integer calories;

        public MealItem(Long id, String name, String mealType, Integer calories) {
            this.id = id;
            this.name = name;
            this.mealType = mealType;
            this.calories = calories;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getMealType() {
            return mealType;
        }

        public Integer getCalories() {
            return calories;
        }
    }
}
