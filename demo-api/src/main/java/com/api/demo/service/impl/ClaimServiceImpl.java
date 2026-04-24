package com.api.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.demo.dto.ClaimDTO;
import com.api.demo.dto.PolicyDTO;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.entity.Account;
import com.api.demo.entity.Claim;
import com.api.demo.repository.ClaimRepository;
import com.api.demo.repository.PolicyRepository;
import com.api.demo.service.AuthorizationService;
import com.api.demo.service.ClaimService;
import com.api.demo.service.PolicyService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class ClaimServiceImpl implements ClaimService {

	private ClaimRepository claimRepository;
	private PolicyService PolicyService;
	private PolicyRepository PolicyRepository;
	private ModelMapper modelMapper;
	private AuthorizationService authService;

	@Autowired
	public ClaimServiceImpl(ClaimRepository claimRepository, PolicyService PolicyService,
			PolicyRepository PolicyRepository, ModelMapper modelMapper, AuthorizationService authService) {
		this.claimRepository = claimRepository;
		this.PolicyService = PolicyService;
		this.PolicyRepository = PolicyRepository;
		this.modelMapper = modelMapper;
		this.authService = authService;
	}

	@CircuitBreaker(name = "claimService", fallbackMethod = "createClaimFallback")
	@Override
	public Claim createNewClaim(Integer policyId, ClaimDTO claimDTO) {
		// Verify policy exists and get policy details
		PolicyDTO policy = PolicyService.getByIdInternal(policyId);

		// If not admin, verify the policy belongs to the current user
		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			if (policy.getAccountId() == null || !policy.getAccountId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot create claim for another user's policy");
			}
		}

		// Create claim with policyId
		Claim claim = Claim.builder().description(claimDTO.getDescription()).claimDate(claimDTO.getClaimDate())
				.claimStatus(claimDTO.getClaimStatus()).policyId(policyId).build();

		return claimRepository.save(claim);
	}

	@CircuitBreaker(name = "claimService", fallbackMethod = "getClaimFallback")
	@Override
	public Claim getClaimById(Integer claimId) {
		Claim claim = claimRepository.findById(claimId)
				.orElseThrow(() -> new ResourceNotFoundException("Claim ", "Claim id", "" + claimId));

		// If not admin, verify the claim belongs to the current user
		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			// Get policy to check ownership
			PolicyDTO policy = PolicyService.getByIdInternal(claim.getPolicyId());
			if (policy.getAccountId() == null || !policy.getAccountId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot access another user's claim");
			}
		}

		return claim;
	}

	@CircuitBreaker(name = "claimService", fallbackMethod = "updateClaimFallback")
	@Override
	public Claim updateClaim(Claim claim, Integer claimId) {
		Claim existingClaim = claimRepository.findById(claimId)
				.orElseThrow(() -> new ResourceNotFoundException("Claim ", "Claim id", "" + claimId));

		// If not admin, verify the claim belongs to the current user
		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			// Get policy to check ownership
			PolicyDTO policy = PolicyService.getByIdInternal(existingClaim.getPolicyId());
			if (policy.getAccountId() == null || !policy.getAccountId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot update another user's claim");
			}
		}

		// Update fields
		existingClaim.setDescription(claim.getDescription());
		existingClaim.setClaimStatus(claim.getClaimStatus());
		// Keep original claim date

		return claimRepository.save(existingClaim);
	}

	@CircuitBreaker(name = "claimService", fallbackMethod = "partialUpdateClaimFallback")
	@Override
	public Claim partialUpdateClaim(ClaimDTO claimDTO, Integer claimId) {
		Claim existingClaim = claimRepository.findById(claimId)
				.orElseThrow(() -> new ResourceNotFoundException("Claim ", "Claim id", "" + claimId));

		// If not admin, verify the claim belongs to the current user
		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			// Get policy to check ownership
			PolicyDTO policy = PolicyService.getByIdInternal(existingClaim.getPolicyId());
			if (policy.getAccountId() == null || !policy.getAccountId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot update another user's claim");
			}
		}

		// Only update fields that are provided (non-null)
		if (claimDTO.getDescription() != null) {
			existingClaim.setDescription(claimDTO.getDescription());
		}
		if (claimDTO.getClaimStatus() != null) {
			existingClaim.setClaimStatus(claimDTO.getClaimStatus());
		}
		// claimDate and policyId cannot be changed

		return claimRepository.save(existingClaim);
	}

	@CircuitBreaker(name = "claimService", fallbackMethod = "deleteClaimFallback")
	@Override
	public String deleteClaim(Integer claimId) {
		Claim claim = claimRepository.findById(claimId)
				.orElseThrow(() -> new ResourceNotFoundException("Claim ", "Claim id", "" + claimId));

		// If not admin, verify the claim belongs to the current user
		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			// Get policy to check ownership
			PolicyDTO policy = PolicyService.getByIdInternal(claim.getPolicyId());
			if (policy.getAccountId() == null || !policy.getAccountId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot delete another user's claim");
			}
		}

		claimRepository.delete(claim);
		return "claim info delete successfully...";
	}

	@CircuitBreaker(name = "claimService", fallbackMethod = "getAllClaimsFallback")
	@Override
	public List<Claim> getAllClaim() {
		// If admin, return all claims
		// If regular user, return only their claims
		if (authService.isAdmin()) {
			return claimRepository.findAll();
		} else {
			// Get current user's account
			Account currentAccount = authService.getCurrentAccount();
			// Filter claims by checking each policy's ownership
			return claimRepository.findAll().stream().filter(claim -> {
				try {
					PolicyDTO policy = PolicyService.getByIdInternal(claim.getPolicyId());
					return policy.getAccountId() != null && policy.getAccountId().equals(currentAccount.getId());
				} catch (Exception e) {
					return false;
				}
			}).collect(Collectors.toList());
		}
	}

	// Fallback methods for Circuit Breaker
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
