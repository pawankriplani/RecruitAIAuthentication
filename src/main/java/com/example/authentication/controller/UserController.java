package com.example.authentication.controller;

import com.example.authentication.dto.UserDto;
import com.example.authentication.model.User;
import com.example.authentication.service.UserService;
import com.example.authentication.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        logger.info("Received request to fetch all users");
        List<UserDto> users = userService.getAllUsers();
        logger.debug("Retrieved {} users", users.size());
        
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Integer id) {
        logger.info("Received request to fetch user with ID: {}", id);
        User user = userService.getUserById(id);
        UserDto userDto = userService.convertToDto(user);
        logger.debug("Retrieved user: {}", userDto.getUsername());
        
        return ResponseEntity.ok(ApiResponse.success(userDto));
    }
}
