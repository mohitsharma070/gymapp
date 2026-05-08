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
import com.gymapp.diet.repository.DietPlanRepository;
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
    private PaymentRepository paymentRepository;

    private AdminService adminService;

    @BeforeEach
    void setUp() {
        adminService = new AdminService(
                userRepository,
                exerciseRepository,
                workoutPlanRepository,
                dietPlanRepository,
                paymentRepository);
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
}
