package com.gymapp.common.mapper;

public interface BaseMapper<S, T> {
    T toDto(S source);
}


