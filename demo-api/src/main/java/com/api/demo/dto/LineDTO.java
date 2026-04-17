package com.api.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineDTO {

	private Integer id;

	@NotBlank(message = "Name is required")
	@Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
	private String name;

	@Size(max = 255, message = "Description must be less than 255 characters")
	private String description;

	@NotNull(message = "Max coverage is required")
	@DecimalMin(value = "1.0", message = "Max coverage must be at least 1")
	private Float maxCoverage;

	@NotNull(message = "Min coverage is required")
	@DecimalMin(value = "1.0", message = "Min coverage must be at least 1")
	private Float minCoverage;
}
