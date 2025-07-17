package com.example.authentication.service.impl;

import com.example.authentication.dto.LoginRequest;
import com.example.authentication.dto.LoginResponse;
import com.example.authentication.dto.UnlockAccountRequest;
import com.example.authentication.dto.UserDto;
import com.example.authentication.exception.*;
import com.example.authentication.util.Constants;
import com.example.authentication.model.User;
import com.example.authentication.model.User.AccountStatus;
import com.example.authentication.model.UserRole;
import com.example.authentication.model.AccountApprovalRequest;
import com.example.authentication.repository.UserRepository;
import com.example.authentication.repository.AccountApprovalRequestRepository;
import com.example.authentication.service.AuthenticationService;
import com.example.authentication.util.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthenticationServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

@Override
@Transactional
public LoginResponse login(LoginRequest loginRequest) {
    User user = userRepository.findByEmailWithRolesAndPermissions(loginRequest.getEmail())
            .orElseThrow(() -> new EmailNotFoundException("No account found with this email address"));
    
    checkAccountStatus(user);

    // Check password
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
        handleFailedLogin(user);
    }

    // Reset failed attempts on successful login
    resetFailedAttempts(user);

    // Update last login
    user.setLastLogin(LocalDateTime.now());
    userRepository.save(user);

    // Generate tokens
    String token = jwtUtil.generateToken(user);
    String refreshToken = jwtUtil.generateRefreshToken(user);

    // Create response
    return new LoginResponse(
        token,
        refreshToken,
        createUserDto(user)
    );
}

private void checkAccountStatus(User user) {
//    if (!user.getAccountLocked()) {
//        logger.warn("Login attempt on inactive account: {}", user.getEmail());
//        throw new InactiveAccountException("Your account is currently inactive. Please contact the administrator.");
//    }
    
    switch (user.getAccountStatus()) {
        case LOCKED:
            logger.warn("Login attempt on locked account: {}", user.getEmail());
            throw new InvalidPasswordException("Account has been locked due to too many failed attempts. Please contact administrator.");
        case PENDING:
            logger.warn("Login attempt on pending account: {}", user.getEmail());
            throw new PendingAccountException("Your account is pending approval. Please wait for admin confirmation.");
        case INACTIVE:
            logger.warn("Login attempt on inactive account: {}", user.getEmail());
            throw new InactiveAccountException("Your account is currently inactive. Please contact the administrator.");
        case REJECTED:
            logger.warn("Login attempt on rejected account: {}", user.getEmail());
            throw new RejectedAccountException("Your account registration has been rejected. Please contact the administrator for more information.");
        case ACTIVE:
            // Proceed with login
            break;
    }
}


    @Override
    public LoginResponse refreshToken(String refreshToken) {
        String email = jwtUtil.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        if (jwtUtil.validateToken(refreshToken, user)) {
            String newToken = jwtUtil.generateToken(user);
            String newRefreshToken = jwtUtil.generateRefreshToken(user);

            return new LoginResponse(
                newToken,
                newRefreshToken,
                createUserDto(user)
            );
        }

        throw new BadCredentialsException("Invalid refresh token");
    }

    private UserDetails createUserDetails(User user) {
        List<SimpleGrantedAuthority> authorities = user.getUserRoles().stream()
            .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getRoleName().toUpperCase()))
            .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPasswordHash(),
            authorities
        );
    }

private UserDto createUserDto(User user) {
        System.out.println("Debug - Creating UserDto for user: " + user.getUsername());
        System.out.println("Debug - User roles before mapping: " + user.getUserRoles());
        
        String role = user.getUserRoles().stream()
            .map(userRole -> {
                String roleName = userRole.getRole().getRoleName();
                System.out.println("Debug - Mapping role: " + roleName);
                return roleName;
            })
            .findFirst().orElse("User");
        
        System.out.println("Debug - Final selected role: " + role);
        
        List<String> permissionNames = user.getPermissions().stream()
            .map(permission -> permission.getPermissionName())
            .distinct()
            .collect(Collectors.toList());
        
        System.out.println("Debug - Collected permission names: " + permissionNames);
            
        UserDto userDto = new UserDto(
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getFullName(),
            user.getEmployeeId(),
            user.getDepartment(),
            user.getDesignation(),
            user.getRegion(),
            user.getCreatedAt(),
            role
        );
        
        userDto.setPermissionNames(permissionNames);
        
        System.out.println("Debug - Created UserDto: " + userDto);
        return userDto;
    }

    @Override
    @Transactional
    public void unlockAccount(UnlockAccountRequest unlockAccountRequest) {
        User user = userRepository.findByEmail(unlockAccountRequest.getEmail())
                .orElseThrow(() -> new EmailNotFoundException("No account found with this email address"));

        if (!unlockAccountRequest.getStatus().equals("INACTIVE") && !unlockAccountRequest.getStatus().equals("ACTIVE")) {
            throw new IllegalArgumentException("Invalid status. Must be either INACTIVE or ACTIVE");
        }

        if(unlockAccountRequest.getStatus().equals("ACTIVE")) {
        	user.setFailedLoginAttempts(0);
        	user.setAccountLocked(Boolean.FALSE);
        	user.setLockTime(null);
        }
        user.setAccountStatus(User.AccountStatus.valueOf(unlockAccountRequest.getStatus()));
        userRepository.save(user);
    }

    private void handleFailedLogin(User user) {
        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        int remainingAttempts = Constants.MAX_FAILED_ATTEMPTS - user.getFailedLoginAttempts();
        
        String message;
        if (remainingAttempts > 0) {
            logger.warn("Failed login attempt for user: {}. Remaining attempts: {}", user.getEmail(), remainingAttempts);
            message = String.format(Constants.INVALID_PASSWORD_MESSAGE, remainingAttempts);
        } else {
            logger.warn("Account locked for user: {} due to {} failed attempts", user.getEmail(), Constants.MAX_FAILED_ATTEMPTS);
            message = Constants.ACCOUNT_LOCKED_MESSAGE;
            user.setAccountLocked(true);
            user.setAccountStatus(AccountStatus.LOCKED);
            user.setLockTime(LocalDateTime.now());
        }
        
        userRepository.save(user);
        throw new InvalidPasswordException(message);
    }

    private void resetFailedAttempts(User user) {
        if (user.getFailedLoginAttempts() > 0) {
            user.setFailedLoginAttempts(0);
            user.setAccountLocked(false);
            user.setLockTime(null);
            userRepository.save(user);
        }
    }
}
