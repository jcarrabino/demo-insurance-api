package com.api.demo.utils;

public class NumberGenerator {

	public static String generatePolicyNumber(Integer policyId) {
		return String.format("POL-%05d", policyId);
	}

	public static String generateClaimNumber(Integer claimId) {
		return String.format("CLM-%05d", claimId);
	}
}
