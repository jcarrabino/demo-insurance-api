package com.api.demo.controller;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.demo.dto.AccountDTO;
import com.api.demo.model.ApiResponse;
import com.api.demo.model.PagedResponse;
import com.api.demo.service.AccountService;
import com.api.demo.service.AuthorizationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "Account Management", description = "APIs for managing user accounts")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

	private final AccountService accountService;
	private final AuthorizationService authService;

	public AccountController(AccountService accountService, AuthorizationService authService) {
		this.accountService = accountService;
		this.authService = authService;
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Update account", description = "Partial update of an account. Only provided fields are updated. Requires admin or account owner.")
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Account updated successfully"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Account not found")})
	@PreAuthorize("hasRole('ADMIN') or @authorizationService.isOwner(#id)")
	@CacheEvict(value = "accounts", allEntries = true)
	public ResponseEntity<ApiResponse<AccountDTO>> updateAccount(@RequestBody AccountDTO account,
			@PathVariable Integer id) {
		// Prevent non-admins from setting admin flag
		if (account.getAdmin() != null && account.getAdmin() && !authService.isAdmin()) {
			throw new SecurityException("Only admins can create admin users");
		}
		AccountDTO updated = accountService.partialUpdateAccountInfo(account, id);
		return new ResponseEntity<>(ApiResponse.success(updated, "Account updated successfully"), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete account", description = "Delete an account. Requires admin or account owner.")
	@PreAuthorize("hasRole('ADMIN') or @authorizationService.isOwner(#id)")
	@CacheEvict(value = "accounts", allEntries = true)
	public ResponseEntity<ApiResponse<String>> deleteAccount(@PathVariable Integer id) {
		String result = accountService.deleteAccount(id);
		return new ResponseEntity<>(ApiResponse.success(result, "Account deleted successfully"), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get account by ID", description = "Retrieve account details by ID. Requires admin or account owner.")
	@PreAuthorize("hasRole('ADMIN') or @authorizationService.isOwner(#id)")
	@Cacheable(value = "accounts", key = "#id")
	@Transactional(readOnly = true)
	public ResponseEntity<ApiResponse<AccountDTO>> getAccountById(@PathVariable Integer id) {
		AccountDTO account = accountService.findById(id);
		return new ResponseEntity<>(ApiResponse.success(account), HttpStatus.OK);
	}

	@GetMapping("/email/{email}")
	@Operation(summary = "Get account by email", description = "Retrieve account details by email.")
	@Cacheable(value = "accounts", key = "#email")
	@Transactional(readOnly = true)
	public ResponseEntity<ApiResponse<AccountDTO>> getAccountByEmail(@PathVariable String email) {
		AccountDTO account = accountService.findByEmail(email);
		return new ResponseEntity<>(ApiResponse.success(account), HttpStatus.OK);
	}

	@GetMapping
	@Operation(summary = "Get all accounts", description = "Retrieve all accounts with pagination. Admin only.")
	@PreAuthorize("hasRole('ADMIN')")
	@Cacheable(value = "accounts", key = "#page + '-' + #size")
	@Transactional(readOnly = true)
	public ResponseEntity<ApiResponse<PagedResponse<AccountDTO>>> getAllAccounts(
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<AccountDTO> accounts = accountService.findAllAccounts(pageable);
		PagedResponse<AccountDTO> response = PagedResponse.from(accounts);
		return new ResponseEntity<>(ApiResponse.success(response), HttpStatus.OK);
	}

}
