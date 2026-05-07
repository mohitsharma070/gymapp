package com.gymapp.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ForgotPasswordResponse {

    /**
     * Generic confirmation message. The actual reset token is delivered
     * exclusively via email link and is never returned in the API response.
     */
    private String message;
}
