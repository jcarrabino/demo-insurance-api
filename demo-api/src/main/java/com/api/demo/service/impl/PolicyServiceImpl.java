package com.api.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.api.demo.dto.PolicyDTO;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.entity.Account;
import com.api.demo.entity.Line;
import com.api.demo.entity.Policy;
import com.api.demo.repository.AccountRepository;
import com.api.demo.repository.LineRepository;
import com.api.demo.repository.PolicyRepository;
import com.api.demo.service.AuthorizationService;
import com.api.demo.service.PolicyService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PolicyServiceImpl implements PolicyService {

	private final PolicyRepository policyRepository;
	private final LineRepository lineRepository;
	private final AccountRepository accountRepository;
	private final ModelMapper modelMapper;
	private final AuthorizationService authService;

	public PolicyServiceImpl(PolicyRepository policyRepository, LineRepository lineRepository,
			AccountRepository accountRepository, ModelMapper modelMapper, AuthorizationService authService) {
		this.policyRepository = policyRepository;
		this.lineRepository = lineRepository;
		this.accountRepository = accountRepository;
		this.modelMapper = modelMapper;
		this.authService = authService;
	}

	@CircuitBreaker(name = "policyService", fallbackMethod = "createPolicyFallback")
	@Override
	public PolicyDTO createNewPolicy(Integer clientId, PolicyDTO policyDTO) {
		Account account = accountRepository.findById(clientId)
				.orElseThrow(() -> new ResourceNotFoundException("Account", "id", String.valueOf(clientId)));
		Line line = lineRepository.findById(policyDTO.getLineId())
				.orElseThrow(() -> new ResourceNotFoundException("Line", "id", String.valueOf(policyDTO.getLineId())));

		Policy policy = Policy.builder()
				.line(line)
				.account(account)
				.premium(policyDTO.getPremium())
				.startDate(policyDTO.getStartDate())
				.expiryDate(policyDTO.getEndDate())
				.build();

		return modelMapper.map(policyRepository.save(policy), PolicyDTO.class);
	}

	@CircuitBreaker(name = "policyService", fallbackMethod = "getPolicyFallback")
	@Override
	public PolicyDTO getByIdInternal(Integer policyId) {
		Policy policy = policyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException("Insurance Policy", "policy", "" + policyId));
		return modelMapper.map(policy, PolicyDTO.class);
	}

	@CircuitBreaker(name = "policyService", fallbackMethod = "getPolicyFallback")
	@Override
	public PolicyDTO getById(Integer policyId) {
		Policy policy = policyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException("Insurance Policy", "policy", "" + policyId));

		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			if (!policy.getAccount().getId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot access another user's policy");
			}
		}

		return modelMapper.map(policy, PolicyDTO.class);
	}

	@CircuitBreaker(name = "policyService", fallbackMethod = "updatePolicyFallback")
	@Override
	public PolicyDTO updatePolicy(PolicyDTO policyDTO, Integer policyId) {
		Policy existingPolicy = policyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException("Insurance Policy", "policy", "" + policyId));

		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			if (!existingPolicy.getAccount().getId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot update another user's policy");
			}
		}

		if (policyDTO.getLineId() != null) {
			Line line = lineRepository.findById(policyDTO.getLineId()).orElseThrow(
					() -> new ResourceNotFoundException("Line", "id", String.valueOf(policyDTO.getLineId())));
			existingPolicy.setLine(line);
		}
		if (policyDTO.getAccountId() != null && authService.isAdmin()) {
			Account account = accountRepository.findById(policyDTO.getAccountId()).orElseThrow(
					() -> new ResourceNotFoundException("Account", "id", String.valueOf(policyDTO.getAccountId())));
			existingPolicy.setAccount(account);
		}
		if (policyDTO.getPremium() != null) {
			existingPolicy.setPremium(policyDTO.getPremium());
		}
		if (policyDTO.getStartDate() != null) {
			existingPolicy.setStartDate(policyDTO.getStartDate());
		}
		if (policyDTO.getEndDate() != null) {
			existingPolicy.setExpiryDate(policyDTO.getEndDate());
		}

		return modelMapper.map(policyRepository.save(existingPolicy), PolicyDTO.class);
	}

	@CircuitBreaker(name = "policyService", fallbackMethod = "deletePolicyFallback")
	@Override
	public String deletePolicy(Integer policyId) {
		Policy policy = policyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException("Insurance Policy", "policy", "" + policyId));

		if (!authService.isAdmin()) {
			Account currentAccount = authService.getCurrentAccount();
			if (!policy.getAccount().getId().equals(currentAccount.getId())) {
				throw new SecurityException("Cannot delete another user's policy");
			}
		}

		policyRepository.delete(policy);
		return "Insurance policy deleted successfully...";
	}

	@CircuitBreaker(name = "policyService", fallbackMethod = "getAllPoliciesFallback")
	@Override
	public List<PolicyDTO> getAllPolicy() {
		List<Policy> policies;

		if (authService.isAdmin()) {
			policies = policyRepository.findAll();
		} else {
			Account currentAccount = authService.getCurrentAccount();
			policies = policyRepository.findByAccount(currentAccount);
		}

		return policies.stream()
				.map(p -> modelMapper.map(p, PolicyDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public PolicyDTO assignPolicyWithAccount(Integer accountId, Integer policyId) {
		PolicyDTO policyDTO = new PolicyDTO();
		policyDTO.setAccountId(accountId);
		return updatePolicy(policyDTO, policyId);
	}

	@Override
	public List<com.api.demo.entity.Policy> getAllClaimsByPolicyNumber(Integer policyId) {
		policyRepository.findById(policyId).orElseThrow(() -> new ResourceNotFoundException(null));
		return null;
	}

	// Fallback methods
	public PolicyDTO createPolicyFallback(Integer clientId, PolicyDTO policyDTO, Exception e) {
		throw new RuntimeException("Policy service is currently unavailable. Please try again later.", e);
	}

	public PolicyDTO getPolicyFallback(Integer policyId, Exception e) {
		throw new RuntimeException("Policy service is currently unavailable. Please try again later.", e);
	}

	public PolicyDTO updatePolicyFallback(PolicyDTO policyDTO, Integer policyId, Exception e) {
		throw new RuntimeException("Policy service is currently unavailable. Please try again later.", e);
	}

	public String deletePolicyFallback(Integer policyId, Exception e) {
		throw new RuntimeException("Policy service is currently unavailable. Please try again later.", e);
	}

	public List<PolicyDTO> getAllPoliciesFallback(Exception e) {
		throw new RuntimeException("Policy service is currently unavailable. Please try again later.", e);
	}

}
