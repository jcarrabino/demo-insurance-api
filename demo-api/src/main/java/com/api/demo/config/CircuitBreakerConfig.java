package com.api.demo.config;

import org.springframework.context.annotation.Configuration;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class CircuitBreakerConfig {

	private static final Logger logger = LoggerFactory.getLogger(CircuitBreakerConfig.class);

	@Autowired
	public void configureCircuitBreakerRegistry(CircuitBreakerRegistry circuitBreakerRegistry) {
		circuitBreakerRegistry.getEventPublisher()
				.onEntryAdded(event -> logCircuitBreakerAdded(event))
				.onEntryRemoved(event -> logCircuitBreakerRemoved(event));
	}

	private void logCircuitBreakerAdded(EntryAddedEvent<CircuitBreaker> event) {
		logger.info("Circuit Breaker ADDED - Name: {}", event.getAddedEntry().getName());
	}

	private void logCircuitBreakerRemoved(EntryRemovedEvent<CircuitBreaker> event) {
		logger.info("Circuit Breaker REMOVED - Name: {}", event.getRemovedEntry().getName());
	}
}
