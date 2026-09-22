package com.example.business_management.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
// ここはPostgreSQLの customers テーブルをJava側では Customer として扱う設定をしている
@Table(name = "customers") 
public class Customer {
    @Id
    @GeneratedValue
    private UUID customerId;
    private String customerName;
    private String customerKanaName;
    private String email;
    private String phoneNumber;
    private String gender;
    
    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerKanaName() {
        return customerKanaName;
    }

    public void setCustomerKanaName(String customerKanaName) {
        this.customerKanaName = customerKanaName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}