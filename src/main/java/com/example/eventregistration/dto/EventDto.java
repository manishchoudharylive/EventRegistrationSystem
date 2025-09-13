package com.example.eventregistration.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDto {
	private Long id;
	private String name;
	private LocalDateTime startDateTime;
	private String location;
	private Integer capacity;
	private Integer registeredCount;
}
