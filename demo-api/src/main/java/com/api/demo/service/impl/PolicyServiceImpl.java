package com.api.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.demo.dto.PolicyDTO;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.entity.Account;
import com.api.demo.entity.Line;
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
		Account client = modelMapper.map(accountService.findById(clientId), Account.class);
		
		// Get the Line entity from lineId
		Line line = lineRepository.findById(policyDTO.getLineId())
				.orElseThrow(() -> new ResourceNotFoundException("Line", "id", String.valueOf(policyDTO.getLineId())));
		
		Policy policy = Policy.builder()
				.line(line)
				.account(client)
				.premium(policyDTO.getPremium())
				.startDate(policyDTO.getStartDate())
				.expiryDate(policyDTO.getEndDate())
				.build();
		
		Policy savedPolicy = policyRepository.save(policy);
		return modelMapper.map(savedPolicy, PolicyDTO.class);
	}

	@Transactional
	@Override
	public PolicyDTO getById(Integer policyId) {
		Policy policy = policyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException("Insurance Policy", "policy ", "" + policyId));
		
		// If not admin, verify the policy belongs to the current user
		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			if (policy.getAccount() == null || !policy.getAccount().getId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot access another user's policy");
			}
		}
		
		return modelMapper.map(policy, PolicyDTO.class);
	}

	@Override
	public PolicyDTO updatePolicy(PolicyDTO policyDTO, Integer policyId) {
		Policy existingPolicy = policyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException("Insurance Policy", "policy ", "" + policyId));
		
		// If not admin, verify the policy belongs to the current user
		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			if (existingPolicy.getAccount() == null 
					|| !existingPolicy.getAccount().getId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot update another user's policy");
			}
		}
		
		// Get the Line entity if lineId is provided
		if (policyDTO.getLineId() != null) {
			Line line = lineRepository.findById(policyDTO.getLineId())
					.orElseThrow(() -> new ResourceNotFoundException("Line", "id", String.valueOf(policyDTO.getLineId())));
			existingPolicy.setLine(line);
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
		return modelMapper.map(updatedPolicy, PolicyDTO.class);
	}

	@Override
	public String deletePolicy(Integer policyId) {
		Policy policy = policyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException("Insurance Policy", "policy ", "" + policyId));
		
		// If not admin, verify the policy belongs to the current user
		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			if (policy.getAccount() == null || !policy.getAccount().getId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot delete another user's policy");
			}
		}
		
		policyRepository.delete(policy);
		return "Insurance policy deleted successfully...";
	}

	@Override
	public List<PolicyDTO> getAllPolicy() {
		// If admin, return all policies
		// If regular user, return only their policies
		if (authService.isAdmin()) {
			List<Policy> policies = policyRepository.findAll();
			return policies.stream().map(policy -> modelMapper.map(policy, PolicyDTO.class))
					.collect(Collectors.toList());
		} else {
			// Get current user's account
			Account currentAccount = authService.getCurrentAccount();
			List<Policy> policies = policyRepository.findAll().stream()
					.filter(policy -> policy.getAccount().getId().equals(currentAccount.getId()))
					.collect(Collectors.toList());
			return policies.stream().map(policy -> modelMapper.map(policy, PolicyDTO.class))
					.collect(Collectors.toList());
		}
	}

	@Override
	public PolicyDTO assignPolicyWithAccount(Integer accountId, Integer policyId) {
		Account client = modelMapper.map(accountService.findById(accountId), Account.class);
		PolicyDTO policy = modelMapper.map(getById(policyId), PolicyDTO.class);
		// policy.setAccount(client);
		PolicyDTO updateInsurancePolcy = updatePolicy(policy, policyId);
		return modelMapper.map(updateInsurancePolcy, PolicyDTO.class);
	}

	@Override
	public List<Policy> getAllClaimsByPolicyNumber(Integer policyId) {
		policyRepository.findById(policyId).orElseThrow(() -> new ResourceNotFoundException(null));
		return null;
	}

}
