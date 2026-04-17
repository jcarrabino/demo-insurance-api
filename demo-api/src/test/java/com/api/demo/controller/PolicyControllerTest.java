package com.api.demo.controller;

import com.api.demo.dto.PolicyDTO;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.service.AuthorizationService;
import com.api.demo.service.PolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyControllerTest {

	@Mock
	PolicyService policyService;
	@Mock
	AuthorizationService authService;
	@InjectMocks
	PolicyController policyController;

	private PolicyDTO policyDTO;

	@BeforeEach
	void setUp() {
		policyDTO = new PolicyDTO();
		policyDTO.setId(1);
		policyDTO.setLineId(1);
		policyDTO.setPremium(BigDecimal.valueOf(500));
		policyDTO.setStartDate(LocalDate.now());
		policyDTO.setEndDate(LocalDate.now().plusYears(1));
	}

	@Test
    void createPolicy_returns201() {
        when(policyService.createNewPolicy(1, policyDTO)).thenReturn(policyDTO);

        ResponseEntity<PolicyDTO> response = policyController.createdPolicy(policyDTO, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getId()).isEqualTo(1);
    }

	@Test
    void getPolicy_returns200() {
        when(policyService.getById(1)).thenReturn(policyDTO);

        ResponseEntity<PolicyDTO> response = policyController.getPolicy(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getLineId()).isEqualTo(1);
    }

	@Test
    void getPolicy_throws_whenNotFound() {
        when(policyService.getById(99)).thenThrow(new ResourceNotFoundException("Insurance Policy", "policy", "99"));

        assertThatThrownBy(() -> policyController.getPolicy(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

	@Test
    void getAllPolicies_returns200() {
        when(policyService.getAllPolicy()).thenReturn(List.of(policyDTO));

        ResponseEntity<List<PolicyDTO>> response = policyController.getAllPolicy();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

	@Test
    void updatePolicy_returns200() {
        when(policyService.updatePolicy(policyDTO, 1)).thenReturn(policyDTO);

        ResponseEntity<PolicyDTO> response = policyController.updatePolicy(policyDTO, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getPremium()).isEqualByComparingTo(BigDecimal.valueOf(500));
    }

	@Test
    void deletePolicy_returns200() {
        when(policyService.deletePolicy(1)).thenReturn("Insurance policy deleted successfully...");

        ResponseEntity<String> response = policyController.deletePolicy(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("deleted");
    }
}
