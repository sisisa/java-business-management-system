package com.example.business_management.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.business_management.entity.Customer;
import com.example.business_management.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer findById(UUID customerId) {
        return customerRepository.findById(customerId)
                .orElse(null);
    }

    public Customer update(UUID customerId, Customer customer) {
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElse(null);

        if (existingCustomer == null) {
            return null;
        }

        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setCustomerKanaName(customer.getCustomerKanaName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPhoneNumber(customer.getPhoneNumber());
        existingCustomer.setGender(customer.getGender());

        return customerRepository.save(existingCustomer);
    }

    public void delete(UUID customerId) {
        customerRepository.deleteById(customerId);
    }
}