package com.gymapp.diet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.gymapp.common.exception.BadRequestException;
import com.gymapp.diet.entity.DietGoalEntity;
import com.gymapp.diet.entity.DietPlanEntity;
import com.gymapp.diet.entity.MealEntity;
import com.gymapp.diet.enums.MealType;
import com.gymapp.diet.mapper.DietResponseMapper;
import com.gymapp.diet.repository.DietPlanRepository;
import com.gymapp.diet.repository.MealProgressRepository;
import com.gymapp.diet.repository.MealRepository;
import com.gymapp.diet.service.DietGoalCatalogService;
import com.gymapp.user.entity.User;
import com.gymapp.user.repository.UserRepository;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DietServiceTest {

    @Mock
    private DietPlanRepository dietPlanRepository;
    @Mock
    private MealRepository mealRepository;
    @Mock
    private MealProgressRepository mealProgressRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DietGoalCatalogService dietGoalCatalogService;

    private DietService dietService;

    @BeforeEach
    void setUp() {
        dietService = new DietService(
                dietPlanRepository,
                mealRepository,
                mealProgressRepository,
                userRepository,
                new DietResponseMapper(),
                dietGoalCatalogService);
    }

    @Test
    void completeMeal_rejectsWhenUserHasNoAssignedPlan() {
        User user = user(1L, null);
        MealEntity meal = meal(10L, dietPlan(5L, "WEIGHT_LOSS", "Weight Loss"));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mealRepository.findById(10L)).thenReturn(Optional.of(meal));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> dietService.completeMeal(10L, 1L));
        assertEquals("No diet plan assigned for user id: 1", ex.getMessage());
    }

    @Test
    void completeMeal_rejectsWhenMealNotInAssignedPlan() {
        DietPlanEntity assigned = dietPlan(1L, "WEIGHT_LOSS", "Weight Loss");
        User user = user(1L, assigned);
        MealEntity meal = meal(10L, dietPlan(2L, "MUSCLE_GAIN", "Muscle Gain"));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mealRepository.findById(10L)).thenReturn(Optional.of(meal));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> dietService.completeMeal(10L, 1L));
        assertEquals("Meal does not belong to assigned diet plan", ex.getMessage());
    }

    @Test
    void getTodayDietPlan_picksFirstFromOrderedResults() {
        DietPlanEntity latest = dietPlan(9L, "WEIGHT_LOSS", "Weight Loss");
        DietPlanEntity older = dietPlan(5L, "MUSCLE_GAIN", "Muscle Gain");
        when(dietPlanRepository.findByPlanDateWithMealsOrderByIdDesc(LocalDate.now()))
                .thenReturn(List.of(latest, older));

        var response = dietService.getTodayDietPlan();
        assertEquals(9L, response.getId());
    }

    @Test
    void getAllDietPlans_withGoal_normalizesAndQueries() {
        when(dietGoalCatalogService.normalizeCode("random goal")).thenReturn("RANDOM_GOAL");
        when(dietPlanRepository.findByGoalCodeWithMeals("RANDOM_GOAL")).thenReturn(List.of());

        var response = dietService.getAllDietPlans("random goal");
        assertEquals(0, response.size());
        verify(dietPlanRepository).findByGoalCodeWithMeals("RANDOM_GOAL");
    }

    private User user(Long id, DietPlanEntity assignedPlan) {
        User user = new User();
        setField(user, "id", id);
        user.setAssignedDietPlan(assignedPlan);
        return user;
    }

    private DietPlanEntity dietPlan(Long id, String code, String displayName) {
        DietPlanEntity plan = new DietPlanEntity();
        setField(plan, "id", id);
        DietGoalEntity goal = new DietGoalEntity();
        goal.setCode(code);
        goal.setDisplayName(displayName);
        plan.setGoal(goal);
        plan.setTitle("Plan " + id);
        plan.setPlanDate(LocalDate.now());
        return plan;
    }

    private MealEntity meal(Long id, DietPlanEntity plan) {
        MealEntity meal = new MealEntity();
        setField(meal, "id", id);
        meal.setName("Meal " + id);
        meal.setCalories(300);
        meal.setMealType(MealType.BREAKFAST);
        meal.setDietPlan(plan);
        return meal;
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}


