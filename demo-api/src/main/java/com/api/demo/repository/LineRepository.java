package com.api.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.demo.entity.Line;

public interface LineRepository extends JpaRepository<Line, Integer> {
}
