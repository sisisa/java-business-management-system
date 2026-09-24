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

import com.example.business_management.dto.customer.CustomerCreateRequest;
import com.example.business_management.dto.customer.CustomerResponse;
import com.example.business_management.dto.customer.CustomerUpdateRequest;
import com.example.business_management.service.CustomerService;

import jakarta.validation.Valid;

/**
 * Customer APIのHTTPリクエストを担当するController。
 *
 * ControllerではHTTPに関する処理だけを行い、
 * 業務処理はCustomerServiceへ委譲する。
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Customerを全件取得する。
     */
    @GetMapping
    public List<CustomerResponse> findAll() {
        return customerService.findAll();
    }

    /**
     * Customerを1件取得する。
     */
    @GetMapping("/{customerId}")
    public CustomerResponse findById(
            @PathVariable UUID customerId) {

        return customerService.findById(customerId);
    }

    /**
     * Customerを登録する。
     *
     * @ValidによってRequest DTOの入力チェックを実行する。
     */
    @PostMapping
    public ResponseEntity<CustomerResponse> create(
            @Valid @RequestBody CustomerCreateRequest request) {

        CustomerResponse response = customerService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Customerを更新する。
     */
    @PutMapping("/{customerId}")
    public CustomerResponse update(
            @PathVariable UUID customerId,
            @Valid @RequestBody CustomerUpdateRequest request) {

        return customerService.update(customerId, request);
    }

    /**
     * Customerを削除する。
     *
     * 正常終了時は204 No Contentを返す。
     */
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID customerId) {

        customerService.delete(customerId);

        return ResponseEntity.noContent().build();
    }
}
