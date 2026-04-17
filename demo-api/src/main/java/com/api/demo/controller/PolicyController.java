package com.api.demo.controller;

import java.util.List;

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

import com.api.demo.dto.PolicyDTO;
import com.api.demo.service.AuthorizationService;
import com.api.demo.service.PolicyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/policies/")
public class PolicyController {

	@Autowired
	private PolicyService policyService;

	@Autowired
	private AuthorizationService authService;

	@PostMapping("/{accountId}")
	public ResponseEntity<PolicyDTO> createdPolicy(@Valid @RequestBody PolicyDTO policy,
			@PathVariable("accountId") Integer AccountId) {
		authService.requireAdmin();
		return new ResponseEntity<PolicyDTO>(policyService.createNewPolicy(AccountId, policy), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PolicyDTO> updatePolicy(@Valid @RequestBody PolicyDTO policy,
			@PathVariable("id") Integer id) {
		authService.requireAdmin();
		return new ResponseEntity<PolicyDTO>(policyService.updatePolicy(policy, id), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePolicy(@PathVariable("id") Integer id) {
		authService.requireAdmin();
		return new ResponseEntity<String>(policyService.deletePolicy(id), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PolicyDTO> getPolicy(@PathVariable("id") Integer id) {
		return new ResponseEntity<PolicyDTO>(policyService.getById(id), HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<List<PolicyDTO>> getAllPolicy() {
		return new ResponseEntity<List<PolicyDTO>>(policyService.getAllPolicy(), HttpStatus.OK);
	}

}
