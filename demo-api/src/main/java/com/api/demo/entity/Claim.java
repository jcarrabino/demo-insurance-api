package com.api.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

import com.api.demo.model.ClaimStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "policy")
@EqualsAndHashCode(exclude = "policy")
@Entity
@Table(name = "claim", indexes = {@Index(name = "idx_policy_id", columnList = "policy_id"),
		@Index(name = "idx_claim_status", columnList = "claim_status"),
		@Index(name = "idx_claim_date", columnList = "claim_date")})
public class Claim {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String description;

	private LocalDate claimDate;

	@Enumerated(EnumType.STRING)
	private ClaimStatus claimStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policy_id", nullable = false)
	private Policy policy;

}
