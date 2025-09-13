package com.example.eventregistration.repository;

import com.example.eventregistration.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
