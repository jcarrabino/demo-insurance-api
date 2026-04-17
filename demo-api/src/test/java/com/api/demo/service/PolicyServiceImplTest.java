package com.api.demo.service;

import com.api.demo.dto.AccountDTO;
import com.api.demo.dto.PolicyDTO;
import com.api.demo.entity.Account;
import com.api.demo.entity.Policy;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.repository.PolicyRepository;
import com.api.demo.service.impl.PolicyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyServiceImplTest {

	@Mock
	PolicyRepository policyRepository;
	@Mock
	AccountService accountService;
	@Mock
	ModelMapper modelMapper;

	@InjectMocks
	PolicyServiceImpl policyService;

	private Policy policy;
	private PolicyDTO policyDTO;
	private Account account;
	private AccountDTO accountDTO;

	@BeforeEach
	void setUp() {
		account = new Account();
		account.setId(1);

		accountDTO = new AccountDTO();
		accountDTO.setId(1);

		policy = new Policy();
		policy.setId(1);
		policy.setPremium(BigDecimal.valueOf(500));
		policy.setStartDate(LocalDate.now());
		policy.setExpiryDate(LocalDate.now().plusYears(1));

		policyDTO = new PolicyDTO();
		policyDTO.setId(1);
		policyDTO.setPolicyNumber("POL-001");
		policyDTO.setPolicyType("Health");
		policyDTO.setCoverageAmount(BigDecimal.valueOf(10000));
		policyDTO.setPremium(BigDecimal.valueOf(500));
		policyDTO.setStartDate(LocalDate.now());
		policyDTO.setEndDate(LocalDate.now().plusYears(1));
	}

	@Test
    void createNewPolicy_savesAndReturnsDTO() {
        when(accountService.findById(1)).thenReturn(accountDTO);
        when(modelMapper.map(accountDTO, Account.class)).thenReturn(account);
        when(modelMapper.map(policyDTO, Policy.class)).thenReturn(policy);
        when(policyRepository.save(policy)).thenReturn(policy);
        when(modelMapper.map(policy, PolicyDTO.class)).thenReturn(policyDTO);

        PolicyDTO result = policyService.createNewPolicy(1, policyDTO);

        assertThat(result).isNotNull();
        verify(policyRepository).save(policy);
    }

	@Test
    void getById_returnsDTO_whenFound() {
        when(policyRepository.findById(1)).thenReturn(Optional.of(policy));
        when(modelMapper.map(policy, PolicyDTO.class)).thenReturn(policyDTO);

        PolicyDTO result = policyService.getById(1);

        assertThat(result.getPolicyNumber()).isEqualTo("POL-001");
    }

	@Test
    void getById_throws_whenNotFound() {
        when(policyRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> policyService.getById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

	@Test
    void updatePolicy_updatesAndReturnsDTO() {
        when(policyRepository.findById(1)).thenReturn(Optional.of(policy));
        when(modelMapper.map(policyDTO, Policy.class)).thenReturn(policy);
        when(policyRepository.save(policy)).thenReturn(policy);
        when(modelMapper.map(policy, PolicyDTO.class)).thenReturn(policyDTO);

        PolicyDTO result = policyService.updatePolicy(policyDTO, 1);

        assertThat(result).isNotNull();
        verify(policyRepository).save(policy);
    }

	@Test
    void updatePolicy_throws_whenNotFound() {
        when(policyRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> policyService.updatePolicy(policyDTO, 99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

	@Test
    void deletePolicy_deletesAndReturnsMessage() {
        when(policyRepository.findById(1)).thenReturn(Optional.of(policy));

        String result = policyService.deletePolicy(1);

        verify(policyRepository).delete(policy);
        assertThat(result).contains("deleted");
    }

	@Test
    void deletePolicy_throws_whenNotFound() {
        when(policyRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> policyService.deletePolicy(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

	@Test
    void getAllPolicy_returnsList() {
        when(policyRepository.findAll()).thenReturn(List.of(policy));
        when(modelMapper.map(policy, PolicyDTO.class)).thenReturn(policyDTO);

        List<PolicyDTO> result = policyService.getAllPolicy();

        assertThat(result).hasSize(1);
    }
}
