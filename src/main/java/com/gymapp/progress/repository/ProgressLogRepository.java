package com.gymapp.progress.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gymapp.progress.entity.ProgressLog;

public interface ProgressLogRepository extends JpaRepository<ProgressLog, Long> {

    List<ProgressLog> findByUserId(Long userId);
}
