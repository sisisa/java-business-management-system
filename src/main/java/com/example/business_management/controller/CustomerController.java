package com.example.business_management.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.business_management.dto.customer.CustomerResponse;
import com.example.business_management.service.CustomerService;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Customer APIのHTTPリクエストを担当するController。
 * 業務処理はCustomerServiceへ委譲する。
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /** 顧客を全件取得する。 */
    @GetMapping
    public List<CustomerResponse> findAll() {
        return customerService.findAll();
    }

    /** 顧客を1件取得する。 */
    @GetMapping("/{customerId}")
    public CustomerResponse findById(
            @PathVariable UUID customerId) {
        return customerService.findById(customerId);
    }

    /** 顧客を登録する。 */
    @PostMapping
    public ResponseEntity<CustomerResponse> create(
            @RequestBody JsonNode request) {
        CustomerResponse response = customerService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /** 顧客を更新する。 */
    @PutMapping("/{customerId}")
    public CustomerResponse update(
            @PathVariable UUID customerId,
            @RequestBody JsonNode request) {
        return customerService.update(customerId, request);
    }

    /** 顧客を削除する。 */
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID customerId) {
        customerService.delete(customerId);
        return ResponseEntity.noContent().build();
    }
}