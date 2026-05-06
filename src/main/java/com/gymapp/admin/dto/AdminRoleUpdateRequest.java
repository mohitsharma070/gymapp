package com.gymapp.admin.dto;

import com.gymapp.common.enums.Role;
import jakarta.validation.constraints.NotNull;

public class AdminRoleUpdateRequest {

    @NotNull(message = "Role is required")
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
