package com.api.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;
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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Authentication", description = "Public authentication endpoints")
public class PublicController {

	private final AccountService accountService;
	private final AuthenticationManager authenticationManager;

	public PublicController(AccountService accountService, AuthenticationManager authenticationManager) {
		this.accountService = accountService;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/api/v1/auth/login")
	@Operation(summary = "Login", description = "Authenticate user and receive JWT token in HttpOnly cookie")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

			AccountDTO account = accountService.findByEmail(authentication.getName());
			String jwt = JwtUtil.generateToken(authentication);

			// Set JWT as HttpOnly cookie
			Cookie cookie = new Cookie("Authorization", jwt);
			cookie.setHttpOnly(true);
			cookie.setSecure(true);
			cookie.setPath("/");
			cookie.setMaxAge(86400); // 24 hours
			cookie.setAttribute("SameSite", "Strict");
			response.addCookie(cookie);

			return ResponseEntity.ok()
					.body(ApiResponse.success(new LoginResponse(account, "Login successful")));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(ApiResponse.error("Invalid credentials", "INVALID_CREDENTIALS"));
		}
	}

	@GetMapping("/welcome")
	@Operation(summary = "Welcome", description = "Health check endpoint")
	@Transactional(readOnly = true)
	public ResponseEntity<ApiResponse<String>> welcome() {
		return new ResponseEntity<>(ApiResponse.success("Welcome to the Demo Insurance Management App"), HttpStatus.OK);
	}

	@PostMapping("/api/v1/auth/register")
	@Operation(summary = "Register", description = "Create a new user account")
	public ResponseEntity<ApiResponse<AccountDTO>> register(@Valid @RequestBody AccountDTO account) {
		AccountDTO created = accountService.addAccount(account);
		return new ResponseEntity<>(ApiResponse.success(created, "Account created successfully"), HttpStatus.CREATED);
	}

	@PostMapping("/api/v1/auth/logout")
	@Operation(summary = "Logout", description = "Clear authentication cookie")
	public ResponseEntity<ApiResponse<String>> logout(HttpServletResponse response) {
		Cookie cookie = new Cookie("Authorization", null);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		return ResponseEntity.ok()
				.body(ApiResponse.success("Logged out successfully"));
	}
}
