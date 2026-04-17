package com.api.demo.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.demo.dto.ClaimDTO;
import com.api.demo.dto.PolicyDTO;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.entity.Claim;
import com.api.demo.repository.ClaimRepository;
import com.api.demo.repository.PolicyRepository;
import com.api.demo.service.ClaimService;
import com.api.demo.service.PolicyService;

@Service
public class ClaimServiceImpl implements ClaimService {

	private ClaimRepository claimRepository;
	private PolicyService PolicyService;
	private PolicyRepository PolicyRepository;
	private ModelMapper modelMapper;

	@Autowired
	public ClaimServiceImpl(ClaimRepository claimRepository, PolicyService PolicyService,
			PolicyRepository PolicyRepository, ModelMapper modelMapper) {
		this.claimRepository = claimRepository;
		this.PolicyService = PolicyService;
		this.PolicyRepository = PolicyRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public Claim createNewClaim(Integer policyId, ClaimDTO claim) {
		PolicyDTO policy = PolicyService.getById(policyId);
		// System.out.println(policy);
		// Set<ClaimDTO> claims = policy.getClaims();
		// claims.add(claim);
		// policy.setClaims(claims);
		claim.setPolicy(policy);

		// PolicyRepository.save(modelMapper.map(policy, Policy.class));

		Claim claimd = modelMapper.map(claim, Claim.class);
		return claimRepository.save(claimd);
	}

	@Override
	public Claim getClaimById(Integer claimId) {
		return claimRepository.findById(claimId)
				.orElseThrow(() -> new ResourceNotFoundException("Claim ", "Claim id", "" + claimId));
	}

	@Override
	public Claim updateClaim(Claim claim, Integer claimId) {
		Claim claime = claimRepository.findById(claimId)
				.orElseThrow(() -> new ResourceNotFoundException("Claim ", "Claim id", "" + claimId));
		Claim newClaim = modelMapper.map(claim, Claim.class);
		newClaim.setId(claimId);
		newClaim.setClaimDate(claime.getClaimDate());
		return claimRepository.save(newClaim);
	}

	@Override
	public String deleteClaim(Integer claimId) {
		Claim claim = claimRepository.findById(claimId)
				.orElseThrow(() -> new ResourceNotFoundException("Claim ", "Claim id", "" + claimId));
		claimRepository.delete(claim);
		return "claim info delete successfully...";
	}

	@Override
	public List<Claim> getAllClaim() {
		return claimRepository.findAll();
	}

}
