package com.api.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.api.demo.dto.AccountDTO;

@Service
public class AccountUserDetailsService implements UserDetailsService {

	@Autowired
	private AccountService accountService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		AccountDTO client = accountService.findByEmail(username);

		List<GrantedAuthority> authority = new ArrayList<>();

		return new User(client.getEmail(), client.getPassword(), authority);

	}

}
