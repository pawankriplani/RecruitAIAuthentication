package com.example.authentication.service;

import com.example.authentication.dto.ManagerDTO;
import com.example.authentication.dto.ManagerStatsDTO;
import java.util.List;

public interface ManagerService {
    ManagerStatsDTO getAllManagersWithStats();
}
