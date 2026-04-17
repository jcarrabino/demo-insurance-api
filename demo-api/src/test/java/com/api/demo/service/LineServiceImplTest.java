package com.api.demo.service;

import com.api.demo.dto.LineDTO;
import com.api.demo.entity.Line;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.repository.LineRepository;
import com.api.demo.service.impl.LineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LineServiceImplTest {

	@Mock
	LineRepository lineRepository;
	@Mock
	ModelMapper modelMapper;
	@InjectMocks
	LineServiceImpl lineService;

	private Line line;
	private LineDTO lineDTO;

	@BeforeEach
	void setUp() {
		line = new Line();
		line.setId(1);
		line.setName("Auto");
		line.setDescription("Automobile insurance coverage");
		line.setMaxCoverage(500000f);
		line.setMinCoverage(10000f);

		lineDTO = new LineDTO();
		lineDTO.setId(1);
		lineDTO.setName("Auto");
		lineDTO.setDescription("Automobile insurance coverage");
		lineDTO.setMaxCoverage(500000f);
		lineDTO.setMinCoverage(10000f);
	}

	@Test
    void create_savesAndReturnsDTO() {
        when(modelMapper.map(lineDTO, Line.class)).thenReturn(line);
        when(lineRepository.save(line)).thenReturn(line);
        when(modelMapper.map(line, LineDTO.class)).thenReturn(lineDTO);

        LineDTO result = lineService.create(lineDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Auto");
        verify(lineRepository).save(line);
    }

	@Test
    void getById_returnsDTO_whenFound() {
        when(lineRepository.findById(1)).thenReturn(Optional.of(line));
        when(modelMapper.map(line, LineDTO.class)).thenReturn(lineDTO);

        LineDTO result = lineService.getById(1);

        assertThat(result.getName()).isEqualTo("Auto");
    }

	@Test
    void getById_throws_whenNotFound() {
        when(lineRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lineService.getById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

	@Test
    void getAll_returnsList() {
        when(lineRepository.findAll()).thenReturn(List.of(line));
        when(modelMapper.map(line, LineDTO.class)).thenReturn(lineDTO);

        List<LineDTO> result = lineService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMinCoverage()).isEqualTo(10000f);
    }

	@Test
    void update_updatesAndReturnsDTO() {
        when(lineRepository.findById(1)).thenReturn(Optional.of(line));
        when(modelMapper.map(lineDTO, Line.class)).thenReturn(line);
        when(lineRepository.save(line)).thenReturn(line);
        when(modelMapper.map(line, LineDTO.class)).thenReturn(lineDTO);

        LineDTO result = lineService.update(1, lineDTO);

        assertThat(result).isNotNull();
        verify(lineRepository).save(line);
    }

	@Test
    void update_throws_whenNotFound() {
        when(lineRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lineService.update(99, lineDTO))
                .isInstanceOf(ResourceNotFoundException.class);
    }

	@Test
    void delete_deletesAndReturnsMessage() {
        when(lineRepository.findById(1)).thenReturn(Optional.of(line));

        String result = lineService.delete(1);

        verify(lineRepository).delete(line);
        assertThat(result).contains("deleted");
    }

	@Test
    void delete_throws_whenNotFound() {
        when(lineRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lineService.delete(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
