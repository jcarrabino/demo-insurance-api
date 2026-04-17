package com.api.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.demo.dto.AccountDTO;
import com.api.demo.service.AccountService;

import jakarta.validation.Valid;

@RestController
public class PublicController {

	@Autowired
	private AccountService accountService;

	@GetMapping("/login")
	public ResponseEntity<AccountDTO> getLoggedInClientDetailsHandler(Authentication auth) {

		AccountDTO account = accountService.findByEmail(auth.getName());
		return new ResponseEntity<AccountDTO>(account, HttpStatus.OK);
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
