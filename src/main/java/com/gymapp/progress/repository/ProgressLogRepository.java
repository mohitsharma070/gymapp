package com.gymapp.progress.repository;

import com.gymapp.progress.entity.ProgressLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressLogRepository extends JpaRepository<ProgressLog, Long> {
}
