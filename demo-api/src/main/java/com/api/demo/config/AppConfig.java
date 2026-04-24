package com.api.demo.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebMvc
@EnableCaching
public class AppConfig {

	@Value("${cors.allowed.origins}")
	private String corsOrigins;

	public static final String[] PUBLIC_API = {"/v3/api-docs", "/api/v1/auth/**", "/v2/api-docs",
			"/swagger-resources/**", "/swagger-ui/**", "/webjars/**", "/actuator/**"

	};

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityConfigrationChain(HttpSecurity http) throws Exception {

		http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/swagger-ui*/**", "/v3/api-docs/**", "/actuator/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/v1/auth/register", "/api/v1/auth/login").permitAll()
						.requestMatchers(HttpMethod.GET, "/welcome", "/login").permitAll().anyRequest().authenticated())
				.addFilterAfter(new JwtGenerator(), BasicAuthenticationFilter.class)
				.addFilterBefore(new JwtValidator(), BasicAuthenticationFilter.class).formLogin(form -> form.disable())
				.httpBasic(Customizer.withDefaults());

		return http.build();

	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		String origins = corsOrigins.trim();

		// Handle wildcard for all origins
		if ("*".equals(origins)) {
			config.setAllowedOriginPatterns(List.of("*"));
			config.setAllowCredentials(false);
		} else {
			config.setAllowedOrigins(Arrays.asList(origins.split(",")));
			config.setAllowCredentials(true);
		}

		// Include PATCH method for partial updates
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setExposedHeaders(List.of("Authorization", "authorization"));
		config.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ModelMapper mapper() {
		ModelMapper mapper = new ModelMapper();
		// Policy entity -> PolicyDTO
		mapper.typeMap(com.api.demo.entity.Policy.class, com.api.demo.dto.PolicyDTO.class)
				.addMapping(com.api.demo.entity.Policy::getExpiryDate, com.api.demo.dto.PolicyDTO::setEndDate)
				.addMapping(src -> src.getLine() != null ? src.getLine().getId() : null, com.api.demo.dto.PolicyDTO::setLineId)
				.addMapping(src -> src.getAccount() != null ? src.getAccount().getId() : null, com.api.demo.dto.PolicyDTO::setAccountId);
		// Claim entity -> ClaimDTO
		mapper.typeMap(com.api.demo.entity.Claim.class, com.api.demo.dto.ClaimDTO.class)
				.addMapping(src -> src.getPolicy() != null ? src.getPolicy().getId() : null, com.api.demo.dto.ClaimDTO::setPolicyId);
		// Account entity -> AccountDTO (map List<Policy> to List<Integer> policy IDs)
		mapper.typeMap(com.api.demo.entity.Account.class, com.api.demo.dto.AccountDTO.class)
				.addMapping(src -> src.getPolicies() != null
						? src.getPolicies().stream().map(com.api.demo.entity.Policy::getId).collect(java.util.stream.Collectors.toList())
						: null,
						com.api.demo.dto.AccountDTO::setPolicies);
		return mapper;
	}

}
