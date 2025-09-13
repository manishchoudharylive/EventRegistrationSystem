package com.example.eventregistration.service;

import com.example.eventregistration.dto.EventDto;
import com.example.eventregistration.entity.Event;
import com.example.eventregistration.repository.EventRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {
	private final EventRepository eventRepository;

	public EventDto create(EventDto dto) {
		checkRegisteredCount(dto);

		Event e = Event.builder()
				.name(dto.getName())
				.startDateTime(dto.getStartDateTime())
				.location(dto.getLocation())
				.capacity(dto.getCapacity())
				.registeredCount(dto.getRegisteredCount()) // Pre-booked
				.build();
		Event saved = eventRepository.save(e);
		dto.setId(saved.getId());
		return dto;
	}

	@Transactional(readOnly = true)
	public List<EventDto> findAll() {
		return eventRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Optional<EventDto> findById(Long id) {
		return eventRepository.findById(id).map(this::toDto);
	}

	public EventDto update(Long id, EventDto dto) {
		Event e = eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found"));

		checkRegisteredCount(dto);

		e.setName(dto.getName());
		e.setStartDateTime(dto.getStartDateTime());
		e.setLocation(dto.getLocation());
		e.setCapacity(dto.getCapacity());
		e.setRegisteredCount(dto.getRegisteredCount() == null ? 0 : dto.getRegisteredCount());

		Event saved = eventRepository.save(e);
		return toDto(saved);
	}

	public void delete(Long id) {
		if (!eventRepository.existsById(id))
			throw new IllegalArgumentException("Event not found");

		eventRepository.deleteById(id);
	}

	public EventDto registerUser(Long eventId) {
		Event e = eventRepository.findById(eventId)
				.orElseThrow(() -> new IllegalArgumentException("Event not found"));
		if (e.getCapacity() != null && e.getRegisteredCount() >= e.getCapacity()) {
			throw new IllegalStateException("Event is full");
		}
		e.setRegisteredCount(e.getRegisteredCount() + 1);
		Event saved = eventRepository.save(e);
		return toDto(saved);
	}

	private EventDto toDto(Event e) {
		EventDto d = new EventDto();
		d.setId(e.getId());
		d.setName(e.getName());
		d.setStartDateTime(e.getStartDateTime());
		d.setLocation(e.getLocation());
		d.setCapacity(e.getCapacity());
		d.setRegisteredCount(e.getRegisteredCount());
		return d;
	}
	Predicate<EventDto> checkRegCount = (dto) -> dto.getRegisteredCount() != null &&
					dto.getRegisteredCount() > dto.getCapacity();
	private void checkRegisteredCount(EventDto dto) {
	if (checkRegCount.test(dto))
		throw new IllegalArgumentException("Registered count exceeds capacity");
	}
}
