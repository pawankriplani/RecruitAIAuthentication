package com.example.authentication.service.impl;

import com.example.authentication.model.Role;
import com.example.authentication.model.User;
import com.example.authentication.model.UserRole;
import com.example.authentication.repository.RoleRepository;
import com.example.authentication.repository.UserRoleRepository;
import com.example.authentication.service.RoleManagementService;
import com.example.authentication.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleManagementServiceImpl implements RoleManagementService {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public RoleManagementServiceImpl(RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Role findRole(String roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException(Constants.ERROR_ROLE_NOT_FOUND));
    }

    @Override
    public void assignRole(User user, Role role) {
        UserRole userRole = new UserRole();
        UserRole.UserRoleId userRoleId = new UserRole.UserRoleId();
        userRoleId.setUserId(user.getUserId());
        userRoleId.setRoleId(role.getRoleId());
        userRole.setId(userRoleId);
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setCreatedBy(user);
        userRole.setUpdatedBy(user);
        userRoleRepository.save(userRole);
    }

    @Override
    public boolean requiresApproval(Role role) {
        return Constants.ROLE_MANAGER.equalsIgnoreCase(role.getRoleName()) || 
               Constants.ROLE_RMG.equalsIgnoreCase(role.getRoleName());
    }
}
