package com.gymapp.workout.service;

import com.gymapp.common.exception.ResourceNotFoundException;
import com.gymapp.common.dto.PageResponseDto;
import com.gymapp.common.util.RepositoryHelper;
import com.gymapp.progress.entity.ProgressLog;
import com.gymapp.progress.repository.ProgressLogRepository;
import com.gymapp.workout.dto.WorkoutPlanResponseDto;
import com.gymapp.workout.entity.ExerciseEntity;
import com.gymapp.workout.entity.WorkoutPlanEntity;
import java.time.LocalDate;
import com.gymapp.workout.repository.ExerciseRepository;
import com.gymapp.workout.repository.WorkoutPlanRepository;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final ExerciseRepository exerciseRepository;
    private final ProgressLogRepository progressLogRepository;

    @Transactional(readOnly = true)
    public PageResponseDto<WorkoutPlanResponseDto> getAllWorkouts(Pageable pageable, Long userId) {
        Page<WorkoutPlanEntity> page = workoutPlanRepository.findAll(pageable);
        List<WorkoutPlanEntity> plans = page.getContent();
        if (plans.isEmpty()) {
            return PageResponseDto.from(page.map(plan -> toResponse(plan, Collections.emptyList(), false)));
        }

        List<Long> planIds = plans.stream().map(WorkoutPlanEntity::getId).toList();
        List<ExerciseEntity> exercises = exerciseRepository.findByWorkoutPlanIdIn(planIds);
        Map<Long, List<ExerciseEntity>> exercisesByPlanId = exercises.stream()
                .collect(Collectors.groupingBy(exercise -> exercise.getWorkoutPlan().getId()));
        Set<Long> completedPlanIds = new HashSet<>(
                progressLogRepository.findCompletedWorkoutPlanIdsByUserIdAndWorkoutPlanIdIn(userId, planIds));

        return PageResponseDto.from(page.map(plan -> toResponse(
                plan,
                exercisesByPlanId.getOrDefault(plan.getId(), Collections.emptyList()),
                completedPlanIds.contains(plan.getId()))));
    }

    @Transactional(readOnly = true)
    public WorkoutPlanResponseDto getWorkoutById(Long id, Long userId) {
        WorkoutPlanEntity plan = findById(id);
        boolean completed = progressLogRepository.existsByUserIdAndWorkoutPlanId(userId, id);
        return toResponse(plan, completed);
    }

    @Transactional
    public WorkoutPlanResponseDto completeWorkout(Long id, Long userId) {
        WorkoutPlanEntity plan = findById(id);
        if (!progressLogRepository.existsByUserIdAndWorkoutPlanIdAndLogDate(userId, id, LocalDate.now())) {
            ProgressLog log = new ProgressLog();
            log.setUserId(userId);
            log.setWorkoutPlanId(id);
            log.setLogDate(LocalDate.now());
            log.setNotes("Workout completed");
            progressLogRepository.save(log);
        }
        return toResponse(plan, true);
    }

    private WorkoutPlanEntity findById(Long id) {
        return RepositoryHelper.getOrThrow(
                workoutPlanRepository.findById(id),
                () -> new ResourceNotFoundException("Workout plan not found with id: " + id));
    }

    private WorkoutPlanResponseDto toResponse(WorkoutPlanEntity plan) {
        List<ExerciseEntity> exercises = exerciseRepository.findByWorkoutPlanId(plan.getId());
        return toResponse(plan, exercises, plan.isCompleted());
    }

    private WorkoutPlanResponseDto toResponse(WorkoutPlanEntity plan, boolean completed) {
        List<ExerciseEntity> exercises = exerciseRepository.findByWorkoutPlanId(plan.getId());
        return toResponse(plan, exercises, completed);
    }

    private WorkoutPlanResponseDto toResponse(
            WorkoutPlanEntity plan,
            List<ExerciseEntity> exercises,
            boolean completed) {
        List<WorkoutPlanResponseDto.ExerciseItem> items = exercises.stream()
                .map(ex -> new WorkoutPlanResponseDto.ExerciseItem(ex.getId(), ex.getName(), ex.getSets(), ex.getReps()))
                .toList();

        return new WorkoutPlanResponseDto(
                plan.getId(),
                plan.getTitle(),
                plan.getGoal(),
                plan.getScheduledDate(),
                completed,
                items);
    }
}


