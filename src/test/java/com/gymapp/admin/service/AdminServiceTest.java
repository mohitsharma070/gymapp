package com.gymapp.admin.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gymapp.common.constants.ErrorMessages;
import com.gymapp.common.enums.Role;
import com.gymapp.common.exception.BadRequestException;
import com.gymapp.common.exception.ResourceNotFoundException;
import com.gymapp.admin.dto.AdminDietPlanCreateRequest;
import com.gymapp.diet.entity.DietGoalEntity;
import com.gymapp.diet.entity.DietPlanEntity;
import com.gymapp.diet.repository.DietPlanRepository;
import com.gymapp.diet.repository.MealRepository;
import com.gymapp.diet.service.DietGoalCatalogService;
import com.gymapp.payment.repository.PaymentRepository;
import com.gymapp.user.entity.User;
import com.gymapp.user.repository.UserRepository;
import com.gymapp.workout.repository.ExerciseRepository;
import com.gymapp.workout.repository.WorkoutPlanRepository;
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private WorkoutPlanRepository workoutPlanRepository;
    @Mock
    private DietPlanRepository dietPlanRepository;
    @Mock
    private MealRepository mealRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private DietGoalCatalogService dietGoalCatalogService;

    private AdminService adminService;

    @BeforeEach
    void setUp() {
        adminService = new AdminService(
                userRepository,
                exerciseRepository,
                workoutPlanRepository,
                dietPlanRepository,
                mealRepository,
                paymentRepository,
                dietGoalCatalogService);
    }

    @Test
    void updateUserRole_success() {
        User target = user(2L, Role.USER, true);
        when(userRepository.findById(2L)).thenReturn(Optional.of(target));
        when(userRepository.save(target)).thenReturn(target);

        var response = adminService.updateUserRole(2L, Role.TRAINER, 1L);

        assertEquals(Role.TRAINER, response.getRole());
        verify(userRepository).save(target);
    }

    @Test
    void updateUserRole_blocksSelfRoleChange() {
        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> adminService.updateUserRole(1L, Role.USER, 1L));

        assertEquals(ErrorMessages.CANNOT_CHANGE_OWN_ROLE, ex.getMessage());
        verify(userRepository, never()).findById(1L);
    }

    @Test
    void updateUserRole_blocksLastAdminDemotion() {
        User target = user(2L, Role.ADMIN, true);
        when(userRepository.findById(2L)).thenReturn(Optional.of(target));
        when(userRepository.countByRoleAndIsActiveTrue(Role.ADMIN)).thenReturn(1L);

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> adminService.updateUserRole(2L, Role.USER, 1L));

        assertEquals(ErrorMessages.CANNOT_DEMOTE_LAST_ADMIN, ex.getMessage());
        verify(userRepository, never()).save(target);
    }

    @Test
    void updateUserRole_userNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> adminService.updateUserRole(99L, Role.USER, 1L));

        assertEquals(ErrorMessages.USER_NOT_FOUND_WITH_ID + 99L, ex.getMessage());
    }

    @Test
    void createDietPlan_resolvesGoalAndSaves() {
        AdminDietPlanCreateRequest request = new AdminDietPlanCreateRequest();
        setField(request, "title", "Plan A");
        setField(request, "goal", "Lean Cut");
        setField(request, "planDate", java.time.LocalDate.now());
        DietGoalEntity goal = goal("LEAN_CUT", "Lean Cut");
        when(dietGoalCatalogService.resolveOrCreate("Lean Cut")).thenReturn(goal);
        when(dietPlanRepository.save(org.mockito.ArgumentMatchers.any(DietPlanEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = adminService.createDietPlan(request);
        assertEquals("Plan A", response.getTitle());
        assertEquals("Lean Cut", response.getGoal());
    }

    @Test
    void updateDietPlan_resolvesGoalAndUpdates() {
        DietPlanEntity plan = new DietPlanEntity();
        setId(plan, 7L);
        when(dietPlanRepository.findById(7L)).thenReturn(Optional.of(plan));

        AdminDietPlanCreateRequest request = new AdminDietPlanCreateRequest();
        setField(request, "title", "Plan B");
        setField(request, "goal", "Body Recomposition");
        setField(request, "planDate", java.time.LocalDate.now());
        DietGoalEntity goal = goal("BODY_RECOMPOSITION", "Body Recomposition");
        when(dietGoalCatalogService.resolveOrCreate("Body Recomposition")).thenReturn(goal);
        when(dietPlanRepository.save(plan)).thenReturn(plan);

        var response = adminService.updateDietPlan(7L, request);
        assertEquals("Plan B", response.getTitle());
        assertEquals("Body Recomposition", response.getGoal());
    }

    private DietGoalEntity goal(String code, String displayName) {
        DietGoalEntity goal = new DietGoalEntity();
        goal.setCode(code);
        goal.setDisplayName(displayName);
        return goal;
    }

    private User user(Long id, Role role, boolean active) {
        User user = new User();
        setId(user, id);
        user.setRole(role);
        user.setActive(active);
        user.setFullName("Test User");
        user.setEmail("test" + id + "@example.com");
        user.setUsername("user" + id);
        user.setPassword("encoded");
        return user;
    }

    private void setId(User user, Long id) {
        try {
            Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(user, id);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private void setId(DietPlanEntity plan, Long id) {
        try {
            Field idField = DietPlanEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(plan, id);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
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


