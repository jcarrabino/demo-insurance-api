package com.api.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.demo.config.SecurityContext;
import com.api.demo.constraints.ValidationGroups;
import com.api.demo.dto.AccountDTO;
import com.api.demo.service.AccountService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

@RestController
@Validated
public class PublicController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private AuthenticationManager authenticationManager;

	public static class LoginRequest {
		private String email;
		private String password;

		public String getEmail() { return email; }
		public void setEmail(String email) { this.email = email; }
		public String getPassword() { return password; }
		public void setPassword(String password) { this.password = password; }
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

		try {
			// Authenticate
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

			// Generate JWT
			SecretKey key = Keys.hmacShaKeyFor(SecurityContext.JWT_KEY.getBytes());
			String authorities = authentication.getAuthorities().stream()
					.map(a -> a.getAuthority())
					.collect(Collectors.joining(","));

			String jwt = Jwts.builder()
					.setIssuer("Insurance API")
					.setSubject("JWT Token")
					.claim("username", authentication.getName())
					.claim("authorities", authorities)
					.setIssuedAt(new Date())
					.setExpiration(new Date(System.currentTimeMillis() + 30000000))
					.signWith(key)
					.compact();

			// Set JWT in response header
			response.setHeader(SecurityContext.JWT_HEADER, jwt);

			// Return user data
			AccountDTO account = accountService.findByEmail(loginRequest.getEmail());
			return new ResponseEntity<>(account, HttpStatus.OK);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("message", "Invalid credentials"));
		}
	}

	@GetMapping("/welcome")
	public String welcomeHandeler() {
		return "Welcome to Insurance manegement system";
	}

	@PostMapping("/register")
	public ResponseEntity<AccountDTO> createClient(@Validated(ValidationGroups.Create.class) @RequestBody AccountDTO account) {
		return new ResponseEntity<AccountDTO>(accountService.addAccount(account), HttpStatus.CREATED);
	}

}
