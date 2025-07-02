package com.example.authentication.service.impl;

import com.example.authentication.dto.LoginRequest;
import com.example.authentication.dto.LoginResponse;
import com.example.authentication.dto.UserDto;
import com.example.authentication.model.User;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

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
        User user = userRepository.findByEmailWithRoles(loginRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        
        System.out.println("Debug - Found user: " + user.getUsername());
        System.out.println("Debug - User roles size: " + (user.getUserRoles() != null ? user.getUserRoles().size() : "null"));
        if (user.getUserRoles() != null) {
            user.getUserRoles().forEach(userRole -> {
                System.out.println("Debug - Role: " + userRole.getRole().getRoleName());
            });
        }

        // Check password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        // Check if user is active and account status is ACTIVE
        if (!user.getIsActive() || user.getAccountStatus() != User.AccountStatus.ACTIVE) {
            throw new BadCredentialsException("Account is not active or pending approval");
        }

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
            .findFirst().get();
        
        System.out.println("Debug - Final selected role: " + role);
        
//        List<String> permissionNames = user.getUserRoles().stream()
//            .flatMap(userRole -> userRole.getRole().getPermissions().stream())
//            .map(permission -> permission.getPermissionName())
//            .distinct()
//            .collect(Collectors.toList());
        List<String> permissionNames =user.getPermissions().stream().map(permission -> permission.getPermissionName())
        .distinct()
        .collect(Collectors.toList());
        
        System.out.println("Debug - Collected permission names: " + permissionNames);
            
        UserDto userDto = new UserDto(
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getDepartment(),
            user.getCreatedAt(),
            role
        );
        
        userDto.setPermissionNames(permissionNames);
        
        System.out.println("Debug - Created UserDto: " + userDto);
        return userDto;
    }
}
