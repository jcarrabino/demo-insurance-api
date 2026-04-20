package com.api.demo.config;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to require admin role or resource owner
 * Usage: @RequireAdminOrOwner("id")
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('ADMIN') or @authorizationService.isOwner(#id)")
public @interface RequireAdminOrOwner {
	String value() default "id";
}
