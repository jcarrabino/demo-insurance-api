package com.api.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.demo.dto.LineDTO;
import com.api.demo.entity.Line;
import com.api.demo.exception.ResourceNotFoundException;
import com.api.demo.repository.LineRepository;
import com.api.demo.service.LineService;

@Service
public class LineServiceImpl implements LineService {

	@Autowired
	private LineRepository lineRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public LineDTO create(LineDTO lineDTO) {
		Line line = modelMapper.map(lineDTO, Line.class);
		return modelMapper.map(lineRepository.save(line), LineDTO.class);
	}

	@Override
	public LineDTO getById(Integer id) {
		Line line = lineRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Line", "id", String.valueOf(id)));
		return modelMapper.map(line, LineDTO.class);
	}

	@Override
	public List<LineDTO> getAll() {
		return lineRepository.findAll().stream().map(line -> modelMapper.map(line, LineDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public LineDTO update(Integer id, LineDTO lineDTO) {
		lineRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Line", "id", String.valueOf(id)));
		Line line = modelMapper.map(lineDTO, Line.class);
		line.setId(id);
		return modelMapper.map(lineRepository.save(line), LineDTO.class);
	}

	@Override
	public String delete(Integer id) {
		Line line = lineRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Line", "id", String.valueOf(id)));
		lineRepository.delete(line);
		return "Line deleted successfully";
	}
}
