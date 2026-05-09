package com.gymapp.common.util;

import java.util.Optional;
import java.util.function.Supplier;

public final class RepositoryHelper {

    private RepositoryHelper() {
    }

    public static <T> T getOrThrow(Optional<T> value, Supplier<? extends RuntimeException> exceptionSupplier) {
        return value.orElseThrow(exceptionSupplier);
    }
}


