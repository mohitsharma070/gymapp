package com.gymapp.progress.service;

import com.gymapp.common.exception.ResourceNotFoundException;
import com.gymapp.common.dto.PageResponseDto;
import com.gymapp.common.util.RepositoryHelper;
import com.gymapp.progress.dto.ProgressLogRequest;
import com.gymapp.progress.dto.ProgressLogResponse;
import com.gymapp.progress.entity.ProgressLog;
import com.gymapp.progress.repository.ProgressLogRepository;
import com.gymapp.workout.repository.WorkoutPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ProgressLogRepository progressLogRepository;
    private final WorkoutPlanRepository workoutPlanRepository;

    @Transactional
    public ProgressLogResponse createProgressLog(ProgressLogRequest request, Long userId) {
        Long workoutPlanId = request.getWorkoutPlanId();
        if (workoutPlanId != null) {
            assertWorkoutPlanExists(workoutPlanId);
        }

        ProgressLog log = new ProgressLog();
        log.setUserId(userId);
        log.setLogDate(request.getLogDate());
        log.setWeight(request.getWeight());
        log.setBodyFatPercentage(request.getBodyFatPercentage());
        log.setNotes(request.getNotes());
        log.setWorkoutPlanId(workoutPlanId);

        ProgressLog saved = progressLogRepository.save(log);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<ProgressLogResponse> getProgressLogsByUser(Long userId, Pageable pageable) {
        Page<ProgressLogResponse> page = progressLogRepository.findByUserIdOrderByLogDateDescIdDesc(userId, pageable)
                .map(this::toResponse);
        return PageResponseDto.from(page);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<ProgressLogResponse> getWorkoutProgressLogsByUser(Long userId, Pageable pageable) {
        Page<ProgressLogResponse> page = progressLogRepository
                .findByUserIdAndWorkoutPlanIdIsNotNullOrderByLogDateDescIdDesc(userId, pageable)
                .map(this::toResponse);
        return PageResponseDto.from(page);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<ProgressLogResponse> getWorkoutProgressLogsByUserAndWorkoutId(
            Long userId,
            Long workoutPlanId,
            Pageable pageable) {
        assertWorkoutPlanExists(workoutPlanId);
        Page<ProgressLogResponse> page = progressLogRepository
                .findByUserIdAndWorkoutPlanIdOrderByLogDateDescIdDesc(userId, workoutPlanId, pageable)
                .map(this::toResponse);
        return PageResponseDto.from(page);
    }

    @Transactional
    public ProgressLogResponse uploadProgressPhoto(Long progressId, String photoUrl, Long userId) {
        ProgressLog log = RepositoryHelper.getOrThrow(
                progressLogRepository.findById(progressId),
                () -> new ResourceNotFoundException("Progress log not found with id: " + progressId));

        if (!log.getUserId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to modify another user's progress log");
        }

        log.setPhotoUrl(photoUrl);
        ProgressLog saved = progressLogRepository.save(log);
        return toResponse(saved);
    }

    private ProgressLogResponse toResponse(ProgressLog log) {
        return new ProgressLogResponse(
                log.getId(),
                log.getLogDate(),
                log.getWeight(),
                log.getBodyFatPercentage(),
                log.getNotes(),
                log.getPhotoUrl(),
                log.getWorkoutPlanId());
    }

    private void assertWorkoutPlanExists(Long workoutPlanId) {
        if (!workoutPlanRepository.existsById(workoutPlanId)) {
            throw new ResourceNotFoundException("Workout plan not found with id: " + workoutPlanId);
        }
    }
}


