package com.api.demo.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.demo.entity.Account;
import com.api.demo.entity.Line;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.model.ApiResponse;
import com.api.demo.repository.AccountRepository;
import com.api.demo.repository.LineRepository;
import com.api.demo.service.CoverageCalculationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/coverage")
@Tag(name = "Coverage Calculator", description = "APIs for calculating insurance coverage")
@SecurityRequirement(name = "bearerAuth")
public class CoverageController {

	@Autowired
	private CoverageCalculationService coverageService;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private LineRepository lineRepository;

	@GetMapping("/calculate/{accountId}/{lineId}")
	@Operation(summary = "Calculate coverage", description = "Calculate coverage for an account and insurance line")
	@PreAuthorize("hasRole('ADMIN') or @authorizationService.isOwner(#accountId)")
	public ResponseEntity<ApiResponse<Map<String, Object>>> calculateCoverage(@PathVariable Integer accountId,
			@PathVariable Integer lineId) {

		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new ResourceNotFoundException("Account", "id", String.valueOf(accountId)));

		Line line = lineRepository.findById(lineId)
				.orElseThrow(() -> new ResourceNotFoundException("Line", "id", String.valueOf(lineId)));

		BigDecimal calculatedCoverage = coverageService.calculateCoverage(account, line);
		String riskLevel = coverageService.getRiskLevel(account.getAddress().getZipCode());
		double premiumAdjustment = coverageService.getPremiumAdjustment(account.getAddress().getZipCode());

		Map<String, Object> response = new HashMap<>();
		response.put("accountId", accountId);
		response.put("accountName", account.getFirstName() + " " + account.getLastName());
		response.put("zipCode", account.getAddress().getZipCode());
		response.put("lineId", lineId);
		response.put("lineName", line.getName());
		response.put("minCoverage", line.getMinCoverage());
		response.put("maxCoverage", line.getMaxCoverage());
		response.put("calculatedCoverage", calculatedCoverage);
		response.put("riskLevel", riskLevel);
		response.put("premiumAdjustment", premiumAdjustment);

		return new ResponseEntity<>(ApiResponse.success(response, "Coverage calculated successfully"), HttpStatus.OK);
	}
}
