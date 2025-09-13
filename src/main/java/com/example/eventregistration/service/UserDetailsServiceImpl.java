package com.example.eventregistration.service;

import com.example.eventregistration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
				.map(u -> User
						.withUsername(u.getUsername())
						.password(u.getPassword())
						.authorities(u.getAuthorities().stream()
								.map(SimpleGrantedAuthority::new)
								.collect(Collectors.toList()))
						.build())
				.orElseThrow(() -> new UsernameNotFoundException("user not found"));
	}
}
