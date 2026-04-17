package com.api.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.demo.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {

	/**
	 *
	 * @param email:
	 *            this para is client email in client we are first check this client
	 *            email able in database or not if client email not able in database
	 *            then throw a particular exception. if able then fetch all about
	 *            client about information
	 * @return: a optional client.
	 */
	Optional<Account> findByEmail(String email);

}
