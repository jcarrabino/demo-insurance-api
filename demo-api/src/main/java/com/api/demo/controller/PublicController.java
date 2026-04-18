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
import com.api.demo.model.LoginRequest;
import com.api.demo.model.LoginResponse;
import com.api.demo.service.AccountService;
import com.api.demo.utils.JwtUtil;

import jakarta.validation.Valid;

@RestController
public class PublicController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/api/v1/auth/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
			);
			
			AccountDTO account = accountService.findByEmail(authentication.getName());
			String jwt = JwtUtil.generateToken(authentication);
			
			return ResponseEntity.ok()
					.header("Authorization", jwt)
					.body(new LoginResponse(account, "Login successful"));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new LoginResponse(null, "Invalid credentials"));
		}
	}

	@GetMapping("/login")
	public ResponseEntity<AccountDTO> getLoggedInClientDetailsHandler(Authentication auth) {
		if (auth == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		//AccountDTO account = accountService.findByEmail(auth.getName());
		return new ResponseEntity<AccountDTO>(HttpStatus.OK);
	}

	@GetMapping("/welcome")
	public String welcomeHandeler() {
		return "Welcome to Insurance manegement system";
	}

	@PostMapping("/register")
	public ResponseEntity<AccountDTO> createClient(@Valid @RequestBody AccountDTO account) {
		return new ResponseEntity<AccountDTO>(accountService.addAccount(account), HttpStatus.CREATED);
	}

}
