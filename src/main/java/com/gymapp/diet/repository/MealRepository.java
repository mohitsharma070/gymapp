package com.gymapp.diet.repository;

import com.gymapp.diet.entity.Meal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {

    List<Meal> findByDietPlanId(Long dietPlanId);
}
