package com.api.demo.service;

import java.util.List;

import com.api.demo.dto.AccountDTO;
//import com.api.demo.entity.Account;

public interface AccountService {

	/**
	 *
	 * @param account:
	 *            this is provide all information about account like name email
	 *            address password extra
	 * @return : return type account info also this is return saved information in
	 *         database. first came account info and this account info save in
	 *         database and return this.
	 */
	AccountDTO addAccount(AccountDTO account);

	/**
	 *
	 * @param id:
	 *            this para is account id in account we are first check this account
	 *            id able in database or not if account id not able in database then
	 *            throw a particular exception. if able then fetch all about account
	 *            about information
	 * @return: after fetch all info return the same info in this method.
	 */
	AccountDTO findById(Integer id);

	/**
	 *
	 * @param email:
	 *            this para is account email in account we are first check this
	 *            account email able in database or not if account email not able in
	 *            database then throw a particular exception. if able then fetch all
	 *            about account about information
	 * @return: after fetch all info return the same info in this method.
	 */
	AccountDTO findByEmail(String email);

	/**
	 *
	 * @return: in this method return all Account information in a list.
	 */
	List<AccountDTO> findAllAccount();

	/**
	 * @param clientID:
	 *            this para is account id in account we are first check this account
	 *            id able in database or not if account id not able in database then
	 *            throw a particular exception. if able then fetch all about account
	 *            about information
	 * @param account:
	 *            this is provide all information about account like name email
	 *            address password extra after fetch about account info in database
	 *            we are swap both account info and update in database
	 * @return : after updating data about account we are return account data
	 */
	AccountDTO updateAccountInfo(AccountDTO account, Integer accountId);

	/**
	 * Partial update - only updates fields that are provided (non-null)
	 * @param account: DTO with fields to update
	 * @param accountId: account ID to update
	 * @return: updated account data
	 */
	AccountDTO partialUpdateAccountInfo(AccountDTO account, Integer accountId);

	/**
	 *
	 * @param idthis
	 *            para is account id in account we are first check this account id
	 *            able in database or not if account id not able in database then
	 *            throw a particular exception. if able then fetch all about account
	 *            about information
	 *
	 *            after fetch all info delete data in database
	 *
	 * @return :String type return is one type msg send like data delete
	 *         successfully.
	 */
	String deleteAccount(Integer id);

}
