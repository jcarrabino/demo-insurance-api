package com.api.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Unified API response wrapper for all endpoints
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
	private T data;
	private String message;
	private boolean success;
	private LocalDateTime timestamp;
	private String errorCode;
	private String requestId;

	public static <T> ApiResponse<T> success(T data, String message) {
		return ApiResponse.<T>builder().data(data).message(message).success(true).timestamp(LocalDateTime.now())
				.build();
	}

	public static <T> ApiResponse<T> success(T data) {
		return success(data, null);
	}

	public static <T> ApiResponse<T> error(String message, String errorCode) {
		return ApiResponse.<T>builder().message(message).success(false).errorCode(errorCode)
				.timestamp(LocalDateTime.now()).build();
	}

	public static <T> ApiResponse<T> error(String message, String errorCode, String requestId) {
		return ApiResponse.<T>builder().message(message).success(false).errorCode(errorCode).requestId(requestId)
				.timestamp(LocalDateTime.now()).build();
	}
}
