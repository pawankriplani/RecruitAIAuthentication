package com.example.authentication.service;

import com.example.authentication.dto.RegistrationRequest;
import com.example.authentication.model.User;

public interface UserCreationService {
    User createUser(RegistrationRequest request);
    User saveUser(User user);
}
