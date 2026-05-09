package com.gymapp.common.dto;

import com.gymapp.common.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {

    private ErrorCode code;
    private String detail;
}


