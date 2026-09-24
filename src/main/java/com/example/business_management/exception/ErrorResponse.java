package com.example.business_management.exception;

import java.time.LocalDateTime;

/**
 * APIエラー時に返す共通レスポンス。
 *
 * エラーごとにレスポンス形式を変えず、
 * クライアント側が扱いやすい一定の形式にする。
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message
) {
}
