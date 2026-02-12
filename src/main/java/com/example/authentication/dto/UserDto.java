package com.example.authentication.dto;

import java.util.List;

public class UserDto {
    private Integer id;
    private String username;
    private String email;
    private String fullName;
    private String employeeId;
    private String phoneNumber;
    private String designation;
    private String region;
    private String costCenter;
    private String businessUnit;
    private String reportingManagerEmail;
    private String department;
    private String profilePicture;
    private String createdAt;
    private String role;
    private List<String> permissionNames;

    public UserDto() {
    }

    public UserDto(Integer id, String username, String email, String fullName, String employeeId, String phoneNumber,
                   String designation, String region, String costCenter, String businessUnit, String reportingManagerEmail,
                   String department, String profilePicture, String createdAt, String role, List<String> permissionNames) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.employeeId = employeeId;
        this.phoneNumber = phoneNumber;
        this.designation = designation;
        this.region = region;
        this.costCenter = costCenter;
        this.businessUnit = businessUnit;
        this.reportingManagerEmail = reportingManagerEmail;
        this.department = department;
        this.profilePicture = profilePicture;
        this.createdAt = createdAt;
        this.role = role;
        this.permissionNames = permissionNames;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getReportingManagerEmail() {
        return reportingManagerEmail;
    }

    public void setReportingManagerEmail(String reportingManagerEmail) {
        this.reportingManagerEmail = reportingManagerEmail;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
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
