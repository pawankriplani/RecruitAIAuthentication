package com.example.authentication.controller;

import com.example.authentication.dto.HRDTO;
import com.example.authentication.service.HRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hr")
public class HRController {

    private final HRService hrService;

    @Autowired
    public HRController(HRService hrService) {
        this.hrService = hrService;
    }

    @GetMapping
    public ResponseEntity<List<HRDTO>> getAllHRs() {
        List<HRDTO> hrs = hrService.getAllHRs();
        return ResponseEntity.ok(hrs);
    }
}
