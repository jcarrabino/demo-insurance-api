package com.api.demo.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimitConfig {

	@Value("${rate.limit.capacity:100}")
	private int capacity;

	@Value("${rate.limit.refill.tokens:100}")
	private int refillTokens;

	@Value("${rate.limit.refill.duration:1m}")
	private String refillDuration;

	@Bean
	public Map<String, Bucket> rateLimitBuckets() {
		return new ConcurrentHashMap<>();
	}

	public Bucket createNewBucket() {
		Duration duration = parseDuration(refillDuration);
		Bandwidth limit = Bandwidth.classic(capacity, Refill.intervally(refillTokens, duration));
		return Bucket.builder().addLimit(limit).build();
	}

	private Duration parseDuration(String duration) {
		if (duration.endsWith("s")) {
			return Duration.ofSeconds(Long.parseLong(duration.substring(0, duration.length() - 1)));
		} else if (duration.endsWith("m")) {
			return Duration.ofMinutes(Long.parseLong(duration.substring(0, duration.length() - 1)));
		} else if (duration.endsWith("h")) {
			return Duration.ofHours(Long.parseLong(duration.substring(0, duration.length() - 1)));
		}
		return Duration.ofMinutes(1);
	}
}
