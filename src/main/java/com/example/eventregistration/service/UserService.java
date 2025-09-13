package com.example.eventregistration.service;

import com.example.eventregistration.entity.User;
import com.example.eventregistration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public User register(String username, String rawPassword) {
		if (userRepository.existsByUsername(username)) {
			throw new IllegalArgumentException("username exists");
		}

		User u = User.builder()
				.username(username)
				.password(passwordEncoder.encode(rawPassword))
				.authorities(Set.of("ROLE_USER"))
				.build();
		return userRepository.save(u);
	}
}
