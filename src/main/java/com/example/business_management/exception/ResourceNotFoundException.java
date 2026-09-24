package com.example.business_management.exception;

/**
 * 指定されたリソースが存在しない場合に使用する例外。
 *
 * CustomerだけでなくProjectやEmployeeでも再利用できる
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
