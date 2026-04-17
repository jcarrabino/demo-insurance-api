package com.api.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.demo.entity.Policy;

public interface PolicyRepository extends JpaRepository<Policy, Integer> {

}
