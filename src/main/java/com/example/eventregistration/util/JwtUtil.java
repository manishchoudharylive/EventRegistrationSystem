package com.example.eventregistration.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
//	@Value("${spring.security.jwt.secret-key}")
	private final SecretKey key;

//	@Value("${spring.security.jwt.expiration}")
	private final long jwtExpiration;

	public JwtUtil(@Value("${spring.security.jwt.secret-key}") String secret,
				   @Value("${spring.security.jwt.expiration}") long expirationMs) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
		this.jwtExpiration = expirationMs;
	}

	public String generateToken(String username) {
		Date now = new Date();
		return Jwts.builder()
				.subject(username)
				.issuedAt(now)
				.expiration(new Date(now.getTime() + jwtExpiration))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public String extractUsername(String token) {
		try {
			Claims claims = Jwts.parser()
					.verifyWith(key)
					.build()
					.parseSignedClaims(token)
					.getPayload();
			return claims.getSubject();
		} catch (JwtException e) {
			return null;
		}
	}
}
