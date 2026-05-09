package com.gymapp.user.repository;

import com.gymapp.common.enums.Role;
import com.gymapp.user.entity.User;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> findAllByOrderByIdDesc();
    long countByRoleAndIsActiveTrue(Role role);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    @Modifying
    @Query("UPDATE User u SET u.assignedDietPlan = null WHERE u.assignedDietPlan.id = :dietPlanId")
    int clearAssignedDietPlanByPlanId(@Param("dietPlanId") Long dietPlanId);
}


