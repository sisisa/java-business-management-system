package com.example.business_management.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.business_management.entity.EmployeeWorkHistory;

public interface EmployeeWorkHistoryRepository
        extends JpaRepository<EmployeeWorkHistory, UUID> {

}