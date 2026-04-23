package com.api.demo.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.demo.dto.AccountDTO;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.entity.Account;
import com.api.demo.repository.AccountRepository;
import com.api.demo.service.AccountService;
import com.api.demo.utils.PasswordValidator;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	private final AccountRepository clientRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;

	public AccountServiceImpl(AccountRepository clientRepository, PasswordEncoder passwordEncoder, 
			ModelMapper modelMapper) {
		this.clientRepository = clientRepository;
		this.passwordEncoder = passwordEncoder;
		this.modelMapper = modelMapper;
	}

	@CircuitBreaker(name = "accountService", fallbackMethod = "addAccountFallback")
	@Override
	public AccountDTO addAccount(AccountDTO account) {
		if (LocalDate.now().isBefore(account.getDateOfBirth()))
			throw new ResourceNotFoundException("Date is not valid plese provide past date...");

		// Validate password
		PasswordValidator.validatePassword(account.getPassword());

		Account customerData = modelMapper.map(account, Account.class);
		customerData.setPassword(passwordEncoder.encode(customerData.getPassword()));
		Account savedAccount = clientRepository.save(customerData);
		return modelMapper.map(savedAccount, AccountDTO.class);
	}

	@CircuitBreaker(name = "accountService", fallbackMethod = "findByIdFallback")
	@Override
	@Transactional(readOnly = true)
	public AccountDTO findById(Integer id) {
		Account account = clientRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Account ", "accountId", "" + id));
		return modelMapper.map(account, AccountDTO.class);
	}

	@CircuitBreaker(name = "accountService", fallbackMethod = "findByEmailFallback")
	@Override
	@Transactional(readOnly = true)
	public AccountDTO findByEmail(String email) {
		Account account = clientRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Account", "account email", email));
		return modelMapper.map(account, AccountDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	@Deprecated(forRemoval = true)
	public List<AccountDTO> findAllAccount() {
		List<Account> clients = clientRepository.findAll();
		return clients.stream().map(account -> modelMapper.map(account, AccountDTO.class)).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AccountDTO> findAllAccounts(Pageable pageable) {
		return clientRepository.findAll(pageable).map(account -> modelMapper.map(account, AccountDTO.class));
	}

	@CircuitBreaker(name = "accountService", fallbackMethod = "updateAccountInfoFallback")
	@Override
	public AccountDTO updateAccountInfo(AccountDTO account, Integer accountId) {
		Account prevAccount = clientRepository.findById(accountId)
				.orElseThrow(() -> new ResourceNotFoundException("Account ", "accountId", "" + accountId));

		// Validate password
		PasswordValidator.validatePassword(account.getPassword());

		Account updatedAccount = modelMapper.map(account, Account.class);
		updatedAccount.setId(accountId);
		updatedAccount.setEmail(prevAccount.getEmail());
		updatedAccount.setPassword(passwordEncoder.encode(updatedAccount.getPassword()));
		Account updatedAccountdata = clientRepository.save(updatedAccount);
		return modelMapper.map(updatedAccountdata, AccountDTO.class);
	}

	@CircuitBreaker(name = "accountService", fallbackMethod = "partialUpdateAccountInfoFallback")
	@Override
	public AccountDTO partialUpdateAccountInfo(AccountDTO account, Integer accountId) {
		Account existingAccount = clientRepository.findById(accountId)
				.orElseThrow(() -> new ResourceNotFoundException("Account ", "accountId", "" + accountId));

		// Only update fields that are provided (non-null)
		if (account.getFirstName() != null) {
			existingAccount.setFirstName(account.getFirstName());
		}
		if (account.getMiddleName() != null) {
			existingAccount.setMiddleName(account.getMiddleName());
		}
		if (account.getLastName() != null) {
			existingAccount.setLastName(account.getLastName());
		}
		if (account.getPhoneNumber() != null) {
			existingAccount.setPhoneNumber(account.getPhoneNumber());
		}
		if (account.getDateOfBirth() != null) {
			if (LocalDate.now().isBefore(account.getDateOfBirth())) {
				throw new ResourceNotFoundException("Date is not valid please provide past date...");
			}
			existingAccount.setDateOfBirth(account.getDateOfBirth());
		}
		if (account.getPassword() != null && !account.getPassword().isEmpty()) {
			// Validate password before updating
			PasswordValidator.validatePassword(account.getPassword());
			existingAccount.setPassword(passwordEncoder.encode(account.getPassword()));
		}
		if (account.getAddress() != null) {
			existingAccount.setAddress(account.getAddress());
		}
		if (account.getAdmin() != null) {
			existingAccount.setAdmin(account.getAdmin());
		}

		// Email cannot be changed (unique identifier)
		Account updatedAccount = clientRepository.save(existingAccount);
		return modelMapper.map(updatedAccount, AccountDTO.class);
	}

	@CircuitBreaker(name = "accountService", fallbackMethod = "deleteAccountFallback")
	@Override
	public String deleteAccount(Integer id) {
		Account account = clientRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Account ", "accountId", "" + id));
		clientRepository.delete(account);

		return "Account Data deleted successfully...";

	}

	// Fallback methods for Circuit Breaker
	public AccountDTO addAccountFallback(AccountDTO account, Exception e) {
		throw new RuntimeException("Account service is currently unavailable. Please try again later.", e);
	}

	public AccountDTO findByIdFallback(Integer id, Exception e) {
		throw new RuntimeException("Account service is currently unavailable. Please try again later.", e);
	}

	public AccountDTO findByEmailFallback(String email, Exception e) {
		throw new RuntimeException("Account service is currently unavailable. Please try again later.", e);
	}

	public AccountDTO updateAccountInfoFallback(AccountDTO account, Integer accountId, Exception e) {
		throw new RuntimeException("Account service is currently unavailable. Please try again later.", e);
	}

	public AccountDTO partialUpdateAccountInfoFallback(AccountDTO account, Integer accountId, Exception e) {
		throw new RuntimeException("Account service is currently unavailable. Please try again later.", e);
	}

	public String deleteAccountFallback(Integer id, Exception e) {
		throw new RuntimeException("Account service is currently unavailable. Please try again later.", e);
	}

}
