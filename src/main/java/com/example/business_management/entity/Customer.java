package com.example.business_management.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 顧客情報をDBのcustomersテーブルに対応付けるEntity。
 *
 * Entityは「DBにどのようなデータを保存するか」を表現する責務に限定する。
 * APIから受け取る入力値の検証や、HTTPレスポンスの形式はDTO側で扱う。
 */
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue
    private UUID customerId;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerKanaName;

    private String email;

    private String phoneNumber;

    private String gender;

    /**
     * 顧客ごとに内容が異なる追加情報をJSON形式で保持する。
     *
     * 検索や他テーブルとの関連に使う主要項目は通常のカラムで管理し、
     * 固定化しにくい補足情報だけをJSONBに寄せる。
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode customerItems;

    /**
     * 登録日時・更新日時はEntity側で自動管理する。
     * APIから直接受け取らないことで、クライアント側から日時を改ざんできないようにする。
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

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

    public JsonNode getCustomerItems() {
        return customerItems;
    }

    public void setCustomerItems(JsonNode customerItems) {
        this.customerItems = customerItems;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

