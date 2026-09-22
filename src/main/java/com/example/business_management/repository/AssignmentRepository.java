package com.example.business_management.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.business_management.entity.Assignment;

public interface AssignmentRepository
        extends JpaRepository<Assignment, UUID> {

}