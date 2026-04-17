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
import com.api.demo.repository.PolicyRepository;
import com.api.demo.service.AccountService;
import com.api.demo.service.PolicyService;

import jakarta.transaction.Transactional;

@Service
public class PolicyServiceImpl implements PolicyService {

	@Autowired
	private PolicyRepository policyRepository;

	@Autowired
	private AccountService accountService;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PolicyDTO createNewPolicy(Integer clientId, PolicyDTO Policy) {
		Account client = modelMapper.map(accountService.findById(clientId), Account.class);
		Policy policy = modelMapper.map(Policy, Policy.class);
		policy.setAccount(client);
		Policy savedPolicy = policyRepository.save(policy);
		return modelMapper.map(savedPolicy, PolicyDTO.class);
	}

	@Transactional
	@Override
	public PolicyDTO getById(Integer policyId) {
		Policy policy = policyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException("Insurance Policy", "policy ", "" + policyId));
		return modelMapper.map(policy, PolicyDTO.class);
	}

	@Override
	public PolicyDTO updatePolicy(PolicyDTO policy, Integer policyId) {
		policyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException("Insurance Policy", "policy ", "" + policyId));
		Policy newInsurance = modelMapper.map(policy, Policy.class);
		newInsurance.setId(policyId);
		Policy updatedPolicy = policyRepository.save(newInsurance);
		return modelMapper.map(updatedPolicy, PolicyDTO.class);
	}

	@Override
	public String deletePolicy(Integer policyId) {
		Policy Policy = policyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException("Insurance Policy", "policy ", "" + policyId));
		policyRepository.delete(Policy);
		return "Insurance policy deleted successfully...";
	}

	@Override
	public List<PolicyDTO> getAllPolicy() {
		List<Policy> policies = policyRepository.findAll();
		return policies.stream().map(policy -> modelMapper.map(policy, PolicyDTO.class)).collect(Collectors.toList());
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
