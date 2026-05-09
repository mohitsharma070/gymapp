package com.gymapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Jwt jwt = new Jwt();

    public Jwt getJwt() {
        return jwt;
    }

    @PostConstruct
    public void validate() {
        String secret = jwt.getSecret();
        if (secret == null || secret.isBlank() || "change-this-secret-key".equals(secret)) {
            throw new IllegalStateException(
                    "Security misconfiguration: APP_JWT_SECRET must be set to a secure random value. "
                    + "Do not use the default placeholder.");
        }
    }

    public static class Jwt {
        private String secret = "change-this-secret-key";
        private long expirationMs = 86400000;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public long getExpirationMs() {
            return expirationMs;
        }

        public void setExpirationMs(long expirationMs) {
            this.expirationMs = expirationMs;
        }
    }
}


