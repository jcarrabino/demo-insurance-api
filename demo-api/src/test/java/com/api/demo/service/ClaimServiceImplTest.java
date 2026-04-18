package com.api.demo.service;

import com.api.demo.dto.ClaimDTO;
import com.api.demo.dto.PolicyDTO;
import com.api.demo.entity.Claim;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.model.ClaimStatus;
import com.api.demo.repository.ClaimRepository;
import com.api.demo.repository.PolicyRepository;
import com.api.demo.service.impl.ClaimServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaimServiceImplTest {

	@Mock
	ClaimRepository claimRepository;
	@Mock
	PolicyService policyService;
	@Mock
	PolicyRepository policyRepository;
	@Mock
	AuthorizationService authService;
	@Mock
	ModelMapper modelMapper;

	@InjectMocks
	ClaimServiceImpl claimService;

	private Claim claim;
	private ClaimDTO claimDTO;
	private PolicyDTO policyDTO;

	@BeforeEach
	void setUp() {
		claim = new Claim();
		claim.setId(1);
		claim.setClaimNumber("CLM-001");
		claim.setDescription("Test claim");
		claim.setClaimDate(LocalDate.now());
		claim.setClaimStatus(ClaimStatus.SUBMITTED);

		claimDTO = new ClaimDTO();
		claimDTO.setClaimNumber("CLM-001");
		claimDTO.setDescription("Test claim");
		claimDTO.setClaimDate(LocalDate.now());
		claimDTO.setClaimStatus(ClaimStatus.SUBMITTED);

		policyDTO = new PolicyDTO();
		policyDTO.setId(1);
	}

	@Test
    void createNewClaim_savesAndReturnsClaim() {
        when(policyService.getById(1)).thenReturn(policyDTO);
        when(authService.isAdmin()).thenReturn(true);
        when(claimRepository.save(any(Claim.class))).thenReturn(claim);

        Claim result = claimService.createNewClaim(1, claimDTO);

        assertThat(result).isNotNull();
        assertThat(result.getClaimNumber()).isEqualTo("CLM-001");
        verify(claimRepository).save(any(Claim.class));
    }

	@Test
    void getClaimById_returnsClaim_whenFound() {
        when(claimRepository.findById(1)).thenReturn(Optional.of(claim));
        when(authService.isAdmin()).thenReturn(true);

        Claim result = claimService.getClaimById(1);

        assertThat(result.getId()).isEqualTo(1);
    }

	@Test
    void getClaimById_throws_whenNotFound() {
        when(claimRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> claimService.getClaimById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

	@Test
    void updateClaim_updatesAndReturnsClaim() {
        when(claimRepository.findById(1)).thenReturn(Optional.of(claim));
        when(authService.isAdmin()).thenReturn(true);
        when(claimRepository.save(any(Claim.class))).thenReturn(claim);

        Claim result = claimService.updateClaim(claim, 1);

        assertThat(result).isNotNull();
        verify(claimRepository).save(any(Claim.class));
    }

	@Test
    void updateClaim_throws_whenNotFound() {
        when(claimRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> claimService.updateClaim(claim, 99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

	@Test
    void deleteClaim_deletesAndReturnsMessage() {
        when(claimRepository.findById(1)).thenReturn(Optional.of(claim));
        when(authService.isAdmin()).thenReturn(true);

        String result = claimService.deleteClaim(1);

        verify(claimRepository).delete(claim);
        assertThat(result).contains("delete");
    }

	@Test
    void deleteClaim_throws_whenNotFound() {
        when(claimRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> claimService.deleteClaim(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

	@Test
    void getAllClaim_returnsList() {
        when(authService.isAdmin()).thenReturn(true);
        when(claimRepository.findAll()).thenReturn(List.of(claim));

        List<Claim> result = claimService.getAllClaim();

        assertThat(result).hasSize(1);
    }
}
