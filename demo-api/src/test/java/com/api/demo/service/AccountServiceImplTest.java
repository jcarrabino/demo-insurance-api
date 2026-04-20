package com.api.demo.service;

import com.api.demo.dto.AccountDTO;
import com.api.demo.entity.Account;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.repository.AccountRepository;
import com.api.demo.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

	@Mock
	AccountRepository accountRepository;
	@Mock
	PasswordEncoder passwordEncoder;
	@Mock
	ModelMapper modelMapper;

	@InjectMocks
	AccountServiceImpl accountService;

	private Account account;
	private AccountDTO accountDTO;

	@BeforeEach
	void setUp() {
		account = new Account();
		account.setId(1);
		account.setFirstName("John");
		account.setLastName("Doe");
		account.setEmail("john@example.com");
		account.setPassword("encoded");
		account.setDateOfBirth(LocalDate.of(1990, 1, 1));

		accountDTO = new AccountDTO();
		accountDTO.setFirstName("John");
		accountDTO.setLastName("Doe");
		accountDTO.setEmail("john@example.com");
		accountDTO.setPassword("Password1@");
		accountDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
		accountDTO.setPhoneNumber("1234567890");
	}

	@Test
    void addAccount_savesAndReturnsDTO() {
        when(modelMapper.map(accountDTO, Account.class)).thenReturn(account);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(accountRepository.save(account)).thenReturn(account);
        when(modelMapper.map(account, AccountDTO.class)).thenReturn(accountDTO);

        AccountDTO result = accountService.addAccount(accountDTO);

        assertThat(result).isNotNull();
        verify(accountRepository).save(account);
    }

	@Test
	void addAccount_throwsWhenDobIsInFuture() {
		accountDTO.setDateOfBirth(LocalDate.now().plusDays(1));

		assertThatThrownBy(() -> accountService.addAccount(accountDTO)).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
    void findById_returnsDTO_whenFound() {
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));
        when(modelMapper.map(account, AccountDTO.class)).thenReturn(accountDTO);

        AccountDTO result = accountService.findById(1);

        assertThat(result.getEmail()).isEqualTo("john@example.com");
    }

	@Test
    void findById_throws_whenNotFound() {
        when(accountRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.findById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

	@Test
    void findByEmail_returnsDTO_whenFound() {
        when(accountRepository.findByEmail("john@example.com")).thenReturn(Optional.of(account));
        when(modelMapper.map(account, AccountDTO.class)).thenReturn(accountDTO);

        AccountDTO result = accountService.findByEmail("john@example.com");

        assertThat(result.getEmail()).isEqualTo("john@example.com");
    }

	@Test
    void findByEmail_throws_whenNotFound() {
        when(accountRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.findByEmail("missing@example.com"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

	@Test
    void findAllAccount_returnsList() {
        when(accountRepository.findAll()).thenReturn(List.of(account));
        when(modelMapper.map(account, AccountDTO.class)).thenReturn(accountDTO);

        List<AccountDTO> result = accountService.findAllAccount();

        assertThat(result).hasSize(1);
    }

	@Test
    void deleteAccount_deletesAndReturnsMessage() {
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        String result = accountService.deleteAccount(1);

        verify(accountRepository).delete(account);
        assertThat(result).contains("deleted");
    }

	@Test
    void deleteAccount_throws_whenNotFound() {
        when(accountRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.deleteAccount(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

	@Test
	void partialUpdateAccountInfo_allowsEmailUpdate() {
		// Email updates are now allowed in partial updates
		accountDTO.setEmail("newemail@example.com");
		accountDTO.setFirstName("UpdatedName");

		Account updatedAccount = new Account();
		updatedAccount.setId(1);
		updatedAccount.setFirstName("UpdatedName");
		updatedAccount.setLastName("Doe");
		updatedAccount.setEmail("john@example.com");
		updatedAccount.setPassword("encoded");
		updatedAccount.setDateOfBirth(LocalDate.of(1990, 1, 1));
		updatedAccount.setPhoneNumber("1234567890");

		AccountDTO updatedDTO = new AccountDTO();
		updatedDTO.setFirstName("UpdatedName");
		updatedDTO.setLastName("Doe");
		updatedDTO.setEmail("john@example.com");
		updatedDTO.setPassword("Password1@");
		updatedDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
		updatedDTO.setPhoneNumber("1234567890");

		when(accountRepository.findById(1)).thenReturn(Optional.of(account));
		when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);
		when(modelMapper.map(updatedAccount, AccountDTO.class)).thenReturn(updatedDTO);

		AccountDTO result = accountService.partialUpdateAccountInfo(accountDTO, 1);

		assertThat(result).isNotNull();
		assertThat(result.getFirstName()).isEqualTo("UpdatedName");
		verify(accountRepository).save(any(Account.class));
	}
}
