package com.example.eventregistration.controller;

import com.example.eventregistration.dto.LoginRequest;
import com.example.eventregistration.dto.RegisterRequest;
import com.example.eventregistration.entity.User;
import com.example.eventregistration.service.UserService;
import com.example.eventregistration.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private UserService userService;

	@MockitoBean
	private AuthenticationManager authenticationManager;

	@MockitoBean
	private JwtUtil jwtUtil;

	@Test
	void registerUser() throws Exception {
		RegisterRequest req = new RegisterRequest();
		req.setUsername("newuser");
		req.setPassword("password");

		User saved = User.builder().id(1L).username("newuser").password("hashed").authorities(Set.of("ROLE_USER")).build();
		when(userService.register("newuser", "password")).thenReturn(saved);

		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(content().string("newuser registered successfully"));
	}

	@Test
	void loginUser() throws Exception {
		LoginRequest req = new LoginRequest();
		req.setUsername("testuser");
		req.setPassword("password");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
		when(jwtUtil.generateToken("testuser")).thenReturn("fake-jwt-token");

		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").value("fake-jwt-token"));
	}
}
