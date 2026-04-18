package com.api.demo.utils;

import com.api.demo.exception.ResourceNotFoundException;

public class PasswordValidator {

	private static final int MIN_LENGTH = 8;
	private static final int MAX_LENGTH = 20;
	private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$";

	public static void validatePassword(String password) {
		if (password == null || password.isEmpty()) {
			throw new ResourceNotFoundException("Password cannot be empty");
		}

		if (password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
			throw new ResourceNotFoundException(
					"Password must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters");
		}

		if (!password.matches(PASSWORD_PATTERN)) {
			throw new ResourceNotFoundException(
					"Password must contain at least one uppercase letter, one lowercase letter, one number and one special character");
		}
	}
}
