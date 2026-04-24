package com.api.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.demo.entity.Account;
import com.api.demo.entity.Policy;

public interface PolicyRepository extends JpaRepository<Policy, Integer> {

	List<Policy> findByAccount(Account account);

}
