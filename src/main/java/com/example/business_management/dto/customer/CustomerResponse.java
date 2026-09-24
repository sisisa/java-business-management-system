package com.example.business_management.dto.customer;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.business_management.entity.Customer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Customer APIのResponse DTO。
 *
 * Entityをそのまま外部へ返さず、
 * APIとして公開するデータだけを明示する。
 */
public record CustomerResponse(
        UUID customerId,
        String customerName,
        String customerKanaName,
        String email,
        String phoneNumber,
        String gender,
        JsonNode customerItems,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    /**
     * EntityからAPI Response DTOへ変換する共通処理。
     *
     * ControllerやServiceの各所で同じ変換処理を書かないため、
     * DTO自身に変換責務を持たせる。
     */
    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.getCustomerId(),
                customer.getCustomerName(),
                customer.getCustomerKanaName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getGender(),
                customer.getCustomerItems(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }
}
