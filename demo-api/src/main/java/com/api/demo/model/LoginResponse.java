package com.api.demo.model;

import com.api.demo.dto.AccountDTO;

public class LoginResponse {

	private AccountDTO account;
	private String message;

	public LoginResponse() {
	}

	public LoginResponse(AccountDTO account, String message) {
		this.account = account;
		this.message = message;
	}

	public AccountDTO getAccount() {
		return account;
	}

	public void setAccount(AccountDTO account) {
		this.account = account;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
