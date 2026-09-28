package com.example.business_management.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.business_management.dto.customer.CustomerResponse;
import com.example.business_management.entity.Customer;
import com.example.business_management.exception.ResourceNotFoundException;
import com.example.business_management.repository.CustomerRepository;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Customerに関する業務処理を担当するService。
 */
@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /** 顧客を全件取得する。 */
    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerResponse::from)
                .toList();
    }

    /** 顧客を1件取得する。 */
    @Transactional(readOnly = true)
    public CustomerResponse findById(UUID customerId) {
        return CustomerResponse.from(findEntityById(customerId));
    }

    /**
     * 顧客を登録する。
     * 入力値の検証はLaravel側で行う。
     */
    public CustomerResponse create(JsonNode request) {
        Customer customer = new Customer();
        applyRequest(customer, request);

        return CustomerResponse.from(customerRepository.save(customer));
    }

    /**
     * 顧客を更新する。
     * IDや登録日時はリクエストから変更させない。
     */
    public CustomerResponse update(UUID customerId, JsonNode request) {
        Customer customer = findEntityById(customerId);
        applyRequest(customer, request);

        return CustomerResponse.from(customer);
    }

    /** 顧客を削除する。 */
    public void delete(UUID customerId) {
        customerRepository.delete(findEntityById(customerId));
    }

    /**
     * リクエストの値をEntityに反映する。
     * Laravelから送信されるJSONのキーはcamelCaseとする。
     */
    private void applyRequest(Customer customer, JsonNode request) {
        customer.setCustomerName(request.path("customerName").asText(null));
        customer.setCustomerKanaName(request.path("customerKanaName").asText(null));
        customer.setEmail(request.path("email").asText(null));
        customer.setPhoneNumber(request.path("phoneNumber").asText(null));
        customer.setGender(request.path("gender").asText(null));

        JsonNode items = request.get("customerItems");
        customer.setCustomerItems(
                items == null || items.isNull() ? null : items
        );
    }

    /** IDから顧客を取得し、存在しない場合は404用の例外を投げる。 */
    private Customer findEntityById(UUID customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found: " + customerId));
    }
}