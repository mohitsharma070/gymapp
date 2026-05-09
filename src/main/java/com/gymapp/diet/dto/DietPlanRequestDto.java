package com.gymapp.diet.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DietPlanRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Goal is required")
    private String goal;

    @NotNull(message = "Plan date is required")
    private LocalDate planDate;

    private List<@Valid MealRequestDto> meals;
}


