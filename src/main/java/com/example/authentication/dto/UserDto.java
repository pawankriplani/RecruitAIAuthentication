package com.example.authentication.dto;

import java.time.LocalDateTime;
import java.util.List;

public class UserDto {
    private Integer id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String department;
    private LocalDateTime createdAt;
    private String role;
    private List<String> permissionNames;

    public UserDto() {
    }

    public UserDto(Integer id, String username, String email, String firstName, String lastName, String department, LocalDateTime createdAt, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.createdAt = createdAt;
        this.role = role;
        this.permissionNames = null;
    }

    public UserDto(String id, String username, String email, String role) {
        this.id = Integer.parseInt(id);
        this.username = username;
        this.email = email;
        this.role = role;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getPermissionNames() {
        return permissionNames;
    }

    public void setPermissionNames(List<String> permissionNames) {
        this.permissionNames = permissionNames;
    }
}
