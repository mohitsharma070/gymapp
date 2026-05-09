package com.gymapp.diet.service;

import com.gymapp.common.validation.StringValidators;
import com.gymapp.diet.entity.DietGoalEntity;
import com.gymapp.diet.repository.DietGoalRepository;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DietGoalCatalogService {

    private final DietGoalRepository dietGoalRepository;

    public DietGoalCatalogService(DietGoalRepository dietGoalRepository) {
        this.dietGoalRepository = dietGoalRepository;
    }

    @Transactional(readOnly = true)
    public String normalizeCode(String value) {
        String raw = StringValidators.requireNotBlankTrimmed(value, "Goal is required");
        return raw.toUpperCase(Locale.ROOT).replace(' ', '_');
    }

    @Transactional
    public DietGoalEntity resolveOrCreate(String value) {
        String displayName = StringValidators.requireNotBlankTrimmed(value, "Goal is required");
        String code = normalizeCode(displayName);
        return dietGoalRepository.findByCode(code).orElseGet(() -> {
            DietGoalEntity goal = new DietGoalEntity();
            goal.setCode(code);
            goal.setDisplayName(displayName);
            return dietGoalRepository.save(goal);
        });
    }
}
