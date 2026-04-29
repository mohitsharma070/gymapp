package com.gymapp.admin.service;

import com.gymapp.diet.entity.DietPlan;
import com.gymapp.diet.repository.DietPlanRepository;
import com.gymapp.payment.entity.Payment;
import com.gymapp.payment.repository.PaymentRepository;
import com.gymapp.user.entity.User;
import com.gymapp.user.repository.UserRepository;
import com.gymapp.workout.entity.Exercise;
import com.gymapp.workout.entity.WorkoutPlan;
import com.gymapp.workout.repository.ExerciseRepository;
import com.gymapp.workout.repository.WorkoutPlanRepository;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

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

    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalUsers", userRepository.count());
        summary.put("totalExercises", exerciseRepository.count());
        summary.put("totalWorkoutPlans", workoutPlanRepository.count());
        summary.put("totalDietPlans", dietPlanRepository.count());
        summary.put("totalPayments", paymentRepository.count());
        return summary;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<Exercise> getExercises() {
        return exerciseRepository.findAll();
    }

    public List<WorkoutPlan> getWorkoutPlans() {
        return workoutPlanRepository.findAll();
    }

    public List<DietPlan> getDietPlans() {
        return dietPlanRepository.findAll();
    }

    public List<Payment> getPayments() {
        return paymentRepository.findAll();
    }
}
