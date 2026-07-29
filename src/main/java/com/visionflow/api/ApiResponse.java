package com.visionflow.api;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

@Data
@Builder
@JsonPropertyOrder({"success", "timestamp", "message", "path", "data"})
public class ApiResponse<T> {
    private boolean success;
    private LocalDateTime timestamp;
    private String message;
    private String path;
    private T data;

    public static <T> ApiResponse<T> success(T data, String path) {
        return success(data, path, null);
    }

    public static <T> ApiResponse<T> success(T data, String path, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .path(path)
                .data(data)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, String path) {
        return ApiResponse.<T>builder()
                .success(false)
                .timestamp(LocalDateTime.now())
                .path(path)
                .message(message)
                .build();
    }
}