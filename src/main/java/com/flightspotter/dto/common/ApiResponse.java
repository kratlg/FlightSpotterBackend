package com.flightspotter.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.flightspotter.enums.AppMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(AppMessage message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message.getMessage())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(AppMessage message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message.getMessage())
                .build();
    }

    public static <T> ApiResponse<T> error(AppMessage message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message.getMessage())
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}
