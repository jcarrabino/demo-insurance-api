package com.api.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.demo.entity.Claim;

public interface ClaimRepository extends JpaRepository<Claim, Integer> {

}
