package com.api.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Standardized pagination response wrapper
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {
	private List<T> content;
	private int page;
	private int size;
	private long totalElements;
	private int totalPages;
	private boolean hasNext;
	private boolean hasPrevious;

	public static <T> PagedResponse<T> from(Page<T> page) {
		return PagedResponse.<T>builder().content(page.getContent()).page(page.getNumber()).size(page.getSize())
				.totalElements(page.getTotalElements()).totalPages(page.getTotalPages()).hasNext(page.hasNext())
				.hasPrevious(page.hasPrevious()).build();
	}
}
