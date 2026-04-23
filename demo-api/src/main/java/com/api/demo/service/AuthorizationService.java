package com.api.demo.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.api.demo.entity.Account;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.repository.AccountRepository;

@Service
public class AuthorizationService {

	private final AccountRepository accountRepository;

	public AuthorizationService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public Account getCurrentAccount() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated()) {
			throw new ResourceNotFoundException("User not authenticated");
		}

		final String email;
		if (auth.getPrincipal() instanceof UserDetails) {
			email = ((UserDetails) auth.getPrincipal()).getUsername();
		} else {
			email = auth.getPrincipal().toString();
		}

		return accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Account", "email", email));
	}

	public boolean isAdmin() {
		try {
			Account account = getCurrentAccount();
			return account.getAdmin() != null && account.getAdmin();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isAdminOrOwner(Integer accountId) {
		try {
			Account current = getCurrentAccount();
			return (current.getAdmin() != null && current.getAdmin()) || current.getId().equals(accountId);
		} catch (Exception e) {
			return false;
		}
	}

	public void requireAdmin() {
		if (!isAdmin()) {
			throw new SecurityException("Admin privileges required");
		}
	}

	public void requireAdminOrOwner(Integer accountId) {
		if (!isAdminOrOwner(accountId)) {
			throw new SecurityException("Insufficient permissions");
		}
	}
}
