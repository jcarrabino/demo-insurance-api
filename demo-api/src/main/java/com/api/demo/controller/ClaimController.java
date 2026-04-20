package com.api.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.demo.dto.ClaimDTO;
import com.api.demo.entity.Claim;
import com.api.demo.model.ApiResponse;
import com.api.demo.model.PagedResponse;
import com.api.demo.service.AuthorizationService;
import com.api.demo.service.ClaimService;
import com.api.demo.service.PolicyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/claims")
@Tag(name = "Claim Management", description = "APIs for managing insurance claims")
@SecurityRequirement(name = "bearerAuth")
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
		if (claim.getPolicyId() != null) {
			dto.setPolicy(policyService.getById(claim.getPolicyId()));
		}
		return dto;
	}

	@PostMapping("/{id}")
	@Operation(summary = "Create claim", description = "Create a new claim for a policy")
	public ResponseEntity<ApiResponse<ClaimDTO>> createClaim(@PathVariable Integer id,
			@Valid @RequestBody ClaimDTO claim) {
		Claim created = claimService.createNewClaim(id, claim);
		return new ResponseEntity<>(ApiResponse.success(toDTO(created), "Claim created successfully"),
				HttpStatus.CREATED);
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Update claim", description = "Partial update of a claim")
	public ResponseEntity<ApiResponse<ClaimDTO>> updateClaim(@RequestBody ClaimDTO claimDTO, @PathVariable Integer id) {
		Claim updated = claimService.partialUpdateClaim(claimDTO, id);
		return new ResponseEntity<>(ApiResponse.success(toDTO(updated), "Claim updated successfully"), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get claim by ID", description = "Retrieve claim details by ID")
	public ResponseEntity<ApiResponse<ClaimDTO>> getClaimById(@PathVariable Integer id) {
		Claim claim = claimService.getClaimById(id);
		return new ResponseEntity<>(ApiResponse.success(toDTO(claim)), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete claim", description = "Delete a claim by ID")
	public ResponseEntity<ApiResponse<String>> deleteClaimById(@PathVariable Integer id) {
		String result = claimService.deleteClaim(id);
		return new ResponseEntity<>(ApiResponse.success(result, "Claim deleted successfully"), HttpStatus.OK);
	}

	@GetMapping
	@Operation(summary = "Get all claims", description = "Retrieve all claims with pagination")
	public ResponseEntity<ApiResponse<PagedResponse<ClaimDTO>>> getAllClaims(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Claim> claims = claimService.getAllClaim();
		List<ClaimDTO> dtos = claims.stream().map(this::toDTO).collect(Collectors.toList());

		// Manual pagination for now (can be optimized with repository pagination)
		int start = page * size;
		int end = Math.min(start + size, dtos.size());
		List<ClaimDTO> pageContent = dtos.subList(start, end);

		PagedResponse<ClaimDTO> response = PagedResponse.<ClaimDTO>builder().content(pageContent).page(page).size(size)
				.totalElements(dtos.size()).totalPages((dtos.size() + size - 1) / size).hasNext(end < dtos.size())
				.hasPrevious(page > 0).build();

		return new ResponseEntity<>(ApiResponse.success(response), HttpStatus.OK);
	}
}
