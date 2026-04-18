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
import com.api.demo.exception.ResourceNotFoundException;

@Service
public class AccountUserDetailsService implements UserDetailsService {

	@Autowired
	private AccountService accountService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		try {
			AccountDTO client = accountService.findByEmail(username);

			List<GrantedAuthority> authority = new ArrayList<>();
			if (client.getAdmin() != null && client.getAdmin()) {
				authority.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN"));
			}
			authority.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"));

			return new User(client.getEmail(), client.getPassword(), authority);
		} catch (ResourceNotFoundException e) {
			throw new UsernameNotFoundException("User not found with email: " + username);
		}

	}

}
