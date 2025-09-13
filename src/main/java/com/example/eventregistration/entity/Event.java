package com.example.eventregistration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	@FutureOrPresent(message = "Event date should be in Present of Future")
	private LocalDateTime startDateTime;

	private String location;

	@Positive(message = "Capacity should be at least one")
	private Integer capacity;

	private Integer registeredCount;
}
