package com.gymapp.diet.repository;

import com.gymapp.diet.entity.DietGoalEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DietGoalRepository extends JpaRepository<DietGoalEntity, Long> {

    Optional<DietGoalEntity> findByCode(String code);
}
