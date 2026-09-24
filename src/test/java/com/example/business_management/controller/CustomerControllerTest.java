package com.example.business_management.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.business_management.dto.customer.CustomerCreateRequest;
import com.example.business_management.dto.customer.CustomerResponse;
import com.example.business_management.dto.customer.CustomerUpdateRequest;
import com.example.business_management.exception.GlobalExceptionHandler;
import com.example.business_management.exception.ResourceNotFoundException;
import com.example.business_management.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * CustomerControllerのAPIテスト。
 *
 * Controller → Serviceの接続と、
 * HTTPステータス・JSONレスポンスを検証する。
 */
class CustomerControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private CustomerService customerService;

    private UUID customerId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        CustomerController controller =
                new CustomerController(customerService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        customerId = UUID.randomUUID();
    }

    /**
     * GET /api/customers が200を返すことを確認する。
     */
    @Test
    void findAll_returns200() throws Exception {
        CustomerResponse response = new CustomerResponse(
                customerId,
                "テスト株式会社",
                "テストカブシキガイシャ",
                "test@example.com",
                "03-1234-5678",
                "unknown",
                null,
                null,
                null);

        when(customerService.findAll())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName")
                        .value("テスト株式会社"));
    }

    /**
     * 存在するCustomerのGETが200を返すことを確認する。
     */
    @Test
    void findById_returns200() throws Exception {
        CustomerResponse response = new CustomerResponse(
                customerId,
                "テスト株式会社",
                "テストカブシキガイシャ",
                "test@example.com",
                "03-1234-5678",
                "unknown",
                null,
                null,
                null);

        when(customerService.findById(customerId))
                .thenReturn(response);

        mockMvc.perform(
                get("/api/customers/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName")
                        .value("テスト株式会社"));
    }

    /**
     * 存在しないCustomerのGETが404を返すことを確認する。
     */
    @Test
    void findById_returns404WhenCustomerDoesNotExist()
            throws Exception {

        when(customerService.findById(customerId))
                .thenThrow(new ResourceNotFoundException(
                        "Customer not found: " + customerId));

        mockMvc.perform(
                get("/api/customers/{customerId}", customerId))
                .andExpect(status().isNotFound());
    }

    /**
     * 正常なCustomer登録が201を返すことを確認する。
     */
    @Test
    void create_returns201() throws Exception {
        CustomerCreateRequest request =
                new CustomerCreateRequest(
                        "テスト株式会社",
                        "テストカブシキガイシャ",
                        "test@example.com",
                        "03-1234-5678",
                        "unknown",
                        null);

        CustomerResponse response = new CustomerResponse(
                customerId,
                request.customerName(),
                request.customerKanaName(),
                request.email(),
                request.phoneNumber(),
                request.gender(),
                null,
                null,
                null);

        when(customerService.create(any(CustomerCreateRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId")
                        .value(customerId.toString()));
    }

    /**
     * 必須項目が空の場合に400を返すことを確認する。
     */
    @Test
    void create_returns400WhenValidationFails()
            throws Exception {

        String invalidRequest = """
                {
                    "customerName": "",
                    "customerKanaName": "",
                    "email": "invalid-email"
                }
                """;

        mockMvc.perform(
                post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    /**
     * 正常なCustomer更新が200を返すことを確認する。
     */
    @Test
    void update_returns200() throws Exception {
        CustomerUpdateRequest request =
                new CustomerUpdateRequest(
                        "更新後株式会社",
                        "コウシンゴカブシキガイシャ",
                        "updated@example.com",
                        "03-9999-9999",
                        "unknown",
                        null);

        CustomerResponse response = new CustomerResponse(
                customerId,
                request.customerName(),
                request.customerKanaName(),
                request.email(),
                request.phoneNumber(),
                request.gender(),
                null,
                null,
                null);

        when(customerService.update(
                eq(customerId),
                any(CustomerUpdateRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                put("/api/customers/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName")
                        .value("更新後株式会社"));
    }

    /**
     * 正常なCustomer削除が204を返すことを確認する。
     */
    @Test
    void delete_returns204() throws Exception {
        doNothing()
                .when(customerService)
                .delete(customerId);

        mockMvc.perform(
                delete("/api/customers/{customerId}", customerId))
                .andExpect(status().isNoContent());
    }

    /**
     * 存在しないCustomerの削除が404を返すことを確認する。
     */
    @Test
    void delete_returns404WhenCustomerDoesNotExist()
            throws Exception {

        org.mockito.Mockito.doThrow(
                new ResourceNotFoundException(
                        "Customer not found: " + customerId))
                .when(customerService)
                .delete(customerId);

        mockMvc.perform(
                delete("/api/customers/{customerId}", customerId))
                .andExpect(status().isNotFound());
    }
}
