package com.api.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineDTO {

	private Integer id;

	@NotBlank(message = "Name is required")
	private String name;

	private String description;

	@NotNull(message = "Max coverage is required")
	@Positive(message = "Max coverage must be a positive number")
	private Float maxCoverage;

	@NotNull(message = "Min coverage is required")
	@Positive(message = "Min coverage must be a positive number")
	private Float minCoverage;
}
