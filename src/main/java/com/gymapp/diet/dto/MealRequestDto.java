package com.gymapp.diet.dto;

import com.gymapp.diet.enums.MealType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealRequestDto {

    @NotBlank(message = "Meal name is required")
    private String name;

    @NotNull(message = "Meal type is required")
    private MealType mealType;

    @NotNull(message = "Calories is required")
    @Min(value = 1, message = "Calories must be at least 1")
    private Integer calories;
}


