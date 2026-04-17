package com.api.demo.controller;

import com.api.demo.dto.LineDTO;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.service.AuthorizationService;
import com.api.demo.service.LineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LineControllerTest {

	@Mock
	LineService lineService;
	@Mock
	AuthorizationService authService;
	@InjectMocks
	LineController lineController;

	private LineDTO lineDTO;

	@BeforeEach
	void setUp() {
		lineDTO = new LineDTO();
		lineDTO.setId(1);
		lineDTO.setName("Auto");
		lineDTO.setDescription("Automobile insurance coverage");
		lineDTO.setMaxCoverage(500000f);
		lineDTO.setMinCoverage(10000f);
	}

	@Test
    void createLine_returns201() {
        when(lineService.create(lineDTO)).thenReturn(lineDTO);

        ResponseEntity<LineDTO> response = lineController.createLine(lineDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getName()).isEqualTo("Auto");
    }

	@Test
    void getLineById_returns200() {
        when(lineService.getById(1)).thenReturn(lineDTO);

        ResponseEntity<LineDTO> response = lineController.getLineById(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMaxCoverage()).isEqualTo(500000f);
    }

	@Test
    void getLineById_throws_whenNotFound() {
        when(lineService.getById(99)).thenThrow(new ResourceNotFoundException("Line", "id", "99"));

        assertThatThrownBy(() -> lineController.getLineById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

	@Test
    void getAllLines_returns200() {
        when(lineService.getAll()).thenReturn(List.of(lineDTO));

        ResponseEntity<List<LineDTO>> response = lineController.getAllLines();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

	@Test
    void updateLine_returns200() {
        when(lineService.update(1, lineDTO)).thenReturn(lineDTO);

        ResponseEntity<LineDTO> response = lineController.updateLine(1, lineDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getDescription()).isEqualTo("Automobile insurance coverage");
    }

	@Test
    void deleteLine_returns200() {
        when(lineService.delete(1)).thenReturn("Line deleted successfully");

        ResponseEntity<String> response = lineController.deleteLine(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("deleted");
    }
}
