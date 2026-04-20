package com.api.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.demo.dto.LineDTO;
import com.api.demo.model.ApiResponse;
import com.api.demo.service.LineService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/lines")
@Tag(name = "Line Management", description = "APIs for managing insurance lines")
@SecurityRequirement(name = "bearerAuth")
public class LineController {

	@Autowired
	private LineService lineService;

	@PostMapping
	@Operation(summary = "Create line", description = "Create a new insurance line. Admin only.")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<LineDTO>> createLine(@Valid @RequestBody LineDTO lineDTO) {
		LineDTO created = lineService.create(lineDTO);
		return new ResponseEntity<>(ApiResponse.success(created, "Line created successfully"), HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get line by ID", description = "Retrieve line details by ID")
	public ResponseEntity<ApiResponse<LineDTO>> getLineById(@PathVariable Integer id) {
		LineDTO line = lineService.getById(id);
		return new ResponseEntity<>(ApiResponse.success(line), HttpStatus.OK);
	}

	@GetMapping
	@Operation(summary = "Get all lines", description = "Retrieve all insurance lines")
	public ResponseEntity<ApiResponse<List<LineDTO>>> getAllLines() {
		List<LineDTO> lines = lineService.getAll();
		return new ResponseEntity<>(ApiResponse.success(lines), HttpStatus.OK);
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Update line", description = "Partial update of a line. Only provided fields are updated. Admin only.")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<LineDTO>> updateLine(@PathVariable Integer id, @RequestBody LineDTO lineDTO) {
		LineDTO updated = lineService.partialUpdate(id, lineDTO);
		return new ResponseEntity<>(ApiResponse.success(updated, "Line updated successfully"), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete line", description = "Delete a line by ID. Admin only.")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<String>> deleteLine(@PathVariable Integer id) {
		String result = lineService.delete(id);
		return new ResponseEntity<>(ApiResponse.success(result, "Line deleted successfully"), HttpStatus.OK);
	}
}
