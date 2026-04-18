package com.api.demo.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@ConditionalOnProperty(name = "logging.request.enabled", havingValue = "true", matchIfMissing = false)
public class RequestLoggingFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Content cache limit: 10KB for request body
		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request, 10240);
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

		long startTime = System.currentTimeMillis();

		try {
			filterChain.doFilter(requestWrapper, responseWrapper);
		} finally {
			long duration = System.currentTimeMillis() - startTime;
			logRequestResponse(requestWrapper, responseWrapper, duration);
			responseWrapper.copyBodyToResponse();
		}
	}

	private void logRequestResponse(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response,
			long duration) {
		String method = request.getMethod();
		String uri = request.getRequestURI();
		int status = response.getStatus();

		String requestBody = getContentAsString(request.getContentAsByteArray());
		String responseBody = getContentAsString(response.getContentAsByteArray());

		logger.info("Request: {} {} | Status: {} | Duration: {}ms", method, uri, status, duration);

		if (!requestBody.isEmpty() && !uri.contains("/login")) {
			logger.debug("Request Body: {}", requestBody);
		}

		if (!responseBody.isEmpty() && status >= 400) {
			logger.debug("Response Body: {}", responseBody);
		}
	}

	private String getContentAsString(byte[] content) {
		if (content == null || content.length == 0) {
			return "";
		}
		return new String(content, StandardCharsets.UTF_8);
	}
}
