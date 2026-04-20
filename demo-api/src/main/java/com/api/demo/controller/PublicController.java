package com.api.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.demo.dto.AccountDTO;
import com.api.demo.model.ApiResponse;
import com.api.demo.model.LoginRequest;
import com.api.demo.model.LoginResponse;
import com.api.demo.service.AccountService;
import com.api.demo.utils.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Authentication", description = "Public authentication endpoints")
public class PublicController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/api/v1/auth/login")
	@Operation(summary = "Login", description = "Authenticate user and receive JWT token")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

			AccountDTO account = accountService.findByEmail(authentication.getName());
			String jwt = JwtUtil.generateToken(authentication);

			return ResponseEntity.ok().header("Authorization", jwt)
					.body(ApiResponse.success(new LoginResponse(account, "Login successful")));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.error("Invalid credentials", "INVALID_CREDENTIALS"));
		}
	}

	@GetMapping("/welcome")
	@Operation(summary = "Welcome", description = "Health check endpoint")
	public ResponseEntity<ApiResponse<String>> welcome() {
		return new ResponseEntity<>(ApiResponse.success("Welcome to Insurance Management System"), HttpStatus.OK);
	}

	@PostMapping("/api/v1/auth/register")
	@Operation(summary = "Register", description = "Create a new user account")
	public ResponseEntity<ApiResponse<AccountDTO>> register(@Valid @RequestBody AccountDTO account) {
		AccountDTO created = accountService.addAccount(account);
		return new ResponseEntity<>(ApiResponse.success(created, "Account created successfully"), HttpStatus.CREATED);
	}
}
