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

	@Override
	public Claim createNewClaim(Integer policyId, ClaimDTO claim) {
		PolicyDTO policy = PolicyService.getById(policyId);
		
		// If not admin, verify the policy belongs to the current user
		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			if (!policy.getAccount().getId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot create claim for another user's policy");
			}
		}
		
		claim.setPolicy(policy);
		Claim claimd = modelMapper.map(claim, Claim.class);
		return claimRepository.save(claimd);
	}

	@Override
	public Claim getClaimById(Integer claimId) {
		Claim claim = claimRepository.findById(claimId)
				.orElseThrow(() -> new ResourceNotFoundException("Claim ", "Claim id", "" + claimId));
		
		// If not admin, verify the claim belongs to the current user
		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			if (claim.getPolicy() == null || claim.getPolicy().getAccount() == null
					|| !claim.getPolicy().getAccount().getId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot access another user's claim");
			}
		}
		
		return claim;
	}

	@Override
	public Claim updateClaim(Claim claim, Integer claimId) {
		Claim existingClaim = claimRepository.findById(claimId)
				.orElseThrow(() -> new ResourceNotFoundException("Claim ", "Claim id", "" + claimId));
		
		// If not admin, verify the claim belongs to the current user
		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			if (existingClaim.getPolicy() == null || existingClaim.getPolicy().getAccount() == null
					|| !existingClaim.getPolicy().getAccount().getId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot update another user's claim");
			}
		}
		
		Claim newClaim = modelMapper.map(claim, Claim.class);
		newClaim.setId(claimId);
		newClaim.setClaimDate(existingClaim.getClaimDate());
		return claimRepository.save(newClaim);
	}

	@Override
	public String deleteClaim(Integer claimId) {
		Claim claim = claimRepository.findById(claimId)
				.orElseThrow(() -> new ResourceNotFoundException("Claim ", "Claim id", "" + claimId));
		
		// If not admin, verify the claim belongs to the current user
		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			if (claim.getPolicy() == null || claim.getPolicy().getAccount() == null
					|| !claim.getPolicy().getAccount().getId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot delete another user's claim");
			}
		}
		
		claimRepository.delete(claim);
		return "claim info delete successfully...";
	}

	@Override
	public List<Claim> getAllClaim() {
		// If admin, return all claims
		// If regular user, return only their claims
		if (authService.isAdmin()) {
			return claimRepository.findAll();
		} else {
			// Get current user's account
			Account currentAccount = authService.getCurrentAccount();
			return claimRepository.findAll().stream()
					.filter(claim -> claim.getPolicy() != null 
							&& claim.getPolicy().getAccount() != null
							&& claim.getPolicy().getAccount().getId().equals(currentAccount.getId()))
					.collect(Collectors.toList());
		}
	}

}
