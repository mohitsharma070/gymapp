package com.gymapp.diet.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MealProgressResponseDto {

    private Long mealId;
    private String status;
    private LocalDateTime completedAt;

}


