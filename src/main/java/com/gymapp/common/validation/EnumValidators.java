package com.gymapp.common.validation;

import com.gymapp.common.exception.BadRequestException;

public final class EnumValidators {

    private EnumValidators() {
    }

    public static <E extends Enum<E>> E requireEnumIgnoreCase(
            String value,
            Class<E> enumType,
            String requiredMessage,
            String invalidMessagePrefix) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException(requiredMessage);
        }
        try {
            return Enum.valueOf(enumType, value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(invalidMessagePrefix);
        }
    }
}


