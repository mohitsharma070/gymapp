package com.gymapp.admin.dto;

import java.time.LocalDateTime;

import com.gymapp.common.enums.Role;
import com.gymapp.user.entity.User;

public class AdminUserDto {

    private Long id;
    private String fullName;
    private String email;
    private String username;
    private Role role;
    private LocalDateTime createdAt;

    public static AdminUserDto from(User user) {
        AdminUserDto dto = new AdminUserDto();
        dto.id = user.getId();
        dto.fullName = user.getFullName();
        dto.email = user.getEmail();
        dto.username = user.getUsername();
        dto.role = user.getRole();
        dto.createdAt = user.getCreatedAt();
        return dto;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public Role getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
