package com.example.authentication.controller;

import com.example.authentication.dto.ManagerStatsDTO;
import com.example.authentication.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping
    public ResponseEntity<ManagerStatsDTO> getAllManagersWithStats() {
        ManagerStatsDTO managerStats = managerService.getAllManagersWithStats();
        return ResponseEntity.ok(managerStats);
    }
}
