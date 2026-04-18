package com.api.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.demo.dto.PolicyDTO;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.entity.Account;
import com.api.demo.entity.Policy;
import com.api.demo.repository.LineRepository;
import com.api.demo.repository.PolicyRepository;
import com.api.demo.service.AccountService;
import com.api.demo.service.AuthorizationService;
import com.api.demo.service.PolicyService;

import jakarta.transaction.Transactional;

@Service
public class PolicyServiceImpl implements PolicyService {

	@Autowired
	private PolicyRepository policyRepository;

	@Autowired
	private LineRepository lineRepository;

	@Autowired
	private AccountService accountService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AuthorizationService authService;

	@Override
	public PolicyDTO createNewPolicy(Integer clientId, PolicyDTO policyDTO) {
		// Verify account exists
		accountService.findById(clientId);
		
		// Verify line exists
		lineRepository.findById(policyDTO.getLineId())
				.orElseThrow(() -> new ResourceNotFoundException("Line", "id", String.valueOf(policyDTO.getLineId())));
		
		Policy policy = Policy.builder()
				.lineId(policyDTO.getLineId())
				.accountId(clientId)
				.premium(policyDTO.getPremium())
				.startDate(policyDTO.getStartDate())
				.expiryDate(policyDTO.getEndDate())
				.build();
		
		Policy savedPolicy = policyRepository.save(policy);
		PolicyDTO result = modelMapper.map(savedPolicy, PolicyDTO.class);
		
		// Populate account details
		result.setAccount(accountService.findById(clientId));
		
		return result;
	}

	@Transactional
	@Override
	public PolicyDTO getById(Integer policyId) {
		Policy policy = policyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException("Insurance Policy", "policy ", "" + policyId));
		
		// If not admin, verify the policy belongs to the current user
		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			if (policy.getAccountId() == null || !policy.getAccountId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot access another user's policy");
			}
		}
		
		PolicyDTO result = modelMapper.map(policy, PolicyDTO.class);
		
		// Populate account details
		if (policy.getAccountId() != null) {
			result.setAccount(accountService.findById(policy.getAccountId()));
		}
		
		return result;
	}

	@Override
	public PolicyDTO updatePolicy(PolicyDTO policyDTO, Integer policyId) {
		Policy existingPolicy = policyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException("Insurance Policy", "policy ", "" + policyId));
		
		// If not admin, verify the policy belongs to the current user
		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			if (existingPolicy.getAccountId() == null 
					|| !existingPolicy.getAccountId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot update another user's policy");
			}
		}
		
		// Update lineId if provided
		if (policyDTO.getLineId() != null) {
			lineRepository.findById(policyDTO.getLineId())
					.orElseThrow(() -> new ResourceNotFoundException("Line", "id", String.valueOf(policyDTO.getLineId())));
			existingPolicy.setLineId(policyDTO.getLineId());
		}
		
		// Update accountId if provided (admin only)
		if (policyDTO.getAccountId() != null && authService.isAdmin()) {
			// Verify account exists
			accountService.findById(policyDTO.getAccountId());
			existingPolicy.setAccountId(policyDTO.getAccountId());
		}
		
		// Update other fields
		if (policyDTO.getPremium() != null) {
			existingPolicy.setPremium(policyDTO.getPremium());
		}
		if (policyDTO.getStartDate() != null) {
			existingPolicy.setStartDate(policyDTO.getStartDate());
		}
		if (policyDTO.getEndDate() != null) {
			existingPolicy.setExpiryDate(policyDTO.getEndDate());
		}
		
		Policy updatedPolicy = policyRepository.save(existingPolicy);
		PolicyDTO result = modelMapper.map(updatedPolicy, PolicyDTO.class);
		
		// Populate account details
		if (updatedPolicy.getAccountId() != null) {
			result.setAccount(accountService.findById(updatedPolicy.getAccountId()));
		}
		
		return result;
	}

	@Override
	public String deletePolicy(Integer policyId) {
		Policy policy = policyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException("Insurance Policy", "policy ", "" + policyId));
		
		// If not admin, verify the policy belongs to the current user
		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			if (policy.getAccountId() == null || !policy.getAccountId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot delete another user's policy");
			}
		}
		
		policyRepository.delete(policy);
		return "Insurance policy deleted successfully...";
	}

	@Override
	public List<PolicyDTO> getAllPolicy() {
		List<Policy> policies;
		
		// If admin, return all policies
		// If regular user, return only their policies
		if (authService.isAdmin()) {
			policies = policyRepository.findAll();
		} else {
			// Get current user's account
			Account currentAccount = authService.getCurrentAccount();
			policies = policyRepository.findAll().stream()
					.filter(policy -> policy.getAccountId() != null && policy.getAccountId().equals(currentAccount.getId()))
					.collect(Collectors.toList());
		}
		
		// Map to DTOs and populate account details
		return policies.stream().map(policy -> {
			PolicyDTO dto = modelMapper.map(policy, PolicyDTO.class);
			if (policy.getAccountId() != null) {
				dto.setAccount(accountService.findById(policy.getAccountId()));
			}
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public PolicyDTO assignPolicyWithAccount(Integer accountId, Integer policyId) {
		// Verify account exists
		accountService.findById(accountId);
		
		PolicyDTO policy = getById(policyId);
		policy.setAccountId(accountId);
		
		return updatePolicy(policy, policyId);
	}

	@Override
	public List<Policy> getAllClaimsByPolicyNumber(Integer policyId) {
		policyRepository.findById(policyId).orElseThrow(() -> new ResourceNotFoundException(null));
		return null;
	}

}
