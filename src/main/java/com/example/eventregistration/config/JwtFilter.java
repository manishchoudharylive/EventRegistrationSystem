package com.example.eventregistration.config;

import com.example.eventregistration.util.JwtUtil;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
									@NonNull FilterChain filterChain) throws ServletException, IOException {
		String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (auth != null && auth.startsWith("Bearer ")) {
			String token = auth.substring(7);
			String username = jwtUtil.extractUsername(token);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				var userDetails = userDetailsService.loadUserByUsername(username);
				if (userDetails != null) {
					var authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
																			userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		}
		filterChain.doFilter(request, response);
	}
}
