package com.example.eventregistration.controller;

import com.example.eventregistration.dto.EventDto;
import com.example.eventregistration.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
	private final EventService eventService;

	@GetMapping
	public ResponseEntity<List<EventDto>> getAll() {
		return ResponseEntity.ok(eventService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EventDto> get(@PathVariable Long id) {
		return eventService.findById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<EventDto> create(@RequestBody EventDto dto) {
		return ResponseEntity.ok(eventService.create(dto));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<EventDto> update(@PathVariable Long id, @RequestBody EventDto dto) {
		return ResponseEntity.ok(eventService.update(id, dto));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		eventService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/register")
	public ResponseEntity<EventDto> registerUser(@PathVariable Long id) {
		return ResponseEntity.ok(eventService.registerUser(id));
	}
}
