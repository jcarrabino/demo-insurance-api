package com.api.demo.controller;

import com.api.demo.dto.ClaimDTO;
import com.api.demo.entity.Claim;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.model.ClaimStatus;
import com.api.demo.service.AuthorizationService;
import com.api.demo.service.ClaimService;
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
class ClaimControllerTest {

	@Mock
	ClaimService claimService;
	@Mock
	AuthorizationService authService;
	@InjectMocks
	ClaimController claimController;

	private Claim claim;
	private ClaimDTO claimDTO;

	@BeforeEach
	void setUp() {
		claim = new Claim();
		claim.setId(1);
		claim.setClaimNumber("CLM-001");
		claim.setDescription("Accident claim");
		claim.setClaimDate(LocalDate.now());
		claim.setClaimStatus(ClaimStatus.SUBMITTED);

		claimDTO = new ClaimDTO();
		claimDTO.setClaimNumber("CLM-001");
		claimDTO.setDescription("Accident claim");
		claimDTO.setClaimDate(LocalDate.now());
		claimDTO.setClaimStatus(ClaimStatus.SUBMITTED);
	}

	@Test
    void createClaim_returns201() {
        when(claimService.createNewClaim(1, claimDTO)).thenReturn(claim);

        ResponseEntity<Claim> response = claimController.createClaim(1, claimDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getClaimNumber()).isEqualTo("CLM-001");
    }

	@Test
    void getClaimById_returns200() {
        when(claimService.getClaimById(1)).thenReturn(claim);

        ResponseEntity<Claim> response = claimController.getClaimById(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getClaimStatus()).isEqualTo(ClaimStatus.SUBMITTED);
    }

	@Test
    void getClaimById_throws_whenNotFound() {
        when(claimService.getClaimById(99)).thenThrow(new ResourceNotFoundException("Claim", "Claim id", "99"));

        assertThatThrownBy(() -> claimController.getClaimById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

	@Test
    void getAllClaims_returns200() {
        when(claimService.getAllClaim()).thenReturn(List.of(claim));

        ResponseEntity<List<Claim>> response = claimController.getAllClaims();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

	@Test
    void updateClaim_returns200() {
        when(claimService.updateClaim(claim, 1)).thenReturn(claim);

        ResponseEntity<Claim> response = claimController.updateClaim(claim, 1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getDescription()).isEqualTo("Accident claim");
    }

	@Test
    void deleteClaim_returns200() {
        when(claimService.deleteClaim(1)).thenReturn("claim info delete successfully...");

        ResponseEntity<String> response = claimController.deleteClaimById(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("delete");
    }
}
