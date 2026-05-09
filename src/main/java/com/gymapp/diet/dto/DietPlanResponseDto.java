package com.gymapp.diet.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DietPlanResponseDto {

    private Long id;
    private String title;
    private String goal;
    private LocalDate planDate;
    private List<MealItem> meals;

    @Getter
    @AllArgsConstructor
    public static class MealItem {
        private Long id;
        private String name;
        private String mealType;
        private Integer calories;
    }
}


