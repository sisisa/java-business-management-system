package com.example.business_management.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.business_management.dto.customer.CustomerCreateRequest;
import com.example.business_management.dto.customer.CustomerResponse;
import com.example.business_management.dto.customer.CustomerUpdateRequest;
import com.example.business_management.entity.Customer;
import com.example.business_management.exception.ResourceNotFoundException;
import com.example.business_management.repository.CustomerRepository;
import com.example.business_management.service.CustomerService;

/**
 * CustomerServiceの単体テスト。
 *
 * DBを実際に使用せず、
 * Serviceの業務処理そのものを検証する。
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

        @Mock
        private CustomerRepository customerRepository;

        @InjectMocks
        private CustomerService customerService;

        private UUID customerId;

        @BeforeEach
        void setUp() {
                customerId = UUID.randomUUID();
        }

        /**
         * Customer登録時にRequestの値がEntityへ設定され、
         * Repositoryへ保存されることを確認する。
         */
        @Test
        void create_savesCustomer() {
                CustomerCreateRequest request = new CustomerCreateRequest(
                                "テスト株式会社",
                                "テストカブシキガイシャ",
                                "test@example.com",
                                "03-1234-5678",
                                "unknown",
                                null);

                Customer savedCustomer = new Customer();
                savedCustomer.setCustomerId(customerId);
                savedCustomer.setCustomerName(request.customerName());
                savedCustomer.setCustomerKanaName(request.customerKanaName());
                savedCustomer.setEmail(request.email());
                savedCustomer.setPhoneNumber(request.phoneNumber());
                savedCustomer.setGender(request.gender());

                when(customerRepository.save(
                                org.mockito.ArgumentMatchers.any(Customer.class)))
                                .thenReturn(savedCustomer);

                CustomerResponse response = customerService.create(request);

                assertEquals(customerId, response.customerId());
                assertEquals("テスト株式会社", response.customerName());
                assertEquals("test@example.com", response.email());

                verify(customerRepository).save(
                                org.mockito.ArgumentMatchers.any(Customer.class));
        }

        /**
         * 存在するCustomerを取得できることを確認する。
         */
        @Test
        void findById_returnsCustomer() {
                Customer customer = new Customer();
                customer.setCustomerId(customerId);
                customer.setCustomerName("テスト株式会社");
                customer.setCustomerKanaName("テストカブシキガイシャ");

                when(customerRepository.findById(customerId))
                                .thenReturn(Optional.of(customer));

                CustomerResponse response = customerService.findById(customerId);

                assertEquals(customerId, response.customerId());
                assertEquals("テスト株式会社", response.customerName());
        }

        /**
         * 存在しないCustomerを取得した場合、
         * ResourceNotFoundExceptionが発生することを確認する。
         */
        @Test
        void findById_throwsExceptionWhenCustomerDoesNotExist() {
                when(customerRepository.findById(customerId))
                                .thenReturn(Optional.empty());

                assertThrows(
                                ResourceNotFoundException.class,
                                () -> customerService.findById(customerId));
        }

        /**
         * Customer更新時に既存Entityの値が変更されることを確認する。
         */
        @Test
        void update_updatesExistingCustomer() {
                Customer existingCustomer = new Customer();
                existingCustomer.setCustomerId(customerId);
                existingCustomer.setCustomerName("変更前");
                existingCustomer.setCustomerKanaName("ヘンコウマエ");

                CustomerUpdateRequest request = new CustomerUpdateRequest(
                                "変更後",
                                "ヘンコウゴ",
                                "updated@example.com",
                                "03-9999-9999",
                                "unknown",
                                null);

                when(customerRepository.findById(customerId))
                                .thenReturn(Optional.of(existingCustomer));

                CustomerResponse response = customerService.update(customerId, request);

                assertEquals("変更後", response.customerName());
                assertEquals("ヘンコウゴ", response.customerKanaName());
                assertEquals("updated@example.com", response.email());

                verify(customerRepository).findById(customerId);
        }

        /**
         * 存在しないCustomerを更新しようとした場合、
         * 更新処理を実行せず404用の例外を発生させる。
         */
        @Test
        void update_throwsExceptionWhenCustomerDoesNotExist() {
                CustomerUpdateRequest request = new CustomerUpdateRequest(
                                "変更後",
                                "ヘンコウゴ",
                                "updated@example.com",
                                "03-9999-9999",
                                "unknown",
                                null);

                when(customerRepository.findById(customerId))
                                .thenReturn(Optional.empty());

                assertThrows(
                                ResourceNotFoundException.class,
                                () -> customerService.update(customerId, request));

                verify(customerRepository, never())
                                .delete(org.mockito.ArgumentMatchers.any(Customer.class));
        }

        /**
         * Customerが存在する場合に削除されることを確認する。
         */
        @Test
        void delete_deletesExistingCustomer() {
                Customer customer = new Customer();
                customer.setCustomerId(customerId);

                when(customerRepository.findById(customerId))
                                .thenReturn(Optional.of(customer));

                customerService.delete(customerId);

                verify(customerRepository).delete(customer);
        }

        /**
         * 存在しないCustomerを削除しようとした場合、
         * 削除処理を実行せず例外を発生させる。
         */
        @Test
        void delete_throwsExceptionWhenCustomerDoesNotExist() {
                when(customerRepository.findById(customerId))
                                .thenReturn(Optional.empty());

                assertThrows(
                                ResourceNotFoundException.class,
                                () -> customerService.delete(customerId));

                verify(customerRepository, never())
                                .delete(org.mockito.ArgumentMatchers.any(Customer.class));
        }
}
