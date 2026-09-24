package com.example.business_management.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.business_management.dto.customer.CustomerCreateRequest;
import com.example.business_management.dto.customer.CustomerResponse;
import com.example.business_management.dto.customer.CustomerUpdateRequest;
import com.example.business_management.entity.Customer;
import com.example.business_management.exception.ResourceNotFoundException;
import com.example.business_management.repository.CustomerRepository;

/**
 * Customerに関する業務処理を担当するService。
 *
 * ControllerにはHTTP処理だけを担当させ、
 * DBアクセスやEntityの更新処理はServiceに集約する。
 *
 * ProjectやEmployeeを実装するときも、
 * 「Controller → Service → Repository」という構造を基本形として再利用する。
 */
@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Customerを全件取得する。
     */
    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerResponse::from)
                .toList();
    }

    /**
     * Customerを1件取得する。
     *
     * 存在しないIDはnullではなく例外として扱う。
     * HTTP上の404への変換はExceptionHandlerに任せる。
     */
    @Transactional(readOnly = true)
    public CustomerResponse findById(UUID customerId) {
        Customer customer = findEntityById(customerId);
        return CustomerResponse.from(customer);
    }

    /**
     * Customerを登録する。
     *
     * Request DTOからEntityへ必要な値だけを移し、
     * Entityを直接Controllerから受け取らない。
     */
    public CustomerResponse create(CustomerCreateRequest request) {
        Customer customer = new Customer();

        customer.setCustomerName(request.customerName());
        customer.setCustomerKanaName(request.customerKanaName());
        customer.setEmail(request.email());
        customer.setPhoneNumber(request.phoneNumber());
        customer.setGender(request.gender());
        customer.setCustomerItems(request.customerItems());

        Customer savedCustomer = customerRepository.save(customer);

        return CustomerResponse.from(savedCustomer);
    }

    /**
     * Customerを更新する。
     *
     * 既存Entityを取得してから値を変更することで、
     * Requestに含まれていないIDや管理項目を外部から変更させない。
     */
    public CustomerResponse update(
            UUID customerId,
            CustomerUpdateRequest request) {

        Customer customer = findEntityById(customerId);

        customer.setCustomerName(request.customerName());
        customer.setCustomerKanaName(request.customerKanaName());
        customer.setEmail(request.email());
        customer.setPhoneNumber(request.phoneNumber());
        customer.setGender(request.gender());
        customer.setCustomerItems(request.customerItems());

        return CustomerResponse.from(customer);
    }

    /**
     * Customerを削除する。
     *
     * 存在しないIDを削除しようとした場合も、
     * GETやUPDATEと同じ404として扱う。
     */
    public void delete(UUID customerId) {
        Customer customer = findEntityById(customerId);
        customerRepository.delete(customer);
    }

    /**
     * IDからEntityを取得する共通処理。
     *
     * 「存在確認 → 404用例外」という処理を各メソッドに重複して書かない。
     */
    private Customer findEntityById(UUID customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found: " + customerId));
    }
}
