package com.gymapp.diet.repository;

import com.gymapp.diet.entity.MealEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<MealEntity, Long> {

    List<MealEntity> findByDietPlanId(Long dietPlanId);
}


