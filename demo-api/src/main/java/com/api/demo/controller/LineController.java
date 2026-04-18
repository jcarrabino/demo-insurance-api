package com.api.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.demo.dto.LineDTO;
import com.api.demo.service.AuthorizationService;
import com.api.demo.service.LineService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/lines/")
public class LineController {

	@Autowired
	private LineService lineService;

	@Autowired
	private AuthorizationService authService;

	@PostMapping("/")
	public ResponseEntity<LineDTO> createLine(@Valid @RequestBody LineDTO lineDTO) {
		authService.requireAdmin();
		return new ResponseEntity<>(lineService.create(lineDTO), HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<LineDTO> getLineById(@PathVariable("id") Integer id) {
		return new ResponseEntity<>(lineService.getById(id), HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<List<LineDTO>> getAllLines() {
		return new ResponseEntity<>(lineService.getAll(), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<LineDTO> updateLine(@PathVariable("id") Integer id, @Valid @RequestBody LineDTO lineDTO) {
		authService.requireAdmin();
		return new ResponseEntity<>(lineService.update(id, lineDTO), HttpStatus.OK);
	}

	@PostMapping("/update/{id}")
	public ResponseEntity<LineDTO> partialUpdateLine(@PathVariable("id") Integer id, @RequestBody LineDTO lineDTO) {
		authService.requireAdmin();
		return new ResponseEntity<>(lineService.partialUpdate(id, lineDTO), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteLine(@PathVariable("id") Integer id) {
		authService.requireAdmin();
		return new ResponseEntity<>(lineService.delete(id), HttpStatus.OK);
	}
}
