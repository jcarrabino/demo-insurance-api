package com.api.demo.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.demo.dto.AccountDTO;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.entity.Account;
import com.api.demo.repository.AccountRepository;
import com.api.demo.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository clientRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public AccountDTO addAccount(AccountDTO account) {
		if (LocalDate.now().isBefore(account.getDateOfBirth()))
			throw new ResourceNotFoundException("Date is not valid plese provide past date...");
		Account customerData = modelMapper.map(account, Account.class);
		customerData.setPassword(passwordEncoder.encode(customerData.getPassword()));
		Account savedAccount = clientRepository.save(customerData);
		return modelMapper.map(savedAccount, AccountDTO.class);
	}

	@Override
	public AccountDTO findById(Integer id) {
		Account account = clientRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Account ", "accountId", "" + id));
		return modelMapper.map(account, AccountDTO.class);
	}

	@Override
	public AccountDTO findByEmail(String email) {
		Account account = clientRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Account", "account email", email));
		return modelMapper.map(account, AccountDTO.class);
	}

	@Override
	public List<AccountDTO> findAllAccount() {
		List<Account> clients = clientRepository.findAll();
		List<AccountDTO> clientsDto = clients.stream().map(account -> modelMapper.map(account, AccountDTO.class))
				.collect(Collectors.toList());
		return clientsDto;
	}

	@Override
	public AccountDTO updateAccountInfo(AccountDTO account, Integer accountId) {
		if (account.getEmail() != null)
			throw new ResourceNotFoundException("email not changeble plese remove email in json data...");

		Account prevAccount = clientRepository.findById(accountId)
				.orElseThrow(() -> new ResourceNotFoundException("Account ", "accountId", "" + accountId));
		Account updatedAccount = modelMapper.map(account, Account.class);
		updatedAccount.setId(accountId);
		updatedAccount.setEmail(prevAccount.getEmail());
		updatedAccount.setPassword(passwordEncoder.encode(updatedAccount.getPassword()));
		Account updatedAccountdata = clientRepository.save(updatedAccount);
		return modelMapper.map(updatedAccountdata, AccountDTO.class);
	}

	@Override
	public String deleteAccount(Integer id) {
		Account account = clientRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Account ", "accountId", "" + id));
		clientRepository.delete(account);

		return "Account Data deleted successfully...";

	}

}
