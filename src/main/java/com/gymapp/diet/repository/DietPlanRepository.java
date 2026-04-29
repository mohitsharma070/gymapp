package com.gymapp.diet.repository;

import com.gymapp.diet.entity.DietPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {
}
