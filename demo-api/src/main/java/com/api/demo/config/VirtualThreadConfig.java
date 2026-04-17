package com.api.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;

import java.util.concurrent.Executors;

@Configuration
public class VirtualThreadConfig {

	/**
	 * Replaces Spring's default async executor with one backed by virtual threads.
	 * This covers @Async methods and Spring's internal async processing. Tomcat's
	 * request thread pool is handled separately by:
	 * spring.threads.virtual.enabled=true (already set in application.properties)
	 */
	@Bean
	public AsyncTaskExecutor applicationTaskExecutor() {
		return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
	}
}
