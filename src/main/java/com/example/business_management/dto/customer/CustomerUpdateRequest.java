package com.example.business_management.dto.customer;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Customer更新API専用のRequest DTO。
 *
 * 登録と更新で入力ルールが将来的に変わる可能性があるため、
 * CreateRequestとUpdateRequestを分離する。
 */
public record CustomerUpdateRequest(

        @NotBlank(message = "顧客名は必須です")
        @Size(max = 255, message = "顧客名は255文字以内で入力してください")
        String customerName,

        @NotBlank(message = "顧客カナ名は必須です")
        @Size(max = 255, message = "顧客カナ名は255文字以内で入力してください")
        String customerKanaName,

        @Email(message = "メールアドレスの形式が正しくありません")
        @Size(max = 255, message = "メールアドレスは255文字以内で入力してください")
        String email,

        @Size(max = 255, message = "電話番号は255文字以内で入力してください")
        String phoneNumber,

        @Size(max = 50, message = "性別は50文字以内で入力してください")
        String gender,

        JsonNode customerItems
) {
}
