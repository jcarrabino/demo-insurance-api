package com.api.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.demo.dto.ClaimDTO;
import com.api.demo.entity.Claim;
import com.api.demo.service.AuthorizationService;
import com.api.demo.service.ClaimService;
import com.api.demo.service.PolicyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/claims/")
public class ClaimController {

	@Autowired
	private ClaimService claimService;

	@Autowired
	private PolicyService policyService;

	@Autowired
	private AuthorizationService authService;

	@Autowired
	private ModelMapper modelMapper;

	private ClaimDTO toDTO(Claim claim) {
		ClaimDTO dto = modelMapper.map(claim, ClaimDTO.class);
		// Populate policy details (without circular reference)
		if (claim.getPolicyId() != null) {
			dto.setPolicy(policyService.getById(claim.getPolicyId()));
		}
		return dto;
	}

	@PostMapping("/{id}")
	public ResponseEntity<ClaimDTO> createClaim(@PathVariable("id") Integer id, @Valid @RequestBody ClaimDTO claim) {
		// Admins can create claims for any policy
		// Regular users can only create claims for their own policies (validation in service layer)
		Claim created = claimService.createNewClaim(id, claim);
		return new ResponseEntity<>(toDTO(created), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClaimDTO> updateClaim(@Valid @RequestBody Claim claim, @PathVariable("id") Integer id) {
		// Admins can update any claim
		// Regular users can only update their own claims (validation in service layer)
		Claim updated = claimService.updateClaim(claim, id);
		return new ResponseEntity<>(toDTO(updated), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClaimDTO> getClaimById(@PathVariable("id") Integer id) {
		// Admins can view any claim
		// Regular users can only view their own claims (validation in service layer)
		Claim claim = claimService.getClaimById(id);
		return new ResponseEntity<>(toDTO(claim), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteClaimById(@PathVariable("id") Integer id) {
		// Admins can delete any claim
		// Regular users can only delete their own claims (validation in service layer)
		return new ResponseEntity<String>(claimService.deleteClaim(id), HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<List<ClaimDTO>> getAllClaims() {
		List<Claim> claims = claimService.getAllClaim();
		List<ClaimDTO> dtos = claims.stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

}
