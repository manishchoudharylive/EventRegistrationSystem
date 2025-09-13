package com.example.eventregistration.controller;

import com.example.eventregistration.dto.EventDto;
import com.example.eventregistration.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
class EventControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private EventService eventService;

	@Test
	@WithMockUser(username = "testuser")
	void getAllEvents() throws Exception {
		EventDto dto = new EventDto();
		dto.setId(1L);
		dto.setName("Sample Event");
		dto.setStartDateTime(LocalDateTime.now());

		Mockito.when(eventService.findAll()).thenReturn(List.of(dto));

		mockMvc.perform(get("/api/events"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Sample Event"));
	}

	@Test
	@WithMockUser(username = "testuser")
	void createEvent() throws Exception {
		EventDto dto = new EventDto();
		dto.setId(2L);
		dto.setName("Created Event");
		dto.setStartDateTime(LocalDateTime.now());

		Mockito.when(eventService.create(any(EventDto.class))).thenReturn(dto);

		mockMvc.perform(post("/api/events")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Created Event"));
	}

	@Test
	@WithMockUser(username = "testuser")
	void getEventById() throws Exception {
		EventDto dto = new EventDto();
		dto.setId(3L);
		dto.setName("Event By Id");
		dto.setStartDateTime(LocalDateTime.now());

		Mockito.when(eventService.findById(3L)).thenReturn(Optional.of(dto));

		mockMvc.perform(get("/api/events/3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(3))
				.andExpect(jsonPath("$.name").value("Event By Id"));
	}

	@Test
	@WithMockUser(username = "testuser")
	void updateEvent() throws Exception {
		EventDto dto = new EventDto();
		dto.setId(4L);
		dto.setName("Updated Event");
		dto.setStartDateTime(LocalDateTime.now());

		Mockito.when(eventService.update(eq(4L), any(EventDto.class))).thenReturn(dto);

		mockMvc.perform(put("/api/events/4")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(4))
				.andExpect(jsonPath("$.name").value("Updated Event"));
	}

	@Test
	@WithMockUser(username = "testuser")
	void deleteEvent() throws Exception {
		mockMvc.perform(delete("/api/events/5"))
				.andExpect(status().isNoContent());
		Mockito.verify(eventService).delete(5L);
	}

	@Test
	@WithMockUser(username = "testuser")
	void registerUserForEvent() throws Exception {
		EventDto dto = new EventDto();
		dto.setId(6L);
		dto.setName("Register Event");
		dto.setRegisteredCount(1);

		Mockito.when(eventService.registerUser(6L)).thenReturn(dto);

		mockMvc.perform(post("/api/events/6/register"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(6))
				.andExpect(jsonPath("$.registeredCount").value(1));
	}
}
