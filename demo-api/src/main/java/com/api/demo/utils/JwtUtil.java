package com.api.demo.utils;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {

	private static final String JWT_KEY = "thisshouldbestoredsomewheresecure";
	private static final long EXPIRATION_TIME = 30000000; // 30 seconds for testing, should be longer in production

	public static String generateToken(Authentication authentication) {
		SecretKey key = Keys.hmacShaKeyFor(JWT_KEY.getBytes());

		String jwt = Jwts.builder()
				.setIssuer("John")
				.setSubject("JWT Token")
				.claim("username", authentication.getName())
				.claim("authorities", convertAuthWithString(authentication.getAuthorities()))
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + EXPIRATION_TIME))
				.signWith(key)
				.compact();

		return jwt;
	}

	private static String convertAuthWithString(Collection<? extends GrantedAuthority> collection) {
		Set<String> auth = new HashSet<>();

		for (GrantedAuthority gran : collection) {
			auth.add(gran.getAuthority());
		}

		return String.join(",", auth);
	}

}
