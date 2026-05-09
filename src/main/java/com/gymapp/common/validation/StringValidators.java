package com.gymapp.common.validation;

import com.gymapp.common.exception.BadRequestException;

public final class StringValidators {

    private StringValidators() {
    }

    public static String requireNotBlankTrimmed(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException(message);
        }
        return value.trim();
    }
}


