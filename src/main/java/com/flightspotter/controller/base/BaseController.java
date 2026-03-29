package com.flightspotter.controller.base;

import com.flightspotter.dto.common.ApiResponse;
import com.flightspotter.enums.AppMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    protected <T> ResponseEntity<ApiResponse<T>> ok(AppMessage message, T data) {
        return ResponseEntity.ok(ApiResponse.success(message, data));
    }

    protected <T> ResponseEntity<ApiResponse<T>> ok(AppMessage message) {
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    protected <T> ResponseEntity<ApiResponse<T>> created(AppMessage message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(message, data));
    }

    protected <T> ResponseEntity<ApiResponse<T>> noContent() {
        return ResponseEntity.noContent().build();
    }

    protected <T> ResponseEntity<ApiResponse<T>> badRequest(AppMessage message) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(message));
    }

    protected <T> ResponseEntity<ApiResponse<T>> notFound(AppMessage message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(message));
    }
}
