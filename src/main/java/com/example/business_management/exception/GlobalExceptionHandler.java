package com.example.business_management.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * API全体の例外を共通処理する。
 *
 * 各Controllerにtry-catchを書くのではなく、
 * アプリケーション全体のエラー処理を一箇所に集約する。
 *
 * Customerだけでなく、ProjectやEmployeeからも再利用できる。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 指定されたデータが存在しない場合は404を返す。
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException exception) {

        return createResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage());
    }

    /**
     * Request DTOのValidationエラーは400を返す。
     *
     * 複数の入力エラーをまとめてクライアントへ返す。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException exception) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return createResponse(
                HttpStatus.BAD_REQUEST,
                message);
    }

    /**
     * 共通のErrorResponse生成処理。
     */
    private ResponseEntity<ErrorResponse> createResponse(
            HttpStatus status,
            String message) {

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message);

        return ResponseEntity
                .status(status)
                .body(response);
    }
}
