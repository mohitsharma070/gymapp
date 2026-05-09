package com.gymapp.diet.enums;

import com.gymapp.common.validation.EnumValidators;
import com.gymapp.diet.constants.DietErrorMessages;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum DietGoal {
    WEIGHT_LOSS,
    MUSCLE_GAIN,
    MAINTENANCE,
    FAT_LOSS;

    public static DietGoal from(String value) {
        return EnumValidators.requireEnumIgnoreCase(
                value,
                DietGoal.class,
                DietErrorMessages.GOAL_REQUIRED,
                DietErrorMessages.INVALID_GOAL_PREFIX + allowedValues());
    }

    public static String allowedValues() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}


