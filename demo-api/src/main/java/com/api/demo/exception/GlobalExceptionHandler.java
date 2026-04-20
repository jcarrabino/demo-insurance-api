package com.api.demo.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.api.demo.model.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
		logger.warn("Resource not found: {}", ex.getMessage());
		String requestId = MDC.get("requestId");
		return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), "RESOURCE_NOT_FOUND", requestId),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException ex) {
		logger.warn("Bad credentials: {}", ex.getMessage());
		String requestId = MDC.get("requestId");
		return new ResponseEntity<>(ApiResponse.error("Invalid credentials", "INVALID_CREDENTIALS", requestId),
				HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(SecurityException.class)
	public ResponseEntity<ApiResponse<Void>> handleSecurityException(SecurityException ex) {
		logger.warn("Security exception: {}", ex.getMessage());
		String requestId = MDC.get("requestId");
		return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), "FORBIDDEN", requestId), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException ex) {
		logger.warn("Validation error: {}", ex.getMessage());
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((err) -> {
			String fieldName = ((FieldError) err).getField();
			String message = err.getDefaultMessage();
			errors.put(fieldName, message);
		});
		String requestId = MDC.get("requestId");
		return new ResponseEntity<>(
				ApiResponse.<Map<String, String>>builder().data(errors).message("Validation failed").success(false)
						.errorCode("VALIDATION_ERROR").requestId(requestId).timestamp(LocalDateTime.now()).build(),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		logger.error("Data integrity violation: {}", ex.getMessage());
		String requestId = MDC.get("requestId");
		return new ResponseEntity<>(ApiResponse.error("Data integrity violation", "DATA_INTEGRITY_ERROR", requestId),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidDataAccessResourceUsageException.class)
	public ResponseEntity<ApiResponse<Void>> handleInvalidDataAccessResourceUsageException(
			InvalidDataAccessResourceUsageException ex) {
		logger.error("Invalid data access: {}", ex.getMessage());
		String requestId = MDC.get("requestId");
		return new ResponseEntity<>(ApiResponse.error("Invalid data access", "DATA_ACCESS_ERROR", requestId),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
		logger.error("Unexpected error: ", ex);
		String requestId = MDC.get("requestId");
		return new ResponseEntity<>(
				ApiResponse.error("An unexpected error occurred", "INTERNAL_SERVER_ERROR", requestId),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
