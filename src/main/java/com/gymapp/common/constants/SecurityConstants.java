package com.gymapp.common.constants;

public final class SecurityConstants {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String USER_ID_HEADER = "X-USER-ID";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final long JWT_EXPIRATION_MS = 86_400_000L;
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    private SecurityConstants() {
    }
}
