package com.api.demo.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import com.api.demo.entity.Address;
import com.api.demo.constraints.ValidationGroups;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {

	private Integer id;

	@NotBlank(message = "First Name is required", groups = ValidationGroups.Create.class)
	@Size(min = 2, max = 50, message = "First Name must be between 2 and 50 characters", groups = ValidationGroups.Create.class)
	private String firstName;

	@Size(max = 50, message = "Middle Name must be less than 50 characters", groups = ValidationGroups.Create.class)
	private String middleName;

	@NotEmpty(message = "Last Name is required", groups = ValidationGroups.Create.class)
	@Size(min = 2, max = 50, message = "Last Name must be between 2 and 50 characters", groups = ValidationGroups.Create.class)
	private String lastName;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Past(message = "Date of Birth must be in the past", groups = ValidationGroups.Create.class)
	private LocalDate dateOfBirth;

	@Email(message = "Email is not valid", groups = ValidationGroups.Create.class)
	@NotBlank(message = "Email is required", groups = ValidationGroups.Create.class)
	private String email;

	@Pattern(regexp = "^[0-9]{10}$", message = "Phone Number must be exactly 10 digits", groups = ValidationGroups.Create.class)
	private String phoneNumber;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters", groups = ValidationGroups.Create.class)
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character", groups = ValidationGroups.Create.class)
	private String password;

	@Valid
	private Address address;

	private Boolean admin;

	@JsonProperty(access = Access.READ_ONLY)
	private List<Integer> policies;
}
