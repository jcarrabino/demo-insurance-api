package com.api.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.api.demo.dto.AccountDTO;

@Service
public class AccountUserDetailsService implements UserDetailsService {

	private final AccountService accountService;

	public AccountUserDetailsService(AccountService accountService) {
		this.accountService = accountService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		AccountDTO client = accountService.findByEmail(username);

		List<GrantedAuthority> authority = new ArrayList<>();

		if (Boolean.TRUE.equals(client.getAdmin())) {
			authority.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}

		return new User(client.getEmail(), client.getPassword(), authority);

	}

}
