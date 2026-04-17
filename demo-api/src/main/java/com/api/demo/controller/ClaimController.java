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

import com.api.demo.dto.ClaimDTO;
import com.api.demo.entity.Claim;
import com.api.demo.service.AuthorizationService;
import com.api.demo.service.ClaimService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/claims/")
public class ClaimController {

	@Autowired
	private ClaimService claimService;

	@Autowired
	private AuthorizationService authService;

	@PostMapping("/{id}")
	public ResponseEntity<Claim> createClaim(@PathVariable("id") Integer id, @Valid @RequestBody ClaimDTO claim) {
		// Admins can create claims for any policy
		// Regular users can only create claims for their own policies (validation in service layer)
		return new ResponseEntity<Claim>(claimService.createNewClaim(id, claim), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Claim> updateClaim(@Valid @RequestBody Claim claim, @PathVariable("id") Integer id) {
		// Admins can update any claim
		// Regular users can only update their own claims (validation in service layer)
		return new ResponseEntity<Claim>(claimService.updateClaim(claim, id), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Claim> getClaimById(@PathVariable("id") Integer id) {
		// Admins can view any claim
		// Regular users can only view their own claims (validation in service layer)
		return new ResponseEntity<Claim>(claimService.getClaimById(id), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteClaimById(@PathVariable("id") Integer id) {
		// Admins can delete any claim
		// Regular users can only delete their own claims (validation in service layer)
		return new ResponseEntity<String>(claimService.deleteClaim(id), HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<List<Claim>> getAllClaims() {
		return new ResponseEntity<List<Claim>>(claimService.getAllClaim(), HttpStatus.OK);
	}

}
