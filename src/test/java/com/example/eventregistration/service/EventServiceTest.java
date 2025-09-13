package com.example.eventregistration.service;

import com.example.eventregistration.dto.EventDto;
import com.example.eventregistration.entity.Event;
import com.example.eventregistration.repository.EventRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

	@Test
	void createAndFindAll() {
		EventRepository repo = mock(EventRepository.class);
		EventService svc = new EventService(repo);

		Event event = Event.builder()
				.id(1L)
				.name("Test Event")
				.startDateTime(LocalDateTime.now())
				.location("Bangalore")
				.capacity(100)
				.registeredCount(0)
				.build();

		when(repo.save(any(Event.class))).thenReturn(event);
		when(repo.findAll()).thenReturn(List.of(event));

		EventDto dto = new EventDto();
		dto.setName("Test Event");
		dto.setStartDateTime(LocalDateTime.now());
		dto.setLocation("Bangalore");
		dto.setCapacity(100);

		EventDto created = svc.create(dto);
		assertNotNull(created.getId());
		assertEquals("Test Event", created.getName());

		List<EventDto> all = svc.findAll();
		assertEquals(1, all.size());
	}

	@Test
	void findById() {
		EventRepository repo = mock(EventRepository.class);
		EventService svc = new EventService(repo);

		Event event = Event.builder().id(2L).name("Another Event").startDateTime(LocalDateTime.now()).build();
		when(repo.findById(2L)).thenReturn(Optional.of(event));

		Optional<EventDto> result = svc.findById(2L);
		assertTrue(result.isPresent());
		assertEquals("Another Event", result.get().getName());
	}
}
