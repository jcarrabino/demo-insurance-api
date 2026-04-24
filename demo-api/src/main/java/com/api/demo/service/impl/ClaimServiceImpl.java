package com.api.demo.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.api.demo.dto.ClaimDTO;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.entity.Account;
import com.api.demo.entity.Claim;
import com.api.demo.entity.Policy;
import com.api.demo.repository.ClaimRepository;
import com.api.demo.repository.PolicyRepository;
import com.api.demo.service.AuthorizationService;
import com.api.demo.service.ClaimService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ClaimServiceImpl implements ClaimService {

	private final ClaimRepository claimRepository;
	private final PolicyRepository policyRepository;
	private final ModelMapper modelMapper;
	private final AuthorizationService authService;

	public ClaimServiceImpl(ClaimRepository claimRepository, PolicyRepository policyRepository,
			ModelMapper modelMapper, AuthorizationService authService) {
		this.claimRepository = claimRepository;
		this.policyRepository = policyRepository;
		this.modelMapper = modelMapper;
		this.authService = authService;
	}

	@CircuitBreaker(name = "claimService", fallbackMethod = "createClaimFallback")
	@Override
	public Claim createNewClaim(Integer policyId, ClaimDTO claimDTO) {
		Policy policy = policyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException("Policy", "id", "" + policyId));

		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			if (!policy.getAccount().getId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot create claim for another user's policy");
			}
		}

		Claim claim = Claim.builder()
				.description(claimDTO.getDescription())
				.claimDate(claimDTO.getClaimDate())
				.claimStatus(claimDTO.getClaimStatus())
				.policy(policy)
				.build();

		return claimRepository.save(claim);
	}

	@CircuitBreaker(name = "claimService", fallbackMethod = "getClaimFallback")
	@Override
	public Claim getClaimById(Integer claimId) {
		Claim claim = claimRepository.findById(claimId)
				.orElseThrow(() -> new ResourceNotFoundException("Claim", "id", "" + claimId));

		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			if (!claim.getPolicy().getAccount().getId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot access another user's claim");
			}
		}

		return claim;
	}

	@CircuitBreaker(name = "claimService", fallbackMethod = "updateClaimFallback")
	@Override
	public Claim updateClaim(Claim claim, Integer claimId) {
		Claim existingClaim = claimRepository.findById(claimId)
				.orElseThrow(() -> new ResourceNotFoundException("Claim", "id", "" + claimId));

		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			if (!existingClaim.getPolicy().getAccount().getId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot update another user's claim");
			}
		}

		existingClaim.setDescription(claim.getDescription());
		existingClaim.setClaimStatus(claim.getClaimStatus());

		return claimRepository.save(existingClaim);
	}

	@CircuitBreaker(name = "claimService", fallbackMethod = "partialUpdateClaimFallback")
	@Override
	public Claim partialUpdateClaim(ClaimDTO claimDTO, Integer claimId) {
		Claim existingClaim = claimRepository.findById(claimId)
				.orElseThrow(() -> new ResourceNotFoundException("Claim", "id", "" + claimId));

		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			if (!existingClaim.getPolicy().getAccount().getId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot update another user's claim");
			}
		}

		if (claimDTO.getDescription() != null) {
			existingClaim.setDescription(claimDTO.getDescription());
		}
		if (claimDTO.getClaimStatus() != null) {
			existingClaim.setClaimStatus(claimDTO.getClaimStatus());
		}

		return claimRepository.save(existingClaim);
	}

	@CircuitBreaker(name = "claimService", fallbackMethod = "deleteClaimFallback")
	@Override
	public String deleteClaim(Integer claimId) {
		Claim claim = claimRepository.findById(claimId)
				.orElseThrow(() -> new ResourceNotFoundException("Claim", "id", "" + claimId));

		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			if (!claim.getPolicy().getAccount().getId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot delete another user's claim");
			}
		}

		claimRepository.delete(claim);
		return "claim info delete successfully...";
	}

	@CircuitBreaker(name = "claimService", fallbackMethod = "getAllClaimsFallback")
	@Override
	public List<Claim> getAllClaim() {
		if (authService.isAdmin()) {
			return claimRepository.findAll();
		}

		Account currentAccount = authService.getCurrentAccount();
		List<Policy> userPolicies = policyRepository.findByAccount(currentAccount);
		return claimRepository.findByPolicyIn(userPolicies);
	}

	// Fallback methods
	public Claim createClaimFallback(Integer policyId, ClaimDTO claimDTO, Exception e) {
		throw new RuntimeException("Claim service is currently unavailable. Please try again later.", e);
	}

	public Claim getClaimFallback(Integer claimId, Exception e) {
		throw new RuntimeException("Claim service is currently unavailable. Please try again later.", e);
	}

	public Claim updateClaimFallback(Claim claim, Integer claimId, Exception e) {
		throw new RuntimeException("Claim service is currently unavailable. Please try again later.", e);
	}

	public Claim partialUpdateClaimFallback(ClaimDTO claimDTO, Integer claimId, Exception e) {
		throw new RuntimeException("Claim service is currently unavailable. Please try again later.", e);
	}

	public String deleteClaimFallback(Integer claimId, Exception e) {
		throw new RuntimeException("Claim service is currently unavailable. Please try again later.", e);
	}

	public List<Claim> getAllClaimsFallback(Exception e) {
		throw new RuntimeException("Claim service is currently unavailable. Please try again later.", e);
	}

}
