package com.example.business_management.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.business_management.entity.Employee;

public interface EmployeeRepository
        extends JpaRepository<Employee, UUID> {

}