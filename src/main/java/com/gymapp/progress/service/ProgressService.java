package com.gymapp.progress.service;

import com.gymapp.common.exception.ResourceNotFoundException;
import com.gymapp.progress.dto.ProgressLogRequest;
import com.gymapp.progress.dto.ProgressLogResponse;
import com.gymapp.progress.entity.ProgressLog;
import com.gymapp.progress.repository.ProgressLogRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgressService {

    private final ProgressLogRepository progressLogRepository;

    public ProgressService(ProgressLogRepository progressLogRepository) {
        this.progressLogRepository = progressLogRepository;
    }

    @Transactional
    public ProgressLogResponse createProgressLog(ProgressLogRequest request) {
        ProgressLog log = new ProgressLog();
        log.setLogDate(request.getLogDate());
        log.setWeight(request.getWeight());
        log.setBodyFatPercentage(request.getBodyFatPercentage());
        log.setNotes(request.getNotes());

        ProgressLog saved = progressLogRepository.save(log);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ProgressLogResponse> getAllProgressLogs() {
        return progressLogRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public ProgressLogResponse uploadProgressPhoto(Long progressId, String photoUrl) {
        ProgressLog log = progressLogRepository.findById(progressId)
                .orElseThrow(() -> new ResourceNotFoundException("Progress log not found with id: " + progressId));

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
                log.getPhotoUrl());
    }
}
