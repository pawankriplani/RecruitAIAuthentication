package com.example.authentication.service;

import com.example.authentication.dto.RegistrationRequest;
import com.example.authentication.dto.RegistrationResponse;
import com.example.authentication.dto.UserDto;
import com.example.authentication.event.UserRegistrationData;
import com.example.authentication.exception.EmailAlreadyInUseException;
import com.example.authentication.exception.ResourceNotFoundException;
import com.example.authentication.exception.UsernameAlreadyTakenException;
import com.example.authentication.model.*;
import com.example.authentication.repository.*;
import com.example.authentication.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserCreationService userCreationService;
    private final RoleManagementService roleManagementService;
    private final ApprovalRequestService approvalRequestService;
    private final PubSubService pubSubService;
    
    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserCreationService userCreationService,
                       RoleManagementService roleManagementService,
                       ApprovalRequestService approvalRequestService,
                       PubSubService pubSubService) {
        this.userRepository = userRepository;
        this.userCreationService = userCreationService;
        this.roleManagementService = roleManagementService;
        this.approvalRequestService = approvalRequestService;
        this.pubSubService = pubSubService;
    }

    @Transactional
    public List<UserDto> getPendingUsers() {
        logger.debug("Fetching pending users");
        List<User> pendingUsers = userRepository.findPendingUsers();
        logger.debug("Found {} pending users", pendingUsers.size());
        
        try {
            List<UserDto> userDtos = pendingUsers.stream()
                    .map(user -> {
                        logger.debug("Mapping user: id={}, username={}, email={}", 
                            user.getUserId(), user.getUsername(), user.getEmail());
                        String role = user.getUserRoles().stream()
                            .map(userRole -> userRole.getRole().getRoleName())
                            .findFirst()
                            .orElse("User");
                        return new UserDto(
                            user.getUserId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getDepartment(),
                            user.getCreatedAt(),
                            role
                        );
                    })
                    .collect(Collectors.toList());
            logger.debug("Successfully mapped {} users to DTOs", userDtos.size());
            return userDtos;
        } catch (Exception e) {
            logger.error("Error mapping users to DTOs: {}", e.getMessage(), e);
            throw e;
        }
    }

    public User getUserById(Integer userId) {
        logger.debug("Fetching user by ID: {}", userId);
        return userRepository.findById(userId)
            .orElseThrow(() -> {
                logger.error("User not found with ID: {}", userId);
                return new ResourceNotFoundException("User not found with id: " + userId);
            });
    }

    public RegistrationResponse registerUser(RegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyTakenException(Constants.ERROR_USERNAME_TAKEN);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyInUseException(Constants.ERROR_EMAIL_IN_USE);
        }

        User user = userCreationService.createUser(request);
        Role role = roleManagementService.findRole(request.getRoleName());
        
        List<Permission> permissions = permissionRepository.findByPermissionNameIn(request.getPermissionNames());
        user.setPermissions(new HashSet<>(permissions));

        User savedUser = userCreationService.saveUser(user);
        roleManagementService.assignRole(savedUser, role);

        boolean requiresApproval = roleManagementService.requiresApproval(role);

        if (requiresApproval) {
            approvalRequestService.createApprovalRequest(savedUser);
            approvalRequestService.notifyApprovalRequest(savedUser, role);
        }

        // Get RMG email
        String rmgEmail = userRepository.findRmgEmail()
            .orElseThrow(() -> new IllegalStateException("No active RMG user found in the system"));

        // Create and publish UserRegistrationData
        UserRegistrationData registrationData = new UserRegistrationData(
            savedUser.getUserId().toString(),
            savedUser.getUsername(),
            savedUser.getEmail(),
            role.getRoleName(),
            savedUser.getAccountStatus().toString(),
            rmgEmail
        );
        pubSubService.publishUserRegistrationEvent(registrationData);

        return new RegistrationResponse(Constants.SUCCESS_USER_REGISTERED, savedUser.getUserId(), requiresApproval);
    }
}
