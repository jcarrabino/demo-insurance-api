package com.api.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.demo.dto.AccountDTO;

import java.util.List;

public interface AccountService {

	/**
	 * Create a new account
	 *
	 * @param account:
	 *            account information to save
	 * @return: saved account data
	 */
	AccountDTO addAccount(AccountDTO account);

	/**
	 * Find account by ID
	 *
	 * @param id:
	 *            account ID
	 * @return: account data
	 */
	AccountDTO findById(Integer id);

	/**
	 * Find account by email
	 *
	 * @param email:
	 *            account email
	 * @return: account data
	 */
	AccountDTO findByEmail(String email);

	/**
	 * Get all accounts (deprecated - use findAllAccounts with pagination)
	 *
	 * @return: list of all accounts
	 */
	@Deprecated(forRemoval = true)
	List<AccountDTO> findAllAccount();

	/**
	 * Get all accounts with pagination
	 *
	 * @param pageable:
	 *            pagination parameters
	 * @return: paginated account data
	 */
	Page<AccountDTO> findAllAccounts(Pageable pageable);

	/**
	 * Full update of account
	 *
	 * @param account:
	 *            account data to update
	 * @param accountId:
	 *            account ID
	 * @return: updated account data
	 */
	AccountDTO updateAccountInfo(AccountDTO account, Integer accountId);

	/**
	 * Partial update - only updates fields that are provided (non-null)
	 *
	 * @param account:
	 *            DTO with fields to update
	 * @param accountId:
	 *            account ID to update
	 * @return: updated account data
	 */
	AccountDTO partialUpdateAccountInfo(AccountDTO account, Integer accountId);

	/**
	 * Delete account by ID
	 *
	 * @param id:
	 *            account ID
	 * @return: success message
	 */
	String deleteAccount(Integer id);
}
