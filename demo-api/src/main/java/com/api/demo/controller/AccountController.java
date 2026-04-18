package com.api.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.demo.dto.AccountDTO;
import com.api.demo.service.AccountService;
import com.api.demo.service.AuthorizationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts/")
@Tag(name = "Account Management", description = "APIs for managing user accounts")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private AuthorizationService authService;

	@PutMapping("/{id}")
	@Operation(summary = "Update account", description = "Full update of an account. Requires admin or account owner.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Account updated successfully"),
		@ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
		@ApiResponse(responseCode = "404", description = "Account not found")
	})
	@CacheEvict(value = "accounts", allEntries = true)
	public ResponseEntity<AccountDTO> updateAccount(
			@Valid @RequestBody AccountDTO account,
			@Parameter(description = "Account ID") @PathVariable("id") Integer id) {
		authService.requireAdminOrOwner(id);
		// Prevent non-admins from setting admin flag
		if (account.getAdmin() != null && account.getAdmin() && !authService.isAdmin()) {
			throw new SecurityException("Only admins can create admin users");
		}
		return new ResponseEntity<AccountDTO>(accountService.updateAccountInfo(account, id), HttpStatus.OK);
	}

	@PostMapping("/update/{id}")
	public ResponseEntity<AccountDTO> partialUpdateAccount(@RequestBody AccountDTO account,
			@PathVariable("id") Integer id) {
		authService.requireAdminOrOwner(id);
		// Prevent non-admins from setting admin flag
		if (account.getAdmin() != null && account.getAdmin() && !authService.isAdmin()) {
			throw new SecurityException("Only admins can create admin users");
		}
		return new ResponseEntity<AccountDTO>(accountService.partialUpdateAccountInfo(account, id), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAccount(@PathVariable("id") Integer id) {
		authService.requireAdminOrOwner(id);
		return new ResponseEntity<String>(accountService.deleteAccount(id), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get account by ID", description = "Retrieve account details by ID. Requires admin or account owner.")
	@Cacheable(value = "accounts", key = "#id")
	public ResponseEntity<AccountDTO> getAccountById(
			@Parameter(description = "Account ID") @PathVariable("id") Integer id) {
		authService.requireAdminOrOwner(id);
		return new ResponseEntity<AccountDTO>(accountService.findById(id), HttpStatus.OK);
	}

	@GetMapping("/email/")
	public ResponseEntity<AccountDTO> getAccountByEmail(@RequestParam("email") String email) {
		return new ResponseEntity<AccountDTO>(accountService.findByEmail(email), HttpStatus.OK);
	}

	@GetMapping("/")
	@Operation(summary = "Get all accounts", description = "Retrieve all accounts. Admin only.")
	@Cacheable(value = "accounts")
	public ResponseEntity<List<AccountDTO>> getAllAccount() {
		authService.requireAdmin();
		return new ResponseEntity<List<AccountDTO>>(accountService.findAllAccount(), HttpStatus.OK);
	}

}
