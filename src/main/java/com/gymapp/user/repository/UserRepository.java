package com.gymapp.user.repository;

import com.gymapp.common.enums.Role;
import com.gymapp.user.entity.User;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> findAllByOrderByIdDesc();
    long countByRoleAndIsActiveTrue(Role role);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
