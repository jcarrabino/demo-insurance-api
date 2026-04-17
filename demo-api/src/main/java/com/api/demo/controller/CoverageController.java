package com.api.demo.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.demo.entity.Account;
import com.api.demo.entity.Line;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.repository.AccountRepository;
import com.api.demo.repository.LineRepository;
import com.api.demo.service.CoverageCalculationService;

@RestController
@RequestMapping("/api/coverage/")
public class CoverageController {

	@Autowired
	private CoverageCalculationService coverageService;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private LineRepository lineRepository;

	@GetMapping("/calculate/{accountId}/{lineId}")
	public ResponseEntity<Map<String, Object>> calculateCoverage(@PathVariable("accountId") Integer accountId,
			@PathVariable("lineId") Integer lineId) {

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

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
