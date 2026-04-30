package com.gymapp.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private Long userId;
    private String fullName;
    private String email;
    private String username;
    private String token;

}
