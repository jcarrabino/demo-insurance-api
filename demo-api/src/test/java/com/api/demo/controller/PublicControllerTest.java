package com.api.demo.controller;

import com.api.demo.dto.AccountDTO;
import com.api.demo.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublicControllerTest {

	@Mock
	AccountService accountService;
	@Mock
	Authentication authentication;
	@InjectMocks
	PublicController publicController;

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
		accountDTO.setPassword("Password1@");
	}

	@Test
	void welcome_returnsMessage() {
		String result = publicController.welcomeHandeler();

		assertThat(result).isEqualTo("Welcome to Insurance manegement system");
	}

	@Test
    void register_returns201() {
        when(accountService.addAccount(accountDTO)).thenReturn(accountDTO);

        ResponseEntity<AccountDTO> response = publicController.createClient(accountDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getEmail()).isEqualTo("john@example.com");
    }

	@Test
    void login_returns200() {
        when(authentication.getName()).thenReturn("john@example.com");
        when(accountService.findByEmail("john@example.com")).thenReturn(accountDTO);

        ResponseEntity<AccountDTO> response = publicController.getLoggedInClientDetailsHandler(authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getFirstName()).isEqualTo("John");
    }
}
