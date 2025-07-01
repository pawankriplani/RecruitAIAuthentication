package com.example.authentication.service;

import com.example.authentication.model.Role;
import com.example.authentication.model.User;

public interface RoleManagementService {
    Role findRole(String roleName);
    void assignRole(User user, Role role);
    boolean requiresApproval(Role role);
}
