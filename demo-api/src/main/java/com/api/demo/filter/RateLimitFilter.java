package com.api.demo.filter;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.demo.config.RateLimitConfig;

import java.io.IOException;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "rate.limit.enabled", havingValue = "true", matchIfMissing = false)
public class RateLimitFilter extends OncePerRequestFilter {

	@Autowired
	private Map<String, Bucket> rateLimitBuckets;

	@Autowired
	private RateLimitConfig rateLimitConfig;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String clientId = getClientId(request);
		Bucket bucket = rateLimitBuckets.computeIfAbsent(clientId, k -> rateLimitConfig.createNewBucket());

		if (bucket.tryConsume(1)) {
			filterChain.doFilter(request, response);
		} else {
			response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
			response.getWriter().write("{\"error\":\"Too many requests. Please try again later.\"}");
			response.setContentType("application/json");
		}
	}

	private String getClientId(HttpServletRequest request) {
		String clientIp = request.getHeader("X-Forwarded-For");
		if (clientIp == null || clientIp.isEmpty()) {
			clientIp = request.getRemoteAddr();
		}
		return clientIp;
	}
}
