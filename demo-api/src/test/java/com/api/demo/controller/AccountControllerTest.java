package com.api.demo.controller;

import com.api.demo.dto.AccountDTO;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.service.AccountService;
import com.api.demo.service.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

	@Mock
	AccountService accountService;
	@Mock
	AuthorizationService authService;
	@InjectMocks
	AccountController accountController;

	private AccountDTO accountDTO;

	@BeforeEach
	void setUp() {
		accountDTO = new AccountDTO();
		accountDTO.setId(1);
		accountDTO.setFirstName("John");
		accountDTO.setLastName("Doe");
		accountDTO.setEmail("john@example.com");
		accountDTO.setPhoneNumber("1234567890");
		accountDTO.setAbout("Test");
		accountDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
	}

	@Test
    void getAccountById_returns200() {
        when(accountService.findById(1)).thenReturn(accountDTO);

        ResponseEntity<AccountDTO> response = accountController.getAccountById(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getEmail()).isEqualTo("john@example.com");
    }

	@Test
    void getAccountById_throws_whenNotFound() {
        when(accountService.findById(99)).thenThrow(new ResourceNotFoundException("Account", "accountId", "99"));

        assertThatThrownBy(() -> accountController.getAccountById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

	@Test
    void getAccountByEmail_returns200() {
        when(accountService.findByEmail("john@example.com")).thenReturn(accountDTO);

        ResponseEntity<AccountDTO> response = accountController.getAccountByEmail("john@example.com");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getFirstName()).isEqualTo("John");
    }

	@Test
    void getAllAccounts_returns200() {
        when(accountService.findAllAccount()).thenReturn(List.of(accountDTO));

        ResponseEntity<List<AccountDTO>> response = accountController.getAllAccount();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getEmail()).isEqualTo("john@example.com");
    }

	@Test
    void deleteAccount_returns200() {
        when(accountService.deleteAccount(1)).thenReturn("Account Data deleted successfully...");

        ResponseEntity<String> response = accountController.deleteAccount(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("deleted");
    }

	@Test
    void updateAccount_returns200() {
        when(accountService.updateAccountInfo(accountDTO, 1)).thenReturn(accountDTO);

        ResponseEntity<AccountDTO> response = accountController.updateAccount(accountDTO, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getEmail()).isEqualTo("john@example.com");
    }
}
