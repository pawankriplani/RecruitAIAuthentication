package com.example.authentication.service.impl;

import com.example.authentication.dto.ManagerDTO;
import com.example.authentication.dto.ManagerStatsDTO;
import com.example.authentication.model.User;
import com.example.authentication.repository.UserRepository;
import com.example.authentication.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final UserRepository userRepository;

    @Autowired
    public ManagerServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ManagerStatsDTO getAllManagersWithStats() {
        List<ManagerDTO> managers = getAllManagers();
        long totalManagers = userRepository.countTotalManagers();
        long activeManagers = userRepository.countActiveManagers();
        long pendingManagers = userRepository.countPendingManagers();
        
        return new ManagerStatsDTO(managers, totalManagers, activeManagers, pendingManagers);
    }

    private List<ManagerDTO> getAllManagers() {
        List<User> managers = userRepository.findAllManagers();
        return managers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ManagerDTO convertToDTO(User user) {
        ManagerDTO dto = new ManagerDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setEmployeeId(user.getEmployeeId());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setDesignation(user.getDesignation());
        dto.setRegion(user.getRegion());
        dto.setCostCenter(user.getCostCenter());
        dto.setBusinessUnit(user.getBusinessUnit());
        dto.setReportingManagerEmail(user.getReportingManagerEmail());
        dto.setDepartment(user.getDepartment());
        dto.setProfilePicture(user.getProfilePicture());
        dto.setAccountStatus(user.getAccountStatus());
        return dto;
    }
}
