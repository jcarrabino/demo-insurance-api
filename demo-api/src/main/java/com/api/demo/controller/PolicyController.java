package com.api.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.demo.dto.PolicyDTO;
import com.api.demo.model.ApiResponse;
import com.api.demo.service.AuthorizationService;
import com.api.demo.service.PolicyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/policies")
@Tag(name = "Policy Management", description = "APIs for managing insurance policies")
@SecurityRequirement(name = "bearerAuth")
public class PolicyController {

	@Autowired
	private PolicyService policyService;

	@Autowired
	private AuthorizationService authService;

	@PostMapping("/{accountId}")
	@Operation(summary = "Create policy", description = "Create a new policy for an account")
	@PreAuthorize("hasRole('ADMIN') or @authorizationService.isOwner(#accountId)")
	public ResponseEntity<ApiResponse<PolicyDTO>> createdPolicy(@Valid @RequestBody PolicyDTO policy,
			@PathVariable Integer accountId) {
		PolicyDTO created = policyService.createNewPolicy(accountId, policy);
		return new ResponseEntity<>(ApiResponse.success(created, "Policy created successfully"), HttpStatus.CREATED);
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Update policy", description = "Partial update of a policy. Only provided fields are updated.")
	public ResponseEntity<ApiResponse<PolicyDTO>> updatePolicy(@RequestBody PolicyDTO policy,
			@PathVariable Integer id) {
		PolicyDTO updated = policyService.updatePolicy(policy, id);
		return new ResponseEntity<>(ApiResponse.success(updated, "Policy updated successfully"), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete policy", description = "Delete a policy by ID")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<String>> deletePolicy(@PathVariable Integer id) {
		String result = policyService.deletePolicy(id);
		return new ResponseEntity<>(ApiResponse.success(result, "Policy deleted successfully"), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get policy by ID", description = "Retrieve policy details by ID")
	public ResponseEntity<ApiResponse<PolicyDTO>> getPolicy(@PathVariable Integer id) {
		PolicyDTO policy = policyService.getById(id);
		return new ResponseEntity<>(ApiResponse.success(policy), HttpStatus.OK);
	}

	@GetMapping
	@Operation(summary = "Get all policies", description = "Retrieve all policies")
	public ResponseEntity<ApiResponse<List<PolicyDTO>>> getAllPolicy() {
		List<PolicyDTO> policies = policyService.getAllPolicy();
		return new ResponseEntity<>(ApiResponse.success(policies), HttpStatus.OK);
	}
}
