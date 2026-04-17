package com.api.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.api.demo.entity.Account;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolicyDTO {

	private Integer id;

	@NotNull(message = "Line ID is required")
	@Positive(message = "Line ID must be a positive number")
	private Integer lineId;

	private LineDTO line;

	@NotNull(message = "Premium is required")
	@DecimalMin(value = "0.01", message = "Premium must be greater than 0")
	private BigDecimal premium;

	@NotNull(message = "Start Date is required")
	@FutureOrPresent(message = "Start Date must be today or in the future")
	private LocalDate startDate;

	@NotNull(message = "End Date is required")
	@Future(message = "End Date must be in the future")
	private LocalDate endDate;

	private Account account;

	private Set<ClaimDTO> claims = new HashSet<>();
}
