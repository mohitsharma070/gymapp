package com.gymapp.auth.repository;

import com.gymapp.auth.entity.PasswordResetToken;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByTokenHashAndUsedAtIsNullAndExpiresAtAfter(String tokenHash, LocalDateTime now);

    @Modifying
    @Query("""
            UPDATE PasswordResetToken t
            SET t.usedAt = :usedAt
            WHERE t.user.id = :userId
              AND t.usedAt IS NULL
              AND t.expiresAt > :usedAt
            """)
    int markActiveTokensAsUsed(@Param("userId") Long userId, @Param("usedAt") LocalDateTime usedAt);
}
