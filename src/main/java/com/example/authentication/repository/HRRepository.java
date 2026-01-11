package com.example.authentication.repository;

import com.example.authentication.model.HR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HRRepository extends JpaRepository<HR, Long> {
}
