package com.example.authentication.service;

import com.example.authentication.dto.HRDTO;
import com.example.authentication.model.HR;
import com.example.authentication.repository.HRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HRService {

    private final HRRepository hrRepository;

    @Autowired
    public HRService(HRRepository hrRepository) {
        this.hrRepository = hrRepository;
    }

    public List<HRDTO> getAllHRs() {
        return hrRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private HRDTO convertToDTO(HR hr) {
        return new HRDTO(hr.getId(), hr.getName(), hr.getEmail());
    }
}
