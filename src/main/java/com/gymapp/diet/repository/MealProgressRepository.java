package com.gymapp.diet.repository;

import com.gymapp.diet.entity.MealProgressEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealProgressRepository extends JpaRepository<MealProgressEntity, Long> {

    Optional<MealProgressEntity> findByUserIdAndMealId(Long userId, Long mealId);

    List<MealProgressEntity> findByUserIdOrderByCompletedAtDescIdDesc(Long userId);
}


