package com.api.demo.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {

	private String Massege;
	private boolean status;
	private LocalDateTime DateAndTime;

}
