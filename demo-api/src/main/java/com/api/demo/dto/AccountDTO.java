package com.api.demo.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import com.api.demo.entity.Address;

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

	@NotBlank(message = "First Name is required")
	@Size(min = 2, max = 50, message = "First Name must be between 2 and 50 characters")
	private String firstName;

	@Size(max = 50, message = "Middle Name must be less than 50 characters")
	private String middleName;

	@NotEmpty(message = "Last Name is required")
	@Size(min = 2, max = 50, message = "Last Name must be between 2 and 50 characters")
	private String lastName;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Past(message = "Date of Birth must be in the past")
	private LocalDate dateOfBirth;

	@Email(message = "Email is not valid")
	@NotBlank(message = "Email is required")
	private String email;

	@Pattern(regexp = "^[0-9]{10}$", message = "Phone Number must be exactly 10 digits")
	private String phoneNumber;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character")
	private String password;

	@Valid
	private Address address;

	private Boolean admin;
}
