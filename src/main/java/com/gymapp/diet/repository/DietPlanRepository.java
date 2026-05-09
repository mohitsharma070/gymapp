package com.gymapp.diet.repository;

import com.gymapp.diet.entity.DietPlanEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DietPlanRepository extends JpaRepository<DietPlanEntity, Long> {

    @Query("SELECT DISTINCT d FROM DietPlanEntity d LEFT JOIN FETCH d.meals LEFT JOIN FETCH d.goal")
    List<DietPlanEntity> findAllWithMeals();

    @Query("SELECT DISTINCT d FROM DietPlanEntity d LEFT JOIN FETCH d.meals LEFT JOIN FETCH d.goal g WHERE g.code = :goalCode")
    List<DietPlanEntity> findByGoalCodeWithMeals(@Param("goalCode") String goalCode);

    @Query("SELECT DISTINCT d FROM DietPlanEntity d LEFT JOIN FETCH d.meals LEFT JOIN FETCH d.goal WHERE d.id = :id")
    Optional<DietPlanEntity> findByIdWithMeals(@Param("id") Long id);

    @Query("SELECT DISTINCT d FROM DietPlanEntity d LEFT JOIN FETCH d.meals LEFT JOIN FETCH d.goal WHERE d.planDate = :planDate ORDER BY d.id DESC")
    List<DietPlanEntity> findByPlanDateWithMealsOrderByIdDesc(@Param("planDate") LocalDate planDate);
}


