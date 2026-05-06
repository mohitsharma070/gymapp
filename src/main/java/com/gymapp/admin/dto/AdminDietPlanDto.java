package com.gymapp.admin.dto;

import com.gymapp.diet.entity.DietPlan;
import java.time.LocalDate;

public class AdminDietPlanDto {

    private Long id;
    private String title;
    private String goal;
    private LocalDate planDate;

    public static AdminDietPlanDto from(DietPlan plan) {
        AdminDietPlanDto dto = new AdminDietPlanDto();
        dto.id = plan.getId();
        dto.title = plan.getTitle();
        dto.goal = plan.getGoal();
        dto.planDate = plan.getPlanDate();
        return dto;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getGoal() { return goal; }
    public LocalDate getPlanDate() { return planDate; }
}
