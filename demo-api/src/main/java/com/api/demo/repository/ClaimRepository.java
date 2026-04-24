package com.api.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.demo.entity.Claim;
import com.api.demo.entity.Policy;

public interface ClaimRepository extends JpaRepository<Claim, Integer> {

	List<Claim> findByPolicy(Policy policy);

	List<Claim> findByPolicyIn(List<Policy> policies);

}
