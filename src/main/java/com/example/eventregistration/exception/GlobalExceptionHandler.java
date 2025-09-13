package com.example.eventregistration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<?> handleIllegalState(IllegalStateException ex) {
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex) {
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleOther(Exception ex) {
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}
