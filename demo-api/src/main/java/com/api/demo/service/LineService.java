package com.api.demo.service;

import java.util.List;

import com.api.demo.dto.LineDTO;

public interface LineService {

	LineDTO create(LineDTO lineDTO);

	LineDTO getById(Integer id);

	List<LineDTO> getAll();

	LineDTO update(Integer id, LineDTO lineDTO);

	LineDTO partialUpdate(Integer id, LineDTO lineDTO);

	String delete(Integer id);
}
