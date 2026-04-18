package com.api.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
	info = @Info(
		title = "Demo Insurance API",
		version = "1.0",
		description = "REST API for managing insurance accounts, policies, claims, and lines",
		contact = @Contact(
			name = "API Support",
			email = "support@example.com"
		),
		license = @License(
			name = "Apache 2.0",
			url = "https://www.apache.org/licenses/LICENSE-2.0.html"
		)
	),
	servers = {
		@Server(url = "http://localhost:8080", description = "Development Server"),
		@Server(url = "https://api.production.com", description = "Production Server")
	}
)
@SecurityScheme(
	name = "bearerAuth",
	description = "JWT Bearer token authentication",
	scheme = "bearer",
	type = SecuritySchemeType.HTTP,
	bearerFormat = "JWT",
	in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
