package com.gymapp.security;

import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${app.jwt.secret:change-this-secret-key}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms:86400000}")
    private long jwtExpirationMs;

    public String extractUsername(String token) {
        return parsePayload(token).getOrDefault("sub", "");
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        long now = System.currentTimeMillis();
        long exp = now + jwtExpirationMs;
        return "dummy-jwt-for-" + userDetails.getUsername() + "-" + now + "-" + exp;
    }

    public long getExpirationTime() {
        return jwtExpirationMs;
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    private Date extractExpiration(String token) {
        Map<String, String> payload = parsePayload(token);
        try {
            long exp = Long.parseLong(payload.getOrDefault("exp", "0"));
            return new Date(exp);
        } catch (NumberFormatException ex) {
            return new Date(0L);
        }
    }

    private Map<String, String> parsePayload(String token) {
        String[] parts = token.split("-");
        if (parts.length >= 5 && "dummy".equals(parts[0]) && "jwt".equals(parts[1]) && "for".equals(parts[2])) {
            String username = parts[3];
            String exp = parts[parts.length - 1];
            return Map.of("sub", username, "exp", exp);
        }
        return Map.of();
    }
}
