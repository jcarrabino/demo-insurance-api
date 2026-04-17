package com.api.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.demo.dto.AccountDTO;
import com.api.demo.service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts/")
public class AccountController {

	@Autowired
	private AccountService accountService;

	// @PostMapping("/")
	// public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody
	// AccountDTO account){
	// return new
	// ResponseEntity<AccountDTO>(accountService.addAccount(account),HttpStatus.CREATED);
	// }

	@PutMapping("/{id}")
	public ResponseEntity<AccountDTO> updateAccount(@Valid @RequestBody AccountDTO account,
			@PathVariable("id") Integer id) {
		return new ResponseEntity<AccountDTO>(accountService.updateAccountInfo(account, id), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAccount(@PathVariable("id") Integer id) {
		return new ResponseEntity<String>(accountService.deleteAccount(id), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AccountDTO> getAccountById(@PathVariable("id") Integer id) {
		return new ResponseEntity<AccountDTO>(accountService.findById(id), HttpStatus.OK);
	}

	@GetMapping("/email/")
	public ResponseEntity<AccountDTO> getAccountByEmail(@RequestParam("email") String email) {
		return new ResponseEntity<AccountDTO>(accountService.findByEmail(email), HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<List<AccountDTO>> getAllAccount() {
		return new ResponseEntity<List<AccountDTO>>(accountService.findAllAccount(), HttpStatus.OK);
	}

}
