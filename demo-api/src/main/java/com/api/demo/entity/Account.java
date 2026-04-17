package com.api.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String firstName;
	private String middleName;
	private String lastName;
	@Embedded
	private Address address;
	private Integer phoneNumber;
	private LocalDate dateOfBirth;

	@Column(unique = true)
	private String email;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	@Builder.Default
	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean admin = false;

	@OneToMany
	private List<Policy> policy;

}
