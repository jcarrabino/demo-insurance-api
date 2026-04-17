package com.api.demo.service;

import java.util.List;

import com.api.demo.dto.PolicyDTO;
import com.api.demo.entity.Policy;

public interface PolicyService {

	/**
	 *
	 * @param policy:
	 *            take all about Policy detail like name, type, amount, duration
	 *            after save all info in database
	 * @return: return a saved info in database.
	 */
	PolicyDTO createNewPolicy(Integer policyId, PolicyDTO policy);

	/**
	 *
	 * @param policyId:
	 *            take insurance id by user and check this id able in database or
	 *            not if in database this id not exit then throw a particular
	 *            exception if exit then take detail in a single variable
	 * @return: after all opration return this variable
	 */
	PolicyDTO getById(Integer policyId);

	/**
	 *
	 * @param policy:
	 *            take all about Policy detail like name, type, amount, duration
	 * @param policyId:
	 *            take insurance id by user and check this id able in database or
	 *            not if in database this id not exit then throw a particular
	 *            exception if exit then take detail in a single variable
	 *
	 *            after this swap both insurance about all data and save in
	 *            database;
	 *
	 * @return: return a updated insurance policy detail
	 */
	PolicyDTO updatePolicy(PolicyDTO policy, Integer policyId);

	/**
	 *
	 * @param policyId:
	 *            take insurance id by user and check this id able in database or
	 *            not if in database this id not exit then throw a particular
	 *            exception if exit then take detail in a single variable
	 *
	 * @return: return type a string msg like this insurance policy deleted in
	 *          database successfully.
	 */
	String deletePolicy(Integer policyId);

	/**
	 *
	 * @return: this is return all insurance policy in a list
	 */
	List<PolicyDTO> getAllPolicy();

	PolicyDTO assignPolicyWithAccount(Integer accountId, Integer policyId);

	/**
	 *
	 * @param policyId:
	 *            take insurance id by user and check this id able in database or
	 *            not if in database this id not exit then throw a particular
	 *            exception if exit then take detail in a single variable
	 * @return: return policy details with all particular claims
	 */
	List<Policy> getAllClaimsByPolicyNumber(Integer policyId);

}
