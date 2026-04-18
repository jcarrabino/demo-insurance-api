package com.api.demo.exception;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.api.demo.model.Response;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Response> responseNotFountException(ResourceNotFoundException rs) {

		Response responseModel = new Response(rs.getMessage(), false, LocalDateTime.now());
		return new ResponseEntity<Response>(responseModel, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> methodArgumentNotValidException(MethodArgumentNotValidException rs) {

		Map<String, String> error = new HashMap<>();
		rs.getBindingResult().getAllErrors().forEach((err) -> {
			String fieldName = ((FieldError) err).getField();
			String massage = err.getDefaultMessage();
			error.put(fieldName, massage);
		});

		return new ResponseEntity<Map<String, String>>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Response> dataIntegrityViolationException(DataIntegrityViolationException rs) {
		String msg = rs.getMostSpecificCause().toString();
		Response responseModel = new Response(msg, false, LocalDateTime.now());
		return new ResponseEntity<Response>(responseModel, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidDataAccessResourceUsageException.class)
	public ResponseEntity<Response> invalidDataAccessResourceUsageException(
			InvalidDataAccessResourceUsageException rs) {
		Response responseModel = new Response(rs.getMessage(), false, LocalDateTime.now());
		return new ResponseEntity<Response>(responseModel, HttpStatus.BAD_REQUEST);
	}

}
