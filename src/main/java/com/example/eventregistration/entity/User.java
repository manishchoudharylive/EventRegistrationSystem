package com.example.eventregistration.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String username;

	@Column(nullable = false)
	private String password; // bcrypt encoded

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "authority")
	private Set<String> authorities;
}
