package com.api.demo.dto;

import java.time.LocalDate;

import com.api.demo.model.ClaimStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClaimDTO {

	private Integer id;

	@NotBlank(message = "Claim Number is required")
	@Size(min = 3, max = 50, message = "Claim Number must be between 3 and 50 characters")
	private String claimNumber;

	@NotBlank(message = "Description is required")
	@Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
	private String description;

	@NotNull(message = "Claim Date is required")
	@PastOrPresent(message = "Claim Date must be today or in the past")
	private LocalDate claimDate;

	@NotNull(message = "Claim Status is required")
	private ClaimStatus claimStatus;

	@NotNull(message = "Policy ID is required")
	@Positive(message = "Policy ID must be a positive number")
	private Integer policyId;

	private PolicyDTO policy;
}
