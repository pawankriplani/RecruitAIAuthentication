package com.example.authentication.service.impl;

import com.example.authentication.dto.RegistrationRequest;
import com.example.authentication.model.User;
import com.example.authentication.repository.UserRepository;
import com.example.authentication.service.UserCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserCreationServiceImpl implements UserCreationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserCreationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(RegistrationRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setEmployeeId(request.getEmployeeId());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setDesignation(request.getDesignation());
        user.setRegion(request.getRegion());
        user.setCostCenter(request.getCostCenter());
        user.setBusinessUnit(request.getBusinessUnit());
        user.setReportingManagerEmail(request.getReportingManagerEmail());
        user.setDepartment(request.getDepartment());
        user.setProfilePicture(request.getProfilePicture());
        user.setAccountStatus(User.AccountStatus.PENDING);
        user.setIsActive(true);
        return user;
    }

    @Override
    public User saveUser(User user) {
        user.setCreatedBy(user);
        user.setUpdatedBy(user);
        User savedUser = userRepository.save(user);
        savedUser.setCreatedBy(savedUser);
        savedUser.setUpdatedBy(savedUser);
        return userRepository.save(savedUser);
    }
}
