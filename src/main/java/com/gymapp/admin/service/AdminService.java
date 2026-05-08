package com.gymapp.admin.service;

import com.gymapp.admin.dto.AdminDietPlanDto;
import com.gymapp.admin.dto.AdminExerciseDto;
import com.gymapp.admin.dto.AdminPaymentDto;
import com.gymapp.admin.dto.AdminUserDto;
import com.gymapp.admin.dto.AdminWorkoutPlanDto;
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
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final DietPlanRepository dietPlanRepository;
    private final PaymentRepository paymentRepository;

    public AdminService(
            UserRepository userRepository,
            ExerciseRepository exerciseRepository,
            WorkoutPlanRepository workoutPlanRepository,
            DietPlanRepository dietPlanRepository,
            PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
        this.workoutPlanRepository = workoutPlanRepository;
        this.dietPlanRepository = dietPlanRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalUsers", userRepository.count());
        summary.put("totalExercises", exerciseRepository.count());
        summary.put("totalWorkoutPlans", workoutPlanRepository.count());
        summary.put("totalDietPlans", dietPlanRepository.count());
        summary.put("totalPayments", paymentRepository.count());
        return summary;
    }

    @Transactional(readOnly = true)
    public List<AdminUserDto> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).getContent().stream()
                .map(AdminUserDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public AdminUserDto updateUserRole(Long userId, Role newRole, Long actorUserId) {
        if (userId.equals(actorUserId)) {
            throw new BadRequestException(ErrorMessages.CANNOT_CHANGE_OWN_ROLE);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND_WITH_ID + userId));

        if (user.getRole() == Role.ADMIN && newRole != Role.ADMIN) {
            long activeAdminCount = userRepository.countByRoleAndIsActiveTrue(Role.ADMIN);
            if (activeAdminCount <= 1) {
                throw new BadRequestException(ErrorMessages.CANNOT_DEMOTE_LAST_ADMIN);
            }
        }

        user.setRole(newRole);
        return AdminUserDto.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<AdminExerciseDto> getExercises(Pageable pageable) {
        return exerciseRepository.findAll(pageable).getContent().stream()
                .map(AdminExerciseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AdminWorkoutPlanDto> getWorkoutPlans(Pageable pageable) {
        return workoutPlanRepository.findAll(pageable).getContent().stream()
                .map(AdminWorkoutPlanDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AdminDietPlanDto> getDietPlans(Pageable pageable) {
        return dietPlanRepository.findAll(pageable).getContent().stream()
                .map(AdminDietPlanDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AdminPaymentDto> getPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable).getContent().stream()
                .map(AdminPaymentDto::from)
                .collect(Collectors.toList());
    }
}
