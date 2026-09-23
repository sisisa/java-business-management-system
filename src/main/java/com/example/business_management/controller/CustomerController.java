package com.example.business_management.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.example.business_management.entity.Customer;
import com.example.business_management.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/{customerId}")
    public Customer findById(@PathVariable UUID customerId) {
        return customerService.findById(customerId);
    }

    @PostMapping
    public Customer create(@RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @PutMapping("/{customerId}")
    public Customer update(
            @PathVariable UUID customerId,
            @RequestBody Customer customer) {

        return customerService.update(customerId, customer);
    }

    @DeleteMapping("/{customerId}")
    public void delete(@PathVariable UUID customerId) {
        customerService.delete(customerId);
    }
}