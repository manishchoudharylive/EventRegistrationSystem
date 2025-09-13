package com.example.eventregistration.controller;

import com.example.eventregistration.dto.LoginRequest;
import com.example.eventregistration.dto.LoginResponse;
import com.example.eventregistration.dto.RegisterRequest;
import com.example.eventregistration.entity.User;
import com.example.eventregistration.service.UserService;
import com.example.eventregistration.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
		User u = userService.register(req.getUsername(), req.getPassword());
		return ResponseEntity.ok(u.getUsername() + " registered successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest req) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
			String token = jwtUtil.generateToken(req.getUsername());
			return ResponseEntity.ok(new LoginResponse(token));
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Invalid credentials");
		}
	}
}
