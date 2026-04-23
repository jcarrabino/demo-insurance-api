package com.api.demo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.api.demo.entity.Account;
import com.api.demo.entity.Line;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class CoverageCalculationService {

	/**
	 * Calculate coverage amount based on zip code and line coverage limits. Mock
	 * business logic: Uses zip code to determine risk factor.
	 *
	 * @param account
	 *            The account with zip code
	 * @param line
	 *            The insurance line with min/max coverage
	 * @return Calculated coverage amount
	 */
	@CircuitBreaker(name = "coverageService", fallbackMethod = "calculateCoverageFallback")
	public BigDecimal calculateCoverage(Account account, Line line) {
		if (account == null || account.getAddress() == null || line == null) {
			return BigDecimal.valueOf(line.getMinCoverage());
		}

		String zipCode = account.getAddress().getZipCode();
		if (zipCode == null || zipCode.isEmpty()) {
			return BigDecimal.valueOf(line.getMinCoverage());
		}

		// Extract first digit of zip code for risk calculation
		int firstDigit = Character.getNumericValue(zipCode.charAt(0));

		// Calculate risk factor based on zip code (0-9 maps to 0.0-1.0)
		// Lower zip codes = higher risk = higher coverage
		double riskFactor = (9 - firstDigit) / 9.0;

		// Calculate coverage range
		double minCoverage = line.getMinCoverage();
		double maxCoverage = line.getMaxCoverage();
		double coverageRange = maxCoverage - minCoverage;

		// Apply risk factor to determine coverage within range
		double calculatedCoverage = minCoverage + (coverageRange * riskFactor);

		// Round to nearest 1000
		long rounded = Math.round(calculatedCoverage / 1000) * 1000;

		return BigDecimal.valueOf(rounded).setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * Get risk level description based on zip code
	 *
	 * @param zipCode
	 *            The zip code to evaluate
	 * @return Risk level description
	 */
	@CircuitBreaker(name = "coverageService", fallbackMethod = "getRiskLevelFallback")
	public String getRiskLevel(String zipCode) {
		if (zipCode == null || zipCode.isEmpty()) {
			return "UNKNOWN";
		}

		int firstDigit = Character.getNumericValue(zipCode.charAt(0));

		if (firstDigit <= 2)
			return "HIGH";
		if (firstDigit <= 5)
			return "MEDIUM";
		if (firstDigit <= 7)
			return "LOW";
		return "VERY_LOW";
	}

	/**
	 * Calculate premium adjustment factor based on zip code
	 *
	 * @param zipCode
	 *            The zip code to evaluate
	 * @return Premium multiplier (0.8 to 1.5)
	 */
	@CircuitBreaker(name = "coverageService", fallbackMethod = "getPremiumAdjustmentFallback")
	public double getPremiumAdjustment(String zipCode) {
		if (zipCode == null || zipCode.isEmpty()) {
			return 1.0;
		}

		int firstDigit = Character.getNumericValue(zipCode.charAt(0));

		// Higher risk (lower zip) = higher premium
		return 1.5 - (firstDigit * 0.07);
	}

	// Fallback methods for Circuit Breaker
	public BigDecimal calculateCoverageFallback(Account account, Line line, Exception e) {
		throw new RuntimeException("Coverage calculation service is currently unavailable. Please try again later.", e);
	}

	public String getRiskLevelFallback(String zipCode, Exception e) {
		throw new RuntimeException("Coverage calculation service is currently unavailable. Please try again later.", e);
	}

	public double getPremiumAdjustmentFallback(String zipCode, Exception e) {
		throw new RuntimeException("Coverage calculation service is currently unavailable. Please try again later.", e);
	}
}
